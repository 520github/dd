function DragMonitor(){
    
};

DragMonitor.para = {
    imgDefaultWidth:240,
    imgDefaultHeight:182,
    previewImgDivId:"#dragActionId .drag-img-1",
    previewImgId:"#dragActionId .drag-img-1 img",
    imgTitle:"#dragActionId .img-title input",
    fileSizeId: "#dragActionId .drag-img .rag-mask-bottom",
    uploadFileButton:"#uploadFile",
    dragActionId:"#dragActionId",
    monitorTempId:"#monitorTemp",
    previewTempId:"#previewTemp",
    successTempId:"#successTemp",//not used
    customObjectTempId:"#customObjectTemp",
    editContactTempId:"#editContactTemp",
    setUpTempId:"#setUpTemp",
    eventObj:[document,"#dragActionId"],//
    eventAction:["dragenter","dragover","dragleave","drop"],//not used
    gIsLogin:"#gIsLogin",
    uploadInput:"#uploadFile1",
    errorTempId:"#errorTemp",
    errorMessageId:"#error_msg",
    commentId:"#commentId",
    defaultComment:"我觉得这个东东对你有帮助，希望你喜欢哦!",
    autoCloseTime:3000,
    titleMaxLenght:30,
    minUseHeight:520,
    minUseHeightCss:"ie-height-size",
    push2CustomDivId:"#customeId",
    fileType:["txt","pdf","doc","docx","ppt","pptx","xls","xlsx","wps","epub","zip"],
    filePath:["TXT.png","PDF.png","DOC.png","DOCX.png","PPT.png","PPTX.png","XLS.png","XLSX.png","WPS.png","EPUB.png","ZIP.png"],
};


DragMonitor.showCurrentDrag = function(showDivId) {
    if(showDivId == DragMonitor.para.monitorTempId) {
        var html = $(DragMonitor.para.dragActionId).html();
        if($.trim(html).length > 0) {
            $(DragMonitor.para.dragActionId+">div").show();
            return;
        }
    }
    $(DragMonitor.para.dragActionId).empty();
    var html = $(showDivId).html();
    $(DragMonitor.para.dragActionId).html(html);
};

DragMonitor.hiddenDragUpload = function() {
    $(DragMonitor.para.dragActionId).empty();
    $(DragMonitor.para.uploadInput).attr("value","");
};

DragMonitor.showError = function(showDivId,errorId,msg) {
	$(DragMonitor.para.dragActionId).empty();
	$(DragMonitor.para.errorMessageId).empty();
    var html = $(errorId).html();
    $(DragMonitor.para.errorMessageId).html(html);
	$(DragMonitor.para.dragActionId).html($(showDivId).html());
	$(DragMonitor.para.uploadInput).attr("value","");
    window.setTimeout("DragMonitor.autoCloseError()",DragMonitor.para.autoCloseTime);
};

