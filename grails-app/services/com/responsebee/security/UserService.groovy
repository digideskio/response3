package com.responsebee.security

import grails.transaction.Transactional

@Transactional
class UserService {
    
    def springSecurityService

    def create(User person, Role authority) {
        if(person.validate() && person.save(flush:true)){
            def ur = new UserRole(user: person, role: authority)
            if(!(ur.validate() && ur.save(flush: true, insert: true))){
                ur.errors.allErrors.each {
                    log.error it.toString()
                }
                throw new RuntimeException(
                    "Couldn't save user together with role")
            }
        }
        else{
            log.error person.username
            person.errors.allErrors.each {
                log.error it.toString()
            }
            throw new RuntimeException(
                "Couldn't save user")
        }
        return true
    }
    
    def update(User person) {
        if(person.validate()
            && person.save(flush:true)){
        }
        else{
            log.error person.username
            person.errors.allErrors.each {
                log.error it.toString()
            }
            throw new RuntimeException(
                "Couldn't update user together with new properties")
        }
        log.debug("Person updated")
        return true
    }
    
    def getCurrentUser(){
        String username = springSecurityService.getPrincipal().username
        return User.findByUsername(username)
    }
}
