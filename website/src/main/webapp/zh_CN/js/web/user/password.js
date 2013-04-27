Validator={
                   password:/^.{6,16}$/,
                   name:/^(-|\w|[\u4e00-\u9fa5]){2,15}$/
};
$(document).ready(function(){
	$("#saveBtn").click(modifyPassword);
});
	
function modifyPassword(){
	$.ajax({
		type : 'post',
		url : '/account/user/password',
		data :'originalPwd='+$("#originalPwd").val()+'&newPwd='+$("#newPwd").val(),
		dataType: 'json',
		beforeSend:function(){
			if(validate()){
				$.blockUI();
				return true;
			}
			return false;
		},
		success : function(data){
			$.unblockUI();
			if(data.result=="success"){
				openMessage("success","密码修改成功");
				$("#originalPwd").val("");
				$("#newPwd").val("");
				$("#newPwdc").val("");
			}else{
				openMessage("failure","密码修改失败");
			}
		}
	});
}
function validate(collection){
	var flag =true;
	if(collection == null){
		collection = $("#originalPwd,#newPwd,#newPwdc");
	}
	collection.each(function(index){
			var $id=$(this).attr("id");
			switch($id){
				case "originalPwd":
					if($(this).val()=='' || !Validator.password.test($(this).val())){
						openMessage("failure","当前密码不规范,(6-16)位");
						flag=false;
					}
					break;	
				case "newPwd":
					if($(this).val()=='' ||  !Validator.password.test($(this).val())){
						openMessage("failure","新密码不规范,(6-16)位");
						flag=false;
					}
					break;
				case "newPwdc":
					if($(this).val()=='' || $("#newPwd").val()!=$(this).val()){
						openMessage("failure","两个密码不一致");
						flag=false;
					}
					break;	
				default:
					flag=false;
			}
			return flag;
	});
	return flag;
}

function openMessage(type,msg) {
    openPopupMessage(type,msg);   
}