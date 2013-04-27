function unbindweibo(){
	jQuery.post("/account/user/unBindWeibo", {}, function(data) {
		if (data == "204") {
			$("#unbindweibo").hide();
			$("#bindweibo").show();
			openMessage("success", "解绑成功");
		}else if (data == "0"){
			$("#unbindweibo").show();
			$("#bindweibo").hide();
			openMessage("failure", "解绑失败");			
		}else if (data == "409"){
			$("#unbindweibo").show();
			$("#bindweibo").hide();
			openMessage("failure", "微博登录帐户不能解除绑定");			
		}else if (data == "470"){
			$("#unbindweibo").show();
			$("#bindweibo").hide();
			openMessage("failure", "没有绑定微博所以无法解绑");			
		}else if (data == "401"){
			$("#unbindweibo").show();
			$("#bindweibo").hide();
			openMessage("failure", "没有登录");			
		}else{
			openMessage("failure", "什么情况"+data);			
		}
		if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){
         setTimeout("window.location.reload()",2000); 
        }     
	});	
}

function setSyncStatus(){
	var url="/account/user/thirdPartyProfile";
	var vvalue="";
	if($("#syncContent").val()=="false"){
		vvalue='true';
	}else{
		vvalue='false';
	}
	$.ajax({
		type : "post",
		url : url,
		data:"vvalue="+vvalue,
		dataType: 'json',
		async:false,
		success : function(data){
			var temp = "";
			for (var prop in data) {
			    //document.write("name: " + prop + "; value: " + data[prop], "<br>");
			    temp = temp + "name: " + prop + "; value: " + data[prop], "<br>";
			}
		// setThirdPartyContentSetting
		if (data.result == "204") {
			$("#cclosesync").toggle();
			$("#oopensync").toggle();
			$("#syncContent").val(vvalue);
			openMessage("success", "成功");
		}else if (data.result == "0"){
			//$("#cclosesync").toggle();
			//$("#oopensync").toggle();
			openMessage("failure", "开启失败");			
		}else if (data.result == "470"){
			//$("#cclosesync").toggle();
			//$("#oopensync").toggle();
			openMessage("failure", "未绑定微博");			
		}else if (data.result == "403"){
			//$("#cclosesync").toggle();
			//$("#oopensync").toggle();
			openMessage("failure", "未登录");			
		}else{
			openMessage("failure", "什么情况"+temp);			
		}
		if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){
         setTimeout("window.location.reload()",2000); 
        }     
	}
	});	
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

function auth(token) {
	window.open("/account/weibo/oauth/authority?authToken=" + token);
}