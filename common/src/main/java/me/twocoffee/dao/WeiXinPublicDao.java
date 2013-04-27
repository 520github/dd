package me.twocoffee.dao;

import me.twocoffee.entity.WeiXinPublic;

public interface WeiXinPublicDao {
	public WeiXinPublic createWeiXinPublic(String accountId,String weixinId);
	
	public WeiXinPublic bindWeiXinPublic(String accountId,String weixinId);
	
	public WeiXinPublic unbindWeiXinPublicByAccountId(String accountId);
	
	public WeiXinPublic unbindWeiXinPublicByWeiXinId(String weixinId);
	
	public WeiXinPublic getWeiXinPublicByAccountId(String accountId);
	
	public WeiXinPublic getWeiXinPublicByWeiXinId(String weixinId);
	
	public WeiXinPublic getWeiXinPublicByAccountIdAndWeiXinId(String accountId, String weixinId);
	
	public boolean isBindWeiXinPublicByWeiXinId(String weixinId);
}
