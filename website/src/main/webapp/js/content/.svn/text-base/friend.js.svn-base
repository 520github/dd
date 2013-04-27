var constant = {
    friendType:['all','favorite','appoint'],
    init:"true",
    friendSize:3,
    notFoundId:"notFriendId",
    isSearchFromFriend:false,
    defaultSearchText:"查找好友",
    matchSelectedClass:"1",
    lastId:"lastId",
    favoriteId:"favoriteId",
    weiboId:"weiboId",
    renrenId:"renrenId",
    friendId:"friendId",
    auf:"plug-in-pfeil-auf",
    weniger:"plug-in-pfeil-weniger",
    check:"plug-in-2coffee-panel-icon-1",
    uncheck:"plug-in-2coffee-panel-icon",
    availHeight:window.screen.availHeight,
    browseHeadHeight:0,
    headHeight:159,
    searchHeight:40,
    myHeight:67,
    friendHeadHeight:30,
    friendFooterHeight:10,
    friendMidHeight:15,
    commentHeight:230+70,    
    emptyFriendHeight:function() {
        var height = 0;
        var length = $("#shareFriendId>div[id!='my']").filter("div[id!='notFoundFriendId']").length;
        var emptySize = this.friendSize - length;
        if(emptySize > 0 ) {
            height = (this.friendHeadHeight + this.friendMidHeight + this.friendFooterHeight)*emptySize;
        }
        return height;
    },
    shareHeight:function() {
       var headHeights = this.browseHeadHeight + this.headHeight + this.searchHeight;
       return this.availHeight - headHeights - this.commentHeight;
    },
    friendHeight:function() {
        var friendHeadHeights = this.friendHeadHeight*3;
        var friendFooterHeights = this.friendFooterHeight*3;
        var friendMidHeights = this.friendMidHeight*3;
        var friendHeights = friendHeadHeights + friendFooterHeights + friendMidHeights;
        
        var headHeights = this.browseHeadHeight + this.headHeight + this.searchHeight + this.myHeight;
        var emptyHeights =  this.emptyFriendHeight();
        var useHeights = headHeights + friendHeights + this.commentHeight - emptyHeights;
        return (this.availHeight-useHeights);
    },
    perFriendHeight:34,
    additionalHeight:15,
    friendOkShowNum:function() {
        var fheight = this.friendHeight();
        var showNum = fheight/this.perFriendHeight;
        showNum = parseInt(showNum);
        if(showNum <=0)showNum =1;
        return showNum;
    },
    cantCommitCss:"plug-in-2coffee-btn-determine-no"
}

var friendCommon = {
    shareHeight:constant.shareHeight(),
    okShowNum:constant.friendOkShowNum(),
    setDivH2SpanCss:function(divId) {
        var obj = friendDefine.getDivH2SpanObject(divId);
        var css = obj.attr("class");
        if(css.indexOf("auf") >-1) {
	        css = constant.weniger;
	    }
	    else {
	        css = constant.auf;
	    }
	    obj.attr("class",css);
    },
    getDivH2SpanCss:function(divId) {
        return friendDefine.getDivH2SpanObject(divId).attr("class");
    },
    setFriendViewportHeight:function(height){
        if(height == null || height <1) height = this.shareHeight;
        //alert(height+"|"+this.shareHeight);
        friendDefine.getFriendViewportObject().css("height",height+"px");
    },
    getAllLiNumByDivId:function(divId) {
        var length = friendDefine.getDivDivLiObject(divId).length;
        return length;
    },
    getAllLiVisiableNumByDivId:function(divId) {
        var length = friendDefine.getDivDivLiVisibleObject(divId).length;
        return length;
    },
    getAllLiNum:function() {
        return friendDefine.getAllDivLiObject().length;
    },
    resetFriendScrollTop:function() {
        friendDefine.getFriendThumbObject().css("top","0px");
        friendDefine.getFriendOverviewObject().css("top","0px");
    },
    getAppointFriendValue:function() {
        var value = friendDefine.getAppointFriendObject().html();
        value = $.trim(value);
        return value.split(",");
    }
    
}

