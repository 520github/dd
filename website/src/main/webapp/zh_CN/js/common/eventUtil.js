function eventUtil() {
    
}

eventUtil.bind = function(obj, type, fn) {
    if ( obj.attachEvent ) {
        obj['e'+type+fn] = fn;
        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
        obj.attachEvent( 'on'+type, obj[type+fn] );
    } else if(obj.addEventListener){
        obj.addEventListener( type, fn, false );
    }
    else {
        obj.bind(type,fn);
    }
}