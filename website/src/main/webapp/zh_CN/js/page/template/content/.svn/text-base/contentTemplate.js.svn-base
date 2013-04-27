function ContentTemplate() {
    Template.call(this);
    this.contentTemplate = "contentTemplate";
    this.imageContent = "imageContent";
    this.imageTextContent = "imageTextContent";
    this.textContent = "textContent";
    this.fileContent = "fileContent";
    this.productContent = "productContent";
    this.videoContent = "videoContent";
    this.tagSourceWeibo = "Source_Weibo";
    this.fileImagePath = "/zh_CN/images1/weixin/";
};

ObjectUtil.obj.extend(ContentTemplate,Template);

ContentTemplate.prototype.getContentHtmlByContentType = function(datas) {
    datas["repositoryId"] = datas.responseId;
    datas["title"] = this.getContentTitle(datas);
    datas["imageUrl"] = this.getImageUrl(datas);
    //<img src=\""+this.getImageUrl(datas)+"\">;
    datas["summary"] = this.getSummary(datas);
    if(datas.file != null) {
        datas["fileSize"] = FileUtil.obj.getFileSize(datas.file.length);
    }
    
    var templateId = this.getTemplateIdByContentType(datas);
    return this.applyTemplate(templateId,datas);
};

ContentTemplate.prototype.getTemplateIdByContentType = function(data) {
    var templateId = this.imageContent;
    if(data == null || data.type == null) {
        return templateId;
    }
    if(data.type == "Type_Image") {
        templateId = this.imageContent;
    }
    else if(data.type == "Type_File") {
        templateId = this.fileContent;
    }
    else if(data.type == "Type_Product") {
        templateId = this.productContent;
    }
    else if(data.type == "Type_Video") {
        templateId = this.videoContent;
    }
    else if(data.web != null && data.web.attachment!=null && data.web.attachment.length>0) {
        templateId = this.imageTextContent;
    }
    else {
        templateId = this.textContent;
    }
    return templateId;
};

ContentTemplate.prototype.getContentTitle = function(data) {
    if(data == null || data.title == null)return "";
    var title = data.title;
    var tagList = data.folders;
    if(this.isContainTag(this.tagSourceWeibo,tagList)) {
        title = "来自新浪微博";
    }
    if(title == null || jQuery.trim(title).length<1) {
        title = "无标题";
    }
    return title;
};

ContentTemplate.prototype.getImageUrl = function(data) {
    if(data == null) return;
    var imageUrl = data.imageUrl;
    if(imageUrl != null && imageUrl.length >0) {
        return imageUrl;
    }
    if(data.type == "Type_File") {//文件
        imageUrl = FileUtil.obj.getFileTypeImageByPath(this.fileImagePath,data.file.extension);
    }
    else if(data.type == "Type_Image") {//图片
        if(data.image != null)imageUrl = data.image.url;
    }
    else if(data.type == "Type_Product") {//商品
        if(data.product != null)imageUrl = data.product.picture;
    }
    else if(data.type == "Type_Video") {//视频
        if(data.video!=null)imageUrl = data.video.thumbnail;
    }
    else if(data.web != null && data.web.attachment != null && data.web.attachment.length >0){//网页
        imageUrl = data.web.attachment[0].url;        
    }
    imageUrl = imageUrl.replace("file.mduoduo.com","2cofee.com");
    return imageUrl;
};

ContentTemplate.prototype.getSummary = function(data) {
    if(data == null || data.summary == null)return "";
    var summary = data.summary;
    
    return summary;
};

ContentTemplate.prototype.isContainTag = function(tag,tagList) {
    if($.trim(tag) == "") return false;
    if(tagList == null || tagList.length < 1)return false;
    for(var i=0;i < tagList.length;i++) {
       var tags = tagList[i];
       if(tags.indexOf("Delete") > -1)continue;
       if(tags.indexOf(tag) > -1) {
           return true;
       }
    }
    return false;
};

ContentTemplate.prototype.applyTemplate = function(templateId, datas) {
    if(datas == null)return "";
    var html = "";
    if(typeof datas === 'array') {
        html = this.applyTemplateByArray(templateId,datas);
    }
    else if(typeof datas == 'object') {
        html = this.applyTemplateByObject(templateId,datas);
    }
    return html;
};

ContentTemplate.prototype.applyTemplateByArray = function(templateId, datas) {
    if(datas == null) return '';
    var html = "";
    var templateHtml = this.getTemplateHtml(templateId);
    for(var i=0; i<datas.length;i++) {
        var data = datas[i];
        html += this.getHtmlFromApplyTpl(templateHtml,data);
    }
    return html;  
};

ContentTemplate.prototype.applyTemplateByObject = function(templateId, data) {
    if(data == null) return '';
    return this.getHtmlFromApplyTpl(this.getTemplateHtml(templateId),data);
};

ContentTemplate.prototype.getTemplateHtml = function(templateId) {
    return $("#"+templateId).html();
};

ContentTemplate.obj = new ContentTemplate();

