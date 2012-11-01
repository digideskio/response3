if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

response3 = {};
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

(function( ajax, $, undefined ) {
	ajax.ShowMore = function(map){
		if(map.constructor === response3.collections.Dictionary){
		}
	};
}(window.response3.ajax = window.response3.ajax || {}, jQuery));

var r3map = response3.collections.Dictionary();
response3.ajax.ShowMore(r3map);
