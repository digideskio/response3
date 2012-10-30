package com.redpill_linpro.response3.security

import static org.junit.Assert.*
import org.junit.*

import com.redpill_linpro.response3.content.Partner
import com.redpill_linpro.response3.content.Customer
import com.redpill_linpro.response3.content.Project

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} 
 * for usage instructions
 */
class LockServiceTests {
    
    final String PARTNERCN = "com.redpill_linpro.response3.content.Partner"
    final String CUSTOMERCN = "com.redpill_linpro.response3.content.Customer"
    final String PROJECTCN = "com.redpill_linpro.response3.content.Project"
    
    def lockService

    @Test
    void testLocking() {
        lockService.lock(PARTNERCN,1)
    }
}
