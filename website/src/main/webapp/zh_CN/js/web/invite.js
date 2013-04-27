$(document).ready(function(){
	
	$("#inviteByEmail").keyup(function(){validateEmail(false)});
	$("#inviteByEmail").blur(function(){validateEmail(true)});
	$("#inviteByMoblie").keyup(function(){validateMobile(false)});
	$("#inviteByMoblie").blur(function(){validateMobile(true)});
	
});

function invite(type){	

	var param = "";
	if(type == "Email"){		
		if(!validateEmail()){
			return;
		}
		param = "url="+$("#copy").val()+"&invitees="+$("#inviteByEmail").val()+"&type="+type+"&code="+$("#code").val();	
	}else{
		if(!validateMobile()){
			return;
		}
		param = "url="+$("#copy").val()+"&invitees="+$("#inviteByMoblie").val()+"&type="+type+"&code="+$("#code").val();		
	}
	$.ajax({
				   url:"/invite/post",
				   dataType:'json',				  
				   type:"post",
				   data:param,
				   success:function(data){				
					   if(data.actionErrors && data.actionErrors.length > 0){
						   alert(data.actionErrors().join("\r\n"));						   
						   return;
					   }
					   else if(data.actionMessages && data.actionMessages.length > 0){
							alert(data.actionMessages.join("\r\n"));							
							location.reload();
							return;
					   }else{ 
					   	if(data.auth == "failure"){
					   	    window.location.href = "/account/default/login";
					   	}else if(data.result == "success"){
						   		openMessage('success');
						   }else if(data.reason == "duplicate"){
						   		openMessage('failure','您已经邀请过该好友！');
						   	}if(data.reason == "exist"){
						   		openMessage('failure','该好友已经存在多多系统,请搜索关注吧！');
						   	}
   					  }
				   }				  
			});

}

Validator={
                   password:/^.{6,16}$/,
                   mobile:/^(13[0-9]|15[0-9]|186|187|188|189)\d{8}$/,
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/,
};
function validateEmail(displayError){
	$("#inviteByMoblie").next().remove();
	if(!Validator.email.test($("#inviteByEmail").val())){	
		if (displayError) {
			$("#inviteByEmail").next().remove();
			$("#inviteByEmail").after("<b class='Remind Remind-2'>邮箱格式不对！</b>");		
		}
		return false;
	}else{
		$("#inviteByEmail").next(".Remind-2").remove();
		return true;
	}
}

function validateMobile(displayError){
	$("#inviteByEmail").next().remove();
	if(!Validator.mobile.test($("#inviteByMoblie").val())){	
		if (displayError) {
			$("#inviteByMoblie").next().remove();
			$("#inviteByMoblie").after("<b class='Remind Remind-2'>手机号码格式不对！</b>");
		}		
		return false;
	}else{
		$("#inviteByMoblie").next(".Remind-2").remove();
		return true;
	}
}

//弹出消息框
function openMessage(type,msg) {
   var message = "";
   if(type == "success") {
       message = "邀请<strong>成功！</strong>";
   }
   else if(type == "failure") {
       message = msg;
   }
   openPopupMessage(type,message);       
}