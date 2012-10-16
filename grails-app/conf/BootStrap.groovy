import com.redpill_linpro.response3.security.User;

class BootStrap {

    def grailsApplication
    
    def init = { servletContext ->
        def admin = new User(
            username: 'admin', enabled: true, password: 'admin123456',
            firstName: 'Olav', lastName: 'Gjerde', 
            email:"olav@administrators.org" 
        )
        if (!admin.validate()){
            admin.errors.allErrors.each{log.error(it)}
        } else {
            admin.save(flush: true)
        }
    }
    def destroy = {
        
    }
}
