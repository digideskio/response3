if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
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
}(window.response3 = window.response3 || {}, jQuery));

(function( disable, $, undefined ) {
	disable.button = function(ele){
		ele.value = "";
		ele.style.background = "#3B659C";
		ele.onclick = function(){return false};
		ele.style.cursor = "default";
	};
}(window.response3.disable = window.response3.disable || {}, jQuery));

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
}(window.response3.table = window.response3.table || {}, jQuery));
