package me.twocoffee.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.LoginToken.DisplayType;
import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.entity.Token;
import me.twocoffee.entity.User;
import me.twocoffee.entity.User.LoginType;
import me.twocoffee.entity.WebToken.UserType;
import me.twocoffee.entity.UserAgentLog;
import me.twocoffee.exception.RegisterFaildException;
import me.twocoffee.exception.TooManyVerifyRequestException;
import me.twocoffee.exception.UpdateAccountFailedException;
import me.twocoffee.rest.entity.AccountPerfictInfo;
import me.twocoffee.rest.entity.AccountUpdateInfo;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.rest.entity.PasswordInfo;
import me.twocoffee.rest.utils.Validator;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.ConfigurationService;
import me.twocoffee.service.EventService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.event.BindAccountEvent;
import me.twocoffee.service.event.SyncGuestRepositoryEvent;
import me.twocoffee.service.event.ThirdPartyUserAuthorizationEvent;
import me.twocoffee.service.event.UnbindAccountEvent;
import me.twocoffee.service.event.message.AuthenticationSuccessEvent;
import me.twocoffee.service.rpc.AccountRpcService;
import me.twocoffee.service.rpc.AccountWithToken;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import me.twocoffee.service.thirdparty.ThirdpartyService;
import me.twocoffee.service.weixin.WeiXinPublicConstant;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("account")
public class UserController extends BaseController {

    enum ErrorType {
	Cancel, Timeout, Error, Forbidden
    }

    enum ResultType {
	Success, Cancel, Error
    }

    private static final String REG_VERIFY_DUPLICATE_EMAIL = "reg.verify.duplicateEmail";

    private static final String REG_EMAIL_INVALID = "reg.email.invalid";

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(UserController.class);

    private static final String MESSAGE_ERROR_EMPTY = "login.error.empty";
    private static final String MESSAGE_ERROR_INVALID = "login.error.invalid";
    private static final String MESSAGE_ERROR_SERVER = "login.error.server";
    private static final String MESSAGE_ERROR_THIRD = "login.error.third.server";
    private static final String MESSAGE_ERROR_REG_VERIFY_EMAILDUPLICATE = "reg.verfiy.duplicateEmail";

    private static final String MESSAGE_SERVER_INTERNAL_ERROR = "serverInternalError";
    private static final String MESSAGE_SEND_MOBILE_SUCCESS = "reg.sendmoblie.success";
    private static final String MESSAGE_ERROR_MOBILE_VERIFY = "reg.sendmoblie.error.invildcode";

    /**
     * 存放邮件服务器的列表 Map<suffix,address> 如gmail.com对于https://mail.google.com
     */
    private static Map<String, String> mailServers = new HashMap<String, String>();
    @Autowired
    private final AccountService service = null;

    @Autowired
    private AccountRpcService accountService;

    @Autowired
    private AuthTokenService authTokenService = null;

    @Autowired
    protected FriendService friendService;

    @Autowired
    private ThirdPartyRpcService thirdPartyRpcService;

    @Autowired
    private ThirdpartyService thirdpartyService;

    @Autowired
    private EventService eventService;

    private static final String SHOWFRIENDSVIEWFLAG_ALWAYS = "always";

    @Autowired
    private ConfigurationService configurationService;

    public UserController() {
	mailServers.put("gmail.com", "https://mail.google.com");
	mailServers.put("babeeta.com", "https://mail.babeeta.com/owa");
    }

    private String getResetAuthorization(String authorization) {
	if (StringUtils.isBlank(authorization)) {
	    return authorization;
	}
	if (authorization.indexOf(TokenUtil.CLIENT_ID) > -1) {
	    if (authorization.indexOf(TokenUtil.CLIENT_ID + " ") == -1) {
		authorization = TokenUtil.CLIENT_ID
			+ " "
			+ authorization.substring(
				authorization.indexOf(TokenUtil.CLIENT_ID)
					+ TokenUtil.CLIENT_ID.length());
	    }
	}
	else if (authorization.indexOf(TokenUtil.WEB_TOKEN) > -1) {
	    if (authorization.indexOf(TokenUtil.WEB_TOKEN + " ") == -1) {
		authorization = TokenUtil.WEB_TOKEN
			+ " "
			+ authorization.substring(
				authorization.indexOf(TokenUtil.WEB_TOKEN)
					+ TokenUtil.WEB_TOKEN.length());
	    }
	}
	else if (authorization.indexOf(TokenUtil.AUTH_TOKEN) > -1) {
	    if (authorization.indexOf(TokenUtil.AUTH_TOKEN + " ") == -1) {
		authorization = TokenUtil.AUTH_TOKEN
			+ " "
			+ authorization.substring(
				authorization.indexOf(TokenUtil.AUTH_TOKEN)
					+ TokenUtil.AUTH_TOKEN.length());
	    }
	}

	return authorization;
    }

    private String getClientAuthorization(HttpServletRequest request,
	    String authorization) {
	if (StringUtils.isBlank(authorization)) {
	    authorization = request.getHeader("Authorization");
	}
	if (StringUtils.isBlank(authorization)) {
	    return null;
	}
	if (authorization.indexOf(TokenUtil.CLIENT_ID) == -1) {
	    authorization = TokenUtil.AUTH_TOKEN + authorization;
	}
	else if (authorization.indexOf(TokenUtil.CLIENT_ID + " ") > -1) {
	    authorization = authorization.replaceAll(" ", "");
	}
	return authorization;
    }

