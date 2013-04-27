package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.twocoffee.common.BaseController;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.User;
import me.twocoffee.entity.WeiXinPublic;
import me.twocoffee.entity.LoginToken.DisplayType;
import me.twocoffee.entity.User.LoginType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.WeiXinPublicService;
import me.twocoffee.service.rpc.AccountRpcService;
import me.twocoffee.service.rpc.AccountWithToken;
import me.twocoffee.service.rpc.ContentRpcService;
import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicConstant.BindMessage;

@Controller
@RequestMapping("/weixin")
public class WeiXinPublicController extends BaseController {
	@Autowired
    private AccountRpcService accountRpcService;
	@Autowired
    private  AccountService accountService = null;
	@Autowired
	private WeiXinPublicService weiXinPublicService;
	@Autowired
    private ContentRpcService contentRpcService;
	private static String resultFiled = "result";
	
	@RequestMapping(value="/{id}")
	public String guideWeiXin(Model model, @PathVariable String id) {
		if(StringUtils.isBlank(id)) {
			return "redirect:/home";
		}
		model.addAttribute("id", id);
		if(weiXinPublicService.isBindWeiXinPublicByWeiXinId(id)) {
			WeiXinPublic wp = weiXinPublicService.getWeiXinPublicByWeiXinId(id);
			String accountId = null;
			String accountName = null;
			if(wp != null) accountId = wp.getAccountId();
			Account account = accountService.getById(accountId);
			if(account != null)accountName = account.getName();
			model.addAttribute("name", accountName);
			return "weixin/authorize";
		}
		return "weixin/login";
	}
	
	@RequestMapping(value="/login/{id}",method=RequestMethod.GET)
	public String login(@PathVariable String id, Model model) {
		model.addAttribute("id", id);
		return "weixin/login";
	}
	
	@RequestMapping(value="/login/{id}",method=RequestMethod.POST)
	public ResponseEntity<String> login(@PathVariable String id,String loginName, String password, Model model) {
		AccountWithToken accountWithToken = accountRpcService.auth(loginName,password, null);
		this.bindWeiXinPublic(accountWithToken, id, model);
		String message = (String)model.asMap().get(resultFiled);
		return buildJSONResult("url","/weixin/bind/"+message);
	}
	
	
	@RequestMapping(value="/register/{id}",method=RequestMethod.GET)
	public String register(@PathVariable String id, Model model) {
		model.addAttribute("id", id);
		return "weixin/register";
	}
	
	@RequestMapping(value="/register/{id}",method=RequestMethod.POST)
	public ResponseEntity<String> register(@PathVariable String id,String email, String password, String name, Model model) {
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		if(email == null)email ="";
		user.setLoginName(email);
		
		if(email.indexOf("@") > -1) {
			user.setLoginType(LoginType.Email);
			user.setEmail(email);
		}
		else {
			user.setMobile(email);
			user.setLoginType(LoginType.Mobile);
		}
		AccountWithToken accountWithToken = null;
		try {
			accountWithToken = accountRpcService.register(user, null);
		} catch (Exception e) {
		}
		this.bindWeiXinPublic(accountWithToken, id, model);
		String message = (String)model.asMap().get(resultFiled);
		return buildJSONResult("url","/weixin/bind/"+message);
	}
	
	@RequestMapping(value = "/thirdparty/{thirdparty}/{id}", method = RequestMethod.GET)
	public String loginByThirdParty(@PathVariable String id, @PathVariable String thirdparty, String reffer) {
		String location = this.getThirdPartyAuthLocation(thirdparty, id);
		return "redirect:" + location;
	}
	
	@RequestMapping(value = "/thirdparty/callback/{id}/{accountId}", method = RequestMethod.GET)
	public String loginByThirdPartyCallback(@PathVariable String id,@PathVariable String accountId,Model model) {
		return this.bindWeiXinPublicResult(accountId, id, model);
	}
	
	@RequestMapping(value = "/content/item/{id}/{repositoryId}", method = RequestMethod.GET)
	public String getItemByRepositoryId(HttpServletRequest request,@PathVariable String id,
			                            @PathVariable String repositoryId, Model model) {
		ResponseContent content = contentRpcService.getResponseContentByRepositoryId(repositoryId);
		if(content != null) {
			model.addAttribute("item", content);
		}
		String agent = request.getHeader("User-Agent");
		String userAgentType = getUserAgentType(agent);
		model.addAttribute("client", userAgentType);
		return "ocean/third_phone";
	}
	
	@RequestMapping(value = "/content/list/{id}", method = RequestMethod.GET)
	public String getContentList(HttpServletRequest request,@PathVariable String id,Model model) {
		WeiXinPublic wxp = weiXinPublicService.getWeiXinPublicByWeiXinId(id);
		String accountId = wxp ==null ? "" : wxp.getAccountId();
		model.addAttribute("accountId", accountId);
		model.addAttribute("id", id);
		return "weixin/list";
	}
	
	@RequestMapping(value = "/bind/{message}", method = RequestMethod.GET)
	public String bindMessage(@PathVariable String message,Model model) {
		model.addAttribute(resultFiled, message);
		return "weixin/bindmessage";
	}
	
	@RequestMapping(value = "/agreement", method = RequestMethod.GET)
	public String agreement() {
		return "weixin/agreement";
	}
	
	private String bindWeiXinPublic(AccountWithToken accountWithToken, String id, Model model) {
		String accountId = null;
		if(accountWithToken != null && accountWithToken.getAccount()!=null)accountId = accountWithToken.getAccount().getId();
		return this.bindWeiXinPublicResult(accountId, id, model);
	}
	
	private String bindWeiXinPublicResult(String accountId,String id,Model model) {
		String message = BindMessage.failure.toString();
		try {
			if(StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(id)) {
				weiXinPublicService.bindWeiXinPublic(accountId, id);
				message = BindMessage.success.toString();
			}
		} catch (Exception e) {
		}
		model.addAttribute(resultFiled, message);
		return "weixin/bindmessage";
	}
	
	private String getThirdPartyAuthLocation(String thirdPartyType,String weixinId) {
		StringBuffer state = new StringBuffer();
		state.append("consumer="+DisplayType.Mobile);
		state.append(";isLogin=true");
		state.append(";weixinId="+weixinId);
		state.append(";reffer="+WeiXinPublicConstant.WEIXIN_THIRDPARTY_CALLBACK_URL_PRE+weixinId+"/");
		return accountService.getAuthLocation(thirdPartyType, state.toString(), DisplayType.Mobile);
	}
}
