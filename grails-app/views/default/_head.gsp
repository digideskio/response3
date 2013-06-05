<div id="header">
	<div class="main_logo">
		<g:img dir="images" file="redpill_logo.png" height="60"/>
	</div>
	<div>
		<h1>
			<g:link controller="default">
				<g:message code="response3" default="Response3"/>
			</g:link>
		</h1>
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
		<h2><g:message code="issue.tracker" default="Issue Tracker"/></h2>
	</div>
</div>
