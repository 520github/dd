function postMsgToParent(action, data) {
	var message = {
		"action" : action
	};
	if (data) {
		message["data"] = data;
	}
	window.parent.postMessage(JSON.stringify(message), "*");
}

function DDPlugin() {
}

DDPlugin.prototype.getJSON = function(url, data, callback) {
	$.ajax({
		url : url,
		dataType : 'json',
		type : 'POST',
		cache : false,
		async : false,
		data : data,
		success : callback
	});
}

DDPlugin.prototype.setClipTitle = function(title) {
	this.title = title;
}
DDPlugin.prototype.getTags = function() {
	var tags = [];
	if ($("#ddCollect").attr("checked")) {
		tags.push("Collect");
	}
	if ($("#ddReadLater").attr("checked")) {
		tags.push("Later");
	}
	if ($("#dd2Coffee").attr("2coffee")) {
		tags.push("Later");
	}
	return tags.join(",");
}
DDPlugin.prototype.resetClipEnv = function() {
	this.setClipAction("clipArchived");
	this.setClipContentType("Web");
	postMsgToParent("getUrl");
}
DDPlugin.prototype.setClipContentUrl = function(value) {
	var id = "ddClipContentUrl";
	if (!document.getElementById(id)) {
		$("body").append($("<input type='hidden' id='" + id + "'/>"));
	}
	$("#" + id).val(value);
}
DDPlugin.prototype.setClipAction = function(value) {
	var id = "ddClipAction";
	if (!document.getElementById(id)) {
		$("body").append($("<input type='hidden' id='" + id + "'/>"));
	}
	$("#" + id).val(value);
}
DDPlugin.prototype.getClipAction = function() {
	return $("#ddClipAction").val();
}
DDPlugin.prototype.getClipContentType = function() {
	return $("#ddClipContentType").val();
}

DDPlugin.prototype.setClipContentType = function(value) {
	var id = "ddClipContentType";
	if (!document.getElementById(id)) {
		$("body").append($("<input type='hidden' id='" + id + "'/>"));
	}
	$("#" + id).val(value);
}
DDPlugin.prototype.getClipContentUrl = function() {
	return $("#ddClipContentUrl").val();
}

DDPlugin.prototype.setArchivedId = function(value) {
	var id = "ddArchviedId";
	if (!document.getElementById(id)) {
		$("body").append($("<input type='hidden' id='" + id + "'/>"));
	}
	$("#" + id).val(value);
}

DDPlugin.prototype.getArchivedId = function() {
	return $("#ddArchviedId").val();
}

DDPlugin.prototype.alert = function(msg) {
	alert(msg);
}

DDPlugin.prototype.showContent=function() {
    var plug = this;
    if(!plug.loaded) return;
    plug.postMessage("showContent");
    window.clearInterval(plug.showContentId);
}

DDPlugin.prototype.preview = function(refer) {
	var plugin = this;

	plugin.getJSON(
					"/newbookmark/preview",
					{
						"url" : plugin.getClipContentUrl(),
						"contentType" : plugin.getClipContentType(),
						"refer" : refer
					},
					function(data) {
						if (data.msg) {
							plugin.alert(data.msg);
							plugin.close();
							return;
						}
						if (data.data.contentType == 'Web' && data.data.title == null && data.data.content == null) {
							plugin.alert('系统忙或出现问题，请稍后再试。');
							plugin.close();
							return;
						}

						if (data.data.contentType == 'Image') {
							var html = "<img width='200px' src='" + data.data.archiveUrl
									+ "'/>";
							html += "<h3>"+plugin.title+"</h3>";
							$("#ddPreview").html(html);
						} else if (data.data.contentType == 'Product') {
							var title = "<h2>" + data.data.product.name + "</h2>";
							var price = "<p>价格：" + data.data.product.price + "</p>";
							var img = "<img src='" + data.data.product.picture
									+ "' width='300px' height='300px'/>";
							$("#ddPreview").html(title + "" + price + "" + img);
						} else if (data.data.contentType == 'Video') {
							var html = "<h3>" + data.data.title + "</h3>";
							html += "<font style='font-size:12px;'>"
									+ data.data.summary + "</font>";
							$("#ddPreview").html(html);
						} else {
							var html = "<h3>" + data.data.title + "</h3>";
							html += "<font style='font-size:12px;color:#868686;line-height:18px'>"
									+ data.data.content + "</font>";
							$("#ddPreview").html(html);
						}
						plugin.setArchivedId(data.data.id);
						plugin.setClipContentType(data.data.contentType);

						$("#ddPreview img").each(function(i) {
							if ($(this)[0].width > 200 || $(this)[0].width == 0) {
								$(this).attr("width", "200px");
							}
						});
					});
}

