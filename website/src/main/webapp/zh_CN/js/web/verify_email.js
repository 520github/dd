function reSendVerifyEmail(email, invite, u){
	$.blockUI();
	var post=$.ajax({
		"cache":false,
		"type":"POST",
		"dataType":"json",
		"url":"/account/default/applyVerify/email",
		data:{"email":email, "invite":invite, "u":u},
		success:function(data){
				$.unblockUI();
				if(data.msg){
					openMessage("failure", data.msg);
				}else{
					openMessage("success", "邮件发送成功，请查收。");
				}
		}
	});
}


function openMessage(type,msg) {
    var message = "";
    if(type == "success") {
        message = "<strong>"+msg+"</strong>";
    
    } else if(type == "failure") {
        message = msg;
    }
    openPopupMessage(type,message);   
}
