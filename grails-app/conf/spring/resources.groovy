// Place your Spring DSL code here

beans = {
    userDetailsService(com.redpill_linpro.response3.security.Response3UserDetailsService) {
        grailsApplication = ref('grailsApplication')
    }

    saltSource(com.redpill_linpro.response3.security.Response3SaltSource) {
        userPropertyToUse = application.config.grails.plugins.springsecurity.dao.reflectionSaltSourceProperty
    }
}
