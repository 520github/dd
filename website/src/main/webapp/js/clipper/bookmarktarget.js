function dd_include_js(id, file,callback) {
	var getTagName = function(obj) {
		if (obj.localName != null && obj.localName != undefined)
			return obj.localName;
		if (obj.tagName != null && obj.tagName != undefined)
			return obj.tagName;
		return "";
	};
	var _doc = document.getElementsByTagName('head')[0]; 
	if (id != null) {
		for(var i = 0; i < _doc.childNodes.length; i++){
			var obj = _doc.childNodes[i];
			if (obj.src == null || getTagName(obj) != "script")
				continue;
			if (obj.src.indexOf(id) > -1) {
				if(callback){
					callback.call();
				}
				return false;
			}
		}
	}
	
	var js = document.createElement('script'); 
	js.setAttribute('type', 'text/javascript'); 
	js.setAttribute('src', file);
	js.setAttribute('charset','UTF-8');
	_doc.appendChild(js);
	if (!/*@cc_on!@*/0) {
		//if not IE
		if(callback){
			js.onload = callback
		}
	} else {
		//IE support js.onreadystatechange 
		js.onreadystatechange = function () {
			if (js.readyState == 'loaded' || js.readyState == 'complete') {
				if(callback){
					callback.call();
				}
			}
		}
	}
	return false; 
}

function DDPlugin(){
     //屏幕高度
	 this.availHeight = window.screen.availHeight;
     //预览内容div高度
	 this.preview = 0;
	 //弹出向下滑动执行步骤
	 this.step = 30;
	 //循环记录数
	 this.i = 0;
	 //循环执行的毫秒数
	 this.perTime = 70;
	 this.XUACompatible = "";
	 this.loaded = false;
}

DDPlugin.prototype.init = function() {
     var doc = document[document.compatMode == "CSS1Compat"?'documentElement':'body'];
	 this.pageWidth = doc['scrollWidth'];
	 this.pageHeight = Math.max(doc['clientHeight'],doc['scrollHeight']);
	 
	 if(this.preview <=0)this.preview = this.pageHeight;
	 if(this.availHeight > this.preview) this.preview = this.availHeight;
	 
	 this.createDiv();
	 this.dialog.style.left = "0px";
	 this.dialog.style.top = "0px";
	 this.dialog.style.position = "absolute";
	 this.dialog.style.background = "#4C4C4C";//
	 this.dialog.style.zIndex = "2147483647";
	 this.dialog.style.width = this.pageWidth + "px";
	 this.dialog.style.height = (this.pageHeight +200) + "px";
	 this.dialog.style.marginTop = "-" +this.preview + "px";
	 
	 this.setFilter(0);
	 
	 this.scrollTop = this.getScroll();
	 this.setScroll(0);
}

DDPlugin.prototype.hiddenScroll = function() {
    document.body.style.overflow = "hidden";
	document.body.style.overflowX = "hidden";
	document.body.style.overflowY = "hidden";
	document.body.scroll = "no";
}

DDPlugin.prototype.showScroll=function() {
    document.body.style.overflow = "auto";
	document.body.style.overflowX = "auto";
	document.body.style.overflowY = "auto";
	document.body.scroll = "yes";
}

DDPlugin.prototype.updateMeta = function() {
    this.hiddenBodyScroll();
    var metas = document.getElementsByTagName("meta");
    for(var i=0;i < metas.length;i++) {
        var meta = metas[i];
        var equiv = meta.httpEquiv;
        if(equiv != "X-UA-Compatible") {
            continue;
        }
        this.XUACompatible = meta.content;
        meta.content = "IE=EmulateIE8, IE=9";
    }
}

DDPlugin.prototype.restoreMeta = function() {   
    var metas = document.getElementsByTagName("meta");
    for(var i=0;i < metas.length;i++) {
        var meta = metas[i];
        var equiv = meta.httpEquiv;
        if(equiv != "X-UA-Compatible") {
            continue;
        }
        meta.content = this.XUACompatible;
    }
}

