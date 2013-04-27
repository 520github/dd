var __coffee2innerCommon__= {
    isDebug:true,
    getJSON:function(url, data, callback,errorFn) {
        var para = {
			url : url,
			dataType : 'json',
			type : 'POST',
			cache : false,
			async : false,
			data : data,
			success : callback
		};
		if(errorFn != null) {
		    para["error"] = errorFn;
		}
		$.ajax(para);
	},
	replaceRegex:function(source,reg,replace) {
	    if(source == null)return source;
	    return source.replace(reg, replace);
	},
	log:function(obj) {
	    if(!this.isDebug) {
	        return;
	    }
	    if(obj !=null){
	        try{
	            if(window.console && window.console.log)window.console.log(obj);
	        }catch(e){
	            //alert(e);
	        }
	    }
	}
}

var __coffee2innerEvent__= {
    bind:function( obj, type, fn ) {
        if ( obj.attachEvent ) {
	        obj['e'+type+fn] = fn;
	        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
	        obj.attachEvent( 'on'+type, obj[type+fn] );
	    } else {
	        obj.addEventListener( type, fn, false );
	    }
	},
	unbind:function( obj, type, fn ) {
	    if ( obj.detachEvent ) {
	        obj.detachEvent( 'on'+type, obj[type+fn] );
	        obj[type+fn] = null;
	    } else
	        obj.removeEventListener( type, fn, false );
    },
    preparePostMessage:function() {
        var pme = this;
        pme.postMessageEvent = function(e) {
            eval("var data="+e.data);
            if(data.action == 'responseClipper') {
                __coffee2innerData__ = data.data;
                __coffee2innerBusi__.preview();
                __coffee2innerBusi__.cycleAutoClose();
            }
        };
        this.bind(window,'message',pme.postMessageEvent);
    },
    post2parentMessage:function(action,data) {
        var message={"action":action};
		if(data){
			message["data"]=data;
		}
		window.parent.postMessage(JSON.stringify(message), "*");
    }
}

var __coffee2innerData__= {
}

