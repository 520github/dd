package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

@Service("helpWeixinHandle")
public class HelpWeixinHandle extends AbstractWeixinHandle {
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		this.help(response);
		return response;
	}
	
	private void help(WeiXinPublicResponseEntity response) {
		String help = WeiXinPublicConstant.WEIXIN_TEMPLATE_HELP_TIP.replaceAll(WeiXinPublicConstant.WEIXIN_TEMPLATE_ID, this.weixinId);
		response.setContent(help);
	}
}
