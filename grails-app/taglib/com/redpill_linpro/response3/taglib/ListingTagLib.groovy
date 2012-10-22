package com.redpill_linpro.response3.taglib

class ListingTagLib {
    static namespace = 'r3'
    
    Closure sortableColumn = { attrs ->
        def writer = out
        if (!attrs.property) {
            throwTagError("Tag [sortableColumn] is missing required attribute [property]")
        }

        if (!attrs.title && !attrs.titleKey) {
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")
        }

        def property = attrs.remove("property")
        def action = attrs.action ? attrs.remove("action") : (actionName ?: "list")

        def defaultOrder = attrs.remove("defaultOrder")
        if (defaultOrder != "desc") defaultOrder = "asc"

        // current sorting property and order
        def sort = params.sort
        def order = params.order

        // add sorting property and params to link params
        def linkParams = [:]
        if (params.id) linkParams.put("id", params.id)
        def paramsAttr = attrs.remove("params")
        if (paramsAttr) linkParams.putAll(paramsAttr)
        linkParams.sort = property

        // propagate "max" and "offset" standard params
        if (params.max) linkParams.max = params.max
        if (params.offset) linkParams.offset = params.offset

        // determine and add sorting order for this column to link params
        attrs.class = (attrs.class ? "${attrs.class} sortable" : "sortable")
        if (property == sort) {
            attrs.class = attrs.class + " sorted " + order
            if (order == "asc") {
                linkParams.order = "desc"
            }
            else {
                linkParams.order = "asc"
            }
        }
        else {
            linkParams.order = defaultOrder
        }

        // determine column title
        def title = attrs.remove("title")
        def titleKey = attrs.remove("titleKey")
        def mapping = attrs.remove('mapping')
        if (titleKey) {
            if (!title) title = titleKey
            def messageSource = grailsAttributes.messageSource
            def locale = RCU.getLocale(request)
            title = messageSource.getMessage(titleKey, null, title, locale)
        }

        writer << "<th "
        // process remaining attributes
        attrs.each { k, v ->
            writer << "${k}=\"${v?.encodeAsHTML()}\" "
        }
        writer << '>'
        def linkAttrs = [params: linkParams]
        if(mapping) {
            linkAttrs.mapping = mapping
        } else {
            linkAttrs.action = action
        }
        writer << link(linkAttrs) {
            title
        }
        writer << '</th>'
    }
}
