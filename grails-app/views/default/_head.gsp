
<div id="header" style="background:${request.currentClient.header.backgroundColor}">
	<cache:block key="${request.currentClient.id}">
		<g:link controller="default">
			<header:logo/>
		</g:link>
	</cache:block>
	<div>
		<cache:block key="${request.currentClient.id}">
			<header:h1/>
		</cache:block>
		<sec:ifLoggedIn>
			<ul id="nav">
				<li class="search-icon">
				  <g:link controller="search">
					<g:message code="search" default="SEARCH"/>
				  </g:link>
				</li>
				<li class="account-icon">
				  <g:link controller="account">
						<g:message code="account" default="ACCOUNT"/>
				  </g:link>
				</li>
				<sec:ifAllGranted roles="ROLE_ADMIN">
					<li class="administration-icon">
					   <g:link controller="administration">
						 <g:message code="administration" default="ADMINISTRATION"/>
					   </g:link>
					</li>
				</sec:ifAllGranted>
				<li class="signoff-icon">
				  <g:link controller="logout">
						<g:message code="sign.off" default="SIGN OFF"/>
				  </g:link>
				</li>
			</ul>
		</sec:ifLoggedIn>
		<cache:block key="${request.currentClient.id}">
			<header:h2/>
		</cache:block>
	</div>
</div>
