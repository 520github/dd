package me.twocoffee.service.weixin;

import me.twocoffee.service.weixin.handle.WeixinHandle;
import me.twocoffee.service.weixin.request.WeixinRequest;
import me.twocoffee.service.weixin.response.WeixinResponse;

public class WeixinServiceImpl implements WeixinService {
	private WeixinRequest weixinRequest;
	private WeixinHandle handle;
	private WeixinResponse weixinResponse;
	
	public WeixinServiceImpl(WeixinRequest request,WeixinHandle handle,WeixinResponse response) {
		this.weixinRequest = request;
		this.handle = handle;
		this.weixinResponse = response;
	}
	
	@Override
	public WeiXinPublicResponseEntity handle(WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		return handle.weixinHandle(request, response);
	}

	@Override
	public WeiXinPublicResponseEntity request(WeiXinPublicRequestEntity request) {
		return weixinRequest.weixinRequest(request);
	}

	@Override
	public WeiXinPublicResponseEntity response(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		return weixinResponse.weixinResponse(request, response);
	}
	
	public WeiXinPublicResponseEntity handleWeixin(WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		response = this.request(request);
		response = this.handle(request, response);
		response = this.response(request, response);
		
		return response;
	}

}
