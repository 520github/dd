function OneKeyFirefox() {
};

OneKeyFirefox.dataKey = "onekey.firefox.datakey";
OneKeyFirefox.loginUrl = "/home";
OneKeyFirefox.notLoginUrl = "/app/firefox/list";
OneKeyFirefox.showDivId = "#fierfox-key-application-preservation";

OneKeyFirefox.go2detail = function(id) {
    var url = "/app/firefox/detail?id="+id;
    window.open(url);
};

OneKeyFirefox.prototype.saveData = function(title,url,text) {
    var dataObj = new OnekeyFirefoxData(title,url,text);
    UserRest.obj.IsLoginUser(
        function(){//login
	        dataObj.saveData2Server();
	    },
	    '',
	    function(){//not login
	        dataObj.saveData2Local();
	    }
    );
};

OneKeyFirefox.prototype.getOneKeyFirefoxList = function() {
    var okFirefoxList = new OneKeyFirefoxList(OneKeyFirefox.dataKey);
    okFirefoxList.showDataList();
};

OneKeyFirefox.prototype.showOneKeyFirefoxDetail = function(id) {
    var okFirefoxDetail = new OneKeyFirefoxDetail(id);
    okFirefoxDetail.showDetailData();
};

OneKeyFirefox.obj = new OneKeyFirefox();




