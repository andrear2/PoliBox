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
</head>
<body>
	<h1>Login</h1>
	<c:if test="${error == true}">
		<p class="error">Email o password errate</p>
	</c:if>
	<form:form commandName="utente" method="post">
		<table>
			<tr><td>Email:</td><td><form:input path="email" /></td><td><form:errors path="email" cssClass="error"></form:errors></td></tr>
			<tr><td>Password:</td><td><form:password path="password" /></td><td><form:errors path="password" cssClass="error"></form:errors></td></tr>
			<tr><td colspan="2"><input type="submit" value="Login" /></td></tr>
		</table>
	</form:form>
</body>
</html>