var friendAction = {
    bindEventAction:function() {
        $("#searchFriendText").focus(function() {
            if($(this).val() == constant.defaultSearchText) {
                $(this).val("");
            }
        });
        $("#searchFriendText").keyup(function(){
            friendAction.searchFriend();
        });
        $("#searchFriendButton").click(function(){
            friendAction.searchFriend();
        });
    },
    initFriendDiv:function() {
        friendCommon.setFriendViewportHeight();
        friendShowOrHidden.hideOrShowFriendScroll();
        
        var okShowNum = friendCommon.okShowNum;
        var lastNum = friendCommon.getAllLiNumByDivId(constant.lastId);
        this.searchShowDiv(constant.lastId);
        if(lastNum >= okShowNum) {            
            //return;
        }
        
        var favoriteNum = friendCommon.getAllLiNumByDivId(constant.favoriteId);	
        this.searchShowDiv(constant.favoriteId);
        if(lastNum + favoriteNum >= okShowNum) {
	        //return ;
	    }
	    
        var weiboNum = friendCommon.getAllLiNumByDivId(constant.weiboId);	
        this.searchShowDiv(constant.weiboId);
        if(lastNum + favoriteNum >= okShowNum) {
	        //return ;
	    }
	    
	    var weiboNum = friendCommon.getAllLiNumByDivId(constant.renrenId);	
        this.searchShowDiv(constant.renrenId);
	    
	    var friendNum = friendCommon.getAllLiNumByDivId(constant.friendId);

	    this.searchShowDiv(constant.friendId);
        constant.init = "false";
    },
    searchShowDiv:function(divId) {
        friendDefine.getDivDivClassObject(divId).show();
        friendDefine.getDivH2SpanObject(divId).attr("class",constant.auf);
    },
    searchHideDiv:function(divId) {
        friendDefine.getDivDivClassObject(divId).hide();
        friendDefine.getDivH2SpanObject(divId).attr("class",constant.weniger);
    },
    toggleDiv:function(divId) {
        this.bindShareFriend();
        var css = friendDefine.getDivH2SpanObject(divId).attr("class");
        if(css == constant.auf) {
            friendDefine.getDivDivClassObject(divId).hide();
        }else {
            friendDefine.getDivDivClassObject(divId).show();
        }
        friendCommon.setDivH2SpanCss(divId);
        this.searchOneShareFriend(divId);
    },
    selectShareFriend:function(friendId,divId,allType) {
    	
    	if(divId == 'weiboId'){
        	// 控制不能超过3个微博用户
        	var checkLength = friendDefine.getLiBCheckObject(divId).length;
        	if(checkLength>=3){        		
        		// 取消当前的选择 否则就够4个了
        		var b = friendDefine.getLiIdObject(friendId).first().find("b");
        		var css = b.attr("class");
		        if(css.indexOf("1") <0){	//	
        			alert("已@3位好友，一次分享最多只能@3位好友");
        			return;		        	
		        }
        	}
        }
        
    	var selectedFriends = $("#friendSelected").val();
    	var selectedWeiboFriends = $("#weibofriendSelected").val();
    	var selectedRenrenFriends = $("#renrenfriendSelected").val();
    	//alert(selectedFriends);
        friendDefine.getLiIdObject(friendId).each(function(){
            var b = $(this).find("b");
            var css = b.attr("class");
	        if(css.indexOf("1") >-1){
	        	css = constant.uncheck;
	        	if(divId == 'weiboId'){
	        		selectedWeiboFriends = selectedWeiboFriends.replace(friendId + ",", "");
	        	}
	        	else if(divId == "renrenId"){
	        	    selectedRenrenFriends = selectedRenrenFriends.replace(friendId + ",", "");
	        	}
	        	else{
	        		selectedFriends = selectedFriends.replace(friendId + ",", "");
	        	}
	        }
	        else{
	        	css = constant.check;
	        	if(divId == 'weiboId'){
		        	if (selectedWeiboFriends.indexOf(friendId) < 0) { 
						selectedWeiboFriends += friendId + ",";
					}
	        	}
	        	else if(divId == 'renrenId'){
	        	    if (selectedRenrenFriends.indexOf(friendId) < 0) { 
						selectedRenrenFriends += friendId + ",";
					}
	        	}
	        	else{
		        	if (selectedFriends.indexOf(friendId) < 0) { 
						selectedFriends += friendId + ",";
					}
	        	}
				
	        }
	       
	        if(allType == 'yes') {
	            css = constant.check;
	        }
	        else if(allType == 'no') {
	            css = constant.uncheck;
	        }
	       
	        b.attr("class",css);
        });
        if(allType == null) {
            this.checkSelectAll();
        }	
        
		$("#friendSelected").val(selectedFriends);
		$("#weibofriendSelected").val(selectedWeiboFriends);
		$("#renrenfriendSelected").val(selectedRenrenFriends);
		friendAction.isSelectedFriend();
    },
    isSelectedFriend:function() {
        var friends = $("#friendSelected").val();
        var weiboFriends = $("#weibofriendSelected").val();
        var obj = $("#shareFriend").find("#btnCommend");
        if(friends.length <1 && weiboFriends.length <1) {
            var css = obj.attr("class");
            if(css.indexOf(constant.cantCommitCss)==-1)obj.attr("class",css+" "+constant.cantCommitCss);
        }
        else {
            obj.attr("class","plug-in-2coffee-btn-determine");
        }
    },
    checkSelectAll:function() {
        friendDefine.getDivH2bAllObject().each(function(){
            var divId = $(this).parent().parent().attr("id");
            var checkLength = friendDefine.getLiBCheckObject(divId).length;
            var allLength = friendDefine.getDivDivLiVisibleObject(divId).length;
            if(checkLength > 0 && allLength ==0) {
                allLength = friendDefine.getDivDivLiObject(divId).length;
            }
            if(checkLength == 0 || allLength==0) {
                $(this).attr("class",constant.uncheck);	
            }
	        else if(checkLength >= allLength) {
	            $(this).attr("class",constant.check);
	        }
	        else {
	            $(this).attr("class",constant.uncheck);
	        }
        });
    },	
    selectAllFriend:function(divId,isAllLi) {
        var css = friendDefine.getDivH2bObject(divId).attr("class");
        var allType = "";
	    if(css.indexOf("1") >-1) {
	        allType = "no";
	        css = constant.uncheck;
	    }
	    else {
	        allType = "yes";
	        css = constant.check;
	    }
	    friendDefine.getDivH2bObject(divId).attr("class",css);
	    var liObj;
	    if(isAllLi) {
	        liObj = friendDefine.getDivDivLiObject(divId);
	    }
	    else {
	        liObj = friendDefine.getDivDivLiVisibleObject(divId);
	    }
	    liObj.each(function(){
	       var friendId = $(this).attr("id");
	       friendAction.selectShareFriend(friendId,divId,allType);
	   });
	   this.checkSelectAll();
    },
    searchFriend:function() {
        $("li[id='"+constant.notFoundId+"']").remove();
        var searchText = $.trim($("#searchFriendText").val());
	    friendShowOrHidden.showDiv();
	    friendDefine.getFriendDivObject().each(function(){
	        var divId = $(this).attr("id");
	        friendAction.searchOneShareFriend(divId);
	    });
	    this.checkSelectAll();
	    friendCommon.resetFriendScrollTop();
    },
    searchOneShareFriend:function(divId) {
        var searchText = $.trim($("#searchFriendText").val());
	    if(constant.defaultSearchText == searchText) {
	        return;
	    }
        var obj = friendDefine.getDivObject(divId);
        if(searchText == '') {
            obj.show();
        }
        else if(constant.isSearchFromFriend && divId !=constant.friendId) {
            obj.hide();
        }
        obj.find("li").each(function(){
            var text = $(this).find("p").text();
	        if(searchText =='' || text.indexOf(searchText) >-1) {
	            $(this).show();
	        }
	        else {
	            $(this).hide();
	        }
        });
        this.searchNotFound(divId);
    },
    searchNotFound:function(divId) {
        var searchText = $.trim($("#searchFriendText").val());
        if(searchText == '')return;
        
        var length = friendCommon.getAllLiVisiableNumByDivId(divId);
        if(length <1) {
            var html = "<li id='"+constant.notFoundId+"' align='center' >没有找到好友\"<font color='red' >"+searchText+"</font>\"</li>";
            //friendDefine.getDivDivUlObject(divId).append(html);
        }
    },
    autoSelectFriendByType:function() {
        var friendType = $("#friendType").val();
        if(friendType == constant.friendType[0]) {//all
            this.selectAllFriend(constant.friendId,true);
        }
        else if(friendType == constant.friendType[1]) {//favorite
            this.selectAllFriend(constant.favoriteId,true);
        }
        else if(friendType == constant.friendType[2]) {//appoint
            var values = friendCommon.getAppointFriendValue();
            for(var i=0;i<values.length;i++) {
                value = values[i];
                value = $.trim(value);
                if(value == '')continue;
                this.selectShareFriend(value);
            }
        }
    },
    getSelecteFriends:function() {
        var friends = "";
        friendDefine.getAllDivLiObject().each(function(){
            var id = $(this).attr("id");
            var css = $(this).find("b").attr("class");
            if(css.indexOf(constant.matchSelectedClass) >-1) {
                if(friends.indexOf(id) == -1) {
                    friends = friends + id + ",";
                }
            }
        });
        if(friends.indexOf(",") >-1) {
            friends = friends.substring(0, friends.length - 1);
        }
        return friends;
    },
    getMy:function() {
        var my = "";
        var obj = friendDefine.getMyDivLiObject();
        var css = obj.find("b").attr("class");
        if(css.indexOf(constant.matchSelectedClass)> -1) {
            my = obj.attr("id");
        }
        return my;
    },
    bindShareFriend:function() {
        var s = friendDefine.getFriendObject();
        if(s.length > 0 && s.attr("scrolled") != "1"){
			s.tinyscrollbar();
			s.attr("scrolled", "1");
		}
    }
}

