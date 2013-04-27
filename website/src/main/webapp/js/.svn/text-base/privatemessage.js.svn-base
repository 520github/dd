function getLength(str){

	var l = str.length;
	var n = l
	for (var i = 0; i < l; i++) {
		if (str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255)
			n++
	}
	return parseInt(n/2,10);
}

function openUrl(url) {
    window.open(url);
    //window.open("/search/personal/url?url="+url);
}

function getMessages(){
	clear();
	var sessionId = $('#sessionId').val();
	$.ajax({
		url : "/message/private/session",
		headers:cookieUtil.getHeaderToken(),
		data : "sessionId="+sessionId,		
		type : "get",
		dataType : "json",
		cache : false,
		success : function(jsonObj) {
			var content = jsonObj.content;
			var session = jsonObj.session;
			var repository = jsonObj.repository;
			var owner = session.owneraccountId;
			var fromAccount = jsonObj.fromAccount;
			var targetAccount = jsonObj.targetAccount;
			$('#c_time').html(session.lastUpdate.date);
			
			switch(content.contentType){
			case 'Web': 
			
			    if(content.htmlPayload.attachment.length>0){
			  
			    	changeMode('web');
			    	var imageUrl = content.htmlPayload.attachment[0].archiveUrl;
			    	$('#firstImage').attr("src",imageUrl);
			    	
			    }else{
			    	changeMode('text');
			    	$('#text').html(content.summary);
			    }
			     $('#right-article').append(content.summary).append('<a href=javascript:openItemDetail("'+content.id+'","","") id="more">查看全文>></a>');
			
			break;
			case 'Url': changeMode('url') ; 
			changeMode('text');
			    $('#text').html(content.summary);
			break;
			case 'Video': changeMode('video'); 
			   changeMode('video');
			   var html = ' <a href=javascript:openItemDetail('+content.id+',"","")><img alt="" src="'+content.videoPayload.imageUrl+'"></a>'
			             +'<span><a href=javascript:openItemDetail("'+content.id+'","","")><img alt="" src="/zh_CN/images/video.png"></a></span>'
			   $('#video_image').html(html);
			 // $('#video_title').html(content.title);
			   $('#title').html('');
			   
			 break;
			case 'Image':
				  changeMode('image'); 
			      $('#image').html('<img alt="" src="'+content.url+'"/>');
			       break;
			       
			case 'HtmlClip':
			
			
			    if(content.htmlPayload.attachment.length>0){
			  
			    	changeMode('web');
			    	var imageUrl = content.htmlPayload.attachment[0].archiveUrl;
			    	$('#firstImage').attr("src",imageUrl);
			    	
			    }else{
			    	changeMode('text');
			    	$('#text').html(content.summary);
			    }
			     $('#right-article').append(content.summary).append('<a href=javascript:openItemDetail("'+content.id+'","","") id="more">查看全文>></a>');
		
			break;
			
			case 'Product':
			  changeMode('product');
			  var imageUrl = content.productPayload.picture;
			  var price = content.productPayload.price;
			  
			  $('#product_image').html('<a href=openItemDetail('+content.id+',"","")><img alt="" src="'+imageUrl+'"></a>')
			  $('#product_price').html(price);
			//  $('#product_title').html(content.title);
			  $('#title').html('');
			
			}
		
		 if(content.title=="")
		 	content.title = "来自新浪博文";
		 $('#title').html('<a href=javascript:openItemDetail("'+content.id+'","","")>'+content.title+'</a>')
		 var pattern = /@/;
		 if (pattern.test(content.url)) {
		 $('#from').attr("href",'#').html(content.url);
		 }	else {
		 $('#from').attr("href","javascript:openUrl('"+content.url+"')").html(content.url);
		 }	
			
		 var messages = jsonObj.messages;
		 var conmments = $('#c_back');
		 $('#c_back').html('');
		 $('#myicon').attr("src",fromAccount.smallPhotoUrl);
		 $.each(messages,function(i,obj){
		 	var mymessage = obj.message;
		 	if(mymessage==null||mymessage==undefined||mymessage==""){
		 	  return;
		 	}
		 	
		 	mymessage=mymessage.replace(/\n/g,'<br />');	//	增加换行效果 
			
			 if(owner==obj.accountId){//自己发的消息
				
				var msg_mine = '<div class="friend-home-list friend-home-list-l"><div class="friend-content-l">'+
			    '<div class="friend-pic-icon"><img src="'+fromAccount.smallPhotoUrl+'" alt="图标"></div><div class="dialogue">'+
				'<b></b><span><a href="#">我:</a>'+obj.date+'</span><p>'+mymessage+'</p>'+
				'</div></div></div>'
				
				
				
				conmments.prepend(msg_mine);
				 
			 }else{
		 
               	 var msg_target = '<div class="friend-home-list"><div class="friend-content-l">'+
			    '<div class="friend-pic-icon"><img src="'+targetAccount.smallPhotoUrl+'" alt="图标"></div><div class="dialogue">'+
				'<b></b><span><a href=\'/'+targetAccount.id+'\' target="blank">'+targetAccount.name+'</a>   '+obj.date+'</span><p>'+mymessage+'</p>'+
				'</div></div></div>'
               	 
               	 
               	 
                 conmments.prepend(msg_target);
               	
               	
			 }
		 })
		 
		 $('#content_list').css("display","block");
		 
		 
		},
		error : function() {
			openMessage("failure","服务器错误，请稍后重试!");

		}
	})
	
	
	
}


