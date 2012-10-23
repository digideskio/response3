<div class="r3widget hlayout">
    <h1><g:message code="partner.administration"/></h1>
    <ul>
    <g:if test="${params.action.toLowerCase() in ['edit','show']}">
        <li><g:link controller="partner" action="list">
            <span class="darkblue box"></span>
            <g:message code="list.partners"/></g:link></li>
    </g:if>
        <li><g:link controller="partner" action="create">
        <span class="darkgreen box"></span>
           <g:message code="create.new.partner"/>
        </g:link></li>
    </ul>
</div>
<div class="clear"></div>