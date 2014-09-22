<%@page import="it.polito.ai.polibox.dao.UtenteDAO"%>
<%@page import="it.polito.ai.polibox.dao.CondivisioneDAO"%>
<%@page import="java.awt.print.Printable"%>
<%@page import="java.io.LineNumberReader"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="it.polito.ai.polibox.entity.Utente"%>
<%@page import="it.polito.ai.polibox.controller.AppCtxProv" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
		
		<c:if test="${msgBool == true}">
			<div class="${msgClass}">
				<p>${msg}</p>
			</div>
		</c:if>
		
		
		<table class="sortable">
			<thead>
				<tr>
					<th>Tipo</th>
					<th>Risorsa</th>
					<th>Data</th>
					<th>Autore della modifica</th>
				</tr>
			</thead>
			<tbody>
				<%
				Utente utente = (Utente) request.getAttribute("utente");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String path = utente.getHome_dir()+"\\log.txt";
				java.io.FileReader fr = new java.io.FileReader (path);
				LineNumberReader lnr = new LineNumberReader (fr);
				String line;
				while ((line = lnr.readLine ()) != null)
				{
				    String[] sub = line.split(":");
				    sub[3] = sub[3].replace("\\", "/");
				    String uName = new String();
				    if (sub.length == 9) {				    	
				    	uName = sub[7] +" "+ sub[8]; 
				    } else {
				    	uName = "Mio";
				    }
				    	
				%>
				<tr>
					<td><%
					if (sub[2].equals("DD")) {
						out.print("<span class='glyphicon glyphicon-trash'></span>");
						out.print("<span class='glyphicon glyphicon-folder-close'></span>");
					} else if (sub[2].equals("ND")) {
						out.print("<span class='glyphicon glyphicon-plus'></span>");
						out.print("<span class='glyphicon glyphicon-folder-close'></span>");
					}
					else if (sub[2].equals("NF")) {
						out.print("<span class='glyphicon glyphicon-plus'></span>");
						out.print("<span class='glyphicon glyphicon-file'></span>");
					}
					else if (sub[2].equals("DF")) {
						out.print("<span class='glyphicon glyphicon-trash'></span>");
						out.print("<span class='glyphicon glyphicon-file'></span>");
					}
					else if (sub[2].equals("RCA")) {
						out.print("<span class='glyphicon glyphicon-plus'></span>");
						out.print("<span class='glyphicon glyphicon-user'></span>");
					}
					else if (sub[2].equals("RCI")) {
						out.print("<span class='glyphicon glyphicon-envelope'></span>");
					}
					else if (sub[2].equals("RCR")) {
						out.print("<span class='glyphicon glyphicon-envelope'></span>");
					}
					else if (sub[2].equals("RCREF")) {
						out.print("<span class='glyphicon glyphicon-minus'></span>");
						out.print("<span class='glyphicon glyphicon-user'></span>");
					}
					else if (sub[2].equals("DELC")) {
						out.print("<span class='glyphicon glyphicon-trash'></span>");
						out.print("<span class='glyphicon glyphicon-user'></span>");
					}
					


%></td>
<%-- 					<td><a href="/ai/home/<%  out.print(sub[3] + "/"); %>"><%= sub[3] %></a></td> --%>
					<td><%= sub[3] %></td>
					<td><%out.print(dateFormat.format(new Date(Long.parseLong(sub[5])))); %></td>
					
					<td><%out.print(uName); %></td>
				</tr>
				<%
				}
				lnr.close();
				%>
			</tbody>
		</table>
		
	</div>
</body>
</html>