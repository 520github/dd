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
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.Content.FilePayload;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Document;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.ImagePayload;
import me.twocoffee.entity.ProductPayload;
import me.twocoffee.entity.Settings.FriendGroup;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.i18n.CoffeeCookieLocaleResolver;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DocumentService;
import me.twocoffee.service.rpc.AccountRpcService;
import me.twocoffee.service.rpc.AccountWithToken;
import me.twocoffee.service.rpc.ContentAlreadyCollectException;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent.ContentComment;
import me.twocoffee.service.rpc.FriendRpcService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("newbookmark")
public class NewBookMarkClipperControl extends BaseController {
	private static final String CLIPPER_INDEX_VIEW = "clipper/bookmark/new_clipper";
	private static final String CLIPPER_FAST_STEP_VIEW = "clipper/bookmark/fast_step_clipper";
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
	private DocumentService documentService;
	@Autowired
	private AuthTokenService authTokenService;

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

	private PostContent getContentFromServletRequest(HttpServletRequest request) {
		PostContent postContent = new PostContent();

		postContent.setLanguage(localeResolver.resolveLocale(request)
				.toString());
		String contentType = request.getParameter("contentType");
		if (contentType == null)
			contentType = "";
		postContent.setShare(this.getToFriendsList(request));
		List<String> tagList = new ArrayList<String>();
		// source tag
		if (ContentType.File.toString().equals(contentType)) {// file
			tagList.add(SystemTagEnum.Source_Upload.toString());
		} else if (contentType.indexOf(ContentType.File.toString()) > -1) {// file_image
			tagList.add(SystemTagEnum.Source_Upload.toString());
		} else {
			tagList.add(SystemTagEnum.Source_Plugin.toString());
		}
		// type tag
		if (StringUtils.isNotBlank(request.getParameter("tag"))) {
			tagList.add(SystemTagEnum.Unread.toString());
			tagList.addAll(Arrays
					.asList(request.getParameter("tag").split(",")));
		}
		if (postContent.getShare() != null && postContent.getShare().size() > 0) {
			tagList.add(SystemTagEnum.My_Recommend.toString());
		}
		postContent.setTag(tagList);

		if (ContentType.File.toString().equals(contentType)) {// file
			postContent.setFilePayload(this.getFilePayload(request));
		} else if (contentType.indexOf(ContentType.File.toString()) > -1
				&& contentType.indexOf(ContentType.Image.toString()) > -1) {
			postContent.setImagePayload(this.getImagePayload(request));
			contentType = ContentType.Image.toString();
			postContent.setUrl(postContent.getImagePayload().getUrl());
		}

		if (StringUtils.isNotBlank(contentType)) {
			postContent.setContentType(ContentType.valueOf(contentType));
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

		ContentComment contentComment = new ContentComment();
		if (StringUtils.isNotBlank(request.getParameter("comment"))) {
			contentComment.setText(request.getParameter("comment"));
		}
		if (StringUtils.isNotBlank(request.getParameter("score"))) {
			contentComment.setScore(Integer.parseInt(request
					.getParameter("score")));
		}
		postContent.setComment(contentComment);
		
		//set product data
		postContent.setProductPayload(this.getProductPayload(request));

		return postContent;
	}
	
	private ProductPayload getProductPayload(HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter("productPrice"))) {
			return null;
		}
		ProductPayload productPayload = new ProductPayload();
		productPayload.setPicture(request.getParameter("productImageUrl"));
		productPayload.setPrice(request.getParameter("productPrice"));
		productPayload.setName(request.getParameter("productName"));
		return productPayload;
	}

	private FilePayload getFilePayload(HttpServletRequest request) {
		FilePayload filePayload = new FilePayload();
		String fileId = request.getParameter("fileId");
		Document document = documentService.getDocumentById(fileId);
		if (document != null) {
			filePayload.setId(fileId);
			filePayload.setContentType(document.getMimeType());
			filePayload.setLength(document.getLength());
			filePayload.setName(document.getName());
			filePayload.setUrl(document.getUrl());
			filePayload.setPostfix(FilenameUtils.getExtension(document
					.getName()));

		} else {
			filePayload.setId(fileId);
			filePayload.setContentType(StringUtils.trimToEmpty(request
					.getParameter("fileType")));
			String fileSize = request.getParameter("fileSize");
			if (fileSize == null)
				fileSize = "0";
			filePayload.setLength(Long.valueOf(fileSize));
			filePayload.setName(StringUtils.trimToEmpty(request
					.getParameter("fileName")));
			filePayload.setUrl(StringUtils.trimToEmpty(request
					.getParameter("fileUrl")));
		}

		return filePayload;
	}

	private ImagePayload getImagePayload(HttpServletRequest request) {
		ImagePayload imagePayload = new ImagePayload();

		String fileId = request.getParameter("fileId");
		Document document = documentService.getDocumentById(fileId);
		if (document != null) {
			imagePayload.setId(fileId);
			imagePayload.setMimeType(document.getMimeType());
			imagePayload.setLength(Integer.parseInt(String.valueOf(document
					.getLength())));
			imagePayload.setName(document.getName());
			imagePayload.setUrl(document.getUrl());
		} else {
			imagePayload.setId(fileId);
			imagePayload.setMimeType(StringUtils.trimToEmpty(request
					.getParameter("fileType")));
			String fileSize = request.getParameter("fileSize");
			if (fileSize == null)
				fileSize = "0";
			imagePayload.setLength(Integer.parseInt(fileSize));
			imagePayload.setName(StringUtils.trimToEmpty(request
					.getParameter("fileName")));
			imagePayload.setUrl(StringUtils.trimToEmpty(request
					.getParameter("fileUrl")));
		}
		return imagePayload;
	}

	private ResponseEntity<String> getMsgResponseEntity(String msg) {
		return getResponseEntity("{\"msg\":\"" + msg + "\"}");
	}

	private ResponseEntity<String> getResponseEntity(Object responseData) {

		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		return new ResponseEntity<String>(json(responseData)
					, responseHeader, HttpStatus.OK);
	}

	private String getSmallPhoto(Account a) {
		if (a == null || a.getPhotos() == null)
			return "";

		return a.getPhotos().get(Account.PHOTO_SIZE_SMALL);
	}

	private List<String> getToFriendsList(HttpServletRequest request) {
		String toFriends = request.getParameter("toFriends");
		if (toFriends == null)
			toFriends = "";
		if (toFriends.trim().length() < 1)
			return null;
		if (FriendGroup.Me.toString().equalsIgnoreCase(toFriends)) {
			return null;
		}
		List<String> toFriendsList = new ArrayList<String>();
		if (FriendGroup.All.toString().equalsIgnoreCase(toFriends)) {
			List<AccountInfo> allFriends = friendService.findFriend(
					AuthUtil.getWebTokenAuthorization(request));
			if (allFriends == null)
				return null;
			for (AccountInfo accountInfo : allFriends) {
				toFriendsList.add(accountInfo.getId());
			}
		} else if (FriendGroup.FavoritesAndMe.toString().equalsIgnoreCase(
				toFriends)) {
			List<AccountInfo> favoriteFriends = friendService
					.findFavoriteFriend(
					AuthUtil.getWebTokenAuthorization(request));
			if (favoriteFriends == null)
				return null;
			for (AccountInfo accountInfo : favoriteFriends) {
				toFriendsList.add(accountInfo.getId());
			}
		} else {
			toFriendsList.addAll(Arrays.asList(toFriends.split(",")));
		}
		return toFriendsList;
	}

	/**
	 * 给第三方提供的截取页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("style2/clipper")
	public String clipperStyle2(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String token = AuthUtil.getWebTokenAuthorization(request);
		String photo = getSmallPhoto((Account) request.getAttribute("account"));
		model.addAttribute("photo", photo);
		model.addAttribute("account", request.getAttribute("account"));

		List<AccountInfo> fs = friendService.findFriend(token);
		model.addAttribute("friends", fs);
		model.addAttribute("favorites", findFavoriteFriend(fs));
		model.addAttribute("lasts", friendService.findFriendLastTimeUsed(token));

		return "clipper/bookmark/cp-style2";
	}

	@RequestMapping("private/fast_step_portal")
	public String fastStepPortal(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.portal(request, response, model);
		return CLIPPER_FAST_STEP_VIEW;
	}

	@RequestMapping("preview")
	@ResponseBody
	public ResponseEntity<String> getContentByUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map content = contentService.getContentByUrl(request
				.getParameter("contentType"), request
				.getParameter("url"), request.getParameter("refer"));

		if (content == null || content.get("id") == null) {
			return getMsgResponseEntity(getMessage("serverInternalError"));
		} else {
			Map responseMap = new HashMap();
			responseMap.put("data", content);
			return getResponseEntity(responseMap);
		}
	}

	@RequestMapping("private/friends")
	@ResponseBody
	public ResponseEntity<String> getFriends(HttpServletRequest request,
			int pageIndex) {
		List<AccountInfo> accountInfoList = friendService.findFriend(AuthUtil.getWebTokenAuthorization(request));

		Map responseMap = new HashMap();
		responseMap.put("data", accountInfoList);

		return getResponseEntity(responseMap);
	}

	@RequestMapping("login")
	@ResponseBody
	public ResponseEntity<String> login(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("P3P", "CP=CAO PSA OUR");
		String account = request.getParameter("username");
		String password = request.getParameter("password");
		String webToken = AuthUtil.getWebTokenAuthorization(request);
		AccountWithToken accountWithToken = accountService.auth(account,password,webToken);

		if (accountWithToken == null) {
			LOGGER.debug("{} ****** auth failed", account);
			return getMsgResponseEntity(getMessage("login.error.invalid"));

		} else {
			AuthUtil.writeAuthToken(response, accountWithToken.getAuthToken(),
					"on".equals(request.getParameter("rememberMe")));

			return getResponseEntity("{\"success\":" + true + "}");
		}
	}

	@RequestMapping("new_login")
	public String new_login() {
		return "clipper/bookmark/new_login";
	}

	@RequestMapping("private/portal")
	public String portal(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
		model.addAttribute("account", tokenUtil.getLoginUserAccontByAuthorization(AuthUtil.getWebTokenAuthorization(request)));

//		List<AccountInfo> fs = friendService.findFriend(token);
//		model.addAttribute("friends", fs);
//		model.addAttribute("favorites", findFavoriteFriend(fs));
//		model.addAttribute("lasts", friendService.findFriendLastTimeUsed(token));

		return CLIPPER_INDEX_VIEW;
	}

	@RequestMapping("private/store")
	@ResponseBody
	public ResponseEntity<String> postContent(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		this.pluginLog(request);
		PostContent postContent = getContentFromServletRequest(request);
		StringBuffer result = new StringBuffer("{");
		Map content = null;
		try {
			content = contentService.postContent(AuthUtil.getWebTokenAuthorization(request),
					postContent);
		} catch (ContentAlreadyCollectException e) {
			log.debug(e.getMessage());
			result.append("\"msg\":\"" + getMessage("contentAlreadyExists")
					+ "\"");
			return getResponseEntity(result.toString());
		}

		if (content == null || content.get("id") == null) {
			result.append("\"msg\":\"" + getMessage("serverInternalError")
					+ "\"");
			// return getMsgResponseEntity(getMessage("serverInternalError"));
		} else {
			result.append("\"msg\":\"" + getMessage("storeSuccess") + "\"");
			result.append(",\"code\":\"200\"");
			result.append(",\"contentId\":\"" + content.get("id") + "\"");
			// return getMsgResponseEntity(getMessage("storeSuccess"));
		}

		result.append("}");
		return getResponseEntity(result.toString());
	}
	
	private void pluginLog(HttpServletRequest request) {
		String pluginLogData = request.getParameter("pluginLogData");
		if(pluginLogData == null)return;
		String url = request.getParameter("url");
		AuthToken authToken = authTokenService.findById(AuthUtil
				.getAuthTokenFromCookie(request));
		String accountId = "";
		if(authToken != null)accountId = authToken.getAccountId();
		Object obj[] = new Object[]{accountId,url,pluginLogData};
		LOGGER.info("pluginLog:userAccountId [{}],clipperUrl [{}],logMessage [{}]",obj);	
	}
}