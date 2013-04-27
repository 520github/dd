function deleteAllMessage() {
	jQuery.post("/messages/deleteAll", {}, function(data) {
		if (data == "success") {
			openMessage('success','删除成功');
		}
		else if (data == "failure") {
			openMessage('failure','删除失败');
		}
	});
}

function deleteMessage(messageId) {
	jQuery.post("/messages/delete", {messageId:messageId}, function(data) {
		if (data == "success") {
			openMessage('success','删除成功');
		}
		else if (data == "failure") {
			openMessage('failure','删除失败');
		}
	});
}

//弹出消息框
function openMessage(type,message) {
    openPopupMessage(type,message);           
}

