Validator={
                   password:/^.{6,16}$/,
                   mobile:/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/,
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)+$/
};

function validateLoginname(displayError){
	if(!Validator.email.test($("#loginName").val()) && !Validator.mobile.test($("#loginName").val())){
		if (displayError) {
			//$("#loginName").next().remove();
			$("#uerror").html("登录邮箱/手机号 格式不正确");
		}
		return false;
	}else{
		$("#uerror").html('');
		return true;
	}
}

function validatePassword(displayError){
	var id="password";
	if(!Validator.password.test($("#"+id).val())){
		if(displayError){
			//$("#"+id).next().remove();
			$("#perror").html("请输入6到16位字符");
		}
		return false;
	}else{
		$("#perror").html("");
		return true;
	}
}

function doReg(target) {
    if(target == null || target =='') {
        window.location= "/account/default/register/";
    }else {
        window.open("/account/default/register/");
    }
	
}

function openMessage(type,msg) {
	if(msg=='false'){
	 return;
	}
    var message = "";
    if(type == "success") {
        message = "<strong>"+msg+"</strong>";
    
    } else if(type == "failure") {
        message = msg;
    }
    openPopupMessage(type,message);   
}

$(document).ready(function(){
	if ($("#error").val() != "") {
		//alert($("#error").val());
		openMessage("failure", $("#error").val());
	}
	$("#loginName").keyup(function(){validateLoginname(false)});
	$("#loginName").blur(function(){validateLoginname(true)});
	$("#password").keyup(function(){validatePassword(false)});
	$("#password").blur(function(){validatePassword(true)});
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateLoginname(true) || !validatePassword(true)){
				return false;
			}
			$.blockUI();
			return true;
		},success:function(data){
			$.unblockUI();
			if(data.error){
				openMessage("failure", data.error);
			}else{
			    if($("#pluginLogin").val() == "true") {
			        window.location = "/newbookmark/private/portal";
			    }
			    else {
			        window.location= data.url;
			    }				
			}
		}
	});

});