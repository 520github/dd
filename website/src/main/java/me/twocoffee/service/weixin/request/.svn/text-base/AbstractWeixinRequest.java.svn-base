package me.twocoffee.service.weixin.request;

import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity.MessageType;

public abstract class AbstractWeixinRequest implements WeixinRequest {
	protected WeiXinPublicResponseEntity response = null;
	
	@Override
	public WeiXinPublicResponseEntity weixinRequest(
			WeiXinPublicRequestEntity request) {
		this.setBaseResposne(request);
		return response;
	}
	
	protected void setBaseResposne(WeiXinPublicRequestEntity request) {
		if(response == null) response = new WeiXinPublicResponseEntity();
		if(request == null)return;
		response.setToUserName(request.getFromUserName());
		response.setFromUserName(request.getToUserName());
		response.setCreateTime(System.currentTimeMillis());
		response.setFunctionFlag(0);
		response.setMessageType(MessageType.text.toString());
	}

}
