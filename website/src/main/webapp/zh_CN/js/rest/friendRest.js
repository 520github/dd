function FriendRest() {
    Rest.call(this);
};

//FriendRest extend Rest
ObjectUtil.obj.extend(FriendRest,Rest);

FriendRest.prototype.getMyFriends = function(data,successFn,errorFn) {
    var ajaxPara = {
        url:"/friend/myFriends?r="+(Math.random()*99999999),
        type:"get",
        data:JSON.stringify(data)
    };
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};

FriendRest.prototype.getMyAllFriends = function(successFn,errorFn) {
    var url = "/service/relation/friend?r="+(Math.random()*99999999);
    this.getFriendsByUrl(url,successFn,errorFn);
};

FriendRest.prototype.getMyFavoriteFriends = function(successFn,errorFn) {
    var url = "/service/relation/friend/frequently?r="+(Math.random()*99999999);
    this.getFriendsByUrl(url,successFn,errorFn);
};

FriendRest.prototype.getMyLastTimeUsedFriends = function(successFn,errorFn) {
    var url = "/service/relation/friend/last-time-used?r="+(Math.random()*99999999);
    this.getFriendsByUrl(url,successFn,errorFn);
};

FriendRest.prototype.getFriendsByUrl = function(url,successFn,errorFn) {
    var ajaxPara = {
        type:"get"
    };
    ajaxPara["url"] = url;
    this.setHeaderToken(ajaxPara);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};


FriendRest.obj = new FriendRest();


