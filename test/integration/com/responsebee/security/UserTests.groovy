package com.responsebee.security

import static org.junit.Assert.*
import org.junit.*

class UserTests {
    def springSecurityService

    @Test
    void testPasswordIsEncodedWhenUserIsCreated() {
        def user = new User(
            username: 'testuser1', enabled: false, password: 'password1234',
            firstName: 'Test', lastName: 'TestPasswordEncode',
            email:"test@testers.org"
        )
        if(!user.validate()){
            user.error.allErrors.each{println it}
        } else {
            user.save(flush:true)
        }
        assertEquals springSecurityService.encodePassword(
            'password1234', user.salt), user.password
    }

    @Test
    void testPasswordIsReEncodedWhenUserIsUpdatedWithNewPassword() {
        def user = new User(
            username: 'testuser1', enabled: false, password: 'password1234',
            firstName: 'Test', lastName: 'TestPasswordEncode',
            email:"test@testers.org"
        )
        if(!user.validate()){
            user.error.allErrors.each{println it}
        } else {
            user.save(flush:true)
        }

        // update password
        user.password = 'password12345'
        user.save(flush: true)

        assertEquals springSecurityService.encodePassword(
            'password12345', user.salt), user.password
    }

    @Test
    void testPasswordIsNotReEncodedWhenUserIsUpdatedWithoutNewPassword() {
        def user = new User(
            username: 'testuser1', enabled: false, password: 'password1234',
            firstName: 'Test', lastName: 'TestPasswordEncode',
            email:"test@testers.org"
        )
        if(!user.validate()){
            user.error.allErrors.each{println it}
        } else {
            user.save(flush:true)
        }

        // update user, but not password
        user.enabled = true
        user.save(flush: true)

        assertEquals springSecurityService.encodePassword(
            'password1234', user.salt), user.password
    }

    @Test
    void testPasswordIsNotReEncodedWhenUserIsReloaded() {
        new User(
            username: 'testuser1', enabled: false, password: 'password1234',
            firstName: 'Test', lastName: 'TestPasswordEncode',
            email:"test@testers.org"
        ).save(flush: true)

        // reload user
        def user = User.findByUsername('testuser1')
        assertNotNull user

        assertEquals springSecurityService.encodePassword(
            'password1234', user.salt), user.password
    }
}
