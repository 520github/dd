
//内容相关对象定义
       
       //获取内容展现对象
       function getContentObject() {
           var contentId = "containerList";
           var currentShowType = jQuery("input[name='showType']").val();
           if(currentShowType == "V") {
               contentId = "vertical";
           }
           return jQuery("#"+contentId)
       }
       
       //获取某个内容ID的div
       function getContentIdDiv(contentId) {
           return getContentObject().find("div[id='"+contentId+"']");
       }
       
       //收藏按钮
       function getContentIdCollectBtn(contentId) {
           return getContentIdDiv(contentId).find("#allBtn>ul>li[class='icon-2 icon-color']");
       }
       
       //取消收藏按钮
       function getContentIdUnCollectBtn(contentId) {
           return getContentIdDiv(contentId).find("#allBtn>ul>li[class='icon-collection']");
       }
       
       //获取user Tag原有的对象
	   function getOldUserTagObject(repositoryId) {
	       return jQuery("#"+repositoryId+" #oldUserTag li");
	   }
	   
	   //替换按钮时的临时对象
	   function getBtnTempObject() {
	       return jQuery("#btnTemp");
	   }
	   
	   //朋友分享对象
	   function getFriendShareIdObject() {
	       return jQuery("#btnTemp #friendShareId");
	   }
	   //未读图标显示
	   function getUnreadShowImgObject() {
	       return jQuery("#btnTemp #unreadShowImg");
	   }
	   
	   //朋友分享模板
	   function getShareTemplateObject() {
	       return jQuery("#shareTemplate");
	   }
	   
	   //获取左侧菜单类型
	   function getMenuType() {
	       return jQuery("input[name=menuType]").val();
	   }
	   
	   //内容ID
	   function getId(data) {
	       if(data == null)return "undefine";
	       return data.id;
	   }
	   


//获取详情页相关对象定义

       //获取详情页面对象
	   function getDetailObject() {
		  return jQuery("#detail");
	   }      
	   
	   //详情页右边对象
	   function getDetailRightObject() {
	      return getDetailObject().find("#right-detail");
	   }
	   
	   //详情页收藏按钮
       function getDetailCollectBtn() {
           return getDetailRightObject().find("#allBtn>ul>li[class='icon-2']");
       }
       
       //详情页取消收藏按钮
       function getDetailUnCollectBtn() {
           return getDetailRightObject().find("#allBtn>ul>li[class='icon-color']");
       }
       
       //获取详情页user Tag原有的对象
	   function getDetailOldUserTagObject() {
	       return getDetailObject().find("#right-detail .pop-up-playing-tag .recommend-list");
	   }
       
       
       
//用户标签相关对象定义       

       //列表中原有标签对象
	   function getContentOldUserTagObject() {
	       return getContentObject().find("div[id=oldUserTag] p a");
	   }
	   
       //编辑tag对象
	   function getEditTagObject() {
	       return jQuery("#editTagId ul");
	   }      
	   
	   
	   
//对内容进行打标签	  
 
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
       
       