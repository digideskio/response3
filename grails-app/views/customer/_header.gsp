<div class="r3widget hlayout">
    <h1><g:message code="customer.administration"/></h1>
    <ul>
    <g:if test="${params.action.toLowerCase() in ['customers','users','show']}">
        <li><g:link controller="partner" action="list">
            <span class="darkblue box"></span>
            <g:message code="list.customers"/></g:link></li>
    </g:if>
        <li><g:link controller="customer" action="create">
        <span class="darkgreen box"></span>
           <g:message code="create.new.customer"/>
        </g:link></li>
    </ul>
</div>
<div class="clear"></div>