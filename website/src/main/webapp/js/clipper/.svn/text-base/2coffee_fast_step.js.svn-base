var __coffee2Common__={
    isDebug:true,
    createInput:function(id,type,parent) {
        var input = this.createObject("input",id,parent);
        if(type == null || type =="") type = "text";
        try{
           input.setAttribute('type', type);
        }catch(e){
           try{
              input.type = type;
           }catch(e){
              //alert("yaya:" +e);
           }
        }
        return input;
    },
    createDiv:function(id,parent){
        return this.createObject("div",id,parent);
    },
    createA:function(id,text,parent){
        var a = this.createObject("a",id,parent);
        a.setAttribute("style","color:red");
        a.setAttribute("href","javascript:__2coffeeFastStep__.go2MoreStep()");
        if(text !=null && text!="") {
            a.appendChild(document.createTextNode(text));
        }
        return a;
    },
    createStyle:function(id,cssText,parent){
        var style = this.createObject("style",id,parent);
        style.setAttribute("type","text/css");
        if(cssText !=null && cssText!="") {
            if(style.sytleSheet){
                style.styleSheet.cssText = cssText;
            }
            else {
                try{
                    style.appendChild(document.createTextNode(cssText));
                }catch(e){
                    try{
                        style.styleSheet.cssText = cssText;
                    }catch(e){
                    }
                }                
            }
        }
        return style;
    },
    createObject:function(tag,id,parent){
        var obj = document.createElement(tag);
        if(id !=null && id!="")obj.setAttribute("id",id);
        if(parent !=null)parent.appendChild(obj);
        return obj;
    },
    getSelectedHtml:function() {
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
    },
    getBrowser:function() {
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
    },
    getBrowserVersion:function() {
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject) {
             try{
                 Sys.ie = document.documentMode;
             }catch(e){
                 Sys.ie = ua.match(/msie ([\d.]+)/)[1]
             }
        }
        else if (ua.indexOf("firefox")>-1)
            Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1]
        else if(ua.indexOf("chrome")>-1)
            browser = "chrome";
        else if (window.MessageEvent && !document.getBoxObjectFor)
            Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1]
        else if (window.opera)
            Sys.opera = ua.match(/opera.([\d.]+)/)[1]
        else if (window.openDatabase)
            Sys.safari = ua.match(/version\/([\d.]+)/)[1];
        return Sys;
    },
    getPageHeight:function() {
        var doc = this.getDoc();
        return Math.max(doc['clientHeight'],doc['scrollHeight']);
    },
    getAvailHeight:function() {
        return window.screen.availHeight;
    },
    getUseHeight:function() {
        var useHeight;
        if(self.innerHeight) {
            useHeight = self.innerHeight;
        }
        else if(document.documentElement && document.documentElement.clientHeight) {
            useHeight = document.documentElement.clientHeight;
        }
        else if(document.body) {
            useHeight = document.body.clientHeight;
        }
        var availHeight = this.getAvailHeight();
        if(useHeight > availHeight) {
             this.log("useHeight is more availHeight");
             useHeight = availHeight;
        }
        return useHeight;
    },
    getDoc:function() {
        try{
            document[document.compatMode == "CSS1Compat"?'documentElement':'body'];
        }catch(e){
            alert("getDoc:" + getDoc);
        }
        return document[document.compatMode == "CSS1Compat"?'documentElement':'body'];
    },
    removeChild:function(removeId) {
        try{
            var removeObj = document.getElementById(removeId);
            document.body.removeChild(removeObj);
        }catch(e){
            alert(e);
        }
    },
    setFilter:function(obj,filter) {
        var browser = this.getBrowser();
        if(browser == "ie") {
           if(filter >1)filter = filter/100;
	       obj.style.filter = 'Alpha(opacity=' + String(filter) + ')';
	    }
	    else if(browser == "firefox") {
	       filter = filter/100;
	       obj.style.opacity = filter;
	    }
	    else if(browser == "chrome") {
	       filter = filter/100;
	       obj.style.opacity = filter;
	    }
    },
    hiddenIframeOrFlash:function() {
        var embeds = document.getElementsByTagName("embed");
	    for(var i=0; i<embeds.length;i++) {
	        var embed = embeds[i];
	        embed.wmode = "transparent";
	    }
	    
	    var iframes = document.getElementsByTagName("iframe");
	    for(var i=0; i<iframes.length;i++) {
	        var iframe = iframes[i];
	        var height = iframe.height;
	        var width = iframe.width;
	        if(height == null) height = 0;
	        if(width == null) width = 0;
	        
	        if(parseInt(width) <400 || parseInt(height) <200) {
	            iframe.style.display = "none";
	        }
	    }
    },
    showIframeOrFlash:function() {
        var iframes = document.getElementsByTagName("iframe");
	    for(var i=0; i<iframes.length;i++) {
	        var iframe = iframes[i];
	        var height = iframe.height;
	        var width = iframe.width;
	        if(height == null) height = 0;
	        if(width == null) width = 0;
	        
	        if(parseInt(width) <400 || parseInt(height) <200) {
	            iframe.style.display = "block";
	        }
	    }
    },
    getHtmlEncode:function() {
        var htmlEncode = '';
        var metas = document.getElementsByTagName("meta");
        if(metas == null || metas.length<1)return htmlEncode;
        for(var i=0;i < metas.length;i++) {
		     var meta = metas[i];
		     var equiv = meta.httpEquiv;
		     if(equiv == null)continue;
		     if(equiv !="Content-Type")continue;
		     var content = meta.content;
		     if(content == null || content.length<1)break;
		     content = content.toLocaleLowerCase();
		     var indexFlag = "charset=";
		     var index = content.indexOf(indexFlag);
		     if(index >-1) {
		         htmlEncode = content.substring(index+indexFlag.length);
		     }
		     break;
        }
        return htmlEncode;
    },
    str2Unicode:function(str) {
        return escape(str).toLocaleLowerCase().replace(/%u/gi,'\\u');
    },
    str2gb2312:function(str) {
        return unescape(str.replace(/\\u/gi,'%u'));
    },
    log:function(obj) {
	    if(!this.isDebug) {
	        return;
	    }
	    if(obj !=null){
	        try{
	            if(window.console && window.console.log) {
	                //console.log(obj);
	            }
	        }catch(e){
	        }
	    }
	}
};

