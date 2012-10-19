<html>
    <head>
        <title><g:message code="response3.administration"/></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <div class="r3widget">
            <h1><g:message code="partner"/></h1>
            <ul>
	            <li><g:link controller="partner" action="create">
	            <span class="darkgreen box"></span>
	               <g:message code="create.new.partner"/>
	            </g:link></li>
                <li><g:link controller="partner" action="list">
                    <span class="darkblue box"></span>
                    <g:message code="list.partners"/></g:link></li>
            </ul>
        </div>
        <div class="r3widget">
            <h1><g:message code="customer"/></h1>
            <ul>
                <li><g:link controller="customer" action="create">
                    <span class="blue box"></span>
                    <g:message code="create.new.customer"/>
                </g:link></li>
                <li><g:link controller="customer" action="list">
                <span class="purple box"></span>
                <g:message code="list.customers"/></g:link></li>
            </ul>
        </div>
        <div class="r3widget">
            <h1><g:message code="project"/></h1>
            <ul>
                <li><g:link controller="project" action="create">
                <span class="red box"></span>
                <g:message code="create.new.project"/></g:link></li>
                <li><g:link controller="project" action="list">
                <span class="green box"></span>
                <g:message code="list.projects"/></g:link></li>
            </ul>
        </div>
    </body>
</html>