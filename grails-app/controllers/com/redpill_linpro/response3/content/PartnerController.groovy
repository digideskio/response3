package com.redpill_linpro.response3.content

class PartnerController {
    
    def lockService
    
    def list(){
        log.debug params
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
        if(partner){
            return [
                instance:partner
            ]
        }
        else{
            flash.message = message(code:'partner.not.found', args:[params.id])
            redirect(action:'list')
        }
    }
    
    def edit(){
        log.debug params
        try{
            String className = "com.redpill_linpro.response3.content.Project"
            def partner = lockService.lock(className,Long.parseLong(params.id))
            if(partner){
                return [
                    instance:partner
                ]
            } else {
                throw new RuntimeException("Partnerservice returned null")
            }
        } catch(RuntimeException e){
            log.error(e.getMessage())
            flash.errorMessage = message(
                code:'could.not.lock.partner', args:[params.id])
            redirect(action:'show', id:params.id)
        }
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
    
    def update(){
        def partner = Partner.get(params.id)
    }
    
    def delete() {}
}
