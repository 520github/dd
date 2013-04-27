function ContentWeixinBusiness() {
    ContentB.call(this);
    this.weixinContentId = "#weixinContent";
    this.accountId = "#accountId";
    this.moreBtn = ".more-btn a";
};

ObjectUtil.obj.extend(ContentWeixinBusiness,ContentB);

ContentWeixinBusiness.prototype.searchContentList = function() {
    var matrix = ";";
    var para = this.getOffsetAndLimitPara();
    var accountId = "AccountId " + $(this.accountId).val();
    ContentWeixinBusiness.obj.getMoreBtnObj().hide();
    ContentRest.obj.searchContent(matrix,para,this.searchContentListSuccessFn,accountId,this.searchContentListErronFn);
};

ContentWeixinBusiness.prototype.searchContentListSuccessFn = function(resp) {
    var total = resp.total;
	var results = resp.result;
	if(results.length <1) {
	    if(ContentWeixinBusiness.obj.getWeixinContentObj().children().length <1) {
	        ContentWeixinBusiness.obj.getMoreBtnObj().html("你当前还没有任何收藏的内容哦！");
	        ContentWeixinBusiness.obj.getMoreBtnObj().attr("href","javascript:void(0)");
	    }
	    else {
	        alert("亲，没有更多的内容可以获取了啊！");
	    }
	    ContentWeixinBusiness.obj.getMoreBtnObj().show();
	    return false;
	}
	for(var i=0;i<results.length;i++) {
        var data = results[i];
        var html = WeixinContentListTemplate.obj.getContentHtmlByContentType(data);
        ContentWeixinBusiness.obj.getWeixinContentObj().append(html);
    }
    ContentWeixinBusiness.obj.getMoreBtnObj().show();
    $("input[name='offset']").val(ContentWeixinBusiness.obj.getWeixinContentObj().children().length);
};

ContentWeixinBusiness.prototype.searchContentListErronFn = function() {
    alert("获取内容失败！");
};

ContentWeixinBusiness.prototype.getWeixinContentObj = function() {
    return $(this.weixinContentId);
};

ContentWeixinBusiness.prototype.getMoreBtnObj = function() {
    return $(this.moreBtn);
};

ContentWeixinBusiness.obj = new ContentWeixinBusiness();