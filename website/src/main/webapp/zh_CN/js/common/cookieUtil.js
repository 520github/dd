function cookieUtil() {

}

cookieUtil.getHeaderToken = function(token) {
    if(token == null || token.length < 1) {
        token = cookieUtil.getToken();
    }
    if(token.indexOf(" ")==-1) token = "WebToken "+token;
    return {"Authorization":token};
};

cookieUtil.getWebToken = function() {
    return "WebToken "+cookieUtil.getToken();
}

cookieUtil.getToken =  function() {
    var _cookieUtil = new cookieUtil();
    return _cookieUtil.getTokenValue();
}

cookieUtil.prototype = {
    getTokenValue:function() {
        var name = "WebToken=";
        return this.getCookieValue(name);
    },
    getCookieValue:function(name) {
        if(name == null || name.length<1)return name;
        var cookValue = document.cookie;
	    var start = this.getCookieStart(name);
	    if(start < 0)return "";
	    start = start + name.length;
	    var end  = this.getCookieEnd(name);
	    
	    cookValue = unescape(cookValue.substring(start,end));
	    return cookValue;
    },
    getCookieStart:function(name) {
        return document.cookie.indexOf(name);
    },
    getCookieEnd:function(name) {
        var start = this.getCookieStart(name);
	    start = start + name.length;
	    var end = document.cookie.indexOf(";",start);
	    if(end ==-1)end = document.cookie.length;
	    return end;
    }
}

