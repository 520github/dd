function ContentB() {
};

ContentB.prototype.getOffsetAndLimitPara = function() {
    var param = new Array();
	       
    var offset = jQuery("input[name='offset']").val();
    if(offset == null)offset = 0;
    var limit = jQuery("input[name='limit']").val();
    if(limit == null)limit = 20;
   
    param.push({name: "offset", value: offset});
    param.push({name: "limit",  value: limit});
   
    var r=(Math.random()*99999999);
    param.push({name: "r",  value: r});
   
    return param;
};