<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="show.partner" /> - ${fieldValue(bean: instance, field: 'name')}</title>
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
            <h1><g:message code="show.partner" /></h1>
            <g:if test="${instance.lockdata != null}">
                <h2><g:message code="locked.by" />:${instance.lockdata.lockedBy.username}</h2>
            </g:if>
            <table>
              <tbody>
                  <tr>
                      <td class="">
                          <label>
                              <g:message code="name" default="Name" />
                              <span class="fieldRequired" title="Required">*</span>
                          </label>
                      </td>
                   </tr>
                   <tr>
                      <td>
                          <g:textField class="${hasErrors(bean: instance, field: 'name', 'error')}" 
                                       id="name" name="name" readonly="readonly"
                                       value="${fieldValue(bean: instance, field: 'name')}" />
                      </td>
                  </tr>
                  
                  <tr>
                      <td class="">
                          <label>
                              <g:message code="description" default="Description" />:
                          </label>
                      </td>
                  </tr>
                  <tr>
                      <td>
                          <g:textArea class="${hasErrors(bean: instance, field: 'description', 'error')}"
                                      readonly="readonly"
                                      name="description" value="${fieldValue(bean: instance, field: 'description')}"/>
                      </td>
                  </tr>
                  <tr>
                      <td>
                          <label>
                              <g:message code="enabled" default="Enabled" />:
                          </label>
                      </td>
                  </tr>
                  <tr>
                      <td>
                          <g:checkBox class="${hasErrors(bean: instance, field: 'enabled', 'error')}"
                                      disabled="disabled"
                                      name="enabled" value="${fieldValue(bean: instance, field: 'enabled')}"/>
                      </td>
                  </tr>
                  <tr>
                      <td>
                      <input class="button" type="submit" value="${message(code:'edit.partner')}" />
                      </td>
                  </tr>
             </tbody>
          </table>
          </g:form>
        </div>

    </body>
</html>