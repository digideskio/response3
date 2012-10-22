package com.redpill_linpro.response3.content

class PartnerController {
    
    def grailsApplication
    
    def list(){
        params.max = Math.min(
            params.max ? params.int('max') : 10, 
            grailsApplication.config.response3.lists.max)
        params.sort = params.sort?:"dateCreated"
        params.order = params.order?:"desc"
        def props = grailsApplication.getDomainClass(
            "com.redpill_linpro.response3.content.Partner"
        ).persistentProperties.collect{ it.name }
        def partners = Partner.list(params)
        return [
            instances: partners,
            props: props,
            total: Partner.count()
        ]
    }

    def create() { 
        def partner = new Partner()
        return [
            instance:partner
        ]
    }
    
    def show() { 
        def partner = Partner.read(params.id)
    }
    
    def save() { 
        log.debug params
        def partner = new Partner(params)
        if(partner.validate() && partner.save(flush:true)){
            flash.message = message(code:'partner.created', args:[partner.name])
            redirect (action:'show', id:partner.id)
        }
        else{
            render(view: "create", model: [instance: partner])
        }
    }
    
    def delete() {}
}
