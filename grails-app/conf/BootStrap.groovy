import com.redpill_linpro.response3.security.User;

class BootStrap {

    def grailsApplication
    
    def adminUserService
    def roleService
    def initService
    
    def init = { servletContext ->
        roleService.init()
        adminUserService.init()
        environments {
            production {
            }
            test{
                developmentSetup()
            }
            development {
                developmentSetup()
            }
        }
        
    }
    def developmentSetup(){
        initService.init()
    }
    def destroy = {
        
    }
}
