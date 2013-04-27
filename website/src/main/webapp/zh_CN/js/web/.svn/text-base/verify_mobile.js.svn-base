function reSendVerifySMS(mobile){
	$.blockUI();
	$.ajax({
		"cache":false,
		"type":"POST",
		"dataType":"json",
		"url":"/account/default/applyVerify/sms",
		data:{"mobile":mobile},
		success:function(data){
				$.unblockUI();
				if(data.msg){
					alert(data.msg);
				}else{
					alert("短信发送成功，请查收。");
				}
		}
		
	});
}


function verifySMSToken(){
	
	var token=$("#mobileToken").val();
	
	if(token==''){
		alert("请先输入短信验证码！");
		return false;
	}
	
	$.ajax({
		"cache":false,
		"type":"POST",
		"dataType":"json",
		"url":"/account/default/verifyMobile",
		data:{"token":token},
		success:function(data){
				$.unblockUI();
				if(data.msg){
					alert(data.msg);
				}else{
					window.location.href="/account/default/domain/";
				}
		}
		
	});
}
