/**
 * 
 */
package me.twocoffee.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.rpc.ContentRpcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiangjun.yu
 * 
 */
@Controller
@RequestMapping("/ocean")
public class OceanController extends BaseController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private final InviteService inviteService = null;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ContentSearcher contentSearcher;

    @Autowired
    private ContentService contentService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private ContentRpcService contentRpcService;

    @Autowired
    private PrivateMessageService messageService;

    @Autowired
    private AccountService accountService;

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
	    String accountId, Model model) {
	ResponseContent result = null;
	// result = contentRpcService.getResponseContentById(id,
	// authToken, null);
	result = contentRpcService.getPublicContent(accountId, contentId);
	model.addAttribute("item", result);
	model.addAttribute("menuType", menuType);

	return result;
    }

    @RequestMapping("item")
    public String getItem(HttpServletRequest request, String id,
	    String menuType, String inviteCode,
	    Model model) {

	String accountId = covertinviteCode2AccountId(inviteCode);
	boolean isFriend = false;
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	String myAccountId = tokenUtil.getAccountIdOrGuestId(authorization);
	Content content = contentService.getById(id);

	if (!tokenUtil.isGuestUser(authorization)) {
	    isFriend = isFriend(myAccountId, accountId);

	    if (content == null) {
		return "content/personal/oceanitem";
	    }

	} else {
	    Account account = accountService.getById(accountId);
	    ResponseContent result = this.setDetail(id, menuType, accountId,
		    model);

	    model.addAttribute("contentId", id);
	    model.addAttribute("account", account);
	    if (result == null) {
		return "content/personal/oceanitem";
	    }
	    model.addAttribute("account", account);
	    model.addAttribute("inviteCode", inviteCode);
	    model.addAttribute("isFriend", isFriend);
	    model.addAttribute("token", authorization);
	    // 增加阅读次数
	    content = content.setVisit(1);
	    contentService.save(content);
	    // 更新索引
	    contentSearcher.updateIndex(result.getResponseId());
	    return "content/personal/oceanitem";
	}
	// 已登录
	Repository repository = repositoryService
		.getRepositoryByContentIdAndAccountId(
			id, myAccountId);

	// 已收藏,不做处理
	if (repository != null) {
	    return "redirect:/private/item?id=" + id;
	}

	// 进行收藏处理
	repository = new Repository();
	List<String> tags = new ArrayList<String>();
	// if (isFriend) {
	// tags.add(SystemTagEnum.Source_Friend.toString());
	// }
	tags.add(SystemTagEnum.Unread.toString());
	repository.setAccountId(myAccountId);
	repository.setContentId(id);
	repository.setTag(tags, content.getContentType().toString());// 系统标签
	repository.setUserTag(null);// 用户标签
	repository.setDate(new Date());
	repository.setLastModified(repository.getDate());
	repositoryService.save(repository);

	// 添加索引
	ContentDetail contentDetail = new ContentDetail();
	contentDetail.setContent(content);
	contentDetail.setRepository(repository);
	contentSearcher.addIndex(contentDetail);
	return "redirect:/private/item?id=" + id;
    }

    private String covertinviteCode2AccountId(String inviteCode) {
	Invite invite = inviteService.getInviteById(inviteCode);

	if (invite != null) {
	    return invite.getOwnerId();
	}
	return "";
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
}