function intervalMessage(){
	var sessionId = $('#sessionId').val();
	$.ajax({
		url : "/message/private/session",
		headers:cookieUtil.getHeaderToken(),
		data : "sessionId="+sessionId,		
		type : "get",
		dataType : "json",
		cache : false,
		success : function(jsonObj) {
			var content = jsonObj.content;
			var session = jsonObj.session;
			var repository = jsonObj.repository;
			var owner = session.owneraccountId;
			var fromAccount = jsonObj.fromAccount;
			var targetAccount = jsonObj.targetAccount;
			
		 var messages = jsonObj.messages;
		 var conmments = $('#c_back');
		 $('#c_back').html('');
		 $('#myicon').attr("src",fromAccount.smallPhotoUrl);
		 $.each(messages,function(i,obj){
		 	var mymessage = obj.message;
		 	if(mymessage==null||mymessage==undefined||mymessage==""){
		 		return;
		 	}
		 	
		 	mymessage=mymessage.replace(/\n/g,'<br />');	//	增加换行效果 
			
			 if(owner==obj.accountId){//自己发的消息
				
				var msg_mine = '<div class="friend-home-list friend-home-list-l"><div class="friend-content-l">'+
			    '<div class="friend-pic-icon"><img src="'+fromAccount.smallPhotoUrl+'" alt="图标"></div><div class="dialogue">'+
				'<b></b><span><a href="#">我:</a>'+obj.date+'</span><p>'+mymessage+'</p>'+
				'</div></div></div>'
				
				
				
				conmments.prepend(msg_mine);
				 
			 }else{
		 
               	 var msg_target = '<div class="friend-home-list"><div class="friend-content-l">'+
			    '<div class="friend-pic-icon"><img src="'+targetAccount.smallPhotoUrl+'" alt="图标"></div><div class="dialogue">'+
				'<b></b><span><a href="#">'+targetAccount.name+'</a>'+obj.date+'</span><p>'+mymessage+'</p>'+
				'</div></div></div>'
               	 
               	 
               	 
                 conmments.prepend(msg_target);
               	
               	
			 }
		 })
		 
		 $('#content_list').css("display","block");
		 
		 
		},
		error : function() {
			openMessage("failure","服务器错误，请稍后重试!");

		}
	})
	
	
	
}




function checkValueSize(){
	var message = $('#back_val').val("");
	var max_size = 140;
	var org_size;
	var t_size;
	$("#back_val").keyup(function(){
		var message = $('#back_val').val();
		org_size = getLength(message);
		t_size = max_size-org_size;
		if(t_size>0){
		$('#message_size').html('还可以输入'+t_size+'个字');
		}else if(t_size==0){
			$('#message_size').html('<font color="red">还可以输入'+t_size+'个字</font>');	
		}else{
			openMessage("failure","不可以再输入了!");
		  $("#back_val").val(message.substring(0,message.length-1));
		  return;
		}
	})

}


function sendMessage(){
	var sessionId = $('#sessionId').val();
	$('#enter_back').click(function(){
		var message = $('#back_val').val();
		if(message==null||message==''){
			openMessage("failure","评论不能为空!");
			return;
		}
		$.ajax({
			url : "/message/private/session",
			headers:cookieUtil.getHeaderToken(),
			data : "sessionId="+sessionId+"&message="+window.encodeURIComponent(message),	
			type : "post",
			cache : false,
			success : function(jsonObj) {
				if(jsonObj.error!=null&&jsonObj!=undefined){
					openMessage("failure", jsonObj.error);
					return;
				}else{
				  openMessage("success", "评论成功!");
				}
				var message = $('#back_val').attr('value','');
				$('#message_size').html('还可以输入140个字');
				intervalMessage();//为修复加载数据量大，页面闪现问题。只评论呢列表局部刷新
				
	
        
			}

		})
		
		
	})
	
	
	
}


function changeMode(css){
	var arr = ['web','video','image','text','product'];
	for(var i = 0;i<arr.length;i++){
		if(css==arr[i]){
			continue;
		}
		$('#'+arr[i]).remove();
		
	}
	
}

function clear(){
	var arr = ['web','video','image','text','product'];
	for(var i = 0;i<arr.length;i++){
		if(arr[i]=='web'){
			$('#'+arr[i]).html('<img id="firstImage" src=""/><div id="right-article" class="right crawl-article"></div>');		
			continue;
		}
		if(arr[i]=='product'){
			$('#'+arr[i]).html('<div class="panel-content-video"  id="product_image"><a href="#"><img alt="" src=""></a></div><div class="panel-title"><h2 style="background: none repeat scroll 0% 0% transparent; padding: 0px;"><a id="product_title"></a></h2><span id="product_price" class="price"></span></div>')							
		    continue;
		}
		if(arr[i]=='video'){
			$('#'+arr[i]).html('<div class="panel-content-video" id="video_image"><a href="#"><img alt="" src="/zh_CN/images/logo-national-flag.gif"></a><span><a href="#"><img alt="" src="/zh_CN/images/video.png"></a></span></div><div class="panel-title"><h2 style="background: none repeat scroll 0% 0% transparent; padding: 0px;"><a id="video_title"></a></h2></div>');
	       continue;
			
		}
		$('#'+arr[i]).html('');
	}

}
function openMessage(type,msg) {
    openPopupMessage(type,msg);
}
$(function(){
	checkValueSize();
	sendMessage();
	getMessages();
	setInterval(intervalMessage,30000)
})