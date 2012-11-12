package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Lock

class Partner {
    
    static searchable = {
        only: ['name']
        clients component: true
    }
    
    String name
    String description
    Lock lockdata
    
    Date dateCreated
    Date lastUpdated
    boolean enabled = true
    
    static hasMany = [
        customers:Customer,
        clients:User,
        contactPersons:User
    ]
    
    static constraints = {
        customers(nullable:true)
        lockdata(nullable:true)
        name(blank: false, nullable:false, unique:true,size:1..60)
        description(nullable: true,size:0..4000)
        clients(nullable: true)
        contactPersons(nullable: true)
        enabled(validator: {
            val, obj ->
            if(val == false){
                if(Customer.executeQuery(
                    "select 1 from Customer c WHERE c.partner = :p AND "+
                    "c.enabled is true",
                    [p:obj]
                ).size() > 0){
                    return false;
                }
                return true
            }
        })
    }
    
    static mapping = {
        table 'partner'
        version false
        id generator: 'sequence', params: [sequence: 'partner_seq']
        lockdata lazy: false
        //Indexes
        id index:'partner_id_idx'
        name index:'partner_name_idx'
        customers index:'partner_customers_idx'
        clients index:'partner_clients_idx'
        contactPersons index:'partner_contact_persons_idx'
        dateCreated index:'partner_date_created_idx'
        cache usage:'read-write'
    }
    
    String toString() {
        return this.name
    }
}
