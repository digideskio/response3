package com.redpill_linpro.response3.content

import groovy.transform.CompileStatic

class ResponseHeader {

    String title
    String titleColor
    String backgroundColor
    String filename
    String logoImageWidth
    String titleLineHeight
    String description
    String descriptionColor

    static constraints = {
        title blank:true, size:1..32
        titleColor nullable: true, blank:false
        description blank: true
        descriptionColor nullable: true, blank:false
        backgroundColor nullable:true, blank:false
        filename nullable:false, blank:false
        logoImageWidth nullable:false, blank:false
        titleLineHeight nullable: true, blank:false
    }

    static mapping = {
        table 'response_header'
        id generator: 'sequence', params: [sequence: 'response_header_seq']
        cache usage:'read-write'
        version false
        id index:'response_header_id_idx'
    }

    def beforeInsert(){
        ifEmpty()
    }

    def beforeUpdate(){
        ifEmpty()
    }

    @CompileStatic
    public void ifEmpty(){
        if(this.titleColor == null){
            this.titleColor = "#000"
        }
        if(this.backgroundColor == null){
            this.backgroundColor = "#fff"
        }
        if(this.descriptionColor == null){
            this.descriptionColor = "#333"
        }
    }
}
