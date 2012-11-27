package com.redpill_linpro.response3.content

import java.util.Set;
import java.util.HashSet;
import org.hibernate.collection.PersistentSet
import org.codehaus.groovy.grails.web.binding.ListOrderedSet
import grails.util.Holders

import com.redpill_linpro.response3.security.Role;
import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Lock

class Partner {
    
    static hasMany = [
        clients:User,
        contactPersons:User,
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
        clients(nullable:true)
        contactPersons(nullable:true,validator: { val, obj ->
            if (val == null){
                return true
            }
            if (val instanceof PersistentSet || 
                val instanceof ListOrderedSet ||
                val instanceof HashSet){
                val.each{
                    if(!obj.clients.contains(it)){
                        return false
                    }
                }
            }
            else if (!obj.clients.contains(val)){
                return false;
            }
            return true
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
    
    Set getClientsAsMap(String sort = 'name', String order = 'asc'){
        String sql = """
            SELECT NEW MAP(c.id ,c.username, c.name) 
            FROM Partner p JOIN p.clients c
            WHERE p.id = :id ORDER BY c.$sort $order
        """.stripMargin()
        return Partner.executeQuery(
            sql, [id:this.id]
        )
    }
    
    Set getContactPersonsAsMap(
        String sort = 'name', String order = 'asc')
    {
        String sql = """
            SELECT NEW MAP(c.id ,c.username, c.name) 
            FROM Partner p JOIN p.contactPersons c
            WHERE p.id = :id ORDER BY c.$sort $order
        """.stripMargin()
        return Partner.executeQuery(
            sql, [id:this.id]
        )
    }
    
    Set getCustomersAsMap(Map params=[:]){
        println this.name
        params.max = Math.min(
        params.max ? params.int('max') :
            Holders.getGrailsApplication().config.response3.lists.length,
            Holders.getGrailsApplication().config.response3.lists.max)
        params.offset = params.offset ? params.long('offset') : 0
        params.sort =
            params.sort in ['id','name'] ? params.sort:'name'
        params.order =
            params.order in ['asc','desc'] ? params.order:'asc'
        String sql = """
            SELECT NEW MAP(c.id as id, c.name as name) FROM Customer c
            WHERE c.partner.id = :id
            ORDER BY c.$params.sort $params.order
        """.stripMargin()
        def customers = Customer.executeQuery(
            sql,
            [id:this.id],
            [max:params.long('max'),offset:params.long('offset')]
        )
        return customers as Set
    }
    
    long getCustomerCount(){
        if(this.id){
            return (long)Customer.executeQuery(
                """SELECT COUNT(c) FROM Customer c WHERE c.partner.id = :id""",
                [id:this.id]
            )[0]
        }
        return 0L
    }
    
    String toString() {
        return this.name
    }
}
