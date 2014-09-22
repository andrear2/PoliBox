<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Polibox - Account</title>
	<link href='<c:url value="/resources/css/style.css" />' type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.12.0/jquery.validate.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
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
	            <li id="nome">${utente.nome} ${utente.cognome}</li>
	            <li id="email">${utente.email}</li>
	            <li id="progBar">
	            	<c:if test = "${(totByteFCond + totByteFReg < 1000)}"> <fmt:formatNumber value="${totByteFCond+totByteFReg}" maxFractionDigits="1" /> B </c:if>
					<c:if test = "${(totByteFCond + totByteFReg >= 1000) && (totByteFCond + totByteFReg < 1000000)}"> <fmt:formatNumber value="${ (totByteFCond+totByteFReg)/1000}" maxFractionDigits="1" /> KB </c:if>
					<c:if test = "${(totByteFCond + totByteFReg >= 1000000) && (totByteFCond + totByteFReg < 1000000000)}"> <fmt:formatNumber value="${(totByteFCond+totByteFReg)/1000000}" maxFractionDigits="1" /> MB </c:if> 
					<c:if test = "${totByteFCond + totByteFReg >= 1000000000}"> <fmt:formatNumber value="${(totByteFCond+totByteFReg)/1000000000}" maxFractionDigits="1" /> GB </c:if>
					di 5 MB in uso
	            	<div class="progress">
	            		<div class="progress-bar" role="progressbar" aria-valuenow="${(totByteFReg + totByteFCond)/50000}" aria-valuemin="0" aria-valuemax="100" style="width: ${(totByteFReg + totByteFCond)/50000}%;">
    						<span class="sr-only">${(totByteFReg + totByteFCond)/50000}% Complete</span>
  						</div>
					</div>
				</li>
	            <li class="divider"></li>
	            <li><a href="<c:url value='/resources/client/Polibox.exe' />">Installa</a></li>
	            <li><a href="/ai/account">Profilo</a></li>
	            <li><a href="/ai/logout">Logout</a></li>
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
<!-- 	    <li><a href="#sicurezza" data-toggle="tab">Sicurezza</a></li> -->
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
				<tr>
					<td>Password</td>
					<td><a data-toggle="modal" href="#divFormPasswordModal">Modifica password</a></td>
				</tr>
			</table>
			<div class="tab-pane" id="account">
		<h3>Spazio utilizzato in Polibox</h3>
		<div class="progress">
  			<div class="progress-bar" style="width: ${totByteFReg/50000}%">
    			<span class="sr-only">${totByteFReg/50000}% Complete</span>
  			</div>
  			<div class="progress-bar progress-bar-info" style="width: ${totByteFCond/50000}%">
    			<span class="sr-only">${totByteFCond/50000}% Complete</span>
  			</div>
		</div>
		<div> 
			<c:if test = "${(totByteFCond + totByteFReg < 1000)}"> <fmt:formatNumber value="${totByteFCond+totByteFReg}" maxFractionDigits="1" /> B </c:if>
					<c:if test = "${(totByteFCond + totByteFReg >= 1000) && (totByteFCond + totByteFReg < 1000000)}"> <fmt:formatNumber value="${ (totByteFCond+totByteFReg)/1000}" maxFractionDigits="1" /> KB </c:if>
					<c:if test = "${(totByteFCond + totByteFReg >= 1000000) && (totByteFCond + totByteFReg < 1000000000)}"> <fmt:formatNumber value="${(totByteFCond+totByteFReg)/1000000}" maxFractionDigits="1" /> MB </c:if> 
					<c:if test = "${totByteFCond + totByteFReg >= 1000000000}"> <fmt:formatNumber value="${(totByteFCond+totByteFReg)/1000000000}" maxFractionDigits="1" /> GB </c:if>
			di 5 MB in uso  
			</div> 
			<div class="progress-bar-legend-normal">leg</div> 
			<p> File regolari 
				<c:if test = "${(totByteFReg < 1000)}"> <fmt:formatNumber value="${totByteFReg}" maxFractionDigits="1" /> B </c:if>
					<c:if test = "${(totByteFReg >= 1000) && (totByteFReg < 1000000)}"> <fmt:formatNumber value="${ (totByteFReg)/1000}" maxFractionDigits="1" /> KB </c:if>
					<c:if test = "${(totByteFReg >= 1000000) && (totByteFReg < 1000000000)}"> <fmt:formatNumber value="${(totByteFReg)/1000000}" maxFractionDigits="1" /> MB </c:if> 
					<c:if test = "${totByteFReg >= 1000000000}"> <fmt:formatNumber value="${(totByteFReg)/1000000000}" maxFractionDigits="1" /> GB </c:if>
			</p>
			<div class="progress-bar-legend-shared">leg</div>
			<p> File condivisi 
				<c:if test = "${(totByteFCond < 1000)}"> <fmt:formatNumber value="${totByteFCond}" maxFractionDigits="1" /> B </c:if>
					<c:if test = "${(totByteFCond >= 1000) && (totByteFCond < 1000000)}"> <fmt:formatNumber value="${ (totByteFCond)/1000}" maxFractionDigits="1" /> KB </c:if>
					<c:if test = "${(totByteFCond >= 1000000) && (totByteFCond < 1000000000)}"> <fmt:formatNumber value="${(totByteFCond)/1000000}" maxFractionDigits="1" /> MB </c:if> 
					<c:if test = "${totByteFCond >= 1000000000}"> <fmt:formatNumber value="${(totByteFCond)/1000000000}" maxFractionDigits="1" /> GB </c:if>
			</p>
			<div class="progress-bar-legend-free">leg</div>
			<p> Spazio inutilizzato 
				<c:if test = "${((5000000 - totByteFCond - totByteFReg) < 1000)}"> <fmt:formatNumber value="${5000000 - totByteFCond-totByteFReg}" maxFractionDigits="1" /> B </c:if>
					<c:if test = "${((5000000 - totByteFCond - totByteFReg) >= 1000) && ((5000000 - totByteFCond - totByteFReg) < 1000000)}"> <fmt:formatNumber value="${ (5000000 - totByteFCond-totByteFReg)/1000}" maxFractionDigits="1" /> KB </c:if>
					<c:if test = "${((5000000 - totByteFCond - totByteFReg) >= 1000000) && ((5000000 - totByteFCond - totByteFReg) < 1000000000)}"> <fmt:formatNumber value="${(5000000 - totByteFCond-totByteFReg)/1000000}" maxFractionDigits="1" /> MB </c:if> 
					<c:if test = "${(5000000 - totByteFCond - totByteFReg) >= 1000000000}"> <fmt:formatNumber value="${((5000000 - totByteFCond-totByteFReg))/1000000000}" maxFractionDigits="1" /> GB </c:if>
			</p>
		</div>
		
		<div>
		<h3>Dispositivi connessi: ${numDisp}</h3>
		</div>
	</div>
			
