<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="edit.partner" /> - ${fieldValue(bean: instance, field: 'name')}</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:hasErrors bean="${instance}">
            <div class="errors">
                <g:renderErrors bean="${instance}" as="list" />
            </div>
        </g:hasErrors>
        <g:render template="header"/>
        <div class="r3widget r3form edit">
          <g:form name="partnerform" action="update">
            <input type="hidden" name="id" value="${fieldValue(bean: instance, field: 'id')}" />
            <h1><g:message code="edit.partner" /></h1>
            <table>
              <tbody>
                  <tr>
                      <td colspan="2">
                          <label>
                              <g:message code="name" default="Name" />
                              <span class="fieldRequired" title="Required">*</span>
                          </label>
                      </td>
                   </tr>
                   <tr>
                      <td colspan="2">
                          <g:textField class="${hasErrors(bean: instance, field: 'name', 'error')}" 
                                       id="name" name="name"
                                       value="${fieldValue(bean: instance, field: 'name')}" />
                      </td>
                  </tr>
                  
                  <tr>
                      <td colspan="2">
                          <label>
                              <g:message code="description" default="Description" />:
                          </label>
                      </td>
                  </tr>
                  <tr>
                      <td colspan="2">
                          <g:textArea class="${hasErrors(bean: instance, field: 'description', 'error')}"
                                      name="description" value="${fieldValue(bean: instance, field: 'description')}"/>
                      </td>
                  </tr>
                  <tr>
                      <td>
                      <input class="button" type="submit" value="${message(code:'update.partner')}" />
                      </td>
                      <td>
                      <input class="button" type="submit" value="${message(code:'cancel.update')}" />
                      </td>
                  </tr>
             </tbody>
          </table>
          </g:form>
        </div>
    </body>
</html>