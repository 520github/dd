Validator={
                   password:/^.{6,16}$/,
                   mobile:/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/,
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/
};

function validateName(displayError){
	var id="name";
	var error="msg_name";
	if($("#"+id).val().replace(/(^[\s]*)|([\s]*$)/g, "")==""){
		if(displayError){
			$("#"+error).addClass("Remind-2").html("请输入昵称");
		}
		return false;
	}else if (!Validator.name.test($("#"+id).val())) {
		if(displayError){
			$("#"+error).addClass("Remind-2").html("只支持2到16位中英文、数字、下划线或减号");
		}
		return false;
	} else {
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/service/accounts/name/'+ encodeURIComponent($("#"+id).val()),
  			async: false,
  			success : function(data) {
  				$("#"+error).addClass("Remind-2").html("该昵称已注册，请更换昵称");
    			flag = false;
    		}
		});
		}
		if (flag) {
		$("#"+error).removeClass("Remind-2").html("2-16个字符，支持中英文、数字 、下划线或减号");
		}
		return flag;
	}
}

function validateEmail(displayError){
	var id="email";
	var error="msg_email";
	if(!Validator.email.test($("#"+id).val())&&!Validator.mobile.test($("#"+id).val())){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind-2'>请输入正确的邮箱地址或者手机号</b>");
		}
		return false;
	}else{
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/service/accounts/login/'+ encodeURIComponent($("#"+id).val()),
  			async: false,
  			success : function(data) {
  				$("#"+id).next().remove();
  				$("#"+id).after("<b class='Remind-2'>已经被占用，请重新填写</b>");
    			flag = false;
    		}
		});
		}
		if (flag) {
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind'>请填写有效邮箱地址或者手机号，它将成为你登录多多的帐号！</b>");
		}
		return flag;
	}
}


function validateconfirmPassword(displayError){
	var id="confirmpassword";
	var error = "msg_pwdconfirm";
	if($("#"+id).val() != $("#password").val()){
		if(displayError){
			$("#"+error).addClass("Remind-2").html("密码不一致");
		}
		return false;
	}else{
		$("#"+error).removeClass("Remind-2").html("请再次填写密码");
		return true;
	}
}

function validatePassword(displayError){
	var id="password";
	var error = "msg_pwd";
	if(!Validator.password.test($("#"+id).val())){
		if(displayError){
			$("#"+error).addClass("Remind-2").html("请输入6到16位字符");
		}
		return false;
	}else{
		$("#"+error).removeClass("Remind-2").html("长度为6-16位字符，区分大小写");
		return true;
	}
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

$(document).ready(function(){
	//$("#email").keyup(function(){validateEmail(false)});
	$("#email").blur(function(){validateEmail(true)});
	$("#password").keyup(function(){validatePassword(false)});
	$("#password").blur(function(){validatePassword(true)});
	$("#confirmpassword").keyup(function(){validateconfirmPassword(false)});
	$("#confirmpassword").blur(function(){validateconfirmPassword(true)});
	$("#agree").attr("checked", "checked");
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateEmail(true) || !validatePassword(true) || !validateconfirmPassword(true)){
				return false;
			}
			
			if(!$("#agree").attr("checked")){
				//alert("请同意《两杯咖啡网络服务协议》，完成注册");
				openMessage("failure", "请同意《两杯咖啡网络服务协议》，完成注册");
				return false;
			}
			$.blockUI();
			return true;
		},success:function(data){
			$.unblockUI();
			if(data.error){
				//alert(data.msg);
				openMessage("failure", data.error);
			} else {
				window.location.href=data.url;
			}
		}
	});
	
});