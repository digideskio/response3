<div class="r3widget">
	<h1><g:message code="client"/></h1>
	<ul>
		<sec:ifAnyGranted roles="ROLE_ADMIN">
			<li><g:link controller="client" action="create">
				<span class="blue box"></span>
				<g:message code="create.new.client"/>
			</g:link></li>
			<li><g:link controller="client" action="list">
				<span class="purple box"></span>
				<g:message code="list.clients"/></g:link></li>
		</sec:ifAnyGranted>
		<li><g:link controller="client" action="edit">
			<span class="purple box"></span>
			<g:message code="edit.client"/></g:link></li>
	</ul>
</div>