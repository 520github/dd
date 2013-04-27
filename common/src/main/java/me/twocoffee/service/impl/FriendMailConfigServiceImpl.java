/**
 * 
 */
package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.twocoffee.dao.FriendDao;
import me.twocoffee.dao.FriendMailConfigDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.FriendMailConfigService;
import me.twocoffee.service.entity.FriendMail;

/**
 * 白名单管理服务
 * 
 * @author xuehui.miao
 *
 */
@Service
public class FriendMailConfigServiceImpl implements FriendMailConfigService {
	@Autowired
	private FriendMailConfigDao friendMailConfigDao;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private FriendDao friendDao;
	
	@Override
	public FriendMailConfig getFriendMailConfig(String accountId, String friendId) {
		return friendMailConfigDao.getFriendMailConfig(accountId, friendId);
	}
	
	@Override
	public List<FriendMail> getFriendMailList(String accountId) {
		List<FriendMailConfig> configList = this.getFriendMailConfigList(accountId);
		//如果为空，看是否有好友，有好友的话就先进行初始化
		if(configList == null || configList.size() < 1) {
			List<Friend> friendList = friendDao.findFriends(accountId);
			if(friendList == null || friendList.size() < 1)return null;
			
			this.saveFriendMailConfig(accountId);
			configList = this.getFriendMailConfigList(accountId);
		}
		
		if(configList == null || configList.size() < 1) {
			return null;
		}
		
		List<FriendMail> mailList = new ArrayList<FriendMail>();
		for (int i = 0; i < configList.size(); i++) {
			FriendMailConfig config = configList.get(i);
			if(config == null)continue;
			
			Account account = accountService.getById(config.getFriendId());
			if(account == null) {
				continue;
			}
			AccountMailConfig mailConfig = account.getMailConfig();
			if(mailConfig == null) mailConfig = new AccountMailConfig();
			List<String> mails = mailConfig.getMails();
			if(mails == null)mails = new ArrayList<String>();
			if(mails.size() < 1) {
				if(account.getEmail() !=null && account.getEmail().trim().length() > 0) {
					mails.add(account.getEmail());
				}
			}
			
			if(mails.size() < 1) {
				continue;
			}
			
			FriendMail mail = new FriendMail();
			mail.setId(config.getId());
			mail.setAccountId(config.getAccountId());
			mail.setFriendId(config.getFriendId());
			mail.setBlocked(config.isBlocked());
			mail.setName(account.getName());//好友昵称
			mail.setFriendMails(mails);//好友邮箱
			
			mailList.add(mail);
		}
		
		return mailList;
	}
	/* (non-Javadoc)
	 * @see me.twocoffee.service.FriendMailConfigService#getFriendMailConfigList(java.lang.String)
	 */
	@Override
	public List<FriendMailConfig> getFriendMailConfigList(String accountId) {
		List<FriendMailConfig> friendMailList = friendMailConfigDao.getFriendMailConfigList(accountId);
		List<Friend> friendList = friendDao.findFriends(accountId);
		if(friendList == null || friendList.size() < 1)return friendMailList;
		
		if(friendMailList != null && friendList != null 
				&& friendMailList.size() == friendList.size()) {
			return friendMailList;
		}
		
		for (int i = 0; i < friendList.size(); i++) {
			Friend friend = friendList.get(i);
			if(friend == null)continue;
			String friendId = friend.getFriendId();
			if(friendId == null || friendId.trim().length() < 1)continue;
			if(this.isContainFriend(friendMailList, friendId))continue;
			
			FriendMailConfig friendMailConfig = new FriendMailConfig();
			friendMailConfig.setAccountId(accountId);
			friendMailConfig.setFriendId(friendId);
			friendMailConfig.setBlocked(false);
			this.saveFriendMailConfig(friendMailConfig);
		}
		friendMailList = friendMailConfigDao.getFriendMailConfigList(accountId);
		return friendMailList;
	}
	
	private boolean isContainFriend(List<FriendMailConfig> friendMailList, String friendId) {
		boolean isContain = false;
		if(friendMailList == null || friendMailList.size() < 1)return isContain;
		for (int i = 0; i < friendMailList.size(); i++) {
			FriendMailConfig config = friendMailList.get(i);
			if(config == null)continue;
			if(friendId.equalsIgnoreCase(config.getFriendId())) {
				return true;
			}
		}
		
		return isContain;
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.service.FriendMailConfigService#saveFriendMailConfig(me.twocoffee.entity.FriendMailConfig)
	 */
	@Override
	public void saveFriendMailConfig(FriendMailConfig friendMailConfig) {
		friendMailConfigDao.saveFriendMailConfig(friendMailConfig);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.service.FriendMailConfigService#saveFriendMailConfig(java.lang.String)
	 */
	@Override
	public void saveFriendMailConfig(String accountId) {
		friendMailConfigDao.saveFriendMailConfig(accountId);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.service.FriendMailConfigService#updateFriendMailConfig(me.twocoffee.entity.FriendMailConfig)
	 */
	@Override
	public void updateFriendMailConfig(FriendMailConfig friendMailConfig) {
		friendMailConfigDao.updateFriendMailConfig(friendMailConfig);
	}
	
	@Override
	public void removeFriendMailConfig(String accountId, String friendId) {
		friendMailConfigDao.removeFriendMailConfig(accountId, friendId);
	}

}
