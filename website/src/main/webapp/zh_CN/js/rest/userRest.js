function UserRest() {
    Rest.call(this);
};

//ContentRest extend Rest
ObjectUtil.obj.extend(UserRest,Rest);

UserRest.prototype.IsLoginUser = function(successFn,token,errorFn) {
    var ajaxPara = 
	    {
		    url:"/account/default?r="+(Math.random()*99999999),
		    type:"get",
        };
        
    this.setHeaderToken(ajaxPara,token);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};

UserRest.obj = new UserRest();