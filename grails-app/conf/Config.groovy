// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    appenders {
        environments {
            production {
                rollingFile name: "response3Appender",
                            maxFileSize: 1024*1024,
                            file: "log/response3.log"
            }
            development{
                console name:'stdout',
                        layout:pattern(
                                conversionPattern:
                                        '%p %d{HH:mm:ss} %c{2} - %m%n'
                        )
            }
        }
    }

    info   'grails.app'
    
    warn   'grails.app'
    
    error  'grails.app.services.org.grails.plugin.resource',
           'grails.app.taglib.org.grails.plugin.resource',
           'grails.app.resourceMappers.org.grails.plugin.resource',
           'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
           
    debug  'grails.app',
           'com.redpill_linpro.response3'
           //'org.hibernate.SQL'
           
    //trace 'org.hibernate.type'
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.redpill_linpro.response3.security.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.redpill_linpro.response3.security.UserRole'
grails.plugins.springsecurity.authority.className = 'com.redpill_linpro.response3.security.Role'

// Additional password protection
grails.plugins.springsecurity.dao.reflectionSaltSourceProperty = 'salt'

// Url protection
grails.plugins.springsecurity.securityConfigType = 'Annotation'
grails.plugins.springsecurity.rejectIfNoRule = true
grails.plugins.springsecurity.controllerAnnotations.staticRules = [
    '/responseClient/**':       ['ROLE_ADMIN'],
    '/responseClient/edit':     ['ROLE_ADMIN','ROLE_MANAGER'],
    '/administration/**':       ['ROLE_ADMIN','ROLE_MANAGER'],
    '/js/**':                   ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/css/**':                  ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/images/**':               ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/*':                       ['IS_AUTHENTICATED_FULLY'],
    '/default/**':              ['IS_AUTHENTICATED_FULLY'],
    '/login/**':                ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/logout/**':               ['IS_AUTHENTICATED_ANONYMOUSLY']
]
grails.plugins.springsecurity.ipRestrictions = [
    '/monitoring/**':           '10.0.0.0/24',
]
grails.gorm.default.mapping = {
    // Prevent GORM to update all properties for each update
    dynamicUpdate true 
    // Use proper DB locking/ Hibernate pessimistic locking
    version false
}
// response3 specific configuration
response3.lists.max=100
response3.lists.length=20
response3.elasticsearch.date.format='yyyy-MM-dd HH:mm:ss'