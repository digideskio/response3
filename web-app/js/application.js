response3={};
(function( collections, $, undefined ) {
	collections.Dictionary = function Dictionary() {
		var self = {};
        self.index = 0;
        return{
        	constructor: collections.Dictionary,
            add: function(key, value){
                self.index += 1;
                return this[key] = value;
            },
            set: function(key, value){
                this[key] = value;
            },
            get: function(key){
                return this[key];
            },
            remove: function(key){
                delete this[key];
            },
            exist: function(key){
                if(this[key]){
                    return true;
                }
                else{
                    return false;
                }
            },
            getIndex: function(){
                return self.index;
            }
        };
    };
}(window.response3.collections = window.response3.collections || {}, jQuery));

(function( response3, $, undefined ) {
	response3.ajax = function(map){
		if(map.constructor === response3.collections.Dictionary){
			var url = map.get('url');
			var params = map.get('params')
			if(!url || !params){ return false;}
			$.ajax({
                type: map.get('method') || 'POST',
                url:url,
                data:params,
                async: map.get('sync') || false,
                complete: map.get('callback') || function(){}
            });
		}
	};
	
	response3.addFilterQuery = function(element, params){
		var tablebody = $("#"+params.tbody).get();
		console.log(tablebody);
		console.log(tablebody.rows);
        response3.table.data.lastListRow = tablebody.rows[tablebody.rows.length-1].cloneNode(true);
        response3.table.data.href = params.link+"/";
        response3.timer.addTypingTimer(element, function(){
        	var map = new response3.collections.Dictionary();
        	map.set('url',params.url);
            map.set('params',{id:params.id});
            map.set('callback', function(response){
                var data = $.parseJSON(response.responseText);
                var props = new response3.collections.Dictionary();
                props.set('oldTBody',tablebody);
                props.set('jsonData',data);
                response3.table.buildSimpleTable(props);
                response3.button.enable($('#button').get(0));
                $("#currentLength").html(data.length);
            });
            response3.ajax(map);
        });
	};
	
	
}(window.response3 = window.response3 || {}, jQuery));

(function( button, $, undefined ) {
	
	var enabled = null;
	button.disable = function(ele){
		if(ele != null){
			enabled = ele.cloneNode(true);
			ele.value = "";
			ele.style.background = "#3B659C";
			ele.onclick = function(){return false};
			ele.style.cursor = "default";
		}
	};
	
	button.enable = function(ele){
		if(enabled != null){
			ele.value = enabled.value;
			ele.style.background = enabled.style.background;
			ele.onclick = enabled.onclick;
			ele.style.cursor = enabled.style.cursor;
		}
	};
}(window.response3.button = window.response3.button || {}, jQuery));

(function( table, $, undefined ) {
	table.updateRowColors = function(ele){
		for(var i=0; i<ele.rows.length; i++){
		   if(i % 2 == 0){
			   ele.rows[i].className="odd";
		   } else {
			   ele.rows[i].className="even";
		   }
		}
	};
	/* map with following properties:
	 * oldTBody, jsonData
	 */
	table.buildSimpleTable = function(map){
		var old_tbody = map.get('oldTBody');
        var new_tbody = document.createElement('tbody');
        new_tbody.id = old_tbody.id;
        var data = map.get('jsonData');
        for(var i=0; i<data.length; i++){
               var atag = document.createElement('a');
               atag.href= response3.table.data.href+data[i].id;
               atag.innerHTML= data[i].name;
               var row=new_tbody.insertRow(-1);
               var cell1=row.insertCell(0);
               cell1.colSpan=2;
               cell1.appendChild(atag);
        }
        new_tbody.appendChild(response3.table.data.lastListRow);
        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
        response3.table.updateRowColors(new_tbody);
	};
	table.data = {};
}(window.response3.table = window.response3.table || {}, jQuery));

(function( timer, $, undefined ) {
	var typingTimer;
	var doneTyping = 300;
	
	timer.addTypingTimer = function(ele, tfunction){
		$(ele).keyup(function(){
		    clearTimeout(typingTimer);
		    if ($(ele).val) {
		        typingTimer = setTimeout(tfunction, doneTyping);
		    }
		});
	};
}(window.response3.timer = window.response3.timer || {}, jQuery));

$('input[name="r3filter"]').each(function() {
	response3.addFilterQuery(this, $.parseJSON(this.getAttribute('data-options')));
});


