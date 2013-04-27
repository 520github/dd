package me.twocoffee.service.weixin;

public interface WeixinService {
	public WeiXinPublicResponseEntity request(WeiXinPublicRequestEntity request);
	
	public WeiXinPublicResponseEntity handle(WeiXinPublicRequestEntity request, WeiXinPublicResponseEntity response);
	
	public WeiXinPublicResponseEntity response(WeiXinPublicRequestEntity request, WeiXinPublicResponseEntity response);
}
