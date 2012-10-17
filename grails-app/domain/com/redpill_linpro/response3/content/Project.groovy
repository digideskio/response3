package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.User

class Project {
    
    Customer customer

    String title
    String description
    
    Date dateCreated
    Date lastUpdated
    boolean deactivated = false
    
    static hasMany = [
        issues:Issue,
        contactPersons: User,
        projectManagers: User,
        clients: User
    ]

    static constraints = {
        
        customer(nullable:false)
        deactivated(validator: {
                val, obj ->
                if(val){
                    // Check Issue
                    if(Issue.executeQuery(
                        "select 1 from Issue i WHERE i.project = :p AND i.status.id NOT IN (0)",
                        [p:obj]
                    ).size() > 0){
                        return false;
                    }
                }
                return true
            }
        )
        contactPersons(nullable:true)
        title(blank: false, nullable:false)
        projectManagers(nullable:true,blank: true)
        description(nullable:true,blank: true)
        clients(nullable: true)
    }
    
    static mapping ={
        table 'project'
        version false
        id generator: 'sequence', params: [sequence: 'project_seq']
        description type: 'text'

        //Indexes
        id index:'project_id_idx'
        contactPersons index:'project_contact_persons_idx'
        dateCreated index:'project_date_created_idx'
        projectManagers index:'project_project_managers_idx'
        issues index:'project_issues_idx'
        clients index:'project_clients_idx'
        customer index:'project_customer_idx'
        deactivated index:'project_deactivated_idx'
        lastUpdated index:'project_last_opdated_idx'

        cache usage:'read-write'
    }
    
    String toString() {
        return this.title
    }
}
