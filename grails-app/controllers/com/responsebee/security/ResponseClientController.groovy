package com.responsebee.security

import com.responsebee.content.Partner
import com.responsebee.search.PartnerSearch

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
