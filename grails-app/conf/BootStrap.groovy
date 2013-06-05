import com.redpill_linpro.response3.EmbeddedElasticSearch

class BootStrap {

    def grailsApplication
    def ipAddressFilter
    def adminUserService
    def roleService
    def initService
    EmbeddedElasticSearch embeddedElasticSearch;
    
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
                embeddedElasticSearch = new EmbeddedElasticSearch()
                developmentSetup()
            }
        }
        Map filterMap = [
                '/monitoring/**':'192.168.0.1',
                '/administration/**':'192.168.0.2'
        ]
        ipAddressFilter.setIpRestrictions(filterMap)
    }
    def developmentSetup(){
        initService.init()
    }
    def destroy = {
        embeddedElasticSearch.stop()
    }
}
