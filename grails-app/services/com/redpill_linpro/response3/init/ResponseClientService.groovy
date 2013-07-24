package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.content.ResponseHeader
import com.redpill_linpro.response3.security.ResponseClient

class ResponseClientService {

    static transactional = true

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
                description: "",
                backgroundColor: "#fff",
                filename: "klogo.gif",
                logoImageWidth: "280px",
                titleLineHeight: "70px"
        )
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
                description: "",
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
