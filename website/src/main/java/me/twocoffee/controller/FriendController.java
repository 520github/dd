package me.twocoffee.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.Pinyin4jUtils;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.controller.entity.FriendVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.rest.entity.FriendLogInfo;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.FriendRpcService;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FriendController extends BaseController {
    @Autowired
    private ThirdpartyService thirdpartyService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private FriendRpcService friendRpcService;

    @Autowired
    private ContentRpcService contentRpcService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ThirdPartyRpcService thirdPartyRpcService;

    /**
     * 过滤掉list中不是myAccountId朋友的item
     * 
     * @param list
     * @param myAccountId
     * @param fullIsMyFriend
     * @return
     */
    private List<FriendVO> convert_filterTOAccounts(List<Account> list,
	    String myAccountId) {
	if (list == null || list.size() < 1) {
	    return null;
	}
	List<FriendVO> fs = new ArrayList();
	List<Account> myFriends = friendService.findFriends(myAccountId);
	for (Account a : list) {
	    FriendVO fv = new FriendVO();
	    AccountVO av = AuthUtil.covertToAccountVO(a);
	    av.setSharedNumber(repositoryService.countSharedByAccountId(a
		    .getId()));
	    av.setFriendNumber(friendService.countByAccountId(a.getId()));
	    fv.setAccount(av);
	    boolean b = hasFriend(a.getId(), myFriends);
	    if (!b) {
		continue;
	    }
	    fv.setMyFriend(true);
	    fv.setSharedToMe(friendService.countSharedTo(myAccountId,
		    av.getId()));
	    Friend f = friendService.getByAccountIdAndFriendId(myAccountId, av
		    .getId());
	    if (null != f) {
		String favorite = "Normal";
		String remarkName = "";
		if (null != f.getFriendType()) {
		    favorite = f.getFriendType().name();
		}
		if (null != f.getRemarkName()) {
		    remarkName = f.getRemarkName();
		}
		fv.setFavorite(favorite);
		fv.setRemarkName(remarkName);
		fv.setCreated(f.getCreated());
	    }
	    fs.add(fv);
	}
	return fs;
    }

    private List<FriendVO> convertTOAccounts(List<Account> list, String id,
	    boolean fullIsMyFriend) {
	if (list == null || list.size() < 1) {
	    return null;
	}
	List<FriendVO> fs = new ArrayList();
	List<Account> myFriends = friendService.findFriends(id);
	for (Account a : list) {
	    FriendVO fv = new FriendVO();
	    AccountVO av = AuthUtil.covertToAccountVO(a);
	    av.setSharedNumber(repositoryService.countSharedByAccountId(a
		    .getId()));
	    av.setFriendNumber(friendService.countByAccountId(a.getId()));
	    fv.setAccount(av);

	    if (fullIsMyFriend) {
		fv.setMyFriend(hasFriend(a.getId(), myFriends));
	    } else {
		fv.setMyFriend(true);
	    }
	    fv.setSharedToMe(friendService.countSharedTo(id, av.getId()));
	    fs.add(fv);
	}
	return fs;
    }

    /**
     * 实际上可以完全替代convertTOAccounts 为了兼容不破坏以前(有潜在性)的代码 暂时不替换convertTOAccounts
     * 
     * @param list
     * @param id
     * @param fullIsMyFriend
     * @return
     */
    private List<FriendVO> convertTOAccounts_includeAttributefriendType(
	    List<Account> list, String id,
	    boolean fullIsMyFriend) {
	if (list == null || list.size() < 1) {
	    return null;
	}
	List<FriendVO> fs = new ArrayList();
	List<Account> myFriends = friendService.findFriends(id);
	for (Account a : list) {
	    FriendVO fv = new FriendVO();
	    AccountVO av = AuthUtil.covertToAccountVO(a);
	    av.setSharedNumber(repositoryService.countSharedByAccountId(a
		    .getId()));
	    av.setFriendNumber(friendService.countByAccountId(a.getId()));
	    fv.setAccount(av);
	    if (fullIsMyFriend) {
		fv.setMyFriend(hasFriend(a.getId(), myFriends));
	    } else {
		fv.setMyFriend(true);
	    }
	    fv.setSharedToMe(friendService.countSharedTo(id, av.getId()));
	    Friend f = friendService.getByAccountIdAndFriendId(id, av.getId());
	    if (null != f) {
		String favorite = "Normal";
		String remarkName = "";
		if (null != f.getFriendType()) {
		    favorite = f.getFriendType().name();
		}
		if (null != f.getRemarkName()) {
		    remarkName = f.getRemarkName();
		}
		fv.setFavorite(favorite);
		fv.setRemarkName(remarkName);
		fv.setCreated(f.getCreated());
	    }
	    fs.add(fv);
	}
	return fs;
    }

    private List<String> createFriendIds(List<AccountInfo> fs) {
	if (fs == null || fs.size() < 1) {
	    return null;
	}

	List<String> fids = new ArrayList();
	for (AccountInfo a : fs) {
	    fids.add(a.getId());
	}
	return fids;
    }

    private List<AccountInfo> findFavoriteFriend(List<AccountInfo> fs) {
	if (fs == null || fs.size() < 1)
	    return null;

	List<AccountInfo> list = new ArrayList();
	for (AccountInfo a : fs) {
	    if (a.getFriend().isFrequently())
		list.add(a);
	}

	return list;
    }

    private List<AccountInfo> findLasterFriend(List<AccountInfo> fs) {
	if (fs == null || fs.size() < 1)
	    return null;

	List<AccountInfo> list = new ArrayList();
	for (AccountInfo a : fs) {
	    if (a.getFriend().isFrequently())
		list.add(a);
	}

	return list;
    }

    private List<String> getSelectedFriends(String friends) {
	if (friends == null || friends.trim().equals("")) {
	    return null;
	}

	String[] ss = friends.split(",");
	if (ss == null || ss.length < 1) {
	    return null;
	}

	List<String> list = new ArrayList();
	for (String s : ss) {
	    if (s == null || ss.equals("")) {
		continue;
	    }
	    list.add(s);
	}
	return list;
    }

    private boolean hasFriend(String id, List<Account> friends) {
	if (friends == null || friends.size() < 1) {
	    return false;
	}
	for (Account a : friends) {
	    if (a.getId().equals(id)) {
		return true;
	    }
	}
	return false;
    }

    private void parseResult(List<ContactResult> contacts,
	    List<ContactResult> duoduoContacts,
	    List<ContactResult> notDuoduoContacts) {

	for (ContactResult r : contacts) {

	    if (r.getAccount() == null) {
		notDuoduoContacts.add(r);

	    } else {
		duoduoContacts.add(r);
	    }
	}
    }

    @RequestMapping("/friend/add")
    @ResponseBody
    public String add(HttpServletRequest request, String friendId,
	    String remark, String postscript, String thirdType, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

	if (accountId.equals(friendId)) {
	    return "3";
	}
	if (friendRpcService
		.add(authorization, friendId, remark, postscript, thirdType)) {
	    return "1";
	}

	return "2";
    }

    @RequestMapping("/friend/favorite")
    @ResponseBody
    public String favorite(HttpServletRequest request, String targetId,
	    String actionFavorite, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

	int result = friendService.dealFavorite(accountId,
		targetId,
		actionFavorite);

	if (actionFavorite.equals("Normal")) {
	    if (result == 0)
		return "0";
	    else
		return "1";

	}
	if (actionFavorite.equals("Favorite"))
	    if (result == 0)
		return "2";
	    else
		return "3";
	return "1";
    }

    /**
     * 常用联系人列表
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/favoriteFriend")
    public String favoriteFriendList(HttpServletRequest request, Model model) {
    String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	model.addAttribute("visitUser", visitUser);

	List<Account> list = friendService
		.findFavoriteFriends(accountId);
	List<FriendVO> fs = convertTOAccounts_includeAttributefriendType(list,accountId,
		false);
	notificationService.deleteByAccountIdAndKey(accountId,
		Notification.CollapseKey.FriendMessage);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("uid", accountId);
	model.addAttribute("friends", fs);
	model.addAttribute("class", "/favoriteFriend");

	return "friend/list";
    }

    @RequestMapping("/friend/find")
    public String find(HttpServletRequest request, Model model) {
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	model.addAttribute("visitUser", visitUser);
	return "friend/find";
    }

    @RequestMapping("/friend/myFriends")
    public String findFriends(String contentId, HttpServletRequest request,
	    Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	List<AccountInfo> fs = friendRpcService.findFriend(authorization);
	List<AccountInfo> favorites = findFavoriteFriend(fs);
	List<AccountInfo> lasters = friendRpcService.findFriendLastTimeUsed(authorization);

	// 得到所有好友是否已分享记录
	List<String> fids = createFriendIds(fs);
	List<Boolean> sharedInfos = friendService.findSharedInfo(
			accountId,
		contentId, fids);

	// 得到常用联系人是否已分享记录
	fids = createFriendIds(favorites);
	List<Boolean> sharedInfos1 = friendService.findSharedInfo(
			accountId,
		contentId, fids);

	// 得到最后分享人是否已分享记录
	fids = createFriendIds(lasters);
	List<Boolean> sharedInfos2 = friendService.findSharedInfo(
			accountId,
		contentId, fids);

	List<Contact> weibos = thirdpartyService.getContacts(accountId,
		ThirdPartyType.Weibo, null, 0, 0);

	List<Contact> renrens = thirdpartyService.getContacts(accountId,
		ThirdPartyType.Renren, null, 0, 0);

	model.addAttribute("weibos", weibos);
	model.addAttribute("renrens", renrens);
	model.addAttribute("friends", fs);
	model.addAttribute("favorites", favorites);
	model.addAttribute("lasts", lasters);
	model.addAttribute("sharedInfos", sharedInfos);
	model.addAttribute("sharedInfos1", sharedInfos1);
	model.addAttribute("sharedInfos2", sharedInfos2);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));

	return "friend/share/share_friend";
    }

    @RequestMapping("/friend/findResult")
    public String findResult(String name, String loginName,
	    HttpServletRequest request, Model model) {
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	model.addAttribute("visitUser", visitUser);
	model.addAttribute("token", AuthUtil.getWebTokenAuthorization(request));

	List<Account> list = null;
	if (name != null && !name.trim().equals("")) {
	    list = accountService.findByName(name);
	    List<FriendVO> fs = convertTOAccounts_includeAttributefriendType(list,accountId, true);
	    model.addAttribute("q", name);
	    model.addAttribute("name", name);
	    model.addAttribute("friends", fs);
	}

	if (loginName != null && !loginName.trim().equals("")) {
	    Account a = accountService.getByLoginName(loginName);
	    if (a != null) {
		list = new ArrayList();
		list.add(a);
	    }
	    List<FriendVO> fs = convertTOAccounts_includeAttributefriendType(
		    list,accountId, true);
	    model.addAttribute("q", loginName);
	    model.addAttribute("loginName", loginName);
	    model.addAttribute("friends", fs);
	}

	return "friend/find_result";
    }

    /**
     * 在我的好友中按昵称查找
     * 
     * @param name
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/myfriend/findResult")
    public String findResultByName(String name,
	    HttpServletRequest request, Model model) {
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	model.addAttribute("visitUser", visitUser);
	model.addAttribute("token", AuthUtil.getWebTokenAuthorization(request));

	List<Account> list = null;
	if (name != null && !name.trim().equals("")) {
	    list = accountService.findByName(name);
	    List<FriendVO> fs = convert_filterTOAccounts(list,accountId);
	    model.addAttribute("q", name);
	    model.addAttribute("name", name);
	    model.addAttribute("friends", fs);
	    model.addAttribute("class", "/friend");
	}

	return "friend/list";
    }

    /**
     * 在我的changyong好友中按昵称查找
     * 
     * @param name
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/myfavoritefriend/findResult")
    public String findResultInMyFavoriteFriend(String name,
	    HttpServletRequest request, Model model) {
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	model.addAttribute("visitUser", visitUser);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));

	List<Account> list = null;
	List<Account> list_filtered = new ArrayList<Account>();
	if (name != null && !name.trim().equals("")) {

	    list = friendService
		    .findFavoriteFriends(accountId);

	    Pattern p = Pattern.compile("^.*" + name + ".*$",
		    Pattern.CASE_INSENSITIVE);
	    for (Iterator<Account> iterator = list.iterator(); iterator
		    .hasNext();) {
		Account account = iterator.next();
		String a_name = account.getName();
		Matcher m = p.matcher(a_name);
		boolean b = m.matches();
		if (b) {
		    list_filtered.add(account);

		}

	    }

	    List<FriendVO> fs = convert_filterTOAccounts(list_filtered,accountId);
	    model.addAttribute("q", name);
	    model.addAttribute("name", name);
	    model.addAttribute("friends", fs);
	    model.addAttribute("class", "/favoriteFriend");
	}

	return "friend/list";
    }

    @RequestMapping("/friend/weibo")
    public String findThirdPartyFriends(HttpServletRequest request,
	    Model model) {
	String valid = tokenUtil.validAuthorizationAndGuestInController(AuthUtil.getWebTokenAuthorization(request));
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	Account account = accountService.getById(accountId);

	if (account == null) {
	    return "redirect:/home";
	}
	boolean bind = false;
	ThirdPartyProfile profile = thirdpartyService.getByAccountId(
			accountId, ThirdPartyType.Weibo);

	if (profile != null && profile.isBind()) {
	    bind = true;
	}
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("bind", bind);

	if (bind) {
	    List<ContactResult> contacts = thirdPartyRpcService
		    .getThirdPartyFriends(authorization, ThirdPartyType.Weibo);

	    if (contacts != null) {
		List<ContactResult> duoduoContacts = new ArrayList<ContactResult>();
		List<ContactResult> notDuoduoContacts = new ArrayList<ContactResult>();
		parseResult(contacts, duoduoContacts, notDuoduoContacts);
		model.addAttribute("duoduoContacts", duoduoContacts);
		model.addAttribute("notDuoduoContacts", notDuoduoContacts);

	    } else {
		model.addAttribute("error", "无法获得微博互粉好友");
	    }
	}
	return "friend/friendmatch";
    }

    @RequestMapping("/friend")
    public String friendList(HttpServletRequest request, Model model) {
	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	model.addAttribute("visitUser", visitUser);
	
	String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
	List<Account> list = friendService
		.findFriends(accountId);
	// List<AccountInfo> fs = friendRpcService.findFriend(token);
	List<FriendVO> fs = convertTOAccounts_includeAttributefriendType(list,
			accountId,
		false);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("uid", accountId);
	model.addAttribute("friends", fs);
	model.addAttribute("class", "/friend");

	return "friend/list";
    }

    @RequestMapping("/friend/Count")
    @ResponseBody
    public ResponseEntity<String> getCount(HttpServletRequest request,
	    Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return null;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	
	long msgCount = messageService.countMessageByAccountId(accountId, CatalogId.Friend);
	long friendCount = friendService.countByAccountId(accountId);
	model.addAttribute("msgCount", msgCount);
	model.addAttribute("friendCount", friendCount);
	return buildJSONResult1(model.asMap());
    }

    @RequestMapping("/friend/message")
    public String getMessage(@QueryParam("page") Integer page,
	    HttpServletRequest request, Model model) {
	int limit = 20;
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

	if (page == null) {
	    page = new Integer(0);
	}
	PagedResult<JsonObject> result = friendRpcService.getFriendMessage(
		authorization,
		"Friend",
		limit, page * limit);

	long count = messageService.countMessageByAccountId(accountId, CatalogId.Friend);
	notificationService.deleteByAccountIdAndKey(accountId,
		Notification.CollapseKey.FriendMessage);
	model.addAttribute("resultList", result.getResult());
	model.addAttribute("totle", result.getTotal());
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("city", "");
	model.addAttribute("add", "");
	model.addAttribute("agree", "");
	model.addAttribute("ignore", "");
	model.addAttribute("msgCount", count);
	model.addAttribute("limit", limit);
	return "friend/message";
    }

    @RequestMapping("/friend/receive")
    public String receiveLogs(HttpServletRequest request, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

	model.addAttribute("token",  AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("uid", accountId);

	List<FriendLogInfo> logs = friendRpcService.findFriendLog(authorization);
	model.addAttribute("friendLog", logs);

	return "friend/receive";
    }

    @RequestMapping("/friend/{friendId}/remarkName")
    public ResponseEntity<String> remarkName(HttpServletRequest request,
	    @PathVariable("friendId") String friendId,
	    @RequestParam("remarkName") String remarkName) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return null;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	Friend f = friendService.getByAccountIdAndFriendId(accountId, friendId);
	if (f != null) {
	    f.setRemarkName(remarkName);
	    f.setRemarkNameInPinyin(Pinyin4jUtils
		    .getPinYin(remarkName));
	    friendService.update(f);
	    return buildJSONResult("result", "success");
	}
	return buildJSONResult("result", "error");
    }

    @RequestMapping("/friend/remove")
    @ResponseBody
    public String remove(HttpServletRequest request, String friendId,
	    Model model) {
	return friendRpcService.removeFriend(AuthUtil.getWebTokenAuthorization(request), friendId) ? "1" : "2";
    }

    @RequestMapping("/friend/replyAdd")
    @ResponseBody
    public String replyAdd(HttpServletRequest request, String friendId,
	    Integer type, Model model) {
    String authorization = AuthUtil.getWebTokenAuthorization(request);

	boolean success = false;
	if (type == null || type.intValue() == 1) {
	    success = friendRpcService.accept(authorization, friendId);
	} else {
	    success = friendRpcService.reject(authorization, friendId);
	}

	return success ? "1" : "2";
    }

    @RequestMapping("/friend/send")
    public String sendLogs(HttpServletRequest request, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("uid", accountId);
	List<FriendLogInfo> logs = friendRpcService.findFriendLog(authorization);
	model.addAttribute("friendLog", logs);

	return "friend/send";
    }

    @RequestMapping("/friend/share")
    @ResponseBody
    public String share(String rid, String friends, Integer grade,
	    String comment,
	    HttpServletRequest request, Model model) {
	int score = 0;
	if (grade != null) {
	    score = grade;
	}
	// 拆分成两个List
	String[] temp = friends.split(";");
	String duoduoFriends = temp[0];
	String weiBoFriends = null;
	if (temp.length > 1) {
	    weiBoFriends = temp[1];
	}
	List<String> fs = getSelectedFriends(duoduoFriends);
	List<String> weibofs = getSelectedFriends(weiBoFriends);
	if ((fs == null || fs.size() < 1)
		&& (weibofs == null || weibofs.size() < 1)) {
	    return "0";
	}

	return contentRpcService.shareToFriend(AuthUtil.getWebTokenAuthorization(request), rid, fs, weibofs, score,
		comment) ? "1"
		: "0";
    }
}
