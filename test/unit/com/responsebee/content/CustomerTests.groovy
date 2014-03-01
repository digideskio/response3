package com.responsebee.content



import grails.test.mixin.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Customer)
@Mock([Partner])
class CustomerTests {
    
    void setUp(){
        new Partner(name:'Redpill').save(flush:true)
    }

    void testConstraints() {
       def existingcustomer = new Customer(
           name:"Telenor",
           partner:Partner.findByName('Redpill')
       )
       mockForConstraintsTests(Customer, [existingcustomer])
       
       def customer = new Customer(partner:Partner.findByName('Redpill'))
       
       assert !customer.validate() 
       assert "nullable" == customer.errors["name"]
       
       customer = new Customer(name:'Statoil')
       
       assert !customer.validate()
       assert "nullable" == customer.errors["partner"]
       
       customer = new Customer(
           name:"Telenor",
           partner:Partner.findByName('Redpill')
       ) 
       assert !customer.validate() 
       assert "unique" == customer.errors["name"] 
       
       // Validation should pass! 
       customer = new Customer(
           name:"Oslo Stock Exchange",
           partner:Partner.findByName('Redpill')
       ) 
       assert customer.validate()
    }
    
    void testSpecialChars(){
        def customer = new Customer(
            name:"Ålbærg Øl",
            partner:Partner.findByName('Redpill')
        )
        assert customer.validate()
        assert customer.save()
        
        def customer2 = new Customer(
            name:"-ナルト-",
            partner:Partner.findByName('Redpill')
        )
        assert customer2.validate()
        assert customer2.save()
        
        def customer3 = new Customer(
            name:"ناروتو",
            partner:Partner.findByName('Redpill')
        )
        assert customer3.validate()
        assert customer3.save()
    }
}