DragMonitor.autoCloseError = function() {
    $(DragMonitor.para.dragActionId).empty();
};
DragMonitor.resetImg = function(img,file) {
	var dragObj = new DragUpload();
	
	if (jQuery.browser.msie && dragObj.isImage(file.value)) {
		
		if(document.all)//IE
  		{	
  			var imgFile= file;
  			imgFile.select();
  			imgFile.blur();
  			path = document.selection.createRange().text;
  			
  			$(DragMonitor.para.previewImgDivId).empty();
  			
  			document.getElementById("image_id").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + path + "\");width=\""+DragMonitor.para.imgDefaultWidth+"\";height=\""+DragMonitor.para.imgDefaultHeight+"\";";//使用滤镜效果www.2cto.com  
  		}
	} else {
	    if(img == null)return;
	    var oldWidth = img.width,
	        oldHeight = img.height,
	        percentage = 1,
	        defaultWidth = DragMonitor.para.imgDefaultWidth,
	        defaultHeight = DragMonitor.para.imgDefaultHeight;
	    
	    if(oldWidth <= defaultWidth && oldHeight <=defaultHeight) {
	        return;
	    }
	    else if(oldHeight > defaultHeight) {
	        percentage = oldHeight/defaultHeight;
	        if(oldWidth/percentage > defaultWidth) {
	            percentage = oldWidth/defaultWidth;
	            img.width = defaultWidth;
	            img.height = oldHeight/percentage;
	        }
	        else {
	            img.width = oldWidth/percentage;
	            img.height = defaultHeight;
	        }
	    }
	    else if(oldWidth > defaultWidth) {
	        percentage = oldWidth/defaultWidth;
	        if(oldHeight/percentage > defaultHeight) {
	            percentage = oldHeight/percentage;
	            img.width = oldWidth/percentage;
	            img.height = defaultHeight;
	        }
	        else {
	            img.width = defaultWidth;
	            img.height = oldHeight/percentage;
	        } 
	    }
    }
};
DragMonitor.checkFile = function(file) {
	var limitation = _dragMt.getLimitation();
	if (!limitation) {
		return true;
	}
    if (limitation.dailyQuota <= limitation.dailyUpload) {
		DragMonitor.showError(DragMonitor.para.errorTempId,"#dailyQuato","超过每日上传限额，每个用户每天最多上传"+limitation.dailyQuota+"个文件！");
		return false;
	}
	var extension;
	if(!window.FileReader) { 
	
        if(file.value.lastIndexOf(".") ==-1) {
            DragMonitor.showError(DragMonitor.para.errorTempId,"#invalid","非法文件类型");
			return false;
        }
		extension = file.value.substring(file.value.lastIndexOf(".") + 1);
    } 
    else {
    	if (limitation.maxFileLength < file.size) {
			DragMonitor.showError(DragMonitor.para.errorTempId,"#length","最多只能传"+limitation.maxFileLength+"兆大小的文件！");
			return false;
		}
		
		if(file.name.lastIndexOf(".") ==-1) {
            DragMonitor.showError(DragMonitor.para.errorTempId,"#invalid","非法文件类型");
			return false;
        }
		extension = file.name.substring(file.name.lastIndexOf(".") + 1);
    }
    extension = extension.toLowerCase();
	
	if (limitation.whitelist) {
		var flag = false;
		for (var ext in limitation.whitelist) {
			if (limitation.whitelist[ext] == extension) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			DragMonitor.showError(DragMonitor.para.errorTempId,"#invalid","非法文件类型");
			return false;
		}
    }
    return true;
};

DragMonitor.previewFile = function(file) {
	if (!DragMonitor.checkFile(file)) {
		return;
	}
	
	_settings.getSharingGroup();
    
    DragMonitor.showCurrentDrag(DragMonitor.para.previewTempId);
    DragMonitor.resetDragHeight();
    DragMonitor.bindEventAction();
    
    if(!window.FileReader) { 
        DragMonitor.setTitle(file.value);
        var imgUrl = DragMonitor.getFileTypeImg(file.value);
        
        DragMonitor.previewImg(imgUrl,file);
        return;
    } 
    else {
	    DragMonitor.setTitle(file);
	    DragMonitor.setFileSize(file);
	    
	    if(file.type.indexOf("image") ==-1) {
	        //alert("非图片预览");
	        var imgUrl = DragMonitor.getFileTypeImg(file);
            DragMonitor.previewImg(imgUrl);
	        return;
	    }
	    var reader = new FileReader();
	    reader.onprogress = function(e) {//文件读取进度
	        var percentage = Math.round((e.loaded*100)/e.total);
	    };
	    reader.onloadend = function(e) {//文件读取结束
	        DragMonitor.previewImg(this.result);
	    }
	    reader.readAsDataURL(file);
    }
};

DragMonitor.previewImg = function(imgUrl, file) {
    
    DragMonitor.getPreviewImg().load(function() {
        DragMonitor.resetImg(DragMonitor.getPreviewImg()[0], file);
    });
    DragMonitor.getPreviewImg().attr("src",imgUrl);
    
};
DragMonitor.isLogin = function() {
    var isLogin = $(DragMonitor.para.gIsLogin).val();
    if(isLogin == "yes") {
        return true;
    }
    return true;
};

DragMonitor.getFileTypeImg = function(file) {
   var imgUrl = systemConstant.fileTypeImgPath;
   var type = file.type;
   //alert("fileType:" +type);
   if(file.name) {
       type = file.name;
   } 
   else if(file) {
       type = file;
   }
   if(type==null)type="";
   if(type.indexOf(".")>-1)type=type.substring(type.lastIndexOf(".")+1);
   type = type.toLowerCase();
   var filePath = "";
   for(var i=0; i<DragMonitor.para.fileType.length;i++) {
       var fileType = DragMonitor.para.fileType[i];
       if(fileType == type) {
           try{
               filePath = DragMonitor.para.filePath[i];
           }catch(e){
           }
       }
   }
   if(filePath == "") filePath = "UNKNOWN.png";
   imgUrl+= filePath;
   return imgUrl;
};

