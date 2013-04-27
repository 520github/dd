function Rest() {
};

//paraObj,success,error,complete
Rest.prototype.setCallbackFn = function() {
    if(arguments.length <=1) {
        return;
    }
    var ajaxPara = arguments[0];
    ajaxPara["success"] = arguments[1];
    if(arguments.length == 3) {
        ajaxPara["error"] = arguments[2];
    }
};
//paraObj,token
Rest.prototype.setHeaderToken = function() {
    if(arguments.length <1)return;
    var ajaxPara = arguments[0];
    
    var token;
    if(arguments.length == 2) {
        token = arguments[1];
    }
    var authTokenObj = document.getElementById("authToken");
    if(authTokenObj != null && authTokenObj.value.length >1) {
        token = authTokenObj.value;
    }
    var header = cookieUtil.getHeaderToken(token);
    ajaxPara["headers"] = header;
};

Rest.prototype.setUserAgent = function() {
    if(arguments.length <1)return;
    var ajaxPara = arguments[0];
    
    var userAgent = "duoduo-browser";
    if(arguments.length == 2) {
        userAgent = arguments[1];
    }
    var headers = ajaxPara["headers"];
    if(headers == null) {
        headers = {"User-Agent":userAgent}
    }
    else {
        headers["User-Agent"] = userAgent;
    }
    ajaxPara["headers"] = headers;
};
