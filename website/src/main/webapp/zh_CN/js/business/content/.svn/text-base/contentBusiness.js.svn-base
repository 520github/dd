function ContentBusiness() {
};

ContentBusiness.prototype.getFavoritePublicContentData = function(contentId) {
    if(contentId.indexOf("_")>-1)contentId = contentId.substring(0,contentId.indexOf("_"));
    var tagList = ["Source_Public","Collect"];//
    var contentData = 
        {
           "id":contentId,
           "tag":tagList
        };
    
    return contentData;
};

ContentBusiness.prototype.addFavoritePublicContent = function(contentId) {
    var data = this.getFavoritePublicContentData(contentId);
    ContentRest.obj.addContent(data,this.addFavoritePublicContentSuccessFn ,'',this.addFavoritePublicContentErrorFn);
};

ContentBusiness.prototype.addFavoritePublicContentSuccessFn = function(resp) {
    var isNew = resp.newRepository;
    openPopupMessage("collect",'');
    if(isNew) {
        var newNum = parseInt(jQuery("#favoriteTotalNum").html()) + 1 ;
	    jQuery("#favoriteTotalNum").html(newNum);
    }
};

ContentBusiness.prototype.addFavoritePublicContentErrorFn = function() {
    openPopupMessage("failure",'');
};
ContentBusiness.obj = new ContentBusiness();