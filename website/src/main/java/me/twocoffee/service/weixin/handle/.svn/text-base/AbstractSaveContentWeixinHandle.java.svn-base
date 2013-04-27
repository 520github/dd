package me.twocoffee.service.weixin.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.WeiXinPublic;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent;
import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity.MessageType;

public abstract class AbstractSaveContentWeixinHandle extends AbstractWeixinHandle {
	@Autowired
	protected ContentRpcService contentService;
	protected PostContent postContent = null;
	
	
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		this.setCommonPostContent(request);
		this.setPostContent(request);
		this.add2Repository(request, postContent);
		
		return response;
	}
	
	protected void setCommonPostContent(WeiXinPublicRequestEntity request) {
		if(postContent == null)postContent = new PostContent();
		
		String messageType = request.getMessageType();
		if(MessageType.text.toString().equalsIgnoreCase(messageType)) {
			postContent.setContentType(ContentType.HtmlClip);
		}
		else if(MessageType.image.toString().equalsIgnoreCase(messageType)) {
			postContent.setContentType(ContentType.Image);
		}
		else if(MessageType.link.toString().equalsIgnoreCase(messageType)) {
			postContent.setContentType(ContentType.Web);
		}
		List<String> tagList = new ArrayList<String>();
		tagList.add(SystemTagEnum.Unread.toString());
		tagList.add(SystemTagEnum.Source_Weixin_Public.toString());
		postContent.setTag(tagList);
		
		String title = request.getContent();
		if(StringUtils.isEmpty(title))title = "来自我微信的收藏";
		if(title.length() > WeiXinPublicConstant.WEIXIN_TITLE_MAX_LENGTH)title = title.substring(0,WeiXinPublicConstant.WEIXIN_TITLE_MAX_LENGTH);
		postContent.setTitle(title);
		postContent.setUrl("http://www.mduoduo.com");
	}
	
	protected abstract void setPostContent(WeiXinPublicRequestEntity request);
	
	private void add2Repository(WeiXinPublicRequestEntity request,PostContent postContent) {
		WeiXinPublic wxp = weiXinPublicService.getWeiXinPublicByWeiXinId(this.weixinId);
		String accountId = wxp.getAccountId();
		if(wxp == null || StringUtils.isEmpty(accountId)) {
			return ;
		}
		try {
			contentService.postContent(TokenUtil.ACCOUNT_ID + " " + accountId, postContent);
			response.setContent(WeiXinPublicConstant.WEIXIN_CONTENT_SAVE_SUCCESS);
		} catch (Exception e) {
			response.setContent(WeiXinPublicConstant.WEIXIN_CONTENT_SAVE_FAILURE);
		}
	}
}
