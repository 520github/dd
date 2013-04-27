function $A(name){return document.getElementsByName(name);}  
    window.onload=function(){  
        /**  
         * 复选框限制  
         * @param {Object} name 复选框的name  
         * @param {Object} maxck 最多复选个数  
         */  
        function checks(name,maxck){  
            var cks = $A(name);  
            function check(){  
                var t=0;  
                for(i=0;i<cks.length;i++){  
                    if(cks[i].checked){t++;}  
                    if(t>maxck){return false;}  
                }  
                return true;  
            }  
            for(i=0;i<cks.length;i++){  
                cks[i].onclick=function(){  
                    if(!check()){  
                        alert("最多选择"+maxck+"个");  
                        this.checked=false;  
                    }  
                }  
            }  
              
        } 
          
        checks("interestString",3);  
        
    }  
      

function postInformation(){
	
$("#informationForm").ajaxSubmit({				
				dataType:"json",
				success:function(data){
					if(data.actionErrors && data.actionErrors.length>0){						
						alert(data.actionErrors[0]);
					}else{									
						if(data.success == false){
								alert("提交失败")
						}else{
						window.location= "/";						
					}
					}
				}
				});	
	
}