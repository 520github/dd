Validator={
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/,
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
  			url: '/service/accounts/login/'+ encodeURIComponent($("#"+id).val()),
  			async: false,
  			success : function(data) {
      			$("#"+id).next().remove();
				$("#"+id).after("<b class='Remind Remind-2'>该邮箱已注册，请更换邮箱</b>");
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
	$("#email").keyup(function(){validateEmail(false)});
	$("#email").blur(function(){validateEmail(true)});
	$("#agree").attr("checked", "checked");
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateEmail(true)){
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
				//alert(data.msg);
				openMessage("failure", data.msg);
			}else{
				window.location.href="/account/default/email/?emailServer="+encodeURIComponent(data.mailServer)+"&email="+encodeURIComponent($("#email").val()) + "&u=" + $("#type").val();
			}
		}
	});
	
});