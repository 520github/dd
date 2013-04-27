package me.twocoffee.dao.impl;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.WeiXinPublicDao;
import me.twocoffee.entity.WeiXinPublic;

@Repository
public class WeiXinPublicDaoImpl extends BaseDao<WeiXinPublic> implements WeiXinPublicDao {

	@Override
	public WeiXinPublic bindWeiXinPublic(String accountId, String weixinId) {
		WeiXinPublic wx = this.getWeiXinPublicByWeiXinId(weixinId);
		if(wx == null) {
			wx = this.createWeiXinPublic(accountId, weixinId);
		}
		else {
			wx.setAccountId(accountId);
			wx.setLastModified(new Date());
			this.save(wx);
		}
		return wx;
	}

	@Override
	public WeiXinPublic createWeiXinPublic(String accountId, String weixinId) {
		WeiXinPublic wx = new WeiXinPublic();
		wx.setAccountId(accountId);
		wx.setWeixinId(weixinId);
		wx.setDate(new Date());
		wx.setLastModified(wx.getDate());
		wx.setId(new ObjectId().toString());
		this.save(wx);
		return wx;
	}

	@Override
	public WeiXinPublic getWeiXinPublicByAccountId(String accountId) {
		return this.createQuery().filter("accountId", accountId).get();
	}

	@Override
	public WeiXinPublic getWeiXinPublicByWeiXinId(String weixinId) {
		return this.createQuery().filter("weixinId", weixinId).get();
	}
	@Override
	public WeiXinPublic getWeiXinPublicByAccountIdAndWeiXinId(String accountId, String weixinId) {
		return this.createQuery().filter("accountId", accountId).filter("weixinId", weixinId).get();
	}

	@Override
	public WeiXinPublic unbindWeiXinPublicByAccountId(String accountId) {
		WeiXinPublic wx = this.getWeiXinPublicByAccountId(accountId);
		if(wx == null)return null;
		wx.setWeixinId("");
		wx.setLastModified(new Date());
		this.save(wx);
		return wx;
	}

	@Override
	public WeiXinPublic unbindWeiXinPublicByWeiXinId(String weixinId) {
		WeiXinPublic wx = this.getWeiXinPublicByWeiXinId(weixinId);
		if(wx == null)return null;
		wx.setWeixinId("");
		wx.setLastModified(new Date());
		this.save(wx);
		return wx;
	}
	
	@Override
	public boolean isBindWeiXinPublicByWeiXinId(String weixinId) {
		boolean isBind = false;
		WeiXinPublic wx = this.getWeiXinPublicByWeiXinId(weixinId);
		if(wx != null) {
			return true;
		}
		return isBind;
	}

}