DDPlugin.prototype.hiddenBodyScroll = function() {
    var styles = document.getElementsByTagName("style");
    for(var i=0;i<styles.length;i++) {
        var style = styles[i];
        if(style.id == "hiddenBodyScrollCSS1") {
            return ;
        }
    }
    var _cssElement = document.createElement('style');
    var _cssText = "html{overflow-x:hidden;overflow-y:hidden;}";
    _cssElement.setAttribute('id', 'hiddenBodyScrollCSS1');
	_cssElement.setAttribute('type', 'text/css');
	if (_cssElement.styleSheet) { 
	    _cssElement.styleSheet.cssText = _cssText;
	}
	else { 
	    _cssElement.appendChild(document.createTextNode(_cssText)); 
	}
}

DDPlugin.prototype.createDiv = function() {
    var divObj = document.getElementById("ddbmDialog");
    if(divObj) {
        this.dialog = divObj;
        return ;
    }
    var height = window.screen.availHeight;
    this.dialog = document.createElement('div'); 
	this.dialog.setAttribute('id', 'ddbmDialog'); 
	this.dialog.setAttribute('style', 'display:none;height:' +height +'px;' );
	document.body.appendChild(this.dialog);
}

DDPlugin.prototype.openDiv = function() {
	 this.dialog.style.display = "block";
	 this.intervalid = window.setInterval("ddPlugin.showDirection();",this.perTime);
}

//显示方向
DDPlugin.prototype.showDirection = function() {
	 var plug = this;
	 
	 var cFilter = plug.getFilter();
	 var sFilter = Math.round(((100 - Number(cFilter)) / plug.step)+2.5);
	 var nFilter = Number(cFilter) + Number(sFilter);//当前透明度 
	 
	 plug.setFilter(nFilter);
	 
	 if(nFilter >100) {
	     plug.setFilter(100);
	 }
	 else{
	     plug.setFilter(nFilter);
	 }
	 
	 var top = plug.dialog.style.marginTop.split("px")[0];
	 if(plug.i == 0) top = -(plug.availHeight-200);
	 var pstep =  Math.round((plug.availHeight/plug.step)+0.5);//plug.pageHeight
	 var newTop =  Number(top) + Number(pstep);
	 
	 if(newTop > 0) {
	 	  plug.dialog.style.marginTop = "0px";
	 }
	 else {
	 	  plug.dialog.style.marginTop = newTop + "px";
	 }
	 plug.i++;
	 if(plug.i >= plug.step) {
	       plug.loaded = true;
	       if(newTop > 0) {
	 	      plug.dialog.style.marginTop = "0px";
	       }
	 	   plug.i = 0;
	 	   plug.step = 20;
	 	   plug.perTime = 2;
	 	   window.clearInterval(plug.intervalid);
	 }
}

DDPlugin.prototype.getFilter=function() {
   var filter = 0;
   var plug = this;
   var browser = plug.getBrowser();
   if(browser == "ie") {
       filter = plug.dialog.style.filter.split("=")[1].split(")")[0];
   }
   else if(browser == "firefox") {
       //filter = plug.dialog.style.mozOpacity;
       //filter = filter*100;
       filter = plug.dialog.style.opacity;
       filter = filter*100;
   }
   else if(browser == "chrome") {
       filter = plug.dialog.style.opacity;
       filter = filter*100;
   }
   else {
   }
   return filter;
}

DDPlugin.prototype.setFilter =function(filter){
   var plug = this;
   var browser = plug.getBrowser();
   if(browser == "ie") {
       plug.dialog.style.filter = 'Alpha(opacity=' + String(filter) + ')';
   }
   else if(browser == "firefox") {
       //filter = filter/100;
       //plug.dialog.style.mozOpacity = filter;
       filter = filter/100;
       plug.dialog.style.opacity = filter;
   }
   else if(browser == "chrome") {
       filter = filter/100;
       plug.dialog.style.opacity = filter;
   }
   else {
   }
}

