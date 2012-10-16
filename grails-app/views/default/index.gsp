<html>
    <head>
        <title><g:message code="Response.Index.Welcome"/></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <g:if test="${flash.message}">
          <div class="contentMargin">
            <div class="message">${flash.message}</div>
          </div>
        </g:if>
        <h1 style="margin-left:20px;">
            <g:message code="welcome.to.response3" default="Welcome to Response-3.0"/></h1>
        <br/>
        <p style="margin-left:20px;width:80%;">
          <strong>
            <g:message code="Response.Index.BoldHeader"/>
          </strong>
          <br/>
          <br/>
          <g:message code="Response.Index.DescriptionText1"/>
        </p>
        <sec:access expression="hasRole('ROLE_ADMIN')">
          <div style="text-align:right;right:10px;position:fixed;bottom:10px;">
                  <div>Version 3.0.<g:meta name="app.version"/></div>
                  <div>Built with Grails <g:meta name="app.grails.version"/></div>
          </div>
        </sec:access>
    </body>
</html>