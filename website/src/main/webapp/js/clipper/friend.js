var constant = {
    friendType:['all','favorite','appoint'],
    init:"true",
    friendSize:3,
    notFoundId:"notFriendId",
    isSearchFromFriend:false,
    defaultSearchText:"查找好友",
    matchSelectedClass:"1",
    lastId:"lastId",
    favoriteId:"favoriteId",
    friendId:"friendId",
    auf:"plug-in-pfeil-auf",
    weniger:"plug-in-pfeil-weniger",
    check:"plug-in-2coffee-panel-icon-1",
    uncheck:"plug-in-2coffee-panel-icon",
    availHeight:window.screen.availHeight,
    browseHeadHeight:0,
    headHeight:159,
    searchHeight:40,
    myHeight:67,
    friendHeadHeight:30,
    friendFooterHeight:10,
    friendMidHeight:15,
    commentHeight:168,    
    topHeight:22,
    getUseHeight:function() {
        return document.documentElement.clientHeight;
    },
    emptyFriendHeight:function() {
        var height = 0;
        var length = $("#shareFriendId>div[id!='my']").filter("div[id!='notFoundFriendId']").length;
        var emptySize = this.friendSize - length;
        if(emptySize > 0 ) {
            height = (this.friendHeadHeight + this.friendMidHeight + this.friendFooterHeight)*emptySize;
        }
        return height;
    },
    shareHeight:function() {
       var headHeights = this.browseHeadHeight + this.headHeight + this.searchHeight;
       var useHeight = __coffee2innerData__["useHeight"];//this.getUseHeight();
       if(useHeight == null || useHeight<1) {
           useHeight = this.availHeight;
       }
       //alert(useHeight+":uh");
       return useHeight - headHeights - this.commentHeight + this.topHeight;
    },
    friendHeight:function() {
        /*var friendHeadHeights = this.friendHeadHeight*3;
        var friendFooterHeights = this.friendFooterHeight*3;
        var friendMidHeights = this.friendMidHeight*3;
        var friendHeights = friendHeadHeights + friendFooterHeights + friendMidHeights;
        
        var headHeights = this.browseHeadHeight + this.headHeight + this.searchHeight + this.myHeight;
        var emptyHeights =  this.emptyFriendHeight();
        var useHeights = headHeights + friendHeights + this.commentHeight - emptyHeights;
        return (this.availHeight-useHeights);*/
        var freeHeight = this.shareHeight();
        return freeHeight - this.myHeight - this.friendHeadHeight*3;
    },
    perFriendHeight:35,
    additionalHeight:15,
    friendOkShowNum:function() {
        var fheight = this.friendHeight();
        var showNum = fheight/this.perFriendHeight;
        showNum = parseInt(showNum);
        if(showNum <=0)showNum =1;
        return showNum;
    }
}

var friendCommon = {
    shareHeight:constant.shareHeight(),
    okShowNum:constant.friendOkShowNum(),
    setDivH2SpanCss:function(divId) {
        var obj = friendDefine.getDivH2SpanObject(divId);
        var css = obj.attr("class");
        if(css.indexOf("auf") >-1) {
	        css = constant.weniger;
	    }
	    else {
	        css = constant.auf;
	    }
	    obj.attr("class",css);
    },
    getDivH2SpanCss:function(divId) {
        return friendDefine.getDivH2SpanObject(divId).attr("class");
    },
    setFriendViewportHeight:function(height){
        if(height == null || height <1) height = this.shareHeight;
        //alert(height+"|"+this.shareHeight);
        friendDefine.getFriendViewportObject().css("height",height+"px");
    },
    getAllLiNumByDivId:function(divId) {
        var length = friendDefine.getDivDivLiObject(divId).length;
        return length;
    },
    getAllLiVisiableNumByDivId:function(divId) {
        var length = friendDefine.getDivDivLiVisibleObject(divId).length;
        return length;
    },
    getAllLiNum:function() {
        return friendDefine.getAllDivLiObject().length;
    },
    resetFriendScrollTop:function() {
        friendDefine.getFriendThumbObject().css("top","0px");
        friendDefine.getFriendOverviewObject().css("top","0px");
    },
    getAppointFriendValue:function() {
        var value = friendDefine.getAppointFriendObject().html();
        value = $.trim(value);
        return value.split(",");
    }
    
}

