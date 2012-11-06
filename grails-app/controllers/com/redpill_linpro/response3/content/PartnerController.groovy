package com.redpill_linpro.response3.content

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN','ROLE_MANAGER'])
class PartnerController {
    private final String CN = "com.redpill_linpro.response3.content.Partner"
    
    def lockService
    
    def list(){
        log.debug params
        params.max = Math.min(
            params.max ? params.int('max') : 
            grailsApplication.config.response3.lists.length, 
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
            flash.message = message(code:'partner.not.found',args:[params.id])
            redirect(action:'list')
        }
    }
    
    def edit(){
        log.debug params
        try{
            long partnerId = checkId(params.id)
            def partner = lockService.lock(CN, partnerId)
            if(partner){
                return [
                    instance:partner
                ]
            } else {
                throw new RuntimeException("lockservice returned null")
            }
        } catch(RuntimeException e){
            log.error(e.getMessage())
            flash.errorMessage = message(
                code:'could.not.lock.partner', args:[params.id])
            redirect(action:'list')
        }
    }
    def cancel(){
        log.debug params
        try{
            long partnerId = checkId(params.id)
            def partner = lockService.unlock(CN,partnerId)
            if(partner){
                flash.message = 
                    message(code:'unlocked.partner', args:[params.id])
                redirect(action:'show', id:params.id)
            } else {
                throw new RuntimeException("lockservice returned null")
            }
        } catch(RuntimeException e){
            log.error(e.getMessage())
            flash.errorMessage = message(code:'could.not.unlock.partner')
            redirect(action:'list')
        }
    }
    
    def save() { 
        log.debug params
        def partner = new Partner(params)
        if(partner.validate() && partner.save(flush:true)){
            flash.message = message(code:'partner.created',args:[partner.name])
            redirect (action:'show', id:partner.id)
        }
        else{
            render(view: "create", model: [instance: partner])
        }
    }
    
    def update(){
        log.debug params
        if(params.id == null){
            flash.message = message(code:'no.partner.found')
            redirect(action:'list')
            return
        }
        try{
            def partner = lockService.update(CN, params)
            if(partner){
                flash.message = message(
                    code:'partner.updated',args:[partner.name])
                redirect (action:'show', id:partner.id)
            }
            else{
                throw new RuntimeException(
                    "lockservice for update returned null")
            }
        } catch(RuntimeException e){
            log.error(e.getMessage())
            flash.errorMessage = message(
                code:'could.not.lock.partner', args:[params.id])
            redirect(action:'list')
        }
    }
    
    def delete() {}
    
    def customers(){
        def partner = Partner.read(params.id)
        if(partner){
            params.max = Math.min(
            params.max ? params.int('max') : 
                grailsApplication.config.response3.lists.length,
                grailsApplication.config.response3.lists.max)
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
                [id:params.long('id')],
                [max:params.long('max'),offset:params.long('offset')]
            )
            def total = Customer.executeQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.partner.id = :id",
                [id:params.long('id')]
            )[0]
            return [
                instance:partner,
                instances:customers,
                total:total
            ]
        }
        else{
            flash.message = message(code:'partner.not.found',args:[params.id])
            redirect(action:'list')
        }
    }
    
    def moreCustomers(){
        def data = []
        def partner = Partner.read(params.id)
        if(partner){
            params.max = Math.min(
            params.max ? params.int('max') :
                grailsApplication.config.response3.lists.length,
                grailsApplication.config.response3.lists.max)
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
            data = Customer.executeQuery(
                sql,
                [id:params.long('id')],
                [max:params.long('max'),offset:params.long('offset')]
            )
        }
        render data as JSON
    }
    
    def filterCustomers(){
        def data = [:]
        if(params.query && params.long('id')){
            def wildcardQuery = params.query + "*"
            def searchOptions = [
                max:grailsApplication.config.response3.lists.length]
            Closure searchClosure = {
                must(term('$/Customer/partner/id', params.long('id')))
                must(queryString(wildcardQuery))
            }
            data = Customer.search(searchClosure, searchOptions)
            data = data.results.collect{it.properties['id','name']}
        }
        render data as JSON
    }
    
    def users(){
        
    }
    
    private def checkId(id){
        if(id instanceof String){
            return Long.parseLong(id)
        }
        else{
            try{
                return Long.valueOf(id)
            } catch (NumberFormatException e){
                return null
            }
        }
    }
}