var __coffee2Constant__= {
    div1Id:"coffee2-fa11st-st11ep-d11iv-1",
    div2Id:"coffee2-fa11st-st11ep-d11iv-2",
    div3Id:"coffee2-fa11st-st11ep-d11iv-3",
    firstStyleId:"coffee2-fa11st-st11ep-st11yle-fi11rst",
    secordStyleId:"coffee2-fa11st-st11ep-st11yle-se11cord",
    iframe1Id:"coffee2-fa11st-st11ep-ifr11ame-1",
    imgReadyId:"coff222-fast-step-img-ready-id",
    jsReadyId:"coff222-fast-step-js-ready-id",
    ddAutoClipId:"ddAutoClip",
    host:window.$httpType+'://'+window.$domainName,
    contentType:['Web','HtmlClip','Image'],
    go2serverUrl:['http://item.taobao.com/item.htm?',
                  'http://detail.tmall.com/item.htm',
                  'http://www.360buy.com/product/',
                  'http://v.youku.com/v_show/id_'],
    domainSuffix:function() {
        var ds = "duoduo.com";
        if(window.$domainName.indexOf(".") >-1) {
            ds = window.$domainName.substring(window.$domainName.indexOf(".")+1);
        }
        //alert("ds:"+ds);
        return ds;
    }
};

var __coffee2ClipperPara__= {
    title:document.title,
    url:window.location.href,
    busiUrl:window.location.href,
    contentType:__coffee2Constant__.contentType[0],
    content:'',
    imgHeight:0
};

