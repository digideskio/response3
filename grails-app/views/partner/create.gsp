<html>
    <head>
        <title><g:message code="response3"/> - <g:message code="create.new.partner" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:hasErrors bean="${instance}">
            <div class="errors">
                <g:renderErrors bean="${instance}" as="list" />
            </div>
        </g:hasErrors>
        <div class="r3widget-create r3form r3widget">
          <g:form name="partnerform" action="save">
			<div>
            	<h1><g:message code="create.new.partner" /></h1>
			</div>
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
                                       id="name" name="name"
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
                                      name="description" value="${fieldValue(bean: instance, field: 'description')}"/>
                      </td>
                  </tr>
                  <tr>
                      <td>
                      <input class="button" type="submit" value="${message(code:'create.partner')}" />

                      </td>
                  </tr>
             </tbody>
          </table>
          </g:form>
        </div>

    </body>
</html>