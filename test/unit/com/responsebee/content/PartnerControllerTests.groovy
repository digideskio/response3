package com.responsebee.content

import grails.test.mixin.*
import grails.converters.JSON

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} 
 * for usage instructions
 */
@TestFor(PartnerController)
@Mock([Partner,Customer])
class PartnerControllerTests {
    
    def lockService = [
        lock:{ String cname, long id ->
            return Partner.get(id)
        },
        unlock:{ String cname, long id ->
            return Partner.get(id)
        },
        update:{ String cname, Map params ->
            def p = Partner.get(params.id)
            p.properties = params
            p.save()
            return p
        }
    ]
    
    void setUp(){
        controller.lockService = lockService
        Customer.metaClass.static.search = { a, b ->
            return [results:Customer.list()]
        }
        def rp = new Partner(name:'Redpill').save(flush:true)
        rp.metaClass.getContactPersonsAsMap{ return [] }
        rp.metaClass.getClientsAsMap{ return [] }
        rp.metaClass.getCustomersAsMap{ Map a=[:] -> 
            return Customer.list(a) as Set
        }
        rp.metaClass.getCustomerCount{ return Customer.count()}
        new Partner(name:'OSE').save(flush:true)
        def c1 = new Customer(partner:rp, name:'Telenor').save(flush:true)
        def c2 = new Customer(partner:rp, name:'Oslo Stock Exchange')
            .save(flush:true)
    }
    void testList() {
        def model = controller.list()
        assert model.instances != null
        assert model.total == 2
    }
    void testCreate() {
        def model = controller.create()
        assert model.instance != null
    }
    void testInvalidShow() {
        params.id = 10
        controller.show()
        
        assert response.redirectedUrl == '/partner/list'
        assert flash.message != null
    }
    void testValidShow() {
        params.id = 2
        def model = controller.show()
        
        assert model.instance != null 
    }
    
    void testInvalidEdit() {
        params.id = 10
        controller.edit()
        assert response.redirectedUrl == "/partner/list"
        assert flash.errorMessage != null
    }
    void testValidEdit() {
        params.id = 1
        def model = controller.edit()
        assert response.redirectedUrl == null
        assert model.instance != null
        assert model.instance.name == "Redpill"
    }
    void testInvalidCancel() {
        params.id = 10
        def model = controller.cancel()
        assert response.redirectedUrl == "/partner/list"
        assert flash.errorMessage != null
    }
    void testCancel() {
        params.id = 1
        def model = controller.cancel()
        assert response.redirectedUrl == "/partner/show/$params.id"
        assert flash.message != null
    }

    void testSaveInvalidPartner() {
       controller.save()
       assert model.instance != null 
       assert view == '/partner/create'
    }
    void testSaveInvalidUniquePartner() {
        params.name = "Redpill"
        controller.save()
        assert !model.instance.validate()
        assert view == '/partner/create'
     }
    
    void testSaveValidPartner() { 
        params.name = "Redpill-Linpro" 
        params.description = "500"
        
        controller.save()
        
        assert response.redirectedUrl == '/partner/show/3'
        assert flash.message != null
        assert Partner.count() == 3
    }
    void testCheckId(){
        assert controller.checkId("1") == 1L
        assert controller.checkId(1) == 1L
        assert controller.checkId(1L) == 1L
        assert controller.checkId(null) == null
    }
    void testEmptyUpdate(){
        controller.update()
        assert flash.message != null
        assert response.redirectedUrl == '/partner/list'
    }
    void testInvalidUpdate(){
        params.id = 10
        controller.update()
        assert flash.errorMessage != null
        assert response.redirectedUrl == "/partner/list"
    }
    void testUpdate(){
        params.id = 1
        params.description = "truls"
        
        controller.update()
        
        assert response.redirectedUrl == "/partner/show/$params.id"
        assert flash.message != null
        def partner = Partner.get(params.id)
        assert partner.description == "truls"
    }
    
    void testInvalidCustomerList(){
        params.id = 10
        def model = controller.customers()
        assert response.redirectedUrl == "/partner/list"
        println flash.message
        assert flash.message == "partner.not.found"
    }
    
    void testCustomerList(){
        params.id = 1
        def model = controller.customers()
        assertNotNull model.instance
        assertNotNull model.instances
        assert model.total == 2
    }
    
    void testMoreCustomers(){
        params.id = 1
        controller.moreCustomers()
        def json = JSON.parse(controller.response.contentAsString)
        assertNotNull json
        assert controller.response.status == 200
    }
    
    void testFilterCustomers(){
        params.id = 1
        params.query = 'te'
        controller.filterCustomers()
        def json = JSON.parse(controller.response.contentAsString)
        assertNotNull json
        assert controller.response.status == 200
    }
}
