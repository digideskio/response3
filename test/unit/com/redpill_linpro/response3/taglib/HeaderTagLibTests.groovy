package com.redpill_linpro.response3.taglib

import com.redpill_linpro.response3.content.ResponseHeader
import com.redpill_linpro.response3.security.ResponseClient
import grails.test.mixin.*

/**

 */
@TestFor(HeaderTagLib)
@Mock([ResponseClient, ResponseHeader])
class HeaderTagLibTests {

    void setUp(){
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
        client1.save(flush:true)
    }

    void testLogo() {
        request.currentClient = ResponseClient.findByName("redpill-linpro")
        assert applyTemplate('<header:logo/>') ==
                "<div class=\"main_logo\" style=\"background:url" +
                "('/response3/images/clients/redpill_logo.png') no-repeat " +
                "20px center;width:100px;\"></div>"
    }

    void testH1(){
        request.currentClient = ResponseClient.findByName("redpill-linpro")
        assert applyTemplate('<header:h2/>') ==
                "<h2 style=\"color:#333;\">Response Issue Tracker</h2>"
    }

    void testH2(){
        request.currentClient = ResponseClient.findByName("redpill-linpro")
        assert applyTemplate('<header:h2/>') ==
                "<h1 style=\"line-height:50px;color:#000;\">Redpill-Linpro</h1>"
    }
}