DDPlugin.prototype.getBrowser =function() {
       var browser = "";
       var ua = navigator.userAgent.toLowerCase();
       if (window.ActiveXObject)
            browser = "ie";
       else if (ua.indexOf("firefox")>-1)
            browser = "firefox";
       else if(ua.indexOf("chrome")>-1)
            browser = "chrome";
       else if (window.MessageEvent && !document.getBoxObjectFor)
            browser = "chrome";
       else if (window.opera)
            browser = "opera";
       else if (window.openDatabase)
            browser = "safari";
       return browser;
}

DDPlugin.prototype.bind = function(obj, type, fn ) {
    if ( obj.attachEvent ) {
        obj['e'+type+fn] = fn;
        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
        obj.attachEvent( 'on'+type, obj[type+fn] );
    } else {
        obj.addEventListener( type, fn, false );
    }
        
}
DDPlugin.prototype.unbind = function( obj, type, fn ) {
    if ( obj.detachEvent ) {
        obj.detachEvent( 'on'+type, obj[type+fn] );
        obj[type+fn] = null;
    } else
        obj.removeEventListener( type, fn, false );
}
DDPlugin.prototype.obj2str = function(o) {
	var r = [], i, j = 0, len;
	if (o == null) {
		return o;
	}
	if (typeof o == 'string') {
		return '"' + o + '"';
	}
	if (typeof o == 'object') {
		if (!o.sort) {
			r[j++] = '{';
			for (i in o) {
				r[j++] = '"';
				r[j++] = i;
				r[j++] = '":';
				r[j++] = obj2str(o[i]);
				r[j++] = ',';
			}
			// 可能的空对象
			// r[r[j-1] == '{' ? j:j-1]='}';
			r[j - 1] = '}';
		} else {
			r[j++] = '[';
			for (i = 0, len = o.length; i < len; ++i) {
				r[j++] = obj2str(o[i]);
				r[j++] = ',';
			}
			// 可能的空数组
			r[len == 0 ? j : j - 1] = ']';
		}
		return r.join('');
	}
	return o.toString();
}

DDPlugin.prototype.getSelectedHtml=function(){
	  var html = "";
	    if (typeof window.getSelection != "undefined") {
	        var sel = window.getSelection();
	        if (sel.rangeCount) {
	            var container = document.createElement("div");
	            for (var i = 0, len = sel.rangeCount; i < len; ++i) {
	                container.appendChild(sel.getRangeAt(i).cloneContents());
	            }
	            html = container.innerHTML;
	        }
	    } else if (typeof document.selection != "undefined") {
	        if (document.selection.type == "Text") {
	            html = document.selection.createRange().htmlText;
	        }
	    }
	    return html;
}

DDPlugin.prototype.close=function(){
    try{
       window.$coffee2ok = null;
    }catch(e){
    }
    
	if (this.postMessageEvent) {
		this.unbind(window, "message", this.postMessageEvent);
	}
	
	document.body.removeChild(this.dialog);

	this.showScroll();
	this.setScroll(this.scrollTop);
	this.restoreMeta();
	this.showIframeOrEmbed();
}

