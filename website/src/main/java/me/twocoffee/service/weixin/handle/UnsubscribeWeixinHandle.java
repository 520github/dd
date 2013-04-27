package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

@Service("unsubscribeWeixinHandle")
public class UnsubscribeWeixinHandle extends AbstractWeixinHandle {
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		this.unsubscribe(response);
		return response;
	}
	
	private void unsubscribe(WeiXinPublicResponseEntity response) {
		String tip = WeiXinPublicConstant.WEIXIN_UN_SUBSCRIBE_TIP;
		response.setContent(tip);
	}
}
