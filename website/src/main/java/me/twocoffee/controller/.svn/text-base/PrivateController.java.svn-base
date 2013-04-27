/**
 * 
 */
package me.twocoffee.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ResponseSession;
import me.twocoffee.service.entity.ContentDetail.From;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuehui.miao
 * 
 */
@Controller
@RequestMapping("/private")
public class PrivateController extends BaseController {
	private static String paths[] = {"all","later","friend","productwish","weibo","renren","upload","favorite","myrecommend","weixinpublic","public"};
	private static String menus[] = {"all","later","friend","productwish","weibo","renren","upload","favorite","myrecommend","weixinpublic","public"};
	@Autowired
	private ThirdpartyService thirdpartyService;
	
	@Autowired
	private ThirdPartyRpcService thirdPartyRpcService;
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ContentSearcher contentSearcher;

	@Autowired
	private ContentService contentService;

	@Autowired
	private TagService tagService;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private ContentRpcService contentRpcService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private FriendService friendService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PrivateMessageService messageService;

	private Map<String, Long> getCommentBadge(String id, String token,
			ResponseContent result) {

		Map<String, Long> r = new HashMap<String, Long>();
		String accountId  = tokenUtil.getAccountIdOrGuestId(token);
		if (null!=result && result.getFrom() != null) {

			for (From f : result.getFrom()) {
				r.put(f.getAccountId(), messageService.countMessage(accountId,
						f.getAccountId(), id));

			}
		}
		return r;
	}

	private int getFriendNumber(String id) {
		return friendService.countByAccountId(id);
	}

	private String getReplaceUrl(String url) {
		if (url == null)
			return url;
		url = url.replaceAll("!AND!", "&");
		url = url.replaceAll("!EQUAL!", "=");
		return url;
	}

	private int getSharedNumber(String id) {
		return repositoryService.countSharedByAccountId(id);
	}

	private String getToken(HttpServletRequest request) {
		return AuthUtil.getWebTokenAuthorization(request);
	}

	/**
	 * 设置内容详细信息
	 * 
	 * @param contentId
	 * @param menuType
	 * @param request
	 * @param model
	 * @return
	 */
	private ResponseContent setDetail(String contentId, String menuType,
			HttpServletRequest request, Model model) {
		ResponseContent result = null;	
		String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
		List<ResponseSession> sessions = messageService.getSessionsForContentId(accountId, contentId,0, Integer.MAX_VALUE);
		
		result = contentRpcService.getResponseContentById(contentId,AuthUtil.getWebTokenAuthorization(request), null);
		model.addAttribute("item", result);
		model.addAttribute("menuType", menuType);
		model.addAttribute("token", this.getToken(request));
		model.addAttribute("badge",
				getCommentBadge(contentId, AuthUtil.getWebTokenAuthorization(request), result));
		model.addAttribute("sessions",sessions);
		
		return result;
	}

	/**
	 * 用户常用的标签
	 * 
	 * @param request
	 * @param model
	 */
	private void setMyUseTag(HttpServletRequest request, Model model) {
		String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
		String myUseTag = tagService.getTopHotTagStr(accountId, 5, " ");
		model.addAttribute("myUseTag", myUseTag);
	}

	/**
	 * 设置用户基本信息
	 * 
	 * @param request
	 * @param model
	 */
	private void setUserInfo(HttpServletRequest request, Model model) {
		AccountVO user = AuthUtil.getUserInfo(tokenUtil,request);
		if (user != null) {
			user.setSharedNumber(getSharedNumber(user.getId()));
			user.setFriendNumber(getFriendNumber(user.getId()));
			model.addAttribute("isHaveClient", this.isHaveClient(user.getId()));
		}
		model.addAttribute("user", user);
	}
	
	@RequestMapping("friend")
	public String friend(HttpServletRequest request, Model model) {
		String accountIdOrGuestId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
		String friendId = request.getParameter("friendId");

		List<Account> friends = friendService.findFriends(accountIdOrGuestId);
		notificationService.deleteByAccountIdAndKey(accountIdOrGuestId,
				Notification.CollapseKey.RecommendByFriend);
		model.addAttribute("friendTag", SystemTagEnum.Source_Friend.toString());
		model.addAttribute("friends", friends);
		model.addAttribute("friendId", friendId);
		
		this.commonMenu(request, model);

		return "content/personal/friend";
	}