var friendShowOrHidden = {
    showDiv:function() {
        friendDefine.getFriendDivObject().each(function() {
            var divId = $(this).attr("id");
            friendDefine.getDivDivObject(divId).show();
            friendDefine.getDivH2SpanObject(divId).attr("class",constant.auf);
	    });
    },
    hideOrShowFriendScroll:function() {
        if(friendCommon.getAllLiNum() < friendCommon.okShowNum) {
            friendDefine.getFriendScrollObject().hide();
        }
        else {
            friendDefine.getFriendScrollObject().show();
        }
    }
}

var friendDefine = {
    getFriendObject:function() {
        return $("#shareFriendId");
    },
    getFriendDivObject:function() {
        return this.getFriendObject().find(".overview>div[id!='my']").filter("div[id!='notFoundFriendId']");
    },
    getFriendThumbObject:function() {
        return this.getFriendObject().find(".thumb");
    },
    getFriendViewportObject:function() {
        return this.getFriendObject().find(".viewport");
    },
    getFriendOverviewObject:function() {
        return this.getFriendObject().find(".overview");
    },
    getFriendScrollObject:function() {
        return this.getFriendObject().find("div[class='scrollbar']");
    },
    getAllDivLiObject:function() {
        return this.getFriendDivObject().find("li");
    },
    getAllDivLiVisibleObject:function() {
        return this.getFriendDivObject().find("li:visible");
    },
    getDivObject:function(divId) {
        return $("#" + divId);
    },
    getDivH2Object:function(divId) {
        return this.getDivObject(divId).find("h2");
    },
    getDivH2bObject:function(divId) {
        return this.getDivH2Object(divId).find("b");
    },
    getDivH2bCheckObject:function() {
        return $("div h2 b[class='"+constant.check+"']");
    },
    getDivH2bAllObject:function() {
        return $("div h2 b");
    },
    getDivH2SpanObject:function(divId) {
        return this.getDivH2Object(divId).find("span");
    },
    getDivDivObject:function(divId) {
        return this.getDivObject(divId).find("div");
    },
    getDivDivClassObject:function(divId) {
        return this.getDivObject(divId).find("div[class='plug-in-2coffee-panel-title-left']");
    },
    getDivDivViewportObject:function(divId){
        return this.getDivObject(divId).find("div[class='viewport']");
    },
    getDivDivLiObject:function(divId) {
        return this.getDivDivObject(divId).find("li");
    },
    getDivDivUlObject:function(divId) {
        return this.getDivDivObject(divId).find("ul");
    },
    getDivDivLiVisibleObject:function(divId) {
        var type = "visible";
        if(constant.init == "true") {
            type = "visible";//hidden
        }
        return this.getDivDivObject(divId).find("li:"+type);
    },
    getLiIdObject:function(id) {
        return $("#shareFriendId li[id='"+id+"']");
    },
    getLiBCheckObject:function(divId) {
        return this.getDivObject(divId).find("li b[class='"+constant.check+"']");
    },
    getMyDivLiObject:function() {
        return $("#my").find("li");
    },
    getAppointFriendObject:function() {
        return $("#appointFriendId");
    }
}

