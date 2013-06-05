<head>
<meta name='layout' content='main' />
<title><g:message code="springSecurity.denied.title" /></title>
</head>

<body>
<div class='body'>
	<h1><g:message code="access.is.denied" /></h1>
	<sec:ifLoggedIn>
		<h2><g:message code="redirecting.to.main.page.in.3.seconds" /></h2>
		<script type="text/javascript">
			setTimeout(function(){
				document.location = "${createLink(controller:"default")}";
				}, 3000);
		</script>
	</sec:ifLoggedIn>
</div>
</body>
