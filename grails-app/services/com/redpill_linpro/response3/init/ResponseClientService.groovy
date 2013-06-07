package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.security.ResponseClient

class ResponseClientService {

    static transactional = true

    def init() {
        new ResponseClient(
                name: "redpill-linpro",
                displayName: "Redpill Linpro",
                description: "Nordic leader in open source software solutions",
                isEnabled:true
        ).save(flush:true)
        new ResponseClient(
                name: "komplett-group",
                displayName: "Komplett Group",
                description: "Komplett Group is a leading player in e-commerce",
                isEnabled:true
        ).save(flush:true)
        new ResponseClient(
                name: "statoil",
                displayName: "Statoil",
                description: "Statoil is an international energy company",
                isEnabled:true
        ).save(flush:true)
    }
}
