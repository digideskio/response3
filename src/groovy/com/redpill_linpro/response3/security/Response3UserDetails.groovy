package com.redpill_linpro.response3.security

import grails.plugin.springsecurity.userdetails.GrailsUser


class Response3UserDetails extends GrailsUser {
    public final String salt
    
    Response3UserDetails(GrailsUser base, String salt) {
        super(base.username, base.password, base.enabled,
            base.accountNonExpired, base.credentialsNonExpired, 
            base.accountNonLocked,
            base.authorities, base.id
        )

        this.salt = salt;
    }
}
