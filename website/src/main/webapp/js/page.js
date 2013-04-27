
/**
 * 分页对象
 * author gudeqiang
 * */
function pagination(){
	/**
	 * 生成分页代码
	 * @param pageNum 当前页码
	 * @param totalRecords 总记录数
	 * @param numPerPage 每页显示记录数
	 * @param url 要跳转的URL
	 * @param divObj Page 内容要挂载的对象 
	 * */
    this.printPagination = function(pageNum, totalRecords, numPerPage, divObj){
        var pageHtml = '';
        
        /**
         * 当前页码
         * */
        var pageNum = pageNum;
        
        if( pageNum < 0 ){
            pageNum = 1;
        }
        
        /**
         * 总记录数
         * */
        var totalNum = totalRecords;
        
        if( totalNum <= 0){
            $('#' + divObj).html('');
            return;
        }
        
        /**
         * 每页记录数
         * */
        var numPerPage = numPerPage;
        
  
        /**
         * 总页码数
         * */
        var totalPages = (totalNum%numPerPage)==0?(totalNum/numPerPage):(totalNum/numPerPage + 1);
        totalPages = parseInt(totalPages,0);
        /**
         * 是否有上一页
         * */
      //  if( pageNum > 1){
        	/**
        	 * 上一页码
        	 * */
        	var prePageNum = pageNum - 1;
        	if(pageNum==1){
       
        	 //    pageHtml +='<span class="an"><a href="javascript:;" >首页</a></span>';
                 pageHtml +='<span class="an"><a href="javascript:;">上一页</a></span>';
        	}else{
        		
        	  //   pageHtml +='<span class="an"><a href="javascript:;" onclick="flipPage(1);">首页</a></span>';
                 pageHtml +='<span class="an"><a href="javascript:;" onclick="flipPage(' + prePageNum + ');">上一页</a></span>';
        	}
        //	}
        
        
        if( 0 < pageNum && pageNum <= 3){
            if(totalPages <=7){
                for(var j = 1; j <= totalPages; j++){
                    if(j == pageNum){
                    	pageHtml +='<strong>[' + j + ']</strong>';
					}else {
						pageHtml +='<a class="pageid" href="javascript:;"  onclick="flipPage('+ j +');">'+ j +'</a>';
					}
                }
            }else{
                for(var j = 1; j <= 7; j++){
                    if(j == pageNum){
						pageHtml +='<strong>[' + j + ']</strong>';
					}else {
						pageHtml +='<a class="pageid" href="javascript:;"  onclick="flipPage('+ j +');">'+ j +'</a>';
					}
                }            
            }
        }
        
        if( pageNum >=4 ){
	        if( pageNum <= totalPages - 3 ){
		        var p = 3;
		        for( var i = -p; i <= p; i++ ){
		        	var page = pageNum + i;					
					if(page > 0 && page <= totalPages){
						if(page == pageNum){
							pageHtml +='<a class="pageid" href="javascript:;">[' + page + ']</a>';
						}else {
							pageHtml+='<a class="pageid" href="javascript:;"  onclick="flipPage('+ page +');">'+ page +'</a>';
						}
					}else if(page > totalPages){
					}
				}
	        }else if( pageNum <=totalPages && pageNum >= totalPages - 3){
                for(var i = totalPages - 7 + 1; i <= totalPages; i++){
                    if(i > 0 && i<= totalPages){
                        if(i == pageNum){
							pageHtml +='<a class="pageid" href="javascript:;">[' + i + ']</a>';
						}else {
						    pageHtml += '<a class="pageid" href="javascript:;"  onclick="flipPage('+ i +');">'+ i +'</a>';
						}
                    }
                }
            }
	        	        
        }
        
      
        
        
        
        //是否有下一页
     //   if( pageNum < totalPages ){        	
        	/**
        	 * 下一页码
        	 * */
        	var nextPage = pageNum + 1;
        	
        	if(pageNum==totalPages){
        		  pageHtml +='<a class="xia" href="javascript:;">下一页</a>';
              //    pageHtml +='<a class="xia" href="javascript:;">尾页</a>';
        	}else{
        		  pageHtml +='<a class="xia" href="javascript:;" onclick="flipPage(' + nextPage + ');">下一页</a>';
                //  pageHtml +='<a class="xia" href="javascript:;" onclick="flipPage('+ totalPages +');">尾页</a>';
        	}
	    
    //    }

        //alert(pageHtml);
        if(totalPages==1) pageHtml='<span class="an"></span><strong>[1]</strong>';
        $('#' + divObj).html(pageHtml);

    };
    
}