var __coffe2outEvent__= {
    bind:function( obj, type, fn ) {
        if ( obj.addEventListener ) {
	        obj.addEventListener( type, fn, false );
	    } else {
	        obj['e'+type+fn] = fn;
	        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
	        obj.attachEvent( 'on'+type, obj[type+fn] );
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
            if(data.action == 'setHeight') {
                if(data.data == '100%') {
                    __coffee2FastStep__.screenType = __coffee2FastStep__.screenFullType;
                }
                else if(data.data == 'full') {
                    __coffee2FastStep__.scrollTop = __coffee2FastStep__.getScrollTop();
                    __coffee2FastStep__.setScrollTop(0);
                    data.data = __coffee2Common__.getPageHeight() + "px";
                    __coffee2FastStep__.getDiv2Obj().style.position = "absolute";
                    __coffee2FastStep__.screenType = __coffee2FastStep__.screenFullType;
                }
                else {
                   __coffee2FastStep__.screenType = "";
                }
                __coffee2FastStep__.setHeight(data.data);
            }
            else if(data.action == 'right') {
                var width = "100%";
                if(__coffee2Common__.getBrowser() == 'ie') {
                    width = "33%";
                }
                var availHeight = __coffee2Common__.getAvailHeight();
                var useHeight = __coffee2Common__.getUseHeight();
                //alert(availHeight+"|"+document.body.clientHeight+"|"+document.documentElement.clientHeight);
                if(availHeight < 800) {
                    //width = "40%";
                }
                //width = "100%";
                if(availHeight > useHeight)availHeight = useHeight;
                var div2Obj = __coffee2FastStep__.getDiv2Obj();
                div2Obj.style.float = "right";
                div2Obj.style.right = "0px";
                div2Obj.style.width = width;
                div2Obj.style.height = availHeight+"px";//"700px";//__coffee2Common__.getPageHeight() + "px";
                div2Obj.style.background = 'transparent';
                div2Obj.style.left = "";
                //__coffee2Common__.setFilter(div2Obj,100);
            }
            else if(data.action == 'requestClipper') {
                if(!__coffee2FastStep__.isJsReady()) {
		            __coffee2FastStep__.prepareJS();
		        }
                if( __coffee2ClipperPara__["contentType"] != __coffee2Constant__.contentType[2]) {
                    var html = __coffee2Common__.getSelectedHtml();
                    __coffee2Common__.log("sourceHtml:" + html);
	                if(html != null && html !='') {//htmlClip
	                    __coffee2ClipperPara__["content"] = html;
	                    __coffee2ClipperPara__["contentType"] = __coffee2Constant__.contentType[1];
	                }else{
						var isgo = __coffe2outEvent__.isGo2ServerUrl();
						if(!isgo) {
						    __coffee2ClipperPara__["startJsHtmlTime"] = new Date().getTime();
						    try{
						        var	_2coffeeD = {
										win: window,
										document: window.document
								};
						        var result = duoduoContent(_2coffeeD);
						        __coffee2ClipperPara__["title"] = result.duoduotitle;
		                	    __coffee2ClipperPara__["content"] = result.duoduocontent; 
						    }catch(e){
						    }
		                	__coffee2ClipperPara__["endJsHtmlTime"] = new Date().getTime();
						}  
	                }
                } 
                
                //product data
                try{
		            var productData = ProductData.obj.getProductData();
		            __coffee2ClipperPara__["product"] = productData;
		        }catch(e){
		        }        
                __coffee2ClipperPara__["useHeight"] = __coffee2Common__.getUseHeight();       
                __coffe2outEvent__.postIframeMessage('responseClipper',__coffee2ClipperPara__);
            }
            else if(data.action == 'close') {
                __coffee2FastStep__.cycleSlowFall(__coffee2FastStep__.fallType[1]);
            }
            else if(data.action == 'slowFall') {
                var div3Obj = __coffee2FastStep__.getDiv3Obj();
                div3Obj.parentNode.removeChild(div3Obj);
                __coffee2FastStep__.setHeight(__coffee2FastStep__.defaultMaxHeight+"px");
                //__coffee2FastStep__.getDiv1Obj.removeNode();
                //__coffee2FastStep__.cycleSlowFall(__coffee2FastStep__.fallType[0]);
            }
            else if(data.action == 'hiddenScroll') {
                //__coffee2FastStep__.hiddenScroll();
            }
            else if(data.action == 'setScrollTop') {
                __coffee2FastStep__.setScrollTop(data.data);
            }
        };
        this.bind(window,'message',pme.postMessageEvent);
    },
    postIframeMessage:function(action,data) {
        var message={"action":action};
		if(data){
			message["data"]=data;
		}
		var frame = document.getElementById(__coffee2Constant__.iframe1Id).contentWindow;
		try{
		    frame.postMessage(JSON.stringify(message),"*");
		}catch(e) {
		    __coffee2Common__.log("postIframeMessage error:" + e);
		}
    },
    isGo2ServerUrl:function() {
        var isgo = false;
        for(var i=0;i<__coffee2Constant__.go2serverUrl.length;i++) {
            var tempUrl = __coffee2Constant__.go2serverUrl[i];
            if(__coffee2ClipperPara__.busiUrl.indexOf(tempUrl)>-1) {
                isgo = true;
                break;
            }
        }
        return isgo;
    }
};

