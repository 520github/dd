Validator={
                   password:/^.{6,16}$/,
                   name:/^(-|\w|[\u4e00-\u9fa5]){2,16}$/,
                   describe:/[''"":;\.\/<>~!@#%]/g
};
$(document).ready(function(){
	init();
	$("#saveBtn").click(save);
	$("#describe").keyup(function(){validate($("#describe"));});
});

function init(){
	initDate();
	initAccount();
	setBirthday();
}
	
function save(){
	setBirthday();
	$("#describe").val($.trim($("#describe").val()));
	validate($("#describe"));
	$("#accountForm").ajaxForm({
		cache:false,
		dataType:"json",
		beforeSubmit:function(){
			if(validate()){
				$.blockUI();
				return true;
			}
			return false;
		},success:function(data){
			$.unblockUI();
			$("div.no-login-div a span ").html($.trim($("#name").val()));
			openMessage("success","保存成功");
		}
	});	
}
function validate(collection){
	var flag =true;
	if(collection == null){
		collection = $("#name,#birthday");
	}
	collection.each(function(index){
			var $id=$(this).attr("id");
			switch($id){
				case "name":
					if($(this).val()=='' || !Validator.name.test($(this).val())){
						openMessage("failure","昵称为2-16的中文、数字、字母");
						flag=false;
					}else if($(this).val()!=account.name && checkName($(this).val())){
						openMessage("failure","昵称已存在");
						flag=false;
					}
					break;	
				case "birthday":
					var year=$("#year").val()==null?"":$("#year").val();
					var month=$("#month").val()==null?"":$("#month").val();
					var day=$("#day").val()==null?"":$("#day").val();
					if((year=='' && month=='' && day=='')||(year!='' && month!='' && day!='')){
						
					}else{
						openMessage("failure","请填写完整的生日");
						flag=false;
					}
					break;	
				case "describe":
					$(this).val($(this).val().replace(Validator.describe,""));
					var str=$(this).val();
					if(str.length>140){
						$(this).val(str.substring(0, 140));
					}
					break;						
				default:
					flag=false;
			}
			return flag;
	});
	return flag;
}

function checkName(name){
	var flag=false;
	$.ajax({
		type : 'get',
		url : '/account/user/information/name/'+encodeURIComponent(name),
		dataType: 'json',
		async:false,
		success : function(data){
			if(data.result=='true'){
				flag=true;
			}
		}
	});	 
	return flag;
}

/*************************************************/

function initAccount(){
	$("#name").val(account.name);
	$("#loginName").text(account.loginName);
	$("input[value="+account.gender+"]").attr("checked","checked");
	if(account.birthday!=null && account.birthday!=''){
		var ymdhms = account.birthday.split("T");
		var ymd=ymdhms[0].split("-");
		initDate(ymd[0],ymd[1],ymd[2]);	
	}

}

function setBirthday(){
	var year=$("#year").val()==null?"":$("#year").val();
	var month=$("#month").val()==null?"":$("#month").val();
	var day=$("#day").val()==null?"":$("#day").val();
	if(year=='' && month=='' && day==''){
		$("#birthday").val("");
	}else{
		$("#birthday").val(year+"-"+month+"-"+day);		
	}
		
		
}


function initDate(yy,mm,dd)
{
	var year=$("#year");
	var month=$("#month");
	var day=$("#day");
      //每个月的初始天数
    MonDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    //当前的年份
    var y = parseInt(new Date().getFullYear());
    
    year.empty();
    year.append('<option  value=""></option>');
    for (var i = y; i > (y-70); i--)
      {
    	var option='<option  value="'+i+'">'+i+'</option>';
        year.append(option);
      }
    if(yy!=null && yy!=''){
    	year.val(yy);
    }

    //填充月份下拉框
    month.empty();
    month.append('<option  value=""></option>');
    for (var i = 1; i <= 12; i++)
	  {
		var option='<option  value="'+i+'">'+i+'</option>';
		month.append(option);
	  }
    if(mm!=null && mm!=""){
    	month.val(mm);
        //获得当月的初始化天数
        var n = MonDays[mm-1];
        //如果为2月，天数加1
        if (mm == 2 && isLeapYear(year.val()))
              n++;
        //填充日期下拉框
        createDay(n,day); 
       
        day.val(dd);
    }
          

}

function change() //年月变化，改变日
{
	var year=$("#year");
	var month=$("#month");
	var day=$("#day");
     var y = year.val();
     var m = month.val();
     var n = MonDays[m - 1];
     if ( m ==2 && isLeapYear(y))
     {
         n++;
     }
     createDay(n,day);
}


function createDay(n,day) //填充日期下拉框
{
    //清空下拉框
     day.empty();
     //几天，就写入几项
     day.append('<option  value=""></option>');
     for(var i=1; i<=n; i++)
     {
    	var option='<option  value="'+i+'">'+i+'</option>';
     	day.append(option);
     }
}


function isLeapYear(year)//判断是否闰年
{ 
    return( year%4==0 || (year%100 ==0 && year%400 == 0));
}


function openMessage(type,msg) {
    openPopupMessage(type,msg);   
}

function calculate_length(content){
	var i,n=content.length,b=0;
    var c;
    for(i=0;i<n;i++){
        c=content.charCodeAt(i);
        if(!(c < 0x4E00 || c > 0x9FA5)){
            b++;
        }
    }	    
    return n+b;
}
   
