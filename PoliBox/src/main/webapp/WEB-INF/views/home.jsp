<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="it.polito.ai.polibox.entity.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Polibox</title>
	<link href="<c:url value='/resources/css/style.css' />" type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="//cdn.datatables.net/1.10.0/css/jquery.dataTables.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
	    $('.sortable').dataTable({
	    	paging: false,
	    	"bInfo": false,
	    	"order": [[0, "asc"]],
	    	"language": {
	    		"search": "Cerca:",
	    		"zeroRecords": "Questa cartella è vuota"
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
	
	<div class="col-xs-3">
		<h2>Benvenuto ${utente.nome} ${utente.cognome}!</h2>
	</div>
	<div class="col-xs-9">
		<h2>Benvenuto ${utente.nome} ${utente.cognome}!</h2>
		<c:if test="${msgBool == true}">
			<div class="${msgClass}">
				<p>${msg}</p>
			</div>
		</c:if>
		
		<p>
			<a data-toggle="modal" href="#divFormFileModal">Carica un file</a><br>
			<a data-toggle="modal" href="#divFormCartellaModal">Crea cartella</a>
		</p>
		
		<table class="sortable">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Ultima modifica</th>
				</tr>
			</thead>
			<tbody>
				<%
				Utente utente = (Utente) request.getAttribute("utente");
				String pathDir = (String) request.getAttribute("pathDir");
				String pathUrl = (String) request.getAttribute("pathUrl");
				if (pathDir == null) {
					pathDir = utente.getHome_dir();
				}
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				java.io.File file;
				java.io.File dir = new java.io.File(pathDir);
				String[] list = dir.list();
				if (list.length > 0) {
					for (int i=0; i<list.length; i++) {
						file = new java.io.File(pathDir + "\\" + list[i]);
				%>
				<tr>
					<td><a href="/ai/home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>"><%= list[i] %></a></td>
					<td><% if (file.isFile()) out.print(dateFormat.format(new Date(file.lastModified()))); %></td>
				</tr>
				<%
					}
				}
				%>
			</tbody>
		</table>
		
		<!-- Modal form per la creazione di una cartella -->
		<div class="modal fade" id="divFormCartellaModal" tabindex="-1" role="dialog" aria-labelledby="modalCartellaLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalCartellaLabel">Crea cartella</h2>
					</div>
					<div class="modal-body">
						<form id="formCartellaModal" data-toggle="validator" action="/ai/creaCartella" method="post">
							<div class="row">
								<div class="form-group col-lg-9">
									<label for="nome" class="control-label">Nome</label>
									<input type="text" id="nome" name="nome" class="form-control" placeholder="Nome cartella" required />
								</div>
							</div>
							<input type="hidden" name="pathCartella" id="pathCartella" />
							<input class="btn btn-primary" type="submit" value="Crea cartella" />
						</form>
						<script type="text/javascript">
							document.getElementById("pathCartella").value = document.URL;
						</script>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal form per l'upload di file -->
		<div class="modal fade" id="divFormFileModal" tabindex="-1" role="dialog" aria-labelledby="modalFileLabel" aria-hidden="true">
			<div class="modal-dialog modal-md">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalFileLabel">Carica file</h2>
					</div>
					<div class="modal-body">
						<form id="formFileModal" data-toggle="validator" action="/ai/fileUpload" method="post" enctype="multipart/form-data" role="form">
							<div class="row">
								<div class="form-group col-lg-12">
									<label for="files" class="control-label">File</label>
									<input name="files" type="file" class="form-control" id="files" multiple="true" />
								</div>
							</div>
							<input type="hidden" name="pathFile" id="pathFile" />
							<input class="btn btn-primary" type="submit" value="Carica" />
						</form>
						<script type="text/javascript">
							document.getElementById("pathFile").value = document.URL;
						</script>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>