Validator={
                   password:/^.{6,16}$/,
                   name:/^(-|\w|[\u4e00-\u9fa5]){2,16}$/,
                   emailName: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]$/
};
function validateEmailName(displayError){
	var id="duoduoEmail";
	if(!Validator.emailName.test($("#"+id).val())){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>请输入正确的邮箱地址</b>");
		}
		return false;
	} else{
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/account/default/coffeemail/?coffeemail='+ encodeURIComponent($("#"+id).val() + "@"+systemConstant.mailSuffix),
  			async: false,
  			success : function(data) {
  				if (data.error) {
      				$("#"+id).next().remove();
					$("#"+id).after("<b class='Remind Remind-2'>该邮箱已被占用，请更换邮箱</b>");
    				flag = false;
    			}
    		}
		});
		}
		if (flag) {
		$("#"+id).next(".Remind-2").remove();
		}
		return flag;
	}
}

function validatePassword(displayError){
	var id="password";
	if(!Validator.password.test($("#"+id).val())){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>请输入6到16位字符</b>");
		}
		return false;
	}else{
		$("#"+id).next(".Remind-2").remove();
		return true;
	}
}

function validateName(displayError){
	var id="name";
	if($("#"+id).val().replace(/(^[\s]*)|([\s]*$)/g, "")==""){
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>请输入昵称</b>");
		}
		return false;
	}else if (!Validator.name.test($("#"+id).val())) {
		if(displayError){
			$("#"+id).next().remove();
			$("#"+id).after("<b class='Remind Remind-2'>只支持2到16位英文、数字、中文、“_”、“-”</b>");
		}
		return false;
	} else {
		var flag = true;
		if (displayError) {
		$.ajax({
  			url: '/service/accounts/name/'+ encodeURIComponent($("#"+id).val()),
  			async: false,
  			success : function(data) {
      			$("#"+id).next().remove();
				$("#"+id).after("<b class='Remind Remind-2'>该昵称已注册，请更换昵称</b>");
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

function doLogin() {
	window.location= "/account/default/login/";
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
	$("#password").keyup(function(){validatePassword(false)});
	$("#password").blur(function(){validatePassword(true)});
	$("#name").keyup(function(){validateName(false)});
	$("#name").blur(function(){validateName(true)});
	$("#duoduoEmail").keyup(function(){validateEmailName(false)});
	$("#duoduoEmail").blur(function(){validateEmailName(true)});
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validatePassword(true) || !validateName(true) || !validateEmailName(true)){
				return false;
			}
			$.blockUI();
			return true;
		},success:function(data){
			$.unblockUI();
			if(data.error){
				//alert(data.error);
				openMessage("failure", data.error);
			}else{
				window.location.href=data.url;
			}
		}
	});
});