package com.redpill_linpro.response3.security

import org.elasticsearch.common.xcontent.XContentFactory

class ResponseClient {

    static searchable = [
        only: ['id','displayName'],
        mapping: XContentFactory.jsonBuilder()
        .startObject()
            .startObject(this.class.simpleName.toLowerCase())
                .startObject("_source")
                    .field("compress", "true")
                .endObject()
                .startObject("_all")
                    .field("enabled", "false")
                .endObject()
                .startObject("properties")
                    .startObject("id")
                        .field("type", "integer")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                    .startObject("displayName")
                        .field("type", "string")
                        .field("store", "yes")
                        .field("index", "analyzed")
                        .field("null_value", "")
                    .endObject()
                .endObject()
            .endObject()
        .endObject()
    ]

    String name
    String displayName
    String description
    Boolean isEnabled
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name unique: true, size:2..32, matches:"^[a-z0-9\\-]+"
        displayName unique: true, size:2..64
        description blank:true, nullable:true
        isEnabled nullable:false
    }

    static mapping = {
        table 'response3_client'
        id generator: 'sequence', params: [sequence: 'client_seq']
        cache usage:'read-write'
        version false
        id index:'client_id_idx'
        name index:'client_name_idx'
    }
}
