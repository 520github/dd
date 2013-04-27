function FriendCustomObject() {
}

FriendCustomObject.para = {
    customObjectTemp : "#customObjectTemp",
    oneCustomObjectTemp :"#oneCustomObjectTemp",
    myId :"#my",
    favorityId:"#favority",
    allId:"#all",
    selectedId:"#selected",
    selectedFriendTemp:"#selectedFriendTemp",
    gFriendData:null,
    searchText:"#searchText",
    defaultSearchText:"昵称/帐号",
    searchTextButton:"#searchTextButton",
    go2backButton:"#go2back",
    go2saveButton:"#go2save",
    divShowClass:"edit-contact-fold",
    scrollDivId:"#scroll",
    scrollContentDiv:"#scroll>.rollbar-content"
};

FriendCustomObject.switchDivShowOrHide = function(divId) {
    if(divId.indexOf("#") ==-1) divId = "#"+divId;
    var spanObj = $(FriendCustomObject.para.customObjectTemp + " " +divId+ " div>span");
    var ulObj = $(FriendCustomObject.para.customObjectTemp + " " +divId+ " ul");
    var css = spanObj.attr("class");
    if(css == FriendCustomObject.para.divShowClass) {//show
        spanObj.attr("class","");
        ulObj.show();
    }
    else {//hide
        spanObj.attr("class",FriendCustomObject.para.divShowClass);
        ulObj.hide();
    }
};

FriendCustomObject.getOneSelectedFriendHtml = function(objVal) {
    var obj = $(FriendCustomObject.para.selectedFriendTemp);
    var friendId = objVal.attr("id");
    var ico = objVal.find("img").attr("src");
    var friendName = objVal.find("p").html();
    
    obj.find("li").attr("id",friendId);
    obj.find("li>a").attr("onmouseenter","FriendCustomObject.showDeleteImg('"+friendId+"')");
    obj.find("li>p>a").attr("onmouseout","FriendCustomObject.hiddenDeleteImg('"+friendId+"')");
    obj.find("li>p>a").attr("href","javascript:FriendCustomObject.removeSelectedFriend('"+friendId+"');");
    obj.find("li>p>a").attr("title","删除-"+friendName);
    //obj.find("a").attr("href","javascript:FriendCustomObject.selectedOneFriend('"+friendData["id"]+"','"+divId+"');");
    obj.find("li>a").attr("title",friendName);
    obj.find("li>a>img").attr("src",ico);

    return obj.html();
};

FriendCustomObject.showDeleteImg = function(friendId) {
    $(FriendCustomObject.para.selectedId).find("ul>li[id='"+friendId+"']").find("p").show();
};

FriendCustomObject.hiddenDeleteImg = function(friendId) {
    $(FriendCustomObject.para.selectedId).find("ul>li[id='"+friendId+"']").find("p").hide();
};

FriendCustomObject.removeSelectedFriend = function(friendId) {
    $(FriendCustomObject.para.selectedId).find("ul>li[id='"+friendId+"']").remove();
    FriendCustomObject.selectedOneFriend(friendId,'','no');
};

FriendCustomObject.setSelectedNum = function() {
    var length = $(FriendCustomObject.para.selectedId).find("ul>li").length;
    $(FriendCustomObject.para.selectedId).find("span>a").html("保存("+length+")");
};

FriendCustomObject.getSelectedFriendIds = function() {
    var friendId = "";
    $(FriendCustomObject.para.selectedId).find("ul>li").each(function(){
        var id = $(this).attr("id");
        friendId = friendId + id +",";
    });
    return friendId;
};

FriendCustomObject.confirmSelectedFriend = function() {
    $(FriendCustomObject.para.customObjectTemp).hide();
    var length = $(FriendCustomObject.para.selectedId).find("ul>li").length;
    $("#dragActionId").show();
    //if(length>0)
    $("#dragActionId #customeFriendNum").html("共"+length+"位好友");
    $("#dragActionId #customeFriendIds").val(FriendCustomObject.getSelectedFriendIds());
    //设置图标
    FriendCustomObject.removeOldSelectedImg();
    var selectedImg = FriendCustomObject.getSelectedImg();
    $("#dragActionId #customeId ul").show();
    if(length<1)$("#dragActionId #customeId ul").hide();
    $("#dragActionId #customeId ul").prepend(selectedImg);
};
FriendCustomObject.removeOldSelectedImg = function() {
    $("#dragActionId #customeId ul>li").each(function(){
        var css = $(this).attr("class");
        if(css != "drag-add-ico") {
            $(this).remove();
        }
    });
};
FriendCustomObject.getSelectedImg = function() {
    return $(FriendCustomObject.para.selectedId).find("ul").html();
};