var tabIndex = 0;
function friendTab(tab) {
	tabIndex = parseInt(tab);
	
	if (tabIndex == 0) {
		$(".check-box-press-a").addClass("check-box-press-change").addClass("check-box-press-l");
		$(".check-box-press-z").removeClass("check-box-press-change-z");
		$(".check-box-press-d").removeClass("check-box-press-change-r");
	}
	else if(tabIndex == 1) {
		$(".check-box-press-a").removeClass("check-box-press-change").removeClass("check-box-press-l");
		$(".check-box-press-z").addClass("check-box-press-change-z");
		$(".check-box-press-d").removeClass("check-box-press-change-r");
	}
	else if(tabIndex == 2) {	//	如果选择的是互粉好友
		$(".check-box-press-a").removeClass("check-box-press-change").removeClass("check-box-press-l");
		$(".check-box-press-z").removeClass("check-box-press-change-z");
		$(".check-box-press-d").addClass("check-box-press-change-r");
	}
	
	for (var i=0; i<3; i++) {
		if (i == tabIndex) {
			$("#friendTab" + i).show();
			$("#friendTab" + i).find("li").show();
		}
		else {
			$("#friendTab" + i).hide();
		}
	}
	
	bindScrollbar(tabIndex);
}

function selectFriend(id) {
	var selectedFriends = $("#friendSelected").val();
	var selectedWeiboFriends = $("#weibofriendSelected").val();
	var repeatSelected = $("#repeatSelected").val();	//	那些已经分享过了的且这次分享又被选中了的
	var shared = $(".fch_" + id).val();	//	以前是否已经分享过 不等于空表示已经分享过
	var controls = $("li[fid='"+id+"']").each(function(i) {
		var c = $(this).find(".fck_friend");
		var selected = c.val();
		if (selected == "0") {	// 选中了的话
			$(this).find(".fimg_" + id).attr("src", "/images/plug-in-pic-d1.png");
			c.val("1");
			if (selectedFriends.indexOf(id) < 0) { 
				selectedFriends += id + ",";
				addToSelect(id, c.attr("fname"));
				if (shared != "") {
					repeatSelected += shared + ",";
				}
			}
		} else {	//	deselected
			$(this).find(".fimg_" + id).attr("src", "/images/plug-in-pic-d.png");
			c.val("0");
			selectedFriends = selectedFriends.replace(id + ",", "");
			
			removeFromSelected(id, c.attr("fname"));
			if (shared != "") {
				repeatSelected = repeatSelected.replace(shared + ",", "");
			}
		}
	});
	
	$("#friendSelected").val(selectedFriends);
	$("#weibofriendSelected").val(selectedWeiboFriends);
	$("#repeatSelected").val(repeatSelected);
}

