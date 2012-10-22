
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
        description(nullable: true,size:0..4000)
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
        cache usage:'read-write'

        //Indexes
        id index:'customer_id_idx'
        name index:'customer_name_idx'
        projects index:'customer_projects_idx'
        clients index:'customer_clients_idx'
        contactPersons index:'customer_contact_persons_idx'
        dateCreated index:'customer_date_created_idx'
        partner index:'customer_partner_idx'
    }
    
    String toString() {
        return this.name
    }
}

