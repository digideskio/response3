package com.redpill_linpro.response3.security

import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.userdetails.UserDetails

class Response3UserDetailsService extends GormUserDetailsService {
    protected UserDetails createUserDetails(user, Collection authorities) {
        new Response3UserDetails(
            (GrailsUser) super.createUserDetails(user, authorities), user.salt
        )
    }
}
