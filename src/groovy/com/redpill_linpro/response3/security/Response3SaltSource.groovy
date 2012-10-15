package com.redpill_linpro.response3.security

import org.springframework.security.authentication.dao.ReflectionSaltSource
import org.springframework.security.core.userdetails.UserDetails

class Response3SaltSource extends ReflectionSaltSource {
    Object getSalt(UserDetails user) {
        user[userPropertyToUse]
    }
}
