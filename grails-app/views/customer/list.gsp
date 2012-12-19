<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="customer.list" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:render template="header"/>
        <div class="fullwidth r3listing">
            <h1><g:message code="customer.list" /></h1>
            <g:form controller="customer">
            <g:hiddenField name="offset" value="${params.offset}"/>
            <g:hiddenField name="sort" value="${params.sort}"/>
            <g:hiddenField name="order" value="${params.order}"/>
            <table>
                <thead>
                <tr>
                      <th class="table_checkbox">
                        <g:checkBox name="" onclick="alert('select all');" value="${false}"/>
                      </th>
                    <r3:sortableColumn action="list" 
                                      class="table_title" 
                                      property="name" 
                                      title="${message(code:'name',default:'Name')}" 
                                      params="${null}" />
                    <r3:sortableColumn action="list" 
                                      class="table_date_created" 
                                      property="partner.name" 
                                      title="${message(code:'partner',default:'Partner')}" 
                                      params="${null}" />
                    <r3:sortableColumn action="list" 
                                      class="table_date_created" 
                                      property="dateCreated" 
                                      title="${message(code:'dateCreated',default:'Date created')}" 
                                      params="${null}" />
                    <r3:sortableColumn action="list" 
                                      class="table_last_updated" 
                                      property="lastUpdated" 
                                      title="${message(code:'lastUpdated',default:'Last updated')}" 
                                      params="${null}" />
                </tr>
                </thead>
                <tbody>
                <g:each in="${instances}" var="i" status="s">
	                <tr class="${(s % 2) == 0 ? 'even' : 'odd'}">
	                <td class="table_checkbox"><g:checkBox name="id" checked="${false}" value="${fieldValue(bean: i, field: 'id')}"/></td>
	                <td><g:link action="show" id="${fieldValue(bean: i, field: 'id')}">${fieldValue(bean: i, field: 'name')}</g:link></td>
	                <td><g:link controller="partner" action="show" id="${fieldValue(bean: i.partner, field: 'id')}">${fieldValue(bean: i.partner, field: 'name')}</g:link></td>
	                <td><g:formatDate format="yyyy.MM.dd - HH:mm" date="${i.dateCreated}"/></td>
	                <td><g:formatDate format="yyyy.MM.dd - HH:mm" date="${i.lastUpdated}"/></td>
	                </tr>
                </g:each>
                <tr>
                    <td colspan="3">
                        <input type="submit" value="${message(code:'delete',default:'Delete')}" class="button delete">
                    </td>
                    <td>
                       <g:if test="${total > grailsApplication.config.response3.lists.length}">
                           <span id="currentLength">
                           <g:message code="showing.total.number.of.customers" args="${[params.long('offset'),params.long('offset')+grailsApplication.config.response3.lists.length > total ? total : params.long('offset')+grailsApplication.config.response3.lists.length]}"/>
                           </span>
                       </g:if>
                       <g:else>
                           <span id="currentLength">${total}</span>
                       </g:else>
                       <span><g:message code="total.number.of.customers" args="${[total]}"/></span>
                    </td>
                    <td>
                        <g:if test="${params.offset < (total - grailsApplication.config.response3.lists.length)}">
                            <g:actionSubmit action="next" value="${message(code:'next',default:'Next')}" class="button next"/>
                        </g:if>
                        <g:if test="${params.offset > 0}">
                            <g:actionSubmit action="previous" value="${message(code:'previous',default:'Previous')}" class="button previous"/>
                        </g:if>
                    </td>
                </tr>
                </tbody>
            </table>
            </g:form>
        </div>

    </body>
</html>