package com.redpill_linpro.response3.security

import com.redpill_linpro.response3.content.ResponseLogo

class ResponseClient {

    String name
    String displayName
    String description
    ResponseLogo logo
    Boolean isEnabled
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name unique: true, size:2..32, matches:"^[a-z0-9\\-]+"
        displayName unique: true, size:2..64
        description blank:true, nullable:true
        isEnabled nullable:false
        logo nullable:true
    }

    static mapping = {
        table 'response_client'
        id generator: 'sequence', params: [sequence: 'response_client_seq']
        cache usage:'read-write'
        version false
        id index:'response_client_id_idx'
        name index:'response_client_name_idx'
    }
}
