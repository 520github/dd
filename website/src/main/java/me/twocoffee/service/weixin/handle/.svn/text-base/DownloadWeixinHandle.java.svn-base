package me.twocoffee.service.weixin.handle;

import org.springframework.stereotype.Service;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

@Service("downloadWeixinHandle")
public class DownloadWeixinHandle extends AbstractWeixinHandle {
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		this.download(response);
		return response;
	}
	
	private void download(WeiXinPublicResponseEntity response) {
		String download = WeiXinPublicConstant.WEIXIN_DOWNLOAD_URL_A;
		response.setContent(download);
	}
}
