function ShareDo() {
    ShareConstant.call(this);
    this.comment = "";
    this.receipt = [];
    this.thirdParty = [];
    
};

ObjectUtil.obj.extend(ShareDo,ShareConstant);

ShareDo.prototype.shareContent2friend = function(contentId) {
    this.contentId = contentId;
    this.initData();
    var shareData = this.getShareData();
    ContentRest.obj.shareContent(this.contentId,shareData,this.shareSucessFn,'',this.shareErrorFn);
};

ShareDo.prototype.initData = function() {
    this.comment = "";
    this.receipt = [];
    this.thirdParty = [];
};

ShareDo.prototype.getShareData = function() {
    this.getInsideSelectedFriendsData();
    this.getWeiboSelectedFriendsData();
    this.getRenrenSelectedFriendsData();
    
    var shareData = {};
    shareData["score"] = 0;
    shareData["comment"] = this.getCommentDivObj().val();
    shareData["receipt"] = this.receipt;
    shareData["thirdparty"] = this.thirdParty;
    
    return shareData;
};

//inside
ShareDo.prototype.getInsideSelectedFriendsData = function() {
    var isf = this.getInsideSelectedFriends().val();
    if(isf == null || isf.length <1) {
        return ;
    }
    var datas = isf.split(this.splitChar);
    for(var i=0; i<datas.length;i++) {
        var data = $.trim(datas[i]);
        if(data.length <1)continue;
        var receiptData = {"accountId":data};
        this.receipt[this.receipt.length] = receiptData;
    }
};

//weibo
ShareDo.prototype.getWeiboSelectedFriendsData = function() {
    var wsf = this.getWeiboSelectedFriends().val();
    if(wsf == null || wsf.length <1) {
        return ;
    }
    var datas = wsf.split(this.splitChar);
    var receiptData = {"type":this.thirdPartyType[0]};
    var receiptId = [];
    for(var i=0; i<datas.length;i++) {
        var data = $.trim(datas[i]);
        if(data.length <1)continue;
        receiptId[receiptId.length] = data;
    }
    receiptData["receipt"] = receiptId;
    this.thirdParty[this.thirdParty.length] = receiptData;
};

//renren
ShareDo.prototype.getRenrenSelectedFriendsData = function() {
    var rsf = this.getRenrenSelectedFriends().val();
    if(rsf == null || rsf.length <1) {
        return ;
    }
    var datas = rsf.split(this.splitChar);
    var receiptData = {"type":this.thirdPartyType[1]};
    var receiptId = [];
    for(var i=0; i<datas.length;i++) {
        var data = $.trim(datas[i]);
        if(data.length <1)continue;
        receiptId[receiptId.length] = data;
    }
    receiptData["receipt"] = receiptId;
    this.thirdParty[this.thirdParty.length] = receiptData;
};

//callback function
ShareDo.prototype.shareSucessFn = function() {
    openPopupMessage("success","分享成功。这个分享太有才了！");
};

ShareDo.prototype.shareErrorFn = function() {
    openPopupMessage("error","分享失败！再推一次。");
};

ShareDo.obj = new ShareDo();