DDPlugin.prototype.prepareClipDialog = function() {
	var plugin = this;

	// 初始化好友列表
	plugin.getJSON("/newbookmark/private/friends", {
		"pageIndex" : 1
	}, function(data) {
		plugin.allFriends = data.data;
		if (data.data) {
			plugin.setFriends(1);
			plugin.pageIndex = 1;
		} else {
			$("#addDDFriendsContainer").css("display", "block");
			$("#ddSelectAllFriContainer").css("display", "none");
			$("#ddFriPageBar").css("display", "none");
		}
	});

	// 好友下一页按钮
	$("#ddFriNextPage").click(function() {
		plugin.pageIndex += 1;
		plugin.setFriends(plugin.pageIndex);
	});
	// 好友上一页按钮
	$("#ddFriPrepage").click(function() {
		plugin.pageIndex -= 1;
		plugin.setFriends(plugin.pageIndex);
	});

	$("#ddClpComment").focus(function() {
		if ($(this).val() == "我觉得这个东东可能对你有帮助，希望你喜欢哦！") {
			$(this).val("");
		}
	});

	// 全选好友效果
	$('input[name*="allDDFri"]').click(function() {
		if ($(this).val() == 'select') {
			$('span[name*="ddFriUnSelect"]').each(function() {
				$(this).html("<img src=\"/images/plug-in-pic-4.png\"/>");
				$(this).attr("name", "ddFriSelect");

				$("#ddStarExtend").show();
				$("#ddCommentExtend").show();
				$("#ddCommentTip").show();
			});
		} else {
			$('span[name*="ddFriSelect"]').each(function() {
				$(this).html("<img src=\"/images/plug-in-pic-3.png\"/>");
				$(this).attr("name", "ddFriUnSelect");

				$("#ddStarExtend").hide();
				$("#ddCommentExtend").hide();
				$("#ddCommentTip").show();
			});
		}
	});
	// 评分效果

	$("#ddCMStarContainer").each(function(i) {
		$(this).raty({
			path : "/images",
			hints : [ '很差', '比较差', '一般', '比较好', '很好' ],
			size : 24,
			starOff : 'star-off-big.png',
			starOn : 'star-on-big.png',
			width : '230px',
			click : function(score, evt) {
				var s = 0;
				if (score != null && score != NaN && score != undefined) {
					s = score;
				}
				$("#ddCliperScore").val(s);
			}
		});
	});

	// 评论140字效果
	$("#ddClpComment").keyup(function() {
		if ($("#ddClpComment").val().length >= 140) {
			$("#ddClpComment").val($("#ddClpComment").val().substr(0, 140));
		}
		$("#ddCommentRemain").html(140 - $("#ddClpComment").val().length);
	});

	// 提交抓取结果
	$("#ddClipperBtn").click(
			function() {
				var comment = $("#ddClpComment").val();
				if ("我觉得这个东东可能对你有帮助，希望你喜欢哦！" == comment){
					alert('评论不能为空');
					return;
					$("#ddClpComment").val("");
				}

				var allDDFriValue = $("input[name='allDDFri']:checked").val();
				var toFriends = new Array();
				if (allDDFriValue == 'select') {
					toFriends = "all";
				} else {
					$('span[name*="ddFriSelect"]').each(function() {
						toFriends.push($(this).attr("id"));
					});
					toFriends = toFriends.join(",");
				}

				if ("clipSelection" == plugin.getClipAction()) {
					var content = {
						"contentType" : "HtmlClip",
						"url" : plugin.getClipContentUrl(),
						"title" : plugin.title,
						"htmlPayload" : $("#ddPreview").html(),
						"tag" : plugin.getTags(),
						"toFriends" : toFriends,
						"comment" : $("#ddClpComment").val(),
						"score" : $("#ddCliperScore").val()
					};
					plugin.getJSON("/newbookmark/private/store", content,
							function(data) {
								plugin.alert(data.msg);
								$("#ddPreview").html("");
								plugin.close();
								plugin.resetClipEnv();
							});

				} else {
					var title = null;
					if ($("#ddClipContentType").val() == "Image") {
						title = plugin.title;
					}
					plugin.getJSON("/newbookmark/private/store", {
						"id" : plugin.getArchivedId(),
						"tag" : plugin.getTags(),
						"toFriends" : toFriends,
						"comment" : $("#ddClpComment").val(),
						"score" : $("#ddCliperScore").val(),
						"title" : title
					}, function(storeResult) {
						plugin.alert(storeResult.msg);
						$("#ddPreview").html("");
						plugin.close();
						plugin.resetClipEnv();
					});
				}
			});
}

DDPlugin.prototype.close = function() {
	postMsgToParent("closeDialog");
}

