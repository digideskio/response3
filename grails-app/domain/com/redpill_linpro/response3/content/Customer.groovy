
package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.User

class Customer {
    Partner partner
    String name
    String description
    Date dateCreated
    Date lastUpdated
    boolean deactivated = false
    
    static hasMany = [
        projects:Project,
        clients: User,
        contactPersons: User
    ]
    
    static constraints = {
        partner(nullable:false)
        name(blank: false, nullable:false)
        description(nullable: true)
        clients(nullable: true)
        contactPersons(nullable: true)
        deactivated(validator: {
                val, obj ->
                if(val){
                    // Check Project
                    if(Project.executeQuery(
                        "select 1 from Project p WHERE p.customer = :c AND p.deactivated is false",
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
        description type: 'text'
        cache usage:'read-write'

        //Indexes
        name index:'Customer_name_idx'
        projects index:'Customer_projects_idx'
        clients index:'Customer_clients_idx'
        contactPersons index:'Customer_contactPersons_idx'
        dateCreated index:'Customer_dateCreated_idx'
        partner index:'Customer_partner_idx'
    }
    
    String toString() {
        return this.name
    }
}

