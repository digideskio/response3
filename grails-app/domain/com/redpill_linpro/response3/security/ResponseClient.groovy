package com.redpill_linpro.response3.security

class ResponseClient {

    static searchable = [
        only: ['id','displayName']
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
