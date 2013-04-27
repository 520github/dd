Validator={
                   mobile:/^(13[0-9]|15[0-9]|186|187|188|189)\d{8}$/,
                   email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/
};

function isEmail1(str){
   return Validator.email.test(str);
}

function isMobile(str){
   return Validator.mobile.test(str);
}

function validateUsername(){
	var flag = true;
	var username = $("#account").val();
	var test = document.getElementById("test");
	if(username == "" || username.length<4 || username.length>16){
		test.style.display="block";
		test.innerHTML="请输入4-16位字符";
		flag = false;
	}else if(!username.match(/^[0-9a-zA-Z_.]*$/)){
		test.style.display="block";
		test.innerHTML="不能包含特殊字符";
		flag = false;
	}else if(username.indexOf('_')==0 || username.indexOf('_')+1==username.length || username.indexOf('.')==0 || username.indexOf(".")+1==username.length){
		test.style.display="block";
		test.innerHTML="首尾不能是下划线或数点";
		flag = false;
	}
	return flag;
}

function validateAccount(){
	var flag = true;
	var email = $("#account").val();
	var test = document.getElementById("mail_id");
	if(!isEmail1(email)){
		test.style.display="block";
		test.innerHTML="邮箱格式错误";
		flag = false;
	}else{
		flag = true;
	}
	return flag;
}

function validateRegisterForm(){
	var flag = true;
	flag=validateAccount();
	var pass = $("#password").val();
	if(pass == "" || pass.length <6 || pass.length>16){
		var se_1 = document.getElementById("se_1");
		se_1.style.display="block";
		se_1.innerHTML="请输入6-16位密码";
		flag = false;
	}
	var conpass = $("#confirmPassword").val();
	if(conpass=="" || conpass.length <6 || conpass.length > 16){
		var con_2 = document.getElementById("con_2");
		con_2.style.display="block";
		con_2.innerHTML="请输入6-16位密码";
		flag = false;
	}else if(pass != "" && pass != conpass){
		var con_2 = document.getElementById("con_2");
		con_2.style.display="block";
		con_2.innerHTML="确认密码应与密码输入一致，请重新输入";
		flag = false;
	}
	var nickname = $("#nickname").val();
	if(nickname == "" ){
		var nick = document.getElementById("nick");
		nick.style.display="block";
		nick.innerHTML="请输出昵称";
		flag = false;
	} 
	var number = $("#number").val();
	if(number != ""){
		if(!isMobile(number)){
			var mail_id = document.getElementById("phone");
			mail_id.style.display="block";
			mail_id.innerHTML="手机格式错误";
			flag = false;
		}
	}
	return flag;
}

function register(){
	var flag=validateRegisterForm();
	if(flag){
		
		if(!$("#agree").attr("checked")){
				alert("对不起，您未选择接受服务条款！","系统提示",true,false);
				return;
		}	
			$("#registerForm").ajaxSubmit({				
				dataType:"json",		
				success:function(data){
					if(data.actionErrors && data.actionErrors.length>0){						
						alert(data.actionErrors[0]);
					}else{									
						if(data.success == false){
								alert("注册失败")
						}else{
						window.location= "domain.html?dm="+data.domain;
					}
					}
				}
				});
	}else{
		return false;
	}
}
