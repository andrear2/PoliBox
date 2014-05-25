<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Polibox - Account</title>
	<link href='<c:url value="/resources/css/style.css" />' type="text/css" rel="stylesheet">
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
	
	<div>
		<h1>Impostazioni</h1>
		${utente.nome} ${utente.cognome}
		<a data-toggle="modal" href="#divFormModal">Cambia nome</a>
	</div>
	
	<div class="modal fade" id="divFormModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
   			<div class="modal-content">
				<div class="modal-header">
				    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				    <h2 id="modalLabel">Cambia nome</h2>
				</div>
				<div class="modal-body">
					<form id="formModal" class="form-horizontal" data-toggle="validator" action="cambiaNome" method="post">
						<div class="row">
							<div class="form-group col-lg-9">
								<label for="nome" class="control-label">Nome</label>
								<input type="text" id="nome" name="nome" data-toggle="validator" class="form-control" placeholder="Nome" maxlength="20" required />
							</div>
						</div>
						<div class="row">
							<div class="form-group col-lg-9">
								<label for="cognome" class="control-label">Cognome</label>
								<input type="text" id="cognome" name="cognome" class="form-control" placeholder="Cognome" maxlength="20" required />
							</div>
						</div>
						<input class="btn btn-primary" type="submit" value="Cambia nome" />
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<div id="nuovonome">
	</div>
</body>
</html>