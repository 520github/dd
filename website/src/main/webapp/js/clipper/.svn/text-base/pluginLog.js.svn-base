function PluginLog() {
    Log.call(this);
    this.startRequestTime = "startRequestTime";
    this.endRequestTime = "endRequestTime";
    this.startJsHtmlTime = "startJsHtmlTime";
    this.endJsHtmlTime = "endJsHtmlTime";
    this.data = {};
};

ObjectUtil.obj.extend(PluginLog,Log);

PluginLog.prototype.setStartRequestTime = function(startDate) {
    this.data[this.startRequestTime] = startDate;
};

PluginLog.prototype.setEndRequestTime = function(endDate) {
    this.data[this.endRequestTime] = endDate;
};

PluginLog.prototype.setStartJsHtmlTime = function(startDate) {
    this.data[this.startJsHtmlTime] = startDate;
};

PluginLog.prototype.setEndJsHtmlTime = function(endDate) {
    this.data[this.endJsHtmlTime] = endDate;
};

PluginLog.prototype.getRequestMilliSecord = function() {
    return this.getBetweenMilliSecord(this.data[this.startRequestTime],this.data[this.endRequestTime]);
};

PluginLog.prototype.getJsHtmlMilliSecord = function() {
    var milliSecord = this.getBetweenMilliSecord(this.data[this.startJsHtmlTime],this.data[this.endJsHtmlTime]);
    return milliSecord;
};

PluginLog.prototype.getPluginLogData = function() {
   var data = "";
   data+=" getHtmlByJs spend MilliSecord:"+this.getJsHtmlMilliSecord();
   data+=" requestServer spend MilliSecord:"+this.getRequestMilliSecord();
   return data;
};

PluginLog.obj = new PluginLog();