function friendMessage(type,message) {
    openPopupMessage(type,message);
}

function jianpanhuicheche(){
	var e =  window.event || arguments.callee.caller.arguments[0];
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
	
	var key = $("#friendKey").val();
	var tab = $("div[name='frienddivname']:visible");
	
	tab.find("li").each(function(i) {
		var c = $(this).find(".fck_friend");
		if (key == null || key == "") {
			$(this).show();
		}
		else if (isMatch(c, key)) {
			$(this).show();
		}
		else {
			$(this).hide();
		}
	});
}

function isMatch(c, key) {
	var name = c.attr("fname");
	var k = key.replace("\\", "\\\\").replace(".", "\.");
	
	var reg = new  RegExp("^.*" + k + ".*$","i");
	if (name != null && name != "" && reg.exec(name) != null)
		return true;
	
	name = c.attr("aname");
	if (name != null && name != "" && reg.exec(name) != null)
		return true;
	
	return false;
}

function addToSelect(id, name) {
	$("#ddSelectedFriends").append('<a href="javascript:selectFriend(\'' + id + '\')" fid="' + id + '">' + name + '<img src="/images/close-pic1.png" /></a>');
}

function removeFromSelected(id, name) {
	$("#ddSelectedFriends").find("a[fid='" + id + "']").remove();
}