FriendCustomObject.setSelectedOneFriend = function(obj) {
    var friendId = obj.attr("id");
    var selObj = $(FriendCustomObject.para.selectedId).find("li[id='" + friendId+"']");
    
    var checked = obj.find("input").attr("checked");
    if(checked) {
        if(selObj.length > 0) return;
        var length = $(FriendCustomObject.para.selectedId).find("ul>li").length;
        var html = FriendCustomObject.getOneSelectedFriendHtml(obj);
        $(FriendCustomObject.para.selectedId).find("ul").append(html);
        if(length >=5){
            //$(FriendCustomObject.para.selectedId).find("ul>li:last").hide();
        }  
    }
    else {
        if(selObj.length < 1) return;
        selObj.remove();
    }
}

FriendCustomObject.selectedMy = function(obj) {
    var checked = $(obj).attr("checked");
    if(checked) {
        checked = true;
    }
    else {
        checked = false;
    }
    $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.myId).find("input").attr("checked",checked);
};
FriendCustomObject.selectedOneFriend = function(friendId,divId,allType) {
    $(FriendCustomObject.para.customObjectTemp).find("li[id='"+friendId+"']").each(function(){
        var id = $(this).parent().parent().attr("id");
        if(FriendCustomObject.para.selectedId.indexOf(id)>-1) {
            return;
        }
        var ok = $(this).find("input").attr("ok");
        var checked =false;
        if(ok == "yes"){
            checked = false;
            ok = "no";
        }
        else {
            checked = true;
            ok = "yes";
        }
        
        if(allType == "yes") {
            checked = true;
            ok = "yes";
        }
        else if(allType == "no"){
            checked = false;
            ok = "no";
        }
        $(this).find("input").attr("checked",checked);
        $(this).find("input").attr("ok",ok);
        
        FriendCustomObject.setSelectedOneFriend($(this));
    });
    FriendCustomObject.setSelectedNum();
    if(allType == null) {
        //this.checkSelectAll();
    }
};

FriendCustomObject.selectedDivFriend = function(divId) {
    $(FriendCustomObject.para.customObjectTemp+" " +divId).find("li").each(function(){
        var friendId = $(this).attr("id");
        FriendCustomObject.selectedOneFriend(friendId,divId,"yes");
    });
};

FriendCustomObject.cancelDivFriend = function(divId) {
    $(FriendCustomObject.para.customObjectTemp+" " +divId).find("li").each(function(){
        var friendId = $(this).attr("id");
        FriendCustomObject.selectedOneFriend(friendId,divId,"no");
    });
};

FriendCustomObject.selectedAllFriend = function(obj,divId) {
    var checked = $(obj).attr("checked");
    if(checked) {
        FriendCustomObject.selectedDivFriend(divId);
    }
    else {
        FriendCustomObject.cancelDivFriend(divId);
    }
};

FriendCustomObject.getOneFriendHtml = function(friendData,divId) {
    var obj = $(FriendCustomObject.para.oneCustomObjectTemp);
    obj.find("li").attr("id",friendData["id"]);
    
    obj.find("a").attr("href","#");
    obj.find("a").attr("onclick","javascript:FriendCustomObject.selectedOneFriend('"+friendData["id"]+"','"+divId+"');");
    obj.find("a>img").attr("src",friendData["ico"]);
    
    obj.find("input").attr("value",friendData["id"]);
    
    var friendNameA= FriendEditContact.getFriendNameA(friendData);
    var friendName = FriendEditContact.getFriendName(friendData);
    
    obj.find("p").html(friendNameA);
    obj.find("p").attr("title",friendName);
    return obj.html();
};

FriendCustomObject.appendOneFriend = function(divId,friendData) {
    var html = FriendCustomObject.getOneFriendHtml(friendData,divId);
    var obj = $(FriendCustomObject.para.customObjectTemp+" " +divId + " ul");
    obj.append(html);
};

FriendCustomObject.appendMy = function() {
    var friendData = {};
    
};

