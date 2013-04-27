package me.twocoffee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.dao.WeiXinPublicDao;
import me.twocoffee.entity.WeiXinPublic;
import me.twocoffee.service.WeiXinPublicService;

@Service
public class WeiXinPublicServiceImpl implements WeiXinPublicService {
	private static final String WEIXIN_BIND_URL = SystemConstant.domainName +"/weixin/";
	
	@Autowired
	private WeiXinPublicDao weiXinPublicDao;
	
	@Override
	public WeiXinPublic bindWeiXinPublic(String accountId, String weixinId) {
		return weiXinPublicDao.bindWeiXinPublic(accountId, weixinId);
	}

	@Override
	public String generateBindUrl(String weixinId) {
		String bindUrl = WEIXIN_BIND_URL + weixinId;
		return bindUrl;
	}

	@Override
	public boolean isBindWeiXinPublicByWeiXinId(String weixinId) {
		return weiXinPublicDao.isBindWeiXinPublicByWeiXinId(weixinId);
	}

	@Override
	public void unbindWeiXinPublicByWeiXinId(String weixinId) {
		weiXinPublicDao.unbindWeiXinPublicByWeiXinId(weixinId);
	}
	
	@Override
	public WeiXinPublic getWeiXinPublicByWeiXinId(String weixinId) {
		return weiXinPublicDao.getWeiXinPublicByWeiXinId(weixinId);
	}

}
