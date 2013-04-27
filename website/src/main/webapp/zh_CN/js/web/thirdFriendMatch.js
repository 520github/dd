var token;
var name;

function showInviteView() {

	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#invite',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}

function finish() {
	showBroadcast();
}

function showBroadcast() {

	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#sendBroadcast',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}

function closeBroadcast(reffer) {
	layer_close();

	if (opener && opener.document) {
		opener.document.location.reload();
		window.close();
	
	} else if (reffer) {
		window.location.href=reffer;
	
	} else {
		window.location.href='/home';
	}
}

function sendBroadcast(token, reffer) {
	var head = {"content-type":"application/json","Authorization":cookieUtil.getWebToken()};
	var flag = false;
	$.ajax({
				   url:"/service/accounts/broadcast/weibo",
				   dataType:'json',				  
				   type:"post",
				   headers:head,
				   async:false,	
				   success:function(data, status, xhr){
				   		
				       if (xhr.readyState ==4 && xhr.status == 470) {
				           flag = true;
				       } else if (opener && opener.document) {
							opener.document.location.reload();
							window.close();
	
						} else if (reffer) {
							window.location.href=reffer;
						} else {
							window.location.href='/home';
						}
				   },
				   error:function(xhr) {
				  
				       if (xhr.readyState ==4 && xhr.status == 470) {
				           flag = true;
				       } else if (opener && opener.document) {
							opener.document.location.reload();
							window.close();
	
						} else if (reffer) {
							window.location.href=reffer;
						} else {
							window.location.href='/home';
						}
				   }				  
			});
			
			if (flag) {
				window.open("/account/weibo/oauth/authority?authToken=" + token, "_blank");
			}
}

function sendInvite() {
	var head = {"content-type":"application/json","Authorization":cookieUtil.getWebToken()};
	var param = {
			  content:jQuery("#inviteMessage").val()			 
		   };	
	var flag = false;
	$.ajax({
				   url:"/service/accounts/invite/weibo",
				   dataType:'json',				  
				   type:"post",
				   data:JSON.stringify(param),
				   headers:head,	
				   async:false,			  
				   success:function(data, status, xhr){
				   
				       if (xhr.readyState ==4 && xhr.status == 470) {
				       	   flag = true;
				       } else if (xhr.readyState ==4 && xhr.status == 406) {
				           openMessage("error", "邀请失败！");
				       } else {		 	
					       openMessage("success", "邀请成功！");
					  }
				   },
				   error:function(xhr) {
				   
				       if (xhr.readyState ==4 && xhr.status == 470) {
				           flag = true;
				       } else if (xhr.readyState ==4) {
				           openMessage("error", "邀请失败！");
				       }
				   }
				     
			});
			
			if (flag) {
				window.open("/account/weibo/oauth/authority?authToken=" + token, "_blank");
			}
}

function bind(token, messageId) {
	window.open("/account/weibo/oauth/authority?authToken=" + token);
	action("Bind", token, messageId);
}

function action1(value,token,msgId,href){	

   var head = {"content-type":"application/json","Authorization":cookieUtil.getWebToken()};
	 var param = {
			  action:value			 
		   };	
			
	$.ajax({
				   url:"/service/message/"+msgId,
				   dataType:'json',				  
				   type:"post",
				   data:JSON.stringify(param),
				   headers:head,				  
				   success:function(data){
				   	if (href) {
				   	window.location.href=href;
				   	}else {				
					  window.location.reload();}
				   }				  
			});
}

function inviteFriend(tokenv, namev) {
	token = tokenv;
	name = namev;
	jQuery("#inviteMessage").val("@" + namev + " 我正在使用多多。它可以一键推送网上内容给自己或好友的手机，并且轻松同步电脑和手机里的照片、文件、视频。你加入多多后，咱两就可以相互分享喜欢的内容了。快来体验吧：");
	initValueSize();
	showInviteView();
}

function auth(tokenv) {
	window.open("/account/weibo/oauth/authority?authToken=" + tokenv);
}

function showThirdFriend(token,msgId) {
	action1('ShowThirdpartyFriend',token,msgId,'/friend/weibo');
}

function download(token, msgId) {
	window.open("/about/tools");
	action('ShowThirdpartyFriend',token,msgId);
}


function getLength(str){

	var l = str.length;
	var n = l
	for (var i = 0; i < l; i++) {
		if (str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255)
			n++
	}
	return parseInt(n/2,10);
}


function checkValueSize(){
	var message = $('#inviteMessage').val("");
	var max_size = 120;
	var org_size;
	var t_size;
	$("#inviteMessage").keyup(function(){
		var message = $('#inviteMessage').val();
		org_size = getLength(message);
		t_size = max_size-org_size;
		if(t_size>0){
		$('#message_size').html(t_size+'/120');
		}else if(t_size==0){
			$('#message_size').html('<font color="red">'+t_size+'</font>/120');	
		}else{
		  openMessage("failure","不可以再输入了!");
		  $("#inviteMessage").val(message.substring(0,message.length-1));
		  return;
		}
	})

}

function initValueSize() {
		var max_size = 120;
		var message = $('#inviteMessage').val();
		var org_size = getLength(message);
		var t_size = max_size-org_size;
		if(t_size>0){
		$('#message_size').html(t_size+'/120');
		}else if(t_size==0){
			$('#message_size').html('<font color="red">'+t_size+'</font>/120');	
		}
}

function addThirdFriend(thirdType) {
	var regMessage=/[''"":;\.\/<>~!@#%&]/g;
	$("#showDialog_message").val($.trim($("#showDialog_message").val()).replace(regMessage,"").substring(0, 140));
	$("#showDialog_remark").val($("#showDialog_remark").val().replace(/ /g,""));
	var str=$.trim($("#showDialog_remark").val());
	if(str==initShowDialog_remark.prototype.str){
		str="";
	}
	var message=$("#showDialog_message").val();
	if(message==initShowDialog_message.prototype.str){
		message="";
	}
	layer_close();
	jQuery.post("/friend/add", {friendId:$("#showDialog_accountId").val(),remark:str,postscript:message,thirdpartyType:thirdType}, function(data) {
		if (data == null || data == "") {
			openMessage("failure", "网络错误。");
		}else if (data == "0" || data == "2") {
			openMessage("failure", "添加失败。");
		}else if (data == "1") {
			openMessage("success", "已发送好友邀请。");
		}else if (data == "3") {
			openMessage("success", "不能添加自己。");
		}
	});
}

$(function(){
  checkValueSize();
})