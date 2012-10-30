package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.security.*

class UsersService {
    
    def userService

    def init() {
        def users = ['mrmanager','mrcustomer','mrconsultant']
        users.each{
            def user = User.findByUsername(it)
            if(!user){
                user = new User(
                    username: it, enabled: true, password: '1234567890',
                    firstname: it, lastname: it,
                    email:"$it@response3.org"
                )
                if (!user.validate()){
                    user.errors.allErrors.each{log.error(it)}
                } else {
                    try{
                        userService.create(
                            user, Role.findByAuthority(
                                "ROLE_"+it.replace("mr", "").toUpperCase()))
                    } catch (RuntimeException e){
                        throw new RuntimeException(e.getMessage())
                    }
                }
            }
            assert User.findByUsername(it) != null
        }
    }
}