DragMonitor.setTitle = function(file) {
    var title = "文件名";
    if(file.name) {
        title = file.name;
    }
    else if(file) {
        title = file;
        if(title.lastIndexOf("\\") >-1)title = title.substring(title.lastIndexOf("\\")+1);
        //alert(title.lastIndexOf("\\"));
    }
    if(title.lastIndexOf(".") >-1) title = title.substring(0,title.lastIndexOf("."));
    
    if(title.length > DragMonitor.para.titleMaxLenght) {
        title = title.substring(0,DragMonitor.para.titleMaxLenght);
    }
    $(DragMonitor.para.imgTitle).val(title);
};

DragMonitor.setFileSize = function(file) {
    var fileSize = 0;
    if(file) {
        fileSize = Math.round(file.size/1024);
    }
    $(DragMonitor.para.fileSizeId).html(fileSize+"kb");
};


DragMonitor.getPreviewImg = function() {
	
	if ($(DragMonitor.para.previewImgId)) {
		return $(DragMonitor.para.previewImgId);
	}
    else {
    	$(DragMonitor.para.previewImgDivId).html("<img src=\"\" alt=\"\">");
    	return $(DragMonitor.para.previewImgId);
    }
};

DragMonitor.bindEventAction =function() {
    $(DragMonitor.para.dragActionId+" "+DragMonitor.para.commentId).focus(function() {
        var value = $.trim($(this).val());
        if(value == DragMonitor.para.defaultComment) {
            $(this).val("");
        }
    });
    $(DragMonitor.para.imgTitle).keyup(function() {
        var maxLength = 30;
		if ($(DragMonitor.para.imgTitle).val().length >= maxLength) {
			$(DragMonitor.para.imgTitle).val($(DragMonitor.para.imgTitle).val().substr(0, maxLength));
		}
	});
};

DragMonitor.resetDragHeight = function() {
    var useHeight = DragMonitor.getUseHeight();
    if(DragMonitor.para.minUseHeight > useHeight) {
       var obj = $(DragMonitor.para.dragActionId + ">div>div");
       var css = obj.attr("class");
       obj.attr("class",css+" " +DragMonitor.para.minUseHeightCss);
       
       var obj1 = $(DragMonitor.para.customObjectTempId + ">div>div");
       var css = obj1.attr("class");
       obj1.attr("class",css+" " +DragMonitor.para.minUseHeightCss);
       
       var obj2 = $(DragMonitor.para.editContactTempId + ">div>div");
       var css = obj2.attr("class");
       obj2.attr("class",css+" " +DragMonitor.para.minUseHeightCss);
       
       var obj3 = $(DragMonitor.para.setUpTempId + ">div>div");
       var css = obj3.attr("class");
       obj3.attr("class",css+" " +DragMonitor.para.minUseHeightCss);
    }
};

DragMonitor.getUseHeight = function() {
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
    var availHeight =  window.screen.availHeight;
    if(useHeight > availHeight) {
         useHeight = availHeight;
    }
    return useHeight;
};

DragMonitor.isGuestUser = function() {
    var userRole = jQuery("input[name='userRole']").val();
    if(userRole == "Guest") {
        return true;
    }	
    return false;
};

