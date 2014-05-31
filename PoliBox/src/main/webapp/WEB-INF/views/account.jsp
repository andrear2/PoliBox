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
	<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.12.0/jquery.validate.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$().ready(function() {
		$("#formEmailModal").validate({
			rules: {
				email: {
		            required: true
		        },
		        confermaEmail: {
		            required: true,
		            equalTo: "#email"
		        },
		        password: {
		            required: true,
		            minlength: 6,
		            passwordCheck: true
		        }
		    },
		    messages: {
		    	email: {
		        	required: "Questo campo è obbligatorio"
		    	},
		        confermaEmail: {
		        	required: "Questo campo è obbligatorio",
		            equalTo: "Gli indirizzi email non corrispondono"
		        },
		        password: {
		        	required: "Questo campo è obbligatorio",
		        	minlength: "La password deve contenere almeno 6 caratteri",
		        	passwordCheck: "La password inserita è errata"
		    	}
		    }
		});
		$("#formPasswordModal").validate({
			rules: {
				vecchiaPassword: {
		            required: true,
		            minlength: 6,
		            passwordCheck: true
		        },
				nuovaPassword: {
		            required: true,
		            minlength: 6,
		        }
			},
		    messages: {
		        vecchiaPassword: {
		        	required: "Questo campo è obbligatorio",
		        	minlength: "La password deve contenere almeno 6 caratteri",
		        	passwordCheck: "La password inserita è errata"
		    	},
		        nuovaPassword: {
		        	required: "Questo campo è obbligatorio",
		        	minlength: "La password deve contenere almeno 6 caratteri"
		    	}
		    }
		});
	});
	jQuery.validator.addMethod('passwordCheck', function(password) {
		if (password != '${utente.password}') {
			return false;
		}
		return true;
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
	      <a class="navbar-brand" href="/ai/home">PoliBox</a>
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
	
	<h1>Impostazioni</h1>
	<c:if test="${msgBool == true}">
		<p class="${msgClass}">${msg}</p>
	</c:if>
	
	<!-- Nav tabs -->
	<ul class="nav nav-tabs">
		<li class="active"><a href="#profilo" data-toggle="tab">Profilo</a></li>
	    <li><a href="#sicurezza" data-toggle="tab">Sicurezza</a></li>
	</ul>
	
	<!-- Tab panes -->
	<div class="tab-content">
		<div class="tab-pane active" id="profilo">
			<table>
				<tr>
					<td>${utente.nome} ${utente.cognome}</td>
					<td><a data-toggle="modal" href="#divFormNomeModal">Cambia nome</a></td>
				</tr>
				<tr>
					<td>${utente.email}</td>
					<td><a data-toggle="modal" href="#divFormEmailModal">Modifica email</a></td>
				</tr>
			</table>
			
			<!-- Modal form per il cambio nome -->
			<div class="modal fade" id="divFormNomeModal" tabindex="-1" role="dialog" aria-labelledby="modalNomeLabel" aria-hidden="true">
				<div class="modal-dialog modal-sm">
		   			<div class="modal-content">
						<div class="modal-header">
						    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						    <h2 id="modalNomeLabel">Cambia nome</h2>
						</div>
						<div class="modal-body">
							<form id="formNomeModal" data-toggle="validator" action="cambiaNome" method="post">
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="nome" class="control-label">Nome</label>
										<input type="text" id="nome" name="nome" class="form-control" placeholder="Nome" maxlength="20" required />
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
			
			<!-- Modal form per la modifica dell'email -->
			<div class="modal fade" id="divFormEmailModal" tabindex="-1" role="dialog" aria-labelledby="modalEmailLabel" aria-hidden="true">
				<div class="modal-dialog modal-sm">
		   			<div class="modal-content">
						<div class="modal-header">
						    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						    <h2 id="modalEmailLabel">Modifica email</h2>
						</div>
						<div class="modal-body">
							<form id="formEmailModal" data-toggle="validator" action="cambiaEmail" method="post">
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="email" class="control-label">Email</label>
										<input type="email" id="email" name="email" class="form-control" placeholder="Nuova email" maxlength="50" required />
									</div>
								</div>
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="confermaEmail" class="control-label">Conferma email</label>
										<input type="email" id="confermaEmail" name="confermaEmail" class="form-control" placeholder="Conferma email" maxlength="50" required />
										<div class="help-block with-errors"></div>
									</div>
								</div>
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="password" class="control-label">Password</label>
										<input type="password" id="password" name="password" class="form-control" placeholder="Password di PoliBox" maxlength="20" required />
									</div>
								</div>
								<input class="btn btn-primary" type="submit" id="submitFormEmail" value="Modifica email" />
							</form>
						</div>
					</div>
				</div>
			</div>
		</div> <!-- tab-pane profilo -->
		
		<div class="tab-pane" id="sicurezza">
			<p>
				Password
				<a data-toggle="modal" href="#divFormPasswordModal">Modifica password</a>
				<a href="passwordDimenticata">Password dimenticata?</a>
			</p>
			
			<!-- Modal form per la modifica della password -->
			<div class="modal fade" id="divFormPasswordModal" tabindex="-1" role="dialog" aria-labelledby="modalPasswordLabel" aria-hidden="true">
				<div class="modal-dialog modal-sm">
		   			<div class="modal-content">
						<div class="modal-header">
						    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						    <h2 id="modalPasswordLabel">Modifica password</h2>
						</div>
						<div class="modal-body">
							<form id="formPasswordModal" data-toggle="validator" action="cambiaPassword" method="post">
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="vecchiaPassword" class="control-label">Vecchia password</label>
										<input type="password" id="vecchiaPassword" name="vecchiaPassword" class="form-control" placeholder="Vecchia password" maxlength="20" required />
									</div>
								</div>
								<div class="row">
									<div class="form-group col-lg-9">
										<label for="nuovaPassword" class="control-label">Nuova password</label>
										<input type="password" id="nuovaPassword" name="nuovaPassword" class="form-control" placeholder="Nuova password" maxlength="20" required />
									</div>
								</div>
								<input class="btn btn-primary" type="submit" value="Modifica password" />
							</form>
						</div>
					</div>
				</div>
			</div>
		</div> <!-- tab-pane sicurezza -->
	</div> <!-- tab-content -->
</body>
</html>