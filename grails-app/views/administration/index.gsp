<html>
    <head>
        <title><g:message code="response3"/></title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <div class="r3widget">
            <h1>Partner</h1>
            <ul>
	            <li><g:link controller="partner" action="create">
	            <span class="darkgreen box"></span>
	               Create new partner
	            </g:link></li>
                <li><g:link controller="partner" action="list">
                    <span class="darkblue box"></span>
                    List partners</g:link></li>
            </ul>
        </div>
        <div class="r3widget">
            <h1>Customer</h1>
            <ul>
                <li><g:link controller="customer" action="create">
                    <span class="blue box"></span>
                    Create new customer
                </g:link></li>
                <li><g:link controller="customer" action="list">
                <span class="purple box"></span>
                List customers</g:link></li>
            </ul>
        </div>
        <div class="r3widget">
            <h1>Project</h1>
            <ul>
                <li><g:link controller="project" action="create">
                <span class="red box"></span>
                Create new project</g:link></li>
                <li><g:link controller="project" action="list">
                <span class="green box"></span>
                List projects</g:link></li>
            </ul>
        </div>
    </body>
</html>