var friendAction = {
    bindEventAction:function() {
        $("#searchFriendText").focus(function() {
            if($(this).val() == constant.defaultSearchText) {
                $(this).val("");
            }
        });
        $("#searchFriendText").keyup(function(){
            friendAction.searchFriend();
        });
        $("#searchFriendButton").click(function(){
            friendAction.searchFriend();
        });
    },
    initFriendDiv:function() {
        friendCommon.setFriendViewportHeight();
        //friendShowOrHidden.hideOrShowFriendScroll();
	    //this.searchShowDiv(constant.friendId);
        constant.init = "false";
    },
    searchShowDiv:function(divId) {
        friendDefine.getDivDivClassObject(divId).show();
        friendDefine.getDivH2SpanObject(divId).attr("class",constant.auf);
    },
    searchHideDiv:function(divId) {
        friendDefine.getDivDivClassObject(divId).hide();
        friendDefine.getDivH2SpanObject(divId).attr("class",constant.weniger);
    },
    toggleDiv:function(divId) {
        this.bindShareFriend();
        var css = friendDefine.getDivH2SpanObject(divId).attr("class");
        if(css == constant.auf) {
            friendDefine.getDivDivClassObject(divId).hide();
        }else {
            friendDefine.getDivDivClassObject(divId).show();
        }
        friendCommon.setDivH2SpanCss(divId);
        this.searchOneShareFriend(divId);
    },
    selectShareFriend:function(friendId,divId,allType) {
        friendDefine.getLiIdObject(friendId).each(function(){
            var b = $(this).find("b");
            var css = b.attr("class");
	        if(css.indexOf("1") >-1)css = constant.uncheck;
	        else css = constant.check;
	       
	        if(allType == 'yes') {
	            css = constant.check;
	        }
	        else if(allType == 'no') {
	            css = constant.uncheck;
	        }
	       
	        b.attr("class",css);
        });
        if(allType == null) {
            this.checkSelectAll();
        }
        
        friendAction.isSelectedFriend();
    },
    isSelectedFriend:function() {
        var friends = friendAction.getSelecteFriends();
        var my = friendAction.getMy();
        var obj = $("#ddClipperBtn");
        if(friends.length <1 && my.length<1) {
            var css = obj.attr("class");
            if(css.indexOf(constant.cantCommitCss)==-1)obj.attr("class",css+" plug-in-2coffee-btn-determine-no");
        }
        else {
            obj.attr("class","plug-in-2coffee-btn-determine");
        }    
    },
    checkSelectAll:function() {
        friendDefine.getDivH2bAllObject().each(function(){
            var divId = $(this).parent().parent().attr("id");
            var checkLength = friendDefine.getLiBCheckObject(divId).length;
            var allLength = friendDefine.getDivDivLiVisibleObject(divId).length;
            if(checkLength > 0 && allLength ==0) {
                allLength = friendDefine.getDivDivLiObject(divId).length;
            }
            if(checkLength == 0 || allLength==0) {
                $(this).attr("class",constant.uncheck);	
            }
	        else if(checkLength >= allLength) {
	            $(this).attr("class",constant.check);
	        }
	        else {
	            $(this).attr("class",constant.uncheck);
	        }
        });
    },	
    selectAllFriend:function(divId,isAllLi) {
        var css = friendDefine.getDivH2bObject(divId).attr("class");
        var allType = "";
	    if(css.indexOf("1") >-1) {
	        allType = "no";
	        css = constant.uncheck;
	    }
	    else {
	        allType = "yes";
	        css = constant.check;
	    }
	    friendDefine.getDivH2bObject(divId).attr("class",css);
	    var liObj;
	    if(isAllLi) {
	        liObj = friendDefine.getDivDivLiObject(divId);
	    }
	    else {
	        liObj = friendDefine.getDivDivLiVisibleObject(divId);
	    }
	    liObj.each(function(){
	       var friendId = $(this).attr("id");
	       friendAction.selectShareFriend(friendId,divId,allType);
	   });
	   this.checkSelectAll();
    },
    searchFriend:function() {
        $("li[id='"+constant.notFoundId+"']").remove();
        var searchText = $.trim($("#searchFriendText").val());
	    friendShowOrHidden.showDiv();
	    friendDefine.getFriendDivObject().each(function(){
	        var divId = $(this).attr("id");
	        friendAction.searchOneShareFriend(divId);
	    });
	    this.checkSelectAll();
	    friendCommon.resetFriendScrollTop();
    },
    searchOneShareFriend:function(divId) {
        var searchText = $.trim($("#searchFriendText").val());
	    if(constant.defaultSearchText == searchText) {
	        return;
	    }
        var obj = friendDefine.getDivObject(divId);
        if(searchText == '') {
            obj.show();
        }
        else if(constant.isSearchFromFriend && divId !=constant.friendId) {
            obj.hide();
        }
        obj.find("li").each(function(){
            var text = $(this).find("p").text();
	        if(searchText =='' || text.indexOf(searchText) >-1) {
	            $(this).show();
	        }
	        else {
	            $(this).hide();
	        }
        });
        this.searchNotFound(divId);
    },
    searchNotFound:function(divId) {
        var searchText = $.trim($("#searchFriendText").val());
        if(searchText == '')return;
        
        var length = friendCommon.getAllLiVisiableNumByDivId(divId);
        if(length <1) {
            var html = "<li id='"+constant.notFoundId+"' align='center' >没有找到好友\"<font color='red' >"+searchText+"</font>\"</li>";
            //friendDefine.getDivDivUlObject(divId).append(html);
        }
    },
    autoSelectFriendByType:function() {
        var friendType = $("#friendType").val();
        if(friendType == constant.friendType[0]) {//all
            this.selectAllFriend(constant.friendId,true);
        }
        else if(friendType == constant.friendType[1]) {//favorite
            this.selectAllFriend(constant.favoriteId,true);
        }
        else if(friendType == constant.friendType[2]) {//appoint
            var values = friendCommon.getAppointFriendValue();
            for(var i=0;i<values.length;i++) {
                value = values[i];
                value = $.trim(value);
                if(value == '')continue;
                this.selectShareFriend(value);
            }
        }
    },
    getSelecteFriends:function() {
        var friends = "";
        friendDefine.getAllDivLiObject().each(function(){
            var id = $(this).attr("id");
            var css = $(this).find("b").attr("class");
            if(css.indexOf(constant.matchSelectedClass) >-1) {
                if(friends.indexOf(id) == -1) {
                    friends = friends + id + ",";
                }
            }
        });
        if(friends.indexOf(",") >-1) {
            friends = friends.substring(0, friends.length - 1);
        }
        return friends;
    },
    getMy:function() {
        var my = "";
        var obj = friendDefine.getMyDivLiObject();
        var css = obj.find("b").attr("class");
        if(css.indexOf(constant.matchSelectedClass)> -1) {
            my = obj.attr("id");
        }
        return my;
    },
    bindShareFriend:function() {
        var s = friendDefine.getFriendObject();
        if(s.length > 0 && s.attr("scrolled") != "1"){
			s.tinyscrollbar();
			s.attr("scrolled", "1");
		}
    }
}

