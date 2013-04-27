function Settings() {
    setUpTemp = "#setUpTemp .drag";
    favoritesAndMeId = "#FavoritesAndMe";
    allId = "#All";
    meId = "#Me";
    pushTypeValue="#pushTypeValue";
}

Settings.selectGroup = function(group) {
    $(setUpTemp).find(pushTypeValue).attr("value",group);
    $(setUpTemp).find("div>div").attr("class","drag-list1 drag-list2");
    $(setUpTemp).find("#"+group).attr("class","drag-list1 bak-color");
    _settings.setPushCommon();
};

Settings.selectedPushType = function(id) {
    $(setUpTemp).find("div>div").attr("class","drag-list1 drag-list2");
    $(setUpTemp).find("#"+id).attr("class","drag-list1 bak-color");
    $(setUpTemp).find(pushTypeValue).attr("value",id);
};

Settings.canclePushType = function() {
    $("#dragActionId").show();
    $("#setUpTemp").hide();
    $("#editContactTemp").hide();
};

Settings.prototype = {
    getSharingGroup:function() {
        var header = cookieUtil.getHeaderToken();
        jQuery.ajax({
            url:"/service/setting/sharing?r="+(Math.random()*99999999),
            type:"get",
            headers:header,
            contentType:"application/json;charset=utf-8",
            success:function(data) {
                Settings.selectGroup(data.defaultGroup);
            },
        });
    },
    saveSharingGroup:function() {
        this.setPushCommon();
        $("#dragActionId").show();
        $("#setUpTemp").hide();
        var header = cookieUtil.getHeaderToken();
        var data = this.getSharingGroupData();
        jQuery.ajax({
            url:"/service/setting/sharing?r="+(Math.random()*99999999),
            type:"put",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(data),
            dataType:"json",
            headers:header,
            success:function() {
                Settings.selectGroup(data.defaultGroup);
                Settings.canclePushType();
            },
            error:function() {
                Settings.canclePushType
            }
        });
    },
    setPushCommon:function() {
        var value = $(setUpTemp).find(pushTypeValue).attr("value");
        //alert($(previewConstant.dragActionId + " " +previewConstant.push2CommonDivId).find("input").attr("value"));
        $(previewConstant.dragActionId + " " +previewConstant.push2CommonDivId).find("input").attr("value",value);
        if(value == "Me") {
            value = "仅我自己";
        }
        else if(value == "All") {
            value = "我和我的全部好友";
        }
        else if(value == "FavoritesAndMe") {
           value = "我和我的常用联系人";
        }
        var html = $(previewConstant.dragActionId + " " +previewConstant.push2CommonDivId).find("h3").html();
        if(html.indexOf("<") >-1)html = html.substring(html.indexOf("<"));
        html = value + html;
        $(previewConstant.dragActionId + " " +previewConstant.push2CommonDivId).find("h3").html(html); 
    },
    getSharingGroupData:function() {
        var value = $(setUpTemp).find(pushTypeValue).val();
        var data ={
            defaultGroup:value
        }
        return data;
    },
    getToken:function() {
        var cookValue = document.cookie;
	    var name = "authToken=";
	    var start = this.getCookieStart(name);
	    if(start < 0)return "";
	    start = start + name.length;
	    var end  = this.getCookieEnd(name);
	    
	    cookValue = unescape(cookValue.substring(start,end));
	    return cookValue;
    },
    getCookieStart:function(name) {
        return document.cookie.indexOf(name);
    },
    getCookieEnd:function(name) {
        var start = getCookieStart(name);
	    start = start + name.length;
	    var end = document.cookie.indexOf(";",start);
	    if(end ==-1)end = document.cookie.length;
	    return end;
    }
}

_settings = new Settings();