var __coffee2FastStep__= {
    defaultMixHeight:0,
    mixHeight:0,
    defaultMaxHeight:80,
    maxHeight:80,
    ieMaxHeight:80,
    stepHeight:5,
    scanTime:1,
    screenType:'',
    screenFullType:'full',
    fallType:["open","close","qkopen"],
    zIndex1:2147483647,
    zIndex2:2147483646,
    scrollTop:0,
    currentImgLength:0,
    pos:'fixed',
    init:function() {
        __coffee2ClipperPara__["startRequestTime"] = new Date().getTime();
        if(this.check2coffeeUrl()) return;
        if (this.isShowDialog())return;
        
        if(__coffee2Common__.getBrowserVersion().ie) {
            if(parseInt(__coffee2Common__.getBrowserVersion().ie) < 7) {
                this.pos = "absolute";
            }
        }

        if(this.isAutoClip()) {
            this.buildFirstDiv();
            this.buildThreeDiv();
        }
         
        //__coffee2Common__.hiddenIframeOrFlash();
        
        //init js
        if(!this.isJsReady()) {
            this.prepareJS();
        }
        
        //init imgCapture
        if(!this.isImgCaptureReady()) {
            this.prepareImgCapture();
            __coffe2outEvent__.bind(window,'scroll',function(){
	            __coffee2FastStep__.prepareImgCapture();
	        });
            __coffe2outEvent__.preparePostMessage();
        }
        
        __coffee2ClipperPara__["contentType"] = __coffee2Constant__.contentType[0];
        __coffee2ClipperPara__.busiUrl = window.location.href;
        
        if(this.isAutoClip()) {
            this.buildDiv();
        }
    },
    buildDiv:function() {
        if(__coffee2ClipperPara__["contentType"] == __coffee2Constant__.contentType[2]) {
            this.buildFirstDiv();
            this.buildThreeDiv();
        }
        this.buildSecordDiv();
    },
    buildFirstDiv:function() {
        var firstDiv = __coffee2Common__.createDiv(__coffee2Constant__.div1Id,document.body);
        firstDiv.style.textAlign = "left";
    },
    buildSecordDiv:function() {
        var firstDiv = this.getDiv1Obj();
        var height = (__coffee2Common__.getPageHeight() + 200)+ "px";
        height = "0px";
        var cssText = "#" + __coffee2Constant__.div2Id + "{";
            cssText+= "position: "+this.pos+";top: 0px;";//left: 0px;position: fixed;
            cssText+= "width: 100%;height:"+height+";background: #4C4C4C;";//#EFEFEF;height: 0px;
            cssText+= "z-index: "+this.zIndex1+";";//flow:hidden;opacity:1;
            cssText+= "}";       
        __coffee2Common__.createStyle(__coffee2Constant__.firstStyleId,cssText,firstDiv);
        
        var secordDiv = __coffee2Common__.createDiv(__coffee2Constant__.div2Id,firstDiv);  
        secordDiv.style.top = "0px";
        secordDiv.style.position = "fixed";
        secordDiv.style.width = "100%";
        secordDiv.style.zIndex = this.zIndex1;
        secordDiv.innerHTML = "<iframe src='"+__coffee2Constant__.host+"/newbookmark/private/fast_step_portal' scrolling='no' height='100%' id='"+__coffee2Constant__.iframe1Id+"'  style='width:100%;height:100%;border:0px; padding:0px; margin:0px;'>";
    },
    buildThreeDiv:function() {
        var firstDiv = this.getDiv1Obj();
        var cssText2 = "#" + __coffee2Constant__.div3Id + "{";
            cssText2+= "position: "+this.pos+";top: 0px;";//left: 0px;position: fixed;
            cssText2+= "width: 100%;height:0px;background: #4C4C4C;";//#EFEFEF;height: 0px;
            cssText2+= "z-index: "+this.zIndex1+";flow:hidden;";//flow:hidden;opacity:1;
            cssText2+= "}";
        __coffee2Common__.createStyle(__coffee2Constant__.secordStyleId,cssText2,firstDiv);
        __coffee2FastStep__.quickResponePage(firstDiv);
        this.cycleSlowFall(__coffee2FastStep__.fallType[2]);
    },
    quickResponePage:function(firstDiv) {
        var threeDiv = __coffee2Common__.createDiv(__coffee2Constant__.div3Id,firstDiv);
        var qrp = "<div  style='background: url("+__coffee2Constant__.host+"/images/plug-in-2coffee.png) no-repeat scroll 0 0 transparent; background-position: 0 -229px; background-repeat: repeat-x; border-top-color: red; height: 80px; padding: 0 10%;'>";
            qrp+=    "<div  style=' float: left; width: 25%;'>";
            qrp+=        "<img src='"+__coffee2Constant__.host+"/images/plug-inLogo.gif' alt='多多logo' style='margin-top: 10px;'>";
            qrp+=    "</div>";
            qrp+=    "<div  style=' display: table; float: left; height: 78px; overflow: hidden; text-align: center; width: 40%;'>";
            qrp+=        "<h3 style='color: #464646; display: table-cell; font-size: 22px; font-weight: 100; margin: 0; position: static; vertical-align: middle;'>";
            qrp+=            this.getQuickText();
            qrp+=        "</h3>";
            qrp+=    "</div>";
            qrp+= "</div>";
        threeDiv.innerHTML = qrp;
    },
    getQuickText:function() {
        var text = "内容正在抓取......";
        var htmlEncode = __coffee2Common__.getHtmlEncode();
        if(htmlEncode.indexOf("gb")>-1) {
            text = __coffee2Common__.str2gb2312(__coffee2Common__.str2Unicode(text));
        }
        return text;
    },
    cycleSlowFall:function(fallType) {
        if(__coffee2Common__.getBrowser() == 'ie') {
            this.maxHeight = this.ieMaxHeight;
        }
        var point = this;
        if(fallType == this.fallType[0]) {
            if(this.cycleOpenId == null) {
                this.cycleOpenId = window.setInterval("__coffee2FastStep__.downFall()",this.scanTime);
            }
        }
        else if(fallType == this.fallType[1]) {
            if(this.cycleCloseId == null) {
                this.cycleCloseId = window.setInterval("__coffee2FastStep__.upFall()",this.scanTime);
            }
        }
        else if(fallType == this.fallType[2]) {
            if(this.cycleOpenId == null) {
                this.cycleOpenId = window.setInterval("__coffee2FastStep__.qkDownFall()",this.scanTime);
            }
        }
    },
    qkDownFall:function() {
        var point = this;
        if(this.mixHeight > this.maxHeight) {
            try{
	            window.clearInterval(this.cycleOpenId);
	            this.cycleOpenId = null;
	        }catch(e){
	        }
	        this.mixHeight = this.defaultMixHeight;
            return ;
        }
        this.setDiv3Height(this.mixHeight+"px");
        this.mixHeight+=this.stepHeight;
    },
    downFall:function() {
        var point = this;
        if(this.mixHeight > this.maxHeight) {
            try{
	            window.clearInterval(this.cycleOpenId);
	            this.cycleOpenId = null;
	        }catch(e){
	        }
	        this.mixHeight = this.defaultMixHeight;
            return ;
        }
        this.setHeight(this.mixHeight+"px");
        this.mixHeight+=this.stepHeight;
    },
    upFall:function() {
        var point = this;
        if(this.maxHeight < this.mixHeight) {
            try{
	            window.clearInterval(this.cycleCloseId);
	            this.cycleCloseId = null;
	        }catch(e){
	            this.log("clearInterval error:" +e);
	        }
	        this.maxHeight = this.defaultMaxHeight;
	        this.close();
            return ;
        }
        this.setHeight(this.maxHeight+"px");
        this.maxHeight-=this.stepHeight;
    },
    prepareImgCapture:function() {
        var pic=this;
	    var zIndex = this.zIndex2;
	    var imgs = document.getElementsByTagName("img");
	    var i = __coffee2FastStep__.currentImgLength;
	    for(i=0;i<imgs.length;i++) {
	        var img = imgs[i];
	        
	        if(img.width<70 || img.height<70 || img.width*img.height<100*100){
				continue;
			}
			img.source = "source_2coffee_"+i;
			img.setAttribute("source","source_2coffee_"+i);
			//img.style.zIndex = zIndex-1;
			
			__coffe2outEvent__.bind(img,"mouseover",function(e) {
				var img = e.target||e.srcElement; 
				var source = img.getAttribute("source");
				var id = "";
				if(source) {
				    id = source.substring(source.indexOf("_")+1);
				}
				
				__coffee2FastStep__.removeAll2coffeeImg();
				
				var coffeeImg = document.getElementById(id);
				if(coffeeImg == null) {
				    coffeeImg = document.createElement('img'); 
				    coffeeImg.id = id;
					coffeeImg.style.zIndex = zIndex;
					pic.set2coffeeImg(coffeeImg,img);
					document.body.appendChild(coffeeImg);
				}
				else{
				   coffeeImg.style.display = "block";
				}
				
				__coffe2outEvent__.bind(img,"mouseout",function(e) {
				    var left = 0 ;
				    var top = 0;
				    if(e.pageX) {
				        left = e.pageX;
				        top = e.pageY;
				    }
				    else if(document.documentElement.scrollTop > 0) {
				        left = e.clientX + document.documentElement.scrollLeft - document.documentElement.clientLeft;
				        top = e.clientY + document.documentElement.scrollTop - document.documentElement.clientTop;
				    }
				    else {
				        left = e.clientX + document.body.scrollLeft - document.body.clientLeft;
				        top = e.clientY + document.body.scrollTop - document.body.clientTop;
				    }
		
				    var imgTop = pic.getOffsetTop(img);
				    var imgLeft = pic.getOffsetLeft(img);
				    
				    if(left < imgLeft || top < imgTop || left >imgLeft+img.width || top>imgTop+img.height) {
				        try{
				            document.body.removeChild(coffeeImg);
				        }catch(e){
				        } 
				    }
				});
				
				pic.clickEvent = function(e) {
				    var title = img.alt;
				    if (title == null || title == "") title = document.title;
				    
				    __coffee2ClipperPara__["contentType"] = __coffee2Constant__.contentType[2];
				    __coffee2ClipperPara__["title"] = title;
				    __coffee2ClipperPara__["busiUrl"] = __coffee2FastStep__.getImgSrc(img);
				    __coffee2ClipperPara__["imgHeight"] = img.height;
				    __coffee2ClipperPara__["isWeb"] = '';
				    
				    pic.buildDiv();
				    
				    return false;
				}
				
				__coffe2outEvent__.bind(coffeeImg,"click",pic.clickEvent);
				
			});
	    }
	    __coffee2FastStep__.currentImgLength = imgs.length;
	    if(!__coffee2FastStep__.isImgCaptureReady()) {
	        __coffee2Common__.createInput(__coffee2Constant__.imgReadyId,"hidden",document.body);
	    }
    },
    getImgSrc:function(img) {
        if(img == null) return "";
        var src = img.src;
        var objUrl = img.getAttribute("obj_url");
        if(objUrl) {
            src = objUrl;
        }        
        return src;
    },
    set2coffeeImg:function(coffeeImg,sourceImg) {
	    coffeeImg.src = this.getHost() +"/images/share.png";
		coffeeImg.border = "0";
		//coffeeImg.style.width = "95px";
		coffeeImg.style.height = "24px";
		coffeeImg.style.top = (this.getOffsetTop(sourceImg)-0) +"px";
		coffeeImg.style.left = this.getOffsetLeft(sourceImg) +"px";
		coffeeImg.style.position = "absolute";
		coffeeImg.style.cursor ="pointer";
		coffeeImg.style.padding = "0px";
		coffeeImg.style.margin = "0px";
    },
    removeAll2coffeeImg:function() {
        var imgs = document.getElementsByTagName("img");
	    for(var i=0;i<imgs.length;i++) {
	        var img = imgs[i];
	        if(img == null)continue;
	        var id = img.id;
	        if(id.indexOf("2coffee_")==0) {
	            try{
		            document.body.removeChild(img);
		        }catch(e){
		        } 
	        }
	    }
    },
    getHost:function() {
        var host = __coffee2Constant__.host;
        return host;
    },
    getOffsetTop:function(obj) {
        var mOffsetTop = obj.offsetTop;
	    var mOffsetParent = obj.offsetParent;
	    while(mOffsetParent) {
	        mOffsetTop += mOffsetParent.offsetTop;
	        mOffsetParent = mOffsetParent.offsetParent;
	    }
        return mOffsetTop;
    },
    getOffsetLeft:function(obj) {
        var mOffsetLeft = obj.offsetLeft;
	    var mOffsetParent = obj.offsetParent;
	    while(mOffsetParent) {
	        mOffsetLeft += mOffsetParent.offsetLeft;
	        mOffsetParent = mOffsetParent.offsetParent;
	    }
	    return mOffsetLeft;
    },
    go2MoreStep:function() {
        alert("go2fullstep");
    },
    isShowDialog:function() {
        return this.getDiv1Obj()?true:false;
    },
    isImgCaptureReady:function() {
        return document.getElementById(__coffee2Constant__.imgReadyId)?true:false;
    },
    isJsReady:function() {
         return document.getElementById(__coffee2Constant__.jsReadyId)?true:false;
    },
    isAutoClip:function() {
        var val = document.getElementById(__coffee2Constant__.ddAutoClipId).value;
        if(val == "true")return true;
        return false;
    },
    getDiv3Obj:function() {
        return document.getElementById(__coffee2Constant__.div3Id);
    },
    getDiv2Obj:function() {
        return document.getElementById(__coffee2Constant__.div2Id);
    },
    getDiv1Obj:function() {
        return document.getElementById(__coffee2Constant__.div1Id);
    },
    setHeight:function(height) {
        var div2 = this.getDiv2Obj();
        if(div2 == null) {
            this.log("div not found");
            return ;
        }
        if(height == null || height =="")height = this.defaultMaxHeight +"px";
        div2.style.height = height;
        //div2.style.overflow="hidden";
    },
    setDiv3Height:function(height) {
        var div3 = this.getDiv3Obj();
        if(div3 == null) {
            this.log("div3 not found");
            return ;
        }
        if(height == null || height =="")height = this.defaultMaxHeight +"px";
        div3.style.height = height;
    },
    check2coffeeUrl:function() {
        if(document.domain.indexOf(__coffee2Constant__.domainSuffix()) > -1) {
            if(this.isAutoClip()) {
                alert("对不起,不能在多多进行收集!");
            }
            window.$coffee2ok = null;
            return true;
        } 
        return false;
    },
    setScrollTop:function(scrollTop) {
        try{
	        document.documentElement.scrollTop = scrollTop;
	    }catch(e){
	        alert(e);
	    }
	    document.body.scrollTop = scrollTop; 
    },
    getScrollTop:function(){
        var scrollTop = 0; 
        scrollTop = document.documentElement.scrollTop; 
	    if(scrollTop <= 0) scrollTop = document.body.scrollTop;
	    if(scrollTop <= 0) scrollTop = window.pageYOffset; 
	    return scrollTop;
    },
    hiddenScroll:function() {
        document.body.style.overflow = "hidden";
		document.body.style.overflowX = "hidden";
		document.body.style.overflowY = "hidden";
		document.body.scroll = "no";
    },
    showScroll:function() {
        document.body.style.overflow = "auto";
		document.body.style.overflowX = "auto";
		document.body.style.overflowY = "auto";
		document.body.scroll = "yes";
    },
    close:function() {
        try{
            document.body.removeChild(this.getDiv1Obj());
        }catch(e){
           this.log("close error:" +e);
        }
        if(this.scrollTop > 0) {
            this.setScrollTop(this.scrollTop);
        }
        this.screenType = '';
        this.showScroll();
        window.$coffee2ok = null;
        //__coffee2Common__.showIframeOrFlash();
    },
    log:function(obj) {
	    __coffee2Common__.log(obj);
	},
	prepareJS:function() {
	    this.coffee2includeJs("json2", this.getHost()+'/js/json2.js',function(){
	        __coffee2Common__.createInput(__coffee2Constant__.jsReadyId,'hidden',document.body);
		    /*var ddJSReady = document.createElement('input'); 
		    ddJSReady.setAttribute('id', __coffee2Constant__.jsReadyId); 
		    ddJSReady.setAttribute('type', 'hidden' );*/
			//callback.call();
		});
	},
	coffee2includeJs:function(id, file,callback) {
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
};

__coffee2FastStep__.init();