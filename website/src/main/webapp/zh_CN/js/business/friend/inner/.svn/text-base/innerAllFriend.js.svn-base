function InnerAllFriend() {
    this.noFriendAllId = "nofriendId";
    this.friendAllId = "friendId";
    this.friendAllName = "所有好友";
    this.message = "你还没有好友,是否现在就去";
    this.url = "/friend/find";
    this.urlName = "添加好友";
    InnerFriend.call(this);
};

ObjectUtil.obj.extend(InnerAllFriend,InnerFriend);

InnerAllFriend.prototype.getAllFriends = function() {
    FriendRest.obj.getMyAllFriends(
		function(resp){
		    InnerAllFriend.obj.getAllFriendsSuccessFn(resp);
		},
		function(jxh,text) {
		    InnerAllFriend.obj.getAllFriendsErrorFn(jxh,text);
		}
    );
};

InnerAllFriend.prototype.getAllFriendsSuccessFn = function(resp) {
    var datas = resp.result;
    if(datas == null || datas.length <1) {
        this.emptyFriendData["noFriendId"] = this.noFriendAllId;
        this.emptyFriendData["friendTypeName"] = this.friendAllName;
        this.emptyFriendData["message"] = this.message;
        this.emptyFriendData["url"] = this.url;
        this.emptyFriendData["urlName"] = this.urlName;
        var html = this.getEmptyFriendHtml(this.emptyFriendData);
        this.getFriendsShareObject().append(html);
        return;
    }
    resp["friendTypeId"] = this.friendAllId;
    resp.friendTypeName = this.friendAllName;
    this.cycleDataLists(datas);
    resp.result = datas;
    var html = FriendsTemplate.obj.applyShareFriendsTemplate(resp);
    this.getFriendsShareObject().append(html);
};

InnerAllFriend.prototype.getAllFriendsErrorFn = function(jxh,text) {
    if(jxh.status == "403") {
        alert("加载所有好友列表失败:"+text+"["+jxh.status+"]");
    }
};

InnerAllFriend.prototype.isLoadedAllFriends = function() {
   if($("#"+this.friendAllId).length >0 || $("#"+this.noFriendAllId).length >0) {
       return true;
   }
   return false;
};

InnerAllFriend.obj = new InnerAllFriend();