DDPlugin.prototype.prepareImgCapture=function(){
    var plugin=this;
    var zIndex = 2147483647;
    var imgs = document.getElementsByTagName("img");
    for(var i=0;i<imgs.length;i++) {
        var img = imgs[i];
        //alert(img.width + "|" + img.height + "|" +img.src);
        if(img.width<70 || img.height<70 || img.width*img.height<150*150){
			continue;
		}
		img.source = "source_2coffee_"+i;
		img.style.zIndex = zIndex-1;
		
		/*var coffeeImg = document.createElement('img');
		coffeeImg.id = "2coffee_"+i;
		coffeeImg.style.zIndex = zIndex;
		coffeeImg.style.display = "none";
		plugin.set2coffeeImg(coffeeImg,img);
		document.body.appendChild(coffeeImg);*/
		
		plugin.bind(img,"mouseover",function(e) {
			var img = e.target||e.srcElement; 
			var id = img.source.substring(img.source.indexOf("_")+1);
			var coffeeImg = document.getElementById(id);
			if(coffeeImg == null) {
			    coffeeImg = document.createElement('img'); 
			    coffeeImg.id = id;
				coffeeImg.style.zIndex = zIndex;
				plugin.set2coffeeImg(coffeeImg,img);
				document.body.appendChild(coffeeImg);
			}
			else{
			   coffeeImg.style.display = "block";
			}
			
			plugin.bind(img,"mouseout",function(e) {
			    var left = 0 ;
			    var top = 0;
			    if(e.pageX) {
			        left = e.pageX;
			        top = e.pageY;
			    }
			    else {
			        //plugin.showScroll();
			        left = e.clientX + document.body.scrollLeft - document.body.clientLeft;
			        top = e.clientY + document.body.scrollTop - document.body.clientTop;
			    }
	
			    var imgTop = plugin.getOffsetTop(img);
			    var imgLeft = plugin.getOffsetLeft(img);
			    
			    if(left < imgLeft || top < imgTop || left >imgLeft+img.width || top>imgTop+img.height) {
			        try{
			            document.body.removeChild(coffeeImg);
			            //plugin.unbind(coffeeImg,"click",plugin.clickEvent);
			        }catch(e){
			        } 
			    }
			    
			});
			
			plugin.bind(coffeeImg,"mouseout",function(e) {
			    //document.body.removeChild(coffeeImg);
			});
			
			plugin.clickEvent = function(e) {
			    var title = img.alt;
			    if (title == null || title == "") {
				    title = document.title;
			    }
			    plugin.showSelectedImg();
			    plugin.setInitInfo(false,"Image",img.src,title,img.height);
			    
			    return false;
			}
			
			plugin.bind(coffeeImg,"click",plugin.clickEvent);
			
		});
		
		var imgReady = document.createElement('input'); 
	    imgReady.setAttribute('id', 'ddImgCaptureReady'); 
	    imgReady.setAttribute('type', 'hidden' );
    }
}

DDPlugin.prototype.set2coffeeImg=function(coffeeImg,sourceImg) {
    var plugin = this;
    coffeeImg.src = plugin.getHost() +"/images/share.png";
	coffeeImg.border = "0";
	coffeeImg.style.width = "95px";
	coffeeImg.style.height = "24px";
	coffeeImg.style.top = plugin.getOffsetTop(sourceImg) +"px";
	coffeeImg.style.left = plugin.getOffsetLeft(sourceImg) +"px";
	coffeeImg.style.position = "absolute";
	coffeeImg.style.cursor ="pointer";
	coffeeImg.style.padding = "0px";
	coffeeImg.style.margin = "0px";
}

DDPlugin.prototype.getOffsetTop = function(obj) {
    var mOffsetTop = obj.offsetTop;
    var mOffsetParent = obj.offsetParent;
    while(mOffsetParent) {
        mOffsetTop += mOffsetParent.offsetTop;
        mOffsetParent = mOffsetParent.offsetParent;
    }
    return mOffsetTop;
} 

DDPlugin.prototype.getOffsetLeft = function(obj) {
    var mOffsetLeft = obj.offsetLeft;
    var mOffsetParent = obj.offsetParent;
    while(mOffsetParent) {
        mOffsetLeft += mOffsetParent.offsetLeft;
        mOffsetParent = mOffsetParent.offsetParent;
    }
    return mOffsetLeft;
}


DDPlugin.prototype.isImgCaptureReady=function(){
	return document.getElementById("ddImgCaptureReady")?true:false;
}

DDPlugin.prototype.postMessage=function(action,data){
	var message={"action":action};
	if(data){
		message["data"]=data;
	}
	var frame = document.getElementById('ddClipperFrame').contentWindow;
	frame.postMessage(JSON.stringify(message),"*");
}
DDPlugin.prototype.getScroll = function() {
	var bodyTop = 0;    
    /*if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {    
        bodyTop = document.documentElement.scrollTop;    
    }    
    else if (typeof document.body != 'undefined') {    
        bodyTop = document.body.scrollTop;    
    }   
    else if (typeof window.pageYOffset != 'undefined') {    
        bodyTop = window.pageYOffset;    
    } */
    bodyTop = document.documentElement.scrollTop; 
    if(bodyTop <= 0) bodyTop = document.body.scrollTop;
    if(bodyTop <= 0) bodyTop = window.pageYOffset; 
    return bodyTop;
}

