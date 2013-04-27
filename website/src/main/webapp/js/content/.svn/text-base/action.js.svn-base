var _repositoryId;
var _menuType;
function showCancelFavoriteDialog(repositoryId,menuType){
	_repositoryId = repositoryId;
	_menuType = menuType;
	
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#cancelFavoriteDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}
function showCancelFriendShareDialog(repositoryId,menuType){
	_repositoryId = repositoryId;
	_menuType = menuType;
	
	$.layer({
	    v_box : 1,
	    v_istitle:false,
	    v_dom : '#cancelFriendShareDialog',	//id
	    v_area : ['1000px','420px'],
	    v_btns : 0,
	    v_showclose:false
	});
}
      //通过内容类型查询
      function queryContentType(contentType,contentTypeName) {
          try{
              layer_close();
          }catch(e){
          }
          jQuery("input[name='contentType']").attr("value",contentType);
          jQuery("#selectedContentType").html(contentTypeName);
          getDataList("refresh");
          if(contentTypeName != "") {
              jQuery("#contentTypeId a[href=#] span").html(contentTypeName);
          }
          //jQuery("input[name='contentType']").attr("value","");
      }
      
      //通过内容来源查询
      function queryContentSource(contentSource,contentSourceName) {
          try{
              layer_close();
          }catch(e){
          }
          jQuery("input[name='contentSource']").attr("value",contentSource);
          jQuery("#selectedContentSource").html(contentSourceName);
          getDataList("refresh");
          if(contentSourceName != "") {
              jQuery("#contentSourceId a[href=#] span").html(contentSourceName);
          }
          //jQuery("input[name='contentSource']").attr("value","");
      }
      
      //通过标签内容查询
	   function queryTag(tag) {
	       try{
	           layer_close();
	       }catch(e) {
	       }
	       jQuery("input[name='userTag']").attr("value",tag);
	       
	       if(tag == "")tag ="全部";
	       if(tag != "") {
	           jQuery("#editTagId a[href=#] span").html(tag);
	       }
           jQuery("#selectedTag").html(tag);
           getDataList("refresh");
           //jQuery("input[name='userTag']").attr("value","");
	   }
      
      //通过好友查询
      function queryFriend(id, name) {
          try{
              layer_close();
          }catch(e) {
          }
          
          jQuery("input[name='friendFilter']").attr("value",id);
          jQuery("#selectedFriend").html(name);
          getDataList("refresh");
          //jQuery("input[name='contentType']").attr("value","");
      }
      
      //通过已读条件查询
      function queryReadCond(readCond,obj) {
          jQuery("input[name='readCond']").attr("value",readCond);
          getDataList("refresh");
          jQuery("input[name='readCond']").attr("value","");
          
          //设置选中css样式
          jQuery(".check-box ul li").attr("class","");
          jQuery(obj).parent().attr("class","check-box-press-l");
          if(readCond == "My_Recommend") {
              jQuery(obj).parent().attr("class","check-box-press-r");
          }
      }
      
      //打开详情页面
      function openItemDetail(repositoryId,url,type) {
          var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
          var open_url = "/private/item?id="+repositoryId+"&menuType="+menuType;
          if(type == "Type_Video") {//视频情况,直接打开原网页
              //open_url = "/private/url?url="+url;
          }
          if(menuType == "unread") {
              jQuery("#"+repositoryId).remove();
	          redureOrAddCountValue(-1,"unread");
          }
          window.open(open_url);
      }
      
      //打开url
      function openUrl(url,contentId) {
          var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
          window.open("/private/url?menuType="+menuType+"&contentId="+contentId+"&url="+url);
      }
      
      //打开目标url
      function openTargetUrl(url) {
         window.open(url);
      }
      
      //收藏与取消收藏进行切换
      function swithCancelOrCollectPage(menuType,action,repositoryId) {
          if(menuType == "collect") {
              return;
          }
          var pageType = jQuery("input[name=pageType]").val();
          if(action == "collect") {//收藏动作
              getContentIdCollectBtn(repositoryId).hide();
              getContentIdUnCollectBtn(repositoryId).show();
              if(pageType == "itemPage") {//详情页面
                  getDetailCollectBtn().hide();
                  getDetailUnCollectBtn().show();
              }
          }
          else if(action == "uncollect"){//取消收藏动作
              getContentIdCollectBtn(repositoryId).show();
              getContentIdUnCollectBtn(repositoryId).hide();
              if(pageType == "itemPage") {//详情页面
                  getDetailCollectBtn().show();
                  getDetailUnCollectBtn().hide();
              }
          }
      }
      //功能内容最爱
      function favoritePublicMes(repositoryId,menuType) {
          var pageType = jQuery("input[name=pageType]").val();
          if(pageType == "itemPage") {//详情页面
              window.opener.favoritePublic(repositoryId,menuType);
          }  
          favoritePublic(repositoryId,menuType);
      }
      
      function favoritePublic(repositoryId,menuType) {
          var contentId = repositoryId;
          ContentBusiness.obj.addFavoritePublicContent(contentId);
      }
      
      //收藏内容
      function favoriteMes(repositoryId,menuType) {
          var pageType = jQuery("input[name=pageType]").val();
          if(pageType == "itemPage") {//详情页面
              window.opener.favorite(repositoryId,menuType);
          }
          else {
              favorite(repositoryId,menuType);
          }
      }
      
      //收藏内容收藏
	   function favorite(repositoryId,menuType) {
	      var data = {
			  tag:["Collect"],
			  menuType:menuType
		  };
	      doContent(repositoryId,data,"collect");	      
	   }
	   
	   //提示是否取消收藏
	   function cancelFavoriteMes(repositoryId,menuType) {
	      //if(confirm('好不容易收藏，你真舍得抛弃吗？')) {
	         var pageType = jQuery("input[name=pageType]").val();
	         if(pageType == "itemPage") {//详情页面
	             if(menuType =="favorite") {
	                 //cancelFavorite(repositoryId,menuType);
	                 window.opener.cancelFavorite(repositoryId,menuType);
	                 window.close();
	             }
	             else{
	                 cancelFavorite(repositoryId,menuType);
	                 window.opener.cancelFavorite(repositoryId,menuType);
	             }
	         }else {
	             cancelFavorite(repositoryId,menuType);
	         }
	      //}
	   }
	   
	   //删除好友分享提示信息
	   function cancelFriendShareMes(repositoryId,menuType) {
	      //if(confirm('这是您知心好友给您分享的内容哦，你真得视之如粪土吗？')) {
	         var pageType = jQuery("input[name=pageType]").val();
	         if(pageType == "itemPage") {//详情页面
	             window.opener.cancelFriendShare(repositoryId,menuType);
	             window.close();
	         }else {
	             cancelFriendShare(repositoryId,menuType);
	         }
	      //}
	   }
	   
	   //取消内容收藏
	   function cancelFavorite(repositoryId,menuType) {
	       var data = {	
			  tag:["Delete_Collect"],
			  menuType:menuType
		   };
		   doContent(repositoryId,data,"uncollect");
	   }
	   
	   //存入稍后看
	   function later(repositoryId,menuType) {
	       var data = {
			  tag:["Later"],
			  menuType:menuType
		   };
		   doContent(repositoryId,data,"later");
	   }
	   
	   //已读
	   function read(repositoryId,menuType) {
	       var data = {
			  tag:["Read"],
			  menuType:menuType
		   };
		   doContent(repositoryId,data,"read");
	   }
	   
	   //取消朋友分享
	   function cancelFriendShare(repositoryId,menuType) {
	       var data = {
			  tag:["Delete_Source_Friend"],
			  menuType:menuType
		   };
		   doContent(repositoryId,data,"cancelFriendShare");
	   }
	   
	   
	   //操作内容,包括收藏、稍后看、取消收藏等
	   function doContent(repositoryId,para,type) {
	       jQuery.ajax({
	           url:"/service/content/personal/item/"+repositoryId,
	           type:"put",
	           contentType:"application/json",
	           data:JSON.stringify(para),
	           headers:token,
	           complete:function(XMLHttpRequest){
	               var menuType = jQuery("input[name=menuType]").val();
	               var num = -1;
	               var status = XMLHttpRequest.status;
	               if(status == 409) num = 0;
	               if(type == "collect") {//收藏
	                   if(menuType=="unread") {//menuType == "later" || 
	                       jQuery("#"+repositoryId).remove();
	                   }
	                   redureOrAddCountValue(num,type);
	               }
	               else if(type == "uncollect") {//取消收藏
	                   if(menuType!="later") {
	                       if(menuType=="favorite" || menuType=="unread")jQuery("#"+repositoryId).remove();
	                       redureOrAddCountValue(num,type);
	                   } 
	               }
	               else if(type == "later") {//稍后看
	                   //redureOrAddCountValue(num,type);
	               }
	               else if(type == "cancelFriendShare") {//取消朋友分享
	                   jQuery("#"+repositoryId).remove();
	                   redureOrAddCountValue(num,type);
	               }
	               if(type !="read") {
	                   //弹出提示消息
	                   openMessage(type);
	               }
	               swithCancelOrCollectPage(menuType,type,repositoryId);
	           }
	       });
	   }
	   
	   //弹出消息框
	   function openMessage(type) {
	       openPopupMessage(type,'');
	   }
	   
	   
	   //设置查询条件的显示情况
	   function setSearchVisit() {
	       jQuery(".cssmenu>ul>li>a").each(function(){
            var obj = jQuery(this).parent().find("ul");
            obj.hide();
            
            jQuery(obj).find("li>a").each(function(){
                var href = jQuery(this).attr("href");
                href = href + ";hiddenUl();";
                //alert(href);
                jQuery(this).attr("href",href)
            });
            
            jQuery(this).click(function(){
                var num = jQuery(this).attr("num");
                if(num == null)num ="0";
                jQuery(this).attr("num",parseInt(num)+1);
                var show = obj.is(":visible");
                jQuery(".cssmenu>ul>li>a").parent().find("ul").hide();
                if(show) {
                    obj.hide();
                }
                else{
                    obj.show();
                    //jQuery(this).next().slideToggle('medium');
                }
            });
            
            jQuery(this).mouseover(function(){
                if(obj.is(":visible")) {
                   obj.show();
                }
            });
            
            jQuery(this).mouseleave(function(){
                if(jQuery(this).attr("num") == "1") {
                   obj.show();
                }
                else if(obj.is(":visible")) {
                   obj.show();
                }
            });
            
         });
	   }
	   //隐藏所有的查询条件
	   function hiddenUl() {
           jQuery(".cssmenu>ul>li>a").each(function(){
               jQuery(this).next().hide();
           });
       }