<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Polibox - Condivisioni</title>
	<link href='<c:url value="/resources/css/style.css" />' type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.12.0/jquery.validate.min.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<c:url value='/resources/javascript/websocket.js' />" ></script>
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
	            <li class="divider"></li>
	            <li><a href="/ai/account">Profilo</a></li>
	            <li><a href="/ai/logout">Logout</a></li>
	          </ul>
	        </li>
	      </ul>
	    </div>
	  </div>
	</nav>
	
	<div class="col-xs-3">
		<h2>Ciao ${utente.nome}!</h2>
		<a href="/ai/condivisioni">Condivisioni <c:if test="${fn:length(pending_sd_list) > 0}">(${fn:length(pending_sd_list)})</c:if></a>
	</div>
	
	<div class="col-xs-9">
		<h3>Condivisioni in sospeso</h3>
		<c:forEach var="dir" items="${pending_sd_list}">
			<div>
				<c:set var="splitDirPath" value="${fn:split(dir.key.dirPath, '\\\\')}"/>
				<c:set var="dirPath" value="${splitDirPath[fn:length(splitDirPath)-1]}"/>
				<p>Sei stato invitato da <i>${dir.value.nome} ${dir.value.cognome}</i> a condividere la cartella <b>${dirPath}</b></p>
				<div style='display:none;' id='c_id' name='c_id'>${dir.key.id}</div>
				<button class='btn btn-success' onclick='accept_cond2()'>Accetta</button>
				<button class='btn btn-danger' onclick='refuse_cond2()'>Rifiuta</button>
			</div>
		</c:forEach>
		
		<hr>
		
		<h3>Cartelle condivise</h3>
		<c:forEach var="dir" items="${sd_list}">
			<div>
				<c:set var="splitDirPath" value="${fn:split(dir.key.dirPath, '\\\\')}"/>
				<c:set var="dirPath" value="${splitDirPath[fn:length(splitDirPath)-1]}"/>
				<p>La cartella <b>${dirPath}</b> è condivisa con <i>${dir.value.nome} ${dir.value.cognome}</i></p>
				<div style='display:none;' id='c_id' name='c_id'>${dir.key.id}</div>
				<button class='btn btn-danger' onclick='refuse_cond3()'>Rimuovi condivisione</button>
			</div>
		</c:forEach>
	</div>
</body>
</html>