function bindScrollbar(index) {
	var s = $('#scrollbar' + index);
	if(s.length > 0 && s.attr("scrolled") != "1"){
		s.tinyscrollbar();
		s.attr("scrolled", "1");
	}
};

function bindScrollbars() {
	var s = $('#scrollbar0');
	if(s.length > 0){
		s.tinyscrollbar();
	}
	s = $('#scrollbar1');
	if(s.length > 0){
		s.tinyscrollbar();
	}
	s = $('#scrollbar2');
	if(s.length > 0){
		s.tinyscrollbar();
	}
};

//分享给朋友
function openShareFriendLayer(repositoryId, contentId) {
	$('#loading').fadeIn();
	$.get('/friend/myFriends', {
		'contentId' : contentId
		}, function(data) {
		$("#shareFriend").html(data);

		$('#loading').fadeOut();

		$("#commentText").focus(function() {
			if ($(this).val() == "我觉得这个东东可能对你有帮助，希望你喜欢哦！") {
				$(this).val("");
			}
		});
		$("#commentText").blur(function() {
			if ($(this).val() == "") {
				$(this).val("我觉得这个东东可能对你有帮助，希望你喜欢哦！");
			}
		});
		$("#commentText").keyup(function() {
			if ($("#commentText").val().length >= 140) {
				$("#commentText").val($("#commentText").val().substr(0, 140));
			}
			$("#ddCommentRemain").html(140 - $("#commentText").val().length);
		});
		
		$("#shareFriend").find("#btnCancel").click(function() {
			layer_close();
		});
		
		$("#shareFriend").find("#btnCommend").click(function() {
            var css = $(this).attr("class");
            if(css.indexOf(constant.cantCommitCss) >-1 ) {
                return;
            }
            
			$(this).attr("disabled", "disabled");
			var score = 0;
			var comment = $("#commentText").val();
			var selected = $("#friendSelected").val();
			var selectedWeiboFriends = $("#weibofriendSelected").val();
			
			if (selected.length < 1 && selectedWeiboFriends.length<1) {
				alert("必须选择好友");
				$(this).removeAttr("disabled");
				return;
			}
			
			selected = selected + ";" + selectedWeiboFriends;	// 将微博账户和站内账户合并起来以供提交 以分号作为delimiter
			var repeatSelected = $("#repeatSelected").val();
			sArrray = selectedWeiboFriends.split(",");
			
			if (repeatSelected != "") {
				if (!confirm("部分选择的好友已经分享过了，是否再次分享？")) {
					var strs = repeatSelected.split(",");
					for ( var i = 0; i < strs.length; i++) {
						var str = strs[i];
						if (str == "")
							continue;
						selected = selected.replace(str + ",", "");	//	去重 将已经分享过的删掉
					}
					if (selected == "") {
						//alert("当前好友已分享。");
						layer_close();
						$(this).removeAttr("disabled");
						return;
					}
				}
			}

			$.post("/friend/share", {
				rid : repositoryId,
				friends : selected,
				grade : score,
				//	isWeiboFriends : isWeiboFriends,
				comment : comment
			}, function(data) {
				if (data == "0") {
					alert("推荐失败！再推一次。");
					$(this).removeAttr("disabled");
					return;
				} else if (data == "1") {
					friendMessage("success", "分享成功。这个分享太有才了！");
					//alert("分享成功。这个分享太有才了！");
					//layer_close();
				}
			});
		});
		$.layer({
			v_title : "分享给朋友",
			v_istitle : false,
			//v_skin:0,
			v_shade : true,
			v_showclose : false,
			v_box : 1,
			v_dom : '#shareFriend', //id
			v_area : [ '579px', '575px' ],
			v_btns : 0,
			v_btn : [ "保存" ],
			v_move : false,
			v_offset : [ '50px', '350px' ]
		//为空时数据默认
		});
		bindScrollbar(0);
	});
}