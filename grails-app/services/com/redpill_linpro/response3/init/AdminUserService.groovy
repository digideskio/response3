package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.security.User
import com.redpill_linpro.response3.security.Role

class AdminUserService {
    
    static transactional = true
    
    def userService

    def init() {
        def admin = User.findByUsername('admin')
        if(!admin){
            admin = new User(
                username: 'admin', enabled: true, password: 'admin123456',
                firstname: 'Olav', lastname: 'Gjerde',
                email:"olav@administrators.org"
            )
            if (!admin.validate()){
                admin.errors.allErrors.each{log.error(it)}
            } else {
                try{
                    userService.create(
                        admin, Role.findByAuthority('ROLE_ADMIN'))
                } catch (RuntimeException e){
                    throw new RuntimeException(e.getMessage())
                }
            }
        }
        def rest = User.findByUsername('rest')
        if(!rest){
            rest = new User(
                username: 'rest', enabled: true, password: 'rest123456',
                firstname: 'REST', lastname: 'HTTP',
                email:"rest@webservices.org"
            )
            if (!rest.validate()){
                rest.errors.allErrors.each{log.error(it)}
            } else {
                try{
                    userService.create(rest, Role.findByAuthority('ROLE_REST'))
                } catch (RuntimeException e){
                    throw new RuntimeException(e.getMessage())
                }
            }
        }
        assert User.findByUsername('admin') != null
        assert User.findByUsername('rest') != null
    }
}
