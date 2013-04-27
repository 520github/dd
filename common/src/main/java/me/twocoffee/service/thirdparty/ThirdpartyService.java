package me.twocoffee.service.thirdparty;

import java.util.Date;
import java.util.List;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.DuplicateThirdPartyMessageException;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.thirdparty.impl.ThirdPartyServiceImpl.MessageOperateType;

/**
 * 
 * @author momo
 * 
 */
// TODO:SNS 新增接口
public interface ThirdpartyService {

	long countFriends(ThirdPartyProfile profile);

	ThirdPartyProfile doAuthCallback(ThirdPartyType weibo, String queryStr);

	// ThirdPartyProfile getBindingProfileByAccountId(String accountId,
	// ThirdPartyType type);

	List<ThirdPartyProfile> getByAccountId(String accountId);

	ThirdPartyProfile getByAccountId(String accountId, ThirdPartyType type);

	/**
	 * 获取使用第三方帐号的用户
	 * 
	 * @param type
	 *            第三方类型
	 * @param userId
	 *            第三方标识
	 * @return
	 */
	Account getByThirdParty(ThirdPartyType type, String userId);

	Account getByThirdParty(ThirdPartyType type, String userId, boolean bind);

	ThirdPartyProfile getByUidAndType(String uid, ThirdPartyType type);

	Contact getContact(String id, ThirdPartyType thirdPartyType, String uid);

	Contact getContact(String id, ThirdPartyType thirdPartyType,
			String[] mobile);

	List<Contact> getContacts(String accountId);
	
	List<Contact> getContacts(String accountId,
			ThirdPartyType thirdPartyType, List<RelationType> relations,
			Integer limit, Integer offset);
	
	List<Contact> getContacts(String accountId,
			List<ThirdPartyType> thirdPartyTypes, List<RelationType> relations,
			Integer limit, Integer offset);

	List<Contact> getContacts(String accountId,
			ThirdPartyType thirdPartyType);

	int getFriendsNumber(String accountId, ThirdPartyType thirdpartyType);

	Account getLoginAccountByThirdParty(ThirdPartyType type, String userId);

	ThirdPartyProfile getLoginThirdParty(ThirdPartyType type, String userId);

	String getShortUrl(String url);

	Contact getThirdPartyFriend(String accountId, ThirdPartyType type,
			String friendUid);

	List<Contact> getThirdPartyFriends(ThirdPartyProfile thirdPartyProfile)
			throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
			ThirdPartyUnbindException;

	List<Contact> getThirdPartyFriends(ThirdPartyProfile thirdPartyProfile,
			int limit, int offset)
			throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
			ThirdPartyUnbindException;

	int removeContact(ThirdPartyType thirdPartyType, String accountId);

	void save(ThirdPartyProfile thirdPartyProfile);

	void saveContacts(List<Contact> contacts);

	void send2ThirdParty(String from, ThirdPartyPostMessage content, MessageOperateType type)
			throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
			ThirdPartyUnbindException, DuplicateThirdPartyMessageException;

	void syncThirdPartyContent(ThirdPartyProfile thirdpartyProfile)
			throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
			ThirdPartyUnbindException;
	
	public List<ThirdPartyProfile> getByThirdpartyType(ThirdPartyType type,boolean bind, Date max, Date min,
			int limit, int offset);

	/**
	 * @param thirdpartyProfile
	 */
	void updateToken(ThirdPartyProfile thirdpartyProfile);

	void removeProfile(ThirdPartyProfile p);

	List<ThirdPartyProfile> getByThirdpartyTypes(List<ThirdPartyType> types,
			int limit, int offset);

	void saveContentSynchronizeLog(
			ThirdPartyContentSynchronizeLog contentSynchronizeLog);


}
