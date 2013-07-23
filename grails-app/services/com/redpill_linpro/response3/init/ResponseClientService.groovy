package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.content.ResponseLogo
import com.redpill_linpro.response3.security.ResponseClient

class ResponseClientService {

    static transactional = true

    def init() {
        def responseLogo1 = new ResponseLogo(
                title:"Redpill-Linpro",
                description: "Response3 Issue Tracker",
                backgroundColor: "#fff",
                logoImagePath: "images/redpill_logo.png",
                logoImageWidth: "80px"
        )
        responseLogo1.save(flush: true)
        new ResponseClient(
                name: "redpill-linpro",
                displayName: "Redpill Linpro",
                description: "Nordic leader in open source software solutions",
                isEnabled:true,
                logo:responseLogo1
        ).save(flush:true)

        def responseLogo2 = new ResponseLogo(
                title:"",
                description: "Response3 Issue Tracker",
                backgroundColor: "#fff",
                logoImagePath: "images/clients/klogo.gif",
                logoImageWidth: "280px"
        )
        responseLogo2.save(flush: true)
        new ResponseClient(
                name: "komplett-group",
                displayName: "Komplett Group",
                description: "Komplett Group is a leading player in e-commerce",
                isEnabled:true,
                logo: responseLogo2
        ).save(flush:true)

        def responseLogo3 = new ResponseLogo(
                title:"",
                description: "",
                backgroundColor: "#333",
                logoImagePath: "images/clients/statoillogo.jpg",
                logoImageWidth: "120px"
        )
        responseLogo3.save(flush: true)
        new ResponseClient(
                name: "statoil",
                displayName: "Statoil",
                description: "Statoil is an international energy company",
                isEnabled:true,
                logo: responseLogo3
        ).save(flush:true)
    }
}
