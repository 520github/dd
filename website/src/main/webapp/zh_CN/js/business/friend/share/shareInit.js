function ShareInit() {
    ShareConstant.call(this);
};

ObjectUtil.obj.extend(ShareInit,ShareConstant);

ShareInit.prototype.init = function(contentId) {
    if(this.isGuestUser()) {
        showLayer("#showLoginOrRegPage");
        return ;
    }
    this.contentId = contentId;
    ShareInit.obj.contentId = contentId;
    this.initFriendsData();
};

ShareInit.prototype.getUserRole = function() {
    var userRole = jQuery("input[name='userRole']").val();
    return userRole;
};

ShareInit.prototype.isGuestUser = function() {
    var userRole = this.getUserRole();
    if(userRole == "Guest") {
        return true;
    }
    var isGuestUser = jQuery("input[name='isGuestUser']").val();
    if(isGuestUser == "true") {
        return true;
    }
    return false;
};

ShareInit.prototype.initFriendsData = function() {
    var data = {"contentId":this.contentId};
    FriendRest.obj.getMyFriends(
        data,
	    function(data){
	        ShareInit.obj.getMyFriendsSuccess(data,ShareInit.obj.contentId);
	    },
        function(){
            ShareInit.obj.getMyFriendsError();
        }
    );
};

ShareInit.prototype.showSharePage = function() {
    $.layer({
		v_title : "分享给朋友",
		v_istitle : false,
		//v_skin:0,
		v_shade : true,
		v_showclose : false,
		v_box : 1,
		v_dom : ShareInit.obj.shareFriendDivId, //id
		v_area : [ '579px', '575px' ],
		v_btns : 0,
		v_btn : [ "保存" ],
		v_move : false,
		v_offset : [ '50px', '350px' ]
	});
};

//about button
ShareInit.prototype.cancelButtonClick = function() {
    this.getCancelButtonObj().click(function() {
		layer_close();
	});
};

ShareInit.prototype.commitButtonClick = function(contentId) {
    ShareInit.obj.getCommitButtonObj().click(function() {
        var css = $(this).attr("class");
        if(css.indexOf(ShareInit.obj.cantCommitCss) >-1 ) {
             return false;
        }
		ShareDo.obj.shareContent2friend(contentId);
	});
};

//about comment
ShareInit.prototype.commentFocus = function() {
    this.getCommentDivObj().focus(function() {
		if ($(this).val() == ShareDo.obj.commentDefaultValue) {
			$(this).val("");
		}
	});
};

ShareInit.prototype.commentBlur = function() {
    var commentObj = this.getCommentDivObj();
    commentObj.keyup(function() {
        var commentMaxLength = ShareDo.obj.commentMaxLength;
		if (commentObj.val().length >= commentMaxLength) {
			commentObj.val(commentObj.val().substr(0, commentMaxLength));
		}
		ShareDo.obj.getCommentRemainDivObj().html(commentMaxLength - commentObj.val().length);
	});
};

ShareInit.prototype.commentKeyup = function() {
    this.getCommentDivObj().blur(function() {
		if ($(this).val() == "") {
			$(this).val(this.commentDefaultValue);
		}
	});
};

//callback function
ShareInit.prototype.getMyFriendsSuccess = function(data,contentId) {
    ShareInit.obj.getShareFriendObj().html(data);

    //about comment
    ShareInit.obj.commentFocus();
    ShareInit.obj.commentBlur();
    ShareInit.obj.commentKeyup();
    
    //about button
    ShareInit.obj.cancelButtonClick();
    ShareInit.obj.commitButtonClick(contentId);
    
    ShareInit.obj.showSharePage();
};

ShareInit.prototype.getMyFriendsError = function() {
    alert("初始化好友分享界面失败");
};

ShareInit.obj = new ShareInit();