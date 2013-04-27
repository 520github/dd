function OnekeyFirefoxData(title,url,text) {
    this.title = title;
    this.url = url;
    this.text = text;
    this.description = text;
    this.descMaxLength = 200;
};

OnekeyFirefoxData.prototype.saveData2Local = function() {
    var title = this.title;
    if(title == null)title="";
    var url = this.url;
    if(url== null)url="";
    if(title.length<1 && url.length<1) {
        console.log("save data is not exist");
        jQuery(OneKeyFirefox.showDivId).find("span").html("没有发现任何内容...");
        return;
    }
    
    var data = LocalStorageData.getDataByKey(OneKeyFirefox.dataKey);
    var currentData = this.getCurrentData();
    if(data == null) {
        var jsonData = new Array();
        currentData["id"]="0";
        jsonData[0] = currentData;
    }
    else {
        var jsonData = JSON.parse(data);
        currentData["id"]=jsonData.length+"";
        jsonData[jsonData.length] = currentData;
    }
    LocalStorageData.setDataByKey(OneKeyFirefox.dataKey,JSON.stringify(jsonData));
    OnekeyFirefoxData.setHomeListUrl(OneKeyFirefox.notLoginUrl);
    OnekeyFirefoxData.saveDataOk();
};

OnekeyFirefoxData.prototype.saveData2Server = function() {
    var currentData = this.getCurrentData();
    OneKeyFirefoxSyncServer.obj.saveData2Server(currentData);
};

OnekeyFirefoxData.prototype.getCurrentData = function() {
    this.setDescription();
    var data = {};
    data["title"] = this.title;
    data["url"] = this.url;
    data["description"] = this.description;
    data["text"] = this.text;
    data["date"] = DateUtil.getDateTimeString(new Date());
    return data;
};

OnekeyFirefoxData.prototype.setDescription = function() {
    if(this.text == null || this.text.length < 1) {
        return;
    }
    if(this.text.length > this.descMaxLength) {
        this.description = this.text.substring(0,this.descMaxLength);
    }
};

OnekeyFirefoxData.setHomeListUrl = function(url) {
    jQuery(OneKeyFirefox.showDivId ).find("p>a").attr("href",url);
};

OnekeyFirefoxData.saveDataOk = function() {
    jQuery(OneKeyFirefox.showDivId).find("span").html("内容保存成功");
    jQuery(OneKeyFirefox.showDivId).find("p").show();
};