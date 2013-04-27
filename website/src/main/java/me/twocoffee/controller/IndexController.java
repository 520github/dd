package me.twocoffee.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import me.twocoffee.common.BaseController;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.controller.entity.FriendVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.rest.generic.GenericContent;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private ThirdpartyService thirdpartyService;

    @Autowired
    private final InviteService inviteService = null;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContentRpcService contentRpcService;

    @Autowired
    private TagService tagService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ThirdPartyRpcService thirdPartyRpcService;

    @Autowired
    private ContentSearcher contentSearcher;

    private String covertinviteCode2AccountId(String inviteCode) {
    	Invite invite = inviteService.getInviteById(inviteCode);

    	if (invite != null) {
    	    return invite.getOwnerId();
    	}
    	return "";
        }

    private AccountVO covertToAccountVO(Account a) {
	AccountVO av = AuthUtil.covertToAccountVO(a);
	av.setSharedNumber(getSharedNumber(a.getId()));
	av.setFriendNumber(getFriendNumber(a.getId()));
	return av;
    }

    private List<FriendVO> findFriends(List<Account> list,
	    String homeAccountId, String visitAccountId) {
	List<Account> visitFriends = null;
	if (!homeAccountId.equals(visitAccountId)) {
	    visitFriends = friendService.findFriends(visitAccountId);
	}

	List<FriendVO> fs = new ArrayList<FriendVO>();
	for (Account a : list) {
	    FriendVO f = new FriendVO();
	    f.setAccount(covertToAccountVO(a));
	    if (!homeAccountId.equals(visitAccountId)) {
		f.setMyFriend(hasFriend(a.getId(), visitFriends));
	    } else {
		f.setMyFriend(true);
	    }
	    f.setSharedToMe(getSharedToMeNumber(visitAccountId, a.getId()));
	    Friend ff = friendService.getByAccountIdAndFriendId(visitAccountId,
		    a.getId());
	    if (ff != null) {
		String favorite = "Normal";
		if (null != ff.getFriendType()) {
		    favorite = ff.getFriendType().name();
		}
		f.setRemarkName(ff.getRemarkName());
		f.setFavorite(favorite);
	    }
	    fs.add(f);
	}
	return fs;
    }

    private AccountVO getAccountByAccountId(String accountId) {
	Account a = accountService.getById(accountId);
	if (a == null) {
	    return null;
	}
	return covertToAccountVO(a);
    }

    private int getFriendNumber(String id) {
	return friendService.countByAccountId(id);
    }

    private int getSharedNumber(String id) {
	return repositoryService.countSharedByAccountId(id);
    }

    private int getSharedToMeNumber(String accountId, String friendId) {
	return friendService.countSharedTo(accountId, friendId);
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

    /**
     * 是否是朋友
     * 
     * @param visitAccountid
     *            当前登录的用户ID
     * @param accountId
     *            当前进入页面的用户ID
     * @return
     */
    private boolean isFriend(String visitAccountId, String accountId) {
	if (visitAccountId.equals(accountId)) {
	    return true;
	}

	List<Account> visitFriends = friendService.findFriends(visitAccountId);
	return hasFriend(accountId, visitFriends);
    }

    /**
     * 用户常用的标签
     * 
     * @param request
     * @param model
     */
    private void setMyUseTag(HttpServletRequest request, Model model) {
//	String accountId = authTokenService.findById(
//		AuthUtil.getAuthTokenFromCookie(request)).getAccountId();
//	String myUseTag = tagService.getTopHotTagStr(accountId, 5, " ");
//	model.addAttribute("myUseTag", myUseTag);
    model.addAttribute("myUseTag", "");
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(HttpServletRequest request,
	    Model model) {
	return "clear";
    }

    @RequestMapping(value = "/cleartag", method = RequestMethod.POST)
    public String changePluginCollect2Later(HttpServletRequest request,
	    Model model) {
	String type = request.getParameter("type");
	type = type == null ? "" : type;
	String message = "";
	if (type.equalsIgnoreCase("productWish")) {
	    message = this.addProductWish2Product();
	}
	else if (type.equalsIgnoreCase("originalNone")) {
	    message = this.addNotOriginal();
	}

	model.addAttribute("errorMes", message);
	return "clear";
    }

    private String addProductWish2Product() {
	String message = "";
	List<String> tags = new ArrayList<String>();
	tags.add(SystemTagEnum.Type_Product.toString());

	List<Repository> resList = repositoryService.getRepositoryBySystemTags(
		tags, 0, 100);
	if (resList == null || resList.size() < 1) {
	    message = "没有需要增加Product_Wish的记录";
	    return message;
	}
	int limit = 100;
	int i = 1;
	while (true) {
	    for (Repository r : resList) {
		if (r == null)
		    continue;
		if (r.getTag() == null || r.getTag().size() < 1)
		    continue;

		if (!this.isExistTag(r.getTag(),
			SystemTagEnum.Type_Product.toString())) {
		    continue;
		}

		if (this.isExistTag(r.getTag(),
			SystemTagEnum.Product_Wish.toString())
			&& this.isExistTag(r.getTag(),
				SystemTagEnum.Source_Plugin.toString())) {
		    continue;
		}

		List<String> newTag = new ArrayList<String>();
		newTag.addAll(r.getTag());

		if (this.isExistTag(r.getTag(),
			SystemTagEnum.Product_Wish.toString())
			&& !this.isExistTag(r.getTag(),
				SystemTagEnum.Source_Plugin.toString())) {
		    newTag.remove(SystemTagEnum.Product_Wish.toString());
		}
		else if (!this.isExistTag(r.getTag(),
			SystemTagEnum.Product_Wish.toString())
			&& this.isExistTag(r.getTag(),
				SystemTagEnum.Source_Plugin.toString())) {
		    newTag.add(SystemTagEnum.Product_Wish.toString());
		}

		r.setTag(newTag);
		r.setLastModified(new Date());
		try {
		    repositoryService.save(r);
		    contentSearcher.updateIndex(r.getId());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    resList = repositoryService.getRepositoryBySystemTags(tags,
		    (i * limit) + 1, (i + 1) * 100);
	    if (resList == null || resList.size() < 1) {
		break;
	    }
	    i++;
	}
	message = "增加Product_Wish操作成功";
	return message;
    }

    private String addNotOriginal() {
	String message = "";
	List<String> tags = new ArrayList<String>();
	tags.add(SystemTagEnum.Type_File.toString());

	List<Repository> resList = repositoryService.getRepositoryBySystemTags(
		tags, 0, 100);
	if (resList == null || resList.size() < 1) {
	    message = "没有需要增加Original_None的记录";
	    return message;
	}
	int limit = 100;
	int i = 1;
	while (true) {
	    for (Repository r : resList) {
		if (r == null)
		    continue;
		if (r.getTag() == null || r.getTag().size() < 1)
		    continue;

		if (!this.isExistTag(r.getTag(),
			SystemTagEnum.Type_File.toString())) {
		    continue;
		}

		if (this.isExistTag(r.getTag(),
			SystemTagEnum.Original_None.toString())) {
		    continue;
		}

		List<String> newTag = new ArrayList<String>();
		newTag.addAll(r.getTag());
		newTag.add(SystemTagEnum.Original_None.toString());

		r.setTag(newTag);
		r.setLastModified(new Date());
		try {
		    repositoryService.save(r);
		    contentSearcher.updateIndex(r.getId());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    resList = repositoryService.getRepositoryBySystemTags(tags,
		    (i * limit) + 1, (i + 1) * 100);
	    if (resList == null || resList.size() < 1) {
		break;
	    }
	    i++;
	}
	message = "增加Original_None操作成功";
	return message;
    }

    private boolean isExistTag(List<String> tagList, String existTag) {
	if (tagList == null || tagList.size() < 1) {
	    return false;
	}

	if (existTag == null || existTag.trim().length() < 1) {
	    return false;
	}

	boolean isExist = false;
	for (String tag : tagList) {
	    if (existTag.equalsIgnoreCase(tag)) {
		isExist = true;
		break;
	    }
	}
	return isExist;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public String clearDo(String loginName, String accountId,
	    HttpServletRequest request,
	    Model model) {
	if (accountId == null || accountId.trim().length() < 1) {
	    if (loginName == null || loginName.trim().length() < 1) {
		model.addAttribute("errorMes", "请输入正确的信息！");
		return "clear";
	    }
	    Account account = accountService.getByLoginName(loginName);
	    if (account != null) {
		accountId = account.getId();
	    }
	}
	if (accountId == null || accountId.trim().length() < 1) {
	    model.addAttribute("errorMes", "请输入正确的信息！");
	    return "clear";
	}

	if (accountService.getById(accountId) == null) {
	    model.addAttribute("errorMes", "请输入正确的信息！");
	    return "clear";
	}

	AuthToken authToken = authTokenService.findByAccountId(accountId);
	if (authToken == null) {
	    model.addAttribute("errorMes", "请先登录！");
	    return "clear";
	}

	List<Repository> responseList = repositoryService
		.findRepositoryByAccountId(accountId);
	if (responseList == null || responseList.size() < 1) {
	    model.addAttribute("errorMes", "没有任何历史记录可删除！");
	    return "clear";
	}

	GenericContent genericContent = new GenericContent();
	List<String> tag = new ArrayList<String>();
	tag.add(SystemTagEnum.Delete_Read.toString());
	genericContent.setTag(tag);
	String repositoryId = null;

	int count = 0;
	for (int i = 0; i < responseList.size(); i++) {
	    Repository res = responseList.get(i);
	    if (res == null) {
		continue;
	    }
	    if (res.getId() == null || res.getId().trim().length() < 1) {
		continue;
		// if(repositoryService.hasSystemTag(res,
		// SystemTagEnum.Delete_Read))continue;
	    }

	    repositoryId = res.getId();
	    String result = contentRpcService.updateRepository(repositoryId,
		    authToken.getId(), genericContent);
	    if (result == null) {
		model.addAttribute("errorMes", "sorry,系统出错了啊!");
		return "clear";
	    }
	    count++;
	}
	if (count < 1) {
	    model.addAttribute("errorMes", "没有任何历史记录可删除！");
	} else {
	    model.addAttribute("errorMes", "成功删除[" + count + "]个历史记录！");
	}

	return "clear";
    }

    @RequestMapping("/messages/deleteAll")
    @ResponseBody
    public String deleteAllMessage(HttpServletRequest request, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	messageService.deleteAll(accountId);
	return "success";
    }

    @RequestMapping("/messages/delete")
    @ResponseBody
    public String deleteMessage(HttpServletRequest request, String messageId,
	    Model model) {
	messageService.delete(messageId);
	return "success";
    }

    @RequestMapping("/download")
    public String download(HttpServletRequest request) {
	return "index/download";
    }

    @RequestMapping("/getLeftContent")
    public String getLeftContent(String accountId, int PageSize, int PageNum,
	    Model model) {
	String accountId_observed = accountId;
	String orderBy = "_id";
	List<ContentDetail> contentDetailList = repositoryService
		.getContentDetailList(accountId_observed, orderBy, PageSize,
			PageNum);
	model.addAttribute("contentDetailList", contentDetailList);

	return "index/user_domain_remain";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request,
	    Model model) {
    String authorization = AuthUtil.getWebTokenAuthorization(request);
    int code = tokenUtil.getAuthorizationErrorCode(authorization);
    if(code > 200) {
    	return "index/index";
    }
    String menuType = "unread";
    String url = "content/personal/unread";
    //guest user
    if(tokenUtil.isGuestUser(authorization)) {
    	menuType = "public";
    }
    //login user
    else {
    	Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
    	String isFirstLogin = "false";
    	if (accountService.removeRole(account, RoleType.NewBie)) {
    	    isFirstLogin = "true";
    	}
    	model.addAttribute("isFirstLogin", isFirstLogin);
    	model.addAttribute("user", covertToAccountVO(account));
    	setMyUseTag(request, model);// 设置用户常用标签
    	model.addAttribute("isHaveClient", this.isHaveClient(account.getId()));
    	this.setThirdPartyProfile(account, request, model);
    }
    model.addAttribute("menuType", menuType);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	
	return url;
    }
    
    private void setThirdPartyProfile(Account account,HttpServletRequest request,Model model) {
    	boolean bind = false;
    	ThirdPartyProfile profile = thirdpartyService.getByAccountId(
    			account.getId(), ThirdPartyType.Weibo);
		if (profile != null && profile.isBind()) {
		    bind = true;
		}
		model.addAttribute("bind", bind);
		if (bind) {
		    List<ContactResult> contacts = thirdPartyRpcService
			    .getSuggestThirdPartyFriends(AuthUtil.getWebTokenAuthorization(request), 5, 0);

		    model.addAttribute("duoduoContacts", contacts);
		}
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
	    return "redirect:/home";//index/index
    }

    @RequestMapping("/ocean/{inviteCode}/{contentId}")
    public String ocean(HttpServletRequest request, Model model,
	    @PathVariable("inviteCode") String inviteCode,
	    @PathVariable("contentId") String contentId) {
	String agent = request.getHeader("User-Agent");
	boolean isMobile = isMobile(agent);
	String accountId = covertinviteCode2AccountId(inviteCode);
	Account account = accountService.getById(accountId);
	// Repository repository = repositoryService
	// .getRepositoryByContentIdAndAccountId(contentId, accountId);
	//
	// model.addAttribute("repository", repository);
	String userAgentType = getUserAgentType(agent);
	if (isMobile) {
	    if (account != null) {
		model.addAttribute("account", account);
	    }
	    ResponseContent content = contentRpcService.getPublicContent(
		    accountId, contentId);

	    if (content != null) {
		model.addAttribute("item", content);
	    }
	    model.addAttribute("client", userAgentType);
	    return "ocean/third_phone";
	}
	// // 登录状态是好友去往咖友分享
	String location = "/ocean/item?id=" + contentId + "&inviteCode="
		+ inviteCode + "&menuType=Later";
	return "redirect:" + location;

    }

    @RequestMapping("/user_base")
    public String userBase(HttpServletRequest request, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	Account account = accountService.getById(accountId);
	if (account == null) {
	    return "index/index";
	}

	model.addAttribute("account", AuthUtil.covertToAccountVO(account));
	return "user_base";
    }

    @RequestMapping("/{domain:[\\d\\w]+$}")
    public String userDomain(@PathVariable("domain") String domain,
	    @QueryParam("pageIndex") String pageIndex,
	    @QueryParam("limit") String limit,
	    HttpServletRequest request, Model model) {
    String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
	AccountVO user = getAccountByAccountId(domain);
	if (user == null) {
	    return "index/user_domain";
	}

	int index = Integer
		.valueOf((pageIndex == null || pageIndex.equals("")) ? "0"
			: pageIndex);
	int size = Integer.valueOf((limit == null || limit.equals("")) ? "20"
		: limit);
	size = ((size < 1 || size > 20) ? 20 : size);

	int total = friendService.countByAccountId(domain);
	List<Account> accountList = null;
	if (total > 0) {
	    accountList = accountService.findByIdList(friendService
		    .pageByAccountId(
			    (index * size >= total ? 0 : index * size),
			    size, domain));
	}

	List<FriendVO> friends = null;
	if (accountList != null && accountList.size() > 0) {
	    friends = findFriends(accountList, user.getId(),
	    		accountId);
	}

	boolean isMyFriend = isFriend(accountId, user.getId());
	if (isMyFriend && !accountId.equals(user.getId())) {
	    model.addAttribute(
		    "friend",
		    friendService.getByAccountIdAndFriendId(
		    		accountId, user.getId()));
	}
	model.addAttribute("isMyFriend", isMyFriend);
	model.addAttribute("friends", friends);
	model.addAttribute("isMe", accountId
		.equals(user.getId()));
	model.addAttribute("user", user);

	model.addAttribute("total", total);
	model.addAttribute("pageIndex", index);
	model.addAttribute("limit", size);

	return "index/user_domain";
    }

    @RequestMapping("/{domain}/friends")
    public String userFriends(@PathVariable("domain") String domain,
	    HttpServletRequest request, Model model) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

	AccountVO visitUser = AuthUtil.getUserInfo(tokenUtil,request);
	AccountVO user = getAccountByAccountId(domain);
	if (user == null) {
	    return "redirect:404";
	}

	List<Account> list = friendService.findFriends(user.getId());
	List<FriendVO> friends = null;
	if (list != null && list.size() > 0) {
	    friends = findFriends(list, user.getId(), accountId);
	}

	model.addAttribute("isMe", accountId.equals(user.getId()));
	model.addAttribute("user", user);
	model.addAttribute("visitUser", visitUser);
	model.addAttribute("friends", friends);

	return "index/user_friends";
    }

    @RequestMapping("/messages")
    public String userMessages(HttpServletRequest request, Model model,
	    @QueryParam("limit") Integer limit,
	    @QueryParam("offset") Integer offset) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
	if(StringUtils.isNotBlank(valid)) {
		return valid;
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	AccountVO user = getAccountByAccountId(accountId);
	notificationService.deleteByAccountIdAndKey(accountId,
		Notification.CollapseKey.message);

	if (limit == null) {
	    limit = 10;
	}

	if (offset == null) {
	    offset = 0;
	}
	List<Message> messagesList = messageService.getMessages(accountId, Message.CatalogId.System, null, limit, offset);

	long totalNumber = messageService.countMessageByAccountId(accountId, Message.CatalogId.System);

	model.addAttribute("totalNumber", totalNumber);
	model.addAttribute("user", user);
	model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	model.addAttribute("messagesList", messagesList);
	return "index/user_messages";
    }
}
