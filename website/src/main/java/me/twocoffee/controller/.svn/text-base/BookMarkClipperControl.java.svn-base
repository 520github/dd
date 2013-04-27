package me.twocoffee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.i18n.CoffeeCookieLocaleResolver;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.service.rpc.AccountRpcService;
import me.twocoffee.service.rpc.AccountWithToken;
import me.twocoffee.service.rpc.ContentAlreadyCollectException;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent.ContentComment;
import me.twocoffee.service.rpc.FriendRpcService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("bookmark")
public class BookMarkClipperControl extends BaseController {
	private static final String CLIPPER_INDEX_VIEW = "/views/clipper/bookmark/clipper.html";
	private static final String CLIPPER_ERR_PWD_VIEW = "clipper/bookmark/errpwd";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookMarkClipperControl.class);
	@Autowired
	private CoffeeCookieLocaleResolver localeResolver;
	@Autowired
	private AccountRpcService accountService;
	@Autowired
	private ContentRpcService contentService;
	@Autowired
	private FriendRpcService friendService;
	@Autowired
	private VelocityEngine velocityEngine;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private PostContent getContentFromServletRequest(HttpServletRequest request) {
		PostContent postContent = new PostContent();

		postContent.setLanguage(localeResolver.resolveLocale(request)
				.toString());
		if (StringUtils.isNotBlank(request
				.getParameter("contentType"))) {
			postContent.setContentType(ContentType.valueOf(request
					.getParameter("contentType")));
		}

		if (StringUtils.isNotBlank(request.getParameter("id"))) {
			postContent.setId(request.getParameter("id"));
		}
		if (StringUtils.isNotBlank(request.getParameter("url"))) {
			postContent.setUrl(request.getParameter("url"));
		}
		if (StringUtils.isNotBlank(request.getParameter("title"))) {
			postContent.setTitle(request.getParameter("title"));
		}
		if (StringUtils.isNotBlank(request.getParameter("htmlPayload"))) {
			HtmlPayload htmlPayload = new HtmlPayload();
			htmlPayload.setContent(request.getParameter("htmlPayload"));
			postContent.setHtmlPayload(htmlPayload);
		}

		List<String> tagList = new ArrayList<String>();
		tagList.add(SystemTagEnum.Source_Plugin.toString());
		tagList.add(SystemTagEnum.Unread.toString());

		if (StringUtils.isNotBlank(request.getParameter("tag"))) {
			tagList.addAll(Arrays
					.asList(request.getParameter("tag").split(",")));
		}
		postContent.setTag(tagList);

		if (StringUtils.isNotBlank(request.getParameter("toFriends"))) {
			if ("all".equals(request.getParameter("toFriends"))) {
				List<AccountInfo> allFriends = friendService
						.findFriend(AuthUtil.getWebTokenAuthorization(request));
				if (allFriends != null) {
					List<String> allFriendsId = new ArrayList<String>();
					for (AccountInfo accountInfo : allFriends) {
						allFriendsId.add(accountInfo.getId());
					}
					postContent.setShare(allFriendsId);
				}
			} else {
				postContent.setShare(Arrays.asList(request.getParameter(
						"toFriends").split(",")));
			}
		}

		ContentComment contentComment = new ContentComment();
		if (StringUtils.isNotBlank(request.getParameter("comment"))) {
			contentComment.setText(request.getParameter("comment"));
		}
		if (StringUtils.isNotBlank(request.getParameter("score"))) {
			contentComment.setScore(Integer.parseInt(request
					.getParameter("score")));
		}
		postContent.setComment(contentComment);

		return postContent;
	}

	private ResponseEntity<String> getDataResponseEntity(Object data,
			HttpServletRequest request) {
		return getResponseEntity("data", request, null, data);
	}

	private ResponseEntity<String> getMsgResponseEntity(String msg,
			HttpServletRequest request) {
		return getResponseEntity("msg", request, null, msg);
	}

	private ResponseEntity<String> getResponseEntity(String type,
			HttpServletRequest request, Map model, Object value) {

		Map responseJson = new HashMap();

		if ("view".equals(type)) {

			String viewContent = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine,
					localeResolver.resolveLocale(request)
							+ String.valueOf(value),
					"utf-8", model);
			viewContent = viewContent.replaceAll("\r\n", "");
			viewContent = viewContent.replaceAll("\n", "");
			responseJson.put("view", viewContent);

		} else {
			responseJson.put(type, value);
		}

		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/x-javascript;charset=utf-8");

		return new ResponseEntity<String>(request.getParameter("callback")
				+ "(" + JSONObject.fromObject(responseJson).toString()
					+ ")", responseHeader, HttpStatus.OK);
	}

	private ResponseEntity<String> getViewResponseEntity(String view,
			HttpServletRequest request, Map model) {
		return getResponseEntity("view", request, model, view);
	}

	@RequestMapping("preview")
	@ResponseBody
	public ResponseEntity<String> getContentByUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map content = contentService.getContentByUrl(request
				.getParameter("contentType"), request
				.getParameter("url"), request.getHeader("Referer"));

		if (content == null || content.get("id") == null) {
			return getMsgResponseEntity(getMessage("serverInternalError"),
					request);
		} else {
			return getDataResponseEntity(content,
					request);
		}
	}

	@RequestMapping("private/friends")
	@ResponseBody
	public ResponseEntity<String> getFriends(HttpServletRequest request,
			int pageIndex) {
		String token = AuthUtil.getWebTokenAuthorization(request);
		List<AccountInfo> accountInfoList = friendService.findFriend(token);

		// FIXME 使用接口中查询出来的数据代替假数据
		// accountInfoList=new ArrayList<AccountInfo>();
		// AccountInfo accountOne=new AccountInfo();
		// accountOne.setAccountName("假的中文名字字字字字字字字"+pageIndex+"1");
		// accountOne.setId("mockId"+pageIndex+"1");
		//
		// AccountInfo accountTwo=new AccountInfo();
		// accountTwo.setAccountName("假的中文名字字字字字字字字"+pageIndex+"2");
		// accountTwo.setId("mockId"+pageIndex+"1");
		//
		// accountInfoList.add(accountOne);
		// accountInfoList.add(accountTwo);

		return getDataResponseEntity(accountInfoList, request);
	}

	@RequestMapping("login")
	@ResponseBody
	public ResponseEntity<String> login(HttpServletRequest request,
			HttpServletResponse response) {

		String account = request.getParameter("username");
		String password = request.getParameter("password");
		String webToken = AuthUtil.getWebTokenFromCookie(request);
		webToken = TokenUtil.WEB_TOKEN + " " + webToken;
		AccountWithToken accountWithToken = accountService.auth(account,password,webToken);

		if (accountWithToken == null) {
			LOGGER.debug("{} ****** auth failed", account);
			return getMsgResponseEntity(getMessage("invalid"), request);

		} else {
			Map<String, Object> model = new HashMap<String, Object>();
			boolean isAutoLogin = "on".equals(request
					.getParameter("rememberMe"));

			AuthUtil.writeAuthToken(response, accountWithToken.getAuthToken(),
					isAutoLogin);

			AuthUtil.writeMessageToken(response, "",
					isAutoLogin);

			model.put("account", accountWithToken.getAccount());
			return getViewResponseEntity(CLIPPER_INDEX_VIEW, request, model);
		}
	}

	@RequestMapping("private/portal")
	@ResponseBody
	public ResponseEntity<String> portal(HttpServletRequest request) {

		Map model = new HashMap();
		model.put("account", request.getAttribute("account"));

		return getViewResponseEntity(CLIPPER_INDEX_VIEW, request, model);
	}

	@RequestMapping("private/store")
	@ResponseBody
	public ResponseEntity<String> postContent(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PostContent postContent = getContentFromServletRequest(request);

		Map content = null;
		try {
			content = contentService.postContent(AuthUtil
					.getAuthTokenFromCookie(request),
					postContent);
		} catch (ContentAlreadyCollectException e) {
			log.debug(e.getMessage());
			return getMsgResponseEntity(getMessage("contentAlreadyExists"),
					request);
		}

		if (content == null || content.get("id") == null) {
			return getMsgResponseEntity(getMessage("serverInternalError"),
					request);
		} else {
			return getMsgResponseEntity(getMessage("storeSuccess"), request);
		}
	}
}