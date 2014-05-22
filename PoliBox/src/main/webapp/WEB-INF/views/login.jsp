<%@ page language="java" contentType="text/html"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html">
	<title>PoliBox - Login</title>
	<link href="<c:url value="/resources/css/style.css" />" type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="index">PoliBox</a>
	    </div>
	  </div>
	</nav>
	
	<h1>Login</h1>
	<c:if test="${error == true}">
		<p class="error">Email o password errate</p>
	</c:if>
	<form:form commandName="utente" method="post" role="form">
		<div class="form-group">
			<label for="email">Email</label>
    		<form:input path="email" type="email" class="form-control" id="email" placeholder="Email" />
    		<form:errors path="email" cssClass="error"></form:errors>
		</div>
		<div class="form-group">
			<label for="password">Password</label>
    		<form:password path="password" class="form-control" id="password" placeholder="Password" />
    		<form:errors path="password" cssClass="error"></form:errors>
		</div>
		<input class="btn btn-primary" type="submit" value="Login" />
	</form:form>
	<br>
	<a href="index" class="btn btn-default" role="button">Indietro</a>
</body>
</html>