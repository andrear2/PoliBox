<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>PoliBox</title>
	<link href='<c:url value="/resources/css/style.css" />' type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/ai/home">PoliBox</a>
	    </div>
	  </div>
	</nav>
	<h1>Benvenuto su PoliBox</h1>
	<p><a class="btn btn-primary" href="/ai/registration">Registrati</a><br>
	oppure <a href="/ai/login">Accedi</a></p>
</body>
</html>
