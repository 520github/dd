jQuery(function(){
	jQuery(".pop-up-window").css("display","none");
	authToken = jQuery.cookie("WebToken");
	if(!authToken){
		return;
	}
	pollNotification();
})
var authToken;
var interval = 100000;
var curSinceId = '';
function pollNotification(){
	
	var flag = $('#isPoll').val(); //为了解决注册跳转页面，不接收通知问题（注册跳转后已是合法用户）。
	if(flag=="false"){
		return;
	}
	
	var param = jqueryCookie("sinceId")==null?"":jqueryCookie("sinceId");
	var url = "/service/notification/";
	if(param!= "\"\"" && jQuery.trim(param).length > 1) {
	    url = url + "?since_id=" +param ;
	}else{
		url = url + '?r='+ Math.random()*99999999;
	}
	url += "&displayType=web";
	jQuery.ajax({
				url: jQuery.trim(url),
				type:"get",
				headers:cookieUtil.getHeaderToken(),
				contentType: "application/json",
				dataType: "json",
				success: function(data, textStatus, jqXHR){
					//console.log(data);
					if(data && data.notification&&data.notification.length>0){
						popup(data.notification,data.badge);
					}else{
						jQuery(".pop-up-window").css("display","none");
					}
					var itval = jqXHR.getResponseHeader("X-Next-Request-After");
					if(itval){
						interval = parseInt(itval)*1000;
					}
					setTimeout("pollNotification()",interval);
					getMsgCount();
				},
				error:function(jqXHR, textStatus, errorThrown){
					setTimeout("pollNotification()",interval);
				}
	});
}


function getMsgCount(){
       jQuery.ajax({
		url : "/friend/Count",
		headers:cookieUtil.getHeaderToken(),		
		type : "get",
		dataType : "json",
		cache : false,
		success : function(jsonObj) {
	      $('#msg').html(jsonObj.msgCount);
	      $('#list').html(jsonObj.friendCount);
		}
	})
   
   
   }



function popup(ns,badge){
	curSinceId = ns.length==0?curSinceId:ns[ns.length-1].id;
	var hasMessage = false;
	jQuery(".pop-up-window>ul>li").remove();
	for(var i in ns){
		var subject;
		if(ns[i].key=="FriendMessage"&&badge.message.friendMessage>0){
			subject=badge.message.friendMessage+"条好友消息";
		}else if(ns[i].key=="Chat"&&badge.chat>0){
		   subject=badge.chat+"条新评论";
		}else if(ns[i].key=="RecommendByFriend"&&badge.folder.friend>0){
		 subject=badge.folder.friend+"条好友推荐内容";
		}else {
		 continue;
		}
		if(badge.total>0){
		var li = '<li>'+subject+' <a href="#" id="'+ns[i].id+'" onclick="lookOver(\''+ns[i].id+'\',\''+ns[i].action+'\')">查看</a></li>'
		jQuery(".pop-up-window>ul").append(li);
		hasMessage = true;
		}
	}
	
	if (hasMessage) {
		jQuery(".pop-up-window").fadeIn();
	}
}

function closePopup(){
	jQuery(".pop-up-window").css("display","none");
	jQuery(".pop-up-window>ul>li").remove();
	jqueryCookie("sinceId",curSinceId,{domain:systemConstant.cookieDomain})
}

function lookOver(nid,action){
	jQuery.ajax({
				url: "/service/notification/"+nid,
				type:"delete",
				headers:cookieUtil.getHeaderToken(),
				contentType: "application/json",
				dataType: "json",
				complete:function(jqXHR, textStatus){
					jQuery("#"+nid).parent().remove();
					if(jQuery(".pop-up-window>ul>li").length==0){
						jQuery(".pop-up-window").css("display","none");
					}
					location.href=actionMapping(action);
														
				}
	});
}
function actionMapping(action){
	//alert(action);
	var pattern_content = /content(?!Id)/; //现好友推荐的内容uri为 iicoffee://view/content/repositeId
	var pattern_chat = /chat/;	//	iicoffee://view/chat/;contentId=5029bc91dca3b66f3206bba3;accountId=500509bb210c9856981f3990
	if(action=='iicoffee://view/relation/friend/'||action=='iicoffee://view/relation/friend/message/'){
		return '/friend/message'
	}else if(action=='iicoffee://view/message/'){
		return '/messages'
	}else if(action=='iicoffee://view/tag/readLater/'){
		return '/private/later'
	}else if(pattern_content.test(action)){
		return '/private/friend'
	}else if(pattern_chat.test(action)){
		return '/message/private/session/view'
	}else{
		return '#';
	}
}