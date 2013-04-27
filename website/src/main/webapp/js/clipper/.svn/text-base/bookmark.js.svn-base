(function(){
      var method = {
          addScript:function(scriptPath) {
              var targetScript = document.createElement("script");
			  targetScript.setAttribute("src", scriptPath);
			  targetScript.setAttribute('charset','UTF-8');
			  var bodyLength = document.getElementsByTagName('body').length;
			  if(bodyLength <=0) {
			      document.createElement("body");
			  }
			  document.getElementsByTagName('body')[0].appendChild(targetScript);
		  },
		  checkScriptPath:function(scriptPath) {
		      var isExist = false;
		      var scripts = document.getElementsByTagName('script');
		      for(var i=0;i<scripts.length;i++) {
			      var script = scripts[i];
			      var src = script.src;
			      if(src == scriptPath) {
			          isExist = true;
			          break;
			      } 
			  }
			  return isExist;
		  },
		  getHttpType:function() {
		      var httpType = "http";
		      var webUrl = window.location.href;
		      if(webUrl.indexOf(":")>-1) {
		          httpType = webUrl.substring(0,webUrl.indexOf(":"));
		      }
		      return httpType;
		  },
		  getDomainName:function() {
		      domainName = "";
		      var script = document.getElementById("ddClipScript");
		      if(script!=null && script.src!=null && script.src.indexOf("http") >-1) {
		          var src = script.src;
		          src = src.substring(src.indexOf("//")+2,src.length);
		          src = src.substring(0,src.indexOf("/"));
		          if(src!=null && src !='') {
		              domainName = src;
		          }
		      }
		      return domainName;
		  },
		  isContainJQuery:function() {
		      var containJquery = "jquery";
		      var scripts = document.getElementsByTagName('script');
		      for(var i=0;i<scripts.length;i++) {
			      var src = scripts[i].src;
			      src=src==null?"":src.toLocaleLowerCase();
			      if(src.indexOf(containJquery)>-1) {
			          return true;
			      }
			  }
			  return false;
		  }
      };
      
      var domainName = method.getDomainName();
      if(domainName == null || domainName == "")domainName = "www.mduoduo.com";
      
      var httpType = method.getHttpType();
      window.$httpType = httpType;
      window.$domainName = domainName;
      var productPath = httpType+"://"+domainName+"/js/clipper/product.js";
	  var path = httpType+"://"+domainName+"/js/clipper/2coffee_fast_step.js";
	  
	  var bodyLength = document.getElementsByTagName('body').length;
	  if(bodyLength <=0) {
	  }
	  
	  var ddAutoClip = document.getElementById("ddAutoClip").value;
	  
	  if(window.$coffee2ok && window.$coffee2ok.clicked) {
	  	  return;
	  }
	  else if(ddAutoClip == "true"){
	      window.$coffee2ok = {};
	  	  window.$coffee2ok.clicked = true;
	  }
	  
	  if(method.checkScriptPath(path)) {
	      __coffee2FastStep__.init();
	       return;
	  }

	  var ds = "duoduo.com";
      if(domainName.indexOf(".") >-1) {
          ds = domainName.substring(domainName.indexOf(".")+1);
      }
      if(window.jQuery || window.$) {
	          
      }
      else {
          var jqueryScriptPath = httpType+"://"+domainName+"/js/clipper/jquery.min.js";
          method.addScript(jqueryScriptPath); 
      }
      if(document.domain.indexOf(ds) < 0) {
          method.addScript(httpType+"://"+domainName+"/js/clipper/clipperHtmlByJs.js"); 
      }
	  
	  method.addScript(productPath);
	  method.addScript(path);
})();