DDPlugin.prototype.setScroll = function(scrollTop) {
    try{
        document.documentElement.scrollTop = scrollTop;
    }catch(e){
        alert(e);
    }
    document.body.scrollTop = scrollTop; 
}

DDPlugin.prototype.prepareDialog=function(){
	var plugin=this;
	
	var scroll = "no";
	var height = plugin.pageHeight + "px";//"1500px";
	if(this.availHeight < 800) {
	    scroll = "no";
	    height = "100%";
	}
	plugin.dialog.innerHTML = "<iframe src='"+plugin.getHost()+"/newbookmark/private/portal' id='ddClipperFrame' height='"+height+"' scrolling='"+scroll+"' frameborder='0' style='width:100%;  border:0px; padding:0px; margin:0px;'>";
	
	plugin.preparePostMessage(plugin.getSelectedHtml());
}

DDPlugin.prototype.isDialogReady=function(){
	return document.getElementById("ddDialogReady")?true:false;
}
DDPlugin.prototype.isShowDialog=function(){
	return document.getElementById("ddbmDialog")?true:false;
}

DDPlugin.prototype.getHost=function(){
	return 'http://'+window.$domainName;
}

DDPlugin.prototype.prepareJS=function(callback){
	var crawlHost=this.getHost();
	/*dd_include_js(null, crawlHost+'/js/jquery.min.js',function(){
		jQueryDD=jQuery.noConflict();

		dd_include_js("jquery-ui", crawlHost+'/js/clipper/jqueryui/js/jquery-ui-1.8.20.custom.min.js',function(){
			dd_include_js("json2", crawlHost+'/js/json2.js',function(){
				jQueryDD("body").append(jQueryDD("<input type='hidden' id='ddJSReady'/>"));
				callback.call();
			});
		});
	});*/
	dd_include_js("json2", crawlHost+'/js/json2.js',function(){
	    var ddJSReady = document.createElement('input'); 
	    ddJSReady.setAttribute('id', 'ddJSReady'); 
	    ddJSReady.setAttribute('type', 'hidden' );
		callback.call();
	});
}

DDPlugin.prototype.isJSReady=function(){
	return document.getElementById("ddJSReady")?true:false;
}

function DDPlugin_ClipInfo(isSelectHtml, contentType, content, title, imgHeight) {
	this.isSelectHtml = isSelectHtml;
	this.contentType = contentType;
	this.content = content;
	this.title = title;
	this.imgHeight = imgHeight;
}

DDPlugin.prototype.setInitInfo=function(isSelectHtml,contentType,content, title, imgHeight){
	this.clipContent = new DDPlugin_ClipInfo(isSelectHtml, contentType, content, title, imgHeight);
	this.clipType = contentType;
}

DDPlugin.prototype.setClipInfo=function(isSelectHtml,contentType,content, title,imgHeight){
	var message={"url":window.location.href,"clipAction":isSelectHtml?"clipSelection":"clipArchived","contentType":contentType,"title":title};
	
	if(content){
		message["content"]=content;
	}
	if(imgHeight) {
	    message["imgHeight"]=imgHeight;
	}
	
	this.postMessage("setClipInfo", message);
}

DDPlugin.prototype.showContent=function() {
    var plug = this;
    if(!plug.loaded) return;
    plug.postMessage("showContent");
    window.clearInterval(plug.showContentId);
    
}

