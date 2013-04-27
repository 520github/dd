Validator={
                   name:/^[0-9a-zA-Z_\.]{4,20}$/
};

function validateEmail(displayError){
	var id="email";
	if(!Validator.name.test($("#"+id).val())){
		if(displayError){
			$("#msgEmail").html("请使用长度为4~20的数字、字母，支持点和下划线。");
		}
		return false;
	}else{
		$("#msgEmail").html("");
		return true;
	}
}
function validateDomain(displayError){
	var id="domain";
	if(!Validator.name.test($("#"+id).val())){
		if(displayError){
			$("#msgDm").html("请使用长度为4~20的数字、字母，支持点和下划线。");
		}
		return false;
	}else{
		$("#msgDm").html("");
		return true;
	}
}

$(document).ready(function(){
	$("#domain").keyup(function(){validateDomain(false)});
	$("#domain").blur(function(){validateDomain(true)});
	$("#email").keyup(function(){validateEmail(false)});
	$("#email").blur(function(){validateEmail(true)});
	
	$("#regForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(!validateDomain(true) || !validateEmail(true)){
				return false;
			}
			$.blockUI();
			return true;
		},success:function(data){
			$.unblockUI();
			if(data.success == "true"){
   				window.location= data.url;
   			}
			
			if (data.msgDm) {
   				$("#msgDm").html(data.msgDm);
   			} 
   					   	
   			if (data.msgEmail) {
   				$("#msgEmail").html(data.msgEmail);
   			}
		}
	});
});