<!-- 			<p>Spazio utilizzato in Polibox</p> -->
<!-- 			<div class="progress"> -->
<%-- 	  			<div class="progress-bar" style="width: ${totByteFReg/50000}%"> --%>
<%-- 	    			<span class="sr-only">${totByteFReg/50000}% Complete</span> --%>
<!-- 	  			</div> -->
<%-- 	  			<div class="progress-bar progress-bar-info" style="width: ${totByteFCond/50000}%"> --%>
<%-- 	    			<span class="sr-only">${totByteFCond/50000}% Complete</span> --%>
<!-- 	  			</div> -->
<!-- 			</div> -->
<!-- 			<p>  -->
<%-- 				<c:if test = "${totByteFCond + totByteFReg >= 1000 && totByteFCond + totByteFReg < 1000000}"> ${(totByteFCond+totByteFReg)/1000} KB </c:if> --%>
<%-- 				<c:if test = "${totByteFCond + totByteFReg >= 1000000 && totByteFCond + totByteFReg < 1000000000}"> ${(totByteFCond+totByteFReg)/1000000} MB </c:if>  --%>
<%-- 				<c:if test = "${totByteFCond + totByteFReg >= 1000000000}"> ${(totByteFCond+totByteFReg)/1000000000} GB </c:if> --%>
<!-- 				di 5 MB in uso  -->
<!-- 			</p> -->
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
		
<!-- 		<div class="tab-pane" id="sicurezza"> -->

			
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
		 <!--</div> tab-pane sicurezza -->
	</div> <!-- tab-content -->
	
	
	
</body>
</html>