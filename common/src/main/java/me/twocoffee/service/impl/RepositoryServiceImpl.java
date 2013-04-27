package me.twocoffee.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.dao.PublicRepositoryDao;
import me.twocoffee.dao.RepositoryDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Comment;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.Counter;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.entity.PublicRepository;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.Repository.FriendShare;
import me.twocoffee.entity.StatisticTag;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.CommentService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ContentAndAccount;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.entity.ContentDetail.Avatar;
import me.twocoffee.service.entity.ContentDetail.From;
import me.twocoffee.service.event.ReadLaterEvent;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class RepositoryServiceImpl implements RepositoryService,
	ApplicationContextAware {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentSearcher contentSearcher;

    @Autowired
    private RepositoryDao repositoryDao;

    @Autowired
    private PublicRepositoryDao publicRepositoryDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThirdpartyService thirdPartyService;

    @Autowired
    private InviteService inviteService;
    
    private static Map<String,String> imageReplaceMap = new HashMap<String,String>();
	//test WeiXin
    static {
		imageReplaceMap.put("file.mduoduo.com", "2cofee.com");
	}
	private static boolean isReplaceImage = false;

    private final SystemTagEnum[] sourceTags = new SystemTagEnum[] {
	    SystemTagEnum.Source_Friend, SystemTagEnum.Source_Plugin,
	    SystemTagEnum.Source_Square, SystemTagEnum.Source_Subscription,
	    SystemTagEnum.Source_Mail, SystemTagEnum.Source_Upload,
	    SystemTagEnum.Source_Clip, SystemTagEnum.Source_Renren,
	    SystemTagEnum.Source_Weibo
    };

    private final SystemTagEnum[] folderTags = new SystemTagEnum[] {
	    SystemTagEnum.Collect, SystemTagEnum.Later,
	    SystemTagEnum.Read, SystemTagEnum.Source_Friend,
	    SystemTagEnum.Delete_Collect, SystemTagEnum.Unread,
	    SystemTagEnum.Source_Upload
    };

    private final SystemTagEnum[] typeTags = new SystemTagEnum[] {
	    SystemTagEnum.Type_HtmlClip, SystemTagEnum.Type_Image,
	    SystemTagEnum.Type_Product, SystemTagEnum.Type_Web,
	    SystemTagEnum.Type_Video, SystemTagEnum.Type_File
    };

    private void addLaterEvent2Listener(Repository repository) {
	if (repository.getTag() == null || repository.getTag().size() < 1) {
	    return;
	}
	// 如果是稍后看,推送通知及消息
	for (String tag : repository.getTag()) {
	    if (SystemTagEnum.Later.toString().equalsIgnoreCase(tag)) {
		ReadLaterEvent event = new ReadLaterEvent(this,
			repository.getAccountId());
		this.applicationContext.publishEvent(event);
	    }
	}
    }
    
    private List<From> getPublicFromList(Repository r) {
    	if(r == null)return null;
    	Account account = accountService.getById(r.getAccountId());
    	if(account == null) {
    		return null;
    	}
    	List<From> fromList = new ArrayList<From>();
    	From publicFrom = new From();
    	publicFrom.setName(account.getName());
    	publicFrom.setAccountId(account.getId());
    	publicFrom.setDate(DateUtil.FormatDateUTC(r.getDate()));
    	Avatar avatar = new Avatar();
	    if (account.getPhotos() != null) {
		avatar.setLarge(account.getPhotos().get(Account.PHOTO_SIZE_BIG));
		avatar.setMedium(account.getPhotos().get(Account.PHOTO_SIZE_MIDDLE));
		avatar.setSmall(account.getPhotos().get(Account.PHOTO_SIZE_SMALL));
	    }
	    publicFrom.setAvatar_trans(avatar);
    	
    	fromList.add(publicFrom);
    	return fromList;
    }
    /**
     * 获取分享好友及评论信息
     * 
     * @param r
     * @return
     */
    private List<From> getFromList(Repository r) {
	List<FriendShare> friendList = r.getFromFriends();
	if (friendList == null || friendList.size() < 1) {
	    return null;
	}
	List<From> fromList = new ArrayList<From>();
	for (FriendShare friend : friendList) {
	    if (friend == null)
		continue;
	    String shareId = friend.getFriendId();
	    if (shareId == null || shareId.trim().length() < 1)
		continue;
	    From from = new From();
	    from.setName("unknown");
	    Account account = accountService.getById(shareId);
	    if (account == null)
		continue;
	    from.setName(account.getName());
	    from.setAccountId(shareId);
	    from.setDate(DateUtil.FormatDateUTC(friend.getShareTime()));
	    from.setComment_trans2(friend);
	    Avatar avatar = new Avatar();
	    if (account.getPhotos() != null) {
		avatar.setLarge(account.getPhotos().get(Account.PHOTO_SIZE_BIG));
		avatar.setMedium(account.getPhotos().get(
			Account.PHOTO_SIZE_MIDDLE));
		avatar.setSmall(account.getPhotos().get(
			Account.PHOTO_SIZE_SMALL));
	    }
	    from.setAvatar_trans(avatar);
	    from.setAcknowledgment(friend.isAcknowledgment());
	    // from.setBadge(messageService.countMessage(
	    // r.getAccountId(), shareId, r.getContentId()));
	    // Comment fromComment =
	    // commentService.getByAccountIdAndContentId(shareId,
	    // r.getContentId());
	    // from.setComment_trans(fromComment);

	    fromList.add(from);
	}
	return fromList;
    }

    private List<String> getOldTag(String newTag, List<String> oldTagList,
	    String menuType) {
	List<String> list = new ArrayList<String>();
	if (newTag == null || newTag.trim().length() < 1) {
	    return list;
	}
	if (oldTagList == null || oldTagList.size() < 1) {
	    return list;
	}
	if (menuType == null)
	    menuType = "";
	for (int i = 0; i < oldTagList.size(); i++) {
	    String oldTag = oldTagList.get(i);
	    if (SystemTagEnum.Collect.toString().equals(newTag)) {// 收藏
		if (SystemTagEnum.Collect.toString().equals(oldTag)) {// 过滤收藏tag
		    continue;
		}
		if (SystemTagEnum.Delete_Collect.toString().equals(oldTag)) {// 过滤删除收藏tag
		    continue;
		}
		// if (menuType.indexOf("later") > -1) {// 来自稍后看菜单操作
		if (SystemTagEnum.Later.toString().equals(oldTag)
			&& !"cover".equalsIgnoreCase(menuType)) {// 过滤稍后看tag
		    // list.add(SystemTagEnum.Delete_Later.toString());
		    // continue;
		}
		// }
		if (SystemTagEnum.Unread.toString().equals(oldTag)
			&& !"cover".equalsIgnoreCase(menuType)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    list.add(SystemTagEnum.Read.toString());
		    continue;
		}
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Delete_Collect.toString().equals(newTag)) {// 删除收藏
		if (SystemTagEnum.Delete_Collect.toString().equals(oldTag)) {// 删除收藏tag
		    continue;
		}
		if (SystemTagEnum.Collect.toString().equals(oldTag)) {// 过滤收藏tag
		    continue;
		}
		// if (menuType.indexOf("later") > -1) {// 来自稍后看菜单操作
		if (SystemTagEnum.Later.toString().equals(oldTag)) {// 过滤稍后看tag
		    // list.add(SystemTagEnum.Delete_Later.toString());
		    // continue;
		}
		// }
		if (SystemTagEnum.Unread.toString().equals(oldTag)
			&& !"cover".equalsIgnoreCase(menuType)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    list.add(SystemTagEnum.Read.toString());
		    continue;
		}
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Later.toString().equalsIgnoreCase(newTag)) {// 稍后看
		if (SystemTagEnum.Later.toString().equals(oldTag)) {// 过滤稍后看
		    continue;
		}
		if (SystemTagEnum.Delete_Later.toString().equals(oldTag)) {// 过滤删除稍后看tag
		    continue;
		}
		if (SystemTagEnum.Unread.toString().equals(oldTag)
			&& !"cover".equalsIgnoreCase(menuType)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    list.add(SystemTagEnum.Read.toString());
		    continue;
		}
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Delete_Later.toString().equals(newTag)) {// 删除稍后看
		if (SystemTagEnum.Delete_Later.toString().equals(oldTag)) {// 过滤删除稍后看tag
		    continue;
		}
		if (SystemTagEnum.Later.toString().equals(oldTag)) {// 过滤稍后看tag
		    continue;
		}
		if (SystemTagEnum.Unread.toString().equals(oldTag)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    list.add(SystemTagEnum.Read.toString());
		    continue;
		}
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Read.toString().equals(newTag)) {// 已读
		if (SystemTagEnum.Unread.toString().equals(oldTag)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    continue;
		}
		if (SystemTagEnum.Read.toString().equals(oldTag)) {// 过滤已读tag
		    continue;
		}
		if (SystemTagEnum.Delete_Read.toString().equals(oldTag)) {// 过滤删除已读tag
		    continue;
		}
		// if (menuType.indexOf("later") > -1) {// 来自稍后看菜单操作
		if (SystemTagEnum.Later.toString().equals(oldTag)) {// 过滤稍后看tag
		    // list.add(SystemTagEnum.Delete_Later.toString());
		    // continue;
		}
		// }
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Delete_Read.toString().equals(newTag)) {// 删除已读
		if (SystemTagEnum.Read.toString().equals(oldTag)) {// 过滤已读tag
		    continue;
		}
		if (SystemTagEnum.Delete_Read.toString().equals(oldTag)) {// 过滤删除已读tag
		    continue;
		}
		list.add(oldTag);
	    }

	    else if (SystemTagEnum.Delete_Source_Friend.toString().equals(
		    newTag)) {// 取消朋友分享
		if (SystemTagEnum.Source_Friend.toString().equals(oldTag)) {// 过滤朋友分享tag
		    continue;
		}
		if (SystemTagEnum.Unread.toString().equals(oldTag)) {// 过滤未读tag
		    list.add(SystemTagEnum.Delete_Unread.toString());
		    list.add(SystemTagEnum.Read.toString());
		    continue;
		}
		list.add(oldTag);
	    }

	}
	return list;
    }

    private boolean isUnread(List<String> tagList) {
	if (tagList == null || tagList.size() < 1)
	    return false;
	for (String tag : tagList) {
	    if (SystemTagEnum.Unread.toString().equalsIgnoreCase(tag)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * 重新获取用户tag列表
     * 
     * @param userTag
     * @param removeTag
     * @return
     */
    private List<String> reGetUserList(List<String> userTag, String removeTag) {
	if (userTag == null || userTag.size() < 1)
	    return null;
	if (removeTag == null)
	    return userTag;
	for (int i = 0; i < userTag.size(); i++) {
	    String tag = userTag.get(i);
	    if (removeTag.equalsIgnoreCase(tag)) {
		userTag.remove(i);
		break;
	    }
	}
	return userTag;
    }

    /**
     * 重新获取用户tag列表
     * 
     * @param userTag
     * @param oldTag
     * @param newTag
     * @return
     */
    private List<String> reGetUserList(List<String> userTag, String oldTag,
	    String newTag) {
	if (userTag == null || userTag.size() < 1)
	    return null;
	if (oldTag == null || newTag == null)
	    return userTag;
	for (int i = 0; i < userTag.size(); i++) {
	    String tag = userTag.get(i);
	    if (oldTag.equalsIgnoreCase(tag)) {
		userTag.set(i, newTag);// 替换新的用户tag
		break;
	    }
	}
	return userTag;
    }

    private void updateFriendSessionBadge(String accountId,
	    List<String> friends, String contentId, int count) {

	boolean commit = false;
	List<Repository> repository = repositoryDao.findBySharedFriends(
		accountId, contentId, friends);

	if (repository != null && repository.size() == 1) {
	    Repository r = repository.get(0);

	    for (FriendShare fs : r.getFromFriends()) {

		if (fs.getFriendId().equals(accountId)) {
		    commit = true;
		    break;
		}
	    }

	    if (commit) {
		repositoryDao.save(r);
	    }
	}
    }

    @Override
    public void clearAllFriendSessionBadge(String accountId) {

	if (StringUtils.isNotBlank(accountId)) {
	    List<Repository> repository = repositoryDao
		    .getRepositoryByAccountId(accountId);

	    if (repository != null) {

		for (Repository r : repository) {
		    boolean commit = false;

		    if (r.getFromFriends() == null) {
			continue;
		    }

		    for (FriendShare fs : r.getFromFriends()) {
			commit = true;
		    }

		    if (commit) {
			repositoryDao.save(r);
		    }
		}
	    }
	}
    }

    @Override
    public void clearFriendSessionBadge(List<ContentAndAccount> targets) {

	if (targets == null) {
	    throw new InvalidParameterException();
	}

	for (ContentAndAccount target : targets) {
	    Repository r = repositoryDao.getByContentIdAndAccountId(
		    target.getContentId(), target.getAccountId());

	    boolean hasFriend = false;

	    if (r != null && r.getFromFriends() != null) {

		for (FriendShare fs : r.getFromFriends()) {

		    if (fs.getFriendId().equals(target.getTargetId())) {
			hasFriend = true;
			break;
		    }
		}
	    }

	    if (hasFriend) {
		repositoryDao.save(r);
	    }
	}
    }

    @Override
    public int countSharedByAccountId(String id) {
	return publicRepositoryDao.countByAccountId(id);
    }

    @Override
    public void delete(String id) {
	repositoryDao.delete(id);
    }

    @Override
    public ContentDetail findContentDetailByContentIdAndAccountId(
	    String contentId, String accountId) {
	Repository repository = this.getRepositoryByContentIdAndAccountId(
		contentId, accountId);
	if (repository == null || repository.getId() == null) {
	    return null;
	}
	return this.findContentDetailById(repository.getId());
    }

    @Override
    public ContentDetail findContentDetailById(String id) {
	if (id == null || id.trim().length() < 1) {
	    return null;
	}
	List<String> idList = new ArrayList<String>();
	idList.add(id);
	List<ContentDetail> resultList = this.findContentDetailsById(idList);
	if (resultList == null || resultList.size() < 1) {
	    return null;
	}
	return resultList.get(0);
    }

    @Override
    public List<ContentDetail> findContentDetailsById(
	    List<String> repositoryIdList) {
	if (repositoryIdList == null || repositoryIdList.size() < 1) {
	    return null;
	}

	List<ContentDetail> list = new ArrayList<ContentDetail>();
	for (String id : repositoryIdList) {
	    Repository r = getById(id);
	    if (r == null) {
		continue;
	    }
	    Content c = contentService.getById(r.getContentId());
	    List<Comment> comments = commentService.getCommentsByContentId(
		    r.getContentId(), null, 10, 0);

	    if (c == null) {
		continue;
	    }
	    if (c.getCounter() == null) {
		c.setCounter(new Counter());
	    }
	    ContentDetail detail = new ContentDetail();
	    detail.setComments(comments);
	    detail.setContent(c);
	    detail.setRepository(r);
	    detail.setDate_i18n(DateUtil.getTimestampStr(r.getDate()));
	    detail.setSource(getRepositorySource(r));
	    detail.setFolders(getRepositoryFolders(r));
	    detail.setType(getRepositoryType(r));
	    if(this.isContainTag(r.getTag(), SystemTagEnum.Public.toString())) {
	    	detail.setFrom(this.getPublicFromList(r));
	    }
	    else {
	    	detail.setFrom(this.getFromList(r));
	    }
	    detail.setImageUrl(null);
	    if(isReplaceImage) {
	    	String imageUrl = replaceImage(detail.getImageUrl());
	    	detail.setImageUrl(imageUrl);
	    }
	    list.add(detail);
	}
	return list;
    }

    @Override
    public List<Repository> findRepositoryByAccountId(String accountId) {
	return repositoryDao.getRepositoryByAccountId(accountId);
    }

    @Override
    public List<ContentDetail> findSharedContentDetailsById(
	    List<String> repositoryIdList) {
	if (repositoryIdList == null || repositoryIdList.size() < 1) {
	    return null;
	}

	List<ContentDetail> list = new ArrayList<ContentDetail>();
	for (String id : repositoryIdList) {

	    Content c = contentService.getById(id);
	    List<Comment> comments = commentService.getCommentsByContentId(id,
		    null, 10, 0);

	    if (c == null) {
		continue;
	    }
	    if (c.getCounter() == null) {
		c.setCounter(new Counter());
	    }
	    ContentDetail detail = new ContentDetail();
	    detail.setComments(comments);
	    detail.setContent(c);
	    // detail.setRepository(r);
	    // detail.setDate_i18n(DateUtil.getTimestampStr(r.getDate()));
	    // detail.setSource(getRepositorySource(r));
	    // detail.setFolders(getRepositoryFolders(r));
	    // detail.setType(getRepositoryType(r));
	    // detail.setFrom(this.getFromList(r));

	    list.add(detail);
	}
	return list;
    }

    @Override
    public List<Repository> getAllRepository() {
	return repositoryDao.getAllRepository();
    }

    @Override
    public List<Repository> getRepositoryBySystemTags(
	    List<String> systemTagList, int offset, int limit) {
	return repositoryDao.getRepositoryBySystemTags(systemTagList, offset,
		limit);
    }

    @Override
    public List<Repository> getRepositoryBySystemTags(
	    List<String> systemTagList, List<String> notSystemTagList,
	    int offset, int limit) {
	return repositoryDao.getRepositoryBySystemTags(systemTagList,
		notSystemTagList, offset, limit);
    }

    @Override
    public Repository getById(String id) {
	Repository repository = repositoryDao.getById(id);
	if (repository == null)
	    return null;
	if (repository.getLastModified() == null) {
	    repository.setLastModified(repository.getDate());
	}
	return repository;
    }

    @Override
    public List<ContentDetail> getContentDetailList(String accountId,
	    String orderby, int PageSize,
	    int PageNum) {
	List<String> getrepositoryIdList = getrepositoryIdList(accountId,
		orderby, PageSize,
		PageNum);
	List<ContentDetail> list = findContentDetailsById(getrepositoryIdList);
	return list;
    }

    @Override
    public Repository getLatestByAccount(String accountId) {
	return repositoryDao.getLatestByAccount(accountId);
    }

    @Override
    public PublicRepository getPublicRepositoryByContentIdAndAccountId(
	    String contentId, String accountId) {
	return publicRepositoryDao.getByContentIdAndAccountId(contentId,
		accountId);
    }

    @Override
    public Repository getRepositoryByContentIdAndAccountId(String contentId,
	    String accountId) {
	return repositoryDao.getByContentIdAndAccountId(contentId, accountId);
    }

    public RepositoryDao getRepositoryDao() {
	return repositoryDao;
    }

    @Override
    public List<String> getRepositoryFolders(Repository r) {
	if (r == null || r.getTag() == null || r.getTag().size() < 1)
	    return null;
	List<String> list = new ArrayList<String>();
	for (String t : r.getTag()) {
	    // for (SystemTagEnum te : folderTags) {
	    // // if (t.equals(tagService.getSystemTagName(te)))
	    // // list.add(te);
	    // }
	    list.add(t);
	}
	return list;
    }

    public List<String> getrepositoryIdList(String accountId, String orderby,
	    int PageSize,
	    int PageNum) {
	return repositoryDao.getRepositoryIdsByAccountIdOrderBy(accountId,
		orderby, PageNum, PageSize);
    }

    @Override
    public SystemTagEnum getRepositorySource(Repository r) {
	if (r == null || r.getTag() == null || r.getTag().size() < 1)
	    return null;
	for (String t : r.getTag()) {
	    for (SystemTagEnum te : sourceTags) {
		if (t.equals(tagService.getSystemTagName(te)))
		    return te;
	    }
	}
	return null;
    }

    @Override
    public SystemTagEnum getRepositoryType(Repository r) {
	if (r == null || r.getTag() == null || r.getTag().size() < 1)
	    return null;
	for (String t : r.getTag()) {
	    for (SystemTagEnum te : typeTags) {
		if (t.equals(tagService.getSystemTagName(te)))
		    return te;
	    }
	}
	return null;
    }

    @Override
    public List<ContentDetail> getSharedByAccountId(String id, String orderby,
	    int limit, int offset) {

	List<String> getrepositoryIdList = getSharedrepositoryIdList(id,
		orderby, limit,
		offset);
	List<ContentDetail> list = findSharedContentDetailsById(getrepositoryIdList);
	return list;

    }

    public List<String> getSharedrepositoryIdList(String accountId,
	    String orderby, int limit,
	    int offset) {
	return publicRepositoryDao.getRepositoryIdsByAccountIdOrderBy(
		accountId, orderby, limit, offset);
    }

    @Override
    public List<String> getTag(List<String> newTagList,
	    List<String> oldTagList, String menuType) {
	if (newTagList == null || newTagList.size() < 1) {
	    return oldTagList;
	}
	List<String> result = new ArrayList<String>();
	for (int i = 0; i < newTagList.size(); i++) {
	    String newTag = newTagList.get(i);
	    if (!this.isContainTag(result, newTag)) {
		result.add(newTag);
	    }
	    List<String> oldTags = this.getOldTag(newTag, oldTagList, menuType);
	    for (String oldTag : oldTags) {
		if (this.isContainTag(result, oldTag)) {// 如果已经存在不需要添加
		    continue;
		}
		result.add(oldTag);
	    }
	    // result.addAll(this.getOldTag(newTag, oldTagList, menuType));
	}
	if ("cover".equalsIgnoreCase(menuType)) {// 重新抓取
	    if (this.isContainTag(result,
		    SystemTagEnum.Delete_Collect.toString())) {
		if (this.isContainTag(newTagList,
			SystemTagEnum.Collect.toString())) {
		    result.remove(SystemTagEnum.Delete_Collect.toString());
		}
	    }
	    if (this.isContainTag(result, SystemTagEnum.Delete_Later.toString())) {
		if (this.isContainTag(newTagList,
			SystemTagEnum.Later.toString())) {
		    result.remove(SystemTagEnum.Delete_Later.toString());
		}
	    }
	}
	return result;
    }

    @Override
    public List<String> getTag(String newTag, List<String> oldTagList,
	    String menuType) {
	if (newTag == null || newTag.trim().length() < 1) {
	    return oldTagList;
	}
	List<String> newTagList = new ArrayList<String>();
	newTagList.add(newTag);
	return this.getTag(newTagList, oldTagList, menuType);
    }

    @Override
    public List<String> getTopTagListByContentId(String contentId, int limit) {
	Map<String, String> topTagMap = this.getTopTagMapByContentId(contentId,
		limit);
	if (topTagMap == null || topTagMap.size() < 1)
	    return null;

	List<String> tagList = new ArrayList<String>();
	Iterator<String> it = topTagMap.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    tagList.add(key);
	}
	return tagList;
    }

    @Override
    public Map<String, String> getTopTagMapByContentId(String contentId,
	    int limit) {
	List<Repository> resultList = repositoryDao
		.getRepositoryByContentId(contentId);
	if (resultList == null || resultList.size() < 1)
	    return null;

	Map<String, String> topTagMap = new LinkedHashMap<String, String>();
	Map<String, Integer> tagMap = new HashMap<String, Integer>();
	// 统计标签出现的次数
	for (int i = 0; i < resultList.size(); i++) {
	    Repository rep = resultList.get(i);
	    if (rep == null)
		continue;
	    List<String> userTag = rep.getUserTag();
	    if (userTag == null || userTag.size() < 1)
		continue;
	    for (int j = 0; j < userTag.size(); j++) {
		String tag = userTag.get(j);
		if (tag == null || tag.trim().length() < 1)
		    continue;
		if (tagMap.containsValue(tag)) {
		    tagMap.put(tag, tagMap.get(tag) + 1);
		} else {
		    tagMap.put(tag, 1);
		}
	    }
	}
	// 排序
	List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(
		tagMap.entrySet());
	Collections.sort(sortList,
		new Comparator<Map.Entry<String, Integer>>() {
		    @Override
		    public int compare(Map.Entry<String, Integer> o1,
			    Map.Entry<String, Integer> o2) {
			return (o2.getValue() - o1.getValue());
		    }
		}
		);
	// 获取排序后的前N个列表
	for (int i = 0; i < sortList.size(); i++) {
	    Map.Entry<String, Integer> sort = sortList.get(i);
	    if (i >= limit) {
		break;
	    }
	    topTagMap.put(sort.getKey(), sort.getValue() + "");
	}
	return topTagMap;
    }

    @Override
    public String getTopTagStrByContentId(String contentId, String joinStr,
	    int limit) {
	List<String> tagList = this.getTopTagListByContentId(contentId, limit);
	if (tagList == null || tagList.size() < 1)
	    return "";
	String topTag = "";
	for (int i = 0; i < tagList.size(); i++) {
	    String tag = tagList.get(i);
	    if (tag == null || tag.trim().length() < 1)
		continue;
	    topTag = topTag + tag + joinStr;
	}
	if (topTag.endsWith(joinStr))
	    topTag = topTag.substring(0, topTag.length() - joinStr.length());
	return topTag;
    }

    @Override
    public boolean hasSystemTag(Repository r, SystemTagEnum tag) {
	if (r == null || r.getTag() == null || r.getTag().size() < 1)
	    return false;
	for (String t : r.getTag()) {
	    if (t.equals(tagService.getSystemTagName(tag)))
		return true;
	}

	return false;
    }

    @Override
    public boolean isContainPublicRepository(String contentId, String accountId) {
	if (publicRepositoryDao
		.getByContentIdAndAccountId(contentId, accountId) == null) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean isContainRepository(String contentId, String accountId) {
	if (repositoryDao.getByContentIdAndAccountId(contentId, accountId) == null) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean isContainTag(List<String> tagList, String tag) {
	if (tagList == null || tagList.size() < 1)
	    return false;
	if (tag == null || tag.trim().length() < 1)
	    return false;
	for (int i = 0; i < tagList.size(); i++) {
	    String stag = tagList.get(i);
	    if (tag.equalsIgnoreCase(stag)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isExistTag(List<String> newTagList, List<String> oldTagList) {
	boolean isExistTag = false;
	if (newTagList == null || newTagList.size() < 1)
	    return false;
	if (oldTagList == null || oldTagList.size() < 1)
	    return false;

	for (int i = 0; i < newTagList.size(); i++) {
	    String newTag = newTagList.get(i);
	    if (SystemTagEnum.Delete_Read.toString().equals(newTag)) {
		return false;
	    }
	    if (i > 0 && !isExistTag) {
		break;
	    }
	    if (newTag == null || newTag.trim().length() < 1)
		continue;
	    isExistTag = false;
	    for (String oldTag : oldTagList) {
		if (newTag.equalsIgnoreCase(oldTag)) {
		    isExistTag = true;
		    break;
		}
	    }
	}

	return isExistTag;
    }

    @Override
    public void removeTagByAccountIdAndTag(String accountId, String tag) {
	List<Repository> resultList = repositoryDao
		.getRepositoryByAccountIdAndTag(accountId, tag);
	if (resultList == null || resultList.size() < 1)
	    return;
	for (int i = 0; i < resultList.size(); i++) {
	    Repository rep = resultList.get(i);
	    if (rep == null)
		continue;
	    rep.setUserTag(this.reGetUserList(rep.getUserTag(), tag));
	    rep.setLastModified(new Date());
	    this.save(rep);
	    // 更新索引
	    contentSearcher.updateIndex(rep.getId());
	}
    }

    @Override
    public void save(Repository repository) {
	if (repository.getLastModified() == null) {
	    repository.setLastModified(repository.getDate());
	}

	if (StringUtils.isBlank(repository.getShareLink())) {
	    updateShareLink(repository);
	}
	repositoryDao.save(repository);
	this.addLaterEvent2Listener(repository);
    }

    @Override
    public void savePublicRepository(PublicRepository repository) {
	publicRepositoryDao.save(repository);
    }

    @Override
    public PagedResult search(String accountId, String tag,
	    String userTag, String firendId, int offset, int limit) {
	return repositoryDao.search(accountId, tag, userTag, firendId, offset,
		limit);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
	    throws BeansException {
	this.applicationContext = applicationContext;
    }

    public void setRepositoryDao(RepositoryDao repositoryDao) {
	this.repositoryDao = repositoryDao;
    }

    @Override
    public List<StatisticTag> statisticTagList(String accountId) {
	Map<String, StatisticTag> tagMap = this.statisticTagMap(accountId);
	if (tagMap == null || tagMap.size() < 1)
	    return null;

	List<StatisticTag> tagList = new ArrayList<StatisticTag>();
	Iterator<String> it = tagMap.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    tagList.add(tagMap.get(key));
	}
	return tagList;
    }

    @Override
    public Map<String, StatisticTag> statisticTagMap(String accountId) {
    Map<String, StatisticTag> taMap = new HashMap<String, StatisticTag>();
    long publicNum = repositoryDao.getPublicRepositoryNum();
    StatisticTag publicTag = new StatisticTag();
    publicTag.setName(SystemTagEnum.Public.toString());
    publicTag.setQuantity((int)publicNum);
    taMap.put(SystemTagEnum.Public.toString(), publicTag);
    
	List<Repository> repList = repositoryDao
		.getRepositoryByAccountId(accountId);
	if (repList == null || repList.size() < 1)
	    return taMap;
	
	
	StatisticTag allTag = new StatisticTag();
	allTag.setName("all");
	allTag.setQuantity(repList.size());
	taMap.put("all", allTag);
	for (Repository rep : repList) {
	    if (rep == null)
		continue;
	    if (rep.getTag() == null || rep.getTag().size() < 1)
		continue;
	    for (String tag : rep.getTag()) {
		if (tag == null || tag.trim().length() < 1)
		    continue;
		StatisticTag stag = null;
		if (taMap.containsKey(tag)) {
		    stag = taMap.get(tag);
		} else {
		    stag = new StatisticTag();
		    stag.setName(tag);
		}
		stag.setQuantity(stag.getQuantity() + 1);
		if (this.isUnread(rep.getTag())) {
		    stag.setUnread(stag.getUnread() + 1);
		}
		taMap.put(tag, stag);
	    }
	}
	return taMap;
    }

    @Override
    public void updateFriendSessionBadge(PrivateMessageSession session,
	    PrivateMessageSession target) {

	if (session != null) {
	    List<String> friends = new ArrayList<String>(1);
	    friends.add(session.getOwneraccountId());
	    updateFriendSessionBadge(session.getAccountId(), friends,
		    session.getContentId(), session.getMessageCount());

	}

	if (target != null) {
	    List<String> friends1 = new ArrayList<String>(1);
	    friends1.add(target.getOwneraccountId());
	    updateFriendSessionBadge(target.getAccountId(), friends1,
		    target.getContentId(), target.getMessageCount());

	}
    }

    @Override
    public void updateTagByAccountIdAndTag(String accountId, String oldTag,
	    String newTag) {
	List<Repository> resultList = repositoryDao
		.getRepositoryByAccountIdAndTag(accountId, oldTag);
	if (resultList == null || resultList.size() < 1)
	    return;
	for (int i = 0; i < resultList.size(); i++) {
	    Repository rep = resultList.get(i);
	    if (rep == null)
		continue;
	    rep.setUserTag(this.reGetUserList(rep.getUserTag(), oldTag, newTag));
	    rep.setLastModified(new Date());
	    this.save(rep);
	    // 更新索引
	    contentSearcher.updateIndex(rep.getId());
	}
    }

    @Override
    public void updateShareLink(Repository repository) {
	Invite invite = inviteService.getByOwnerId(repository
		.getAccountId());

	String code;

	if (invite == null) {
	    Invite it = new Invite();
	    it.setOwnerId(repository.getAccountId());
	    it.setCreateTime(new Date());
	    code = inviteService.save(it);

	} else {
	    code = invite.getId();
	}
	String url = "http://www.mduoduo.com/ocean/" + code + "/"
		+ repository.getContentId();

	url = thirdPartyService.getShortUrl(url);
	repository.setShareLink(url);
    }
    
    private static String replaceImage(String image) {
		if(StringUtils.isBlank(image))return image;
		for(String key: imageReplaceMap.keySet()) {
			if(image.indexOf(key) ==-1)continue;
			String value = imageReplaceMap.get(key);
			image = image.replaceFirst(key, value);
			break;
		}
		return image;
	}
}
