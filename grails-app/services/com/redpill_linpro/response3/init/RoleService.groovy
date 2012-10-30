package com.redpill_linpro.response3.init

import com.redpill_linpro.response3.security.Role

class RoleService {

    def init() {
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        if(!adminRole){
            adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
        }
        def restRole = Role.findByAuthority('ROLE_REST')
        if(!restRole){
            restRole = new Role(authority: 'ROLE_REST').save(flush: true)
        }
        def managerRole = Role.findByAuthority('ROLE_MANAGER')
        if(!managerRole){
            managerRole = new Role(authority: 'ROLE_MANAGER').save(flush: true)
        }
        def consultantRole = Role.findByAuthority('ROLE_CONSULTANT')
        if(!consultantRole){
            consultantRole = new Role(
                authority: 'ROLE_CONSULTANT').save(flush: true)
        }
        def partnerRole = Role.findByAuthority('ROLE_PARTNER')
        if(!partnerRole){
            partnerRole = new Role(authority: 'ROLE_PARTNER').save(flush: true)
        }
        def customerRole = Role.findByAuthority('ROLE_CUSTOMER')
        if(!customerRole){
            customerRole = new Role(
                authority: 'ROLE_CUSTOMER').save(flush: true)
        }
    }
}
