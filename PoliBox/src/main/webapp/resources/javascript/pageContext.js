$(document).ready(function() {
    $('.sortable').dataTable({
    	paging: false,
    	"bInfo": false,
    	"order": [[1, "asc"]],
    	"language": {
    		"search": "Cerca:",
    		"zeroRecords": "Questa cartella è vuota"
    	}
    });
    if (document.getElementById("ownerCond").innerHTML == "0") {
		$('tr').each(function() {
			var $a = $(this).find("a");
			
			var menu = new Array();
			menu[0] = ["Opzioni cartella condivisa", "#"];
			menu[1] = ["Invita alla cartella", "#divFormCondividi"];
			menu[2] = ["Scarica", "http://localhost:8080" + $a.attr('href')];
			menu[3] = ["Elimina", "#divFormElimina"];
			menu[4] = ["Rinomina", "#divFormRinomina"];
			menu[5] = ["Sposta", "#"];
			menu[6] = ["Copia", "#"];
	
    	var id = $a.attr('id');
    	var isFile = /^file/.test(id);
    	
    	var contextMenu = '<div class="context-menu">';
    	for(var i = 0; i < menu.length; i++){
    		if ($a.hasClass("not_editable")) {
    			if (i==0 || i==1 || i==4 || i==5 || i==6) continue;
    		} else {
        		if(isFile && (i == 0 || i == 1 || i == 8)) continue;
        		if(!isFile && (i == 0 || i == 9)) continue;
    		}
        		contextMenu += '<div><a data-toggle="modal" href="' + menu[i][1] + '">' + menu[i][0] + '</a></div>';
    	}
    	contextMenu += '</div>';
    
    	$(contextMenu).insertAfter($a).hide();
    });
    } else {
    	$('tr').each(function() {
			var $a = $(this).find("a");
			
			var menu = new Array();
			menu[0] = ["Opzioni cartella condivisa", "#"];
			menu[1] = ["Scarica", "http://localhost:8080" + $a.attr('href')];
			menu[2] = ["Elimina", "#divFormElimina"];
			menu[3] = ["Rinomina", "#divFormRinomina"];
	
    	var id = $a.attr('id');
    	var isFile = /^file/.test(id);
    	
    	var contextMenu = '<div class="context-menu">';
    	for(var i = 0; i < menu.length; i++){
    		if ($a.hasClass("not_editable")) {
    			if (i==0 || i==1 || i==4 || i==5 || i==6) continue;
    		} else {
        		if(isFile && (i == 0 )) continue;
        		if(!isFile && (i == 0 || i == 9)) continue;
    		}
        		contextMenu += '<div><a data-toggle="modal" href="' + menu[i][1] + '">' + menu[i][0] + '</a></div>';
    	}
    	contextMenu += '</div>';
    
    	$(contextMenu).insertAfter($a).hide();
    });
    }
	
	$('tr').bind('contextmenu', function(e) {
	    
	    //alert("pippo");
	    var $a = $(this).find("a");
	    var contextMenu = $a.next();
	    
	    document.getElementById("nomefile").value = $a.html();
	    document.getElementById("nomefile2").value = $a.html();
	    document.getElementById("newName").value = $a.html();
		document.getElementById("fileDeleted").innerHTML = $a.html();
		document.getElementById("fileSelected").innerHTML = $a.html();
		document.getElementById("path").value = document.URL;
		document.getElementById("path2").value = document.URL;

	    
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
	
	$(".draggable").draggable({
		revert: "invalid",
		helper: "clone",
	    cursor: "move"
	});
	$(".droppable").droppable({
		activeClass: "ui-state-default",
        hoverClass: "ui-drop-hover",
		drop: function( event, ui ) {
			
			var idDrag = ui.draggable.find("a").attr("id");
			var idDrop = $(this).find("a").attr("id");
			
			if(idDrop === null)
				idDrop = $(this).find("form").attr("id");
			if(idDrop === null)
				idDrop = $(this).find("a").attr("id");
			
			if(((/^file/.test(idDrag)) || (/^directory/.test(idDrag))) && (/^directory/.test(idDrop)) ){  // sposto file/directory non condivisa in directory non condivisa
				
				document.location.href = "/ai/sposta?cond=0&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href")+"&cId=-1";
				
			} else if(((/^file/.test(idDrag)) || (/^directory/.test(idDrag))) && (/^notReadOnly/.test(idDrop))) {  // sposto file/directory NON condivisa in directory condivisa di cui NON sono proprietario
				
				document.location.href = "/ai/sposta?cond=2&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("input.submit").attr("value") + "&cId=" + $(this).find("input.hidden").attr("value");
				
			} else if(((/^file/.test(idDrag)) || (/^directory/.test(idDrag))) && (/^sharedDir/.test(idDrop))){  // sposto file/directory NON condivisa in directory condivisa di cui sono proprietario
				
				document.location.href = "/ai/sposta?cond=3&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href");
				
			} else if(document.getElementById("ownerCond").innerHTML == "1"){  // spostamento interno a directory condivisa di cui sono proprietario
				
				document.location.href = "/ai/sposta?cond=4&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href");
				
			} else if( (/^sharedDir/.test(idDrag)) && (/^directory/.test(idDrop)) ) { // sposto cartella condivisa di cui sono proprietario in cartella NON condivisa
				
				document.location.href = "/ai/sposta?cond=5&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href")+"&cId=-1";
				
			} else if ((/^breadcrumb/.test(idDrop))) {
				document.location.href = "/ai/sposta?cond=0&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href") + "&cId=-1";
			}
			
			/*
			else if( ((/^sharedDir/.test(idDrag)) && ((/^readOnly/.test(idDrop)) || (/^notReadOnly/.test(idDrop)))) ) { // sposto cartella condivisa di cui sono proprietario in cartella condivisa di cui NON sono proprietario: VIETATO
				
			} else if(false) { // sposto cartella condivisa di cui NON sono proprietario in cartella condivisa di cui sono proprietario: VIETATO
				
			} else if( false ) { // sposto cartella condivisa di cui non sono proprietario in cartella non condivisa
				
			}
			*/
		}
	});
	
});

function clearText(field){   
    if (field.defaultValue == field.value)  {
        field.value = '';
    }    
    else if (field.value == '') {
        field.value = field.defaultValue;    
    }
}

function changeValueCheckbox(field) {
	if(field.value.equal("true"))
		field.value = "false";
	else
		field.value = "true";
}
function hideMsgDiv() {
	document.getElementById('msg').innerHTML='';
	location.reload(true);
}