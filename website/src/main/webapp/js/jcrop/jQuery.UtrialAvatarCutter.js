jQuery.UtrialAvatarCutter = function(){
	var api = null;
	var sel = this;
	this.reload = function(img_url){
		sel.init();
	};
	this.init = function(){
		if(api!=null){
			api.destroy();
		}
		api = $.Jcrop('#cropImage',{ 
			onChange : showPreview,
			onSelect : showPreview,
			aspectRatio : 1,
			setSelect:   [ 0, 0, 100, 100 ],
			minSize: [ 35, 35 ],
			allowResize : true,
			allowSelect : true
		});
	
	};
	var showPreview=function(coords){
		var ow = $("#cropImage").width();
		var oh = $("#cropImage").height();
		preview1(coords,ow,oh);
		preview2(coords,ow,oh);
		preview3(coords,ow,oh);
		$("#coordw").attr("value",Math.round(coords.w));
		$("#coordh").attr("value",Math.round(coords.h));
		$("#coordx").attr("value",Math.round(coords.x));
		$("#coordy").attr("value",Math.round(coords.y));
	};
	var preview1=function(coords,ow,oh){
		var rx = 150 / coords.w;
		var ry = 150 / coords.h;
		$('#preview1').css({
			width: 	Math.round(rx * ow) + 'px',
			height: Math.round(ry * oh) + 'px',
			marginLeft: '-' + Math.round(rx * coords.x) + 'px',
			marginTop: '-' + Math.round(ry * coords.y) + 'px'
		});		
	};
	var preview2=function(coords,ow,oh){
		var rx = 100 / coords.w;
		var ry = 100 / coords.h;
		$('#preview2').css({
			width: 	Math.round(rx * ow) + 'px',
			height: Math.round(ry * oh) + 'px',
			marginLeft: '-' + Math.round(rx * coords.x) + 'px',
			marginTop: '-' + Math.round(ry * coords.y) + 'px'
		});		
	};
	var preview3=function(coords,ow,oh){
		var rx = 35 / coords.w;
		var ry = 35 / coords.h;
		$('#preview3').css({
			width: 	Math.round(rx * ow) + 'px',
			height: Math.round(ry * oh) + 'px',
			marginLeft: '-' + Math.round(rx * coords.x) + 'px',
			marginTop: '-' + Math.round(ry * coords.y) + 'px'
		});		
	};	
};