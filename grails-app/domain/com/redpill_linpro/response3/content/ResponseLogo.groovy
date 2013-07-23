package com.redpill_linpro.response3.content

class ResponseLogo {

    String title
    String backgroundColor
    String logoImagePath
    String logoImageWidth
    String description

    static constraints = {
        title blank:true, size:1..32
        description blank: true
        backgroundColor nullable:false, blank:false
        logoImagePath nullable:false, blank:false
        logoImageWidth nullable:false, blank:false
    }

    static mapping = {
        table 'response_logo'
        id generator: 'sequence', params: [sequence: 'response_logo_seq']
        cache usage:'read-write'
        version false
        id index:'response_logo_id_idx'
    }
}