var __coffee2innerBusi__= {
    previewFrom:"client",
    contentType:['Web','HtmlClip','Image','Product','Video'],
    defaultComment:"我觉得这个东东可能对你有帮助，希望你喜欢哦！",
    successTips:"将收藏到多多,推送到手机客户端！<br>(3秒后自动关闭)",//<br>同时分享给我的所有好友。
    serverData:{},
    autoCloseCount:5,
    defaultTag:"Later",//Collect
    go2serverUrl:['http://item.taobao.com/',
                  'http://detail.tmall.com/',
                  'http://www.360buy.com/product/',
                  'http://v.youku.com/v_show/id_'],
    showFastStep:function() {
        //$("#plug-in-2coffee").hide();
        $("#ddPreview").hide();
        $("#ddAction").hide();
        $("#ddPreview_loading").hide();
        $("#collection-close").hide();
        $("#ddFastStep").show();
        $("#ddFastMore").hide();
    },
    preview:function() {
        for(var i=0;i<__coffee2innerBusi__.go2serverUrl.length;i++) {
            var tempUrl = __coffee2innerBusi__.go2serverUrl[i];
            if(__coffee2innerData__.busiUrl.indexOf(tempUrl)>-1) {
                this.previewFrom = "server";
                break;
            }
        }
        if(__coffee2innerData__.contentType != this.contentType[0] 
            &&  __coffee2innerData__.contentType != this.contentType[1]) {
            this.previewFrom = "server";
        }
        if(__coffee2innerData__.contentType == this.contentType[0] 
            && __coffee2innerData__.content == '') {
            this.previewFrom = "server";
        }
        
        if(this.previewFrom == "server") {
            this.previewFromServer();
        }
        else if(this.previewFrom == "client") {
            this.previewFromClient();
        }
        this.previewTips();
    },
    previewTips:function() {
        $("#ddFastTips h3").html(this.successTips);
        $("#ddFastMore").show();
    },
    previewFromServer:function() {
        var point = this;
        if(__coffee2innerData__.contentType == this.contentType[1]) {//HtmlClip
            return;
        }
        __coffee2innerCommon__.getJSON(
			"/newbookmark/preview",
			{
				"url" : __coffee2innerData__.busiUrl,
				"contentType" : __coffee2innerData__.contentType,
				"refer" : __coffee2innerData__.url
			},
			function(data) {
			    point.serverData = data;
			}
	    );
    },
     previewFromClient:function(data) {
    	if(data == null)return;       
        this.serverData.data = data;
    },
    getArchivedId:function() {
        var id = "";
        if(this.serverData.data !=null) {
            id = this.serverData.data.id;
        }
        if(id == null || id=="") {
            id = __coffee2innerData__.contentId;
        }
        this.log("getArchivedId:" +id);
        return id;
    },
    getTags:function() {
        var tags = [];
		if ($("#ddCollect").attr("checked")) {
			tags.push("Collect");
		}
		if ($("#ddReadLater").attr("checked")) {
			tags.push("Later");
		}
		if ($("#dd2Coffee").attr("2coffee")) {
			tags.push("Later");
		}
		if(friendAction.getMy().length > 0) {
		    tags.push(this.defaultTag);
		}
		return tags.join(",");
    },
    getToFriends:function() {
        var selectedFriends = friendAction.getSelecteFriends();
        if(selectedFriends.length > 0) {
            return selectedFriends;
        }
        var allDDFriValue = $("input[name='allDDFri']:checked").val();
		var toFriends = $("#friendSelected").val();
		if (toFriends != null && toFriends != "") {
			toFriends = toFriends.substring(0, toFriends.length - 1);
		}
		return toFriends;
    },
    commitRequest:function() {
        var point = this;
        $("#ddClipperBtn").click(function(){
            var css = $(this).attr("class");
            if(css.indexOf("plug-in-2coffee-btn-determine-no") >-1)return;
            
            var toFriends = point.getToFriends();
            if($.trim(toFriends).length < 1) {
			    /*if (!$("#ddCollect").attr("checked") && !$("#ddReadLater").attr("checked")) {
			        alert("请选择收藏或者分享给好友！");
			        return ;
                }*/
                if(friendAction.getMy().length<1){
                    return;
                }
			}
			point.saveRequest();
        });
    },
    closeRequest:function() {
        var point = this;
        $("#ddCloseBtn").click(function(){
			point.close();
	    });
    },
    commentRequest:function() {
		$.bindTextAreaCounter({
		    textAreaObj:"ddClpComment",
		    counterObj:"ddCommentRemain",
		    defaultValue:this.defaultComment,
		    defaultLength:140
		});
    },
    request:function() {
        this.commitRequest();
        this.closeRequest();
        this.commentRequest();
    },
    setComment:function(tag) {
       if(tag!=null)return;
       var comment = $("#ddClpComment").val();
	   if (this.defaultComment == comment) {
		   //$("#ddClpComment").val("");
	   } 
    },
    saveRequest:function(tag) {
        var paraTag = tag;
        var point = this;
        var loading = $("#ddClipperBtn").attr("loading");
        if(loading == null)loading = "";
	    if(loading =="yes") {
	        alert("无需再提交哦！已正在处理中，请稍等......");
	        return ;
	    }
	    $("#ddClipperBtn").attr("loading","yes");
	    
	    this.setComment(tag);
		
		if(tag == null) {
		    tag = this.getTags();
		}
        var data = {
            "contentType":__coffee2innerData__.contentType,
            "url":__coffee2innerData__.busiUrl,
            "title":__coffee2innerData__.title,
            "tag":tag,
            "toFriends":this.getToFriends(),
            "comment" : $("#ddClpComment").val(),
			"score" : $("#ddCliperScore").val()
        };
        if(paraTag != null && paraTag != "") {
            //data["toFriends"] = "all";
            data["comment"] = "";
        }

        if(this.previewFrom == "client") {//
            var html = __coffee2innerData__.content;
            if(html == null)html = "";
            data["htmlPayload"]=html;
        }
        else {
            data["id"]=this.getArchivedId();
        }            
        //set product data
        var productData = __coffee2innerData__.product;
        if(productData != null && productData.price!= null) {
            data["productImageUrl"] = productData.imageUrl;
            data["productPrice"] = productData.price;
            data["productName"] = productData.name;
        }
        data["pluginLogData"] = this.getPluginLog();
        __coffee2innerCommon__.getJSON("/newbookmark/private/store", data,
			function(data) {
			    if(paraTag == null || paraTag=="") {
			        point.resultTips(data);
			    }
			    else if(__coffee2innerData__.contentType == point.contentType[1]){
			        __coffee2innerData__.contentId = data.contentId;
			    }
			},
			function(XMLHttpRequest, textStatus, errorThrown) {
			    var text = "内容抓取失败,请稍后重试!";
			    if(textStatus.indexOf("parsererror")>-1) {
			        text = "系统发现你已退出了登陆,请重新登陆后进行操作!";
			    }
			    alert(text);
			    point.close();
			});
		$("#ddClipperBtn").attr("loading","");
    },
    getPluginLog:function() {
        PluginLog.obj.setStartJsHtmlTime(__coffee2innerData__.startJsHtmlTime);
        PluginLog.obj.setEndJsHtmlTime(__coffee2innerData__.endJsHtmlTime);
        PluginLog.obj.setStartRequestTime(__coffee2innerData__.startRequestTime);
        
        return PluginLog.obj.getPluginLogData();
    },
    resultTips:function(result) {
        var point = this;
        var msg = result.msg;
	    var code = result.code;
	    if(code == null)code = "";
	    if(code != "200") {
	        alert(msg);
	        return;
	    }
	    
	    __coffee2innerEvent__.post2parentMessage('setScrollTop','0');
	    msg = "操作成功";
	    $("#plug-in-2coffee").hide();
	    this.showResultTips(msg);
	    
    },
    showResultTips:function(mes) {
        $("#messageId #pop-up span").html(mes);
         //__coffee2innerEvent__.post2parentMessage('setHeight','600px');
        $.layer({
	       v_istitle:false,
	       v_btns : 0,
	       v_box : 1,
	       v_showclose:false,
	       v_dom : '#messageId #pop-up',
	       v_showtime : 2,
	       v_area : ['450px','30px']
		});
		window.setTimeout("__coffee2innerBusi__.close()",2000);
    },
    close:function() {
        if(this.cycleId) {
            try{
                window.clearInterval(this.cycleId);
            }catch(e){
                
            }
        }
        __coffee2innerEvent__.post2parentMessage('close');
    },
    autoClose:function() {
        this.autoCloseCount--;
        if(this.autoCloseCount < 1) {
            try{
	            window.clearInterval(this.cycleId);
	        }catch(e) {
	        }
	        __coffee2innerBusi__.saveRequest(this.defaultTag);
            __coffee2innerBusi__.close();
            this.autoCloseCount = 5;
        } 
    },
    cycleAutoClose:function() {
        this.cycleId = window.setInterval("__coffee2innerBusi__.autoClose()",1000);
    },
    go2more:function() {
        var title = jQuery(".plug-in-2coffee-user-name a").attr("title");
        if(title == null || title == "") {
            alert("你还未登录,只有登录后才能进行该操作！");
            jQuery("#guestUserDiv").show();
            return ;
        }
        this.showShareFriend();
        try{
            window.clearInterval(this.cycleId);
        }catch(e) {
        }
        try{
            document.body.style.background = 'transparent';
        }catch(e){
            alert(e);
        }
        //__coffee2innerEvent__.post2parentMessage('setHeight','full');
        __coffee2innerEvent__.post2parentMessage('right');
        __coffee2innerEvent__.post2parentMessage('hiddenScroll');
        this.setTitle();
        this.setContent();
        this.resetImg();
        $("#ddFastStep").hide();
        $("#ddPreview").hide();
        $("#ddAction").show();
        $("#ddPreview_loading").hide();
        $("#collection-close").hide(); 
    },
    showShareFriend:function() {
        PluginFriendsShare.obj.getAllFriends();
    },
    setTitle:function() {
        var title = "";
        if(this.serverData.data !=null) {
            title = this.serverData.data.title;
        }
        if(title == null || title =="") {
            title = __coffee2innerData__.title;
        }
        $("#ddPreview_title").html(title);
    },
    setContent:function() {
        var data = this.serverData.data;
        if(data == null) {
            data = __coffee2innerData__;
        }
        var content = "";
        var contentType = data.contentType;
        if(contentType == this.contentType[2]) {//image
            content = "<img width='400px' align='center' src='" + data.archiveUrl + "'/>";
        }
        else if(contentType == this.contentType[3]) {//product
            content = "<p>价格：" + data.product.price + "</p>";
            content+= "<img src='" + data.product.picture
					+ "' width='400px' height='300px'/>";
        }
        else if(contentType == this.contentType[4]) {//video
            content = data.summary ;
        }
        else {
            content = data.content;
        }
        if(content == null || $.trim(content) == '') {//
            this.setNotContent();
            return ;
        }
        
        content = __coffee2innerCommon__.replaceRegex(content,/<script[^>]*>[^<]*<\/script>/ig,"");
	    try{
	        $("#ddPreview_content").html(content);
	    }catch(e) {
	        this.log("content found error:\n" + content);
	        this.setNotContent();
	    }
    },
    setNotContent:function() {
        var data = this.serverData.data;
        $("#ddPreview_url").html(__coffee2innerData__.title + "" +__coffee2innerData__.url);
        $("#ddPreview_yes").hide();
        $("#ddPreview_no").show();
    },
    resetImg:function() {
        var point = this;
        var type = __coffee2innerData__.contentType;
	    var url = __coffee2innerData__.url;
	    var width = 0;
	    var height = 0;
	    $("#ddPreview img").each(function(i) {
	        if(type == point.contentType[3]) {//Product
	            width = 400;
	            height = 300;
	        }
	        else if(type ==point.contentType[4]) {//Video
	            width = 400;
	            height = 300;
	        }
	        else if(type ==point.contentType[2]) {//Image
	            width = 400;
	            height = 300;
	            if(__coffee2innerData__.imgHeight >300)height = __coffee2innerData__.imgHeight;            
	        }
	        else if(type==point.contentType[0] || type==point.contentType[1]) {//Web HtmlClip
	            if($(this).attr("width")>200)width = 400;
	            else{
	                width = $(this).attr("width");
	            }
	            if($(this).attr("height")>50 && $(this).attr("height")<200)height = 200;
	            else {
	                height = $(this).attr("height");
	            }
	        }
	        
	        if(width >0) $(this).attr("width", width+"px"); 
	        if(height >0) $(this).attr("height", height+"px");
	        
	        var src = $.trim($(this).attr("src"));
	        var srcTemp = src.toLowerCase();
	        if(srcTemp.substring(0,4) == "http") {
	            return;
	        }
	        if(srcTemp.substring(0,2) =="./") {
	           src = src.substring(2);
	           src = point.getImgSrc(url,src,1);
	        }
	        else if(srcTemp.substring(0,3) =="../") {
	           var size =1;
	           while(src.substring(0,3) =="../") {
	               src = src.substring(3);
	               size++;
	           }
	           src = point.getImgSrc(url,src,size);
	        }
	        else {
	           src = point.getImgSrc(url,src,1);
	        }
	        
	        $(this).attr("src",src);
	        
	    });
    },
    getImgSrc:function(url,src,size) {
        for(var i=0;i<size;i++) {
	        url = url.substring(0,url.lastIndexOf("/"));
	    }
	    url = url + "/" + src;
	    return url;
    },
    log:function(obj){
        __coffee2innerCommon__.log(obj);
    }
    
}