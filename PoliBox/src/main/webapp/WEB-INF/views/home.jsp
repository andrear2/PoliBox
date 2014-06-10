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
	    
		$('tr').each(function() {
			var $a = $(this).find("a");
			
			var menu = new Array();
			menu[0] = ["Opzioni cartella condivisa", "#"];
			menu[1] = ["Invita alla cartella", "#"];
			menu[2] = ["Condividi link", "#"];
			menu[3] = ["Scarica", "http://localhost:8080/ai/" + $a.attr('href')];
			menu[4] = ["Elimina", "#divFormElimina"];
			menu[5] = ["Rinomina", "#"];
			menu[6] = ["Sposta", "#"];
			menu[7] = ["Copia", "#"];
			menu[8] = ["Crea album", "#"];
			menu[9] = ["Versioni precedenti", "#"];

        	var id = $a.attr('id');
        	var isFile = /^file/.test(id);
        	
        	var contextMenu = '<div class="context-menu">';
        	for(var i = 0; i < menu.length; i++){
        		if(isFile && (i == 0 || i == 1 || i == 8)) continue;
        		if(!isFile && (i == 0 || i == 9)) continue;
        		contextMenu += '<div><a data-toggle="modal" href="' + menu[i][1] + '">' + menu[i][0] + '</a></div>';
        	}
        	contextMenu += '</div>';
        
        	$(contextMenu).insertAfter($a).hide();
        });
		
		$('tr').bind('contextmenu', function(e) {
		    
		    //alert("pippo");
		    var $a = $(this).find("a");
		    var contextMenu = $a.next();
		    
			document.getElementById("nomefile").value = $a.html();
			document.getElementById("fileDeleted").innerHTML = $a.html();

		    
		    $(this).parent().find('.context-menu').each( function() {
		    	$(this).css("display", "none");		
		    });
		    
		    contextMenu.css("display", "table");
		    
		    /*
		    contextMenu.find("div").each( function() {
		    		var $div = $(this);
		    		$div.css("display", "row");
		    		
		    		$div.find("a").each( function() {
		    			$(this).css("display", "table-cell");
		    		});
		    
		    });
		    */

		    e.preventDefault();
		});
		
		/*$('.context-menu').bind('click', function(e) {
			alert("pippo");
			var nomeFile = $(this).parent();
			alert(nomeFile);
		});*/
		$('html').bind('click', function(e) {
			$(this).find(".context-menu").each( function() {
				$(this).css("display", "none");		
			});	
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
		<!-- Breadcrumbs -->
		<ol class="breadcrumb">
			<%
			String pathBreadcrumb = (String) request.getAttribute("javax.servlet.forward.request_uri");
			if (pathBreadcrumb != null) {
				String[] pathElementsBreadcrumb = pathBreadcrumb.split("/");
				String pathUrlBreadcrumb = new String();
				for (int i=2; i<pathElementsBreadcrumb.length-1; i++) {
					pathUrlBreadcrumb += "/" + pathElementsBreadcrumb[i];
					out.print("<li><a href='/ai" + pathUrlBreadcrumb + "'>" + pathElementsBreadcrumb[i] + "</a></li>");
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
		
		<p>
			<a data-toggle="modal" href="#divFormFileModal">Carica un file</a><br>
			<a data-toggle="modal" href="#divFormCartellaModal">Crea cartella</a>
		</p>
		
		<table class="sortable" id="list">
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
				if (list != null && list.length > 0) {
					for (int i=0; i<list.length; i++) {
						file = new java.io.File(pathDir + "\\" + list[i]);
				%>
				<tr>
					<td><a id=<% if (file.isFile()) out.print("file" + i); else out.print("directory" + i); %> class="filename_link" href="/ai/home/<% if (pathUrl != null) out.print(pathUrl + "/"); %><%= list[i] %>" draggable="true"><%= list[i] %></a></td>
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
		
		<!-- Modal form per l'eliminazione di un file o una directory -->
		<div class="modal fade" id="divFormElimina" tabindex="-1" role="dialog" aria-labelledby="modalCartellaLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
	   			<div class="modal-content">
					<div class="modal-header">
					    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					    <h2 id="modalCartellaLabel">Eliminare il file?</h2>
					</div>
					<div class="modal-body">
						<form data-toggle="validator" action="/ai/elimina" method="post">
							<div class="row">
								<div class="form-group col-lg-9">
									Eliminare il file <b id="fileDeleted"></b> selezionato? 
								</div>
							</div>
							<input type="hidden" name="path" id="path" />
							<input type="hidden" name="nomefile" id="nomefile" />
							<input class="btn btn-primary" type="submit" value="Elimina" />
							<button class="btn btn-primary" type="button" aria-hidden="true" data-dismiss="modal">Annulla</button>
						</form>
						<script type="text/javascript">
							document.getElementById("path").value = document.URL;
						</script>
					</div>
				</div>
			</div>
	</div>
</body>
</html>