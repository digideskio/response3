<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="create.new.partner" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:render template="header"/>
        <div class="r3listing">
            <h1><g:message code="partner.list" /></h1>
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
                <g:each in="${instances}" var="i">
                <tr>
                <td><g:checkBox name="id" checked="${false}" value="${fieldValue(bean: i, field: 'id')}"/></td>
                <td>${fieldValue(bean: i, field: 'name')}</td>
                <td><g:formatDate format="yyyy.MM.dd - HH:mm" date="${i.dateCreated}"/></td>
                <td><g:formatDate format="yyyy.MM.dd - HH:mm" date="${i.lastUpdated}"/></td>
                </tr>
                </g:each>
                </tbody>
            </table>
        </div>

    </body>
</html>