$(document).ready(function() {
	    $('.sortable').dataTable({
	    	paging: false,
	    	"bInfo": false,
	    	"order": [[0, "asc"]],
	    	"language": {
	    		"search": "Cerca:",
	    		"zeroRecords": "Questa cartella � vuota"
	    	}
	    });
	    
		$('tr').each(function() {
			var $a = $(this).find("a");
			
			var menu = new Array();
			menu[0] = ["Opzioni cartella condivisa", "#"];
			menu[1] = ["Invita alla cartella", "#divFormCondividi"];
			menu[2] = ["Condividi link", "#"];
			menu[3] = ["Scarica", "http://localhost:8080" + $a.attr('href')];
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
			document.getElementById("fileSelected").innerHTML = $a.html();
			document.getElementById("path").value = document.URL;

		    
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