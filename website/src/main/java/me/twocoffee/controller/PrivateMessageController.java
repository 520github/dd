/**
 * 
 */
package me.twocoffee.controller;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.search.PagedResult;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.entity.Repository;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.rpc.PrivateMessageRpcService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author momo
 * 
 */
@Controller
@RequestMapping("/message/private")
public class PrivateMessageController extends BaseController {
	private static final String MESSAGE_SERVER_INTERNAL_ERROR = "serverInternalError";
	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private FriendService friendService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private PrivateMessageRpcService messageRpcService;

	@Autowired
	private NotificationService notificationService;

	private static final String COMMENT_ERROR_NOTFRIEND = "comment.error.notfriend";

	private AccountVO covertToAccountVO(Account a) {
		AccountVO av = AuthUtil.covertToAccountVO(a);
		av.setSharedNumber(getSharedNumber(a.getId()));
		av.setFriendNumber(getFriendNumber(a.getId()));
		return av;
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

	private boolean isFriend(String from, String to) {
		List<Account> friends = friendService.findFriends(from);

		if (friends != null) {

			for (Account a : friends) {

				if (a.getId().equals(to)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 删除会话； accountId和contentId同时为空则删除当前用户的所有会话。
	 * 
	 * @param accountId
	 * @param contentId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/session/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteSession(HttpServletRequest request,
			String accountId,
			String contentId, Model model) {

		int result = messageRpcService.deleteSession(
				AuthUtil.getWebTokenAuthorization(request), contentId, accountId);

		if (result == 204) {
			return buildJSONResult("success", "success");
		}
		return buildJSONResult("error", "server error!please try again!");
	}

	/**
	 * 获取指定会话的消息列表
	 * 
	 * @param sessionId
	 * @return
	 */
	@RequestMapping("/session")
	public ResponseEntity<String> getMessagesInSession(
			HttpServletRequest request,
			String sessionId, String sinceId) {

		PrivateMessageSession session = privateMessageService
				.getSession(sessionId);

		if (session == null) {
			return buildJSONResult("error", "error");
		}
		PagedResult<PrivateMessage> pageResult = messageRpcService
				.getPrivateMessagesInSession(
						AuthUtil.getWebTokenAuthorization(request),
						session.getAccountId(), session.getContentId(), sinceId);

		Content content = contentService.getById(session.getContentId());
		Repository repository = repositoryService
				.getRepositoryByContentIdAndAccountId(session.getContentId(),
						session.getOwneraccountId());

		long totalNumber = 0;
		Map<String, Object> model = new HashMap<String, Object>();

		if (pageResult != null) {
			model.put("messages", pageResult.getResult());
			totalNumber = pageResult.getTotal();
		}
		Account fromAccount = accountService.getById(session.getOwneraccountId());
		if(fromAccount == null) {
			return buildJSONResult("error", "error");
		}

		Account targetAccount = accountService.getById(session.getAccountId());
		notificationService.deleteByAccountIdAndKey(fromAccount.getId(),
				Notification.CollapseKey.Chat);

		model.put("fromAccount",
				AuthUtil.covertToAccountVO(fromAccount));

		model.put("targetAccount",
				AuthUtil.covertToAccountVO(targetAccount));

		model.put("repository", repository);
		model.put("totalNumber", totalNumber);
		model.put("session", session);
		model.put("content", content);
		return buildJSONResult1(model);
	}

	/**
	 * 获取指定用户的会话列表
	 * 
	 * @param accountId
	 * @param model
	 * @return
	 */
	@RequestMapping("/session/index")
	@ResponseBody
	public ResponseEntity<String> getSessions(HttpServletRequest request,
			String limit,
			String offset) {

		int limitNum = 0;
		int offsetNum = 0;

		if (StringUtils.isNotBlank(limit)) {

			try {
				limitNum = Integer.valueOf(limit);

			} catch (NumberFormatException e) {
				// ignore
			}
		}

		if (StringUtils.isNotBlank(offset)) {

			try {
				offsetNum = Integer.valueOf(offset);

			} catch (NumberFormatException e) {
				// ignore
			}
		}
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if (!tokenUtil.isGuestUser(authorization)) {
			    String accountId = tokenUtil.getGuestUserId(authorization);
				AccountVO user = getAccountByAccountId(accountId);
				PagedResult<PrivateMessageSession> result = messageRpcService
						.getPrivateMessageSessions(authorization, "", limitNum,
								offsetNum);

				List<Map<String, Object>> sessionVo = new LinkedList<Map<String,
						Object>>();

				long totalNumber = 0;

				if (result != null) {
					totalNumber = result.getTotal();

					if (result.getResult() != null) {

						for (PrivateMessageSession s : result.getResult()) {
							s.setOwneraccountId(accountId);
							Map<String, Object> map = new LinkedHashMap<String, Object>();
							map.put("session", s);

							Account account = accountService.getById(s
									.getAccountId());
							if (null == account)
								continue;
							map.put("talker",
									AuthUtil.covertToAccountVO(account));

							Content content = contentService.getById(s
									.getContentId());
							if (null == content) { // 有时会发生content在数据库中不见了的情况，虽然实际中不应该发生
								continue;
							}

							Repository repository = repositoryService
									.getRepositoryByContentIdAndAccountId(
											s.getContentId(),accountId);
							map.put("repository", repository);
							map.put("content", content);
							sessionVo.add(map);
						}
					}
				}
				notificationService
						.deleteByAccountIdAndKey(accountId,
								Notification.CollapseKey.Chat);

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("sessions", sessionVo);
				model.put("totalNumber", totalNumber);
				model.put("user", user);
				return buildJSONResult1(model);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sessions", null);
		model.put("totalNumber", 0);
		model.put("user", null);
		return buildJSONResult1(model);
	}

	/**
	 * 发表评论
	 * 
	 * @param accountId
	 * @param sessionId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> postMessage(HttpServletRequest request,
			String message,
			String sessionId, Model model) {

		if (StringUtils.isBlank(sessionId)) {
			return buildJSONResult("error", "invalid input!");
		}

		try {
			PrivateMessageSession session = privateMessageService
					.getSession(sessionId);

			if (!isFriend(session.getAccountId(), session.getOwneraccountId())) {
				return buildJSONResult("error",
						getMessage(COMMENT_ERROR_NOTFRIEND));

			}
			String msg = URLEncoder.encode(message, "UTF-8");
			int result = messageRpcService
					.sendPrivateMessage(
							AuthUtil.getWebTokenAuthorization(request),
							session.getContentId(), session.getAccountId(),
							msg, "");

			if (result == 204 || result == 202) {
				return buildJSONResult("success", "success");

			} else if (result == 409) {
				return buildJSONResult("error", "send the same message!");
			}
			return buildJSONResult("error",
					getMessage(MESSAGE_SERVER_INTERNAL_ERROR));

		} catch (Exception e) {
			return buildJSONResult("error",
					getMessage(MESSAGE_SERVER_INTERNAL_ERROR));
		}
	}

	/**
	 * 显示消息列表页
	 * 
	 * @return
	 */
	@RequestMapping("/message/view")
	public String showMessageview(String sessionId, String accountId,
			String contentId, HttpServletRequest request, Model model) {
		String accountIdOrGuestId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));

		if (StringUtils.isBlank(sessionId)) {
			PrivateMessageSession session = privateMessageService.getSession(
					accountIdOrGuestId, accountId,contentId);
			if (session != null) {
				sessionId = session.getId();

			} else {
				session = new PrivateMessageSession();
				session.setOwneraccountId(accountIdOrGuestId);
				session.setAccountId(accountId);
				session.setContentId(contentId);
				session.setMessageCount(0);
				session.setUnread(0);
				PrivateMessageSession.LastUpdate lastupdate = new PrivateMessageSession.LastUpdate();
				lastupdate.setAccountId(accountIdOrGuestId);
				lastupdate.setDate(new Date());
				lastupdate.setMessage("他很深沉，只有分享，没有评论。");
				session.setLastUpdate(lastupdate);
				privateMessageService.creatPrivateMessageSession(session);
				sessionId = session.getId();
			}
		}
		notificationService.deleteByAccountIdAndKey(accountIdOrGuestId,
				Notification.CollapseKey.Chat);
		model.addAttribute("sessionId", sessionId);
		return "/message/msgs";
	}

	/**
	 * 显示会话列表页
	 * 
	 * @return
	 */
	@RequestMapping("/session/view")
	public String showSessionview(HttpServletRequest request) {
		return "/message/sessions";
	}

}
