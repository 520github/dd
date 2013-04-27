// it seems that nobody need this file,maybe should be abolish. 2012-07-11 14:14:40

	function openRemainContent(accountId,pageType){
	
		var currentPage = parseInt(jQuery("input[name='currentPage']").val());
		if(pageType =='next') {
          jQuery("input[name='currentPage']").attr("value",currentPage+1);
	    }
	    else {
	        jQuery("input[name='currentPage']").attr("value",currentPage-1);
	    }
	    jQuery("#PageNum").html(jQuery("input[name='currentPage']").val());	    
       
		var control = $("#My_index");
		PageNum = control.find("#PageNum").html();
		PageSize = control.find("#PageSize").val();
		if(PageNum==null||PageNum=="undefined")	
			PageNum = 0;
		if(PageSize==null||PageSize=="undefined")	
			PageSize =3;		
		$.get("/getLeftContent?accountId=" + accountId + "&PageNum=" + PageNum +"&PageSize=" + PageSize ,null,function(data) {
			var zhankai_div = control.find("#zhankai_div");
			var Template = control.find("#Template");
			//$(data).insertBefore(zhankai_div);
			Template.html(data);
			var t_Page_Num = control.find("#PageNum").val();
			var t_PageSize = control.find("#PageSize").val();
			// alert(parseInt(t_Page_Num) + 1);
			control.find("#PageNum").val(parseInt(t_Page_Num) + 1);
			var totalNumber = $("#totalNumber").val();
			var isLastPage = control.find(".isLastPage").last().val();
			//alert(parseInt(totalNumber)-(parseInt(t_Page_Num) + 1)*parseInt(t_PageSize));
			control.find("#remainNum").html(parseInt(totalNumber)-(parseInt(t_Page_Num) + 1)*parseInt(t_PageSize));
			
			if(isLastPage=="true"){
				zhankai_div.hide();
			}	     
		}); //end of $.get(
	}
	
		function openRemainMessages(accountId,pageType){
	
		var currentPage = parseInt(jQuery("input[name='currentPage']").val());
		if(pageType =='next') {
          jQuery("input[name='currentPage']").attr("value",currentPage+1);
	    }
	    else {
	        jQuery("input[name='currentPage']").attr("value",currentPage-1);
	    }
	    jQuery("#PageNum").html(jQuery("input[name='currentPage']").val());	    
       
		var control = $("#My_index");
		PageNum = control.find("#PageNum").html();
		PageSize = control.find("#PageSize").val();
		if(PageNum==null||PageNum=="undefined")	
			PageNum = 0;
		if(PageSize==null||PageSize=="undefined")	
			PageSize =3;		
		var offset = PageSize*PageNum;
		$.get("//messages?accountId=" + accountId + "&offset=" + offset +"&limit=" + PageSize ,null,function(data) {
			var zhankai_div = control.find("#zhankai_div");
			var Template = control.find("#Template");
			//$(data).insertBefore(zhankai_div);
			Template.html(data);
			var t_Page_Num = control.find("#PageNum").val();
			var t_PageSize = control.find("#PageSize").val();
			// alert(parseInt(t_Page_Num) + 1);
			control.find("#PageNum").val(parseInt(t_Page_Num) + 1);
			var totalNumber = $("#totalNumber").val();
			var isLastPage = control.find(".isLastPage").last().val();
			//alert(parseInt(totalNumber)-(parseInt(t_Page_Num) + 1)*parseInt(t_PageSize));
			control.find("#remainNum").html(parseInt(totalNumber)-(parseInt(t_Page_Num) + 1)*parseInt(t_PageSize));
			
			if(isLastPage=="true"){
				zhankai_div.hide();
			}	     
		}); //end of $.get(
	}