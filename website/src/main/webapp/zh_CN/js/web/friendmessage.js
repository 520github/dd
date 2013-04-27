String.prototype.Trim = function() 
{ 
return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 
String.prototype.LTrim = function() 
{ 
return this.replace(/(^\s*)/g, ""); 
} 
String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
} 

function action(value,token,msgId){	

   var head = {"content-type":"application/json","Authorization":cookieUtil.getWebToken()};
	 var param = {
			  action:value			 
		   };	
			
	$.ajax({
				   url:"/service/message/"+msgId,
				   dataType:'json',				  
				   type:"post",
				   data:JSON.stringify(param),
				   headers:head,				  
				   success:function(data){				
					  window.location.reload();
				   }				  
			});

}
function addFriend(friendId,token,msgId){	
	param = "friendId="+friendId;			
	$.ajax({
				   url:"/friend/add",
				   dataType:'json',				  
				   type:"post",
				   data:param,
				   success:function(data){				
					   window.location.reload();
				   }				  
			});

}
function showDeleteDialog() {
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#showDeleteDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}


function deleteMsg(token,msgId){	

 var auth = cookieUtil.getHeaderToken();
	$.ajax({
				   url:"/service/message/"+msgId,
				   dataType:'json',				  
				   type:"delete",				   
				   headers:auth,
				   success:function(data){
					  window.location.reload();   					 
				   }				  
			});

}

//弹出消息框
function openMessage(type,msg) {
	openPopupMessage(type,msg);
}
	   
	   
function showDialog(accountId,name,msgId){
	$('#friendId').attr('value',accountId);
	$('#setName').html(name);
	$('#msgId').attr('value',msgId);

 $.layer({        
          v_title:"设置备注",
          v_istitle:false,
          v_showclose : false,
          //v_skin:0,
          v_shade:true,
          v_box : 1,
          v_dom : '#showDialog',        //id
          v_area : ['300px','300px'],
          v_btns : 0,
          v_btn  :["保存"],
          v_move:false
         // v_offset : ['50px','350px']//为空时数据默认
        });

}

function editNode(){
	//var accountId = $('#accountId').val();
	var friendId = $('#friendId').val();
	var remarkName = $('#remarkName').val();
	var msgId = $('#msgId').val();
    
	if($.trim(remarkName).length==0){
		return;
	}
    
	
    $.ajax({
		url : "/friend/"+friendId+"/remarkName",
		headers:cookieUtil.getHeaderToken(),
		data : "remarkName="+remarkName,		
		type : "get",
		dataType : "json",
		cache : false,
		success : function(jsonObj) {
	     deleteMsg(getToken(),msgId);
		}
	})
   


}

$(function(){
  $("#cancel").click(function(){
     layer_close();
  })
})