DDPlugin.prototype.preparePostMessage=function(selection){
	var plugin=this;
	plugin.postMessageEvent = function(e) {
		eval("var data="+e.data);
		if(data.action=='closeDialog'){
			plugin.close();
		}
		else if(data.action=='showContent'){
		    plugin.showContentId = window.setInterval("ddPlugin.showContent();",plugin.perTime);
		}
		else if(data.action=='getPreviewHeight'){
		    plugin.showScroll();
		    var previewHeight = data.data;
		    if(plugin.availHeight > 800 && previewHeight < plugin.availHeight) {
		        plugin.hiddenScroll();
		    }
		}
		else if(data.action =='hiddenScroll') {
		    plugin.hiddenScroll();
		}
		else if(data.action=='report'){
			plugin.dialogRole=data.data;
			
			if(plugin.dialogRole=='clipper'){
				if(selection!="" && plugin.dialogRole=='clipper'){
					plugin.setClipInfo(true,"HtmlClip",selection, document.title);
				}else if (plugin.clipType != null){
					plugin.setClipInfo(plugin.clipContent.isSelectHtml, plugin.clipContent.contentType, plugin.clipContent.content, plugin.clipContent.title, plugin.clipContent.imgHeight);
				}
				else {
					plugin.setClipInfo(false,"Web");
				}
			}else{
			    
			}
			
		}else if(data.action=='getUrl'){
			plugin.postMessage("setUrl", window.location.href);
		}
	};
	plugin.bind(window, 'message', plugin.postMessageEvent);
}


//隐藏iframe或flash，解决有些层出现在2coffee插件或书签的弹窗之上
DDPlugin.prototype.hideIframeOrEmbed = function() {
    var embeds = document.getElementsByTagName("embed");
    for(var i=0; i<embeds.length;i++) {
        var embed = embeds[i];
        embed.wmode = "transparent";
    }
    
    var objects = document.getElementsByTagName("object");
    //alert(objects.length);
    for(var i=0; i<objects.length;i++) {
        var object = objects[i];
        try{
            var vmode = object.getAttribute("vmode");
            if(vmode == null)vmode = "opaque";
            if(vmode == "transparent"){
                vmode = "opaque";
            }
            else if(vmode == "opaque"){
                vmode = "transparent";
            }
            object.removeAttribute("vmode");
            object.setAttribute("vmode",vmode);
        }catch(e){
            alert("ggg");
        }
        //object.wmode = "opaque";
        /*var param = document.createElement("param");
        param.setAttribute("name","wmode");
        param.setAttribute("value","opaque");
        object.appendChild(param);*/
        //alert("id:"+object.id +"|" +object.vmode);
    }
    
    var iframes = document.getElementsByTagName("iframe");
    for(var i=0; i<iframes.length;i++) {
        var iframe = iframes[i];
        var height = iframe.height;
        if(height == null) height = 0;
        if(parseInt(height) <200) {
            iframe.style.display = "none";
        }
    }
}

DDPlugin.prototype.showIframeOrEmbed = function() {
    var iframes = document.getElementsByTagName("iframe");
    for(var i=0; i<iframes.length;i++) {
        var iframe = iframes[i];
        var height = iframe.height;
        if(height == null) height = 0;
        if(parseInt(height) <200) {
            iframe.style.display = "block";
        }
    }
}

DDPlugin.prototype.showSelectedImg = function() {
    if (ddPlugin.isShowDialog())return;
	ddPlugin.init();
    ddPlugin.prepareDialog();
    ddPlugin.openDiv();
}

DDPlugin.prototype.showDDCLPlugin = function(){
	if (ddPlugin.isShowDialog())return;
	
	if(!ddPlugin.isImgCaptureReady()){
		ddPlugin.prepareImgCapture();
	}
	
	ddPlugin.hideIframeOrEmbed();
    ddPlugin.updateMeta();
	var autoClip=document.getElementById("ddAutoClip").value;
	if(autoClip=='true'){
	    ddPlugin.setInitInfo(false,"Web","","");
	    ddPlugin.clipType = "Web";
	    ddPlugin.init();
		ddPlugin.prepareDialog();
		ddPlugin.openDiv();
	}
}

DDPlugin.prototype.startDDCLPlugin = function() {
    var clipOpt = document.getElementById('ddAutoClip');
    if (clipOpt && clipOpt.value == "true" 
	   && document.domain.indexOf("duoduo.com") > -1) {
	    alert("对不起，不能在多多进行收集。");
	    return;
    }
    
    if(!ddPlugin.isJSReady()){
		ddPlugin.prepareJS(function(){
			ddPlugin.showDDCLPlugin();
		});
	}else{
		ddPlugin.showDDCLPlugin();
	}
}

var ddPlugin=new DDPlugin();
ddPlugin.startDDCLPlugin();