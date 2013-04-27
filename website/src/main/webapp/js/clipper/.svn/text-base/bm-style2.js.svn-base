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
	this.perTime = 70;
	this.loaded = false;
}

DDPlugin.prototype.bind = function(obj, type, fn ) {
    if ( obj.attachEvent ) {
        obj['e'+type+fn] = fn;
        obj[type+fn] = function(){obj['e'+type+fn]( window.event );}
        obj.attachEvent( 'on'+type, obj[type+fn] );
    } else
        obj.addEventListener( type, fn, false );
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
	if (this.postMessageEvent) {
		//window.removeEventListener("message", this.postMessageEvent);
		this.unbind(window, "message", this.postMessageEvent);
	}
	
	document.body.removeChild(this.dialog);
	//jQueryDD("#ddbmDialog").dialog("destroy");
	//jQueryDD("#ddbmDialog").remove();
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
    if (typeof window.pageYOffset != 'undefined') {    
        bodyTop = window.pageYOffset;    
    } else if (typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {    
        bodyTop = document.documentElement.scrollTop;    
    }    
    else if (typeof document.body != 'undefined') {    
        bodyTop = document.body.scrollTop;    
    }    
    return bodyTop;
}

DDPlugin.prototype.prepareDialog=function(){
	var plugin=this;
	
	this.dialog = document.createElement('div'); 
	this.dialog.setAttribute('id', 'ddbmDialog'); 
	this.dialog.setAttribute('style', 'display:none;');
	document.body.appendChild(this.dialog);

	plugin.dialog.innerHTML = "<iframe src='"+plugin.getHost()+"/newbookmark/style2/clipper' id='ddClipperFrame' scrolling='no' frameborder='0' style='width:100%; height:100%; border:0px; padding:0px; margin:0px;'>";

	plugin.preparePostMessage(plugin.getSelectedHtml());
}

DDPlugin.prototype.isDialogReady=function(){
	return document.getElementById("ddDialogReady")?true:false;
}
DDPlugin.prototype.isShowDialog=function(){
	return document.getElementById("ddbmDialog")?true:false;
}

DDPlugin.prototype.getHost=function(){
    var host = "http://www.mduoduo.com";
    var script = document.getElementById("ddClipScript");
    if(script!=null && script.src!=null && script.src.indexOf("http") >-1) {
          var src = script.src;
          src = src.substring(src.indexOf("//")+2,src.length);
          src = src.substring(0,src.indexOf("/"));
          if(src!=null && src !='') {
              host = "http://" + src;
          }
      }
	return host;
}

DDPlugin.prototype.prepareJS=function(callback){
	var crawlHost=this.getHost();
	
	dd_include_js(null, crawlHost+'/js/jquery.min.js',function(){
		jQueryDD=jQuery.noConflict();

		dd_include_js("jquery-ui", crawlHost+'/js/clipper/jqueryui/js/jquery-ui-1.8.20.custom.min.js',function(){
			dd_include_js("json2", crawlHost+'/js/json2.js',function(){
				jQueryDD("body").append(jQueryDD("<input type='hidden' id='ddJSReady'/>"));
				callback.call();
			});
		});
	});
}

DDPlugin.prototype.isJSReady=function(){
	return document.getElementById("ddJSReady")?true:false;
}

function DDPlugin_ClipInfo(isSelectHtml, contentType, content, title) {
	this.isSelectHtml = isSelectHtml;
	this.contentType = contentType;
	this.content = content;
	this.title = title;
}

DDPlugin.prototype.setInitInfo=function(isSelectHtml,contentType,content, title){
	this.clipContent = new DDPlugin_ClipInfo(isSelectHtml, contentType, content, title);
	this.clipType = contentType;
}

DDPlugin.prototype.setClipInfo=function(isSelectHtml,contentType,content, title){
	var message={"url":window.location.href,"clipAction":isSelectHtml?"clipSelection":"clipArchived","contentType":contentType,"title":title};
	
	if(content){
		message["content"]=content;
	}
	
	this.postMessage("setClipInfo", message);
}

DDPlugin.prototype.open=function(){
	//jQueryDD("#ddbmDialog").dialog('open');
	var topScroll=this.getScroll();
	jQueryDD(this.dialog).attr("style", "overflow: hidden;display: block; z-index: 2147483647; outline: 0px none; height: 640px; width: 700px; top: 0px; right:0px; position: absolute;");
	this.loaded = true;
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
		else if(data.action='report'){
			plugin.dialogRole=data.data;
			
			if(plugin.dialogRole=='clipper'){
				jQueryDD(plugin.dialog).dialog("option","height",640);
				jQueryDD(plugin.dialog).dialog("option","width",800);
				
				if(selection!="" && plugin.dialogRole=='clipper'){
					plugin.setClipInfo(true,"HtmlClip",selection, document.title);
				}else if (plugin.clipType != null){
					plugin.setClipInfo(plugin.clipContent.isSelectHtml, plugin.clipContent.contentType, plugin.clipContent.content, plugin.clipContent.title);
				}
				else {
					plugin.setClipInfo(false,"Web");
				}
			}else{
				jQueryDD(plugin.dialog).dialog("option","height",490);
				jQueryDD(plugin.dialog).dialog("option","width",620);
			}
			
			jQueryDD(plugin.dialog).dialog("option","position",['right',10]);
			
		}else if(data.action=='getUrl'){
			plugin.postMessage("setUrl", window.location.href);
		}
	};
	plugin.bind(window, 'message', plugin.postMessageEvent);
}

function showDDCLPlugin(init){
	if (ddPlugin.isShowDialog())
		return;
	var autoClip=jQueryDD("#ddAutoClip").val();
	if(autoClip!='false'){
		ddPlugin.prepareDialog(init);
	}
	
	jQueryDD(".ui-dialog-titlebar").hide();
	if(autoClip!='false'){
		ddPlugin.open();
	}
}

var ddPlugin=new DDPlugin();
if(!ddPlugin.isJSReady()){
	ddPlugin.prepareJS(function(){
		showDDCLPlugin();
	});
}else{
	showDDCLPlugin();
}