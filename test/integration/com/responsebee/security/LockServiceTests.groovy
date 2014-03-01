package com.responsebee.security

import static org.junit.Assert.*
import org.junit.*

import com.responsebee.content.Partner

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} 
 * for usage instructions
 */
class LockServiceTests {
    
    final String PARTNERCN = "com.responsebee.content.Partner"
    final String CUSTOMERCN = "com.responsebee.content.Customer"
    final String PROJECTCN = "com.responsebee.content.Project"
    
    def lockService
    def userService
    
    def mockedUserService = [
        currentUser: User.get(1),
        getCurrentUser:{ -> return mockedUserService.currentUser },
        setCurrentUser:{ User user-> mockedUserService.currentUser = user}
    ]
    
    private Partner getNewPartner(){
        // Create new Partner object
        def partner = new Partner(name:"HibernateLockTest")
        def user = new User(
            username: 'hibernatelocktest', enabled: true,
            password: '1234567890',
            firstname: 'hibernate', lastname: 'locktest',
            email:'hibernate@responsebee.org'
        )
        try{
            userService.create(user, Role.findByAuthority("ROLE_PARTNER"))
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage())
        }
        partner.addToClients(user)
        partner.addToContactPersons(user)
        def user2 = new User(
            username: 'hibernatelocktest2', enabled: true,
            password: '1234567890',
            firstname: 'hibernate2', lastname: 'locktest',
            email:'hibernate2@responsebee.org'
        )
        try{
            userService.create(user2, Role.findByAuthority("ROLE_PARTNER"))
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage())
        }
        partner.addToClients(user2)
        partner.validate()
        partner.errors.allErrors.each{println it}
        partner.save(flush:true)
        return partner
    }

    @Test
    void testPartnerLocking() {
        lockService.userService = mockedUserService
        def pm = User.findByUsername('mrmanager')
        def admin = User.findByUsername('mrmanager')
        def consultant = User.findByUsername('mrconsultant')
        lockService.userService.setCurrentUser(admin)
        
        def partner = getNewPartner()

        partner = lockService.lock(PARTNERCN,partner.id)
        assertNotNull partner
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
    
    def testPartnerLockContactPerson(){
        println Partner.list().name
        println Partner.list().id
        def partner = getNewPartner()
        def consultant = User.findByUsername('mrconsultant')
        lockService.userService = mockedUserService
        // Try to lock partner as consultant
        lockService.userService.setCurrentUser(consultant)
        RuntimeException rtException = null
        println partner.id
        try{
            lockService.lock(PARTNERCN,partner.id)
        } catch(RuntimeException e){
            rtException = e
            println rtException.getMessage()
        }
        assertNull rtException
        def contactPersons = [] as Set
        for(user in partner.clients){
            contactPersons.add(user)
        }
        def parameters = [id:partner.id,contactPersons:contactPersons] 
        def instance = lockService.update(PARTNERCN,parameters)
        assert instance.contactPersons.size() == 2
        assertNull instance.lockdata
    }
}