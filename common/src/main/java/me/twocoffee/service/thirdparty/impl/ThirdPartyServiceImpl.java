/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.twocoffee.common.auth.AuthClient;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.ContactDao;
import me.twocoffee.dao.ThirdPartyContentDao;
import me.twocoffee.dao.ThirdPartyContentSynchronizeLogDao;
import me.twocoffee.dao.ThirdPartyProfileDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.DuplicateThirdPartyMessageException;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.thirdparty.ThirdPartyClient;
import me.twocoffee.service.thirdparty.ThirdpartyService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class ThirdPartyServiceImpl implements ThirdpartyService {

    public static enum MessageOperateType {
	Invite, Broadcast, Share
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ThirdPartyServiceImpl.class);

    private Map<ThirdPartyType, ThirdPartyClient> thirdPartyClients = new HashMap<ThirdPartyType, ThirdPartyClient>();

    public Map<ThirdPartyType, ThirdPartyClient> getThirdPartyClients() {
	return thirdPartyClients;
    }

    public void setThirdPartyClients(
	    Map<ThirdPartyType, ThirdPartyClient> thirdPartyClients) {
	this.thirdPartyClients = thirdPartyClients;
    }

    private final Pattern imgPattern = Pattern.compile(
	    "<[Ii][Mm][Gg][^\"]*\"([^\"]*)\"[^>]*>", Pattern.CASE_INSENSITIVE);

    @Autowired
    private AuthClient authClient;

    @Autowired
    protected FileOperator fileOperator;

    @Autowired
    private final AccountDao dao = null;

    @Autowired
    private ThirdPartyProfileDao thirdProfileDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ThirdPartyContentDao thirdPartyContentDao;

    @Autowired
    private ThirdPartyContentSynchronizeLogDao contentSynchronizeLogDao;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private ContentSearcher contentSearcher;

    private void collectContent(ThirdPartyProfile thirdpartyProfile) {
	Date syncDate = thirdpartyProfile.getSyncDate();

	if (!syncDate.after(new Date(1000))) {
	    ThirdPartyContentSynchronizeLog contentSynchronizeLog = findThirdPartyContentSynchronizeLog(thirdpartyProfile);

	    if (contentSynchronizeLog != null) {
		syncDate = contentSynchronizeLog.getSysnDate();
	    }
	}
	Date latestDate = syncDate;
	List<ThirdPartyContent> contents = thirdPartyContentDao.findAfter(
		thirdpartyProfile.getAccountType(),
		thirdpartyProfile.getUserId(), syncDate);

	Set<Date> favoriteDateSet = new HashSet<Date>();

	for (ThirdPartyContent c : contents) {

	    if (favoriteDateSet.contains(c.getFavoriteTime())) {
		LOGGER.debug(
			"find dumplicate thirdparty content! thirdtype {} account {} uid {}",
			new Object[] { thirdpartyProfile.getAccountType(),
				thirdpartyProfile.getAccountId(),
				thirdpartyProfile.getUserId() });

		continue;
	    }
	    favoriteDateSet.add(c.getFavoriteTime());
	    Content duoduoContent = new Content();
	    duoduoContent.setContentType(ContentType.HtmlClip);
	    duoduoContent.setTitle(c.getTitle());
	    duoduoContent.setUrl(c.getRealurl());
	    HtmlPayload hp = new HtmlPayload();
	    hp.setContent(thirdPartyClients.get(
		    thirdpartyProfile.getAccountType()).getContentDisplay(c));

	    hp = setHtmlAttachment(hp);
	    duoduoContent.setHtmlPayload(hp);
	    // 从内容中提取摘要
	    duoduoContent.setSummary(thirdPartyClients.get(
		    thirdpartyProfile.getAccountType()).getSummaryDisplay(c));

	    contentService.save(duoduoContent);
	    // 加入抓取队列，等待抓
	    fetchService.addToFetch(duoduoContent);
	    Repository repository = repositoryService
		    .getRepositoryByContentIdAndAccountId(
			    duoduoContent.getId(),
			    thirdpartyProfile.getAccountId());

	    if (repository == null) {// 第一次收藏，添加关系
		repository = new Repository();
		repository.setAccountId(thirdpartyProfile.getAccountId());
		repository.setContentId(duoduoContent.getId());
		repository.setTag(c.getTags(), duoduoContent.getContentType()
			.toString());// 系统标签

		repository.setDate(c.getFavoriteTime());
		repository.setLastModified(new Date());

	    } else {// 重新收藏，重新设置 日期
		repository.setDate(c.getFavoriteTime());
		repository.setLastModified(new Date());
		// 重新设置系统标签
		List<String> tagList = new ArrayList<String>();

		if (repositoryService.isContainTag(c.getTags(),
			SystemTagEnum.Collect.toString())) {

		    tagList.add(SystemTagEnum.Collect.toString());
		}
		if (repositoryService.isContainTag(c.getTags(),
			SystemTagEnum.Later.toString())) {

		    tagList.add(SystemTagEnum.Later.toString());
		}
		repository.setTag(repositoryService.getTag(tagList,
			repository.getTag(), "cover"));

	    }
	    repositoryService.save(repository);
	    // 添加索引
	    ContentDetail contentDetail = new ContentDetail();
	    contentDetail.setContent(duoduoContent);
	    contentDetail.setRepository(repository);
	    contentSearcher.addIndex(contentDetail);

	    if (c.getFavoriteTime().after(latestDate)) {
		latestDate = c.getFavoriteTime();
	    }
	}

	if (latestDate.after(syncDate)) {
	    thirdpartyProfile.setSyncDate(latestDate);
	    thirdProfileDao.save(thirdpartyProfile);
	}
    }

    // private String getTemplate(String string) {
    // BufferedReader reader = null;
    // StringBuilder builder = new StringBuilder();
    //
    // try {
    // reader = new BufferedReader(new FileReader(string));
    //
    // String tempString = null;
    //
    // while ((tempString = reader.readLine()) != null) {
    // builder.append(tempString);
    // }
    // reader.close();
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    //
    // } finally {
    //
    // if (reader != null) {
    //
    // try {
    // reader.close();
    //
    // } catch (IOException e1) {
    // }
    // }
    // }
    // return builder.toString();
    // }

    public ThirdPartyContentSynchronizeLog findThirdPartyContentSynchronizeLog(
	    ThirdPartyProfile thirdpartyProfile) {

	return contentSynchronizeLogDao
		.findThirdPartyContentSynchronizeLog(thirdpartyProfile);
    }

    private HtmlPayload setHtmlAttachment(HtmlPayload hp) {
	List<Attachment> list = hp.getAttachment();
	if (list == null) {
	    list = new ArrayList<Attachment>();
	}
	String c = hp.getContent();
	Matcher m = imgPattern.matcher(c);
	while (m.find()) {
	    String url = m.group(1);
	    if (url == null || !url.startsWith("http://"))
		continue;

	    Attachment a = new Attachment();
	    a.setArchiveUrl(m.group(1));
	    a.setOrgUrl(m.group(1));

	    list.add(0, a);
	}
	hp.setAttachment(list);
	return hp;
    }

    boolean hasError(JSONObject object) {

	if (object.containsKey("error_code")) {
	    return true;
	}
	return false;
    }

    @Override
    public long countFriends(ThirdPartyProfile profile) {
	return contactDao.count(profile.getAccountId(),
		profile.getAccountType());

    }

    @Override
    public ThirdPartyProfile doAuthCallback(ThirdPartyType weibo,
	    String queryStr) {

	return authClient.doCallback(weibo.toString(), queryStr);
    }

    @Override
    public List<ThirdPartyProfile> getByAccountId(String accountId) {

	if (StringUtils.isBlank(accountId)) {
	    return null;
	}
	return thirdProfileDao.findByAccountId(accountId);
    }

    @Override
    public ThirdPartyProfile getByAccountId(String accountId,
	    ThirdPartyType type) {

	return thirdProfileDao.findByTypeAndAccountId(type, accountId);
    }

    @Override
    public Account getByThirdParty(ThirdPartyType type, String userId) {
	ThirdPartyProfile profile = thirdProfileDao.findByTypeAndId(type,
		userId);

	if (profile == null) {
	    return null;
	}
	return dao.getById(profile.getAccountId());
    }

    @Override
    public Account getByThirdParty(ThirdPartyType type, String userId,
	    boolean bind) {

	ThirdPartyProfile profile = thirdProfileDao.findByTypeAndId(type,
		userId, bind);

	if (profile == null) {
	    return null;
	}
	return dao.getById(profile.getAccountId());
    }

    @Override
    public List<ThirdPartyProfile> getByThirdpartyType(ThirdPartyType type,
	    boolean bind, Date max, Date min, int limit, int offset) {

	return thirdProfileDao.findByTypeAndAccountId(type, bind, max, min,
		limit, offset);
    }

    @Override
    public ThirdPartyProfile getByUidAndType(String uid, ThirdPartyType type) {
	return thirdProfileDao.findByTypeAndId(type, uid);
    }

    @Override
    public Contact getContact(String id, ThirdPartyType thirdPartyType,
	    String uid) {

	return contactDao.get(id, thirdPartyType, uid);
    }

    @Override
    public Contact getContact(String id, ThirdPartyType thirdPartyType,
	    String[] mobile) {

	return contactDao.get(id, thirdPartyType, mobile);
    }

    @Override
    public List<Contact> getContacts(String accountId) {
	return contactDao.find(accountId);
    }

    @Override
    public List<Contact> getContacts(String accountId,
	    ThirdPartyType thirdPartyType, List<RelationType> rtypes,
	    Integer limit, Integer offset) {
	List<ThirdPartyType> thirdPartyTypes = new ArrayList<ThirdPartyType>();
	thirdPartyTypes.add(thirdPartyType);
	return contactDao.find(accountId, thirdPartyTypes, rtypes, limit,
		offset);
    }

    @Override
    public List<Contact> getContacts(String accountId,
	    List<ThirdPartyType> thirdPartyTypes, List<RelationType> rtypes,
	    Integer limit, Integer offset) {

	return contactDao.find(accountId, thirdPartyTypes, rtypes, limit,
		offset);
    }

    @Override
    public List<Contact> getContacts(String accountId,
	    ThirdPartyType thirdPartyType) {

	return contactDao.find(accountId, thirdPartyType, 0, 0);
    }

    @Override
    public int getFriendsNumber(String accountId, ThirdPartyType thirdpartyType) {
	ThirdPartyProfile p = thirdProfileDao.findByTypeAndAccountId(
		thirdpartyType, accountId);

	if (p.getProfile() != null) {
	    return thirdPartyClients.get(thirdpartyType).getFriendNumber(p);
	}
	return 0;
    }

    @Override
    public Account getLoginAccountByThirdParty(ThirdPartyType type,
	    String userId) {

	ThirdPartyProfile p = getLoginThirdParty(type, userId);

	if (p != null) {
	    return dao.getById(p.getAccountId());
	}
	return null;
    }

    @Override
    public ThirdPartyProfile getLoginThirdParty(ThirdPartyType type,
	    String userId) {

	return thirdProfileDao.getByTypeAndID(type, userId, true);
    }

    @Override
    public String getShortUrl(String longUrl) {

	try {
	    if (longUrl == null)
		longUrl = "";
	    if (longUrl.indexOf("?") == -1) {
		longUrl += "?r=" + RandomUtils.nextInt();
	    } else if (!longUrl.endsWith("&")) {
		longUrl += "&r=" + RandomUtils.nextInt();
	    } else {
		longUrl += "r=" + RandomUtils.nextInt();
	    }
	    // longUrl = URLEncoder.encode(longUrl, "UTF-8");
	    String source = authClient.getConsumerProperties().getConsumer(
		    ThirdPartyType.Weibo.toString()).consumerKey;
	    // String source = "3525403958";
	    String url = "https://api.weibo.com/2/short_url/shorten.json";
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("source", source);
	    params.put("url_long", longUrl);
	    JSONObject obj = AuthUtils.invoke("Get", url,
		    AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params);

	    if (hasError(obj)) {
		throw new ThirdPartyAccessException(obj.getString("error_code"));
	    }
	    JSONObject o = obj.getJSONArray("urls").getJSONObject(0);
	    return o.getString("url_short");

	}
	// catch (MalformedURLException e) {
	// throw new RuntimeException(e);
	//
	// }
	catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Contact getThirdPartyFriend(String accountId, ThirdPartyType type,
	    String friendUid) {

	return contactDao.get(accountId, type, friendUid);
    }

    @Override
    public List<Contact> getThirdPartyFriends(ThirdPartyProfile profile)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
	    ThirdPartyUnbindException {

	if (profile.isBind() == false) {
	    throw new ThirdPartyUnbindException();
	}
	ThirdPartyClient thirdPartyClient = thirdPartyClients.get(profile
		.getAccountType());

	if (thirdPartyClient == null) {
	    return null;
	}

	return thirdPartyClient.getFriends(profile);
    }

    @Override
    public List<Contact> getThirdPartyFriends(
	    ThirdPartyProfile thirdPartyProfile, int limit, int offset)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
	    ThirdPartyUnbindException {

	return contactDao.find(thirdPartyProfile.getAccountId(),
		thirdPartyProfile.getAccountType(), limit, offset);

    }

    @Override
    public int removeContact(ThirdPartyType thirdPartyType, String accountId) {
	return contactDao.remove(accountId, thirdPartyType);
    }

    @Override
    public void save(ThirdPartyProfile profile) {
	thirdProfileDao.save(profile);
    }

    @Override
    public void saveContacts(List<Contact> contacts) {
	contactDao.save(contacts);
    }

    @Override
    public void send2ThirdParty(String from, ThirdPartyPostMessage content,
	    MessageOperateType type) throws ThirdPartyAuthExpiredException,
	    ThirdPartyAccessException, ThirdPartyUnbindException,
	    DuplicateThirdPartyMessageException {

	ThirdPartyProfile profile = thirdProfileDao.findByTypeAndAccountId(
		content.getThirdPartyType(), from);

	if (profile == null || profile.isBind() == false) {
	    throw new ThirdPartyUnbindException();
	}

	if (StringUtils.isNotBlank(content.getUuid())
		&& content.getUuid().equals(profile.getUuid())) {
	    throw new DuplicateThirdPartyMessageException();
	}
	ThirdPartyClient client = thirdPartyClients.get(content
		.getThirdPartyType());

	switch (type) {
	case Invite:
	    client.invite(profile, content);
	    break;
	case Broadcast:
	    client.broadcast(profile, content);
	    break;
	case Share:
	    client.share(profile, content);
	    break;
	default:
	    throw new InvalidParameterException("unknow MessageOperateType "
		    + type);
	}
    }

    @Override
    public void syncThirdPartyContent(ThirdPartyProfile thirdpartyProfile)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException,
	    ThirdPartyUnbindException {

	if (thirdpartyProfile.isBind() && thirdpartyProfile.isSyncContent()) {
	    ThirdPartyClient client = thirdPartyClients.get(thirdpartyProfile
		    .getAccountType());

	    List<ThirdPartyContent> contents = client
		    .getContents(thirdpartyProfile);
	    // List<ThirdPartyContent> contents =
	    // getThirdPartyContent(thirdpartyProfile);

	    if (contents != null && contents.size() > 0) {
		thirdPartyContentDao.save(contents);
	    }
	    collectContent(thirdpartyProfile);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * me.twocoffee.service.ThirdpartyService#updateToken(me.twocoffee.entity
     * .ThirdPartyProfile)
     */
    @Override
    public void updateToken(ThirdPartyProfile thirdpartyProfile) {
	this.thirdProfileDao.updateToken(thirdpartyProfile);
    }

    @Override
    public void removeProfile(ThirdPartyProfile p) {
	this.thirdProfileDao.delete(p);
    }

    @Override
    public List<ThirdPartyProfile> getByThirdpartyTypes(
	    List<ThirdPartyType> types, int limit, int offset) {

	return thirdProfileDao.getByTypes(types, limit, offset);
    }

    @Override
    public void saveContentSynchronizeLog(
	    ThirdPartyContentSynchronizeLog contentSynchronizeLog) {

	contentSynchronizeLogDao
		.saveContentSynchronizeLog(contentSynchronizeLog);
    }

}
