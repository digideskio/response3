package com.redpill_linpro.response3.content

class PartnerController {

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
