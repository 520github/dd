function FriendEditContact() {
};

FriendEditContact.para = {
    frequentlyClass:"edit-contact-has-been",
    oneFriendTemp:"#oneFriendTemp",
    friendDiv:"#editContactTemp",
    friendScroll:"#editContactScroll",
    friendUl:"#editContactTemp .edit-contact-box .edit-contact-list",
    setTitle:"设置常用联系人",
    cancleTitle:"取消常用联系人",
    searchText:"#searchText",
    defaultSearchText:"昵称/帐号",
    searchTextButton:"#searchTextButton",
    go2backButton:"#go2back",
    go2saveButton:"#go2save",
    saveEditContactDiv:"#saveEditContact"
}

FriendEditContact.changeFriend = function(friendId) {
    var obj = $(FriendEditContact.para.friendUl).find("li[id='"+friendId+"']");
    var css = obj.find("span").attr("class");
    var title = "";
    if(css == FriendEditContact.para.frequentlyClass) {
        title = FriendEditContact.para.setTitle;
        css = "";
    }
    else {
        title = FriendEditContact.para.cancleTitle;
        css = FriendEditContact.para.frequentlyClass;
    }
    obj.find("span").attr("class",css)
    obj.find("span").attr("title",title)
    
    FriendEditContact.setSaveButtonHtml();
};

FriendEditContact.setSaveButtonHtml = function() {
    var num = FriendEditContact.getSelectedNum();
    $(FriendEditContact.para.saveEditContactDiv).find("a").html("保存("+num+")");
};

FriendEditContact.getSelectedNum = function() {
    var num = 0;
    $(FriendEditContact.para.friendUl).find("li").each(function(){
        var css = $(this).find("span").attr("class");
        if(css == FriendEditContact.para.frequentlyClass) {
            num++;
        }
    });
    return num;
}

FriendEditContact.getOneFriendHtml = function(friendData) {
    var obj = $(FriendEditContact.para.oneFriendTemp);
    obj.find("li").attr("id",friendData["id"]);
    
    obj.find("a").attr("href","javascript:FriendEditContact.changeFriend('"+friendData["id"]+"');");
    obj.find("a>img").attr("src",friendData["ico"]);
    
    if(friendData["frequently"]) {
        obj.find("span").attr("class",FriendEditContact.para.frequentlyClass);
        obj.find("span").attr("title",FriendEditContact.para.cancleTitle);
    }
    else {  
        obj.find("span").attr("class","");
        obj.find("span").attr("title",FriendEditContact.para.setTitle);
    }
    
    var friendNmaeA = FriendEditContact.getFriendNameA(friendData);
    var friendNmae = FriendEditContact.getFriendName(friendData);
    obj.find("p").html(friendNmaeA);
    obj.find("p").attr("title",friendNmae);
    return obj.html();
};

FriendEditContact.getFriendNameA = function(friendData) {
    var friendName = "";
    friendName = $.trim(friendData["name"]);
    var alias = $.trim(friendData["alias"]);
    var maxLength = 12;
    
    if(friendName.length + alias.length < maxLength) {
        if(alias.length >0) friendName = friendName + "(" + alias +")";
        return friendName;
    }
    
    if(friendName.length < maxLength ){
        if(alias.length >0) friendName = friendName + "(" + alias.substring(0,maxLength-friendName.length) +"...)";
    }
    else {
        friendName = friendName.substring(0,maxLength)+"...";
    }
    return friendName;
};

FriendEditContact.getFriendName = function(friendData) {
    var friendName = "";
    friendName = friendData["name"];
    if($.trim(friendData["alias"]).length >0) {  
        friendName = friendName + "(" + friendData["alias"] +")";
    }
    return friendName;
};

FriendEditContact.addOneFriend = function(friendData) {
    var html = FriendEditContact.getOneFriendHtml(friendData);
    $(FriendEditContact.para.friendUl).append(html);
};

FriendEditContact.showFriendsList = function(data) {
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
        FriendEditContact.addOneFriend(friendData);
    }
    FriendEditContact.setSaveButtonHtml();
};

FriendEditContact.searchFriends = function() {
    FriendEditContact.searchDivFriend(FriendEditContact.para.friendUl);
};

FriendEditContact.searchDivFriend = function(divId) {
    var searchText = $.trim($(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.searchText).val());
    if(FriendEditContact.para.defaultSearchText == searchText) {
        return;
    }
    $(divId).find("li").each(function() {
        var title = $(this).find("p").attr("title");
        if(searchText == '' || title.indexOf(searchText)>-1) {
            $(this).show();
        }
        else {
            $(this).hide();
        }
    });
};

FriendEditContact.go2setUp = function() {
    $("#setUpTemp").show();
    $("#editContactTemp").hide();
}

FriendEditContact.prototype = {
    init:function() {
        this.bindEventAction();
    },
    bindEventAction:function() {
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.searchText).focus(function(){
             if($(this).val() == FriendEditContact.para.defaultSearchText) {
                $(this).val("");
             }
        });
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.searchText).keyup(function(){
             FriendEditContact.searchFriends();
        });
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.searchTextButton).click(function(){
            FriendEditContact.searchFriends();
        });
        
        
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.go2backButton).click(function() {
            FriendEditContact.go2setUp();
        });
        
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.go2saveButton).click(function() {
            _friendEditContact.saveFriendsEdit();
        });
        $(FriendEditContact.para.friendDiv + " " +FriendEditContact.para.friendScroll).rollbar({zIndex:80,wheelSpeed:10}); 
        
    },
    getFriendsList:function() {
        //var length = $(FriendEditContact.para.friendUl).find("li").length;
        //if(length > 0)return;
        $(FriendEditContact.para.friendUl).empty();
        var header = cookieUtil.getHeaderToken();
        jQuery.ajax({
            url:"/service/relation/friend?r="+(Math.random()*99999999),
            type:"get",
            headers:header,
            contentType:"application/json;charset=utf-8",
            success:function(data) {
                FriendEditContact.showFriendsList(data.result);
            },
        });
    },
    saveFriendsEdit:function() {
        var header = cookieUtil.getHeaderToken();
        var data = this.getFriendEditData();
        jQuery.ajax({
            url:"/service/relation/friend?r="+(Math.random()*99999999),
            type:"put",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(data),
            dataType:"json",
            headers:header,
            success:function() {
                $("#editContactTemp").show();
                $("#setUpTemp").show();
            }
        });
    },
    getFriendEditData:function() {
        var datas = new Array();
        $(FriendEditContact.para.friendUl).find("li").each(function(){
            var friendId = $(this).attr("id");
            var frequently = false;
            var css = $(this).find("span").attr("class");
            if(FriendEditContact.para.frequentlyClass == css) {
                frequently = true;
            }
            
            var data = {
                account:friendId,
                frequently:frequently
            };
            datas[datas.length] = data;
        });
        return datas;
    }
}

var _friendEditContact = new FriendEditContact();
//eventUtil.bind(window,"load",_friendEditContact.init());