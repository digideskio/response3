package com.redpill_linpro.response3.security

class LockService {
    
    def grailsApplication
    def springSecurityService

    static transactional = true
    def lock(String className, long id) {
        def domainClass = grailsApplication.getClassForName(className)
        log.debug domainClass
        def instance = domainClass.lock(id)
        if(instance.hasProperty('lock') && instance.lock == null){
            User currentUser = springSecurityService.getPrincipal()
            instance.lock = new Lock(lockedBy:currentUser)
            if(instance.validate() && instance.save()){
                return instance
            }
            else{
                throw new RuntimeException("Couldn't lock partner.")
            }
        }
        else{
            throw new RuntimeException(instance.lock.toString())
        }
    }
}
