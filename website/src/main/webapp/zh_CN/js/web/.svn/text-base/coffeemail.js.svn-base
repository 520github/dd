Validator={
                   emailName: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]$/
};
function validateEmailName(displayError){
	var id="email";
	var error="msg_dm";
	if(!Validator.emailName.test($("#"+id).val())){
		if(displayError){
			$("#"+error).html("<b class='Remind Remind-2'>请输入正确的邮箱地址</b>");
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
      				$("#"+error).html("<b class='Remind Remind-2'>该邮箱已被占用，请重新填写</b>");
    				flag = false;
    			}
    		}
		});
		}
		if (flag) {
		$("#"+error).html('<b class="Remind">请谨慎填写，确定后不可以再次修改</b>');
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
	$("#email").keyup(function(){validateEmailName(false)});
	$("#email").blur(function(){validateEmailName(true)});
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateEmailName(true)){
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