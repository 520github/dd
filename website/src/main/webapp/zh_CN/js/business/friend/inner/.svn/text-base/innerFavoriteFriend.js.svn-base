function InnerFavoriteFriend() {
    this.noFriendFavoriteId = "nofavoriteId";
    this.friendFavoriteId = "favoriteId";
    this.friendFavoriteName = "常用联系人";
    this.message = "你还没有常用联系人,是否现在就去";
    this.url = "/friend";
    this.urlName = "设置常用联系人";
    InnerFriend.call(this);
};

ObjectUtil.obj.extend(InnerFavoriteFriend,InnerFriend);

InnerFavoriteFriend.prototype.getFavoriteFriends = function() {
    FriendRest.obj.getMyFavoriteFriends(function(resp){
        InnerFavoriteFriend.obj.getFavoriteFriendsSuccessFn(resp);
    });
};

InnerFavoriteFriend.prototype.getFavoriteFriendsSuccessFn = function(resp) {
    var datas = resp.result;
    if(datas == null || datas.length <1) {
        this.emptyFriendData["noFriendId"] = this.noFriendFavoriteId;
        this.emptyFriendData["friendTypeName"] = this.friendFavoriteName;
        this.emptyFriendData["message"] = this.message;
        this.emptyFriendData["url"] = this.url;
        this.emptyFriendData["urlName"] = this.urlName;
        var html = this.getEmptyFriendHtml(this.emptyFriendData);
        this.getFriendsShareObject().append(html);
        return;
    }
    resp["friendTypeId"] = this.friendFavoriteId;
    resp.friendTypeName = this.friendFavoriteName;
    this.cycleDataLists(datas);
    resp.result = datas;
    var html = FriendsTemplate.obj.applyShareFriendsTemplate(resp);
    this.getFriendsShareObject().append(html);
};

InnerFavoriteFriend.prototype.getFavoriteFriendsErrorFn = function() {
};

InnerFavoriteFriend.prototype.isLoadedFavoriteFriends = function() {
   if($("#"+this.noFriendFavoriteId).length >0 || $("#"+this.friendFavoriteId).length >0) {
       return true;
   }
   return false;
};

InnerFavoriteFriend.obj = new InnerFavoriteFriend();