DragMonitor.prototype = {
    init:function() {
        var version = jQuery.browser.version;
        if(jQuery.browser.msie) {//ie
            //alert("ie is't support drag&drop");
            //return;
        }
        else if(jQuery.browser.mozilla) {//firefox
            this.regDragEventListener();
        }
        else if(jQuery.browser.opera) {//opera
            alert("now opera is not known support drag&drop");
            return;
        }
        else if(jQuery.browser.safari) {//safari or chrome
            this.regDragEventListener();
        }
        this.onChangeFileInput();
    },
    onChangeFileInput:function() {
        if($(DragMonitor.para.uploadInput).length <1)return;
        $(DragMonitor.para.uploadInput).unbind();
        $(DragMonitor.para.uploadInput).change(function() {
        	if(DragMonitor.isGuestUser()) {
        	    showLayer("#showLoginOrRegPage");
        	    return;
        	}
			var file;
			if (!window.FileReader) {
            	file=this;
            	DragMonitor.previewFile(file);
            } else {
            	file= this.files[0];
            	DragMonitor.previewFile(file);
            }
        	$(DragMonitor.para.uploadFileButton).click(function() {
            	var _dragUpload = new DragUpload();
                
            	if (!window.FileReader) {
            		_dragUpload.uploadFileType = "iframe";
            		file = DragMonitor.para.uploadInput;
            	}
		    	_dragUpload.uploadFile(file);
			});
        });
    },
    onDragStart:function(e) {
        var dataTransfer;
        try{
            dataTransfer = e.dataTransfer;
        }catch(e){
            dataTransfer = e.originalEvent.dataTransfer;
        }
        dataTransfer.effectAllowed = "copy";
    },
    onDragEnter:function(e) {
        e.preventDefault();
        e.stopPropagation();
        if(DragMonitor.isGuestUser()) {
            //showLayer("#showLoginOrRegPage");
            return;
        }
        var dataTransfer = e.dataTransfer;
        if(dataTransfer == null) dataTransfer = e.originalEvent.dataTransfer;    
        
        dataTransfer.dropEffect = 'copy';
        DragMonitor.showCurrentDrag(DragMonitor.para.monitorTempId);
    },
    onDragOver:function(e) {
        e.preventDefault();
        e.stopPropagation();
        var dataTransfer = e.dataTransfer;
        if(dataTransfer == null) dataTransfer = e.originalEvent.dataTransfer;
        dataTransfer.dropEffect = 'copy';
    },
    onDragLeave:function(e) {
        e.preventDefault();
        e.stopPropagation();
        
        var dataTransfer = e.dataTransfer;
        if(dataTransfer == null) dataTransfer = e.originalEvent.dataTransfer;
        dataTransfer.dropEffect = 'copy';
        dataTransfer.effectAllowed = 'copy';
        
        if(e.clientX <= 0 || e.clientY <= 0) {
            $(DragMonitor.para.dragActionId).empty();
        }
    },
    onDrop:function(e) {
        e.preventDefault();
        e.stopPropagation();
        if(DragMonitor.isGuestUser()) {
            showLayer("#showLoginOrRegPage");
            return;
        }
        var limitSize = 1;
        
        var dataTransfer = e.dataTransfer;
        if(dataTransfer == null) dataTransfer = e.originalEvent.dataTransfer;
        var files = dataTransfer.files;
        if(files.length <1) {
            $(DragMonitor.para.dragActionId).empty();
            return;
        }
        
        if(files.length > limitSize) {
            //alert("一次只能拖拉"+limitSize+"个文件！");
            DragMonitor.showError(DragMonitor.para.errorTempId,"#duplication","一次只能拖1个文件");
            return;
        }
        for(var i=0;i<files.length;i++) {
            var file = files[i];
            DragMonitor.previewFile(file);
            $(DragMonitor.para.dragActionId + " " +DragMonitor.para.uploadFileButton).click(function() {
                var _dragUpload = new DragUpload();
			    _dragUpload.uploadFile(file);
			});
        }
    },
    
    regDragEventListener:function() {
        for(var i=0;i<DragMonitor.para.eventObj.length;i++) {
            var obj = DragMonitor.para.eventObj[i];
            if(obj == null) {
                continue;
            }
            if(typeof obj === "string") {
                obj = $(obj);
                if(obj==null)continue;
                eventUtil.bind(obj,"dragover",this.onDragOver);
			    eventUtil.bind(obj,"drop",this.onDrop);
            }
            else {
                eventUtil.bind(obj,"dragenter",this.onDragEnter);
	            eventUtil.bind(obj,"dragleave",this.onDragLeave);
			    eventUtil.bind(obj,"dragover",this.onDragOver);
			    eventUtil.bind(obj,"drop",this.onDrop);
            }
        }
        
    },
    getLimitation : function(){
    	var limitation;
		$.ajax({
			url : "/service/setting/fileupload/limitation",
			headers:cookieUtil.getHeaderToken(),	
			type : "get",
			cache : false,
			async : false,
			success : function(jsonObj) {
				limitation = jsonObj;
			}
		});
		return limitation;
	}
}

var _dragMt = new DragMonitor();
/*
window.addEventListener("load",function(e){
    _dragMt.init();
},false);
*/
