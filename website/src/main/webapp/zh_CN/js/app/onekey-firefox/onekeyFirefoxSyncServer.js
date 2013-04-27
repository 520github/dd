function OneKeyFirefoxSyncServer() {
    this.dataKey = OneKeyFirefox.dataKey;
    this.isDoing = false;
    this.authError = false;
    this.timerId = null;
    this.scanTime = 3000;
    this.allowBrowser = ["firefox"];
};

OneKeyFirefoxSyncServer.prototype.startSyncDatas2Server = function() {
    var isAllow = false;
    if(this.allowBrowser.length <=0) isAllow = true;
    var currentBrowser = browserUtil.getBrowserType();
    for(var i=0;i<this.allowBrowser.length;i++) {
        if(currentBrowser == this.allowBrowser[i]){
            isAllow = true;
            break;
        }
    }
    if(!isAllow) {
        console.log("current browser[" + currentBrowser +"] can't sync data to server....");
        return ;
    }
    this.timerId = window.setInterval("OneKeyFirefoxSyncServer.obj.syncData2Server()",this.scanTime);
};

OneKeyFirefoxSyncServer.prototype.stopTimer = function() {
    var ss = this;
    ss.isDoing = false;
    ss.authError = false;
    window.clearInterval(ss.timerId);
};

OneKeyFirefoxSyncServer.prototype.syncData2Server = function() {
    console.log("start syncData2Server....");
    var ss = this;
    
    //break
    if(ss.authError) {
        console.log("user auth is invalid....");
        ss.stopTimer();
        return;
    }
    //wait
    if(ss.isDoing) {
        console.log("now is doing,please wait current doing complete....");
        return;
    }
    
    var data = ss.getLongestData();
    //break
    if(data == null) {
        console.log("no found content need sync to server....");
        ss.stopTimer();
        return;
    }
    
    ss.isDoing = true;
    var syncData = this.getContentData(data);
    ContentRest.obj.addContent(syncData,this.successFn,'',this.errorFn);
};

OneKeyFirefoxSyncServer.prototype.saveData2Server = function(data) {
    this.saveData = data;
    var syncData = this.getContentData(data);
    ContentRest.obj.addContent(syncData,this.save2ServerSuccessFn,'',this.save2ServerErrorFn,true);
};

OneKeyFirefoxSyncServer.prototype.getLongestData = function() {
    var datas = LocalStorageData.getDataByKey(this.dataKey);
    if(datas == null) {       
        return null;
    }
    
    var dataList = JSON.parse(datas);
    if(dataList == null) {
        return null;
    }
    
    if(dataList.length < 1) {
        return null;
    }
    
    return dataList[0];
};

OneKeyFirefoxSyncServer.prototype.removeLongestData = function() {
    var datas = LocalStorageData.getDataByKey(this.dataKey);
    if(datas == null) {
        return ;
    }
    
    var dataList = JSON.parse(datas);
    if(dataList == null) {
        return ;
    }
    
    if(dataList.length < 1) {
        return ;
    }
    //remove first data
    dataList.shift();
    
    LocalStorageData.setDataByKey(this.dataKey,JSON.stringify(dataList));
};

OneKeyFirefoxSyncServer.prototype.getContentData = function(data) {
    var tagList = ["Source_Plugin","Collect","Unread"];
    var contentData = 
         {
           "contentType":"HtmlClip",
           "tag":tagList,
           "title":data.title,
           "url":data.url,
           "htmlPayload":{"content":data.text}
          };
    
    return contentData;
};

OneKeyFirefoxSyncServer.prototype.successFn = function(resp) {
    OneKeyFirefoxSyncServer.obj.isDoing = false;
    OneKeyFirefoxSyncServer.obj.removeLongestData();
};

OneKeyFirefoxSyncServer.prototype.errorFn = function(req,errorText,errorThrown) {
    OneKeyFirefoxSyncServer.obj.isDoing = false;
    if(req.status == 401 || req.status == 403) {
        OneKeyFirefoxSyncServer.obj.authError = true;
    }
};

OneKeyFirefoxSyncServer.prototype.save2ServerSuccessFn = function(resp) {
    console.log("save data to server is ok....");
    OnekeyFirefoxData.setHomeListUrl(OneKeyFirefox.loginUrl);
    OnekeyFirefoxData.saveDataOk();
};

OneKeyFirefoxSyncServer.prototype.save2ServerErrorFn = function(req,errorText,errorThrown) {
    //save data 2 local
    console.log("save data to server is error,temporary save data to local....");
    var title = this.saveData.title;
    var url = this.saveData.url;
    var text = this.saveData.text;
    var dataObj = new OnekeyFirefoxData(title,url,text);
    dataObj.saveData2Local();
};

OneKeyFirefoxSyncServer.obj = new OneKeyFirefoxSyncServer();
