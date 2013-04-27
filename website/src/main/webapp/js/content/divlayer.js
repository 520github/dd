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
      
      
      function openDetailComment(contentId){
		   var obj = $(".comment_layer");
		   obj.attr("contentId", contentId);
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
      
      //显示第一次登录之后的引导页
      function showGuide() {
          jQuery("#guide").dialog({
              bgiframe:true,
              "autoOpen":true,
              "position":['top'],
              "height":550,
              "width":730,
              "draggable":false,
              "resizable":false,
              //title:'我来了啊',
              modal:true
          });
          jQuery(".ui-dialog-titlebar").hide();
      }