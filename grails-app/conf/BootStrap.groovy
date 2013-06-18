import com.redpill_linpro.response3.search.ElasticSearchIndex
import com.redpill_linpro.response3.search.EmbeddedElasticSearch
import com.redpill_linpro.response3.security.ResponseClient

class BootStrap {

    def grailsApplication
    def ipAddressFilter
    def initService
    EmbeddedElasticSearch embeddedElasticSearch;
    
    def init = { servletContext ->
        environments {
            production {
            }
            test{
                developmentSetup()
            }
            development {
                embeddedElasticSearch = new EmbeddedElasticSearch()
                developmentSetup()
                ElasticSearchIndex searchable =
                    new ElasticSearchIndex(ResponseClient.list());
                searchable.buildMapping()
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
