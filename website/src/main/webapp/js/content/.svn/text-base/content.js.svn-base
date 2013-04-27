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
               //jQuery(this).attr("class","panel");
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
	       //layer_load(20);//设置加载层最大时间20秒
	       
	       initPara(type);//初始化参数
	       var token = cookieUtil.getHeaderToken();
	       var matrix = getMatrixPara();//encodeURI();
	       var param = getQueryPara();

	       jQuery.ajax({
	           url:"/service/content/personal/search/"+matrix,
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
	                  //layer_close();
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
		   
		   var r=(Math.random()*99999999);
		   param.push({name: "r",  value: r});
	       
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
	       userTag = encodeURI(userTag);
	      
	       //排序
	       var sortType = jQuery("input[name='sortType']").val();
	       if(sortType == null)sortType = "";
	       
	       var matrix = ";";
	       //系统tag
	       if(jQuery.trim(tag)!=''){
	           //var index = tag.indexOf("_");
	           //if(index >-1)tag = tag.substring(0,index);
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
               var index = rehtml.toLowerCase().indexOf("<div ");
               rehtml = rehtml.substring(index);
               //window.console.log("rehtml:\n" + rehtml);
               var loadDataSize = parseInt(getContentObject().children().size());
               var maxSize = parseInt(jQuery("input[name='itemsPerPage']").val());
               if(loadDataSize >= maxSize){
                   jQuery("input[name='isLastPage']").attr("value","false");
                   break;
               }
               if(i ==10){
                   //window.console.log("rehtml:\n" + rehtml);
               }
               getContentObject().append(rehtml);
           }     
           getContentObject().children("strong").remove();   
           //如果列表为空，提示相关文案
           var loadNum = jQuery("input[name='loadNum']").val();
		   if(loadNum == "0") {
			   jQuery("input[name='loadNum']").attr("value","1");
			   if(getContentObject().children().size() <1) {
			      setFolderPrompt();
			   }
		   }          
	   }
	   
	   //设置总记录数
	   function setCountValue(resp) {
	       if(resp.lastPage) {
	           jQuery("input[name='isLastPage']").attr("value","true");
	       }
	       var total = resp.total;
	       
	       jQuery("#numCount").html(total);
	       if(getMenuType() == "read") {
	           jQuery("#numCount").hide();//历史记录暂时隐藏
	       }
	   }
	   
	   //初始化相关参数
      function initPara(type) {
            jQuery("input[name='offset']").attr("value",getOffset());
            
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
      
      function getOffset() {
          var offset = 0;
          if(jQuery("input[name='scroll']").val() == "true") {
                var currentPage = parseInt(jQuery("input[name='currentPage']").val())-1;
                var limit = parseInt(jQuery("input[name='itemsPerPage']").val());
                offset = (currentPage*limit) + getContentObject().children().size();
                //if(offset > 0)jQuery("input[name='offset']").attr("value",offset);
           }else {
                var currentPage = parseInt(jQuery("input[name='currentPage']").val())-1;
                var limit = parseInt(jQuery("input[name='itemsPerPage']").val());
                offset = (currentPage*limit);
           }
           return offset;
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
	       
	       var tempNum = num;
	       
	       var menuType = getMenuType();
	       if(menuType=="myrecommend" ) {
	           tempNum = 0;
	       }
	       if(menuType=="later" ) {
	           tempNum = 0;
	       }
	       if(menuType == "friend") {//好友推荐
	           if(action != "cancelFriendShare") {
	               tempNum = 0;
	           }
	       }
	       oldNum = oldNum + tempNum;
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
	               //newNum = parseInt(jQuery("#laterTotalNum").html());
	               //newNum = newNum - Math.abs(num);
	               //jQuery("#laterTotalNum").html(newNum);
	           }
	           newNum = parseInt(jQuery("#readTotalNum").html());
	           newNum = newNum + Math.abs(num);
	           jQuery("#readTotalNum").html(newNum);
	       }
	       else if(action == "later") {//稍后看
	           //var newNum = parseInt(jQuery("#laterTotalNum").html());
	           //newNum = newNum + Math.abs(num);
	           //jQuery("#laterTotalNum").html(newNum);
	       }
	       else if(action == "uncollect") {//取消收藏
	           var newNum = parseInt(jQuery("#favoriteTotalNum").html());
	           newNum = newNum - Math.abs(num);
	           jQuery("#favoriteTotalNum").html(newNum);
	       }
	       else if(action == "cancelFriendShare") {//取消朋友分享
	           var newNum = parseInt(jQuery("#friendTotalNum").html());
	           newNum = newNum - Math.abs(num);
	           jQuery("#friendTotalNum").html(newNum);
	       }
	   }
	   
	   //替换模板
	   function replaceDiv(html,data) {
	       if(html==null)return "";
	       var contentId = data.id;
	       var menuType = jQuery("input[name=menuType]").val();
	       //support public content,public content detail is get by responseId
	       if(menuType == "public") {
	           data.id = data.id + "_"+data.responseId;
	       }
	       var title = getTitle(data);
	       html = html.replace(/\%title\%/g,title);
	       var image = getImageUrl(data);
	       html = html.replace("%image%",image);
	       html = html.replace("%date%",data.date_i18n);
	       var summary = getSummary(data);
	       html = html.replace("%summary%",summary);
	       html = html.replace(/\%url\%/g,data.url);
	       html = html.replace(/\%repositoryId\%/g,data.id);//关系ID改为内容ID
	       html = html.replace(/\%contentId\%/g,contentId);
	       html = html.replace(/\%sourceType\%/g,data.type);
	       html = html.replace(/\%collect\%/g,data.counter.collect);
	       var price = "";
	       if(data.product !=null)price = data.product.price;
	       html = html.replace("%price%",price);
	       html = html.replace("%comment%",data.counter.comment);
	       html = html.replace("%share%",data.counter.share);
	       html = html.replace("%visit%",data.counter.visit);
	       var userTag = "";
	       try{
	           userTag = data.userTag.join(" ");
	           userTag = getUserTagHtml(userTag);
	       }catch(e){
	       }
	       html = html.replace(/\%tag\%/g,userTag);
	       
	       
	       //根据内容类型进行选择
	       html = selectContentType(html,data);
	       //替换好友分享
	       html = replaceFriendShare(html,data);
	       //未读首页，图片显示
	       html = replaceUnreadShowImg(html,data.folders);
	       //替换按钮
	       html = replaceBtn(html,data.folders);
	       //根据内容来源进行选择
	       html = selectContentSource(html,data);
	       
	       return html;
	   }
	   
	   //获取标题
	   function getTitle(data) {
	       var url = data.url;
	       var contentId = data.id;
	       var menuType = jQuery("input[name=menuType]").val();
	       var title = data.title;
	       
	       var stag = getFolderType(data.folders);
	       if(stag == "weibo") {
	           title = "来自新浪微博";
	       }
	       if(title == null || jQuery.trim(title).length<1) {
	           title = "无标题";
	       }
	       
	       var result = "<a href='/private/item?id="+contentId+"&menuType="+menuType+"'  onclick=\"javascript:openItemDetail('"+contentId+"','"+url+"','"+menuType+"');return false;\" >"+title+"</a>";
	       if(data.type == "Type_Web") {
	           if(data.web.content == null || jQuery.trim(data.web.content) == "") {//内容为空，网址
	               //result = "<a href='"+data.url+"' target='_blank'>"+title+"</a>";
	           }
	       }
	       return result;
	   }
	   
	   //获取摘要
	   function getSummary(data) {
	       var summary = data.summary;
	       if(summary == null)summary = "";
	       if(data.web != null) {
	           var attachs = data.web.attachment;
	           if(attachs !=null && attachs.length>0) {
	               imageUrl = attachs[0].url;
	               imageUrl = "<img src=\""+imageUrl+"\" />";
	               summary = imageUrl + summary;
	           }
	       }
	       if(data.type == "Type_Web") {
	           if(data.web.content == null || jQuery.trim(data.web.content) == "") {//内容为空，网址
	               summary = "<a href='"+data.url+"' target='_blank'>"+data.url+"</a>";
	           }
	       }
	       if(data.type == "Type_Video") {//视频
	           summary = "";
	       }
	       return summary;
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
	   
	   //获取user Tag原有的值
	   function getOldUserTagValue(repositoryId) {
	       var oldValue= "";
	       getOldUserTagObject(repositoryId).find("a").each(function() {
	           var tagValue = jQuery(this).html();
	           if(jQuery.trim(tagValue) == "")return;
	           oldValue = oldValue + tagValue + " ";
	       });
	       
	       if(oldValue == "") {
	           getDetailOldUserTagObject().find("a").each(function() {
	               var tagValue = jQuery(this).html();
	               if(jQuery.trim(tagValue) == "")return;
	               oldValue = oldValue + tagValue + " ";
	           });
	       }
	       return oldValue;
	   }
	   
	   //设置user Tag原有的值
	   function setOldUserTagValue(repositoryId, newUserTag) {
	       var newUserTagHtml = getUserTagHtml(newUserTag);
	       var pageType = jQuery("input[name=pageType]").val();
	       if(pageType == "itemPage") {//详情页
	           getDetailOldUserTagObject().find("li p").remove();
	           if(getDetailOldUserTagObject().find("li span").length <=0) {
	               getDetailOldUserTagObject().find("li").append("<span>标签：</span>");
	           }
	           getDetailOldUserTagObject().find("li").append(newUserTagHtml);
	       }
	       else{//列表页
	           getOldUserTagObject(repositoryId).empty();
	           getOldUserTagObject(repositoryId).html(newUserTagHtml);
	       }
	   }
	   
	   //根据内容来源进行处理
	   function selectContentSource(html,data) {
	       getBtnTempObject().empty();
	       getBtnTempObject().html(html);
	       var url = jQuery.trim(data.url);
	       if(url==null)url = "";
	       
	       if(url.substring(0,4) != "http") {//不是url,就是来自邮箱
	           getBtnTempObject().find(".user-option .from a").attr("href","javascript:void(0);");
	           try{
	               getBtnTempObject().find(".user-option .from a").removeAttr("onclick");
	           }catch(e){
	               
	           }
	       }
	       if(data.source == "Source_Upload") {//来自我的上传
	           setTypeFile(data);
	       }
	       else if(data.type == "Type_File") {
	           setTypeFile(data);
	       }
	       
	       return getBtnTempObject().html();
	   }
	   
	   function setTypeFile(data) {
	       getBtnTempObject().find(".user-option .from a").html("我的上传");
           if(data.type != "Type_Image") {//非图片,其他文件格式进行下载操作
               var downloadFile = "javascript:downloadFile('" + data.file.id + "');";
               var openFile = "javascript:openFile('" + data.file.id + "');";
               getBtnTempObject().find(".panel-content-video .open-drag").attr("href",openFile);
	           getBtnTempObject().find(".panel-content-video .download-drag").attr("href",downloadFile);
           }
	   }
	   
	   //根据内容类型选择
	   function selectContentType(html,data) {
	       getBtnTempObject().empty();
	       getBtnTempObject().html(html);
	       var cssValue = "panel";
	       if(data.type == "Type_Image" && data.source == "Source_Upload") {//上传图片
	           removeVideoImg();
	           removeInnerTitle();
	           removeFile();
	           cssValue = "panel panel-text-pic panel-img";
	       }
	       else if(data.type == "Type_Image") {//抓取图片
	           //removerOutTitle();
	           removeVideoImg();
	           removeInnerTitle();
	           removePrice();
	           removeOpenRead();
	           removeFile();
	           cssValue = "panel panel-text-pic panel-img";
	           
	       }
	       else if(data.type == "Type_Product") {//商品
	           removeVideoImg();
	           //removeInnerTitle();
	           //removerOutTitle();
	           removeOpenRead();
	           removeFile();
	           cssValue = "panel panel-commodity";
	       }
	       else if(data.type == "Type_Video") {//视频
	           //removerOutTitle();
	           removeInnerTitle();
	           removeOpenRead();
	           removeFile();
	           cssValue = "panel panel-video";
	       }
	       else if(data.type == "Type_File") {//文件
	           removeVideoImg();
	           removeInnerTitle();
	           appendFileSize(data);
	           cssValue = "panel panel-video format-ion";
	       }
	       else {//网页
	           removeVideo();
	           removeInnerTitle();
	           if(data.web != null) {
	               var attachs = data.web.attachment;
	               if(attachs !=null && attachs.length>0) {
	                   cssValue = "panel panel-text-pic";
	               }
	               if(data.web.content == null || jQuery.trim(data.web.content) == "") {
	                   removeOpenRead();
	               }
	           }
	       }
	       
	       setCssValue(data,cssValue);
	       return getBtnTempObject().html();
	   }
	   
	   //设置不同内容类型的css样式
	   function setCssValue(data,cssValue) {
	       var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
	       if(menuType != "unread") {	           
	           getBtnTempObject().find("#"+getId(data)+" h2").attr("class","left-floder-list-none");
	           /*getBtnTempObject().find("#"+getId(data)+" h2").css(
	           	{"background":"none"}
	           );
	           if(getBtnTempObject().find("#"+getId(data)+" h2").css("background")==""){
	               //var temp1 = document.getElementById("btnTemp");
	               var temp2 = document.getElementById(getId(data));
	               var temp3 = temp2.getElementsByTagName("h2")[0];
	               temp3.style.backgroundPositionX="0px";
	               temp3.style.backgroundPositionY="0px";
	           }
	           getBtnTempObject().find("#"+getId(data)+" h2").css("padding","0"); 
	           */
	       }
	       getBtnTempObject().find("#"+getId(data)).attr("class",cssValue);
	   }
	   
	   //删除视频信息
	   function removeVideo() {
	       getBtnTempObject().find(".panel-content-video").remove(); 
	   }
	   //删除视频图片
	   function removeVideoImg() {
	       getBtnTempObject().find(".panel-content-video span").remove(); 
	   }
	   //删除文件类型信息
	   function removeFile() {
	       getBtnTempObject().find(".panel-content-video>p").remove(); 
	       getBtnTempObject().find(".panel-content-video .open-drag").remove(); 
	       getBtnTempObject().find(".panel-content-video .download-drag").remove(); 
	   }
	   //删除标题
	   function removerOutTitle() {
	       getBtnTempObject().find(".panel>.panel-title>h2").remove();
	   }
	   //删除内标题
	   function removeInnerTitle() {
	       getBtnTempObject().find(".panel-content>.panel-title").remove();
	   }
	   //删除价格
	   function removePrice() {
	       getBtnTempObject().find(".panel-content>.panel-title>span").remove();
	   }
	   //删除阅读全文
	   function removeOpenRead() {
	       getBtnTempObject().find("#readFullContent").remove();
	   }
	   
	   function appendFileSize(data) {
	       var size = getBtnTempObject().find(".panel-content-video p");
	       getBtnTempObject().find(".panel-content-video p").append(getFileSize(data.file.length));; 
	   }
	   
	   function getFileSize(size) {
	       if(size == null)size = 0;
	       var level = 1024;
	       var unit = "KB";
	       size = Math.round(size/level);
	       if(Math.floor(size/level) >0) {
	           size = Math.floor(size/level);
	           unit = "MB";
	       }
	       return size+unit;
	   }
	   
	   
	   //替换朋友分享
	   function replaceFriendShare(html,data) {
	       getBtnTempObject().empty();
	       getBtnTempObject().html(html);
	       var shareId = getFriendShareIdObject();
	       
	       var fromFriend = data.from;
	       var menuType = jQuery("input[name=menuType]").val();
	       if(menuType == "myrecommend" || fromFriend == null || fromFriend.length < 1) {
	           shareId.remove();
	           return getBtnTempObject().html();
	       }
	       var html = getShareTemplateObject().html();
	       //最先分享
	       var firstFriend = fromFriend[0];
	       var text = "";
	       var score = 0;
	       if(firstFriend.comment != null) {
	           text = firstFriend.comment.text;
	           score = firstFriend.comment.score;
	       }
	       if(text == null)text = "";
	       html = html.replace("%name%",firstFriend.name);
	       html = html.replace("%logo%",firstFriend.avatar.small);
	       html = html.replace("%comment%",text);
	       html = html.replace("%score%",getScoreStar(score));
	       shareId.append(html);
	       
	       //最近分享
	       if(fromFriend.length > 1) {
	           var lastFriend = fromFriend[fromFriend.length-1];
	           var text = "";
	           var score = 0;
	           if(lastFriend != null) {
	               text = lastFriend.comment.text;
	               score = firstFriend.comment.score;
	           }
	           if(text == null)text = "";
	           html = getShareTemplateObject().html();
	           html = html.replace("%name%",lastFriend.name);
	           html = html.replace("%logo%",lastFriend.avatar.small);
	           html = html.replace("%comment%",text);
	           html = html.replace("%score%",getScoreStar(score));
	           shareId.append(html);
	           shareId.attr("class","personal-comments personal-comments-right");
	       }
	       
	       return getBtnTempObject().html();
	   }
	   
	   //替换未读首页图片显示
	   function replaceUnreadShowImg(html,folders) {
	       var menuType = jQuery.trim(jQuery("input[name=menuType]").val());
	       var src = "";
	       if(menuType != "unread" && menuType != "favorite") {
	           //html = html.replace("%unreadShowImg%","");
	           return html;
	       }
	       
	       var stag =  getFolderType(folders);
	       if(menuType == "favorite") {
	           if(stag == "weibo") {
	               src = "left-floder-list-6";
	           }
	       }
	       else if(stag == "weibo") {
	           src = "left-floder-list-6";
	       }
	       else if(stag == "later" ) {//稍后看
	           src = "left-floder-list-1";
	       }
	       else if(stag == "collect" ) {//收藏
	           src = "left-floder-list-4";
	       }
	       else if(stag == "friend" ) {//朋友分享
	           src = "left-floder-list-2";
	       }
	       else {
	           return html;
	       }
	       
	       if(src != "") {
	          getBtnTempObject().empty();
	          getBtnTempObject().html(html);
	          getBtnTempObject().find(".panel-title h2").attr("class",src);
	       }
	       html = getBtnTempObject().html();
	       return html;
	   }
	   
	   //替换列表操作按钮
	   function replaceBtn(html,folders) {
	       getBtnTempObject().empty();
	       getBtnTempObject().html(html);
	       var btnHtml = "";
	       var menuType = jQuery("input[name=menuType]").val();
	       
	       if(menuType == "unread" || menuType == "") {//未读情况,需要获取具体每天记录的关系来源
	           menuType = getFolderType(folders);
	       }
	       if(menuType == "collect")menuType = "favorite";
	       
	       //btnHtml = btnHtml + getBtnTempObject().find("div[id=laterBtn]").html();//稍后看
	       if(menuType == "weibo"){
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "all") {
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "renren") {
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "upload") {
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "productwish") {
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "favorite"){//收藏
	           //btnHtml = btnHtml + getBtnTempObject().find("div[id=addTagBtn]").html();//打标签
	           btnHtml = btnHtml + getBtnTempObject().find("div[id=cancelFavoriteBtn]").html();//取消收藏 
	       }
	       else if(menuType == "later"){//稍后看
	           btnHtml = "";
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else if(menuType == "read"){//已读
	           btnHtml = btnHtml + getBtnTempObject().find("div[id=favoriteBtn]").html();//收藏
	       }
	       else if(menuType == "friend"){//朋友分享
	           btnHtml = "";
	           btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	           //btnHtml = btnHtml + getBtnTempObject().find("div[id=addTagBtn]").html();//打标签
	       }
	       else if(menuType == "public") {//公共内容
	           btnHtml = "";
	           btnHtml = btnHtml + getBtnTempObject().find("div[id=publicBtn]").html();//收藏
	       }
	       else if(menuType == "myrecommend") {//我的分享
	          btnHtml = "";
	          btnHtml = getCollectAndUncollectBtnHtml(folders,btnHtml);
	       }
	       else {
	           btnHtml = btnHtml + getBtnTempObject().find("div[id=favoriteBtn]").html();//收藏
	       }
	       btnHtml = btnHtml + getBtnTempObject().find("div[id=ShareFriendBtn]").html();//分享给朋友
	       //btnHtml = btnHtml + getBtnTempObject().find("div[id=snsBtn]").html();//分享到SNS
	       //btnHtml = btnHtml + getBtnTempObject().find("div[id=commentBtn]").html();//评论
	       
	       if(menuType == "friend") {//朋友分享
	           btnHtml = btnHtml + getBtnTempObject().find("div[id=deleteFriendShareBtn]").html();//取消朋友分享
	       }
	       
	       getBtnTempObject().find("div[id=allBtn] ul").empty();
	       getBtnTempObject().find("div[id=allBtn] ul").html(btnHtml);
	       
	       html = getBtnTempObject().html();
	       return html;
	   }
	   
	   function getCollectAndUncollectBtnHtml(folders,btnHtml) {
	       if(isHaveCollect(folders)) {
               btnHtml = btnHtml + getBtnTempObject().find("div[id=cancelFavoriteBtn]").html();//取消收藏 
               getBtnTempObject().find("div[id=favoriteBtn]").find("li").hide();
               btnHtml = btnHtml + getBtnTempObject().find("div[id=favoriteBtn]").html();//收藏
           }else {
               btnHtml = btnHtml + getBtnTempObject().find("div[id=favoriteBtn]").html();//收藏
               getBtnTempObject().find("div[id=cancelFavoriteBtn]").find("li").hide();
               btnHtml = btnHtml + getBtnTempObject().find("div[id=cancelFavoriteBtn]").html();//取消收藏 
           }
           return btnHtml;
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
	       if(result.indexOf("Source_Weibo") > -1) {
	           result = "weibo";
	       }
	       else if(result.indexOf("Source_Friend") > -1) {
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
	   //判断是否含有收藏folder
	   function isHaveCollect(folders) {
	       var isCollect = false;
	       if(folders == null || folders.length < 1)return "";
	       var result = "";
	       for(var i=0;i < folders.length;i++) {
	           var folder = folders[i];
	           if(folder.indexOf("Delete") > -1)continue;
	           result = result + folder + ",";
	       }
	       if(result.indexOf("Collect") > -1) {
	           isCollect = true;
	       }
	       return isCollect;
	   }
	   
	   //列表要显示的图片url(可能是：商品图片、就一张图片、文章中的第一张图片)
	   function getImageUrl(data) {
	       var imageUrl = "";
	       if(data.type == "Type_File") {//文件
	           imageUrl = getFileTypeImage(data.file.extension);
	       }
	       else if(data.type == "Type_Image") {//图片
	           if(data.image != null)imageUrl = data.image.url;
	       }
	       else if(data.type == "Type_Product") {//商品
	           if(data.product != null)imageUrl = data.product.picture;
	       }
	       else if(data.type == "Type_Video") {//视频
	           if(data.video!=null)imageUrl = data.video.thumbnail;
	       }
	       else {//网页
	           if(data.web != null) {
	               var attachs = data.web.attachment;
	               var folder = getFolderType(data.folders);
	               if(folder == "weibo" && attachs !=null && attachs.length>1){
	                   imageUrl = attachs[0].url;
	               }
	               else if(folder != "weibo" && attachs !=null && attachs.length>0) {
	                   imageUrl = attachs[0].url;
	               }
	           }
	       }
	       
	       return imageUrl;
	   }
	   
	   //获取文件类型图标
	   function getFileTypeImage(extension) {
	       var imgUrl = systemConstant.fileTypeImgPath;
	       //var type = data.file.contentType;
	       //type = data.file.extension;
	       var type = extension;
	       //alert("fileType:" +type);
	       if(type==null)type="";
	       type = type.toLowerCase();
	       if(type.indexOf("pdf")>-1) {
	           imgUrl+= "PDF.png";
	       }
	       else if(type.indexOf("txt")>-1) {
	           imgUrl+= "TXT.png";
	       }
	       else if(type.indexOf("docx")>-1) {
	           imgUrl+= "DOCX.png";
	       }
	       else if(type.indexOf("doc")>-1) {
	           imgUrl+= "DOC.png";
	       }
	       else if(type.indexOf("pptx")>-1) {
	           imgUrl+= "PPTX.png";
	       }
	       else if(type.indexOf("ppt")>-1) {
	           imgUrl+= "PPT.png";
	       }
	       else if(type.indexOf("xlsx")>-1) {
	           imgUrl+= "XLSX.png";
	       }
	       else if(type.indexOf("xls")>-1) {
	           imgUrl+= "XLS.png";
	       }
	       else if(type.indexOf("wps")>-1) {
	           imgUrl+= "WPS.png";
	       }
	       else if(type.indexOf("epub")>-1) {
	           imgUrl+= "EPUB.png";
	       }
	       else {
	           imgUrl+= "UNKNOWN.png";
	       }
	       
	       return imgUrl;
	   }
	   
	   //替换操作按钮
	   function replaceOperateButton(html,data) {//详情页
	       html = html.replace(/\%userTag\%/g,data.userTags);
           html = html.replace(/\%contentId\%/g,data.id);
	       html = html.replace(/\%repositoryId\%/g,getId(data));
	       html = html.replace(/\%collect\%/g,data.collect);
	       html = html.replace("%comment%",data.comment);
	       //html = html.replace("%share%",data.share);
           //操作按钮
	       html = replaceBtn(html,data.folder);
	       return html;
	   }
	   
	   //获取跳转url
	   function getSourceUrl(data) {
	       
	   }
	   
	   //设置列表提示文案
	   function setFolderPrompt() {
        var size = getContentObject().children().size();
        if(size > 0) {
            return;
        }
        var menuType = getMenuType();
        var isFirstLogin = jQuery("input[name=isFirstLogin]").val();
        if(menuType == "unread" && isFirstLogin=="true") {
            return;
        }
        var html = "<div class=\"panel not-sharing\">";
        html="";
        //html = html + "<h5 class=\"h5-list-1\">";
        if(menuType == "unread") {
            html = html + jQuery("#promptId #unreadPromptId").html();
        }
        else if(menuType == "later") {
            html = html + jQuery("#promptId #unreadPromptId").html();//laterPromptId
        }
        else if(menuType == "favorite") {
            html = html + jQuery("#promptId #unreadPromptId").html();//favoritePromptId
        }
        else if(menuType == "friend") {
            html = html + jQuery("#promptId #unreadPromptId").html();//friendPromptId
        }
        else if(menuType == "read") {
            html = html + jQuery("#promptId #unreadPromptId").html();//readPromptId
        }
        else if(menuType == "myrecommend") {
            html = html + jQuery("#promptId #unreadPromptId").html();//myrecommendPromptId
        }
        else {
            html = html + jQuery("#promptId #unreadPromptId").html();
        }
        
        //html = html + "</h5>";
        //html = html + "</div>";
        getContentObject().append(html);
    }
	   
	function getScoreStar(score) {
		var html = "";
		/*var count = 5;
		for (var i=0; i<count; i++) {
			if (i < score) {
				html += "<img src='"+imgPath+"star-on-big.png' />";
			}
			else {
				html += "<img src='"+imgPath+"star-off-big.png' />";
			}
		}*/
		return html;
	}
	
	function openFile(fileId) {
	    window.open(systemConstant.downloadHost+"/service/content/file/"+fileId+"?disposition=inline","blank");
	}
	
	function downloadFile(fileId) {
	    window.open(systemConstant.downloadHost+"/service/content/file/"+fileId,"blank");
	}