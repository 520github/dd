function PluginFriendsShare() {
    this.bindScrollCycleId = null;
};

PluginFriendsShare.prototype.getAllFriends = function() {
    InnerLastFriend.obj.getLastFriends();
    InnerFavoriteFriend.obj.getFavoriteFriends();
    InnerAllFriend.obj.getAllFriends();
    this.bindScrollCycleId = window.setInterval("PluginFriendsShare.obj.bindScroll()",1000);
};

PluginFriendsShare.prototype.bindScroll = function() {
    
    if(InnerAllFriend.obj.isLoadedAllFriends() && InnerFavoriteFriend.obj.isLoadedFavoriteFriends()) {
        friendAction.bindShareFriend();
        window.clearInterval(this.bindScrollCycleId);
    }
};

PluginFriendsShare.obj = new PluginFriendsShare();