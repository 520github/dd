(function($){
    $.bindTextAreaCounter = function(parameters) {
        parameters = $.extend({
            textAreaObj: '',
            counterObj:'',
            defaultValue: '',
            defaultLength: 1
        },parameters);       
        
        function getJQueryObj(obj) {
            if(obj == null)return null;
            if(typeof obj === 'object') {
                return $(obj);
            }
            var jobj;
            if(typeof obj === 'string') {
                jobj = $(obj);
                if(jobj.length ==0)jobj=$("#"+obj);
                if(jobj.length ==0)jobj = null;
            }
            
            return jobj;
        };
        
        function isEmptyJQueryObj(obj) {
            if(obj == null) return true;
            if(obj.length == 0)return true;
            return false;
        };
        
        parameters["textAreaObj"] = getJQueryObj(parameters["textAreaObj"]);
        parameters["counterObj"] = getJQueryObj(parameters["counterObj"]);
        
        function onFocus() {
            if(isEmptyJQueryObj(parameters.textAreaObj))return;
            parameters["textAreaObj"].focus(function() {
				if ($(this).val() == parameters["defaultValue"]) {
					$(this).val("");
				}
		    });
        };
        
        function onKeyup() {
            if(isEmptyJQueryObj(parameters.textAreaObj))return;
            parameters["textAreaObj"].keyup(function() {
				var value = $(this).val();
		        var length = $(this).val().length;
				if (length >= parameters.defaultLength) {
					$(this).val(value.substr(0, parameters.defaultLength));
				}
				parameters["counterObj"].html(parameters.defaultLength - $(this).val().length);
			});
        };
        
        function onKeydown() {
            if(isEmptyJQueryObj(parameters.textAreaObj))return;
            parameters["textAreaObj"].keydown(function() {
				var value = $(this).val();
		        var length = $(this).val().length;
				if (length >= parameters.defaultLength) {
					$(this).val(value.substr(0, parameters.defaultLength));
				}
				parameters["counterObj"].html(parameters.defaultLength - $(this).val().length);
			});
        };
        
        onFocus();
        onKeyup();
        onKeydown();
    };
})(jQuery);