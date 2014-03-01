import com.redpill_linpro.response3.search.ElasticSearchIndex
import com.redpill_linpro.response3.search.EmbeddedElasticSearch
import com.redpill_linpro.response3.security.ResponseClient

class BootStrap {

    def grailsApplication
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
    }
    def developmentSetup(){
        initService.init()
    }
    def destroy = {
        embeddedElasticSearch.stop()
    }
}
