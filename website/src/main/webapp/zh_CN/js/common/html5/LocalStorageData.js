function LocalStorageData() {
};

LocalStorageData.isSupportStorage = function() {
    var isSupport = false;
    
    if(window.localStorage) {
        isSupport = true;
    }
    if(!isSupport) {
        alert("This browser does not support localStorage");
    }
    
    return isSupport;
};

LocalStorageData.getStorage = function() {
    if(!LocalStorageData.isSupportStorage()) {
        return null;
    }
    return window.localStorage;
};

LocalStorageData.setDataByKey = function(key,value){
    if(!LocalStorageData.isSupportStorage()) {
        return ;
    }
    LocalStorageData.getStorage().setItem(key,value);
};

LocalStorageData.getDataByKey = function(key){
    if(!LocalStorageData.isSupportStorage()) {
        return ;
    }
    return LocalStorageData.getStorage().getItem(key);
};

LocalStorageData.removeDataByKey = function(key){
    if(!LocalStorageData.isSupportStorage()) {
        return ;
    }
    LocalStorageData.getStorage().removeItem(key);
};

LocalStorageData.clear = function() {
    if(!LocalStorageData.isSupportStorage()) {
        return ;
    }
    LocalStorageData.getStorage().clear(key);
};

