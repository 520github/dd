package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

@Service("unknownWeixinHandle")
public class UnknownWeixinHandle extends AbstractWeixinHandle {
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		response.setContent(WeiXinPublicConstant.WEIXIN_UNKNOWN_CONTENT);
		return response;
	}
}
