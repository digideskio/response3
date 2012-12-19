package com.redpill_linpro.response3.content

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import com.redpill_linpro.response3.security.User

@Secured(['ROLE_ADMIN','ROLE_MANAGER'])
class CustomerController {
    
    private final String PNAME = this.getClass().getPackage().getName();
    private final String CN = PNAME + ".Customer"
    
    def lockService
    def searchableService
    
    def list(){
        log.debug params
        params.max = Math.min(
            params.max ? params.int('max') : listLength(), listMax())
        params.offset = params.offset ? params.long('offset') : 0
        def sortAttributes = [
            'id','name','partner.name','dateCreated','lastUpdated']
        params.sort =
            params.sort in sortAttributes ? params.sort:'name'
        params.order = params.order?:"asc"
        def customers = Customer.listAll(params)
        return [
            instances: customers,
            total: Customer.count()
        ]
    }
    def next(){
        params.offset = params.long('offset') + listLength()
        long total = Customer.count()
        if(params.offset > (total - listLength())){
            params.offset = params.offset - (params.offset % listLength())
        }
        redirect(action:'list', params: newParams(params))
    }
    def previous(){
        params.offset = params.long('offset') > 0l ? 
            0l : (params.long('offset') - listLength())
        redirect(action:'list', params: newParams(params))
    }
    private long listLength(){
        return grailsApplication.config.response3.lists.length
    }
    private long listMax(){ 
        return grailsApplication.config.response3.lists.max
    }
    private def newParams(Map params){
        return [
            sort: params.sort, order: params.order,
            max: params.max, offset: params.offset
        ]
    }

    def create() { }
    
    def show() { }
    
    def save() { }
    
    def destroy() {}
}
