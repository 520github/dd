package me.twocoffee.service;

import me.twocoffee.entity.WeiXinPublic;

public interface WeiXinPublicService {
	public String generateBindUrl(String weixinId);
	
	public boolean isBindWeiXinPublicByWeiXinId(String weixinId);
	
	public WeiXinPublic bindWeiXinPublic(String accountId, String weixinId);
	
	public void unbindWeiXinPublicByWeiXinId(String weixinId);
	
	public WeiXinPublic getWeiXinPublicByWeiXinId(String weixinId);
}
