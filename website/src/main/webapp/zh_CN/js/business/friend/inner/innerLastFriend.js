function InnerLastFriend() {
    this.friendLastId = "lastId";
    this.friendLastName = "最近联系";
    InnerFriend.call(this);
};

ObjectUtil.obj.extend(InnerLastFriend,InnerFriend);

InnerLastFriend.prototype.getLastFriends = function() {
    FriendRest.obj.getMyLastTimeUsedFriends(function(resp){
        InnerLastFriend.obj.getLastFriendsSuccessFn(resp);
    });
};

InnerLastFriend.prototype.getLastFriendsSuccessFn = function(resp) {
    var datas = resp.result;
    if(datas == null || datas.length <1) {
        this.emptyFriendData["friendTypeName"] = this.friendLastName;
        this.getEmptyFriendHtml(this.emptyFriendData);
        return;
    }
    resp["friendTypeId"] = this.friendLastId;
    resp.friendTypeName = this.friendLastName;
    this.cycleDataLists(datas);
    resp.result = datas;
    var html = FriendsTemplate.obj.applyShareFriendsTemplate(resp);
    this.getFriendsShareObject().append(html);
};

InnerLastFriend.prototype.getLastFriendsErrorFn = function() {
};

InnerLastFriend.obj = new InnerLastFriend();