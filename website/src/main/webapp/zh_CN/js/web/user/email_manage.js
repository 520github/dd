Validator={
        email: /^[0-9a-zA-Z_\-\.]*[0-9a-zA-Z_\-]@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/
};
$(document).ready(function(){
	init();
	$("#friendsVisible").click(function(){$(this).focus();$(this).blur();});
	$("#friendsVisible").change(function(){
		var flag='false';
		if($(this).attr("checked")=='checked'){
			flag='true';
		}
		$.ajax({
			type : 'post',
			url : '/account/user/accountmail/friendsVisible',
			data:"friendsVisible="+flag,
			dataType: 'json',
			async:false,
			success : function(data){
			}
		});	 
	});
	
	$("div.i-email :checkbox").click(function(){$(this).focus();$(this).blur();});
	$("div.i-email :checkbox").change(function(){
		var url="";
		if($(this).attr("checked")=="checked"){
			url='/account/user/accountmail/tag';
		}else{
			url='/account/user/accountmail/deletetag';
		}
		$.ajax({
			type : "post",
			url : url,
			data:"tag="+$(this).attr("name"),
			dataType: 'json',
			async:false,
			success : function(data){
			}
		});	
	});
	$("#addAnchor").click(addEmail);	

});
function init(){
	
}
function validate(obj){
	obj.val($.trim(obj.val()));
	if(obj.val()=='' || !Validator.email.test(obj.val())){
		openMessage("failure","邮箱不规范");
		return false;
	}
	return true;
}

function addEmail(){
	var li=$("li.remove-modified-btn");
	if(li.length<2){
		if(validate($("#email"))){
			$.ajax({
				type : 'post',
				url : '/account/user/accountmail',
				data:'original=&new='+$("#email").val(),
				dataType: 'json',
				async:false,
				success : function(data){
					if(data.result=='success'){
						var html='<li class="remove-modified-btn"><span>'+$("#email").val()+'</span><a href="#;return false;" onclick="deleteEmail(this)">移 除</a><a href="#;return false;" onclick="modifyEmail(this)">修 改</a></li>';
						$("div.new-commonly ul li:first").after(html);
						$("#email").val('');
					}
				}
			});				
		}	
	}else{
		openMessage("failure","只能添加2个");
	}
}
function modifyEmail(obj){
	$(obj).prev().prev("span").prev("input").remove();
	var str=$(obj).prev().prev("span").text();
	$(obj).prev().attr("onclick","confirmEmail(this)").text("确 定").prev().before('<input type="text" value="'+str+'" />').hide();
}
function deleteEmail(obj){
	$.ajax({
		type : 'post',
		url : '/account/user/accountmail',
		data:'new=&original='+$(obj).prev().text(),
		dataType: 'json',
		async:false,
		success : function(data){
			if(data.result=='success'){
				$(obj).parent("li").remove();
			}
		}
	});					
}
function confirmEmail(obj){
	if(validate($(obj).prev().prev("input"))){
		$.ajax({
			type : 'post',
			url : '/account/user/accountmail',
			data:'original='+$(obj).prev("span").text()+'&new='+$(obj).prev().prev("input").val(),
			dataType: 'json',
			async:false,
			success : function(data){
				if(data.result=='success'){
					$(obj).attr("onclick","deleteEmail(this)").text("移 除");
					$(obj).prev("span").text($(obj).prev().prev("input").val()).show();
					$(obj).prev().prev("input").remove();
				}
			}
		});				
	}		
	
}


function openMessage(type,msg) {
    openPopupMessage(type,msg);   
}