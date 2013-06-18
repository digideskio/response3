package com.redpill_linpro.response3.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;


/**
 *
 * User: olav
 * Date: 6/5/13
 * Time: 5:11 PM
 *
 */
public class EmbeddedElasticSearch {

    private static Node node;
    private static Client client;

    public EmbeddedElasticSearch(){
        ImmutableSettings.Builder settings =
                ImmutableSettings.settingsBuilder();
        settings.put("node.name", "response3-dev-node");
        settings.put("path.data", "/tmp/r3data/index");
        settings.put("http.enabled", true);
        settings.put("http.port", 9201);
        node = NodeBuilder.nodeBuilder()
                .settings(settings)
                .clusterName("response3-cluster")
                .data(true).local(true).node();
        client = node.client();
    }

    public static Client getClient(){
        return client;
    }

    public void stop(){
        node.close();
    }
}