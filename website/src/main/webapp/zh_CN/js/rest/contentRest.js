function ContentRest() {
    Rest.call(this);
};
//ContentRest extend Rest
ObjectUtil.obj.extend(ContentRest,Rest);

ContentRest.prototype.searchContent = function(matrix,contentData,successFn,token,errorFn) {
    var ajaxPara = {
        url:"/service/content/personal/search/"+matrix,
        type:"get",
        contentType:"application/json;charset=utf-8",
        //dataType:"json",
        data:contentData //JSON.stringify(contentData)
    };
    this.setHeaderToken(ajaxPara,token);
    //this.setUserAgent(ajaxPara);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    jQuery.ajax(ajaxPara);
};

ContentRest.prototype.addContent = function(contentData,successFn,token,errorFn,sync) {
    var ajaxPara = {
        url:"/service/content/personal/item",
        type:"post",
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        data:JSON.stringify(contentData)
    };
    if(sync) {
        ajaxPara["async"] = false;
    }
    this.setHeaderToken(ajaxPara,token);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};

ContentRest.prototype.shareContent = function(contentId,shareData,successFn,token,errorFn) {
    var ajaxPara = {
        url:"/service/content/personal/item/"+contentId+"/share",
        type:"post",
        contentType:"application/json;charset=utf-8",
        dataType:"json",
        data:JSON.stringify(shareData)
    };
    this.setHeaderToken(ajaxPara,token);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};

ContentRest.obj = new ContentRest();

