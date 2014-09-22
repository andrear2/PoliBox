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
    if (document.getElementById("readOnly").innerHTML==0) {
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
	        		if(isFile && (i == 0)) continue;
	        		if(!isFile && (i == 0)) continue;
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

        	var id = $a.attr('id');
        	var isFile = /^file/.test(id);
        	
        	var contextMenu = '<div class="context-menu">';
        	for(var i = 0; i < menu.length; i++){
        		if ($a.hasClass("not_editable")) {
        			if (i==0 || i==1 || i==4 || i==5 || i==6) continue;
        		} else {
            		if(isFile && (i == 0)) continue;
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
	    

	    e.preventDefault();
	});
	
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
			document.location.href = "/ai/sposta?cond=1&path=" + ui.draggable.find("a").attr("href") + "&newPath=" + $(this).find("a").attr("href");
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