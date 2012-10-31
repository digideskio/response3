package com.redpill_linpro.response3.content



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} 
 * for usage instructions
 */
@TestFor(Partner)
class PartnerTests {

    void testConstraints() {
       def existingPartner = new Partner(name:"Redpill-Linpro")
       mockForConstraintsTests(Partner, [existingPartner])
       
       def partner = new Partner()
       
       assert !partner.validate() 
       assert "nullable" == partner.errors["name"] 
       
       partner = new Partner(name:"Redpill-Linpro") 
       assert !partner.validate() 
       assert "unique" == partner.errors["name"] 
       
       // Validation should pass! 
       partner = new Partner(name:"Oslo Stock Exchange") 
       assert partner.validate()
    }
    
    void testSpecialChars(){
        def partner = new Partner(name:"Ålbærg Øl")
        assert partner.validate()
        assert partner.save()
        
        def partner2 = new Partner(name:"-ナルト-")
        assert partner2.validate()
        assert partner2.save()
        
        def partner3 = new Partner(name:"ناروتو")
        assert partner3.validate()
        assert partner3.save()
    }
}
