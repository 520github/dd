function FileUtil() {
    this.fileType = ["txt","pdf","doc","docx","ppt","pptx","xls","xlsx","wps","epub","zip"];
    this.filePath = ["TXT.png","PDF.png","DOC.png","DOCX.png","PPT.png","PPTX.png","XLS.png","XLSX.png","WPS.png","EPUB.png","ZIP.png"];
    this.fileImagePath = "/zh_CN/images1/weixin/";
};

FileUtil.prototype.getFileTypeImageByPath = function(path,type) {
    if(path == null || path == "") path = this.fileImagePath;
    return path + this.getFileTypeImage(type);
};

FileUtil.prototype.getFileTypeImagePath = function(type) {
    return this.fileImagePath + this.getFileTypeImage(type);
};

FileUtil.prototype.getFileTypeImage = function(type) {
    var filePath = "";
    for(var i=0; i<this.fileType.length;i++) {
       var fileType = this.fileType[i];
       if(fileType == type) {
           try{
               filePath = this.filePath[i];
           }catch(e){
           }
       }
    }
    if(filePath == "") filePath = "UNKNOWN.png";
    return filePath;
};

FileUtil.prototype.getFileSize = function(size) {
    if(size == null)size = 0;
    var level = 1024;
    var unit = "KB";
    size = Math.round(size/level);
    if(Math.floor(size/level) >0) {
        size = Math.floor(size/level);
        unit = "MB";
    }
    return size+unit;
};

FileUtil.obj = new FileUtil();

