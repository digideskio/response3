package com.redpill_linpro.response3.security

import com.redpill_linpro.response3.search.ESSearch
import com.redpill_linpro.response3.search.ElasticSearchIndex
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class ResponseClientController {
    def grailsApplication

    def index() {
        //new ElasticSearchIndex()
        render ""
    }

    def list() {
        new ESSearch(request.currentClient).query()
        render ""
    }
}
