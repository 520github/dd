$(document).ready(function(){
	bindImage();
	var cutter = new jQuery.UtrialAvatarCutter();
	$("#cropImage").bind("load",function(){
		cutter.init();	  
	});	
	$("#saveImage").click(function(){
		crop();
	});
});
	
function ajaxFileUpload(){
/*
$("#uploadBtn").ajaxStart(function(){
    $(this).hide();
}).ajaxComplete(function(){
	$(this).show();
});
*/	        
$.ajaxFileUpload
({
    url:'/account/user/gravatar', 
    secureuri:false,
    fileElementId:'imageFile',
    dataType: 'json',
    success: function (data, status){
    	bindImage();
    	if(data.error){
    		openMessage("failure","文件大于5M",false);
    		$.unblockUI();
    	}else if(data.imageid==''){
    		openMessage("failure","文件类型不支持",false);
    		$.unblockUI();
    	}else{
	    	$("#tipsDiv").hide();
	    	$("#cropImageId").val(data.imageid);
	    	$("#cropImage").attr("src",data.url);
	    	$("#preview1").attr("src",data.url);
	    	$("#preview2").attr("src",data.url);
	    	$("#preview3").attr("src",data.url);
	    	$.unblockUI();
    	}
    	
    },
    error: function (data, status, e){
        alert(e);
    }
});
return false;
} 	

function bindImage(){
	$("#imageFile").unbind();
	$("#imageFile").change(function() {	
		if(imageValidate()){
			$.blockUI();
			ajaxFileUpload();
		}
	});
}
function crop(){
	var x = $("#coordx").val();
	var y = $("#coordy").val();
	var w = $("#coordw").val();
	var h = $("#coordh").val();
	$.ajax({
		cache : false,
		type : 'post',
		url : '/account/user/gravatar/crop',
		data :'x='+x+'&y='+y+'&w='+w+'&h='+h+"&imageid="+$("#cropImageId").val(),
		dataType: 'json',
		beforeSend:function(){
			if(x=="" || w=="" || w==0 || h==0){
				openMessage("failure","请选择需要裁剪的图片",true);
				return false;
			}
			$.blockUI();
			return true;
		},
		success : function(data){
			$.unblockUI();
			cropSuccess();
		}
	});
}
function imageValidate(){
	var flag=true;
	var temp =$("#imageFile").val();
	if(temp!="" && (temp.length<5 || (temp.toLowerCase().indexOf(".jpeg")!=(temp.length-5)  && temp.toLowerCase().indexOf(".png")!=(temp.length-4) && temp.toLowerCase().indexOf(".jpg")!=(temp.length-4)))){
		openMessage("failure",'支持png,jpg类型的文件',true);	
		var image=$("#imageFile");	
		image.after(image.clone(true).val(""));
		image.remove();
		bindImage();
		flag=false;
	}
	return flag;
}


function openMessage(type,msg,auto) {
    if(auto){
        openPopupMessage(type,msg);
    }else{
        openPopupMessageHand(type,msg);    	
    }
}

function cropSuccess() {
    openPopupMessage("success","保存成功");
    setTimeout(function() {location.reload();}, 2000);
}
