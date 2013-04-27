function ObjectUtil() {
};

ObjectUtil.prototype.extend = function(subClass,superClass) {
    var F = function(){};
    F.prototype = superClass.prototype;
    subClass.prototype = new F();
    subClass.prototype.constructor = subClass;
    if(subClass.prototype.constructor == Object.prototype.constructor) {
        subClass.prototype.constructor = subClass;
    }
};	

ObjectUtil.obj = new ObjectUtil();