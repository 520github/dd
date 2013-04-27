/*
**	Anderson Ferminiano
**	contato@andersonferminiano.com -- feel free to contact me for bugs or new implementations.
**	jQuery ScrollPagination
**	28th/March/2011
**	http://andersonferminiano.com/jqueryscrollpagination/
**	You may use this script for free, but keep my credits.
**	Thank you.
*/

(function( $ ){
	 
		 
 $.fn.scrollPagination = function(options) {
		var opts = $.extend($.fn.scrollPagination.defaults, options);  
		opts.fetchStart = false;
		opts.offset = 0;
		var target = opts.scrollTarget;
		if (target == null){
			target = obj; 
	 	}
		opts.scrollTarget = target;
		var initStatus = false;
		
		try{
		    getContentObject().empty();
		    jQuery("input[name='isLastPage']").attr("value","false");
		}catch(e){
		}
		
		this.each(function() {
		  initStatus = true;
		  $.fn.scrollPagination.init($(this), opts);
		});
		
		if(!initStatus) {
		    initStatus = true;
		    $.fn.scrollPagination.init($(this), opts);
		}
		return ;
  };
  
  $.fn.stopScrollPagination = function(){
	  return this.each(function() {
	 	$(this).attr('scrollPagination', 'disabled');
	 	alert("亲，别滚了，缺货了啊！");
	 	return ;
	  });
	  
  };
  
  $.fn.scrollPagination.resetDetail = function() {
      var dwidth = window.screen.availWidth;
      var dheight = window.screen.availHeight;
      var sleft = document.body.scrollLeft;
      var stop = document.body.scrollTop;
      var top = (dheight - stop)/2;
      if(stop > dheight) {
          top = (stop - dheight)/2;
      }
      top = (document.body.scrollTop+(document.body.clientHeight-document.getElementById("detail").offsetHeight)/2);
      //$("detail").css("top",top);
      //$("detail").css("left",15);
      //$("div.ui-dialog").css("top", (($(window).height() - $("div.ui-dialog").outerHeight()) / 2) + $(window).scrollTop() + "px");
      //$("div.ui-dialog").css("left", (($(window).width() - $("div.ui-dialog").outerWidth()) / 2) + $(window).scrollLeft() + "px");
  }
  
  $.fn.scrollPagination.loadContent = function(obj, opts){
     //$.fn.scrollPagination.resetDetail();
     //已经是最后一页
     if(jQuery("input[name='isLastPage']").val() == "true") {
         var currentPage = parseInt(jQuery("input[name='currentPage']").val());
         if(currentPage <= 1) {
             $("#pageNav").hide();
             return;
         }
         $("#pageNav").show();
         $("#nextPage").hide();
         if(currentPage > 1)$("#prePage").show();
         return;
     }
     
     $("#pageNav").hide();
     var itemsPerPage = parseInt(jQuery("input[name='itemsPerPage']").val());
     if(itemsPerPage == null || itemsPerPage < 0)itemsPerPage =20;
     //当前记录数大于等于一页最大展现数时,启动分页机制
     var offset = getContentObject().children().size();
	 if(offset >= itemsPerPage) {
	     $("#pageNav").show();
	     var currentPage = parseInt(jQuery("input[name='currentPage']").val());
	     $("#prePage").show();
	     if(currentPage <= 1) {
	         $("#prePage").hide();
	     }
	     var isLastPage = jQuery("input[name='isLastPage']").val();
	     $("#nextPage").show();
	     if(isLastPage == "true") {
	         $("#nextPage").hide();
	     }
	     
	     return;
	 }   
	 //分页触发动作的，不再进行自动加载
	 if(jQuery("input[name='scroll']").val() == "false") {
	     jQuery("input[name='scroll']").attr("value","mid");
	     return;
	 } 
	 //分页触发动作的，不再进行自动加载
	 if(jQuery("input[name='scroll']").val() == "mid") {
	     jQuery("input[name='scroll']").attr("value","true");
	     return;
	 }  
	 //否则以滚动鼠标方式自动加载数据  
	 var target = opts.scrollTarget;
	 var mayLoadContent = $(target).scrollTop()+opts.heightOffset >= $(document).height() - $(target).height();
	 if (opts.initStatus || mayLoadContent){
	     if(opts.initStatus)opts.initStatus = false;
		 if (opts.beforeLoad != null){
			opts.beforeLoad(); 
		 }
		 //$(obj).children().attr('rel', 'loaded');
		 
		 opts.offset = offset;
		 getDataList("");
		 /*$.ajax({
			  type: 'POST',
			  url: opts.contentPage,
			  data: opts.contentData,
			  success: function(data){
			    alert("gogo");
			    getDataList("");
				//var objectsRendered = $(obj).children('[rel!=loaded]');
				
				if (opts.afterLoad != null){
					//opts.afterLoad(objectsRendered);	
				}
			  },
			  dataType: 'html'
		 });
		 */
	 }
	 
  };
  
  $.fn.scrollPagination.init = function(obj, opts){
	 var target = opts.scrollTarget;
	 $(obj).attr('scrollPagination', 'enabled');
	
	 $(target).scroll(function(event){
		if ($(obj).attr('scrollPagination') == 'enabled'){
		    if(opts.fetchStart) {
		        return;
		    }
		    
		    var offset = getOffset();
		    if(offset <= opts.offset) {
		        return;
		    }
		    
		    opts.fetchStart = true;
	 		$.fn.scrollPagination.loadContent(obj, opts);	
	 		opts.fetchStart = false;
		}
		else {
			event.stopPropagation();	
		}
	 });
	 
	 $.fn.scrollPagination.loadContent(obj, opts);
 };
	
 $.fn.scrollPagination.defaults = {
      	 'contentPage' : null,
     	 'contentData' : {},
		 'beforeLoad': null,
		 'afterLoad': null	,
		 'scrollTarget': null,
		 'heightOffset': 0,
		 'initStatus':true		  
 };	
})( jQuery );