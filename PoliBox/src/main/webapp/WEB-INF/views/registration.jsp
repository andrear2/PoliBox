<%@ page language="java" contentType="text/html"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html">
	<title>PoliBox - Registrazione</title>
	<link href="<c:url value="/resources/css/style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<form:form commandName="utente" method="post">
		<table>
			<tr><td>Nome:</td><td><form:input path="nome" /></td><td><form:errors path="nome" cssClass="error"></form:errors></td></tr>
			<tr><td>Cognome:</td><td><form:input path="cognome" /></td><td><form:errors path="cognome" cssClass="error"></form:errors></td></tr>
			<tr><td>Email:</td><td><form:input path="email" /></td><td><form:errors path="email" cssClass="error"></form:errors></td></tr>
			<tr><td>Password:</td><td><form:password path="password" /></td><td><form:errors path="password" cssClass="error"></form:errors></td></tr>
			<tr><td colspan="2"><input type="submit" value="Registrati" /></td></tr>
		</table>
	</form:form>
</body>
</html>