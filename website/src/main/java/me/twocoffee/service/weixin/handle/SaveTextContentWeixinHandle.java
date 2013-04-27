package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;

@Service("saveTextContentWeixinHandle")
public class SaveTextContentWeixinHandle extends
		AbstractSaveContentWeixinHandle {
	
	@Override
	protected void setPostContent(WeiXinPublicRequestEntity request) {
		String content = request.getContent();
		HtmlPayload htmlPayload = new HtmlPayload();
		htmlPayload.setContent(content);
		postContent.setHtmlPayload(htmlPayload);
	}
}
