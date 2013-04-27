function StringUtil() {
    this.pointSign = ".";
};

StringUtil.prototype.getSectionValue = function(str,valNum,signNum) {
    if(str == null)return null;
    if(valNum >= str.length)return str;
    str = str.substring(0,valNum);
    str+=this.getSign(this.pointSign,signNum);
    return str;
};

StringUtil.prototype.getSign = function(sign,signNum) {
    if(sign == null) return '';
    if(signNum < 1)return '';
    var signVal = '';
    for(var i=0;i<signNum;i++) {
        signVal+=sign;
    }
    return signVal;
};

StringUtil.obj = new StringUtil();