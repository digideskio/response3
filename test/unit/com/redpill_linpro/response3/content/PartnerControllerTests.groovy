package com.redpill_linpro.response3.content

import grails.test.mixin.*
import org.junit.*

import com.redpill_linpro.response3.security.LockService
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PartnerController)
@Mock([Partner])
class PartnerControllerTests {
    
    def lockService = [
        lock:{ String cname, long id ->
            return Partner.get(id)
        },
        unlock:{ String cname, long id ->
            return Partner.get(id)
        }
    ]
    
    void setUp(){
        controller.lockService = lockService
        new Partner(name:'Redpill').save(flush:true)
        new Partner(name:'OSE').save(flush:true)
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
        
        assert response.redirectedUrl == "/partner/show/$params.id"
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
    
    void testSaveValidBook() { 
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
}
