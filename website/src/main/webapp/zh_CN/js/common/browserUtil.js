function browserUtil() {
}

browserUtil.getBrowserType = function() {
    var type = "unknown";
    var useAgent = window.navigator.userAgent.toLowerCase();
    if(/^.*lbbrowser/.test(useAgent)) {//LBBROWSER
        type = "liebao";
    }
    else if(useAgent.indexOf("chrome")>-1) {
        type = "chrome";
    }
    else if(jQuery.browser.msie){
        type = "ie";
    }
    else if(jQuery.browser.mozilla){
        type = "firefox";
    }
    else if(jQuery.browser.opera) {//opera
        type = "opera";
    }
    else if(jQuery.browser.safari) {
        type = "safari";
    }
    return type;
};

browserUtil.getBrowserVersion = function() {
    var version = -1;
    var useAgent = window.navigator.userAgent.toLowerCase();
    if(/^.*lbbrowser/.test(useAgent)) {//LBBROWSER
        
    }
    else if(useAgent.indexOf("chrome")>-1) {
        version = useAgent.match(/chrome\/([\d.]+)/)[1];
    }
    else {
        version = jQuery.browser.version;
    }
    
    return version;
};

browserUtil.getUserAgent = function() {
    var agent = navigator.userAgent.toLocaleLowerCase();
    if(agent == null)agent = "";
    if(agent.indexOf("android") >-1) {
        agent = "android";
    }
    else if(agent.indexOf("iphone")>-1) {
        agent = "iphone";
    }
    else {
        agent = "android";
    }
    return agent;
};