var friendShowOrHidden = {
    showDiv:function() {
        friendDefine.getFriendDivObject().each(function() {
            var divId = $(this).attr("id");
            friendDefine.getDivDivObject(divId).show();
            friendDefine.getDivH2SpanObject(divId).attr("class",constant.auf);
	    });
    },
    hideOrShowFriendScroll:function() {
        friendCommon.okShowNum = constant.friendOkShowNum();
        if(friendCommon.getAllLiNum() < friendCommon.okShowNum) {
            friendDefine.getFriendScrollObject().hide();
        }
        else {
            friendDefine.getFriendScrollObject().show();
        }
    }
}

var friendDefine = {
    getFriendObject:function() {
        return $("#shareFriendId");
    },
    getFriendDivObject:function() {
        return this.getFriendObject().find(".overview>div[id!='my']").not("div[id^='no']");
    },
    getFriendThumbObject:function() {
        return this.getFriendObject().find(".thumb");
    },
    getFriendViewportObject:function() {
        return this.getFriendObject().find(".viewport");
    },
    getFriendOverviewObject:function() {
        return this.getFriendObject().find(".overview");
    },
    getFriendScrollObject:function() {
        return this.getFriendObject().find("div[class='scrollbar']");
    },
    getAllDivLiObject:function() {
        return this.getFriendDivObject().find("li");
    },
    getAllDivLiVisibleObject:function() {
        return this.getFriendDivObject().find("li:visible");
    },
    getDivObject:function(divId) {
        return $("#" + divId);
    },
    getDivH2Object:function(divId) {
        return this.getDivObject(divId).find("h2");
    },
    getDivH2bObject:function(divId) {
        return this.getDivH2Object(divId).find("b");
    },
    getDivH2bCheckObject:function() {
        return $("div h2 b[class='"+constant.check+"']");
    },
    getDivH2bAllObject:function() {
        return $(".overview>div h2 b");
    },
    getDivH2SpanObject:function(divId) {
        return this.getDivH2Object(divId).find("span");
    },
    getDivDivObject:function(divId) {
        return this.getDivObject(divId).find("div");
    },
    getDivDivClassObject:function(divId) {
        return this.getDivObject(divId).find("div[class='plug-in-2coffee-panel-title-left']");
    },
    getDivDivViewportObject:function(divId){
        return this.getDivObject(divId).find("div[class='viewport']");
    },
    getDivDivLiObject:function(divId) {
        return this.getDivDivObject(divId).find("li");
    },
    getDivDivUlObject:function(divId) {
        return this.getDivDivObject(divId).find("ul");
    },
    getDivDivLiVisibleObject:function(divId) {
        var type = "visible";
        if(constant.init == "true") {
            type = "visible";//hidden
        }
        return this.getDivDivObject(divId).find("li:"+type);
    },
    getLiIdObject:function(id) {
        return $("li[id='"+id+"']");
    },
    getLiBCheckObject:function(divId) {
        return this.getDivObject(divId).find("li b[class='"+constant.check+"']");
    },
    getMyDivLiObject:function() {
        return $("#my").find("li");
    },
    getAppointFriendObject:function() {
        return $("#appointFriendId");
    }
}