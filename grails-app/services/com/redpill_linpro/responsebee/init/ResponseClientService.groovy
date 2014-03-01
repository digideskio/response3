package com.redpill_linpro.responsebee.init

import com.responsebee.content.ResponseHeader
import com.responsebee.security.ResponseClient
import grails.transaction.Transactional

@Transactional
class ResponseClientService {

    def init() {
        def responseHeader1 = new ResponseHeader(
                title:"Redpill-Linpro",
                titleColor: "#000",
                description: "Response Issue Tracker",
                backgroundColor: "#fff",
                filename: "redpill_logo.png",
                logoImageWidth: "100px",
                titleLineHeight: "50px"
        )
        responseHeader1.save(flush: true)
        def client1 = new ResponseClient(
                name: "redpill-linpro",
                displayName: "Redpill Linpro",
                description: "Nordic leader in open source software solutions",
                isEnabled:true,
                header:responseHeader1
        )
        client1.validate()
        client1.errors.allErrors.each {log.error it}
        client1.save(flush:true)

        def responseHeader2 = new ResponseHeader(
                title:"Support Center",
                titleColor: "#1F1949",
                description: "Komplett support",
                backgroundColor: "#fff",
                filename: "klogo.gif",
                logoImageWidth: "280px",
                titleLineHeight: "70px"
        )
        log.debug(responseHeader2.validate())
        responseHeader2.errors.allErrors.each {log.error it}
        responseHeader2.save(flush: true)
        new ResponseClient(
                name: "komplett-group",
                displayName: "Komplett Group",
                description: "Komplett Group is a leading player in e-commerce",
                isEnabled:true,
                header: responseHeader2
        ).save(flush:true)

        def responseHeader3 = new ResponseHeader(
                title:"Support System",
                titleColor: "#dadada",
                description: "Statoil support",
                backgroundColor: "#333",
                filename: "statoillogo.jpg",
                logoImageWidth: "180px",
                titleLineHeight: "90px"
        )
        responseHeader3.save(flush: true)
        new ResponseClient(
                name: "statoil",
                displayName: "Statoil",
                description: "Statoil is an international energy company",
                isEnabled:true,
                header: responseHeader3
        ).save(flush:true)
    }
}
