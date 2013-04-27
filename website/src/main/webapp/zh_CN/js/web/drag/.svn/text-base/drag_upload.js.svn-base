function DragUpload() {
};

DragUpload.para = {
    uploadFileButton:DragMonitor.para.uploadFileButton,
    uploadInput:DragMonitor.para.uploadInput,
    dragActionId:DragMonitor.para.dragActionId,
    uploadStatus: "#dragActionId .drag-img .rag-mask-top",
    imgTitle:DragMonitor.para.imgTitle,
    homeUrl:"/home",
    successTemp:"#successTemp",
    autoCloseTime:3000,
    lookButton:"#durian-look-btn",
    fileServiceHost:"http://www.mduoduo.com",
    imageType:["gif","jpg","png","jpeg","bmp"],
    titleMaxLenght:DragMonitor.para.titleMaxLenght,
    commonValue:"#commonValue",
    successMe:"#successMe",
    successMeAndFriend:"#successMeAndFriend",
    defaultTag:"Later"
};

DragUpload.getFileServiceUrl = function(data) {
	if (data) {
		if (data.indexOf("/") == 0) {
			return DragUpload.para.fileServiceHost + data;
		}
		return DragUpload.para.fileServiceHost + "/" + data;
	}
	else {
		return "";
	}
};

DragUpload.successCallback = function(data) {
    var code = data.code;
    $(DragUpload.para.uploadInput).attr("value","");
    if(code ==200) {
        var commonValue = $(DragUpload.para.dragActionId+" " + DragUpload.para.commonValue).attr("value");
        $(DragUpload.para.uploadStatus).html("上传100%");
		$(DragUpload.para.dragActionId).empty();
		$(DragUpload.para.dragActionId).html($(DragUpload.para.successTemp).html());
		if(commonValue == "Me") {
		    $(DragUpload.para.dragActionId + " " +DragUpload.para.successMe).show();
		}
		else {
		    $(DragUpload.para.dragActionId + " " +DragUpload.para.successMeAndFriend).show();
		}
		
        if(window.location.href.indexOf(DragUpload.para.homeUrl)>-1) {
		    $(DragUpload.para.dragActionId + " " +DragUpload.para.lookButton).hide();
            window.setTimeout("window.location.reload()",DragUpload.para.autoCloseTime);
        }
        else {           
		    window.setTimeout("DragUpload.autoClose()",DragUpload.para.autoCloseTime);
        }
    }
    else {
        //TODO show error page
        DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
        //alert("操作失败");
    }
};


DragUpload.hiddenDragUpload = function() {
    DragMonitor.hiddenDragUpload();
};

DragUpload.goLook = function() {
    DragUpload.hiddenDragUpload();
    window.open(DragUpload.para.homeUrl,"blank");
};

DragUpload.showError = function(showDivId,errorId,msg) {
    DragMonitor.showError(showDivId,errorId,msg);
};

DragUpload.autoClose = function() {
    $(DragUpload.para.dragActionId).empty();
};

