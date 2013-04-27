//统计tag
       function statisticTag() {
           jQuery.ajax({
	           url:"/service/content/personal/statistics/tags?r="+(Math.random()*99999999),
	           type:"get",
	           headers:token,
	           success:function(resp) {
	               statisticTagNum(resp);
	               AccountRest.obj.getProfile(getProfileSuccessFnInList,getProfileErrorFnInList);
	           }
	       });
       };
       
       function statisticTagNum(resp) {
           if(resp == null)return;
           
           var publicNum = resp.Public == null?0:resp.Public.quantity;
           jQuery("#publicTotalNum").html(publicNum);
           
           var allNum = resp.all == null?0:resp.all.quantity;
           jQuery("#allTotalNum").html(allNum);
           
           var laterNum = eval("resp.Later") == null?0:eval("resp.Later.quantity");
           jQuery("#laterTotalNum").html(laterNum);
           
           var friendNum = resp.Source_Friend == null?0:resp.Source_Friend.quantity;
           jQuery("#friendTotalNum").html(friendNum);
           
           var productWishNum = resp.Product_Wish == null?0:resp.Product_Wish.quantity;
           jQuery("#productWishTotalNum").html(productWishNum);
           
           var weiboNum = resp.Source_Weibo == null?0:resp.Source_Weibo.quantity;
           jQuery("#weiboTotalNum").html(weiboNum);
           
           var renrenNum = resp.Source_Renren == null?0:resp.Source_Renren.quantity;
           jQuery("#renrenTotalNum").html(renrenNum);
           
           var weixinPublicNum = resp.Source_Weixin_Public == null?0:resp.Source_Weixin_Public.quantity;
           jQuery("#weixinPublicTotalNum").html(weixinPublicNum);
           
           var uploadNum = resp.Source_Upload == null?0:resp.Source_Upload.quantity;
           jQuery("#uploadTotalNum").html(uploadNum);
           
           var collectNum = resp.Collect == null?0:resp.Collect.quantity;
           jQuery("#favoriteTotalNum").html(collectNum);
           
           var myRecommendNum = resp.My_Recommend == null?0:resp.My_Recommend.quantity;
           jQuery("#myRecommendTotalNum").html(myRecommendNum); 
       };
       

