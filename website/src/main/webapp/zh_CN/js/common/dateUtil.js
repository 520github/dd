function DateUtil() {
};

DateUtil.getDate8String = function(dateIn) {
    if(dateIn == null) return "";
    var year = dateIn.getFullYear()+"";
    var month = dateIn.getMonth()+1;
    if(month <10)month = "0"+month;
    else month=month+"";
    var day = dateIn.getDate();
    if(day<10)day = "0"+day;
    else day = day +"";
    
    return year+month+day;
};

DateUtil.getDate10String = function(dateIn) {
    if(dateIn == null) return "";
    
    var date = DateUtil.getDate8String(dateIn);
    if(date.length <8)return date;
    
    return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
};

DateUtil.getDateTimeString = function(dateIn) {
    if(dateIn == null) return "";
    var time = dateIn.toLocaleTimeString()
    var date = DateUtil.getDate10String(dateIn);
    
    return date + " " +time;
};

DateUtil.getBetweenDistanceMilliSecord = function(beforeDate,afterDate) {
    if(beforeDate == null)return 0;
    if(afterDate == null)return 0;
    if(typeof beforeDate === 'date') {
        console.log("jjjjjjj:date");
    }
    return afterDate.getTime() - beforeDate.getTime();
};
