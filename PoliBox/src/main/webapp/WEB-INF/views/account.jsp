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
	<script type="text/javascript">
	$(function() {
		$('#cambianomesubmit').click(function() {
		    if ($('#nome').val() === "") {
		        $('#nome').next('.help-inline').show();
		        return false;
		    } else if ($('#cognome').val() === "") {
		        $('#cognome').next('.help-inline').show();
		        return false;
		    } else {
		        $('#cambianomeform').submit();
		        return true;
		    }
		});
	});
	</script>
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
	      <a class="navbar-brand" href="index">PoliBox</a>
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
		<a data-toggle="modal" href="#cambianomemodal">Cambia nome</a>
	</div>
	
	<div class="modal fade" id="cambianomemodal" tabindex="-1" role="dialog" aria-labelledby="cambianomelabel" aria-hidden="true">
		<div class="modal-dialog">
   			<div class="modal-content">
				<div class="modal-header">
				    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				    <h2 id="cambianomelabel">Cambia nome</h2>
				</div>
				<div class="modal-body">
					<form id="cambianomeform" action="account/cambiaNome" method="POST">
						<label for="nome">Nome</label>
						<input type="text" id="nome" />
						<span class="hide help-inline">La lunghezza del campo nome deve essere compresa tra 1 e 20</span>
						<label for="cognome">Cognome</label>
						<input type="text" id="cognome" />
						<span class="hide help-inline">La lunghezza del campo cognome deve essere compresa tra 1 e 20</span>
					</form>
				</div>
				<div class="modal-footer">
			    	<button class="btn" data-dismiss="modal" aria-hidden="true">Annulla</button>
			    	<button class="btn btn-primary" data-dismiss="modal" id="cambianomesubmit">Cambia nome</button>
			    </div>
			</div>
		</div>
	</div>
	
	<div id="nuovonome">
	</div>
</body>
</html>