    private String doCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state,
	    ThirdPartyType type) {

	String userAgent = request.getHeader("User-Agent");
	Map<String, String> states = parseState(state);

	if ("false".equals(states.get("isLogin"))) {
	    return doBindCallback(request.getQueryString(), type, states);
	}
	
	ThirdPartyProfile profile = null;
	try {
	    profile = thirdpartyService.doAuthCallback(type,
		    request.getQueryString());

	} catch (Exception e) {
	    LOGGER.error("auth error!", e);
	    return showResult(ResultType.Error, "", states.get("consumer"), "");
	}

	if (profile == null) {
	    return showResult(ResultType.Cancel, "", states.get("consumer"), "");
	}
	Account account = thirdpartyService.getLoginAccountByThirdParty(type,
		profile.getUserId());

	if (account != null) {
	    return doLoginAgain(response, type, states, profile, account);

	}
	account = new Account();
	account.setAccountType(AccountType.valueOf(type.toString()));
	account.setCreateDate(new Date());
	account.setLoginName(type.toString() + "." + profile.getUserId());
	account.setAvatar(profile.getAvatar());
	account.setName(profile.getNickName());
	account.addStat(Status.ProfileResolved);
	account.addStat(Status.ShowThirdpartyFriends);
	AccountMailConfig config = new AccountMailConfig();
	account.setMailConfig(config);

	try {
	    service.saveWithUniqueName(account);
	} catch (Exception e) {
	    LOGGER.error("can not save account when thirdparty login!", e);
	    return showResult(ResultType.Error, "", states.get("consumer"), "");
	}
	profile.setAccountId(account.getId());
	profile.setCreateDate(new Date());
	profile.setAccountType(type);
	profile.setId(new ObjectId().toString());
	profile.setLogin(true);
	thirdpartyService.save(profile);

	// support client old version
	String authorization = this.getResetAuthorization(states.get("token"));
	String authToken = null;
	if (authorization == null || !TokenUtil.isEmptyAuthToken(authorization)) {
	    AuthToken token = authTokenService.createToken(account.getId());
	    if (token != null)
		authToken = token.getId();
	}

	AuthUtil.writeAuthToken(response, authToken, false);
	AuthUtil.writeMessageToken(response, "", false);
	postEvent(type, profile, account);
	eventService.send(new BindAccountEvent(this, authorization, account
		.getId(), UserType.LoginThird));
	eventService.send(new SyncGuestRepositoryEvent(this, authorization,
		account.getId()));
	UserAgentLog log = new UserAgentLog();
	log.setId(new ObjectId().toString());
	log.setAccount(account.getId());
	log.setCreateDate(new Date());
	log.setUserAgent(userAgent);
	service.saveUserAgent(log);
	
	this.resetWeixinReffer(states, account.getId());
	return showResult(ResultType.Success, authToken,
		states.get("consumer"), states.get("reffer"));
    }

    private void postEvent(ThirdPartyType type, ThirdPartyProfile profile,
	    Account account) {

	eventService.send(new AuthenticationSuccessEvent(this, account));
	ThirdPartyUserAuthorizationEvent authEvent = new ThirdPartyUserAuthorizationEvent(
		profile, true);

	eventService.send(authEvent);
    }
    
    /**
     * 微信公共账号重新设置跳转url
     * @param states
     * @param accountId
     */
    private void resetWeixinReffer(Map<String,String> states,String accountId) {
    	if(states == null || states.size()<1) {
    		return;
    	}
    	if(!states.containsKey("reffer")) {
    		return;
    	}
    	String reffer = StringUtils.trimToEmpty(states.get("reffer"));
    	if(reffer.indexOf(WeiXinPublicConstant.WEIXIN_THIRDPARTY_CALLBACK_URL_PRE)>-1) {
    		reffer = reffer + accountId;
    		states.put("reffer", reffer);
    	}
    }

    /**
     * @param response
     * @param type
     * @param states
     * @param profile
     * @param account
     * @return
     */
    private String doLoginAgain(HttpServletResponse response,
	    ThirdPartyType type, Map<String, String> states,
	    ThirdPartyProfile profile, Account account) {

	if (!SHOWFRIENDSVIEWFLAG_ALWAYS.equals(configurationService
		.getShowFriendsViewFlag())) {

	    account.removeStatus(Status.ShowThirdpartyFriends);
	    service.save(account);
	}

	ThirdPartyProfile newprofile = thirdpartyService.getLoginThirdParty(
		type, profile.getUserId());

	if (newprofile == null) {
	    LOGGER.error(
		    "account {} ThirdParty {} login again, can not find ThirdPartyProfile",
		    new Object[] { account.getId(), type });

	} else {
	    newprofile.setBind(true);
	    newprofile.setSyncContent(true);
	    newprofile.setAvatar(profile.getAvatar());
	    newprofile.setExpiredDate(profile.getExpiredDate());
	    newprofile.setNickName(profile.getNickName());
	    newprofile.setProfile(profile.getProfile());
	    newprofile.setRefreshToken(profile.getRefreshToken());
	    newprofile.setToken(profile.getToken());
	    newprofile.setTokenSecret(profile.getTokenSecret());
	    newprofile.setAccountType(type);
	    thirdpartyService.save(newprofile);
	}
	// support client old version
	String authorization = this.getResetAuthorization(states.get("token"));
	String authToken = null;
	if (authorization == null || !TokenUtil.isEmptyAuthToken(authorization)) {
	    AuthToken token = authTokenService.createToken(account.getId());
	    if (token != null)
		authToken = token.getId();
	}
	AuthUtil.writeAuthToken(response, authToken, false);
	AuthUtil.writeMessageToken(response, "", false);
	ThirdPartyUserAuthorizationEvent event = new ThirdPartyUserAuthorizationEvent(
		newprofile, false);

	eventService.send(new AuthenticationSuccessEvent(this, account));
	eventService.send(event);
	eventService.send(new BindAccountEvent(this, authorization, account
		.getId(), UserType.LoginThird));
	eventService.send(new SyncGuestRepositoryEvent(this, authorization,
		account.getId()));
	
	this.resetWeixinReffer(states, account.getId());
	return showResult(ResultType.Success, authToken,
		states.get("consumer"), states.get("reffer"));

    }

    /**
     * @param request
     * @param type
     * @param states
     * @return
     */
    private String doBindCallback(String queryString, ThirdPartyType type,
	    Map<String, String> states) {

	ThirdPartyProfile profile = null;
	String authorization = this.getResetAuthorization(states.get("token"));
	int code = tokenUtil
		.getGuestAuthorizationErrorCode(authorization, true);
	if (code > 200) {
	    return showAuthResult(ResultType.Error, "", states.get("consumer"),
		    ErrorType.Forbidden, "");
	}
	String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
	try {
	    profile = thirdpartyService.doAuthCallback(type, queryString);
	} catch (Exception e) {
	    LOGGER.error("auth error!", e);
	    return showAuthResult(ResultType.Error, "", states.get("consumer"),
		    null, "");
	}

	if (profile == null) {
	    return showAuthResult(ResultType.Cancel, "",
		    states.get("consumer"), null, "");
	}
	ThirdPartyProfile newprofile = thirdpartyService.getByAccountId(
		accountId, type);

	if (newprofile != null) {

	    if (!accountId.equals(newprofile.getAccountId())) {
		return showAuthResult(ResultType.Error, "",
			states.get("consumer"), ErrorType.Forbidden, "");

	    }

	    if (!newprofile.getUserId().equals(profile.getUserId())) {
		ThirdPartyContentSynchronizeLog contentSynchronizeLog = new ThirdPartyContentSynchronizeLog();
		contentSynchronizeLog.setId(new ObjectId().toString());
		contentSynchronizeLog.setAccountId(accountId);
		contentSynchronizeLog.setSysnDate(newprofile.getSyncDate());
		contentSynchronizeLog.setThirdPartyType(newprofile
			.getAccountType());
		contentSynchronizeLog.setUid(newprofile.getUserId());
		thirdpartyService
			.saveContentSynchronizeLog(contentSynchronizeLog);
		thirdpartyService.removeProfile(newprofile);
		thirdpartyService.removeContact(profile.getAccountType(),
			accountId);

		profile.setBind(true);
		profile.setSyncContent(true);
		profile.setAccountId(accountId);
		profile.setCreateDate(new Date());
		profile.setAccountType(type);
		profile.setId(new ObjectId().toString());
		thirdpartyService.save(profile);

	    } else {
		newprofile.setBind(true);
		newprofile.setSyncContent(true);
		newprofile.setAvatar(profile.getAvatar());
		newprofile.setExpiredDate(profile.getExpiredDate());
		newprofile.setNickName(profile.getNickName());
		newprofile.setRefreshToken(profile.getRefreshToken());
		newprofile.setToken(profile.getToken());
		newprofile.setTokenSecret(profile.getTokenSecret());
		thirdpartyService.save(newprofile);
	    }
	    Account account = service.getById(accountId);
	    account.addStat(Status.ShowThirdpartyFriends);
	    service.save(account);
	    ThirdPartyUserAuthorizationEvent event = new ThirdPartyUserAuthorizationEvent(
		    newprofile, false);

	    eventService.send(event);

	} else {
	    profile.setBind(true);
	    profile.setSyncContent(true);
	    profile.setAccountId(accountId);
	    profile.setCreateDate(new Date());
	    profile.setAccountType(type);
	    profile.setId(new ObjectId().toString());
	    thirdpartyService.save(profile);
	    ThirdPartyUserAuthorizationEvent event = new ThirdPartyUserAuthorizationEvent(
		    profile, true);

	    eventService.send(event);
	}
	return showAuthResult(ResultType.Success, "", states.get("consumer"),
		null, states.get("reffer"));
    }

    /**
     * @param state
     * @return
     */
    private Map<String, String> parseState(String state) {
	Map<String, String> states = new HashMap<String, String>();

	if (StringUtils.isNotBlank(state)) {

	    try {
		state = URLDecoder.decode(state, "utf-8");
		String params[] = state.split(";");

		for (String p : params) {
		    states.put(p.split("=")[0], p.split("=")[1]);
		}

	    } catch (Exception e1) {
		throw new RuntimeException("auth error!");
	    }

	    if (!DisplayType.Mobile.toString().equals(states.get("consumer"))
		    && !DisplayType.Web.toString().equals(
			    states.get("consumer"))) {

		throw new RuntimeException("auth error!");
	    }
	}
	return states;
    }

    private ResponseEntity<String> doRegister(String registerType,
	    String loginName, String password, String name, String inviteCode,
	    String mobileToken, String emailToken, String duoduoEmail,
	    HttpServletResponse response, HttpServletRequest request) {

	User user = new User();
	user.setName(name);
	user.setPassword(password);
	user.setInviteCode(inviteCode);
	user.setLoginName(loginName);
	user.setCoffeeMail(duoduoEmail);
	user.setVerify(StringUtils.isBlank(emailToken) ? mobileToken
		: emailToken);

	if ("mobile".equals(registerType)) {
	    user.setMobile(loginName);
	    user.setLoginType(LoginType.Mobile);

	} else {
	    user.setEmail(loginName);
	    user.setLoginType(LoginType.Email);
	}
	AccountWithToken accountWithToken = null;

	try {
	    accountWithToken = accountService.register(user,
		    AuthUtil.getWebTokenAuthorization(request));
	    AuthUtil.writeAuthToken(response, accountWithToken.getAuthToken(),
		    false);

	    AuthUtil.writeMessageToken(response, "", false);

	} catch (RegisterFaildException e) {
	    String errorCode = MESSAGE_SERVER_INTERNAL_ERROR;

	    if (e.getFieldErrors() != null && !e.getFieldErrors().isEmpty()
		    && e.getFieldErrors().values().iterator().hasNext()) {

		errorCode = (String) e.getFieldErrors().values().iterator()
			.next();

	    }
	    return buildJSONResult("error", getMessage(errorCode));
	}
	return buildJSONResult("url", "/home/");
    }

    private ResponseEntity<String> doUpdateAccount(String registerType,
	    String loginName, String password, String name, String invite,
	    String mobileToken, String emailToken, String token,
	    String duoduoEmail, HttpServletResponse response) {

	AccountPerfictInfo user = new AccountPerfictInfo();
	user.setLoginName(loginName);
	user.setPassword(new PasswordInfo("", password));
	user.setName(name);
	user.setEmail(loginName);
	user.setInviteCode(invite);
	// user.setCoffeeMail(duoduoEmail);

	long res = accountService.perfectInformation(user, token);

	if (res == 204) {
	    return buildJSONResult("url", "/");
	}
	return buildJSONResult("msg", getMessage(MESSAGE_SERVER_INTERNAL_ERROR));
    }

    private String getAuthLoaction(DisplayType web, String consumerStr) {
	return getAuthLoaction(web, consumerStr, true, "");
    }

    private String getAuthLoaction(DisplayType mobile, String consumerStr,
	    boolean isLogin, String authToken) {

	return getAuthLoaction(mobile, consumerStr, isLogin, authToken, "");
    }

    private String getAuthLoaction(DisplayType mobile, String consumerStr,
	    boolean isLogin, String authToken, String reffer) {

	if (isLogin) {
	    String state = "consumer=" + mobile;

	    if (StringUtils.isNotBlank(reffer)) {
		state += ";reffer=" + reffer;
	    }
	    if (StringUtils.isNotBlank(authToken)) {
		state += ";token=" + authToken;
	    }
	    return service.getAuthLocation(consumerStr, state, mobile);

	} else {
	    StringBuffer state = new StringBuffer("consumer=").append(mobile)
		    .append(";isLogin=").append(isLogin).append(";token=")
		    .append(authToken);

	    if (StringUtils.isNotBlank(reffer)) {
		state.append(";reffer=").append(reffer);
	    }

	    return service.getAuthLocation(consumerStr, state.toString(),
		    mobile);

	}
    }

    private String getAuthLoaction(DisplayType web, String consumerStr,
	    String reffer) {

	return getAuthLoaction(web, consumerStr, true, "", reffer);
    }

    private String getDDEmailName(String registerType, String loginName) {
	String emailName = "";

	if ("email".equals(registerType)) {
	    emailName = loginName.substring(0, loginName.indexOf("@"));

	} else {
	    emailName = loginName.substring(loginName.length() - 6);
	}
	return emailName;
    }

    private String getUniqName(String dm) {

	if (service.countByDuoduoEmail(dm + "@" + SystemConstant.mailSuffix) > 0) {
	    dm = postfix(dm);
	}
	return dm;
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

    private String postfix(String dm) {
	Random random = new Random();
	StringBuilder builder = new StringBuilder(dm);
	builder.append("_");

	for (int i = 0; i < 4; i++) {
	    builder.append(random.nextInt(10));
	}
	return builder.toString();
    }

    private String showAuthResult(ResultType rtype, String token,
	    String display, ErrorType etype, String reffer) {

	if (etype == null) {
	    etype = ErrorType.Timeout;
	}
	StringBuilder builder = new StringBuilder("redirect:" + "http://"
		+ SystemConstant.domainName + "/account/authority-");

	switch (rtype) {
	case Success:
	    builder.append("success");

	    if (StringUtils.isNotBlank(display)
		    && DisplayType.Mobile.toString().equals(display)) {

		builder.append("?display=").append(display);

	    } else {
		builder.append("?reffer=").append(reffer);
	    }
	    break;

	case Cancel:
	    return "redirect:" + "http://" + SystemConstant.domainName
		    + "/account/login-error?cause="
		    + ErrorType.Cancel.toString();

	case Error:
	default:
	    return "redirect:" + "http://" + SystemConstant.domainName
		    + "/account/login-error?cause=" + etype.toString();
	}
	return builder.toString();
    }

    private String showResult(ResultType type, String token, String display,
	    String reffer) {
    reffer = StringUtils.trimToEmpty(reffer);
	if(reffer.indexOf(WeiXinPublicConstant.WEIXIN_THIRDPARTY_CALLBACK_URL_PRE)>-1) {
		return "redirect:" + reffer;
	}
	StringBuilder builder = new StringBuilder("redirect:" + "http://"
		+ SystemConstant.domainName + "/account/login-");

	switch (type) {
	case Success:
	    builder.append("success?authToken=").append(
		    token == null ? "" : token);

	    if (StringUtils.isNotBlank(display)
		    && DisplayType.Mobile.toString().equals(display)) {

		builder.append("&display=").append(display);
	    }

	    if (StringUtils.isNotBlank(reffer)) {
		builder.append("&reffer=").append(reffer);
	    }
	    break;

	case Cancel:
	    builder.append("error?cause=").append(ErrorType.Cancel.toString());
	    break;

	case Error:
	default:
	    builder.append("error?cause=").append(ErrorType.Timeout.toString());
	    break;
	}
	return builder.toString();
    }

    /**
     * 向指定邮箱发送验证邮件
     * 
     * @param request
     * @param email
     * @return
     */
    @RequestMapping(value = "default/applyVerify/email", method = RequestMethod.POST)
    public ResponseEntity<String> applyVerifyEmail(HttpServletRequest request,
	    String email, String invite, String u) {

	Map<String, String> result = new HashMap<String, String>();

	// 发送验证邮件
	try {
	    accountService.applyVerifyEmail(email, u, invite);

	} catch (TooManyVerifyRequestException e) {
	    result.put(
		    "msg",
		    getMessage(
			    "tooManyVerifyRequest",
			    new Object[] { e.getVerifyTimesLimitCycleInHours(),
				    e.getMaxVerifyTimes(),
				    e.getMinVerifyIntervalInMinutes() }));

	    return buildJSONResult(result);

	} catch (Exception e) {
	    result.put("msg", getMessage(MESSAGE_SERVER_INTERNAL_ERROR));

	    return buildJSONResult(result);
	}
	String suffix = email.substring(email.indexOf("@") + 1, email.length());

	String mailServer = mailServers.get(suffix) == null ? "http://mail."
		+ suffix : mailServers.get(suffix);

	result.put("mailServer", mailServer);
	return buildJSONResult(result);
    }

    @RequestMapping(value = "mobile/weibo/oauth/authority", method = RequestMethod.GET)
    public String authorityByMobileWeiboAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Weibo.toString(), false,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/tencent/oauth/authority", method = RequestMethod.GET)
    public String authorityByMobileTencentAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Tencent.toString(), false,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/renren/oauth/authority", method = RequestMethod.GET)
    public String authorityByMobileRenrenAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Renren.toString(), false,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "weibo/oauth/authority", method = RequestMethod.GET)
    public String authorityByWeiboAuth(HttpServletRequest request,
	    String authToken, String reffer) {

	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Weibo.toString(), false,
		AuthUtil.getWebTokenAuthorization(request), reffer);

	return "redirect:" + location;
    }

    @RequestMapping(value = "tencent/oauth/authority", method = RequestMethod.GET)
    public String authorityByTencentAuth(HttpServletRequest request,
	    String authToken, String reffer) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Tencent.toString(), false,
		AuthUtil.getWebTokenAuthorization(request),
		reffer);

	return "redirect:" + location;
    }

    @RequestMapping(value = "renren/oauth/authority", method = RequestMethod.GET)
    public String authorityByRenrenAuth(HttpServletRequest request,
	    String authToken, String reffer) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Renren.toString(), false,
		AuthUtil.getWebTokenAuthorization(request), reffer);

	return "redirect:" + location;
    }

    @RequestMapping("default/change")
    public String changeUser(HttpServletRequest request,
	    HttpServletResponse response, Model model) throws Exception {

	String token = AuthUtil.getWebTokenFromCookie(request);

	if (token != null) {
	    AuthUtil.deleteWebToken(response, token);
	}
	return "redirect:/default/login";
    }

    /**
     * 检查coffeemail是否被占用
     * 
     * @param coffeemail
     * @return
     */
    @RequestMapping(value = "default/coffeemail", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> checkCoffeemail(String coffeemail) {

	if (StringUtils.isBlank(coffeemail)
		|| !Validator.validateEmail(coffeemail)) {

	    return buildJSONResult("error", REG_EMAIL_INVALID);
	}
	Account account = service.getByDuoduoEmail(coffeemail);

	if (account == null) {
	    return buildJSONResult("success", "success");
	}
	return buildJSONResult("error", REG_VERIFY_DUPLICATE_EMAIL);
    }

    @RequestMapping(value = "facebook/connection/callback", method = RequestMethod.GET)
    // TODO:SNS ThirdPartyType
    public String doFacebookCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state,
		ThirdPartyType.Facebook);
    }

    @RequestMapping(value = "google/oauth/callback", method = RequestMethod.GET)
    public String doGoogleCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state, ThirdPartyType.Gmail);
    }

    @RequestMapping(value = "qq/oauth/callback", method = RequestMethod.GET)
    public String doTencentCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state, ThirdPartyType.QQ);
    }

    @RequestMapping(value = "tencent/oauth/callback", method = RequestMethod.GET)
    public String doTencentWeiboCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state,
		ThirdPartyType.Tencent);
    }

    @RequestMapping(value = "twitter/oauth/callback", method = RequestMethod.GET)
    public String doTwitterCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state,
		ThirdPartyType.Twitter);
    }

    @RequestMapping(value = "weibo/oauth/callback", method = RequestMethod.GET)
    public String doWeiboCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state, ThirdPartyType.Weibo);
    }

    @RequestMapping(value = "renren/oauth/callback", method = RequestMethod.GET)
    public String doRenrenCallback(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state,
		ThirdPartyType.Renren);
    }

    /**
     * 在web页面上给微博好友分享
     * 
     * @param request
     * @param response
     * @param model
     * @param state
     * @return
     */
    @RequestMapping(value = "weibo/postweibo", method = RequestMethod.GET)
    public String doWeiboPostWeibo(HttpServletRequest request,
	    HttpServletResponse response, Model model, String state) {

	return doCallback(request, response, model, state, ThirdPartyType.Weibo);
    }

    public AuthTokenService getAuthTokenService() {
	return authTokenService;
    }

    @RequestMapping(value = "default", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getDefault(HttpServletRequest request,
	    HttpServletResponse response, Model model) {
	String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil
		.getWebTokenAuthorization(request));
	if (!StringUtils.isBlank(accountId)) {
	    Account account = service.getById(accountId);

	    if (account != null) {
		AccountVO vo = AuthUtil.covertToAccountVO(account);

		if (vo != null) {
		    response.setStatus(200);
		    return buildJSONResult(vo);
		}
	    }
	}
	response.setStatus(403);
	return null;
    }

    @RequestMapping("list")
    public String list(HttpServletRequest request, Model model) {
	List<Account> users = service.findAll();
	model.addAttribute("users", users);
	return "user/list";
    }

    @RequestMapping(value = "default/login", method = RequestMethod.GET)
    public String login(String reffer, Model model) {

	if (StringUtils.isNotBlank(reffer)) {
	    model.addAttribute("reffer", reffer);
	}
	return "user/login";
    }

    @RequestMapping(value = "default/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(String loginName, String password,
	    HttpServletRequest request, HttpServletResponse response,
	    String autologin, String reffer) throws Exception {

	if (loginName == null || loginName.equals("") || password == null
		|| password.equals("")) {

	    return buildJSONResult("error", getMessage(MESSAGE_ERROR_EMPTY));
	}
	String webToken = AuthUtil.getWebTokenAuthorization(request);
	AccountWithToken accountWithToken = accountService.auth(loginName,
		password, webToken);

	if (accountWithToken == null) {
	    return buildJSONResult("error", getMessage(MESSAGE_ERROR_INVALID));

	} else {
	    AuthUtil.writeAuthToken(response, accountWithToken.getAuthToken(),
		    autologin == null ? false : true);

	    AuthUtil.writeMessageToken(response, "", autologin == null ? false
		    : true);

	    if (StringUtils.isNotBlank(reffer)) {
		return buildJSONResult("url", reffer);
	    }
	    return buildJSONResult("url", "/");
	}
    }

    @RequestMapping(value = "facebook/connection/", method = RequestMethod.GET)
    public String loginByFacebookConn(String state) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Facebook.toString());

	return "redirect:" + location;
    }

    @RequestMapping(value = "google/oauth/", method = RequestMethod.GET)
    public String loginByGoogleAuth() {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Gmail.toString());

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/facebook/connection/", method = RequestMethod.GET)
    public String loginByMobileFacebookConn(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Facebook.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/google/oauth/", method = RequestMethod.GET)
    public String loginByMobileGoogleAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Gmail.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/qq/oauth/", method = RequestMethod.GET)
    public String loginByMobileQQAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.QQ.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/tencent/oauth/", method = RequestMethod.GET)
    public String loginByMobileTencentAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Tencent.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/twitter/oauth/", method = RequestMethod.GET)
    public String loginByMobileTwitterAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Twitter.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/weibo/oauth/", method = RequestMethod.GET)
    public String loginByMobileWeiboAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Weibo.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "mobile/renren/oauth/", method = RequestMethod.GET)
    public String loginByMobileRenrenAuth(HttpServletRequest request,
	    String authToken) {
	String location = getAuthLoaction(DisplayType.Mobile,
		Account.AccountType.Renren.toString(), true,
		this.getClientAuthorization(request, authToken));

	return "redirect:" + location;
    }

    @RequestMapping(value = "qq/oauth/", method = RequestMethod.GET)
    public String loginByQQAuth(HttpServletRequest request) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.QQ.toString(), true,
		AuthUtil.getWebTokenAuthorization(request));

	return "redirect:" + location;
    }

    @RequestMapping(value = "tencent/oauth/", method = RequestMethod.GET)
    public String loginByTencentAuth(HttpServletRequest request) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Tencent.toString(), true,
		AuthUtil.getWebTokenAuthorization(request));

	return "redirect:" + location;
    }

    @RequestMapping(value = "twitter/oauth/", method = RequestMethod.GET)
    public String loginByTwitterAuth(HttpServletRequest request) {
	String location = getAuthLoaction(DisplayType.Web,
		Account.AccountType.Twitter.toString(), true,
		AuthUtil.getWebTokenAuthorization(request));

	return "redirect:" + location;
    }

    @RequestMapping(value = "weibo/oauth/", method = RequestMethod.GET)
    public String loginByWeiboAuth(HttpServletRequest request, String reffer) {
	String location = getAuthLoaction(DisplayType.Web,
		AccountType.Weibo.toString(), true,
		AuthUtil.getWebTokenAuthorization(request), reffer);

	return "redirect:" + location;
    }

    @RequestMapping(value = "renren/oauth/", method = RequestMethod.GET)
    public String loginByRenrenAuth(HttpServletRequest request, String reffer) {
	String location = getAuthLoaction(DisplayType.Web,
		AccountType.Renren.toString(), true,
		AuthUtil.getWebTokenAuthorization(request), reffer);

	return "redirect:" + location;
    }

    @RequestMapping("default/logout")
    public String logout(HttpServletRequest request,
	    HttpServletResponse response, Model model) throws Exception {
	String token = AuthUtil.getWebTokenFromCookie(request);
	if (token != null) {
	    AuthUtil.deleteWebToken(response, token);
	}
	return "redirect:/";
    }

    @RequestMapping(value = "third/profile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> perfectUserInformation(
	    HttpServletRequest request, HttpServletResponse response, User u) {

	AccountPerfictInfo user = new AccountPerfictInfo();

	String email = u.getEmail();
	if (email.indexOf("@") < 0) { // 没有@符号 视其为手机号
	    u.setMobile(email);
	    u.setEmail("");
	}
	user.setLoginName(email);
	// if (u.getEmail() != null && !"".equals(u.getEmail())) {
	// user.setLoginName(u.getEmail());
	// } else {
	// user.setLoginName(u.getMobile());
	// }
	user.setPassword(new PasswordInfo("", u.getPassword()));
	user.setName(u.getName());
	user.setEmail(u.getEmail());
	user.setInviteCode(u.getInviteCode());
	user.setMobile(u.getMobile());

	try {
	    long res = accountService.perfectInformation(user,
		    AuthUtil.getWebTokenAuthorization(request));

	    if (res == 204) {
		return buildJSONResult(
			"url",
			"/account/thirdPartyFriends/weibo?reffer="
				+ u.getReffer());

	    } else {
		return buildJSONResult("error",
			getMessage(MESSAGE_SERVER_INTERNAL_ERROR));
	    }

	} catch (UpdateAccountFailedException e) {
	    String errorCode = MESSAGE_SERVER_INTERNAL_ERROR;

	    if (e.getFieldErrors() != null && !e.getFieldErrors().isEmpty()
		    && e.getFieldErrors().values().iterator().hasNext()) {

		errorCode = e.getFieldErrors().values().iterator().next();

	    }
	    return buildJSONResult("error", getMessage(errorCode));
	}
    }

    @RequestMapping(value = "default/register", method = RequestMethod.POST)
    // @ResponseBody
    public ResponseEntity<String> register(String registerType, String mobile,
	    String email, String password, String name, String inviteCode,
	    String reffer, HttpServletRequest request,
	    HttpServletResponse response, Model model) {

	User user = new User();
	user.setName(name);
	user.setPassword(password);
	user.setInviteCode(inviteCode);

	if ("mobile".equals(registerType)) {
	    user.setMobile(mobile);
	    user.setLoginName(mobile);
	    user.setLoginType(LoginType.Mobile);

	} else {
	    user.setEmail(email);
	    user.setLoginName(email);
	    user.setLoginType(LoginType.Email);
	}
	AccountWithToken accountWithToken = null;

	try {
	    accountWithToken = accountService.register(user,
		    AuthUtil.getWebTokenAuthorization(request));
	    AuthUtil.writeAuthToken(response, accountWithToken.getAuthToken(),
		    false);

	    AuthUtil.writeMessageToken(response, "", false);

	} catch (RegisterFaildException e) {
	    String errorCode = MESSAGE_SERVER_INTERNAL_ERROR;

	    if (e.getFieldErrors() != null && !e.getFieldErrors().isEmpty()
		    && e.getFieldErrors().values().iterator().hasNext()) {

		errorCode = (String) e.getFieldErrors().values().iterator()
			.next();

	    }
	    return buildJSONResult("error", getMessage(errorCode));
	}

	if (StringUtils.isNotBlank(reffer)) {
	    return buildJSONResult("url", reffer);
	}
	return buildJSONResult("url", "/home");
    }

    /**
     * 处理注册第二步，设置密码、昵称、邀请码
     * 
     * @param password
     * @param name
     * @param inviteCode
     * @param request
     * @param response
     * @param model
     * @return
     */
    public ResponseEntity<String> register_old(String registerType,
	    String loginName, String password, String name, String inviteCode,
	    String mobileToken, String emailToken, String optype,
	    String duoduoEmail, HttpServletRequest request,
	    HttpServletResponse response, Model model) {

	if (!duoduoEmail.trim().endsWith("@" + SystemConstant.mailSuffix)) {
	    duoduoEmail = duoduoEmail.trim() + "@" + SystemConstant.mailSuffix;
	}

	if (StringUtils.isNotBlank(optype)) {
	    return doUpdateAccount(registerType, loginName, password, name,
		    inviteCode, mobileToken, emailToken, optype, duoduoEmail,
		    response);

	}
	return doRegister(registerType, loginName, password, name, inviteCode,
		mobileToken, emailToken, duoduoEmail, response, request);

    }

    /**
     * 注册首页获取手机验证码
     * 
     * @param request
     * @param mobile
     * @return
     */
    @RequestMapping(value = "default/sms", method = RequestMethod.GET)
    public ResponseEntity<String> sendSMS(HttpServletRequest request,
	    String mobile) {

	try {
	    accountService.applyVerifyMobile(mobile, "", "");

	} catch (TooManyVerifyRequestException e) {
	    return buildJSONResult(
		    "error",
		    getMessage(
			    "tooManyVerifyRequest",
			    new Object[] { e.getVerifyTimesLimitCycleInHours(),
				    e.getMaxVerifyTimes(),
				    e.getMinVerifyIntervalInMinutes() }));

	} catch (IllegalArgumentException e) {
	    return buildJSONResult("error",
		    getMessage(MESSAGE_ERROR_REG_VERIFY_EMAILDUPLICATE));

	} catch (Exception e) {
	    return buildJSONResult("error",
		    getMessage(MESSAGE_SERVER_INTERNAL_ERROR));

	}
	return buildJSONResult("msg", getMessage(MESSAGE_SEND_MOBILE_SUCCESS));
    }

    public void setAuthTokenService(AuthTokenService authTokenService) {
	this.authTokenService = authTokenService;
    }

    // @RequestMapping("third/success")
    // public String showThirdLogin(Model model, String cause, String authToken)
    // {
    //
    // if (StringUtils.isNotBlank(authToken)) {
    // model.addAttribute("authToken", authToken);
    // return "user/third_login_success";
    // }
    // model.addAttribute("cause", cause);
    // return "user/third_login_error";
    // }

    /**
     * 显示设置二级域名页面
     * 
     * @param request
     * @param response
     * @param model
     * @param accountType
     * @param nickName
     * @param dm
     * @param loginName
     * @return
     */
    @RequestMapping(value = "user/coffeemail", method = RequestMethod.GET)
    public String showCoffeemail(HttpServletRequest request,
	    HttpServletResponse response, Model model) {

	String dm = "";
	Account account = tokenUtil.getLoginUserAccontByAuthorization(AuthUtil
		.getWebTokenAuthorization(request));
	if (account == null) {
	    return "redirect:/home";
	}
	if (StringUtils.isNotBlank(account.getDuoduoEmail())) {
	    return "redirect:/home";
	}
	switch (account.getAccountType()) {
	case TwoCoffeeEmail:
	    dm = account.getLoginName().substring(0,
		    account.getLoginName().indexOf("@"));
	    break;
	case TwoCoffeeMobile:
	    dm = account.getLoginName().substring(
		    account.getLoginName().length() - 6);
	    break;
	default:
	    dm = account.getLoginName().substring(0,
		    account.getLoginName().indexOf("@"));

	    break;
	}
	dm = getUniqName(dm);
	model.addAttribute("dm", dm);
	return "user/coffeemail";
    }

    /**
     * 展示第三方帐号个人信息设置页面
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "third/profile", method = RequestMethod.GET)
    public String showProfile(HttpServletRequest request,
	    HttpServletResponse response, Model model) throws IOException {
	Account account = tokenUtil.getLoginUserAccontByAuthorization(AuthUtil
		.getWebTokenAuthorization(request));

	if (account != null) {
	    model.addAttribute("name", account.getName());
	    model.addAttribute("picurl", account.getAvatar());
	}
	return "user/thirdinput";
    }

    @RequestMapping(value = "default/reg", method = RequestMethod.GET)
    public String showReg(String loginName, String invite, String registerType,
	    String emailToken, String mobileToken, String optype,
	    HttpServletRequest request, HttpServletResponse response,
	    Model model) {

	model.addAttribute("loginName", loginName);
	model.addAttribute("invite", invite);
	model.addAttribute("registerType", registerType);
	model.addAttribute("emailToken", emailToken);
	model.addAttribute("mobileToken", mobileToken);
	model.addAttribute("optype", optype);
	model.addAttribute("ddEmail", getDDEmailName(registerType, loginName));
	return "user/reg";
    }

    /**
     * 显示注册页面
     * 
     * @return
     */
    @RequestMapping(value = "default/register", method = RequestMethod.GET)
    public String showRegisterView(Model model, String invite, String reffer) {
	model.addAttribute("invite", StringUtils.isBlank(invite) ? "" : invite);
	model.addAttribute("reffer", reffer);
	return "user/register";
    }

    @RequestMapping("invite/{inviteCode}")
    public String showRegWithInvite(HttpServletRequest request,
	    @PathVariable("inviteCode") String inviteCode, Model model) {

	return "redirect:/account/default/register/?invite=" + inviteCode;
    }

    @RequestMapping("authority-error")
    public String showThirdAuthError(Model model, HttpServletRequest request,
	    String cause) {

	String agent = request.getHeader("User-Agent");

	if (isMobile(agent)) {
	    model.addAttribute("cause", cause);
	    return "user/third_auth_error";

	} else {

	    if (ErrorType.Cancel.toString().equals(cause)) {
		model.addAttribute("result", "cancel");

	    } else {
		model.addAttribute("result", "error");
		model.addAttribute("error", getMessage(MESSAGE_ERROR_THIRD));
	    }
	    return "user/third_auth_web_result";
	}
    }

    @RequestMapping("authority-success")
    public String showThirdAuthSuccess(Model model, HttpServletRequest request,
	    String authToken, String display, String reffer) {

	String agent = request.getHeader("User-Agent");

	if (isMobile(agent)) {
	    model.addAttribute("authToken", authToken);
	    return "user/third_auth_success";

	} else {

	    if (DisplayType.Mobile.toString().equals(display)) {
		model.addAttribute("authToken", authToken);
		return "user/third_auth_success";
	    }
	    model.addAttribute("result", "success");
	    model.addAttribute("reffer", reffer);
	    return "user/third_auth_web_result";
	}
    }

    @RequestMapping("login-error")
    public String showThirdLoginError(Model model, HttpServletRequest request,
	    String cause) {

	String agent = request.getHeader("User-Agent");

	if (isMobile(agent)) {
	    model.addAttribute("cause", cause);
	    return "user/third_login_error";

	} else {

	    if (ErrorType.Cancel.toString().equals(cause)) {
		model.addAttribute("cause", cause);
		return "user/login";
	    }
	    model.addAttribute("error", getMessage(MESSAGE_ERROR_THIRD));
	    return "user/login";
	}
    }

    @RequestMapping("login-success")
    public String showThirdLoginSuccess(Model model,
	    HttpServletRequest request, String authToken, String display,
	    String reffer) {

	String agent = request.getHeader("User-Agent");

	if (isMobile(agent)) {
	    model.addAttribute("authToken", authToken == null ? "" : authToken);
	    return "user/third_login_success";

	} else {

	    if (DisplayType.Mobile.toString().equals(display)) {
		model.addAttribute("authToken", authToken == null ? ""
			: authToken);

		return "user/third_login_success";
	    }
	    authToken = AuthUtil.getWebTokenFromCookie(request);
	    Account ainfo = accountService.getUserProfile(authToken);

	    if (ainfo == null) {
		return "redirect:/account/login-error/?cause="
			+ ErrorType.Error.toString();

	    }

	    if (ainfo.hasRole(RoleType.NewBie)
		    && StringUtils.isBlank(ainfo.getPasswordHash())
		    && !ainfo.hasStatus(Status.ProfileResolved)) {

		model.addAttribute("name", ainfo.getName());
		model.addAttribute("picurl", ainfo.getAvatar());
		model.addAttribute("reffer", reffer);
		return "user/thirdinput";

	    } else {

		if (StringUtils.isNotBlank(reffer)) {
		    return "redirect:" + reffer;
		}
		return "redirect:/";
	    }
	}
    }

    /**
     * 处理邮箱验证
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "thirdPartyFriends/weibo", method = RequestMethod.GET)
    public String showThirdPartyFriends(HttpServletRequest request,
	    HttpServletResponse response, Model model, String reffer) {
	String authorization = AuthUtil.getWebTokenAuthorization(request);
	Account account = tokenUtil
		.getLoginUserAccontByAuthorization(authorization);

	if (account == null) {
	    return "redirect:/home";
	}
	List<ContactResult> contacts = thirdPartyRpcService
		.getThirdPartyFriends(authorization, ThirdPartyType.Weibo);

	if (contacts != null) {
	    List<ContactResult> duoduoContacts = new ArrayList<ContactResult>();
	    List<ContactResult> notDuoduoContacts = new ArrayList<ContactResult>();
	    parseResult(contacts, duoduoContacts, notDuoduoContacts);
	    model.addAttribute("duoduoContacts", duoduoContacts);
	    model.addAttribute("notDuoduoContacts", notDuoduoContacts);
	    model.addAttribute("token", AuthUtil.getWebTokenFromCookie(request));
	    model.addAttribute("reffer", reffer);

	} else {
	    model.addAttribute("error", "无法获得微博互粉好友");
	}
	return "user/weibofriends";
    }

    /**
     * 显示查看邮件页面
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "default/email", method = RequestMethod.GET)
    public String showVerifyEmail(String emailServer, String email, String u,
	    String invite, Model model) {

	model.addAttribute("email", email);
	model.addAttribute("emailServer", emailServer);
	model.addAttribute("u", u);
	model.addAttribute("invite", invite);
	return "user/email_verify";
    }

    @RequestMapping(value = "user/information", method = RequestMethod.GET)
    public String userInformation(HttpServletRequest request, Model model) {
	Account account = tokenUtil.getLoginUserAccontByAuthorization(AuthUtil
		.getWebTokenAuthorization(request));
	model.addAttribute("account", account);

	switch (account.getAccountType()) {
	case Facebook:
	case Gmail:
	case QQ:
	case Renren:
	case Twitter:
	case Weibo:

	    if (!account.hasStatus(Account.Status.ProfileResolved)) {

		return "redirect:/account/third/profile/";
	    }
	    break;

	default:
	    break;
	}

	// if (StringUtils.isBlank(account.getDuoduoEmail())) {
	// return "redirect:/account/user/coffeemail/";
	// }
	return "user/information";
    }

    @RequestMapping(value = "user/information/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<String> userInformationName(
	    HttpServletRequest request, @PathVariable("name") String name) {
	return buildJSONResult("result",
		String.valueOf(accountService.checkName(name)));
    }

    @RequestMapping(value = "user/coffeemail", method = RequestMethod.POST)
    public ResponseEntity<String> userModifyCoffeemail(
	    HttpServletRequest request, User user) {

	if (StringUtils.isBlank(user.getEmail())) {
	    throw new InvalidParameterException("cofeemail is empty!");
	}
	String dm = user.getEmail();

	if (!user.getEmail().trim().endsWith("@" + SystemConstant.mailSuffix)) {
	    dm = dm + "@" + SystemConstant.mailSuffix;
	}
	AccountUpdateInfo info = new AccountUpdateInfo();
	info.setCoffeeMail(dm);

	try {
	    long res = accountService.postInformation(info,
		    AuthUtil.getWebTokenAuthorization(request));

	    if (res == 204) {
		return buildJSONResult("url", "/home");

	    } else {
		return buildJSONResult("error",
			getMessage(MESSAGE_SERVER_INTERNAL_ERROR));

	    }

	} catch (UpdateAccountFailedException e) {
	    String errorCode = MESSAGE_SERVER_INTERNAL_ERROR;

	    if (e.getFieldErrors() != null && !e.getFieldErrors().isEmpty()
		    && e.getFieldErrors().values().iterator().hasNext()) {

		errorCode = e.getFieldErrors().values().iterator().next();

	    }
	    return buildJSONResult("error", getMessage(errorCode));
	}
    }

    @RequestMapping(value = "user/information", method = RequestMethod.POST)
    public ResponseEntity<String> userModifyInformation(
	    HttpServletRequest request, User user) {
	String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil
		.getWebTokenAuthorization(request));
	if (!StringUtils.isBlank(accountId)) {
	    Account account = service.getById(accountId);
	    if (account != null) {
		account.setProvince(user.getProvince() == null ? "" : user
			.getProvince());
		account.setCity(user.getCity() == null ? "" : user.getCity());
		account.setGender(user.getGender());
		account.setBirthday(DateUtil.getBirthdayStr(user.getBirthday()));
		account.setDescribe(user.getDescribe());
		account.setName(user.getName());
		service.save(account);
	    }
	}
	return buildJSONResult("result", "success");
    }

    @RequestMapping(value = "user/password", method = RequestMethod.POST)
    public ResponseEntity<String> userModifyPassword(
	    HttpServletRequest request,
	    @RequestParam("originalPwd") String originalPwd,
	    @RequestParam("newPwd") String newPwd) {
	String accountId = tokenUtil.getAccountIdOrGuestId(AuthUtil
		.getWebTokenAuthorization(request));
	if (!StringUtils.isBlank(accountId)) {
	    Account account = service.getById(accountId);
	    if (account != null) {
		if (accountService
			.auth(account.getLoginName(), originalPwd, "") != null) {
		    AccountUpdateInfo user = new AccountUpdateInfo();
		    PasswordInfo pwdInfo = new PasswordInfo(originalPwd, newPwd);
		    user.setPassword(pwdInfo);
		    accountService.postInformation(user,
			    AuthUtil.getWebTokenAuthorization(request));
		    return buildJSONResult("result", "success");
		}
	    }
	}
	return buildJSONResult("result", "error");
    }

    @RequestMapping(value = "user/password", method = RequestMethod.GET)
    public String userPassword() {
	return "user/password";
    }

    /**
     * 处理注册第一步发送验证邮件或验证手机
     * 
     * @param registerType
     * @param email
     * @param mobile
     * @param mobileCode
     * @param request
     * @param response
     * @param model
     * @param type
     *            第三方认证用户获取验证邮件
     * @return
     */
    @RequestMapping(value = "default/register/verify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> verify(String registerType, String email,
	    String mobile, String mobile2, String invite, String type,
	    HttpServletRequest request, HttpServletResponse response,
	    Model model) {

	Map<String, String> result = new HashMap<String, String>();
	User user = new User();
	user.setLoginName("mobile".equals(registerType) ? mobile : email);

	if ("email".equals(registerType)) {
	    // 发送验证邮件
	    try {
		accountService.applyVerifyEmail(email, type, invite);

	    } catch (TooManyVerifyRequestException e) {
		result.put(
			"msg",
			getMessage(
				"tooManyVerifyRequest",
				new Object[] {
					e.getVerifyTimesLimitCycleInHours(),
					e.getMaxVerifyTimes(),
					e.getMinVerifyIntervalInMinutes() }));

		return buildJSONResult(result);

	    } catch (IllegalArgumentException e) {
		result.put("msg",
			getMessage(MESSAGE_ERROR_REG_VERIFY_EMAILDUPLICATE));

		return buildJSONResult(result);

	    } catch (Exception e) {
		result.put("msg", getMessage(MESSAGE_SERVER_INTERNAL_ERROR));

		return buildJSONResult(result);
	    }
	    String suffix = user.getLoginName().substring(
		    user.getLoginName().indexOf("@") + 1,
		    user.getLoginName().length());

	    String mailServer = mailServers.get(suffix) == null ? "http://mail."
		    + suffix
		    : mailServers.get(suffix);

	    result.put("mailServer", mailServer);
	    response.setCharacterEncoding("utf-8");
	    return buildJSONResult(result);

	} else if ("mobile".equals(registerType)) {
	    // 验证手机号
	    int t = accountService.verifyAccountByMobile(mobile2, mobile);

	    if (t == 404) {
		return buildJSONResult("msg",
			getMessage(MESSAGE_ERROR_MOBILE_VERIFY));
	    }

	    if (t != 204) {
		return buildJSONResult("msg", getMessage(MESSAGE_ERROR_SERVER));
	    }
	    StringBuilder builder = new StringBuilder(
		    "/account/default/reg/?invite=");

	    builder.append(invite).append("&loginName=").append(mobile)
		    .append("&registerType=mobile&mobileToken=")
		    .append(mobile2);

	    return buildJSONResult("url", builder.toString());
	}
	return buildJSONResult("msg", getMessage(MESSAGE_ERROR_SERVER));
    }

    /**
     * 处理邮箱验证
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "default/verifyEmail", method = RequestMethod.GET)
    public String verifyEmail(String token, String invite, String email,
	    String u, Model model) {

	Token t = service.verifyAccountByEmail(token, email);

	if (t != null) {
	    String opttype = "";

	    if (StringUtils.isNotBlank(t.getAccountId())) {
		opttype = t.getAccountId();
	    }
	    return addQueryParams("redirect:/account/default/reg/",
		    "loginName", t.getRefer(), "registerType", "email",
		    "invite", invite, "emailToken", token, "optype", opttype);

	} else {
	    model.addAttribute("email", email);
	    model.addAttribute("invite", invite);
	    model.addAttribute("type", u);
	    return "user/email_verify_failed";
	}
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
	eventService.send(new UnbindAccountEvent(this, AuthUtil
		.getWebTokenAuthorization(request)));
	return "redirect:/home";// user/login
    }
}
