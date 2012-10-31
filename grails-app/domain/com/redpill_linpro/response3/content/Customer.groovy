
package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Lock

class Customer {
    Partner partner
    String name
    String description
    Date dateCreated
    Date lastUpdated
    Lock lockdata
    boolean enabled = true
    
    static hasMany = [
        projects:Project,
        clients: User,
        contactPersons: User
    ]
    
    static constraints = {
        partner(nullable:false)
        lockdata(nullable:true)
        name(blank: false, nullable:false,unique:true)
        description(nullable: true,size:0..4000)
        clients(nullable: true)
        contactPersons(nullable: true)
        enabled(validator: {
                val, obj ->
                if(val == false){
                    // Check Project
                    if(Project.executeQuery(
                        "select 1 from Project p WHERE p.customer = :c AND p.enabled is true",
                        [c:obj]
                    ).size() > 0){
                        return false;
                    }
                }
                return true
            }
        )
    }
    
    static mapping = {
        table "customer"
        version false
        id generator: 'sequence', params: [sequence: 'customer_seq']
        cache usage:'read-write'

        //Indexes
        id index:'customer_id_idx'
        name index:'customer_name_idx'
        projects index:'customer_projects_idx'
        clients index:'customer_clients_idx'
        contactPersons index:'customer_contact_persons_idx'
        dateCreated index:'customer_date_created_idx'
        partner index:'customer_partner_idx'
        enabled index:'customer_enabled_idx'
    }
    
    String toString() {
        return this.name
    }
}

