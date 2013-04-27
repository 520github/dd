package me.twocoffee.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.rpc.ImageService;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//TODO:SNS 解除绑定 微博
@Controller
public class UserGravatarController extends BaseController {

	@Autowired
	private ThirdPartyRpcService thirdPartyRpcService;

	@Autowired
	private FileOperator fileOperator;
	@Autowired
	private ImageService imageService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ThirdpartyService thirdPartyService;

	@Autowired
	private AuthTokenService authTokenService;

	@RequestMapping(value = "/account/user/accountmail/deletetag", method = RequestMethod.POST)
	public ResponseEntity<String> deleteInTags(
			HttpServletRequest request,
			@RequestParam("tag") String tag) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
			Account account = accountService.getById(accountId);
			AccountMailConfig mailConfig = account.getMailConfig();
			if (mailConfig != null && mailConfig.getInTags() != null) {
				mailConfig.getInTags().remove(tag);
			}
			account.setMailConfig(mailConfig);
			accountService.save(account);
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/accountmail", method = RequestMethod.GET)
	public String getAccountmail(
			HttpServletRequest request, Model model) {

		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		model.addAttribute("account", tokenUtil.getLoginUserAccontByAuthorization(authorization));
		return "user/email_manage";
	}

	@RequestMapping(value = "/account/user/thirdparty_config", method = RequestMethod.GET)
	public String getThirdparty_config(HttpServletRequest request, Model model) {
		Account account = tokenUtil.getLoginUserAccontByAuthorization(AuthUtil.getWebTokenAuthorization(request));
		if(account != null) {
			model.addAttribute("account", account);
			ThirdPartyProfile thirdPartyProfileWeibo = thirdPartyService
			.getByAccountId(account.getId(), ThirdPartyType.Weibo);
			model.addAttribute("thirdPartyProfileWeibo", thirdPartyProfileWeibo);
		}
		model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
		return "user/thirdparty_manage";
	}

