package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

@Service("generateBindWeixinHandle")
public class GenerateBindWeixinHandle extends AbstractWeixinHandle {
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		this.generateBind(response);
		return response;
	}
	
	private void generateBind(WeiXinPublicResponseEntity response) {
		String bindTip = WeiXinPublicConstant.WEIXIN_TEMPLATE_BIND_TIP.replaceAll(WeiXinPublicConstant.WEIXIN_TEMPLATE_ID, this.weixinId);
		response.setContent(bindTip);
	}
}
