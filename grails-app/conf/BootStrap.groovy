import com.redpill_linpro.response3.security.User;

class BootStrap {

    def grailsApplication
    
    def roleService
    def AdminUserService
    
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
        
    }
    def destroy = {
        
    }
}
