package com.redpill_linpro.response3.security

import com.redpill_linpro.response3.content.Partner
import com.redpill_linpro.response3.search.ESSearch
import com.redpill_linpro.response3.search.ElasticSearchIndex
import com.redpill_linpro.response3.search.PartnerSearch
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class ResponseClientController {
    def grailsApplication

    def index() {
        //new ElasticSearchIndex()
        render ""
    }

    def list() {
        new PartnerSearch(request.currentClient).filterCustomers(Partner.get(1))
        render ""
    }
}
