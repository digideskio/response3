package com.redpill_linpro.response3.search;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.redpill_linpro.response3.security.ResponseClient;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.*;
import grails.util.Holders;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;

import org.elasticsearch.action.admin.indices.settings.UpdateSettingsRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexMissingException;
import org.hibernate.*;


/**
 * Code inspired by https://github.com/mstein/elasticsearch-grails-plugin/
 * blob/master/src/java/org/grails/plugins/elasticsearch/mapping/
 * SearchableDomainClassMapper.java
 *
 * User: olav
 * Date: 6/6/13
 * Time: 10:59 AM
 */

public class ElasticSearchIndex extends GroovyObjectSupport {

    private Logger log = Logger.getLogger(this.getClass());
    private static final String SEARCHABLE_PROPERTY_NAME = "searchable";
    private static final String SEARCHABLE_MAPPING_NAME = "mapping";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Object only;
    private Client esClient;
    private List<ResponseClient> clients;
    private SessionFactory sessionFactory =
            (SessionFactory) Holders.getApplicationContext()
                    .getBean("sessionFactory");

    private List<GrailsDomainClass> superMappings = new ArrayList<>();

    // Find all domain classes, search for the searchable property
    public ElasticSearchIndex(List<ResponseClient> clients){
        this.esClient = EmbeddedElasticSearch.getClient();
        this.clients = clients;
    }
    public void buildMapping() throws IOException,
            IllegalAccessException, InvocationTargetException{

        GrailsApplication app = Holders.getGrailsApplication();
        GrailsClass[] domains = app.getArtefacts("Domain");
        for(GrailsClass domain : domains){
            Class<?> currentClass = domain.getClazz();
            GrailsDomainClass superDomainClass = (GrailsDomainClass)
                    app.getArtefact(
                            DomainClassArtefactHandler.TYPE,
                            currentClass.getName());
            this.superMappings.add(superDomainClass);
            log.debug(
                    "Found domain class: " +
                            superDomainClass.getClazz().getCanonicalName());
        }
        for(ResponseClient client : this.clients){
            createIndex(client);
        }
        for(GrailsDomainClass domainClass: this.superMappings){
            only = null;
            if (domainClass.hasProperty(SEARCHABLE_PROPERTY_NAME)){
                XContentBuilder mapping = null;
                Object searchable =
                        domainClass.getPropertyValue(
                                SEARCHABLE_PROPERTY_NAME);
                if (searchable instanceof Boolean) {
                    log.debug("Searchable is Boolean");
                    break;
                } else if (searchable instanceof LinkedHashMap) {
                    log.debug("Searchable is LinkedHashMap");
                    buildHashMapMapping((LinkedHashMap)searchable);
                    mapping = getHashMapMapping((LinkedHashMap)searchable);
                } else if (searchable instanceof Closure) {
                    log.debug("Searchable is Closure");
                    buildClosureMapping((Closure)searchable);
                } else {
                    log.debug("Searchable is of unknown type.");
                    throw new IllegalArgumentException(
                            "'searchable' property has unknown type: " +
                                    searchable.getClass());
                }
                Set<String> propsOnly = convertToSet(only);
                log.debug(propsOnly);
                for(ResponseClient client : this.clients){
                    String type = domainClass.getClazz()
                            .getSimpleName()
                            .toLowerCase();
                    if(mapping != null){
                        createMapping(type, mapping, client.getName());
                        fetchContent(domainClass, propsOnly, client);
                        enableRealTimeIndexing(client.getName());
                        optimizeIndex(client.getName());
                        flushIndex(client.getName());
                    }
                }
            }
        }
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private void fetchContent(
            GrailsDomainClass domainClass, Set<String> props,
            ResponseClient client)
    throws IllegalAccessException, InvocationTargetException, IOException {
        String propertyType = domainClass.getClazz().getSimpleName();
        long clientId = 0;
        for(Method m : client.getClass().getDeclaredMethods()){
            String methodName = m.getName();
            if(methodName.equals("getId")){
                clientId = (long)m.invoke(client);
            }
        }
        String sql =
                "SELECT t FROM "
                + propertyType
                +" as t"
                +" WHERE t.responseClient.id = " + clientId;
        StatelessSession statelessSession =
                this.sessionFactory.openStatelessSession();
        ScrollableResults results = statelessSession.createQuery(sql)
                .setReadOnly(true)
                .setFetchSize(1000)
                .setCacheable(false)
                .scroll(ScrollMode.FORWARD_ONLY);
        List<Object> objects = new ArrayList<>();
        long count = 0;
        while(results.next()){
            objects.add(results.get(0));
            if(++count > 0 && count % 1000 == 0){
                indexFetchedObject(objects, props, client, propertyType);
                objects.clear();
            }
        }
        if(objects.size() > 0){
            indexFetchedObject(objects, props, client, propertyType);
        }
        statelessSession.close();
    }

    private void indexFetchedObject(
            List<Object> results, Set<String> props, ResponseClient client,
            String propertyType)
    throws IllegalAccessException, InvocationTargetException, IOException {
        List<Map<String, Object>> items = new ArrayList<>();
        for(Object obj : results){
            Map<String, Object> properties = new HashMap<>();
            for(Method m : obj.getClass().getDeclaredMethods()){
                String methodName = m.getName();
                for(String p : props){
                    char[] stringArray = p.toCharArray();
                    stringArray[0] = Character.toUpperCase(stringArray[0]);
                    String uppedP = new String(stringArray);
                    Object value = null;
                    if(methodName.equals("get"+uppedP)){
                        Class<?> returnType = m.getReturnType();
                        if(returnType.equals(Long.class)){
                            value = m.invoke(obj);
                        }
                        else if(returnType.equals(String.class)){
                            value = m.invoke(obj);
                        }
                        else if(returnType.equals(Date.class)){
                            value = DATE_FORMAT.format(
                                    (Date) m.invoke(obj));
                        }
                        else if(returnType.equals(boolean.class)){
                            value = m.invoke(obj);
                        }
                        else if(returnType.equals(Integer.class)){
                            //noinspection RedundantCast
                            value = (Long)m.invoke(obj);
                        }
                        else if(returnType.equals(Boolean.class)){
                            value = m.invoke(obj);
                        }
                        else{
                            value = findValueInSuperMappings(
                                    m.invoke(obj), returnType);
                        }
                    }
                    if(value != null){
                        properties.put(p, value);
                    }
                }
            }
            items.add(properties);
        }
        indexContent(client.getName(), propertyType.toLowerCase(), items);
    }

    private Object findValueInSuperMappings(Object obj, Class<?> returnType)
            throws IllegalAccessException, InvocationTargetException {
        for(GrailsDomainClass clazz : this.superMappings){
            if(returnType.equals(clazz.getClazz())){
                for(Method m : obj.getClass().getDeclaredMethods()){
                    String methodName = m.getName();
                    if(methodName.equals("getId")){
                        return m.invoke(obj);
                    }
                }
            }
        }
        return null;
    }

    private void indexContent(String idxName, String propertyName,
              List<Map<String, Object>> items) throws IOException{
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();
        for(Map<String, Object> document : items){
            XContentBuilder obj = XContentFactory.jsonBuilder();
            XContentBuilder fields = obj.startObject();
            String id = "";
            Set<String> keys = document.keySet();
            for(String key :keys){
                if(key.equals("id")){
                    id = String.valueOf(document.get(key));
                } else {
                    fields.field(key, document.get(key));
                }
            }
            obj.endObject();
            //log.debug(obj.prettyPrint().string());
            bulkRequest.add(esClient.prepareIndex(idxName, propertyName, id)
                    .setSource(obj)
            );
        }

        log.debug("Bulk items: " + bulkRequest.numberOfActions());
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            log.error("Bulk insert failed!");
        }
    }

