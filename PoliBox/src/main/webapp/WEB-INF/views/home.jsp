<%@page import="it.polito.ai.polibox.entity.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Polibox</title>
	<link href="<c:url value='/resources/css/style.css' />" type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="home">PoliBox</a>
	    </div>
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav navbar-right">
	        <li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown">${utente.nome} ${utente.cognome} <b class="caret"></b></a>
	          <ul class="dropdown-menu">
	            <li>${utente.nome} ${utente.cognome}</li>
	            <li>${utente.email}</li>
	            <li class="divider"></li>
	            <li><a href="account">Profilo</a></li>
	            <li><a href="logout">Logout</a></li>
	          </ul>
	        </li>
	      </ul>
	    </div>
	  </div>
	</nav>
	
	<div class="col-xs-3">
		<h2>Benvenuto ${utente.nome} ${utente.cognome}!</h2>
	</div>
	<div class="col-xs-9">
		<h2>Benvenuto ${utente.nome} ${utente.cognome}!</h2>
		<c:if test="${msgBool == true}">
			<p class="${msgClass}">${msg}</p>
		</c:if>
		
		<a href="fileUpload">Carica un file</a>
		
		<table>
			<tr><th>Nome</th></tr>
			<%
			Utente utente = (Utente) request.getAttribute("utente");
			java.io.File file;
			java.io.File dir = new java.io.File(utente.getHome_dir());
			String[] list = dir.list();
			if (list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					file = new java.io.File(utente.getHome_dir() + "\\" + list[i]);
			%>
			<tr><td><a href="home/<%= list[i] %>"><%= list[i] %></a></td></tr>
			<%
				}
			}
			%>
		</table>
	</div>
</body>
</html>