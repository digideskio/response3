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
    
    def mockedUserService = [
        currentUser: User.get(1),
        getCurrentUser:{ -> return mockedUserService.currentUser },
        setCurrentUser:{ User user-> mockedUserService.currentUser = user}
    ]

    @Test
    void testPartnerLocking() {
        lockService.userService = mockedUserService
        def pm = User.findByUsername('mrmanager')
        def admin = User.findByUsername('mrmanager')
        def consultant = User.findByUsername('mrconsultant')
        lockService.userService.setCurrentUser(admin)
        
        def partner = Partner.findByName('Redpill')
        assertNotNull partner
        partner = lockService.lock(PARTNERCN,partner.id)
        assertNotNull partner.lockdata
        
        // Try to lock partner as consultant
        lockService.userService.setCurrentUser(consultant)
        RuntimeException rtException = null
        try{
            lockService.lock(PARTNERCN,partner.id)
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        
        // Try to unlock partner as consultant
        rtException = null
        try{
            lockService.unlock(PARTNERCN,partner.id)
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        
        // Try to update partner as consultant
        rtException = null
        try{
            lockService.update(PARTNERCN,[id:partner.id])
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        
        // Unlock partner as admin
        lockService.userService.setCurrentUser(admin)
        partner = lockService.unlock(PARTNERCN,partner.id)
        assertNull partner.lockdata
        
        // Lock partner as project manager
        lockService.userService.setCurrentUser(pm)
        partner = lockService.lock(PARTNERCN,partner.id)
        assertNotNull partner.lockdata
        assertEquals partner.lockdata.lockedBy, pm
        
        // Unlock partner as admin
        lockService.userService.setCurrentUser(admin)
        partner = lockService.unlock(PARTNERCN,partner.id)
        assertNull partner.lockdata
        
        // Try update partner as project manager
        rtException = null
        lockService.userService.setCurrentUser(pm)
        try{
            partner = lockService.update(PARTNERCN,[id:partner.id])
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        
        // Lock partner as project manager and update with wrong data
        partner = lockService.lock(PARTNERCN,partner.id)
        def tmap = null
        rtException = null
        try{
            partner = lockService.update(PARTNERCN,tmap)
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        rtException = null
        String description = 'LockServiceTest was here' 
        tmap = [description:description]
        try{
            partner = lockService.update(PARTNERCN,tmap)
        } catch(RuntimeException e){
            rtException = e
        }
        assertNotNull rtException
        rtException = null
        tmap = [id:partner.id,description:description]
        try{
            partner = lockService.update(PARTNERCN,tmap)
        } catch(RuntimeException e){
            rtException = e
        }
        assertNull rtException
        assertNull partner.lockdata
        assertEquals partner.description, description
    }
}
