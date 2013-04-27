var sysSecond; 
var interValObj; 
Validator={
                   mobile:/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/,
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/
};

function validateEmail(displayError){
	var id="email";
	if(!Validator.email.test($("#"+id).val())){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>请输入正确的邮箱地址</b>");
		}
		return false;
	}else{
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/service/accounts/login/'+ encodeURIComponent(getLoginName()),
  			async: false,
  			success : function(data) {
      			$("#"+id).next().remove();
				$("#"+id).after("<b class='Remind Remind-2'>"+getLoginNameDes()+" 被占用，请重新填写</b>");
    			flag = false;
    		}
		});
		}
		if (flag) {
		$("#"+id).next(".Remind-2").remove();
		}
		return flag;
	}
}

function getLoginName() {
	
	if ("email" == $("#registerType").val()) {
		return $("#email").val();
	}
	return $("#mobile").val();
}

function getLoginNameDes() {
	
	if ("email" == $("#registerType").val()) {
		return "邮箱";
	}
	return "手机号";
}

function validateMobileCode(displayError){
	var id = "mobile2";
	if($("#"+id).val() == ""){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>请输入短信验证码</b>");
		}
		return false;
	}else{
		$("#"+id).next(".Remind-2").remove();
		return true;
	}
}

function validateMobile(displayError){
	var id="mobile";
	if(!Validator.mobile.test($("#"+id).val())){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>手机号码格式不正确</b>");
		}
		return false;
	}else{
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/service/accounts/login/'+ encodeURIComponent(getLoginName()),
  			async: false,
  			success : function(data) {
      			$("#"+id).next().remove();
				$("#"+id).after("<b class='Remind Remind-2'>"+getLoginNameDes()+" 被占用，请重新填写</b>");
    			flag = false;
    		}
		});
		}
		if (flag) {
		$("#"+id).next(".Remind-2").remove();
		}
		return flag;
	}
}

function validateLoginname(){
	if($("#registerType").val()=='mobile'){
		return validateMobile(true) && validateMobileCode(true);
	}else{
		return validateEmail(true);
	}
}

function initRegTypeTrigger(){
	$("#mailRegHiddenLi").click(function(){
		$("#mailRegHiddenLi").css("display","none");
		$("#mailRegActiveLi").css("display","block");
		$("#mobileRegActiveLi").css("display","none");
		$("#mobileRegHiddenLi").css("display","block");
		$("#emailContainer").css("display","block");
		$("#mobileContainer").css("display","none");
		$("#mobileButton").css("display","none");
		$("#mobileCode").css("display","none");
		$("#bottom").css("display","none");
		$("#emailDes").css("display","block");
		$("#mobileDes").css("display","none");
		$("#registerType").val("email");
	});
	
	$("#mobileRegHiddenLi").click(function(){
		$("#mailRegHiddenLi").css("display","block");
		$("#mailRegActiveLi").css("display","none");
		$("#mobileRegActiveLi").css("display","block");
		$("#mobileRegHiddenLi").css("display","none");
		$("#emailContainer").css("display","none");
		$("#mobileContainer").css("display","block");
		$("#mobileButton").css("display","block");
		$("#mobileCode").css("display","block");
		$("#bottom").css("display","none");
		$("#emailDes").css("display","none");
		$("#mobileDes").css("display","block");
		$("#registerType").val("mobile");
	});
}

function doLogin() {
	window.location= "/account/default/login/";
}

function sendMobileCode() {
	if (!validateMobile(true)) {
		return false;
	}
	$.ajax({
		  url: "/account/default/sms/",
		  dataType:'json',
		  async: false,
		  data:{"mobile":$("#mobile").val()},
		  beforeSend:function() {
		  	$.blockUI();
			return true;
		  },
		  success:function(data) {
		  	  $.unblockUI();
		  	  if (data.msg) {
		  	  	   $("#bottomText").html("已向手机号"+$("#mobile").val()+"发送了一条验证短信。");
		  	 	   $("#bottom").css("display","block");
		  	  	   openMessage("success", data.msg);
		  	  	   $("#sendButton").css("display","none");
		  	  	   $("#timerButton").css("display","block");
		  	  	   sysSecond = 60;
                   interValObj = window.setInterval(SetRemainTime, 1000); //间隔函数，1秒执行 
		  	  }
		  	  if (data.error) {
		  	       $("#bottom").css("display","none");
		  	  	   openMessage("failure", data.error);
		  	  }
		  }
	});
}

function SetRemainTime() { 
    if (sysSecond > 0) { 
        sysSecond = sysSecond - 1; 
        $("#remainSecond").html(sysSecond); 
    } else {//剩余时间小于或等于0的时候，就停止间隔函数 
        window.clearInterval(interValObj); 
   	    $("#sendButton").css("display","block");
		$("#timerButton").css("display","none");
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
	$("#agree").attr("checked", "checked");
	initRegTypeTrigger();
	var index = location.href.indexOf("?");
	
	if (index > 0) {
	var queryString = location.href.substring(index+1); 
	if (queryString.length > 0) {
		var pos = queryString.indexOf("=");
		var end = queryString.indexOf("#");
	
		if (end <= 0) {
			end = queryString.length;
		}
		var paraName = queryString.substring(0, pos);          
		var paraValue = queryString.substring(pos + 1, end);
		$("#invite").val(paraValue);	
	}
	}
	$("#email").keyup(function(){validateEmail(false)});
	$("#email").blur(function(){validateEmail(true)});
	$("#mobile").keyup(function(){validateMobile(false)});
	$("#mobile").blur(function(){validateMobile(true)});
	$("#mobile2").keyup(function(){validateMobileCode(false)});
	$("#mobile2").blur(function(){validateMobileCode(true)});
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateLoginname()){
				return false;
			}
			if(!$("#agree").attr("checked")){
				//alert("请同意《多多网络服务协议》，完成注册");
				openMessage("failure", "请同意《多多网络服务协议》，完成注册");
				return false;
			}
			$.blockUI();
			return true;
		},success:function(data){
			$.unblockUI();
			if(data.msg){
				//alert(data.msg);failure
				openMessage("failure", data.msg);
			}else{
				if($("#registerType").val()=='email'){
					window.location.href="/account/default/email/?emailServer="+encodeURIComponent(data.mailServer)+"&email="+encodeURIComponent($("#email").val())+"&invite=" + $("#invite").val();
					
				}else{
					window.location.href=data.url;
				}
			}
		}
	});
});