    public void createIndex(ResponseClient client){
        try{
            try{
                DeleteIndexResponse delete = esClient.admin().indices().delete(
                        new DeleteIndexRequest(client.getName())).actionGet();
                if (!delete.isAcknowledged()) {
                    log.error("Index wasn't deleted");
                }
            } catch (IndexMissingException e){
                log.error(e.getMessage());
            }
            XContentBuilder settings = XContentFactory.jsonBuilder()
            .startObject()
                .startObject("index")
                    .field("refresh_interval", "60s")
                    .field("number_of_shards", 1)
                    .field("number_of_replicas", 0)
                    .startObject("store")
                        .field("type", "niofs")
                        .startObject("compress")
                            .field("stored", true)
                            .field("tv", true)
                        .endObject()
                    .endObject()
                    .startObject("gateway")
                        .field("type", "none")
                    .endObject()
                .endObject()
            .endObject();

            CreateIndexResponse req =
                esClient.admin().indices().prepareCreate(client.getName())
                    .setSettings(settings)
                    .execute()
                    .actionGet();
            if(!req.isAcknowledged()){
                log.error(client.getName() +" index not created!");
            }
            flushIndex(client.getName());
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }
    private void enableRealTimeIndexing(String idxName){
        Map<String,String> settingsMap = new HashMap<>();
        settingsMap.put("refresh_interval", "1s");
        Settings settings = ImmutableSettings
                .settingsBuilder()
                .put(settingsMap)
                .build();
        UpdateSettingsRequestBuilder request = esClient.admin().indices()
                .prepareUpdateSettings();
        request.setIndices(idxName)
                .setSettings(settings)
                .execute()
                .actionGet();
    }
    private void optimizeIndex(String idxName){
        esClient.admin().indices().prepareOptimize(idxName)
                .setFlush(true)
                .setOnlyExpungeDeletes(true)
                .setWaitForMerge(true)
                .execute()
                .actionGet();
    }

    private void flushIndex(String idxName){
        esClient.admin().indices().flush(
                new FlushRequest(idxName)
                        .refresh(true)).actionGet();
    }

    private void createMapping(
            String type, XContentBuilder mapping, String idxName) {
        PutMappingResponse req =
                esClient.admin().indices().preparePutMapping(idxName)
                .setType(type)
                .setSource(mapping)
                .execute()
                .actionGet();
        if(!req.isAcknowledged()){
            log.error("Error with mapping for: " + type);
        }
    }

    public void setOnly(Object only) {
        this.only = only;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void doMapping(Set<String> mappableProperties){
        for (String propertyName : mappableProperties) {
            log.debug(propertyName);
        }
    }

    private Set<String> convertToSet(Object arg) {
        if (arg == null) {
            log.debug("Arg is null!");
            return Collections.emptySet();
        } else if (arg instanceof String) {
            log.debug("Arg is String!");
            return Collections.singleton((String) arg);
        } else if (arg instanceof Object[]) {
            log.debug("Arg is Object[]!");
            return new HashSet<>(Arrays.asList((String[]) arg));
        } else if (arg instanceof Collection) {
            log.debug("Arg is Collection!");
            //noinspection unchecked
            return new HashSet<>((Collection<String>) arg);
        } else {
            log.debug("Error: arg is unknown!");
            throw new IllegalArgumentException("Unknown argument: " + arg);
        }
    }
    public void buildHashMapMapping(LinkedHashMap map) {
        only = map.containsKey("only") ? map.get("only") : null;
    }
    public XContentBuilder getHashMapMapping(LinkedHashMap map){
        return map.containsKey(SEARCHABLE_MAPPING_NAME) ?
                (XContentBuilder)map.get(SEARCHABLE_MAPPING_NAME) : null;
    }

    public void buildClosureMapping(Closure searchable) {
        assert searchable != null;
        Closure closure = (Closure) searchable.clone();
        closure.setDelegate(this);
        closure.call();
    }

    @SuppressWarnings("UnusedDeclaration")
    private Set<String> getInheritedProperties(GrailsDomainClass domainClass) {
        // check which properties belong to this domain class ONLY
        Set<String> inheritedProperties = new HashSet<>();
        for (GrailsDomainClassProperty prop :
                domainClass.getPersistentProperties()) {
            if (GrailsClassUtils.isPropertyInherited(
                    domainClass.getClazz(), prop.getName())) {
                inheritedProperties.add(prop.getName());
            }
        }
        log.debug(inheritedProperties);
        return inheritedProperties;
    }
}
