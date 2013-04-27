       //获取内容展现对象
       function getContentObject() {
           var contentId = "containerList";
           var currentShowType = jQuery("input[name='showType']").val();
           if(currentShowType == "V") {
               contentId = "vertical";
           }
           return jQuery("#"+contentId)
       }
       
       //统计tag
       function statisticTag() {
           jQuery.ajax({
	           url:"/service/content/personal/statistic/tags",
	           type:"get",
	           headers:token,
	           success:function(resp) {
	               var collect = resp.Collect;
	               if(collect != null) {
	                   jQuery("#favoriteTotalNum").html(collect.quantity);
	               }
	               var read = resp.Read;
	               if(read!=null) {
	                   jQuery("#readTotalNum").html(read.quantity);
	               }
	               var later = resp.Later;
	               if(later!=null) {
	                   jQuery("#laterTotalNum").html(later.quantity);
	               }
	               var friend = resp.Source_Friend;
	               if(friend!=null) {
	                   jQuery("#friendTotalNum").html(friend.quantity);
	               }
	           }
	       });
       }
       
       //切换排序
       function switchSortType(sortType,sortName) {
           jQuery("input[name='sortType']").attr("sortType",sortType);
           getDataList('refresh');
           jQuery("#sortTypeId a[href=#] span").html(sortName);
       }
       
       //切换展现类型
       function switchShowType(showType) {
           var currentShowType = jQuery("input[name='showType']").val();
           if(showType == currentShowType) {
              return ;
           }
           
           if(showType == null || showType == "")showType = currentShowType;
           var contentId = "containerList";
           if(showType == "H") {
               contentId = "containerList";
           }
           else if(showType == "V") {
               contentId = "vertical";
           }
           getContentObject().attr("id",contentId);
           jQuery("input[name='showType']").attr("value",showType);
           
           getContentObject().children().each(function(i){
               jQuery(this).attr("class","panel");
               if(showType == "H") {//横向排列
                   swithH(jQuery(this),i);
               }
               else if(showType == "V") {//纵向排列
                   if(i%3 == 1)jQuery(this).attr("class","panel panel-middle");
                   swithV(jQuery(this),i);
               }
           });
       }
       
       //横向排列
       function swithH(dataObj,index) {
           jQuery(dataObj).find(".panel-info").show();
           jQuery(dataObj).find(".panel-content").show();
           jQuery(dataObj).find(".from").show();
       }
       //纵向排列
       function swithV(dataObj,index) {
           jQuery(dataObj).find(".panel-info").hide();
           jQuery(dataObj).find(".panel-content").hide();
           jQuery(dataObj).find(".from").hide();
       }
       
       //获取数据列表
	   function getDataList(type) {
	       layer_load(20);//设置加载层最大时间20秒
	       
	       initPara(type);//初始化参数
	       var token = cookieUtil.getHeaderToken();
	       var matrix = getMatrixPara();
	       var param = getQueryPara();

	       jQuery.ajax({
	           url:"/service/content/personal/search"+matrix,
	           type:"get",
	           data:param,
	           contentType:"application/json;charset=utf-8",
	           headers:token,
	           success:function(resp) {
	               setCountValue(resp);
	               loadItemList(resp,type);
	               switchShowType("");
	           },
	           complete:function() {
	               try{
	                  layer_close();
	               }catch(e){
	               }
	           }
	       }
	       );
	   }
	   
	   //点击上一页、下一页操作按钮动作
	   function onPage(pageType) {
	       var currentPage = parseInt(jQuery("input[name='currentPage']").val());
	       if(pageType =='next') {
	           jQuery("input[name='currentPage']").attr("value",currentPage+1);
	       }
	       else {
	           jQuery("input[name='currentPage']").attr("value",currentPage-1);
	       }
	       jQuery("#pageNum").html(jQuery("input[name='currentPage']").val());	       
	       jQuery("input[name='scroll']").attr("value","false");
	       getDataList("newPage");
	       //jQuery("input[name='scroll']").attr("value","true");
	   }
	   
	   //获取query参数
	   function getQueryPara() {
	       var param = new Array();
	       
	       var offset = jQuery("input[name='offset']").val();
	       if(offset == null)offset = 0;
	       var limit = jQuery("input[name='limit']").val();
	       if(limit == null)limit = 20;
	       
	       param.push({name: "offset", value: offset});
		   param.push({name: "limit",  value: limit});
	       
	       return param;
	   }
	   
	   //获取Matrix参数
	   function getMatrixPara() {
	       //关键字
	       var keyword = jQuery("input[name='keyword']").val();
	       if(keyword == null)keyword = "";
	       keyword = encodeURI(keyword);
	       //系统标签
	       var tag = jQuery("input[name='tag']").val();
	       if(tag == null)tag = "";
	       //内容类型系统标签
	       var contentType = jQuery("input[name='contentType']").val();
	       if(contentType != null && contentType.length > 0)tag = tag + "," +contentType;
	       //搜索来源系统标签
	       var contentSource = jQuery("input[name='contentSource']").val();
	       if(contentSource != null && contentSource.length > 0)tag = tag + "," + contentSource;
	       //我推荐、我评论等系统标签
	       var readCond = jQuery("input[name='readCond']").val();
	       if(readCond != null && readCond.length > 0)tag = tag + "," + readCond;
	       
	       //搜索好友
	       var friend = jQuery("input[name='friendFilter']").val();
	       if (friend == null)
	    	   friend = "";
	       if (friend != null && friend != "")
	    	   friend = encodeURI(friend);
	       
	       //用户标签
	       var userTag = jQuery("input[name='userTag']").val();
	       if(userTag == null)userTag = "";
	      
	       //排序
	       var sortType = jQuery("input[name='sortType']").val();
	       if(sortType == null)sortType = "";
	       
	       var matrix = ";";
	       //系统tag
	       if(jQuery.trim(tag)!=''){
	           matrix = matrix + "tag="+tag+";";
	       }
	       //用户tag
	       if(jQuery.trim(userTag)!=''){
	           matrix = matrix + "userTag="+userTag+";";
	       }
	       if(jQuery.trim(keyword)!=''){
	           matrix = matrix + "keyword="+keyword+";";
	       }
	       if(jQuery.trim(sortType)!=''){
	           matrix = matrix + "order="+sortType+";";
	       }
	       if(jQuery.trim(friend)!=''){
	           matrix = matrix + "friend="+friend+";";
	       }
	       return matrix;
	   }
	   
	   //加载列表
	   function loadItemList(resp,type) {
	       if(type == "refresh" || type =="newPage")getContentObject().empty();
	       var html = jQuery("#itemTemplate").html();
	       var total = resp.total;
	       var results = resp.result;
	       for(var i=0;i<results.length;i++) {
               var data = results[i];
               var rehtml = replaceDiv(html,data);
               //alert(rehtml);
               var loadDataSize = parseInt(getContentObject().children().size());
               var maxSize = parseInt(jQuery("input[name='itemsPerPage']").val());
               if(loadDataSize >= maxSize){
                   jQuery("input[name='isLastPage']").attr("value","false");
                   break;
               }
               getContentObject().append(rehtml);
           }     
           getContentObject().children("strong").remove();   
           var loadDataSize = parseInt(getContentObject().children().size());
           //alert("loadDataSize:"+loadDataSize);
	       //alert(jQuery("#content-right").html());               
	   }
	   
	   //设置总记录数
	   function setCountValue(resp) {
	       if(resp.lastPage) {
	           jQuery("input[name='isLastPage']").attr("value","true");
	       }
	       var total = resp.total;
	       
	       jQuery("#numCount").html(total);
	   }
	   
	   //初始化相关参数
      function initPara(type) {
            if(jQuery("input[name='scroll']").val() == "true") {
                var currentPage = parseInt(jQuery("input[name='currentPage']").val())-1;
                var limit = parseInt(jQuery("input[name='itemsPerPage']").val());
                var offset = (currentPage*limit) + getContentObject().children().size();
	            if(offset > 0)jQuery("input[name='offset']").attr("value",offset);
            }else {
                var currentPage = parseInt(jQuery("input[name='currentPage']").val())-1;
                var limit = parseInt(jQuery("input[name='itemsPerPage']").val());
                var offset = (currentPage*limit);
                jQuery("input[name='offset']").attr("value",offset);
            }
            
	        //重新查询或者翻页
	        if(type =="refresh" || type =="newPage") {
	            //重新设置可用
	            getContentObject().attr('scrollPagination', 'enabled');
	            //
	            jQuery("input[name='isLastPage']").attr("value","false");
	        }
	        if(type =="refresh") {
	            //从0开始
	            jQuery("input[name='offset']").attr("value",0);
	            jQuery("input[name='currentPage']").attr("value","1");
	            jQuery("#pageNum").html("1");       
	            $("#pageNav").hide();
	        }     
      }
	   
	   //减少或增加收藏数
	   function redureOrAddCountValue(num,action) {
	       try{
	          parseInt(num);
	       }catch(e) {
	          return;
	       }
	       var oldNum = jQuery("#numCount").html();
	       try{
	          oldNum = parseInt(oldNum);
	       }catch(e) {
	          oldNum = 0;
	       }
	       oldNum = oldNum + num;
	       if(oldNum < 0)oldNum=0;
	       jQuery("#numCount").html(oldNum);
	       
	       setLeftCount(num,action);
	   }
	   //设置左侧统计数
	   function setLeftCount(num,action) {
	       var menuType = jQuery("input[name=menuType]").val();
	       if(action == "collect") {//收藏成功时候，更新统计数量
	           var newNum = parseInt(jQuery("#favoriteTotalNum").html());
	           newNum = newNum + Math.abs(num);
	           jQuery("#favoriteTotalNum").html(newNum);
	           
	           if(menuType =="later") {
	               newNum = parseInt(jQuery("#laterTotalNum").html());
	               newNum = newNum - Math.abs(num);
	               jQuery("#laterTotalNum").html(newNum);
	           }
	           newNum = parseInt(jQuery("#readTotalNum").html());
	           newNum = newNum + Math.abs(num);
	           jQuery("#readTotalNum").html(newNum);
	       }
	       else if(action == "later") {//稍后看
	           var newNum = parseInt(jQuery("#laterTotalNum").html());
	           newNum = newNum + Math.abs(num);
	           jQuery("#laterTotalNum").html(newNum);
	       }
	       else if(action == "uncollect") {//取消收藏
	           var newNum = parseInt(jQuery("#favoriteTotalNum").html());
	           newNum = newNum - Math.abs(num);
	           jQuery("#favoriteTotalNum").html(newNum);
	       }
	   }
	   
	   //替换模板
	   function replaceDiv(html,data) {
	       if(html==null)return "";
	       var title = data.content.title;
	       if(title == null)title = "图片";
	       html = html.replace("%title%",title);
	       var image = data.imageUrl;
	       if(image!="") {
	           image = "<img src=\""+image+"\" />";
	       }
	       html = html.replace("%image%",image);
	       html = html.replace("%date%",data.date_i18n);
	       var summary = data.content.summary;
	       if(summary == null)summary = "";
	       html = html.replace("%summary%",summary);
	       html = html.replace(/\%url\%/g,data.content.url);
	       html = html.replace(/\%repositoryId\%/g,data.repository.id);
	       html = html.replace(/\%contentId\%/g,data.repository.contentId);
	       html = html.replace(/\%sourceType\%/g,data.type);
	       html = html.replace(/\%collect\%/g,data.content.counter.collect);
	       html = html.replace("%comment%",data.content.counter.comment);
	       html = html.replace("%share%",data.content.counter.share);
	       html = html.replace("%visit%",data.content.counter.visit);
	       var userTag = "";
	       try{
	           userTag = data.repository.userTag.join(" ");
	           userTag = getUserTagHtml(userTag);
	       }catch(e){
	       }
	       html = html.replace(/\%tag\%/g,userTag);
	       
	       //替换好友分享
	       html = replaceFriendShare(html,data);
	       //未读首页，图片显示
	       html = replaceUnreadShowImg(html);
	       //替换按钮
	       replaceBtn(html,data.folders);
	       
	       //如果没有好友分享,去掉好友分享情况
	       if(data.from == null || data.from.length < 1) {
	           jQuery("#btnTemp #friendShareId").remove();
	       }
	       
	       html = jQuery("#btnTemp").html();
	       
	       return html;
	   }
	   
	   //获取userTag的html
	   function getUserTagHtml(userTagStr) {
	       var userTagHtml = "";
	       if(userTagStr == null || userTagStr =="")return userTagHtml;
	       var userTagStrs =  userTagStr.split(" ");
	       for(var i = 0;i<userTagStrs.length;i++) {
	           var userTag = userTagStrs[i];
	           if(jQuery.trim(userTag) == "")continue;
	           userTagHtml = userTagHtml + "<p><a>"+userTag+"</a></p>";
	       }
	       
	       return userTagHtml;
	   }
	   
	   //获取user Tag原有的对象
	   function getOldUserTagObject(repositoryId) {
	       return jQuery("#"+repositoryId+" #oldUserTag li");
	   }
	   
	   //获取user Tag原有的值
	   function getOldUserTagValue(repositoryId) {
	       var oldValue= "";
	       getOldUserTagObject(repositoryId).find("a").each(function() {
	           var tagValue = jQuery(this).html();
	           if(jQuery.trim(tagValue) == "")return;
	           oldValue = oldValue + tagValue + " ";
	       });
	       return oldValue;
	   }
	   
	   //设置user Tag原有的值
	   function setOldUserTagValue(repositoryId, newUserTag) {
	       var newUserTagHtml = getUserTagHtml(newUserTag);
	       getOldUserTagObject(repositoryId).empty();
	       getOldUserTagObject(repositoryId).html(newUserTagHtml);
	   }
	   
	   //替换朋友分享
	   function replaceFriendShare(html,data) {
	       var fromFriend = data.from;
	       if(fromFriend == null || fromFriend.length < 1) {
	           return html;
	       }
	       var firstFriend = fromFriend[0];
	       html = html.replace("%firstFriendName%",firstFriend.name);
	       html = html.replace("%firstFriendComment%",firstFriend.comment.text);
	       
	       var lastFriend = fromFriend[fromFriend.length-1];
	       html = html.replace("%lastFriendName%",lastFriend.name);
	       html = html.replace("%lastFriendComment%",lastFriend.comment.text);
	       return html;
	   }
	   
	   //替换未读首页图片显示
	   function replaceUnreadShowImg(html) {
	       var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
	       var src = "";
	       if(menuType == "later" ) {//稍后看
	           src = "/images/logo-national-flag.gif";
	       }
	       else if(menuType == "collect" ) {//收藏
	           src = "/images/logo-national-flag.gif";
	       }
	       else if(menuType == "friend" ) {//朋友分享
	           src = "/images/logo-national-flag.gif";
	       }
	       jQuery("#btnTemp").empty();
	       jQuery("#btnTemp").html(html);
	       
	       jQuery("#btnTemp #unreadShowImg").attr("src",src);
	       if(menuType != "unread") {
	           jQuery("#btnTemp #unreadShowImg").remove();
	       }
	       return jQuery("#btnTemp").html();
	   }
	   
	   //替换列表操作按钮
	   function replaceBtn(html,folders) {
	       jQuery("#btnTemp").empty();
	       jQuery("#btnTemp").html(html);
	       var btnHtml = "";
	       var menuType = jQuery("input[name=menuType]").val();
	       
	       if(menuType == "unread" || menuType == "") {//未读情况,需要获取具体每天记录的关系来源
	           menuType = getFolderType(folders);
	       }
	       
	       btnHtml = btnHtml + jQuery("#btnTemp div[id=laterBtn]").html();//稍后看
	       if(menuType == "favorite"){//收藏
	           btnHtml = btnHtml + jQuery("#btnTemp div[id=addTagBtn]").html();//打标签
	           btnHtml = btnHtml + jQuery("#btnTemp div[id=cancelFavoriteBtn]").html();//取消收藏 
	       }
	       else if(menuType == "later"){//稍后看
	           btnHtml = "";
	           if(getFolderType(folders) == "collect") {
	               //btnHtml = btnHtml + jQuery("#btnTemp div[id=addTagBtn]").html();//打标签
	               btnHtml = btnHtml + jQuery("#btnTemp div[id=cancelFavoriteBtn]").html();//取消收藏 
	           }else {
	               btnHtml = btnHtml + jQuery("#btnTemp div[id=favoriteBtn]").html();//收藏
	           } 
	       }
	       else if(menuType == "read"){//已读
	           btnHtml = btnHtml + jQuery("#btnTemp div[id=favoriteBtn]").html();//收藏
	       }
	       else if(menuType == "friend"){//朋友分享
	           btnHtml = btnHtml + jQuery("#btnTemp div[id=addTagBtn]").html();//打标签
	       }
	       else {
	           btnHtml = btnHtml + jQuery("#btnTemp div[id=favoriteBtn]").html();//收藏
	       }
	       btnHtml = btnHtml + jQuery("#btnTemp div[id=ShareFriendBtn]").html();//分享给朋友
	       //btnHtml = btnHtml + jQuery("#btnTemp div[id=snsBtn]").html();//分享到SNS
	       btnHtml = btnHtml + jQuery("#btnTemp div[id=commentBtn]").html();//评论
	       
	       jQuery("#btnTemp div[id=allBtn] ul").empty();
	       jQuery("#btnTemp div[id=allBtn] ul").html(btnHtml);
	   }
	   
	   //如果是未读列表情况,获取每天记录对应的内容来源
	   function getFolderType(folders) {
	       if(folders == null || folders.length < 1)return "";
	       var result = "";
	       for(var i=0;i < folders.length;i++) {
	           var folder = folders[i];
	           if(folder.indexOf("Delete") > -1)continue;
	           result = result + folder + ",";
	       }
	       if(result.indexOf("Source_Friend") > -1) {
	           result = "friend";
	       }
	       else if(result.indexOf("Collect") > -1) {
	           result = "collect";
	       }
	       else if(result.indexOf("Later") > -1) {
	           result = "later";
	       }
	       return result;
	   }
	   
	   //编辑tag对象
	   function getEditTagObject() {
	       return jQuery("#editTagId ul");
	   }
	   
	   //获取用户所有的标签列表
	   function getTagList(token) {
	       jQuery.ajax({
	           url:"/service/content/personal/tags",
	           type:"get",
	           headers:token,
	           success:function(resp){
	               getEditTagObject().html("");
	               var editTag = getEditTagObject();
	               var html = "<li>";
	               html = html + "<a id='a%tag%' href=\"javascript:queryTag('%tag%')\"><span>%tag%</span></a>";
	               html = html + "<p><img id='img%tag%' src='/images/close-pic.png' onclick=\"deleteUserTag('%tag%')\" /></p>";
	               html = html + "<span class='active-input'><input id='input%tag%' value='%tag%' size='4' /></span>";
	               html = html + "</li>";
	               if(resp.length < 1) {
	                   getEditTagObject().append("<span align=\"right\">亲，你还没有打过标签，去给收藏的内容打标签吧！\n可以方便查看收藏哦！</span>");
	                   return;
	               }
	               var next = 4;
	               for(var i=0;i<resp.length;i++) {
	                   var data = resp[i];
	                   if(i !=0 && i%next==0) {
	                       //jQuery("#selectTag").append("<br><br>");
	                   }
	                   editTag.append(html.replace(/\%tag\%/g,data.name));
	                   jQuery("#img"+data.name).hide();
	                   jQuery("#input"+data.name).hide();  
	               }
	               html = "<li class='label-button'><input id='selectTagBtn' type='image' src='../zh_CN/images/label.png'  onclick='editTagList()'></li>";
	               editTag.append(html);
	           }
	       });
	   }
	   
	   //编辑标签列表
	   function editTagList() {
	       getEditTagObject().find("img[id^=img]").show();
	       getEditTagObject().find("a[id^=a]").each(function(){
	           var id = jQuery(this).attr("id");
	           var tag = id.substring(1,id.length);
	           jQuery(this).attr("href","javascript:editTag('"+tag+"')");
	       });
	   }
	   //编辑标签
	   function editTag(tag) {
	       getEditTagObject().find("a[id=a"+tag+"]").hide();
	       getEditTagObject().find("img[id=img"+tag+"]").hide();
	       getEditTagObject().find("input[id=input"+tag+"]").show();
	       getEditTagObject().find("#selectTagBtn").attr("src","../zh_CN/images/confirm-3.png");
	       getEditTagObject().find("#selectTagBtn").attr("onclick","updateUserTag()");
	   }
	   
	   //删除用户标签
	   function deleteUserTag(userTag) {
	       if(confirm('删除标签不会删除标签下对应的内容，是否删除？')) {
	           jQuery.ajax({
	               url:"/service/content/personal/tags/"+userTag,
	               type:"delete",
	               headers:token,
	               success:function(resp){
	                   //getDataList("refresh");
	                   removeContentUserTag(userTag);
	                   getEditTagObject().find("a[id=a"+userTag+"]").remove();
	                   getEditTagObject().find("img[id=img"+userTag+"]").remove();
	                   getEditTagObject().find("input[id=input"+userTag+"]").remove();
	                   alert("亲，恭喜您，删除标签成功！");
	               }
	           });
	       }
	   }
	   
	   //更新用户标签
	   function updateUserTag() {
	       var oldUserTag = "";
	       var newUserTag = "";
	       var data = new Array();
	       getEditTagObject().find("input[id^=input]").each(function(){
	           var id = jQuery(this).attr("id");
	           id = id.substring(5, id.length);
	           var value = jQuery.trim(jQuery(this).attr("value"));
	           if(id == value) {//标签没有变化
	               return;
	           }
	           if(value == null || jQuery.trim(value)==''){
	               return;
	           }
	           oldUserTag = oldUserTag + id + ",";
	           newUserTag = newUserTag + value+ ",";
	           
	           var tagObj = {
	               name:id,
	               newName:value
	           };
	           data[data.length] = tagObj;
	       });
	       
	       if(oldUserTag == '' || newUserTag == '') {
	           resetShowOrHide();
	           return;
	       }

		  jQuery.ajax({
	               url:"/service/content/personal/tags",
	               type:"post",
	               contentType:"application/json;charset=utf-8",
	               data:JSON.stringify(data),
	               dataType:"json",
	               headers:token,
	               complete:function(resp){
	                   var jsonData = jQuery.parseJSON(resp.responseText)
	                   var filterResult = filterErrorTag(jsonData,oldUserTag,newUserTag);
	                   if(filterResult =="" || filterResult.indexOf(";") < 0) {
	                      oldUserTag = "";
	                      newUserTag = "";
	                   }
	                   else {
	                       var filterResults = filterResult.split(";");
	                       oldUserTag = filterResults[0];
	                       newUserTag = filterResults[1];
	                   }
	                   updateTagPage(oldUserTag,newUserTag);
	                   refreshContentUserTag(oldUserTag,newUserTag);
	                   resetShowOrHide();
	                   if(jsonData == null || jsonData.length< 1) {
	                       alert("亲，恭喜您更新标签成功！");
	                   }
	               }
	      }); 
	   }
	   
	   //过滤修改失败的tag 过滤掉,和;
	   function filterErrorTag(errorTagList,oldUserTag,newUserTag) {
	       var filterResult = "";
	       if(errorTagList == null || errorTagList.length < 1) {
	           filterResult = oldUserTag + ";" + newUserTag;
	           return filterResult;
	       }
	       
	       var errorMessage = "";
	       var oldUserTags = oldUserTag.split(",");
	       var newUserTags = newUserTag.split(",");
	       oldUserTag = "";
	       newUserTag = "";
	       for(var i =0; i<oldUserTags.length;i++) {
	           var oldTag = oldUserTags[i];
	           var newTag = newUserTags[i];
	           if(oldTag =="")continue;
	           
	           var isError = false;
	           for(var j=0;j<errorTagList.length;j++) {
	                var errorTag = errorTagList[j];
	                if(errorTag == null)continue;
	                if(errorTag.name == oldTag) {
	                    isError = true;
	                    errorMessage = errorMessage + "修改标签["+errorTag.name+"]失败," +errorTag.message + "\n";
	                    break;
	                }
	           }
	           
	           if(!isError) {
	               oldUserTag = oldUserTag + oldTag +",";
	               newUserTag = newUserTag + newTag +",";
	           } 
	       }
	       
	       filterResult = oldUserTag + ";" + newUserTag;
	       if(errorMessage !="")alert(errorMessage);
	       return filterResult;
	   }
	   
	   //更新标签页面
	   function updateTagPage(oldUserTag,newUserTag) {
	       var oldUserTags = oldUserTag.split(",");
	       var newUserTags = newUserTag.split(",");
	       for(var i =0; i<oldUserTags.length;i++) {
	           var oldTag = oldUserTags[i];
	           var newTag = newUserTags[i];
	           if(jQuery.trim(oldTag) =='' || jQuery.trim(newTag) =='') {
	               continue;
	           }
	           getEditTagObject().find("a[id=a"+oldTag+"]").attr("href","javascript:queryTag('"+newTag+"')");
	           getEditTagObject().find("a[id=a"+oldTag+"]").html(newTag);
	           getEditTagObject().find("a[id=a"+oldTag+"]").attr("id","a"+newTag);
	           getEditTagObject().find("img[id=img"+oldTag+"]").attr("onclick","deleteUserTag('"+newTag+"')");
	           getEditTagObject().find("img[id=img"+oldTag+"]").attr("id","img"+newTag);
	           getEditTagObject().find("input[id=input"+oldTag+"]").attr("value",newTag);
	           getEditTagObject().find("input[id=input"+oldTag+"]").attr("id","input"+newTag);
	       }
	   }
	   
	   //重新设置显示或隐藏
	   function resetShowOrHide() {
	       getEditTagObject().find("a[id^=a]").show();
	       getEditTagObject().find("a[id^=a]").each(function(){
	           jQuery(this).show();
	           var id = jQuery(this).attr("id");
	           var tag = id.substring(1,id.length);
	           jQuery(this).attr("href","javascript:queryTag('"+tag+"')");
	       });
	       getEditTagObject().find("img[id^=img]").hide();
	       getEditTagObject().find("input[id^=input]").hide();
	       getEditTagObject().find("#selectTagBtn").attr("src","../zh_CN/images/label.png");
	       getEditTagObject().find("#selectTagBtn").attr("onclick","editTagList()");
	   }
	   
	   //
	   function getContentOldUserTagObject() {
	       return getContentObject().find("div[id=oldUserTag] p a");
	   }
	   
	   //刷新内容对应的用户标签
	   function refreshContentUserTag(oldUserTag,newUserTag) {
	       var oldUserTags = oldUserTag.split(",");
	       var newUserTags = newUserTag.split(",");
	       for(var i =0; i<oldUserTags.length;i++) {
	           var oldTag = jQuery.trim(oldUserTags[i]);
	           var newTag = jQuery.trim(newUserTags[i]);
	           if(oldTag =='' || newTag =='') {
	               continue;
	           }

	           getContentOldUserTagObject().each(function(){
	               var userTag = jQuery.trim(jQuery(this).html());
	               if(userTag != oldTag) {
	                   return;
	               }
	               jQuery(this).html(newTag);
	          });
	       }
	   }
	   
	   //删除内容对应的用户标签
	   function removeContentUserTag(removeUserTag) {
	       getContentOldUserTagObject().each(function(){
	               var userTag = jQuery.trim(jQuery(this).html());
	               if(userTag != removeUserTag) {
	                   return;
	               }
	               jQuery(this).remove();
	       });
	   }
	   
	   //打开选择标签层
	   function openQueryTagLayer() {
	       $.layer({        
              v_title:"选择标签",
              v_istitle:false,
              v_btns : 0,
              v_box : 1,
              v_dom : '#selectTag',        //id
              v_area : ['300px','300px'],
              v_move:true
           });
	   }
	   
	   //通过标签内容查询
	   function queryTag(tag) {
	       try{
	           layer_close();
	       }catch(e) {
	       }
	       if(tag != "") {
	           jQuery("#editTagId a[href=#] span").html(tag);
	       }
           jQuery("input[name='userTag']").attr("value",tag);
           jQuery("#selectedTag").html(tag);
           getDataList("refresh");
           //jQuery("input[name='userTag']").attr("value","");
	   }
	   
	   //打标签对象
	   function getAddTagObject() {
	       return jQuery("#addTag");
	   }
	   //打标签输入框对象
	   function getAddTagInputObject() {
	       return getAddTagObject().find("input[name='userTagValue']");
	   }
	   //打标签按钮对象
	   function getAddTagButtonObject() {
	       return getAddTagObject().find("input[type='image']");
	   }
	   
	   //打标签
	   function openAddTagLayer(repositoryId,contentId) {
	    //设置文章热门标签
	    setContentHotTag(contentId);
	    //设置我常用的标签
	    setMyUseTag();
	    
	    //用户原有打的标签值
	    var tag = getOldUserTagValue(repositoryId);
	    //设置原有用户打的标签值
	    getAddTagInputObject().attr("value",tag);
	    //设置打标签动作按钮name为userTagValue
	    getAddTagButtonObject().attr("name",repositoryId);
	    $.layer({        
          v_title:"打标签",
          v_istitle:false,
          v_showclose : false,
          //v_skin:0,
          v_shade:true,
          v_box : 1,
          v_dom : '#addTag #pop-up',        //id
          v_area : ['300px','300px'],
          v_btns : 0,
          v_btn  :["保存"],
          v_move:false
        });
      }
      
      //设置我常用标签
      function setMyUseTag() {
          var size = jQuery("#myUseTagId p").children().size();
          if(size >0)return;
          
          var myUseTag = jQuery("input[name=myUseTag]").val();
          if(jQuery.trim(myUseTag) =="") {
              return ;
          }
          var myUseTags = myUseTag.split(" ");
          for(var i=0;i<myUseTags.length;i++) {
              var useTag = myUseTags[i];
              if(jQuery.trim(useTag) =="")continue;
              var useTagHtml = "<p><a href=\"javascript:setUserTagValue('"+useTag+"')\">"+useTag+"</a></p>";
              jQuery("#myUseTagId").append(useTagHtml);
          }
      }
      
      //设置用户输入标签值
      function setUserTagValue(userTag) {
          var userTagValue = trimUserTagValue();
          if(isSelectedTag(userTag))return;
          
          if(userTagValue.split(" ").length >=3) {
              alert("最多只能选择3个标签哦！");
              return ;
          }
          getAddTagInputObject().attr("value",userTagValue+" "+userTag);
      }
      
      //去掉用户输入标签多余的空格
      function trimUserTagValue() {
          var userTagValue = getAddTagInputObject().attr("value");
          userTagValue = jQuery.trim(userTagValue);
          var userTagValues = userTagValue.split(" ");
          
          var trimUserTag = "";
          for(var i=0;i<userTagValues.length;i++) {
              var userTag = userTagValues[i];
              userTag = jQuery.trim(userTag);
              if(userTag == "")continue;
              trimUserTag = trimUserTag + userTag + " ";
          }
          trimUserTag = jQuery.trim(trimUserTag);
          return trimUserTag;
      }
      
      //判断tag是否已经被选择
      function isSelectedTag(userTag) {
          userTag = jQuery.trim(userTag);
          var userTagValue = trimUserTagValue();
          if(userTagValue =="") return false;
          var userTagValues = userTagValue.split(" ");
          for(var i=0;i<userTagValues.length;i++) {
              var tag = userTagValues[i];
              if(tag == userTag) {
                  return true;
              }
          }
          return false;
      }
      
      //设置文章热门标签
      function setContentHotTag(contentId) {
          jQuery.ajax({
              url:"/service/content/archive/"+contentId+"/tags/hot",
              type:"get",
              contentType:"application/json",
              success:function(resp){
                 jQuery("#contentHotTag p").empty();
                 for(var i=0;i<resp.length;i++) {
                     var data = resp[i];
                     var hotTag = data.tag;
                     var hotTagHtml = "<p><a href=\"javascript:setUserTagValue('"+hotTag+"')\">"+hotTag+"</a></p>";
                     jQuery("#contentHotTag").append(hotTagHtml);
                 }
              }
          }); 
      }
      
      //校验用户添加的标签值
      function checkUserTagUser() {
          var tag = trimUserTagValue();
          if(tag == '') {
              alert("亲，请输入您挚爱的标签！");
              return false;
          }
          var tags = tag.split(" ");
          if(tags.length > 3) {
              alert("亲，标签个数最多只能3个哦！");
              return false;
          }
          for(var i=0;i<tags.length;i++) {
             var userTag = tags[i];
             if(userTag.length > 12) {
                 alert("亲，每个标签不能超过12个字符哦！");
                 return false;
             }
          }
          var existTag = "";
          for(var i=0;i<tags.length;i++) {
             var userTag = " " + tags[i] + " ";
             if(existTag.indexOf(userTag) > -1) {
                 alert("亲，不能输入重复的标签哦！");
                 return false;
             }
             existTag = existTag + userTag;
          }
          return true;
      }
      
      //添加标签动作
      function addTag() {
          var repositoryId = getAddTagButtonObject().attr("name");
          if(repositoryId == null) {
              var butObj = getAddTagButtonObject()[0];
              repositoryId = jQuery(butObj).attr("name");
          }
          
          if(!checkUserTagUser()) {
              return;
          }
          
          //用户打的标签
          var tag = trimUserTagValue();
          var tags = tag.split(" ");
    
          var data = {
			  userTag:tags	
		  };
          jQuery.ajax({
              url:"/service/content/personal/item/"+repositoryId,
              type:"put",
              contentType:"application/json",
              data:JSON.stringify(data),
              headers:token,
              success:function(){
                 setOldUserTagValue(repositoryId,tag);
                 getTagList(token);
                 layer_close();
              }
          });   
      }
      
      function selectFriend(id) {
    	  var selected = $("#fck_" + id).val();
    	  var selectedFriends = $("#friendSelected").val();
    	  var repeatSelected = $("#repeatSelected").val();
    	  var shared = $("#fch_" + id).val();
    	  if (selected == "0") {
    		  $("#fimg_" + id).attr("src", "/images/plug-in-pic-d1.png");
    		  $("#fck_" + id).val("1");
    		  selectedFriends += id + ",";
    		  if (shared != "") {
    			  repeatSelected += shared + ",";
    		  }
    	  }
    	  else {
    		  $("#fimg_" + id).attr("src", "/images/plug-in-pic-d.png");
    		  $("#fck_" + id).val("0");
    		  selectedFriends = selectedFriends.replace(id + ",", "");
    		  if (shared != "") {
    			  repeatSelected = repeatSelected.replace(shared + ",", "");
    		  }
    	  }
    	  
    	  $("#friendSelected").val(selectedFriends);
    	  $("#repeatSelected").val(repeatSelected);
      }
      
     //分享给朋友
	  function openShareFriendLayer(repositoryId, contentId) {
		  $('#loading').fadeIn();
		  $.get('/friend/myFriends', {'contentId':contentId}, function(data) {
			  $("#shareFriend").html(data);
			  
			  $('#loading').fadeOut();
			  if ($("#shareFriend").find("#friendCount").val() == "0") {
				  return;
			  }
			  
			  $("#selectStar").raty({
		            path : "/images",
		            hints   : ['2','4','6','8','10'],
		            half       : true,
		            precision  : true,
		            starHalf   : 'star-half-big.png',
		            starOff    : 'star-off-big.png',
		            starOn     : 'star-on-big.png',
		            targetType : 'number',
		            width: 200,
		            mouseover : function(score, evt) {
		                var s = 0;
		                if (score != null && score != NaN && score != undefined) {
		                	s = Math.round(score * 2);
		                }
		                
		                $(this).attr("score", s);
		            }
		      });
			  
			  $("#shareFriend").find("#btnCommend").click(function() {
				  var score = $("#selectStar").attr("score");
				  var comment = $("#commentText").val();
				  var selected = $("#friendSelected").val();
				  var repeatSelected = $("#repeatSelected").val();
				  if (selected.length < 1) {
					  alert("必须选择好友");
					  return;
				  }
				  
				  if (repeatSelected != "") {
					  if (!confirm("部分选择的好友已经分享过了，是否再次分享？")) {
						  var strs = repeatSelected.split(",");
						  for (var i=0; i<strs.length; i++) {
							  var str = strs[i];
							  if (str == "")
								  continue;
							  selected = selected.replace(str + ",", "");
						  }
						  if (selected == "") {
							  alert("当前好友已分享。");
							  layer_close();
							  return;
						  }
					  }
				  }
				  
				  $.post("/friend/share", {rid:repositoryId,friends:selected,grade:score,commend:comment}, function(data) {
					  if (data == "0") {
						  alert("分享失败。");
						  return;
					  }
					  else if (data == "1") {
						  alert("分享成功。");
						  layer_close();
					  }
				  });
				  
				  var param = {
				   		score:score,
				   		text:comment
				  };
				  $.ajax({
			           url:"/service/comment/"+contentId,
			           type:"post",
			           data:JSON.stringify(param),
			           datatype:"application/json",
			           contentType:"application/json;charset=utf-8",
			           headers:token,
			           success:function(resp) {
			               return;
			           },
			           complete:function() {
			               return;
			           }
			       });
			  });
			  $.layer({        
				  v_title:"分享给朋友",
				  v_istitle:false,
				  //v_skin:0,
				  v_shade:true,
				  v_box : 1,
				  v_dom : '#shareFriend',        //id
				  v_area : ['300px','300px'],
				  v_btns : 0,
				  v_btn  :["保存"],
				  v_move:false
			  });
		  });
     }
      
      //选择内容类型
      function openContentTypeLayer() {
          $.layer({        
          v_title:"内容类型",
          v_istitle:false,
          v_btns : 0,
          v_box : 1,
          v_dom : '#contentType',        //id
          v_area : ['300px','50px'],
          v_move:true
        });
      }
    //选择朋友
      function openFriendLayer() {
    	  $.layer({        
	          v_title:"朋友",
	          v_istitle:false,
	          v_btns : 0,
	          v_box : 1,
	          v_dom : '#friends',        //id
	          v_area : ['300px','50px'],
	          v_move:true
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
    //通过内容类型查询
      function queryFriend(id, name) {
          layer_close();
          jQuery("input[name='friendFilter']").attr("value",id);
          jQuery("#selectedFriend").html(name);
          getDataList("refresh");
          //jQuery("input[name='contentType']").attr("value","");
      }
      //选择内容来源
      function openContentSourceLayer() {
          $.layer({        
          v_title:"内容类型",
          v_istitle:false,
          v_btns : 0,
          v_box : 1,
          v_dom : '#contentSource',        //id
          v_area : ['450px','30px'],
          v_move:true
        });
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
      
      //通过已读条件查询
      function queryReadCond(readCond) {
          jQuery("input[name='readCond']").attr("value",readCond);
          getDataList("refresh");
          jQuery("input[name='readCond']").attr("value","");
      }
      
      //打开详情页面
      function openItemDetail(repositoryId,url,type) {
          var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
          var open_url = "/private/item?id="+repositoryId+"&menuType="+menuType;
          if(type == "Type_Video") {//视频情况,直接打开原网页
              open_url = "/private/url?url="+url;
          }
          window.open(open_url);
      }
      
      //打开url
      function openUrl(url) {
          window.open("/search/personal/url?url="+url);
      }
      
      //收藏内容收藏
	   function favorite(repositoryId,menuType) {
	      var data = {
			  tag:["Collect"],
			  menuType:menuType
		  };
	      doContent(repositoryId,data,"collect");
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
	               if(status == 204) num = 0;
	               if(type == "collect") {//收藏
	                   if(menuType == "later") {
	                       jQuery("#"+repositoryId).remove();
	                   }
	                   redureOrAddCountValue(num,"collect");
	               }
	               else if(type == "uncollect") {//取消收藏
	                   jQuery("#"+repositoryId).remove();
	                   redureOrAddCountValue(num,"uncollect");
	               }
	               else if(type == "later") {//稍后看
	                   redureOrAddCountValue(num,"later");
	               }
	               if(type !="read") {
	                   //弹出提示消息
	                   openMessage(type);
	               }
	           }
	       });
	   }
	   
	   function openDetailComment(contentId){
		   var obj = $(".comment_layer");
		   obj.attr("contentId", contentId);
		}
		
		//获取详情页面对象
	   function getDetailObject() {
		  return jQuery("#detail");
	   }
	   
	   //打开详情页
	   function openDetail(repositoryId) {
	       jQuery.ajax({
              url:"/service/content/personal/item/"+repositoryId,
              type:"get",
              headers:token,
              success:function(resp){
                  read(repositoryId);
                  if(resp.from == null) {
                      jQuery("#detailTemplate #left-detail").hide();
                  }else {
                      jQuery("#detailTemplate #left-detail").show();
                  }
                  var html = jQuery("#detailTemplate").html();
                  html = replaceDetailDiv(html, resp);
                  getDetailObject().empty();
                  getDetailObject().html(html);
                  if(resp.type == "Type_Image") {//图片
	                   //getDetailObject().find(".panel-content").hide();
	                   getDetailObject().find(".price-button").hide();
	              }else if(resp.type =="Type_Product") {//商品
	                   //getDetailObject().find(".panel-content").hide();
	              }
	              else {//正文或片段
	                   getDetailObject().find(".commodity-pic").hide();
	                   getDetailObject().find(".price-button").hide();
	              }
                  
                  openDetailComment(resp.id);
                  
                  openDetailLayer();
              }
           });   
	   }
	   
	   //替换详情模板
	   function replaceDetailDiv(html,data) {
	       if(html==null)return "";
	       var title = data.title;
	       if(title == null)title = "图片";
	       html = html.replace("%title%",title);
	       var image = data.imageUrl;
	       if(data.type =="Type_Product") {
	           //image = data.productPayload.picture;
	       }
	       else if(data.type =="Type_Image") {
	           //image = data.archiveUrl;
	       }
	       
	       if(image == null)image = "";

	       html = html.replace("%image%",image);
	       html = html.replace("%date%",data.date_i18n);
	       html = html.replace(/\%url\%/g,data.url);
	       html = html.replace(/\%contentId\%/g,data.id);
	       html = html.replace(/\%repositoryId\%/g,data.responseId);
	       var content = data.content;
	       var price = "";
	       if(data.type == "Type_Image") {//图片
	           content = "";
	       }else if(data.type =="Type_Product") {//商品
	           content = "";
	           price = data.productPayload.price;
	       }
	       content = content.replace(/\id="detail"/g,"");
	       html = html.replace("%price%",price);
	       html = html.replace("%content%",content);
	       
	       //最先分享
	       var firstFrom = data.from;
	       if(firstFrom !=null) {
	           html = html.replace("%firstFromName%",firstFrom.name);
	           html = html.replace("%firstFromComment%",firstFrom.comment);
	       }
	       
	       //操作按钮
	       replaceBtn(html,data.folders);
	       
	       html = jQuery("#btnTemp").html();
	       
	       html = html.replace(/\%collect\%/g,data.counter.collect);
	       html = html.replace("%comment%",data.counter.comment);
	       html = html.replace("%share%",data.counter.share);
	       //html = html.replace("%visit%",data.content.counter.visit);
	       var userTag = "";
	       try{
	           //userTag = data.repository.userTag.join("  ");
	       }catch(e){
	       }
	       //html = html.replace(/\%tag\%/g,userTag);
	       
	       return html;
	   }
	   
	    //打开详情页
	   function openDetailLayer() {
	       /*var height = $(window).height();
	       var width = $(window).width();
	       $.layer({        
              v_title : "内容详情",
              v_istitle : false,
              v_showclose : false,
              v_move:true,
              v_btns : 0,
              v_box : 1,
              v_shade : true,
              v_dom : '#detail',        //id
              v_area : ['600px','600px']
           });
           */
           var dwidth = window.screen.availWidth;
           dwidth = document.body.clientWidth;
           var dheight = window.screen.availHeight;
           dheight = document.body.clientHeight;
           
           var owidth = parseInt($("#detail").width());
           //owidth = $("#detail").clientWidth;
           var oheight = parseInt($("#detail").height());
           //oheight = $("#detail").clientHeight;
           
           var left = (dwidth - owidth)/2 ;//+ document.body.scrollLeft
           //var top = (document.body.scrollTop - oheight)/2 + document.body.scrollTop;
           var top = (document.body.scrollTop - dheight)/2;
           getDetailObject().css("top",top);
           getDetailObject().css("left",35);
           
           //var height = $("#"+repositoryId).height();
           //alert("height:" + height);//"position": "top","modal":true,
           getDetailObject().dialog({"autoOpen":true,"title":"title","draggable":true,"width":600,"height":600,"resizable":true});
	   }
	   
	   function popupDiv(div_id) {            
           var div_obj = $("#" + div_id);            //窗口宽度,高度           
           var winWidth = $(window).width();            
           var winHeight = $(window).height();            //弹出的div的宽度,高度            
           var popHeight = div_obj.height();            
           var popWidth = div_obj.width();            //(winWidth-popWidth) / 2 (winHeight-popHeight)/2
           div_obj.animate({ opacity: "show", left: 200, 
           top: 20,width:popWidth,height:popHeight}, 300);                  
      } 
      
      function closeDetail() {
          getDetailObject().dialog("close");
      }
	   
	   //弹出消息框
	   function openMessage(type) {
	       var template = jQuery("#messageTemplate");// .pop-up-later
	       var pop = jQuery("#messageId #pop-up");
	       var message = "";
	       var src = "";
	       if(type == "later") {//稍后看
	           src = "/images/pic-9.png";
	           message = "该内容已存入<strong>稍后看！</strong>";
	       }
	       else if(type == "collect") {//收藏
	           src = "/images/pic-10.png";
	           message = "该内容<strong>收藏成功！</strong>";
	       }
	       else if(type == "uncollect") {//取消收藏
	           src = "/images/pic-11.png";
	           message = "该内容已<strong>取消收藏！</strong>";
	       }
	       
	       var html = template.html();
	       html = html.replace("%message%",message);
	       pop.empty();
	       pop.html(html);
	       pop.find("img").attr("src",src);
	       $.layer({
	           v_istitle:false,
	           v_btns : 0,
	           v_box : 1,
	           v_showclose:false,
	           v_dom : '#messageId #pop-up',
	           v_showtime : 2,
	           v_area : ['450px','30px']
	       });
	   }