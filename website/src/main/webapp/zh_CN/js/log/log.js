function Log() {
};

Log.prototype.getBetweenMilliSecord = function(beforeDate,afterDate) {
    //return DateUtil.getBetweenDistanceMilliSecord(beforeDate,afterDate);
    return afterDate - beforeDate;
};