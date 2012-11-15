package com.redpill_linpro.response3.content

import java.util.Set;

import com.redpill_linpro.response3.security.Role;
import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Lock

class Partner {
    
    static transients = [
        'getClients',
        'getContactPersons',
        'getCustomers'
    ]
    
    static searchable = {
        only: ['name']
    }
    
    String name
    String description
    Lock lockdata
    
    Date dateCreated
    Date lastUpdated
    boolean enabled = true
    
    static constraints = {
        lockdata(nullable:true)
        name(blank: false, nullable:false, unique:true,size:1..60)
        description(nullable: true,size:0..4000)
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
        dateCreated index:'partner_date_created_idx'
        cache usage:'read-write'
    }
    
    Set<User> getClients(String sort = 'name', String order = 'asc'){
        String sql = """
            SELECT c FROM PartnerClients p JOIN p.client c
            WHERE p.partner.id = :id ORDER BY c.$sort $order
        """.stripMargin()
        return PartnerClients.executeQuery(
            sql, [id:this.id]
        )
    }
    
    Set<User> getContactPersons(String sort = 'name', String order = 'asc'){
        String sql = """
            SELECT c FROM PartnerContactPersons p JOIN p.contactPerson c
            WHERE p.partner.id = :id ORDER BY c.$sort $order
        """.stripMargin()
        return PartnerContactPersons.executeQuery(
            sql, [id:this.id]
        )
    }
    
    String toString() {
        return this.name
    }
}
