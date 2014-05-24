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
	<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$(function() {
		var nome = $("#nome"),
			cognome = $("#cognome"),
			allFields = $([]).add(nome).add(cognome),
			tips = $(".validateTips");
		function updateTips(t) {
		    tips.text(t)
		        .addClass("ui-state-highlight");
		    setTimeout(function() {
		        tips.removeClass("ui-state-highlight", 1500);
		    }, 500);
		}
		function checkLength(o, n, min, max) {
		    if (o.val().length > max || o.val().length < min) {
			    o.addClass("ui-state-error");
			    updateTips("La lunghezza del campo " + n + " deve essere compresa tra " + min + " e " + max + ".");
			    return false;
		    } else {
		        return true;
		    }
	    }
		$("#cambianomemodal").dialog({
		    autoOpen: false,
		    height: 300,
		    width: 350,
		    modal: true,
		    buttons: {
				"Cambia nome": function() {
					var bValid = true;
			        allFields.removeClass("ui-state-error");
			        bValid = bValid && checkLength(nome, "nome", 1, 20);
			        bValid = bValid && checkLength(cognome, "cognome", 1, 20);
			        if (bValid) {
						$("#nuovonome").html("Il tuo nuovo nome è " + nome.val() + " " + cognome.val());
						$("form[name='cambianomeform']").submit();
						$(this).dialog("close");
			        }
				},
				"Annulla": function() {
				    $(this).dialog("close");
				}
		    },
		    close: function() {
		   		allFields.val("");
		    }
		});
		$("#modaltrigger").click(function() {
	    	$("#cambianomemodal").dialog("open");
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
		<a href="#cambianomemodal" id="modaltrigger">Cambia nome</a>
	</div>
	
	<div id="cambianomemodal" title="Cambia nome">
		<p class="validateTips">Tutti i campi sono obbligatori.</p>
		<form name="cambianomeform" action="account/cambiaNome" method="post" role="form">
			<div class="row">
				<div class="form-group col-lg-4">
					<label for="nome">Nome</label>
					<input type="text" id="nome" class="text ui-widget-content ui-corner-all" />
<%-- 		    		<form:input path="nome" class="form-control" id="nome" placeholder="Nome" /> --%>
<%-- 		    		<form:errors path="nome" cssClass="error"></form:errors> --%>
				</div>
			</div>
			<div class="row">
				<div class="form-group col-lg-4">
					<label for="cognome">Cognome</label>
					<input type="text" id="cognome" class="text ui-widget-content ui-corner-all" />
<%-- 		    		<form:input path="cognome" class="form-control" id="cognome" placeholder="Cognome" /> --%>
<%-- 		    		<form:errors path="cognome" cssClass="error"></form:errors> --%>
				</div>
			</div>
<!-- 			<input class="btn btn-primary" type="submit" value="Cambia nome" /> -->
		</form>
	</div>
	
	<div id="nuovonome">
	</div>
</body>
</html>