	@RequestMapping("favorite")
	public String getFavorite(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/favorite";
	}

	@RequestMapping("later")
	public String getLaterLook(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/laterlook";
	}

	@RequestMapping("read")
	public String getRead(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/read";
	}
	
	@RequestMapping("myrecommend")
	public String getMyRecommend(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/myrecommend";
	}
	
	@RequestMapping("all")
	public String getAll(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("public")
	public String getPublic(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("weibo")
	public String getWeibo(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("renren")
	public String getRenren(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("weixinpublic")
	public String getWeixinPublic(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("productwish")
	public String getProductWish(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("upload")
	public String getUpload(HttpServletRequest request, Model model) {
		this.commonMenu(request, model);
		return "content/personal/menu";
	}
	
	@RequestMapping("item")
	public String getItem(String id, String menuType,
			HttpServletRequest request, Model model ,String inviteCode) {
		String contentId = id;
		ResponseContent result = this.setDetail(contentId, menuType, request, model);
		if (result == null) {
			if(StringUtils.isNotBlank(inviteCode)){
				String ocean_location = "/ocean/item?id=" + contentId + "&inviteCode=" + inviteCode;
				return "redirect:" + ocean_location;
			}
			return "content/personal/item";
		}
		// 更新系统标签,删除系统未读tag
		Repository repository = repositoryService.getById(result
				.getResponseId());

		repository.setTag(repositoryService.getTag(
				SystemTagEnum.Read.toString(), repository.getTag(), menuType));
		repository.setLastModified(new Date());
		repositoryService.save(repository);

		// 增加阅读次数
		Content content = contentService.getById(result.getId());
		content = content.setVisit(1);
		contentService.save(content);

		this.setMyUseTag(request, model);

		// 更新索引
		contentSearcher.updateIndex(result.getResponseId());

		return "content/personal/item";
	}


	@RequestMapping("shareperson")
	public String sharePerson(String contentId, String menuType,
			HttpServletRequest request, Model model) {
		this.setDetail(contentId, menuType, request, model);
		return "content/personal/shareperson";
	}

	@RequestMapping("third")
	public String thirdUrl(String url, HttpServletRequest request, Model model) {
		url = this.getReplaceUrl(url);
		model.addAttribute("url", url);
		return "content/personal/third";
	}
	
	@RequestMapping("url")
	public String forwardUrl(String contentId, String menuType, String url,
			HttpServletRequest request, Model model) {
		model.addAttribute("url", url);
		model.addAttribute("contentId", contentId);
		model.addAttribute("menuType", menuType);
		return "content/personal/url";
	}

	@RequestMapping("urlhead")
	public String urlHead(String url, HttpServletRequest request, Model model) {
		model.addAttribute("url", url);
		model.addAttribute("target", "target=\"blank\"");
		return "head2";
	}
	
	private void commonMenu(HttpServletRequest request, Model model) {
		model.addAttribute("token", this.getToken(request));
		this.setMenuType(request, model);
		this.setMyUseTag(request, model);
		this.setUserInfo(request, model);
		this.setThirdParty(request, model);
	}
	
	private void setMenuType(HttpServletRequest request, Model model) {
		String servletPath = request.getServletPath()==null?"":request.getServletPath();
		if(paths == null)return;
		if(menus == null)return;
		
		String menuType = "";
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if(servletPath.indexOf(path) >-1) {
				try {
					menuType = menus[i];
				} catch (Exception e) {
				}
				break;
			}
		}
		
		model.addAttribute("menuType", menuType);
	}
	
	private void setThirdParty(HttpServletRequest request, Model model) {
		String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
		boolean bind = false;
		ThirdPartyProfile profile = thirdpartyService.getByAccountId(
				accountId, ThirdPartyType.Weibo);
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
}
