<div id="header" style="background:${request.currentClient.logo.backgroundColor}">
	<g:link controller="default">
	<div class="main_logo" style="
		background:url('${request.currentClient.logo.logoImagePath}') no-repeat 20px center;
		width:'${request.currentClient.logo.logoImageWidth}';">
	</div>
	</g:link>
	<div>
		<h1>${request.currentClient.logo.title}</h1>
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
		<h2>${request.currentClient.logo.description}</h2>
	</div>
</div>
