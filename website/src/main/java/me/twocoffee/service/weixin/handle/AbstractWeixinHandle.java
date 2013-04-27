package me.twocoffee.service.weixin.handle;

import org.springframework.beans.factory.annotation.Autowired;

import me.twocoffee.entity.WeiXinPublic;
import me.twocoffee.service.WeiXinPublicService;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

public abstract class AbstractWeixinHandle implements WeixinHandle {
	@Autowired
	protected WeiXinPublicService weiXinPublicService;
	protected WeiXinPublicRequestEntity request;
	protected WeiXinPublicResponseEntity response;
	protected String weixinId;
	protected String accountId;
	
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		this.request =  request;
		this.response = response;
		this.response.setWeiXinPublicArticleList(null);
		this.setWeixinId();
		
		return response;
	}
	
	public void setWeixinId() {
		this.weixinId = request.getFromUserName();
	}
	
	public void setAccountId() {
		this.accountId = this.getBindAccountId();
	}
	
	protected String getBindAccountId() {
		String accountId = null;
		WeiXinPublic weixinPublic = weiXinPublicService.getWeiXinPublicByWeiXinId(this.weixinId);
		if(weixinPublic != null) {
			accountId = weixinPublic.getAccountId();
		}
		return accountId;
	}
}
