package me.twocoffee.service.weixin.request;

import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

public interface WeixinRequest {
	public WeiXinPublicResponseEntity weixinRequest(WeiXinPublicRequestEntity request);
}
