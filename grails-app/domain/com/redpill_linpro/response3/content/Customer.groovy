
package com.redpill_linpro.response3.content

import com.redpill_linpro.response3.security.ResponseClient
import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Lock
import org.elasticsearch.common.xcontent.XContentFactory

class Customer {

    static searchable = [
        only: ['id', 'partner', 'name','dateCreated','lastUpdated','enabled'],
        mapping: XContentFactory.jsonBuilder()
        .startObject()
            .startObject(this.class.simpleName.toLowerCase())
                .startObject("_source")
                    .field("compress", "true")
                .endObject()
                .startObject("_all")
                    .field("enabled", "false")
                .endObject()
                .startObject("properties")
                    .startObject("id")
                        .field("type", "integer")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                    .startObject("partner")
                        .field("type", "integer")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                    .startObject("name")
                        .field("type", "string")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                        .field("null_value", "")
                    .endObject()
                    .startObject("displayName")
                        .field("type", "string")
                        .field("store", "yes")
                        .field("index", "analyzed")
                        .field("null_value", "")
                    .endObject()
                    .startObject("dateCreated")
                        .field("type", "date")
                        .field("format", "yyyy-MM-dd HH:mm:ss")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                    .startObject("lastUpdated")
                        .field("type", "date")
                        .field("format", "yyyy-MM-dd HH:mm:ss")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                    .startObject("enabled")
                        .field("type", "boolean")
                        .field("store", "yes")
                        .field("index", "not_analyzed")
                    .endObject()
                .endObject()
            .endObject()
        .endObject()
    ]

    ResponseClient responseClient
    Partner partner
    String name
    String displayName
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
        responseClient nullable: false
        partner(nullable:false)
        lockdata(nullable:true)
        name(unique:true)
        displayName(blank: false, nullable:false,unique:true)
        description(nullable: true,size:0..4000)
        clients(nullable: true)
        contactPersons(nullable: true)
        enabled(validator: {
                val, obj ->
                if(!val){
                    // Check Project
                    if(executeQuery(
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

    def beforeInsert(){
        this.name = this.displayName.toLowerCase()
    }

    def beforeUpdate() {
        if (isDirty('displayName')) {
            this.name = this.displayName.toLowerCase()
        }
    }
    
    String toString() {
        return this.name
    }
    
    static def listAll(Map params=null){
        if(!params){
            return []
        }
        String sql = """
            SELECT c FROM Customer c 
            ORDER BY c.$params.sort $params.order
        """.stripMargin()
        return executeQuery(sql,
            [max:params.max,offset:params.offset])
    }
}