//用户管理编辑自己的标签
       
       var tagRemark = "<li><a><span align=\"right\">亲，你还没有打过标签，去给收藏的内容打标签吧！\n可以方便查看收藏哦！</span></a></li>";
       var editTagBtn = "<li class='label-button'><input id='selectTagBtn' type='image' src='"+imgPath+"label.png'  onclick='editTagList()'></li>";
       //获取用户所有的标签列表
	   function getTagList(token) {
	       jQuery.ajax({
	           url:"/service/content/personal/tags?r="+(Math.random()*99999999),
	           type:"get",
	           headers:token,
	           success:function(resp){
	               getEditTagObject().html("");
	               var editTag = getEditTagObject();
	               var html = "<li>";
	               html = html + "<a id='a%tag%' href=\"javascript:queryTag('%tag%');hiddenUl();\"><span>%tag%</span></a>";
	               html = html + "<p><img id='img%tag%' src='"+ imgPath + "close-pic.png' onclick=\"deleteUserTag('%tag%')\" /></p>";
	               html = html + "<span class='active-input' ><input id='input%tag%' value='%tag%' size='4' onmouseout='checkInputValue(this)' onblur='checkInputValue(this)'/></span>";
	               html = html + "</li>";
	               var result = resp.result;
	               if(result.length < 1) {
	                   getEditTagObject().append(tagRemark);
	                   getEditTagObject().append(editTagBtn);
	                   setTagWidth();
	                   return;
	               }
	               var num = "全部(" + result.length + ")";
	               var all = "<li><a id=\"searchAll\" href=\"javascript:queryTag('');resetShowOrHide();hiddenUl();\"><span>"+num+"</span></a></li>";
	               editTag.append(all);
	               var next = 4;
	               for(var i=0;i<result.length;i++) {
	                   var data = result[i];
	                   if(i !=0 && i%next==0) {
	                       //jQuery("#selectTag").append("<br><br>");
	                   }
	                   editTag.append(html.replace(/\%tag\%/g,data.name));
	                   jQuery("#img"+data.name).hide();
	                   jQuery("#input"+data.name).hide();  
	               }
	               html = "<li class='label-button'><input id='selectTagBtn' type='image' src='"+imgPath+"label.png'  onclick='editTagList()'></li>";
	               editTag.append(editTagBtn);
	           }
	       });
	   }
	   
	   //检查输入值
	   function checkInputValue(obj) {
	       var value = obj.value;
	       if(value == null) value = "";
	       value = jQuery.trim(value);
	       if(value.length < 1) {
	           alert("亲，标签不能为空哦！");
	           obj.focus();
	           return false;
	       }
	       if(getStrLength(value) >12) {
	           alert("亲，每个标签不能超过12个字符哦！");
	           obj.focus();
	           return false;
	       }
	       if(value.indexOf(" ")>-1) {
	           alert("亲，标签中不能含有空格！");
	           obj.focus();
	           return false;
	       }
	       return true;
	   }
	   
	   //编辑标签列表
	   function editTagList() {
	       getEditTagObject().find("img[id^=img]").show();
	       getEditTagObject().find("a[id^=a]").each(function(){
	           var id = jQuery(this).attr("id");
	           var tag = id.substring(1,id.length);
	           jQuery(this).attr("href","javascript:editTag('"+tag+"')");
	       });
	       getEditTagObject().find("#selectTagBtn").attr("src",imgPath+"confirm-3.png");
	       getEditTagObject().find("#selectTagBtn").attr("onclick","updateUserTag()");
	   }
	   //编辑单个标签及操作按钮
	   function editTag(tag) {
	       getEditTagObject().find("a[id=a"+tag+"]").hide();
	       getEditTagObject().find("img[id=img"+tag+"]").hide();
	       getEditTagObject().find("input[id=input"+tag+"]").show();
	       getEditTagObject().find("#selectTagBtn").attr("src",imgPath+"confirm-3.png");
	       getEditTagObject().find("#selectTagBtn").attr("onclick","updateUserTag()");
	   }
	   
	   //删除用户标签
	   function deleteUserTag(userTag) {
	       if(confirm('删除标签不会删除标签下对应的内容，是否删除？')) {
	           var url = encodeURI("/service/content/personal/tags/"+userTag);
	           jQuery.ajax({
	               url:url,
	               type:"delete",
	               contentType:"application/json;charset=utf-8",
	               headers:token,
	               success:function(resp){
	                   //getDataList("refresh");
	                   removeContentUserTag(userTag);
	                   getEditTagObject().find("a[id=a"+userTag+"]").parent().remove();
	                   //getEditTagObject().find("img[id=img"+userTag+"]").remove();
	                   //getEditTagObject().find("input[id=input"+userTag+"]").remove();
	                   setAllNum();
	                   //alert("亲，恭喜您，删除标签成功！");
	               }
	           });
	       }
	   }
	   
	   //设置全部数量
	   function setAllNum() {
	       var anum = getEditTagObject().find("a").length;
	       anum = anum - 1;
	       if(anum > 0) {
	           getEditTagObject().find("#searchAll span").html("全部("+anum+")");
	       }
	       else {
	           getEditTagObject().find("#searchAll").parent().remove(); 
	           getEditTagObject().append(tagRemark);
	           setTagWidth();
	       }
	       getEditTagObject().show();
	   }
	   
	   //设置标签显示的宽度
	   function setTagWidth() {
	       if(getEditTagObject().find("a").length == 1) {
	           getEditTagObject().find("a").css("width","400px");
	           getEditTagObject().find("#selectTagBtn").attr("src",imgPath+"confirm-3.png");
	           getEditTagObject().find("#selectTagBtn").attr("onclick","hiddenUl()()");
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
	                       //alert("亲，恭喜您更新标签成功！");
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
	           getEditTagObject().find("a[id=a"+oldTag+"]").attr("href","javascript:queryTag('"+newTag+"');hiddenUl();");
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
	           jQuery(this).attr("href","javascript:queryTag('"+tag+"');hiddenUl();");
	       });
	       getEditTagObject().find("img[id^=img]").hide();
	       getEditTagObject().find("input[id^=input]").hide();
	       getEditTagObject().find("#selectTagBtn").attr("src",imgPath+"label.png");
	       getEditTagObject().find("#selectTagBtn").attr("onclick","editTagList()");
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
	   
	   
//用户对内容进行打标签	   
	   
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
             if(getStrLength(userTag) > 12) {
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
                 openPopupMessage('success','添加标签成功');
              }
          });   
      }
      
      //获取字符串长度
      function getStrLength(str) {
          if(str == null) return 0;
          var len = 0;  
          for (var i=0; i<str.length; i++) {  
             if (str.charCodeAt(i)>127 || str.charCodeAt(i)==94) {  
                 len += 2;  
             } else {  
                 len ++;  
             }  
          }  
          //alert(len);
          return len;  
       }