FriendCustomObject.appendAllDivFriend = function() {
    $(FriendCustomObject.para.favorityId+">ul").empty();
    $(FriendCustomObject.para.allId+">ul").empty();
    $(FriendCustomObject.para.selectedId+">ul").empty();
    $(FriendCustomObject.para.selectedId).find("span>a").html("保存");
    FriendCustomObject.appendDivFriend(FriendCustomObject.para.favorityId);
    FriendCustomObject.appendDivFriend(FriendCustomObject.para.allId);
};

FriendCustomObject.appendDivFriend = function(divId) {
    var data = FriendCustomObject.getDivData(divId);
    for(var i=0;i<data.length;i++) {
        var friend = data[i];
        var friendId = friend.id;
        var friendName = friend.name;
        var friendAlias = friend.friend.alias;
        if(friendAlias == null)friendAlias="";
        var friendFrequently = friend.friend.frequently;
        var ico = friend.avatar.small;
        
        var friendData = {};
        friendData["id"] = friendId;
        friendData["name"] = friendName;
        friendData["alias"] = friendAlias;
        friendData["frequently"] = friendFrequently;
        friendData["ico"] = ico;
        
        FriendCustomObject.appendOneFriend(divId,friendData);
    }
};

FriendCustomObject.getDivData = function(divId) {
    var data;
    if(divId == FriendCustomObject.para.allId) {
        data = FriendCustomObject.para.gFriendData;
    }
    else if(divId == FriendCustomObject.para.favorityId) {
        data = FriendCustomObject.getFavoriteData(FriendCustomObject.para.gFriendData);
    }
    return data;
};

FriendCustomObject.getFavoriteData = function(data) {
    var fdata = new Array();
    if(data.length <1)return fdata;
    for(var i=0;i<data.length;i++) {
        var friend = data[i];
        var friendFrequently = friend.friend.frequently;
        if(friendFrequently) {
            fdata[fdata.length] = friend;
        }
    }
    return fdata;
}

FriendCustomObject.searchFriends = function() {
    FriendCustomObject.searchDivFriend(FriendCustomObject.para.favorityId);
    FriendCustomObject.searchDivFriend(FriendCustomObject.para.allId);
    $(FriendCustomObject.para.scrollContentDiv).css("top","0px");
};

FriendCustomObject.searchDivFriend = function(divId) {
    var searchText = $.trim($(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.searchText).val());
    if(FriendCustomObject.para.defaultSearchText == searchText) {
        return;
    }
    FriendCustomObject.showDiv(divId);
    $(FriendCustomObject.para.customObjectTemp +" "+divId).find("li").each(function() {
        var title = $(this).find("p").attr("title");
        if(searchText == '' || title.indexOf(searchText)>-1) {
            $(this).show();
        }
        else {
            $(this).hide();
        }
    });
};

FriendCustomObject.showDiv = function(divId) {
    if(divId.indexOf("#") ==-1) divId = "#"+divId;
    var spanObj = $(FriendCustomObject.para.customObjectTemp + " " +divId+ " div>span");
    var ulObj = $(FriendCustomObject.para.customObjectTemp + " " +divId+ " ul");
    spanObj.attr("class","");
    ulObj.show();
};

FriendCustomObject.prototype = {
    init:function() {
        this.bindEventAction();
    },
    bindEventAction:function() {
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.searchText).focus(function(){
             if($(this).val() == FriendCustomObject.para.defaultSearchText) {
                $(this).val("");
             }
        });
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.searchText).keyup(function(){
             FriendCustomObject.searchFriends();
        });
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.searchTextButton).click(function(){
            FriendCustomObject.searchFriends();
        });
        
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.go2backButton).click(function() {
            FriendCustomObject.confirmSelectedFriend();
        });
        
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.go2saveButton).click(function() {
            FriendCustomObject.confirmSelectedFriend();
        });
        $(FriendCustomObject.para.customObjectTemp + " " +FriendCustomObject.para.scrollDivId).rollbar({zIndex:80,wheelSpeed:10}); 
        
    },
    getFriendsList:function() { 
        //var length = $(FriendCustomObject.para.customObjectTemp).find("li").length;
        //if(length >1) return;
        var header = cookieUtil.getHeaderToken();
        jQuery.ajax({
            url:"/service/relation/friend?r="+(Math.random()*99999999),
            type:"get",
            headers:header,
            contentType:"application/json;charset=utf-8",
            success:function(data) {
                FriendCustomObject.para.gFriendData = data.result;
                FriendCustomObject.appendAllDivFriend();
            },
        });
    }
}

var _friendCustomObject = new FriendCustomObject();