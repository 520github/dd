	function openComment(repositoryId,contentId,flag){
		var control = null;
		var cid = contentId;
		if (contentId.indexOf("%") > -1) {
			control = $(".comment_layer[rid='"+repositoryId+"']");
			cid = control.attr("contentId");
		}
		else {
			control = $("#comment_layer_" + contentId);
		}
		PageNum = control.find("#PageNum").val();
		PageSize = control.find("#PageSize").val();
		if(PageNum==null||PageNum=="undefined")	
			PageNum = 0;
		if(PageSize==null||PageSize=="undefined")	
			PageSize =10;
		if (flag=="1"&&control.attr("state") == "1") {
			control.attr("state", "0").hide();
			return;
		}
		$.get("/comment/detail?contentId=" + cid + "&PageNum=" + PageNum +"&PageSize=" + PageSize ,null,function(data) {
			var obj = control;
			var zhankai_div = control.find("#zhankai_div");
			obj.show().attr("state", "1");
			$(data).insertBefore(zhankai_div);
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
			
			
			$(".star_star").each(function(i){
				$(this).raty({
		            path : "/images",
		            hints   : ['1','2','3','4','5'],
		            half       : false,
		            precision  : true,
		            size       : 24,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'star-off-big.png',
		            starOn     : 'star-on-big.png',
		            targetType : 'number',
		            mouseover : function(score, evt) {
		                var s = 0;
		                if (score != null && score != NaN && score != undefined) {
		                	s = Math.round(score);
		                }
		                
		                $(this).attr("score", s);
		                $("#scoreqq").attr("value", s);
		            }
	        	});
	        });
			
			$(".shit_shit").each(function(i){
				$(this).raty({
		            path : "/images",
		            hints   : ['-1','-2','-3','-4','-5'],
		            half       : false,
		            precision  : true,
		            size       : 24,
		            starOff    : 'shit-off.png',
		            starOn     : 'shit-on.png',
		            targetType : 'number',
		            mouseover : function(score, evt) {
		                var s = 0;
		                if (score != null && score != NaN && score != undefined) {
		                	s = Math.round(score * -1);
		                }
		                
		                $(this).attr("score", s);
		                $("#scoreqq").attr("value", s);
		            }    
	        	});
	        });
			
			$(".fixStar").each(function(i){
				var score = $(this).attr("score");
				$(this).raty({
		            path : "/images",
		            hints   : ['1','2','3','4','5'],
		            half       : false,
		            precision  : true,
		            score : score,
		            size       : 24,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'star-off-big.png',
		            starOn     : 'star-on-big.png',
		            targetType : 'number',
		            readOnly : true
				});
	         });
		
			$(".fixShit").each(function(i){
				var score = $(this).attr("score");
				score = -score;
				$(this).raty({
		            path : "/images",
		            hints   : ['-1','-2','-3','-4','-5'],
		            half       : false,
		            precision  : true,
		            score : score,
		            size       : 24,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'shit-off.png',
		            starOn     : 'shit-on.png',
		            targetType : 'number',
		            readOnly : true
				});
	         });
		
		//sleep(2000);
		var ll = obj.find("#textarea_comment").length;
		//alert('ll='+ll);
		$("#textarea_comment").keyup(function(){
		 	//alert('chinese');

			var maxl = 140;
			
			var tishi = "还dd可以输入" + maxl +"个字";
			
			obj.find("#wordsnumberleft").html(tishi);
			
			//var xianyou = $(this).val().length ;
			
			var content = $(this).val();	//	可能是中英文混合的评论
			
			var content_legth = calculate_length(content);
			
			
			var keyi = maxl - content_legth;
			
			//var tishi = "还可以输入" + keyi +"个字";
			var tishi = "" + keyi +"";
			
			if(content_legth > 139){
			
			 //var tishi = "还可以输0个字"
			 var tishi = "0"
			alert('不可以在输入了');
			 obj.find("#wordsnumberleft").css({"color":"red"});
			
			}
			
			obj.find("#wordsnumberleft").html(tishi);
			
			  });
		}); //end of $.get(
		
	}
	   //获取query参数
	   function getzhankaiQueryPara() {
	       var param = new Array();
	       
	       var offset = jQuery("input[id='offset']").val();
	       
	       var limit = jQuery("input[id='limit']").val();
	       if(offset == null)offset = "0";
	       if(limit == null)limit = "6";
	       
	       
		   param.push({name: "limit",  value: limit});
		   param.push({name: "offset",  value: offset});
	       return param;
	   }
	   
	   //加载评论列表------加载展开按钮取回的评论
	   function loadCommentList(repositoryId,resp) {
	       var html = jQuery("#commentlist").html();
	       var total = resp.total;
           //alert("totalwwwwww="+total+"resp="+resp);
	       var results = resp.result;
           //alert("results.length="+results.length);
	       for(var i=0;i<results.length;i++) {
               var comment = results[i];
               //alert(comment.accountId+"|"+comment.text);
               
               var star = ""
		   	   for(var i=1;i<=10;i++){
		   	   	if(i==comment.score){
		   	   		star = star + "<input class='star {split:2}' type='radio' name='test-4-rating-3' disabled='disabled' value='" +i +"'/>";
		   	   	}else{
		   	   		star = star + "<input class='star {split:2}' type='radio' name='test-4-rating-3' value='" +i +"'/>";
		   	   	}
		   	   	
		   	   }
               
               var uuuu = "------------------------------------------------------------------------<div class='user-info'>昵称:"+comment.accountId+"评分:"
                +comment.score
               	+"</div><div class='comment-rst'><div id='"+comment.id+"'>"+comment.text
               	+"</div></div>";
               jQuery("div#"+repositoryId).find("div#commentlist").append(uuuu);
           }    
           offset_inc(total); 
	   }
	   
	   
	   //offset加1
	   function offset_inc(total) {
	       var offset = jQuery("#offset").val();
	       var limit = jQuery("#limit").val();
	       var new_offset = parseInt(offset)+1;
	       jQuery("#offset").val(new_offset);
	       
	       if(new_offset*limit>total){	//	隐藏展开按钮
	       		//alert("total"+total+"limit"+limit+"new_offset"+new_offset);
	       		jQuery("#zhankai_button").attr("style","display:none");
	       }
	   }
	   
	   function checkContent(textObj){
		  //alert('hhhhhh');
	      //var textObj = document.getElementById("textarea_comment");
	      if(textObj.value == "我觉得这个东东可能对你有帮助，希望你喜欢哦！"){
	           textObj.value = "";
	      }
		}
		
	   function checkwordnumber(){
	   	  alert('checkwordnumber');
	      var textObj = document.getElementById("textarea_comment");
	      var wordsnumberleft = document.getElementById("wordsnumberleft");
	      var lefy = 140-textObj.length;
	      $("p#wordsnumberleft").html(lefy);
	      if(lefy<0){
	      	
	      }
		}
		
		// 发表评论
	   function postcomment(contentId){
		   		//alert('contentId='+contentId);
		   //var control = $("div#detail .comment_layer [contentid=]"+contentId);
		   var control = jQuery("div#detail .comment_layer");	// 这是详情页面上的,以前用浮层的时候
			var statee = control.attr("state");
	   		//alert('statee='+statee);
			
		   if (control == null || control == undefined||statee!=1) {
		   		//alert('zzzzzzzzzzzzzzzz');
		   		control = $("#comment_layer_" + contentId);	//	这是在列表页 每个文章内容之后的评论 不过在这个地方也不对 这个地方一般不能阅读全文 没有阅读全文就给评价?
		   }
		   //alert('control='+control);
	   		var comment = $("#textarea_comment").val();
	   		if(comment.length>140){
	   			alert('不超过140字');
	   			return;
	   		}
	   		//var score = control.find(".star_star").attr("score");
	   		var score = $("#scoreqq").val();
	   		//alert('score='+score);
	   		var token = cookieUtil.getHeaderToken();
	   		var param = {
	   			score:score,
	   			text:comment
	   		}
	   		jQuery.ajax({
	           url:"/service/comment/"+contentId,
	           type:"post",
	           data:JSON.stringify(param),
	           datatype:"application/json",
	           contentType:"application/json;charset=utf-8",
	           headers:token,
	           success:function(resp) {
	               refresh_div(control, resp);
	           },
	           complete:function() {
	               try{
	                  // refresh_div();
	               }catch(e){
	               }
	           }
	       }
	       );
	   }
	   
	   function refresh_div(control, comment){
	   		if(comment.text=="nomoreoneday")	 {alert('bu足一天');return;}
		   var str = '<div id="cmt_'+comment.id+'" class="personal-comments personal-comments-1">';
		   //str += '------------------------------------------------------------------------';
		   str += '<dl>';
		   str += '<dt class="person-logo">';
		   str += '<img src="/images/logo-national-flag.gif" alt="" />';
		   str += '</dt>';
		   str += '<dd>';
		   str += '<p>';
		   str += comment.accountId;
		   str += '</p>';
		   str += '<div class="';
		   //alert('comment.score='+comment.score);
		   if(comment.score>0)
		   	str += 'fixStar';
		   	
		   if(comment.score<0)
		   	str += 'fixShit';
		   	
		   if(comment.score==0)
		   	str += 'noScore';
		   	
		   str += '" score="';
		   str += comment.score;
		   str += '"</div>';
		   str += '<p class="money">';
		   str += comment.lastComment;
		   str += '</p>';
		   str += '<p class="money">';
		   str += comment.text;
		   str += '</p>';
		   str += '</dd>';
		   str += '</dl>';
		   str += '</div>';
		   //alert('str='+str);
		   //control.find(".commentlist").prepend(str);
		   //control.prepend(str);
		   $(str).insertBefore(control);
		   $(".fixStar").each(function(i){
				var score = $(this).attr("score");
				$(this).raty({
		            path : "/images",
		            hints   : ['1','2','3','4','5'],
		            half       : false,
		            precision  : true,
		            score : score,
		            size       : 24,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'star-off-big.png',
		            starOn     : 'star-on-big.png',
		            targetType : 'number',
		            readOnly : true
				});
	         });
	         
		   $(".fixShit").each(function(i){
				var score = $(this).attr("score");
				score = -score;
				$(this).raty({
		            path : "/images",
		            hints   : ['-1','-2','-3','-4','-5'],
		            half       : false,
		            precision  : true,
		            score : score,
		            size       : 24,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'shit-off.png',
		            starOn     : 'shit-on.png',
		            targetType : 'number',
		            readOnly : true
				});
	         });
	   }	// end of refresh_div
	   
