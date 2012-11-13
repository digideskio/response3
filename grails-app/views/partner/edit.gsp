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
        <div class="r3widget r3form edit ${instance.lockdata == null ? 'unlocked':'locked'}">
          <g:form name="partnerform" controller="partner">
            <div>
                <h1>${fieldValue(bean: instance, field: 'name')}</h1>
                <g:if test="${instance.lockdata != null}">
                    <h2><g:message code="locked.by" />:${instance.lockdata.lockedBy.username}</h2>
                </g:if>
            </div>
            <input type="hidden" name="id" value="${fieldValue(bean: instance, field: 'id')}" />
            <table>
              <tbody>
                  <tr>
                      <td colspan="2">
                          <label>
                              <g:message code="name" default="Name" />
                              <span class="fieldRequired" data-tooltip="${message(code:'required')}">*</span>
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
                          <label>
                              <g:message code="enabled" default="Enabled" />:
                          </label>
                      </td>
                  </tr>
                  <tr>
                      <td>
                          <g:checkBox class="${hasErrors(bean: instance, field: 'enabled', 'error')}"
                                      name="enabled" checked="${instance.enabled}" value="${true}"/>
                      </td>
                  </tr>
                  <tr>
                      <td>
                        <g:actionSubmit class="button" value="${message(code:'update.partner')}" action="update"/>
                      </td>
                      <td>
                        <g:actionSubmit class="button" value="${message(code:'cancel.update')}" action="cancel"/>
                      </td>
                  </tr>
             </tbody>
          </table>
          <div class="edit_contact_persons">
            <label>
                <g:message code="contact.persons" default="Contact persons" />:
            </label>
            <table>
              <tbody>
                <g:each var="c" in="${clients}">
                  <tr>
                    <td><g:checkBox class="table_checkbox" name="contactPersons" checked="${instance.contactPersons.contains(c)}" value="${c.id}"/></td>
                    <td>
                      <g:link controller="user" action="show" id="${c.id}">
                       <div>
                           <img alt="user" src="${createLinkTo(dir:'images/icons',file:'account.png')}" height="16" width="16">
                           ${fieldValue(bean: c, field: 'name')}
                       </div>
                       </g:link>
                    </td>
                  </tr>
                </g:each>
              </tbody>
            </table>
          </div>
          <div class="clear"></div>
          </g:form>
        </div>
    </body>
</html>