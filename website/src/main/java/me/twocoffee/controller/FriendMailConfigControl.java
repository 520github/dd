/**
 * 
 */
package me.twocoffee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.twocoffee.common.BaseController;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendMailConfigService;
import me.twocoffee.service.entity.FriendMail;

/**
 * 白名单管理控制器
 * 
 * @author xuehui.miao
 *
 */
@Controller
public class FriendMailConfigControl extends BaseController {
	@Autowired
	private FriendMailConfigService friendMailConfigService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@RequestMapping("/account/user/friend_mail_config")
	public String findFriendMail(HttpServletRequest request, Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
		
		List<FriendMail> friendMailList = 
			friendMailConfigService.getFriendMailList(accountId);
		
		model.addAttribute("friendMailList", friendMailList);
		return "/user/friend_mail_config";
	}
	
	@RequestMapping("/account/user/friend_mail_config_do")
	public String configFriendMail(String friendId, boolean isBlocked, HttpServletRequest request, Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
		
		if(friendId != null && friendId.trim().length() > 0) {
			FriendMailConfig  friendMailConfig = friendMailConfigService.getFriendMailConfig(accountId, friendId);
			if(friendMailConfig != null) {
				friendMailConfig.setBlocked(isBlocked);
				friendMailConfigService.updateFriendMailConfig(friendMailConfig);
			}
		}
		
		List<FriendMail> friendMailList = 
			friendMailConfigService.getFriendMailList(accountId);
		
		model.addAttribute("friendMailList", friendMailList);
		return "/user/friend_mail_config";
	}
}
