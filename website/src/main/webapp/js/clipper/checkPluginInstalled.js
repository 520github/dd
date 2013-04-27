function checkPluginIsInstalled() {
    var obj = document.getElementById(systemConstant.pluginInstanceId);
    if(obj == null) {
        return ;
    }
    var browser = getBrowser();
    if(browser != 'ie') {
        var obj =jQuery(".download-tools>.download-mobile");
        if(obj.length < 1) {
            jQuery(".download-tools").hide();
        }else {
            jQuery(".download-tools>.download-pc").hide();
        }
    }
}
checkPluginIsInstalled();