function sleep(milisecond) {

        var currentDate, beginDate = new Date();

        var beginHour, beginMinute, beginSecond, beginMs;

        var hourGaps, minuteGaps, secondGaps, msGaps, gaps;

        beginHour = beginDate.getHours();

        beginMinute = beginDate.getMinutes();

        beginSecond = beginDate.getSeconds();

        beginMs = beginDate.getMilliseconds();

        do {

            currentDate = new Date();

            hourGaps = currentDate.getHours() - beginHour;

            minuteGaps = currentDate.getMinutes() - beginMinute;

            secondGaps = currentDate.getSeconds() - beginSecond;

            msGaps = currentDate.getMilliseconds() - beginMs;

            if (hourGaps < 0) hourGaps += 24;   //考虑进时进分进秒的特殊情况

            gaps = hourGaps * 3600 + minuteGaps * 60 + secondGaps;

            gaps = gaps * 1000 + msGaps;

        } while (gaps < milisecond);

    }	   
	   
	   
	function calculate_length(content){
		var i,n=content.length,b=0;
	    var c;
	    for(i=0;i<n;i++){
	        c=content.charCodeAt(i);
	        if(isChinese(c)){
	            b++;
	        }
	    }	    
	    return n+b;
	}
	   
	function isChinese(c){
		//alert('c='+c);
		if(c < 0x4E00 || c > 0x9FA5) {
            return false;
        }
        return true;
	}

	
	// 目前在unicode标准中，汉字的charCode范围是[0x4E00, 0x9FA5]
    function test() {
        var s = document.all.name.value ;
        for(var i = 0; i < s.length; i++)
            if(s.charCodeAt(i) < 0x4E00 || s.charCodeAt(i) > 0x9FA5) {
                window.alert("输入非中文，请重新输入") ; 
                break ;
            }
    }