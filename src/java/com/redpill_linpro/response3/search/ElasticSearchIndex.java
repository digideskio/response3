package com.redpill_linpro.response3.search;

import java.io.IOException;
import java.util.*;

import com.redpill_linpro.response3.security.ResponseClient;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.*;
import grails.util.Holders;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import static org.elasticsearch.common.xcontent.XContentFactory.*;


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

    private Object only;
    private Client esClient;

    // Find all domain classes, search for the searchable property
    public ElasticSearchIndex(){
        this.esClient = EmbeddedElasticSearch.getClient();
    }
    public void buildMapping(){
        List<GrailsDomainClass> superMappings = new ArrayList<>();
        GrailsApplication app = Holders.getGrailsApplication();

        GrailsClass[] domains = app.getArtefacts("Domain");
        for(GrailsClass domain : domains){
            Class<?> currentClass = domain.getClazz();
            GrailsDomainClass superDomainClass = (GrailsDomainClass)
                    app.getArtefact(
                            DomainClassArtefactHandler.TYPE,
                            currentClass.getName());
            superMappings.add(superDomainClass);
            log.debug(
                    "Found domain class: " +
                            superDomainClass.getClazz().getCanonicalName());
        }
        for(GrailsDomainClass domainClass: superMappings){
            only = null;
            if (domainClass.hasProperty(SEARCHABLE_PROPERTY_NAME)){
                Object searchable =
                        domainClass.getPropertyValue(
                                SEARCHABLE_PROPERTY_NAME);
                if (searchable instanceof Boolean) {
                    log.debug("Searchable is Boolean");
                    break;
                } else if (searchable instanceof java.util.LinkedHashMap) {
                    log.debug("Searchable is LinkedHashMap");
                    buildHashMapMapping((LinkedHashMap)searchable);
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
                createSchema(domainClass, propsOnly);
            }
        }
    }
    public void createIndexes(List<ResponseClient> clients)
            throws IOException {
        for(ResponseClient client : clients){
            DeleteIndexResponse delete = esClient.admin().indices().delete(
                    new DeleteIndexRequest(client.getName())).actionGet();
            if (!delete.isAcknowledged()) {
                log.error("Index wasn't deleted");
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

            XContentBuilder mapping = XContentFactory.jsonBuilder()
            .startObject()
                .startObject("partner")
                    .startObject("_source")
                        .field("compress", "true")
                    .endObject()
                    .startObject("_all")
                        .field("enabled", "false")
                    .endObject()
                    .startObject("properties")
                        .startObject("name")
                            .field("type", "string")
                            .field("store", "yes")
                            .field("index", "analyzed")
                            .field("null_value", "")
                        .endObject()
                    .endObject()
                .endObject()
            .endObject();

            esClient.admin().indices().prepareCreate(client.getName())
                    .setSettings(settings)
                    .addMapping("partner", mapping)
                    .execute()
                    .actionGet();
            // Flush Index
            esClient.admin().indices().flush(
                    new FlushRequest(client.getName())
                            .refresh(true)).actionGet();
        }
    }

    public void createIndex(ResponseClient client){

    }

    private void createSchema(
            GrailsDomainClass domainClass, Set<String> propsOnly) {
        //
        log.debug(domainClass.getFullName());
        //client.admin().indices().prepareCreate(indexName)
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