DragUpload.prototype = {
    uploadFileType:"request",
    uploadUrl:DragUpload.getFileServiceUrl("/service/content/file"),
    checkPushType:function(){
        var check = true;
        var pushType = this.getPushType();
        //common
        if(pushType == previewConstant.pushType[0]){
            //alert($(DragUpload.para.dragActionId +" "+previewConstant.push2CommonId).val());
        }
        //custom
        else if(pushType == previewConstant.pushType[1]){
            var customVal = $(previewConstant.push2CustomId).val();
            var checked = $(customConstant.customObjectTemp + " " +customConstant.myDivId).find("input").attr("checked");
            if($.trim(customVal).length < 1 && !checked) {
                alert("未选择自定义用户");
                check = false;
            }
        }
        return check;
    },
    getPushType:function() {
        var pushType = previewConstant.pushType[0];
        var css = $(DragUpload.para.dragActionId + " " +previewConstant.push2CommonDivId).attr("class");
        if(css.indexOf(previewConstant.isPushFlag)>-1) {
            pushType = previewConstant.pushType[0];
        }
        css = $(DragUpload.para.dragActionId + " " +previewConstant.push2CustomDivId).attr("class");
        if(css.indexOf(previewConstant.isPushFlag)>-1) {
            pushType = previewConstant.pushType[1];
        }
        return pushType;
    }, 
    getToFriend:function() {
        var toFriend = "";
        var pushType = this.getPushType();
        if(pushType == previewConstant.pushType[0]) {//common
            toFriend = $(DragUpload.para.dragActionId + " " +previewConstant.push2CommonId).val();
        }
        else if(pushType == previewConstant.pushType[1]) {//custom
            toFriend = $(DragUpload.para.dragActionId + " " +previewConstant.push2CustomId).val();
        }
        return toFriend;
    },
    getTag:function() {
        var tag = "";
        var pushType = this.getPushType();
        if(pushType == previewConstant.pushType[0]) {//common
            tag = DragUpload.para.defaultTag;
        }
        else if(pushType == previewConstant.pushType[1]) {//custom
            var checked = $(customConstant.customObjectTemp + " " +customConstant.myDivId).find("input").attr("checked");
            if(checked)tag = DragUpload.para.defaultTag;
        }
        return tag;
    },
    uploadFile:function(file) {
        if(!this.checkPushType())return ;;
        //alert(this.uploadFileType);
        
        var css = $(DragUpload.para.dragActionId + " " +DragUpload.para.uploadFileButton).attr("class");
        if(css.indexOf("durian-cancel-btn-1") >-1) {
            return ;
        }
        $(DragUpload.para.dragActionId + " " +DragUpload.para.uploadFileButton).attr("class",css+" durian-cancel-btn-1");
        $(DragUpload.para.dragActionId + " " +DragUpload.para.uploadFileButton).attr("disabled","true");
                
        if(this.uploadFileType =="ajax") {
            this.ajaxUploadFile(file);
        }
        else if (this.uploadFileType =="iframe") {
        	this.iframeUploadFile(file);
        }
        else {
            //alert("request");
            this.httpRequestUploadFile(file);
        }
    },
    ajaxUploadFile:function(file) {
        var data = this.getData(file);
        jQuery.ajax({
            url:this.uploadUrl,
            type:"post",
            dataType:"json",
            data:data,
            contentType:false,
            processData:false,
            xhr:this.xhr_provider,
            success:function(resp) {
                share2friends(resp,file);
            },
            error:function() {
                alert("ajax upload file error");
            }
        });
    },
    httpRequestUploadFile:function(file) {
        var _upload = this;
        var xhr = new XMLHttpRequest();
        xhr.open("POST",this.uploadUrl,true);
        //xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Authorization",cookieUtil.getWebToken());
        var data = this.getData(file);
        this.addUploadEvent(xhr);
        xhr.onreadystatechange = function(x) {
            if(xhr.readyState ==4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                _upload.share2friends(responseData,file);
                //alert(responseData.id);
            }
            else if(xhr.readyState ==4 && xhr.status == 500) {
                DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
            } 
            else if(xhr.readyState ==4 && xhr.status == 400) {
                DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
            } 
            else if(xhr.readyState ==4 && xhr.status == 403) {
                DragMonitor.showError(DragMonitor.para.errorTempId,"#login","请重新登录！");
            } 
            else if(xhr.readyState ==4 && xhr.status == 409) {
                DragMonitor.showError(DragMonitor.para.errorTempId,"#invalid","非法文件类型！");
            } 
            else if(xhr.readyState ==4 && xhr.status == 413) {
            	var responseData = JSON.parse(xhr.responseText);
                DragMonitor.showError(DragMonitor.para.errorTempId,"#length","上传文件大小超过限制！最大支持"+responseData.maxFileLength/(1024*1024)+"兆");
            } 
            else if(xhr.readyState ==4 && xhr.status == 423) {
            	var responseData = JSON.parse(xhr.responseText);
                DragMonitor.showError(DragMonitor.para.errorTempId,"#quato","上传次数超过每日限额！每日限额为"+responseData.dailyQuota+"次");
            }
            else {
                //DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
            }
        }
        xhr.send(data);
    },
    iframeUploadFile:function(id) {
    	var _upload = this;
        var value;
        if ($(id).val().lastIndexOf("\\") > 0) {
        	value = $(id).val().substring($(id).val().lastIndexOf("\\") + 1);
        }
        
        var contentType = "File"; 
        if(this.isImage(value)) {
            contentType = "File_Image";
        }
        
        var header = cookieUtil.getHeaderToken();
        var fileId;
        if (id.indexOf('#') == 0) {
        	fileId=id.substring(1);
        }
    	var formData= {
    		"name":'fileName',
    		"value":encodeURIComponent(value)
    	};
    	$(DragUpload.para.dragActionId + " .drag-mask").empty();
    	$(DragUpload.para.dragActionId + " .drag-mask").html("<span class=\"drag-lodg-gif\"><img src=\"/zh_CN/images1/drag/load-gif.gif\"></span>");
        $.ajaxFileUpload({
    		url:this.uploadUrl, 
    		secureuri:false,
    		headers:header,
    		data:formData,
    		fileElementId:fileId,
    		dataType: 'json',
    		success: function (resp, status){
    		_dragMt.onChangeFileInput();
    		if (resp.result) {
    			//alert("result " + resp.result);
    			DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
    		}
    		
    		if (resp.fileId) {
    			//alert("fileId " + resp.fileId);
    			resp.contentType = contentType;
    			_upload.share2friends(resp);
    		}

			if (resp.dailyQuota) {
				//alert("dailyQuota " + resp.dailyQuota);
				DragMonitor.showError(DragMonitor.para.errorTempId,"#quato","上传次数超过每日限额！每日限额为"+resp.dailyQuota+"次");
			}
			
			if (resp.whitelist) {
				//alert(resp.whitelist);
				DragMonitor.showError(DragMonitor.para.errorTempId,"#invalid","非法文件类型！");
			}
    		
    		if (resp.maxFileLength) {
    			//alert(resp.maxFileLength);
    			DragMonitor.showError(DragMonitor.para.errorTempId,"#length","上传文件大小超过限制！最大支持"+resp.maxFileLength/(1024*1024)+"兆");
    		}
    	   },
    	   error: function (resp, e){
    	   	   //alert("error");
    	   	   _dragMt.onChangeFileInput();
    	   	   DragMonitor.showError(DragMonitor.para.errorTempId,"#serverError","上传失败！");
    	   }
	    });
    },
    getData:function(file) {
        var boundary = '———————–' + new Date().getTime();
        var data;
        var filename = file.name;
        filename = this.getFileName(file);
        if(window.FormData) {
            var formData = new FormData();
            formData.append("uploadedFile",file);
            formData.append("fileName",filename);
            data = formData;
            //alert("formdata");
        }
        else if(file.getAsBinary) {
            data = "-" + boundary + "";
        }
        return data;
    },
    xhr_provider:function() {
        var xhr = jQuery.ajaxSettings.xhr();
        this.addUploadEvent(xhr);
        return xhr;
    },
    addUploadEvent:function(xhr) {
        if(xhr.upload) {
            xhr.upload.addEventListener('progress',this.onprogress,false);
            xhr.upload.addEventListener('load',this.onsucess,false);
            xhr.upload.addEventListener('error',this.onerror,false);
        }
    },
    onprogress:function(e) {
        var percentage = Math.round((e.loaded*100)/e.total);
        if(percentage >=100)percentage ="99";
		$(DragUpload.para.uploadStatus).html("上传"+percentage+"%");
    },
    onsucess:function(e) {
        $(DragUpload.para.uploadStatus).html("上传99%");
    },
    onerror:function(e) {
        $(DragUpload.para.uploadStatus).html("上传99%");
    },
    share2friends:function(resp,file) {
        var data = this.getResporityData(resp, file);
        this.getJSON("/newbookmark/private/store", data,DragUpload.successCallback);
    },
    getResporityData:function(resp,file) {
        var fileId = resp.fileId;
        var fileUrl = resp.url==null?"":resp.url;;
        var contentId = resp.contentId==null?"":resp.contentId;
        
        var contentType = this.getContentType(file);
        if(resp.contentType) {
            contentType = resp.contentType;
        }
        var title = this.getFileTitle(file);
        var fileSize = this.getFileSize(file);
        var fileType = this.getFileType(file);
        var tag = this.getTag();
        var toFriends = this.getToFriend();
        var comment = this.getComment();
        
        var data = {
            "contentType":contentType,
            "url":fileUrl,
            "title":title,
            "tag":tag,
            "toFriends":toFriends,
            "comment" : comment,
			"score" : '',
			"id":contentId,
			"fileId":fileId,
			"fileName":title,
			"fileType":fileType,
			"fileSize":fileSize,
			"fileUrl":fileUrl
       };
       return data;
    },
    isImage:function(fileName) {
       if(fileName == null)return false;
       var isImage = false;
       if(fileName.indexOf(".")>-1)fileName = fileName.substring(fileName.lastIndexOf(".")+1);
       fileName = fileName.toLowerCase();
       for(var i =0;i<DragUpload.para.imageType.length;i++) {
           var type = DragUpload.para.imageType[i];
           if(type == fileName) {
               isImage = true;
               break;
           }
       }
       return isImage;
    },
    getComment:function() {
        var comment = $(DragUpload.para.dragActionId + " " +previewConstant.commentId).val();
        if($.trim(comment).length < 1) {
            comment = "我觉得这个东东对你有帮助，希望你喜欢哦!";
        }
        return comment;
    },
    getContentType:function(file) {
        var type = "File";
        if(file == null)return type;
        if(file.type.indexOf("image") >-1) {
            type = "File_Image";//Image
        }
        else {
            type = "File";
        }
        //alert("contentType:"+ type);
        return type;
    },
    getFileName:function(file) {
        var fileName;
        fileName = $(DragUpload.para.imgTitle).val();
        if(file == null) return fileName;
        if($.trim(fileName).length < 1) {
            fileName = file.name;
            if(fileName.length > DragUpload.para.titleMaxLenght){
                fileName = fileName.substring(0,DragUpload.para.titleMaxLenght);
            }
        }
        fileName = fileName + file.name.substring(file.name.lastIndexOf("."));
        return encodeURIComponent(fileName);
    },
    getFileTitle:function(file) {
        var title = $(DragUpload.para.imgTitle).val();
        if($.trim(title).length < 1) {
            if(file) {
                title = file.name;
            }
        }
        if(title == null || title =="")title = "未知标题";
        return title;
    },
    getFileSize:function(file) {
       var fileSize = 0;
       if(file) {
           fileSize = file.size;
       }
       return fileSize;
    },
    getFileType:function(file) {
       var fileType = "unknown";
       if(file) {
           fileType = file.type;
       }
       return fileType;
    },
    getJSON:function(url, data, callback) {
        jQuery.ajax({
			url : url,
			dataType : 'json',
			type : 'POST',
			cache : false,
			async : false,
			data : data,
			success : callback
		});
    }
}