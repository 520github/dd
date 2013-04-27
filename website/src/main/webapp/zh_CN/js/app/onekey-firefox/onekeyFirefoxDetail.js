function OneKeyFirefoxDetail(id) {
    this.id = id;
    this.onekeyFirefoxDetail = "center";
    this.onekeyFirefoxDetailTemplate = "onekeyFirefoxDetailTemplate";
    this.dataKey = OneKeyFirefox.dataKey;
};

OneKeyFirefoxDetail.prototype.getDetailDataById = function() {
    var datas = LocalStorageData.getDataByKey(this.dataKey);
    var dataList = JSON.parse(datas);
    //TODO Mulit maybe
    return dataList[parseInt(this.id)];
};

OneKeyFirefoxDetail.prototype.showDetailData = function() {
    var data = this.getDetailDataById();
    if(data == null)return;
    
    var html = this.getDetailHtml(data);
    //this.getOnekeyFirefoxDetailObj().empty();
    this.getOnekeyFirefoxDetailObj().append(html);
};

OneKeyFirefoxDetail.prototype.getDetailHtml = function(data) {
    var html = this.getOnekeyFirefoxDetailTemplateObj().html();
    html = html.replace(/\%title\%/g,data.title);
    html = html.replace(/\%url\%/g,data.url);
    html = html.replace(/\%date\%/g,data.date);
    html = html.replace(/\%text\%/g,data.text);
    return html;
};

OneKeyFirefoxDetail.prototype.getOnekeyFirefoxDetailObj = function() {
    return $("#"+this.onekeyFirefoxDetail);
};

OneKeyFirefoxDetail.prototype.getOnekeyFirefoxDetailTemplateObj = function() {
    return $("#"+this.onekeyFirefoxDetailTemplate);
};