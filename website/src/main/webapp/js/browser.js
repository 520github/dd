//获取浏览器类型
function getBrowser() {
       var browser = "";
       var ua = navigator.userAgent.toLowerCase();
       //alert(ua);
       if (window.ActiveXObject)
            browser = "ie";
       else if (ua.indexOf("firefox")>-1)
            browser = "firefox";
       else if(ua.indexOf("chrome")>-1)
            browser = "chrome";
       else if (window.MessageEvent && !document.getBoxObjectFor)
            browser = "chrome";
       else if (window.opera)
            browser = "opera";
       else if (window.openDatabase)
            browser = "safari";
       //alert("browser:" +browser);
       return browser;
}

   //根据不同的浏览器,显示不同的提示信息
   function getBookRemark(browser) {
       var remark = "还不知道怎么安装书签吗？请查看如何安装书签说明吧！";
       if(browser.indexOf("chrome") > -1) {
          //remark = remark + "直接将这个按钮拖拽到你的书签工具栏就可以了，这一刻奇迹由你见证哦！";
       }
       
       return remark;
   }
