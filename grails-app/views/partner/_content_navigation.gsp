<div class="cnavigation">
    <ul>
     <li class="${params.action.equals('show') ? 'selected':''}">
        <g:link controller="partner" action="show" id="${params.id}">
            <span class="blue box"></span>
            <g:message code="show.partner"/>
        </g:link>
     </li>
     <li class="${params.action.equals('customers') ? 'selected':''}">
        <g:link controller="partner" action="customers" id="${params.id}">
            <span class="cyan box"></span>
            <g:message code="show.customers"/>
        </g:link>
     </li>
     <li class="${params.action.equals('users') ? 'selected':''}">
        <g:link controller="partner" action="users" id="${params.id}">
            <span class="yellow box"></span>
            <g:message code="show.users"/>
        </g:link>
     </li>
    </ul>
</div>