	@RequestMapping(value = "/account/user/gravatar/crop", method = RequestMethod.POST)
	public ResponseEntity<String> imageCrop(
			HttpServletRequest request,
			@RequestParam("imageid") String imageid,
			@RequestParam("x") String x, @RequestParam("y") String y,
			@RequestParam("w") String w, @RequestParam("h") String h) {
		try {
			String authorization = AuthUtil.getWebTokenAuthorization(request);
			if(!tokenUtil.isGuestUser(authorization)) {
				String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
				Account account = accountService.getById(accountId);
				Map<String, String> map = imageService.cropImage(
						x.equals("") ? 0
								: Integer.valueOf(x), y
								.equals("") ? 0 : Integer.valueOf(y),
						w.equals("") ? 0
								: Integer.valueOf(w),
						h.equals("") ? 0 : Integer.valueOf(h), imageid);
				account.setPhotos(map);
				accountService.save(account);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return buildJSONResult("result", "error");
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/gravatar", method = RequestMethod.POST)
	public ResponseEntity<String> imageUpload(
			@RequestParam("imageFile") MultipartFile item) {
		String id = "";
		try {
			String type = imageService.getSuffixByContentType(item
					.getContentType());
			if (!type.equals("")) {
				id = fileOperator.putFile(
						item.getBytes(),
						"." + type);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"text/html;charset=utf-8");
		Map<String, String> map = new HashMap<String, String>();
		map.put("imageid", id);
		map.put("url", fileOperator.getFileUrl(id));
		return new ResponseEntity<String>(json(map), responseHeader,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/account/user/accountmail", method = RequestMethod.POST)
	public ResponseEntity<String> putAccountmail(
			HttpServletRequest request,
			@RequestParam("original") String originalEmail,
			@RequestParam("new") String newEmail) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
			Account account = accountService.getById(accountId);
			AccountMailConfig mailConfig = account.getMailConfig();
			if (mailConfig == null) {
				mailConfig = new AccountMailConfig();
				mailConfig.setFriendsVisible(true);
			}
			if (originalEmail != null && !originalEmail.equals("")
					&& mailConfig.getMails().contains(originalEmail)) {
				mailConfig.getMails().remove(originalEmail);
			}
			if (newEmail != null && !newEmail.equals("")) {
				if (mailConfig.getMails() == null) {
					List<String> l = new ArrayList<String>();
					l.add(newEmail);
					mailConfig.setMails(l);
				} else if (!mailConfig.getMails().contains(newEmail)) {
					mailConfig.getMails().add(0, newEmail);
				}
			}
			account.setMailConfig(mailConfig);
			accountService.save(account);
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/thirdparty_config", method = RequestMethod.POST)
	public ResponseEntity<String> putThirdparty_config(
			HttpServletRequest request,
			@RequestParam("original") String originalEmail,
			@RequestParam("new") String newEmail) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
			AccountMailConfig mailConfig = account.getMailConfig();
			if (mailConfig == null) {
				mailConfig = new AccountMailConfig();
				mailConfig.setFriendsVisible(true);
			}
			if (originalEmail != null && !originalEmail.equals("")
					&& mailConfig.getMails().contains(originalEmail)) {
				mailConfig.getMails().remove(originalEmail);
			}
			if (newEmail != null && !newEmail.equals("")) {
				if (mailConfig.getMails() == null) {
					List<String> l = new ArrayList<String>();
					l.add(newEmail);
					mailConfig.setMails(l);
				} else if (!mailConfig.getMails().contains(newEmail)) {
					mailConfig.getMails().add(0, newEmail);
				}
			}
			account.setMailConfig(mailConfig);
			accountService.save(account);
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/accountmail/friendsVisible", method = RequestMethod.POST)
	public ResponseEntity<String> setFriendsVisible(
			HttpServletRequest request,
			@RequestParam("friendsVisible") String friendsVisible) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
			AccountMailConfig mailConfig = account.getMailConfig();
			if (mailConfig == null) {
				mailConfig = new AccountMailConfig();
			}
			mailConfig.setFriendsVisible(friendsVisible.equals("true") ? true
					: false);
			account.setMailConfig(mailConfig);
			accountService.save(account);
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/accountmail/tag", method = RequestMethod.POST)
	public ResponseEntity<String> setInTags(
			HttpServletRequest request,
			@RequestParam("tag") String tag) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
			AccountMailConfig mailConfig = account.getMailConfig();
			if (mailConfig == null) {
				mailConfig = new AccountMailConfig();
			}
			List<String> list = mailConfig.getInTags();
			if (list == null) {
				list = new ArrayList<String>();
			}
			list.add(tag);
			mailConfig.setInTags(list);
			account.setMailConfig(mailConfig);
			accountService.save(account);
		}
		return buildJSONResult("result", "success");
	}

	@RequestMapping(value = "/account/user/thirdPartyProfile", method = RequestMethod.POST)
	public ResponseEntity<String> setThirdPartyProfile(
			HttpServletRequest request,
			@RequestParam("vvalue") String vvalue) {
		int result = 0;
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			result = thirdPartyRpcService.setSyncStatus(
					authorization, vvalue);
		}
		return buildJSONResult("result", Integer.toString(result));
	}

	@RequestMapping(value = "/account/user/unBindWeibo")
	@ResponseBody
	public String unBindWeibo(
			HttpServletRequest request) {
		int result = 0; // error
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			result = thirdPartyRpcService.unbind(authorization);
		}
		return Integer.toString(result);
	}

	@RequestMapping(value = "/account/user/gravatar", method = RequestMethod.GET)
	public String userGravatar(HttpServletRequest request, Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		if(!tokenUtil.isGuestUser(authorization)) {
			Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
			model.addAttribute("account", account);
		}
		return "user/gravatar";
	}

	@RequestMapping(value = "/account/user/preview", method = RequestMethod.GET)
	public String userPreview(HttpServletRequest request) {
		String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil.getWebTokenAuthorization(request));
		return "redirect:/" + accountId;
	}
}
