package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.security.*
import grails.transaction.Transactional

@Transactional
class UsersService {
    
    def userService

    def init() {
        List<String> users = ['mrmanager','mrcustomer','mrconsultant','mrpartner']
        createUsersForClient(users, ResponseClient.get(1))

        users = ['geirmanager','erikcustomer','trulsconsultant','vidarpartner']
        createUsersForClient(users, ResponseClient.get(2))

        users = ['olemanager','jamescustomer','ottoconsultant','karipartner']
        createUsersForClient(users, ResponseClient.get(3))
    }

    private def createUsersForClient(List<String> users, ResponseClient client){
        List<String> roles = ['ROLE_MANAGER','ROLE_CUSTOMER',
                'ROLE_CONSULTANT','ROLE_PARTNER']
        Iterator<String> iterator = roles.iterator();
        users.each{
            Role role = Role.findByAuthority(iterator.next())
            def user = User.findByUsername(it)
            if(!user){
                user = new User(
                        responseClient: client,
                        username: it, enabled: true, password: '1234567890',
                        firstname: it, lastname: it,
                        email:"$it@response3.org"
                )
                if (!user.validate()){
                    user.errors.allErrors.each{log.error(it)}
                } else {
                    try{
                        userService.create(user, role)
                    } catch (RuntimeException e){
                        throw new RuntimeException(e.message)
                    }
                }
            }
            assert User.findByUsername(it) != null
        }
    }
}
