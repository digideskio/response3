package com.redpill_linpro.response3.taglib

import org.codehaus.groovy.grails.web.mapping.CachingLinkGenerator

class HeaderTagLib {
    static namespace = 'header'

    CachingLinkGenerator grailsLinkGenerator

    def logo = {
        String imageURL = grailsLinkGenerator.link(
                uri:"/images/clients/" + request.currentClient.header.filename
        )
        out << '<div class="main_logo" style="'
        out << "background:url('${imageURL}') no-repeat 20px center;"
        out << "width:${request.currentClient.header.logoImageWidth};\">"
        out << '</div>'
    }

    def h1 = {
        out << "<h1 style=\"line-height:"
        out << "${request.currentClient.header.titleLineHeight};"
        out << "color:${request.currentClient.header.titleColor};\">"
        out << request.currentClient.header.title
        out << "</h1>"
    }

    def h2 = {
        out << "<h2 style=\"color:"
        out << "${request.currentClient.header.descriptionColor};\">"
        out << request.currentClient.header.description
        out << "</h2>"
    }
}
