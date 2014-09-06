var socket = null;

$(document).ready(function() {

	if(window.WebSocket) {
		socket = new WebSocket("wss://localhost:8443/ai/serverWebSocket?email=" + document.getElementById("email").innerHTML + "&nome=" + document.getElementById("nome").innerHTML);
	
		socket.onmessage = function (event) {
			var data = event.data;
		
			document.getElementById("msg").innerHTML = data;
			console.log("MSG received: " + data);
		}
	
		socket.onopen = function (event) {
			console.log("Connessione WebSocket APERTA!");
			
		}
	
		socket.onclose = function (event) {
			console.log("Connessione WebSocket CHIUSA!");
		}
	
	}
});

function forwardForm() {
	
	var allowInvitations;
	var allowChanges;
	var usersList;
	var message;
	var path;
	var nomefile;
	
//	if(document.getElementById("allowInvitations").checked)
//		allowInvitations = true;
//	else
//		allowInvitations = false;
	
	if(document.getElementById("allowChanges").checked)
		allowChanges = true;
	else
		allowChanges = false;
	
	usersList = document.getElementById("usersList").value;
	message = document.getElementById("message").value;
	
	if (usersList == "") {
		alert("Devi inserire almeno un indirizzo email");
		return false;
	}
	
	path = document.getElementById("path").value;
	nomefile = document.getElementById("nomefile").value;
	
	if (message == "") {
		message = "Sei stato invitato alla cartella condivisa " + nomefile;
	}
	
	if(socket != null && socket.readyState === WebSocket.OPEN){
//		var msg = "allowInvit: " + allowInvitations + "; allowChanges: " + allowChanges + "; usersList: " + usersList + "; message: " + message + "; path: " + path + "; nomefile: " + nomefile;     
		var msg = "NEW; allowChanges: " + allowChanges + "; usersList: " + usersList + "; message: " + message + "; path: " + path + "; nomefile: " + nomefile;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
	}
	

	
}
function accept_cond () {
	var c_id = document.getElementById("c_id").textContent;
	if(socket != null && socket.readyState === WebSocket.OPEN){
		var msg = "ACC; c_id: "+c_id;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
		document.getElementById("msg").innerHTML="";
		location.reload(true);
	}
}

function refuse_cond () {
	var c_id = document.getElementById("c_id").textContent;
	if(socket != null && socket.readyState === WebSocket.OPEN){
		var msg = "REF; c_id: "+c_id;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
		document.getElementById("msg").innerHTML="";
		location.reload(true);
	}
}

function accept_cond2 () {
	var c_id = document.getElementById("c_id").textContent;
	if(socket != null && socket.readyState === WebSocket.OPEN){
		var msg = "ACC; c_id: "+c_id;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
		location.reload(true);
	}
}

function refuse_cond2 () {
	var c_id = document.getElementById("c_id").textContent;
	if(socket != null && socket.readyState === WebSocket.OPEN){
		var msg = "REF; c_id: "+c_id;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
		location.reload(true);
	}
}

function refuse_cond3 () {
	var c_id = document.getElementById("c_id").textContent;
	if(socket != null && socket.readyState === WebSocket.OPEN){
		var msg = "REF; c_id: "+c_id;
		socket.send(msg);
		console.log("Ho inviato: " + msg);
		location.reload(true);
	}
}