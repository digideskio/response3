import com.responsebee.search.ElasticSearchIndex
import com.responsebee.search.EmbeddedElasticSearch
import com.responsebee.security.ResponseClient

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
