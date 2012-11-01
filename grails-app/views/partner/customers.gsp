<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="customer.list.partner" /> - ${fieldValue(bean: instance, field: 'name')}</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:hasErrors bean="${instance}">
            <div class="errors">
                <g:renderErrors bean="${instance}" as="list" />
            </div>
        </g:hasErrors>
        <g:render template="header"/>
        <div class="r3widget r3form show ${instance.lockdata == null ? 'unlocked':'locked'}">
          <g:form name="partnerform" action="edit" id="${instance.id}">
            <div>
	            <h1>${fieldValue(bean: instance, field: 'name')}</h1>
	            <g:if test="${instance.lockdata != null}">
	                <h2><g:message code="locked.by" />:${instance.lockdata.lockedBy.username}</h2>
	            </g:if>
	        </div>
            <div class="r3listing">
	            <table id="customers-table">
	                <thead>
	                <tr>
	                    <r3:sortableColumn action="customers" 
	                                      class="table_title"
	                                      colspan="2"
	                                      property="name" 
	                                      title="${message(code:'name',default:'Name')}" 
	                                      params="${[id:params.id]}" />
	                </tr>
	                </thead>
	                <tbody>
	                <g:each in="${instances}" var="i" status="s">
	                    <tr class="${(s % 2) == 0 ? 'even' : 'odd'}">
	                    <td colspan="2"><g:link controller="customer" action="show" id="${i.id}">${i.name}</g:link></td>
	                    </tr>
	                </g:each>
	                <tr>
	                    <td>
	                       <g:if test="${total > grailsApplication.config.response3.lists.length}">
                               <input type="submit" class="button more" value="${message(code:'show.more')}" onclick=""/>
                           </g:if>
	                       <script type="text/javascript">
	                       var listLength = ${grailsApplication.config.response3.lists.length};
	                       var totalLength = ${total};
	                       </script>
	                    </td>
	                    <td>
	                       <g:message code="total.number.of.customers" args="${[grailsApplication.config.response3.lists.length,total]}"/>
	                    </td>
	                </tr>
	                </tbody>
	            </table>
	        </div>
          <g:render template="content_navigation"/>
          <div class="clear"></div>
          </g:form>
        </div>

    </body>
</html>