package com.responsebee.search;

import com.responsebee.content.Partner;
import com.responsebee.security.ResponseClient;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: olav
 * Date: 7/24/13
 * Time: 4:22 PM
 */
public class PartnerSearch extends ESSearch {

    private Logger log = Logger.getLogger(this.getClass());

    public PartnerSearch(ResponseClient client){
        super(client);
    }
    public List<String> searchCustomers(){
        List<String> list = new ArrayList<>();
        return list;
    }

    public List<String> filterCustomers(Partner partner){
        List<String> list = new ArrayList<>();
        QueryBuilder qb = QueryBuilders.matchQuery(
                "customer.partner",
                String.valueOf(partner.getDomainId())
        );
        SearchResponse response =
                this.esClient.prepareSearch(this.client.getName())
                        .setTypes("customer")
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setQuery(qb)
                        .setQuery(QueryBuilders.wildcardQuery(
                                "displayName", "ak*"))
                        .setFrom(0).setSize(this.size)
                        .addSort("name", SortOrder.DESC)
                        .execute()
                        .actionGet();
        SearchHits hits = response.getHits();
        for(SearchHit hit : hits){
            log.debug(hit.getSource());
        }
        return list;
    }

    private SearchRequestBuilder prepareSearch(){
        return this.esClient.prepareSearch(this.client.getName())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFrom(0).setSize(this.size)
                .addSort("name", SortOrder.DESC);
    }
}
