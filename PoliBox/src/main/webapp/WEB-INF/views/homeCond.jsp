<%@page import="it.polito.ai.polibox.entity.Condivisione"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="it.polito.ai.polibox.entity.Utente"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Polibox</title>
	<link href="<c:url value='/resources/css/style.css' />" type="text/css" rel="stylesheet">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="//cdn.datatables.net/1.10.0/css/jquery.dataTables.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<c:url value='/resources/javascript/pageContextCond.js' />" ></script>
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
	
	<div class="col-xs-3">
		<h2>Ciao ${utente.nome}!</h2>
		<a href="/ai/home">Home</a><br>
		<a href="/ai/condivisioni">Condivisioni <c:if test="${fn:length(pending_sd_list) > 0}">(${fn:length(pending_sd_list)})</c:if></a><br>
		<a href="/ai/events">Eventi</a>
	</div>
	<div class="col-xs-9">
		<!-- Breadcrumbs -->
		<ol class="breadcrumb">
			<%
			String pathBreadcrumb = ((String) request.getAttribute("javax.servlet.forward.request_uri")).replace("%20", " ");
			if (pathBreadcrumb != null) {
				String[] pathElementsBreadcrumb = pathBreadcrumb.split("/");
				String pathUrlBreadcrumb = new String();
				for (int i=2; i<pathElementsBreadcrumb.length-1; i++) {
					pathUrlBreadcrumb += "/" + pathElementsBreadcrumb[i];
					if (pathElementsBreadcrumb[i].equals("Home")) {
						out.print("<li><a href='/ai/home'>home</a></li>");
					} else {
						out.print("<li><a href='/ai" + pathUrlBreadcrumb + "'>" + pathElementsBreadcrumb[i] + "</a></li>");
					}
				}
				out.print("<li class='active'>" + pathElementsBreadcrumb[pathElementsBreadcrumb.length-1] + "</li>");
			}
			%>
		</ol>
		
		<c:if test="${msgBool == true}">
			<div class="${msgClass}">
				<p>${msg}</p>
			</div>
		</c:if>
		<div id="msg" onclick="document.getElementById('msg').innerHTML='';">
			
		</div>
		<% if (!((Condivisione) session.getAttribute("condivisione")).getReadOnly()) { %>
		<p>
			<a data-toggle="modal" href="#divFormFileModal">Carica un file</a><br>
			<a data-toggle="modal" href="#divFormCartellaModal">Crea cartella</a>
		</p>
		<div id="readOnly" style="display:none">0</div>
		<% } else { %><div id="readOnly" style="display:none">1</div><%} %>
		
		<table class="sortable table-striped table-hover" id="list">
			<thead>
				<tr>
					<th></th>
					<th>Nome</th>
					<th>Tipo</th>
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
				if (list != null && list.length > 0) {
					for (int i=0; i<list.length; i++) {
						file = new java.io.File(pathDir + "\\" + list[i]);
				%>
				<tr class="draggable droppable">
					<% if (!((Condivisione) session.getAttribute("condivisione")).getReadOnly()) { %>
						<% if(file.isFile()) { %>
							<td><span class="glyphicon glyphicon-file"></span></td>
							<td><a id=<%= "file" + i %> class="filename_link" href="/ai/Home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>" draggable="true"><%= list[i] %></a></td>
							<td>File</td>
							<td><% out.print(dateFormat.format(new Date(file.lastModified()))); %></td>
						<% } else { %>
							<td><span class="glyphicon glyphicon-folder-close"></span></td>
							<td><a id=<%= "directory" + i %> class="filename_link" href="/ai/Home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>" draggable="true"><%= list[i] %></a></td>
							<td>Cartella</td>
							<td>--</td>
						<% } %>
					<% } else { %>
						<% if(file.isFile()) { %>
								<td><span class="glyphicon glyphicon-file"></span></td>
								<td><a id=<%= "file" + i %> class="filename_link" href="/ai/Home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>" draggable="true"><%= list[i] %></a></td>
								<td>File</td>
								<td><% out.print(dateFormat.format(new Date(file.lastModified()))); %></td>
							<% } else { %>
								<td><span class="glyphicon glyphicon-folder-close"></span></td>
								<td><a id=<%= "directory" + i %> class="filename_link" href="/ai/Home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>" draggable="true"><%= list[i] %></a></td>
								<td>Cartella</td>
								<td>--</td>
							<% } %>
						<% } %>
<%-- 					<td><% if (file.isFile()) out.print("File"); else out.print("Cartella"); %></td> --%>
<%-- 					<td><% if (file.isFile()) out.print(dateFormat.format(new Date(file.lastModified()))); %></td> --%>
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
						<form id="formCartellaModal" data-toggle="validator" action="/ai/creaCartella?cond=1" method="post">
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
						<form id="formFileModal" data-toggle="validator" action="/ai/fileUpload?cond=1" method="post" enctype="multipart/form-data" role="form">
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
		
		<!-- Modal form per l'eliminazione di un file o una directory -->
		<div class="modal fade" id="divFormElimina" tabindex="-1" role="dialog" aria-labelledby="modalCartellaLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalCartellaLabel">Eliminare il file/la cartella?</h2>
					</div>
					<div class="modal-body">
						<form data-toggle="validator" action="/ai/elimina?cond=1" method="post">
							<div class="row">
								<div class="form-group col-lg-9">
									Eliminare il file/la cartella <b id="fileDeleted"></b> selezionato/a? 
								</div>
							</div>
							<input type="hidden" name="path" id="path" />
							<input type="hidden" name="nomefile" id="nomefile" />
							<input class="btn btn-primary" type="submit" value="Elimina" />
							<button class="btn btn-primary" type="button" aria-hidden="true" data-dismiss="modal">Annulla</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal form per la rinomina di un file o una directory -->
		<div class="modal fade" id="divFormRinomina" tabindex="-1" role="dialog" aria-labelledby="modalCartellaLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalCartellaLabel">Rinomina file/cartella</h2>
					</div>
					<div class="modal-body">
						<form data-toggle="validator" action="/ai/rinomina?cond=1" method="post">
							<div class="row">
								<div class="form-group col-lg-12">
									<label for="newName" class="control-label">Rinomina in</label>
									<input name="newName" id="newName" type="text" required="true" />
								</div>
							</div>
							<input type="hidden" name="path" id="path2" />
							<input type="hidden" name="nomefile" id="nomefile2" />
							<input class="btn btn-primary" type="submit" value="Rinomina" />
							<button class="btn btn-primary" type="button" aria-hidden="true" data-dismiss="modal">Annulla</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal form per la condivisione di una direcotry -->
		<div class="modal fade" id="divFormCondividi" tabindex="-1" role="dialog" aria-labelledby="modalCartellaLabel" aria-hidden="true">
			<div class="modal-dialog modal-md">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalCartellaLabel">Condividi "<b id="fileSelected"></b>" con altre persone</h2>
					</div>
					<div class="modal-body">
						<form data-toggle="validator">
<!-- 							<div class="form-group col-lg-9"> -->
<!-- 								<input type="checkbox" id="allowInvitations" name="allowInvitations" value="true" checked onclick="changeValueCheckbox(this)"/> -->
<!-- 								<label for="allowInvitations">Consenti ai membri di invitare altre persone</label> -->
<!-- 							</div> -->
							<div class="form-group col-lg-9">
								<input type="checkbox" type="checkbox" id="allowChanges" name="allowChanges" checked/>
								<label for="allowChanges">Consenti ai membri di modificare la risorsa</label>
							</div>
	
							<table>
								<tr><input type="text" name="usersList" id="usersList" size="40" placeholder="Invita membri a questa cartella" style="padding: 7px; width: 558px"/></tr>
								<tr><textarea name="messsage" id="message" rows="3" cols="42" style="padding: 7px; width: 558px" placeholder="Aggiungi un messaggio"></textarea></tr>
							</table>
							
							<input type="hidden" name="path" id="path" />
							<input type="hidden" name="nomefile" id="nomefile" />
							
							<button class="btn btn-primary" type="button" aria-hidden="true" data-dismiss="modal" onclick="forwardForm()">Condividi cartella</button>
							<button class="btn btn-primary" type="button" aria-hidden="true" data-dismiss="modal">Annulla</button>
							
						</form>
					</div>
				</div>
			</div>	
		</div>
		
</body>
</html>