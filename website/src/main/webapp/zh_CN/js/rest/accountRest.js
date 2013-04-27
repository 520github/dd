function AccountRest() {
};

//AccountRest extend Rest
ObjectUtil.obj.extend(AccountRest,Rest);

AccountRest.prototype.getProfile = function(successFn,errorFn) {
    var ajaxPara = {
        url:"/service/accounts/profile?r="+(Math.random()*99999999),
        type:"get"
    };
    this.setHeaderToken(ajaxPara);
    this.setCallbackFn(ajaxPara,successFn,errorFn);
    
    jQuery.ajax(ajaxPara);
};

AccountRest.obj = new AccountRest();