DDPlugin.prototype.getPageIndicate = function(index, count) {
	if (count < 1)
		return "";
	
	var html = "";
	var current = count - index - 1;
	for (var i=0; i<count; i++) {
		if (i == current) {
			html += '<img src="/images/plug-in-pic-di-color-1.png" style="width:11px; float:right; margin:0; height:5px;" />';
		}
		else {
			html += '<img src="/images/plug-in-pic-1.png" style="width:11px; float:right; margin:0; height:5px;" />';
		}
	}
	return html;
}

DDPlugin.prototype.setFriends = function(pageIndex) {
	if (!this.allFriends) {
		return;
	}

	var allFriends = this.allFriends;
	if (!pageIndex || pageIndex < 1) {
		pageIndex = 1;
	}
	// 每页显示数量
	var pageSize = 5;
	// 最多4页
	var maxPageNum = 10;

	// 总页数
	var pageNum = parseInt(allFriends.length / pageSize);
	if (allFriends.length % pageSize != 0) {
		pageNum += 1;
	}
	this.pageNum = pageNum;

	// 设置翻页小箭头
	if (pageIndex == 1) {
		$("#ddFriPrepage").parent().css("display", "none");
		$("#ddFriFirstPage").parent().css("display", "block");
	} else {
		$("#ddFriPrepage").parent().css("display", "block");
		$("#ddFriFirstPage").parent().css("display", "none");
	}
	if (pageIndex >= pageNum) {
		$("#ddFriNextPage").parent().css("display", "none");
		$("#ddFriLastpage").parent().css("display", "block");
	} else {
		$("#ddFriNextPage").parent().css("display", "block");
		$("#ddFriLastpage").parent().css("display", "none");
	}
	// 设置页数的显示效果
	$("#ddFriPageIndexContainer").html(this.getPageIndicate(pageIndex - 1, pageNum));
	/*children().each(function(index, element) {
		if (index == maxPageNum - pageIndex) {
			$(element).attr("src", "/images/plug-in-pic-di-color-1.png");
		} else {
			$(element).attr("src", "/images/plug-in-pic-1.png");
		}
	});*/

	var start = (pageIndex - 1) * pageSize;
	var end = start + pageSize;

	if (end > allFriends.length) {
		end = allFriends.length;
	}
	$("#ddCPFriendsList").html("");
	for ( var i = start; i < end; i++) {
		var friendContainer = $("<li style=\"float:left; width:53px; text-align:center; line-height:30px; float:left;	position:relative; margin:15px 0px 0 11px ;\"></li>");
		var friendImgContainer = $("<span style=\"border:1px solid #dadbd9; float:left;-moz-border-radius: 20px;-khtml-border-radius: 20px;-webkit-border-radius: 20px; padding:2px;border-radius: 20px; \"></span>");
		var friendImg = $("<img src=\""
				+ allFriends[i].avatar.medium
				+ "\" style=\"cursor:pointer;width:47px; border:0; height:47px;float:left; background:#fff; -moz-border-radius: 20px; -khtml-border-radius: 20px; -webkit-border-radius: 20px;  border-radius: 20px;\" />");
		var name = allFriends[i].name;
		if (name.length > 4) {
			name = name.substr(0, 3) + "...";
		}
		var nameElement = $("<a href='#' style=\"font-size:12px; color:#4585bf; text-decoration:none;\" title='"
				+ allFriends[i].name + "'>" + name + "</a>");

		var unSelecteElement = $("<span style=\"position:absolute;left:40px;top:40px;\" id='"
				+ allFriends[i].id
				+ "' name='ddFriUnSelect'><img src=\"/images/plug-in-pic-3.png\"/></span>");
		var selectedElement = $("<span style=\"position:absolute;left:40px;top:40px;\" id='"
				+ allFriends[i].id
				+ "' name='ddFriSelect'><img src=\"/images/plug-in-pic-4.png\"/></span>");

		$("#ddCPFriendsList").append(friendContainer);
		friendContainer.append(friendImgContainer);
		friendImgContainer.append(friendImg);
		friendContainer.append(nameElement);

		var allDDFriValue = $("input[name='allDDFri']:checked").val();

		if (allDDFriValue == 'select') {
			friendContainer.append(selectedElement);
		} else {
			friendContainer.append(unSelecteElement);
		}

		friendImg.click(function() {
			var s = $(this).parent().next().next();
			if (s.attr("name") == "ddFriUnSelect") {
				s.html("<img src=\"/images/plug-in-pic-4.png\"/>");
				s.attr("name", "ddFriSelect");
			} else {
				s.html("<img src=\"/images/plug-in-pic-3.png\"/>");
				s.attr("name", "ddFriUnSelect");
			}

			if ($('span[name*="ddFriSelect"]') != null
					&& $('span[name*="ddFriSelect"]').length > 0) {
				$("#ddStarExtend").show();
				$("#ddCommentExtend").show();
				$("#ddCommentTip").show();
			} else {
				$("#ddStarExtend").hide();
				$("#ddCommentExtend").hide();
				$("#ddCommentTip").hide();
			}
		});
	}
}