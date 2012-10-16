package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.User

class Partner {
    String name
    String description
    
    Date dateCreated
    Date lastUpdated
    boolean deactivated = false
    
    static hasMany = [
        customers:Customer,
        clients:User,
        contactPersons:User
    ]
    
    static constraints = {
        customers(nullable:true)
        name(blank: false, nullable:false, unique:true)
        description(nullable: true)
        clients(nullable: true)
        contactPersons(nullable: true)
        deactivated(validator: {
            val, obj ->
            if(val){
                if(Customer.executeQuery(
                    "select 1 from Customer c WHERE c.partner = :p AND c.deactivated is false",
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
        description type: 'text'
        //Indexes
        name index:'partner_name_idx'
        customers index:'partner_customers_idx'
        clients index:'partner_clients_idx'
        contactPersons index:'partner_contactPersons_idx'
        dateCreated index:'partner_dateCreated_idx'
        cache usage:'read-write'
    }
    
    String toString() {
        return this.name
    }
}
