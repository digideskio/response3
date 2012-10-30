package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.content.*

class ContentService {

    def init() {
        createPartners()
        createCustomers()
        createProjects()
        createIssues()
    }
    
    def createPartners(){
        new Partner(name:'Redpill').save(flush:true)
        new Partner(name:'Mulesoft').save(flush:true)
        new Partner(name:'Springsource').save(flush:true)
        new Partner(name:'Varnish').save(flush:true)
        assert Partner.count() == 4
    }
    
    def createCustomers(){
        new Customer(
            partner:Partner.findByName('Redpill'),
            name:'Stavanger Kommune').save(flush:true)
        new Customer(
            partner:Partner.findByName('Redpill'),
            name:'Telenor').save(flush:true)
        new Customer(
            partner:Partner.findByName('Redpill'),
            name:'Oslo Stock Exchange').save(flush:true)
        assert Customer.count() == 3
    }
    def createProjects(){
        
    }
    def createIssues(){
        
    }
}
