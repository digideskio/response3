<div class="cnavigation">
    <ul>
     <li class="${params.action.equals('show') ? 'selected':''}">
        <g:link controller="customer" action="show" id="${params.id}">
            <span class="blue box"></span>
            <g:message code="show.customer"/>
        </g:link>
     </li>
     <li class="${params.action.equals('projects') ? 'selected':''}">
        <g:link controller="customer" action="projects" id="${params.id}">
            <span class="cyan box"></span>
            <g:message code="show.projects"/>
        </g:link>
     </li>
     <li class="${params.action.equals('users') ? 'selected':''}">
        <g:link controller="customer" action="users" id="${params.id}">
            <span class="yellow box"></span>
            <g:message code="show.users"/>
        </g:link>
     </li>
    </ul>
</div>