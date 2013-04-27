			function flipPage(pageIndex){
				if(pageIndex==undefined){
					// get current pagenum
					pageIndex = $("#currentpagenum").val();
				}
				$("#currentpagenum").val(pageIndex);
				getAppListForCategoryId(pageIndex);
				var interval = 1300000;
				setTimeout("flipPage()",interval);
			}
			
			function getAppListForCategoryId(pageIndex){
				var pageSize = 20;	//	DDS-436 每页显示20个好友评论内容
				var contentId = "";
				$.ajax({
					//PrivateMessageController.getSessions
					url : "/message/private/session/index",
					headers:{
								"Authorization":cookieUtil.getWebToken()
					},
					data : "offset="+(pageIndex-1)*pageSize+"&limit="+pageSize,		//	在本js中pageIndex是从1开始 但是在数据库中是从0开始 所以要减一
					type : "get",
					dataType : "json",
					cache : false,
					success : function(jsonObj) {
						//console.log(jsonObj);	// IE不兼容  DDS-564
						var sessionlist = jsonObj.sessions;
						printToResult('Template',sessionlist);
						var paginate = new pagination();
						paginate.printPagination(pageIndex,jsonObj.totalNumber,pageSize,'pagenav'); 
					},
					error : function() {
						//alert('服务器错误，请稍后重试!');
			
					}
				})
			}
			
			function printToResult(domId,list){
				 var domlist = $('#'+domId).html('');
				 $.each(list,function(i,obj){				 
					 var imgsrc = obj.talker.middlePhotoUrl;
					 if(imgsrc==undefined||imgsrc=="undefined"){
					 	imgsrc = "/images/avatar-35px.png";
					 }
					 
					 var mymessage = obj.session.lastUpdate.message;
					 if(mymessage==null||mymessage==undefined||mymessage==""){
					 	mymessage="他很深沉，只有分享，没有评论。";
					 }
					 	
		 			 mymessage=mymessage.replace(/\n/g,'<br />');	//	增加换行效果
		 			 var str = "";
		 			 //str = str + "第" + (i+1) +"条";
					 str = str + '<div class="panel friend-content friend-home private-message"><div class="friend-home-list"><div class="r-frien-button"><div class="panel-info"><span class="left">'
					 	//+ new Date(obj.session.lastUpdate.date.time).toLocaleString()
					 	+ obj.session.lastUpdate.date
					 	+ '</span></div><a href="javascript:replyprivateMessage('
					 	+ "'" + obj.session.id
					 	+ "');"
					 	+ '"'
					 	+ ' class="friends-btn">回复('
					 	+ obj.session.messageCount
					 	+ ')</a><a href="javascript:showprivatemessagesessiondeleteDialog('
					 	+ "'" + obj.content.id
					 	+ "','"
					 	//+ obj.to.id
					 	+ obj.session.accountId	//	DDS-494
					 	+ "');"
					 	+ '">删 除</a></div><div class="friend-pic-icon"><img src="'
					 	+ imgsrc
					 	//+ '/images/avatar-35px.png'
					 	+ '" alt="图标" /></div><div class="friend-content-l"><h2><a href="/'
					 	+obj.talker.id
					 	+'/">'
					 	+obj.talker.name
					 	+'</a><p>评论了</p><span class="title"><a href="javascript:openItemDetail('
					 	+ "'" + obj.content.id
					 	+ "');"
					 	+ '"'
					 	+ ' class="friends-btn">'
					 	+ obj.content.title
					 	+ '</a></span></h2><div class="desc"><strong>“</strong><p>'
					 	+ mymessage
					 	+ '<span>”</span></p></div></div></div></div>';
					 //alert('i:'+i+"\nid:"+obj.id+"\nstr:"+str);
					 domlist.append(str);
					 //alert(domlist.html());
				 })
				 if(list.length==0){
				 	$('a#baddddddd').html('');
				 	domlist.append('<div class="panel friend-content friend-home private-message"><div class="friend-home-list"><div class="friend-content-l"><div class="desc"><strong></strong><p>你还没有评论<span></span></p></div></div></div></div>');
				 }
			}
			
			
			//打开详情页面
      function openItemDetail(contentId) {
          //var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
          var menuType = "read";
          var open_url = "/private/item?id="+contentId+"&menuType="+menuType;
         
          window.open(open_url);
      }
      
        var _contentId;
		var _sessionAccountId;
		function showprivatemessagesessiondeleteDialog(contentId,sessionAccountId){
			_contentId = contentId;
			_sessionAccountId = sessionAccountId;
			
			$.layer({
			    v_box : 1,
			    v_istitle:false,
			    v_dom : '#privatemessagesessiondeleteDialog',	//id
			    v_area : ['1000px','420px'],
			    v_btns : 0,
			    v_showclose:false
			});
		}
      
		function showprivatemessagesessiondeleteAllDialog(contentId,sessionAccountId){
			
			$.layer({
			    v_box : 1,
			    v_istitle:false,
			    v_dom : '#privatemessagesessiondeleteAllDialog',	//id
			    v_area : ['1000px','420px'],
			    v_btns : 0,
			    v_showclose:false,
			    v_offset : ['100px','']	//为空时数据默认
			});
		}
      
      


jQuery(function(){
	flipPage(1);
})



function replyprivateMessage(sessionId) {
	//	PrivateMessageController.showMessageview
	window.open('/message/private/message/view?sessionId='+sessionId)	
}

// 没有使用
function deleteAllprivateMessage() {
	jQuery.post("/privatemessages/deleteAll", {}, function(data) {
		if (data == "success") {
			openMessage('success','删除成功');
		}
		else if (data == "failure") {
			openMessage('failure','删除失败');
		}
	});
}

// 若两个参数均为空 则删除当前登录用户的所有PrivateMessageSession
function deleteprivateMessage(contentId,accountId) {
	//	me.twocoffee.controller.PrivateMessageController.deleteSession(String accountId, String contentId, Model model)
	jQuery.post("/message/private/session/delete", {contentId:contentId,accountId:accountId}, function(data) {
		var temp = "";
		for(var prop in data){
		temp = temp+"data["+prop +"]="+data[prop]+"\n";
		}
		//alert("reslult    is \n"+temp);
		if (data['success'] == "success") {
			openMessage('success','删除成功');
			flipPage();
		}
		else if (data['error'] == "server error!please try again!") {
			openMessage('failure','删除失败');
		}
	});
}


//弹出消息框

function openMessage(type,message) {
    openPopupMessage(type,message);
}

