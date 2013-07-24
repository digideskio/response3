package com.redpill_linpro.response3.search;

import com.redpill_linpro.response3.security.ResponseClient;
import grails.util.Holders;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;

/**
 * User: olav
 * Date: 7/19/13
 * Time: 3:02 PM
 */
public class ESSearch {

    private Logger log = Logger.getLogger(this.getClass());
    protected final Client esClient;
    protected final ResponseClient client;
    protected final int size;

    public ESSearch(ResponseClient client){
        this.esClient = EmbeddedElasticSearch.getClient();
        this.client = client;
        GrailsApplication app = Holders.getGrailsApplication();
        Map<String, Object> config = app.getFlatConfig();
        this.size = (int)config.get("response3.lists.length");
    }

    public void query(){
        GrailsApplication app = Holders.getGrailsApplication();
        Map<String, Object> config = app.getFlatConfig();
        int size = (int)config.get("response3.lists.length");

        QueryBuilder qb = QueryBuilders.matchQuery(
                "customer.partner",
                "1"
        );
        SearchResponse response =
                this.esClient.prepareSearch(this.client.getName())
                .setTypes("customer")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)             // Query
                //.setFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(size)
                .addSort("name", SortOrder.DESC)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        for(SearchHit hit : hits){
            log.debug(hit.getSource());
        }

    }
}
