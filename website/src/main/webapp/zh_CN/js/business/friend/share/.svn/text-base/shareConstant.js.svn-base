function ShareConstant() {
    //common
    this.splitChar = ",";
    
    //thirdparty
    this.thirdPartyType = ["weibo","renren"];
    
    //about shareFriend
    this.shareFriendDivId = "#shareFriend";
    
    //selected friends
    this.insideSelectedFriends = "#friendSelected";
    this.weiboSelectedFriends = "#weibofriendSelected";
    this.renrenSelectedFriends = "#renrenfriendSelected";
    
    //about share button
    this.cancelButton = "#btnCancel";
    this.commitButton = "#btnCommend";
    this.cantCommitCss = "plug-in-2coffee-btn-determine-no";
    
    //about comment
    this.commentDivId = "#commentText";
    this.commentRemainDivId = "#ddCommentRemain";
    this.commentDefaultValue = "我觉得这个东东可能对你有帮助，希望你喜欢哦！";
    this.commentMaxLength = 140;
    this.score = 0;
};

//about shareFriend
ShareConstant.prototype.getShareFriendObj = function() {
    return $(this.shareFriendDivId);
};

//about selectedFriend
ShareConstant.prototype.getInsideSelectedFriends = function(){
    return $(this.insideSelectedFriends);
};

ShareConstant.prototype.getWeiboSelectedFriends = function(){
    return $(this.weiboSelectedFriends);
};

ShareConstant.prototype.getRenrenSelectedFriends = function(){
    return $(this.renrenSelectedFriends);
};

//about button
ShareConstant.prototype.getCancelButtonObj = function() {
    return this.getShareFriendObj().find(this.cancelButton);
};

ShareConstant.prototype.getCommitButtonObj = function() {
    return this.getShareFriendObj().find(this.commitButton);
};

//about comment
ShareConstant.prototype.getCommentDivObj = function() {
    return $(this.commentDivId);
};

ShareConstant.prototype.getCommentRemainDivObj = function() {
    return $(this.commentRemainDivId);
};

