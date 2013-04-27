var _friendid;
function showRemoveFriendDialog(id){
	_friendid = id;
	
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#showRemoveFriendDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}
function removeFriend(id) {
	jQuery.post("/friend/remove", {friendId:id}, function(data) {
		if (data == "0") {
			openMessage("failure", "断交失败。");
			return;
		}
		else if (data == "1") {
			openMessage("success", "你太绝情了，已断交。");
			alert
			window.setTimeout(reloadView,2000)
		}
		
		//document.location.reload();
	});
}
function reloadView() {
	document.location.reload();
}
function addFriend() {
	var regMessage=/[''"":;\.\/<>~!@#%&]/g;
	$("#showDialog_message").val($.trim($("#showDialog_message").val()).replace(regMessage,"").substring(0, 140));
	$("#showDialog_remark").val($("#showDialog_remark").val().replace(/ /g,""));
	var str=$.trim($("#showDialog_remark").val());
	if(str==initShowDialog_remark.prototype.str){
		str="";
	}
	var message=$("#showDialog_message").val();
	if(message==initShowDialog_message.prototype.str){
		message="";
	}
	layer_close();
	jQuery.post("/friend/add", {friendId:$("#showDialog_accountId").val(),remark:str,postscript:message}, function(data) {
		if (data == null || data == "") {
			openMessage("failure", "网络错误。");
		}else if (data == "0" || data == "2") {
			openMessage("failure", "添加失败。");
		}else if (data == "1") {
			openMessage("success", "已发送好友邀请。");
		}else if (data == "3") {
			openMessage("success", "不能添加自己。");
		}
	});
}

var initShowDialog_message=function(){
	if($("#showDialog_message").val()==initShowDialog_message.prototype.str){
		$("#showDialog_message").val("");
	}
};
initShowDialog_message.prototype.str="说点什么吧！至少让他知道你是谁";
var initShowDialog_remark=function(){
	if($("#showDialog_remark").val()==initShowDialog_remark.prototype.str){
		$("#showDialog_remark").val("");
	}	
};
initShowDialog_remark.prototype.str="给TA添加一个备注名呗！";
function showDialog(id,name){
	$("#showDialog_name").text(name);
	$("#showDialog_accountId").val(id);
	$("#showDialog_remark").val(initShowDialog_remark.prototype.str);
	$("#showDialog_message").val(initShowDialog_message.prototype.str);
	
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#showDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}
var _targetid;
var _actionFavorite;
var _direction;
function showFavariteFriendDialog(targetid,actionFavorite,direction){
	_targetid = targetid;
	_actionFavorite = actionFavorite;
	_direction = direction;
	
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#favariteFriendDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}

//	处理常用联系人逻辑
//	actionFavorite 执行的动作:add or remove
function dealFavoriteFriend(targetid,actionFavorite,direction){
	if(actionFavorite == "Favorite"){
		actionFavorite = "Normal"
	}
	else{
		actionFavorite = "Favorite"
	
	}
	
	jQuery.post("/friend/favorite", {targetId:targetid,actionFavorite:actionFavorite}, function(data) {
		if (data == "0") {
			if(direction=='/friend'){
				$("#div"+targetid).removeClass("not-star-pic");
				$("#div"+targetid).attr("title","设为常用联系人");
				$("#div"+targetid).attr("onclick","javascript:dealFavoriteFriend('"+targetid+"','"+actionFavorite+"','"+direction+"');");
				openMessage("success", "看来你真的变心了……");
				
			}
			else{	//	如果是在常用联系人tab 则要删掉这个
					//     DDS-723 常用联系人页面：取消一个常用联系人，该联系人仍然会显示在该页面
				$("#list"+targetid).hide();
				openMessage("success", "看来你真的变心了……");
			}
		}
		else if (data == "1") {
			openMessage("failure", "取消失败！");
		}
		else if (data == "2") {
			//alert($("#div"+targetid).attr("title"));
			$("#div"+targetid).addClass("not-star-pic");
			$("#div"+targetid).attr("title","取消常用联系人");
			$("#div"+targetid).attr("onclick","javascript:showFavariteFriendDialog('"+targetid+"','"+actionFavorite+"','"+direction+"');");
			openMessage("success", "常来常往，感情深！");
		}
		else if (data == "3") {
			openMessage("failure", "标记常用人失败");
		}
	
        if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){
         setTimeout("window.location.reload()",2000); 
        }
      
     
	});
	
}

function jianpanhuicheche(){
	var e = e || window.event || arguments.callee.caller.arguments[0] || event ;
            if(e && e.keyCode==27){ // 按 Esc 
            	//alert('Esc');
                //要做的事情
              }
            if(e && e.keyCode==113){ // 按 F2 
            	//alert('按 F2');
                //要做的事情
               }            
             if(e && e.keyCode==13){ // enter 键
            	search();
                //要做的事情
            }
}


function search() {
	
	var key = $("#name").val();
	var tab = $("div[name='frienddivname']:visible");
	var dds917 = $("div[id='findresult']");
	dds917.hide();
	var found = false;
	tab.find(".friend-home-list").each(function(i) {
		var c = $(this).find("a[id]");
		if (key == null || key == "") {
			$(this).show();
		}
		else if (isMatch(c, key)) {
			$(this).show();
			found = true;
		}
		else {
			$(this).hide();
		}
	});
	if(!found){
		// DDS-917 在我的好友列表，搜索好友，无结果时没有任何提示
		dds917.show();
	}
}

function isMatch(c, key) {
	var name = c.attr("nameremarkName");
//	alert("name, key:"+name+"|||\n"+key)
	var k = key.replace("\\", "\\\\").replace(".", "\.");
	//alert("k="+k);
	var reg = new  RegExp("^.*" + k + ".*$","i");
	if (name != null && name != "" && reg.exec(name) != null)
		return true;
	
	return false;
}


function aheadto(id,name){
	window.location = "/private/friend?friendId="+id+"&name="+name;
}
