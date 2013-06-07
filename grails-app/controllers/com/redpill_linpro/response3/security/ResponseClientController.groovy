package com.redpill_linpro.response3.security

import com.redpill_linpro.response3.search.ElasticSearchIndex

class ResponseClientController {
    def grailsApplication

    def index() {
        new ElasticSearchIndex()
        render ""
    }
}
