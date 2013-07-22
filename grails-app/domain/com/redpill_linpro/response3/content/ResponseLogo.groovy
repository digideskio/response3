package com.redpill_linpro.response3.content

class ResponseLogo {

    String title
    String backgroundColor
    String path

    static constraints = {
        title unique: true, size:2..32, matches:"^[a-z0-9\\-]+"
        backgroundColor nullable:false, blank:false
        path nullable:false, blank:false
    }

    static mapping = {
        table 'response_logo'
        id generator: 'sequence', params: [sequence: 'response_logo_seq']
        cache usage:'read-write'
        version false
        id index:'response_logo_id_idx'
    }
}
