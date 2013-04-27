function InnerFriend() {
    this.friendsShareId = ".overview";    //
    this.pointSignNum =3;
    this.friendNameSubNum =10;
    this.friendAliasSubNum = 4;
    this.emptyFriendData = {};
    this.removeFriendsId = ["504a1e20e4b0454adefeb9ce"]; //5099cff8e4b0b1427d4a6d01
};


InnerFriend.prototype.getEmptyFriendHtml = function(data) {
    return FriendsTemplate.obj.applyEmptyShareFriendsTemplate(data);
};

InnerFriend.prototype.cycleDataLists = function(datas) {
    if(datas == null)return;
    for(var i=0;i<datas.length;i++) {
        var data = datas[i];
        var temp = this.changeOneFriendData(data);
        if(temp == null) {
            datas.splice(i,1);
            i--;
            continue;
        }
    }
};

InnerFriend.prototype.changeOneFriendData = function(data) {
    if(data == null)return null;
    if(this.isRemoveFriend(data))return null;
    
    var name = data.name;
    var alias = data.friend.alias;
    if(alias == null)alias = "";
    
    var fullAliasName = name;
    if(alias.length >0)fullAliasName+="("+alias+")";
    var aliasName = fullAliasName;
    
    var pointSignNum = this.pointSignNum;
    var friendNameSubNum = this.friendNameSubNum;
    var friendAliasSubNum = this.friendAliasSubNum;
    
    name = StringUtil.obj.getSectionValue(name,friendNameSubNum,pointSignNum);
    alias = StringUtil.obj.getSectionValue(alias,friendAliasSubNum,this.pointSignNum);
    aliasName = StringUtil.obj.getSectionValue(aliasName,friendNameSubNum+friendAliasSubNum,pointSignNum);
    data["name"] = name;
    data["friend.alias"] = alias;
    data["fullAliasName"] = fullAliasName;  
    data["aliasName"] = aliasName;  
    
    var avatarSmallSrc = "src=" + data.avatar.small +"";
    data["src"] = avatarSmallSrc;
    
    return data;
};

InnerFriend.prototype.isRemoveFriend = function(data) {
    if(data == null)return true;
    if(data.id == null)return true;
    var isRemove = false;
    for(var i=0;i<this.removeFriendsId.length;i++) {
        var friendId = this.removeFriendsId[i];
        if(friendId == data.id) {
            isRemove = true;
            break;
        }
    }
    return isRemove;
};

InnerFriend.prototype.getFriendsShareObject = function() {
    return $(this.friendsShareId);
};

InnerFriend.obj = new InnerFriend();