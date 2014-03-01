// Place your Spring DSL code here

beans = {
    userDetailsService(com.responsebee.security.Response3UserDetailsService) {
        grailsApplication = ref('grailsApplication')
    }

    saltSource(com.responsebee.security.Response3SaltSource) {
        userPropertyToUse = application.config.grails.plugins.springsecurity.dao.reflectionSaltSourceProperty
    }
}
