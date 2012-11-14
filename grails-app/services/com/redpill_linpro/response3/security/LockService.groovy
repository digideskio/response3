package com.redpill_linpro.response3.security

import org.hibernate.FetchMode
import org.hibernate.LockMode

class LockService {
    
    static transactional = true
    
    def grailsApplication
    def userService
    
    def lock(String className, long id) {
        def currentUser = userService.getCurrentUser()
        log.debug("Currentuser: $currentUser")
        if(!currentUser){
            String msg = "Couldn't find current user"
            log.error(msg)
            throw new RuntimeException(msg)
        }
        def domainClass = grailsApplication.getClassForName(className)
        def instance = domainClass.lock(id)
        if(instance.hasProperty('lockdata') && instance.lockdata == null){
            def lock = new Lock(lockedBy:currentUser)
            lock.save()
            instance.lockdata = lock 
            if(instance.validate() && instance.save(flush:true)){
                return instance
            }
            else{
                String msg = "Couldn't lock $className id:$instance.id"
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
        else{
            if(instance.lockdata.lockedBy == currentUser || 
                currentUser.isAdmin()){
                return instance
            }
            else{
                String msg = instance.lockdata.toString()
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
    }
    
    def unlock(String className, long id){
        def currentUser = userService.getCurrentUser()
        log.debug("Currentuser: $currentUser")
        def domainClass = grailsApplication.getClassForName(className)
        def instance = domainClass.lock(id)
        if(instance.hasProperty('lockdata') && 
            (instance.lockdata.lockedBy == currentUser || 
             currentUser.isAdmin())){
            instance.lockdata.delete()
            instance.lockdata = null
            if(instance.validate() && instance.save(flush:true)){
                return instance
            }
            else{
                String msg = "Couldn't unlock $className id:$instance.id"
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
        else{
            String msg = "No lock found"
            log.error(msg)
            throw new RuntimeException(msg)
        }
    }
    
    def update(String className, Map params){
        def currentUser = userService.getCurrentUser()
        if(!(params instanceof Map) || params.id == null){
            String msg = "Invalid params: $params.toString()"
            log.error(msg)
            throw new RuntimeException(msg)
        }
        def domainClass = grailsApplication.getClassForName(className)
        def criteria = domainClass.createCriteria()
        def instance = criteria.get {
            eq "id", params.long('id')
            domainClass.hasMany.each{ k,v ->
                fetchMode k, FetchMode.JOIN
            }
            // LockMode.PESSIMISTIC_WRITE doesn't work with PostgreSQL as
            // Hibernate 4.1.0 *fixed*!! this by changing their tests by writing a
            // hack that returns a LockMode.READ for PostgreSQL and IBM DB2
            // https://hibernate.onjira.com/browse/HHH-6985
            delegate.criteria.lockMode=LockMode.WRITE
        }
        if(instance.hasProperty('lockdata') && 
            (instance.lockdata.lockedBy == currentUser || 
             currentUser.isAdmin())){
            instance.properties = params
            if(instance.validate() && instance.save(flush:true)){
                instance.lockdata.delete()
                instance.lockdata = null
                instance.save(flush:true)
                return instance
            }
            else{
                if(instance.errors['enabled'] &&
                    instance.errors['enabled'].code == "validator.invalid"){
                    throw new RuntimeException(instance.errors['enabled'].code)
                } 
                instance.errors.allErrors.each{log.debug it}
                String msg = "Couldn't update $className id:$instance.id"
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
        else{
            String msg = "No lock found"
            log.error(msg)
            throw new RuntimeException(msg)
        }
    }
}
