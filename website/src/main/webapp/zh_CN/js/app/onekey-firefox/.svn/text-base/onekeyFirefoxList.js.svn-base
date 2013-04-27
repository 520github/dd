function OneKeyFirefoxList(dataKey) {
    this.dataKey = dataKey;
    this.onekeyFirefoxList = "onekeyFirefoxList";
    this.onekeyFirefoxListTemplate = "onekeyFirefoxListTemplate";
};

OneKeyFirefoxList.prototype.showDataList = function() {
    var datas = this.getDatas();
    var dataList = JSON.parse(datas);
    //this.getListObj().empty();
    for(var i=dataList.length-1; i>=0;i--) {
        var data = dataList[i];
        this.showOneData(data);
    }
};

OneKeyFirefoxList.prototype.showOneData = function(data) {
    var html = this.getOneHtml(data);
    this.getListObj().append(html);
};

OneKeyFirefoxList.prototype.getOneHtml = function(data) {
    var html = "";
    html = this.getListTemplateObj().html();
    var date = data.date;
    if(date == null) {
        date = new Date().toString();
    }
    var id = data.id;
    if(id == null)id="";
    
    html = html.replace(/\%id\%/g,id);
    html = html.replace(/\%date\%/g,date);
    html = html.replace(/\%title\%/g,data.title);
    html = html.replace(/\%url\%/g,data.url);
    //console.log("description:"+data.description);
    html = html.replace(/\%description\%/g,data.description);
    
    return html;
};

OneKeyFirefoxList.prototype.getDatas = function() {
    return LocalStorageData.getDataByKey(this.dataKey);
};

OneKeyFirefoxList.prototype.getListObj = function() {
    return $("#"+this.onekeyFirefoxList);
}

OneKeyFirefoxList.prototype.getListTemplateObj = function() {
    return $("#"+this.onekeyFirefoxListTemplate);
}
