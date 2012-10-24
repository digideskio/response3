package com.redpill_linpro.response3.security

class LockService {
    
    def grailsApplication
    def userService

    static transactional = true
    def lock(String className, long id) {
        def currentUser = userService.getCurrentUser()
        if(!currentUser){
            String msg = "Couldn't find current user"
            log.error(msg)
            throw new RuntimeException(msg)
        }
        def domainClass = grailsApplication.getClassForName(className)
        def instance = domainClass.lock(id)
        if(instance.hasProperty('lock') && instance.lock == null){
            def lock = new Lock(lockedBy:currentUser)
            lock.save()
            instance.lock = lock 
            if(instance.validate() && instance.save()){
                return instance
            }
            else{
                String msg = "Couldn't lock $className id:$instance.id"
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
        else{
            if(instance.lock.lockedBy == currentUser || 
                currentUser.isAdmin()){
                return instance
            }
            else{
                String msg = instance.lock.toString()
                log.error(msg)
                throw new RuntimeException(msg)
            }
        }
    }
    
    def unlock(String className, long id){
        def currentUser = userService.getCurrentUser()
        def domainClass = grailsApplication.getClassForName(className)
        def instance = domainClass.lock(id)
        if(instance.hasProperty('lock') && 
            (instance.lock.lockedBy == currentUser || 
             currentUser.isAdmin())){
            instance.lock.delete()
            instance.lock = null
            if(instance.validate() && instance.save()){
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
}
