package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.ResponseUser;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.entity.WebToken.UserType;
import me.twocoffee.entity.User;
import me.twocoffee.entity.UserAgentLog;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;
import me.twocoffee.rest.utils.Validator;
import me.twocoffee.service.EventService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.event.BindAccountEvent;
import me.twocoffee.service.event.BindThirdPartyEvent;
import me.twocoffee.service.event.InviteeSuggestEvent;
import me.twocoffee.service.event.ShowThirdPartyFriendsEvent;
import me.twocoffee.service.event.SyncGuestRepositoryEvent;
import me.twocoffee.service.event.message.AuthenticationSuccessEvent;
import me.twocoffee.service.impl.InviteServiceImpl.InviteVerifyResult;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

@Controller
@Path("/service/accounts")
public class RegisterResource extends AbstractAccountResource {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private ThirdpartyService thirdpartyService;
    @Autowired
    private EventService eventService;

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(RegisterResource.class);

    private void checkRegisterRequest(User user,
	    Map<String, String> fieldErrors) {

	if (user == null) {
	    LOGGER.error("user can not be empty");
	    fieldErrors.put(FIELD_LOGIN_NAME, LOGIN_NAME_ERROR_CODE);
	    fieldErrors.put(FIELD_NAME, NAME_ERROR_CODE);
	    fieldErrors.put(FIELD_PASSWORD, password_ERROR_CODE);
	    fieldErrors.put(FIELD_LOGINTYPE, LOGINTYPE_EMPTY_ERROR_CODE);
	}

	if (StringUtils.isBlank(user.getLoginName())) {
	    LOGGER.error("loginName can not be empty");
	    fieldErrors.put(FIELD_LOGIN_NAME, LOGIN_NAME_ERROR_CODE);
	}

	if (user.getLoginType() == null) {
	    LOGGER.error("loginType can not be empty");
	    fieldErrors.put(FIELD_LOGINTYPE, LOGINTYPE_EMPTY_ERROR_CODE);
	}

	if (User.LoginType.Email.equals(user.getLoginType())
		&& StringUtils.isBlank(user.getEmail())) {

	    LOGGER.error("email can not be empty");
	    fieldErrors.put(FIELD_EMAIL, EMAIL_ERROR_CODE);
	}

	if (User.LoginType.Email.equals(user.getLoginType())
		&& StringUtils.isNotBlank(user.getEmail())
		&& !Validator.validateEmail(user.getEmail())) {

	    LOGGER.error("email is invalid!");
	    fieldErrors.put(FIELD_EMAIL, REG_EMAIL_INVALID);
	}

	if (User.LoginType.Email.equals(user.getLoginType())
		&& !user.getLoginName().equals((user.getEmail()))) {

	    LOGGER.error("email must be equals with loginname when logintype is email");
	    fieldErrors.put(FIELD_EMAIL, EMAIL_ERROR_INCONSISTENT_CODE);
	}

	if (User.LoginType.Mobile.equals(user.getLoginType())
		&& StringUtils.isBlank(user.getMobile())) {

	    LOGGER.error("mobile can not be empty");
	    fieldErrors.put(FIELD_MOBILE, MOBILE_ERROR_CODE);
	}

	if (User.LoginType.Mobile.equals(user.getLoginType())
		&& StringUtils.isNotBlank(user.getMobile())
		&& !Validator.validateMobile(user.getMobile())) {

	    LOGGER.error("mobile is invalid");
	    fieldErrors.put(FIELD_MOBILE, MOBILE_INVALID_CODE);
	}

	if (User.LoginType.Mobile.equals(user.getLoginType())
		&& !user.getLoginName().equals((user.getMobile()))) {

	    LOGGER.error("mobile must be equals with loginname when logintype is mobile");
	    fieldErrors.put(FIELD_MOBILE, MOBILE_ERROR_INCONSISTENT_CODE);
	}

	if (StringUtils.isBlank(user.getPassword())) {
	    LOGGER.error("password cant not be empty");
	    fieldErrors.put(FIELD_PASSWORD, password_ERROR_CODE);
	}

	if (StringUtils.isBlank(user.getName())) {
	    LOGGER.error("name cant not be empty");
	    fieldErrors.put(FIELD_NAME, NAME_ERROR_CODE);
	}

	if (!Validator.validatePassword(user.getPassword())) {
	    LOGGER.error("password is invalid");
	    fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_INVALID_CODE);
	}

	if (!Validator.validateName(user.getName())) {
	    LOGGER.error("name is invalid");
	    fieldErrors.put(FIELD_NAME, NAME_INVALID_CODE);
	}
	// if (StringUtils.isBlank(user.getVerify())
	// && User.LoginType.Mobile.equals(user.getLoginType())) {
	//
	// LOGGER.error("login by mobile, verify can not be empty");
	// fieldErrors.put(FIELD_VERIFY, VERIFY_EMPTY_ERROR_CODE);
	// }

	if (StringUtils.isNotBlank(user.getInviteCode())) {
	    InviteVerifyResult verifyResult = inviteService.verify(user
		    .getInviteCode());

	    if (verifyResult == InviteVerifyResult.NotExists) {
		fieldErrors.put(FIELD_INVITE_CODE, INVITE_CODE_INVALID_CODE);

	    } else if (verifyResult == InviteVerifyResult.Expired) {
		fieldErrors.put(FIELD_INVITE_CODE, INVITE_CODE_EXPIRED_CODE);
	    }
	}
	Account account = accountService.getByLoginName(user.getLoginName());

	if (account != null) {
	    fieldErrors.put(FIELD_LOGIN_NAME, LOGIN_NAME_DUPLICATE_ERROR_CODE);
	}
	account = accountService.getByName(user.getName());

	if (account != null) {
	    fieldErrors.put(FIELD_NAME, NAME_DUPLICATE_ERROR_CODE);
	}
    }

    @Path("/default")
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response register(User user,
	    @HeaderParam("User-Agent") String userAgent,
	    @HeaderParam("Authorization") String token) {
	Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
	checkRegisterRequest(user, fieldErrors);

	if (fieldErrors != null && !fieldErrors.isEmpty()) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}
	Account account = new Account();
	AccountMailConfig config = new AccountMailConfig();
	account.setMailConfig(config);

	if (User.LoginType.Email.equals(user.getLoginType())) {
	    account.setAccountType(Account.AccountType.TwoCoffeeEmail);
	    List<String> mails = new ArrayList<String>();
	    mails.add(user.getLoginName());
	    config.setMails(mails);
	}

	if (User.LoginType.Mobile.equals(user.getLoginType())) {
	    account.setAccountType(AccountType.TwoCoffeeMobile);
	}
	account.setEmail(user.getEmail());
	account.setLoginName(user.getLoginName());
	account.setCreateDate(new Date());
	account.setName(user.getName());
	account.setMobile(user.getMobile());
	account.setPasswordHash(DigestUtils.md5DigestAsHex(user.getPassword()
		.getBytes()));

	if (StringUtils.isNotBlank(user.getInviteCode())) {
	    Invite invite = inviteService.getInviteById(user.getInviteCode());

	    if (invite != null) {

		if (invite.getRoles() != null
			&& invite.getRoles().size() > 0) {

		    account.setRole(invite.getRoles());
		    LOGGER.debug("set user roles to {} from invite code {}",
			    account.getRole(), invite.getId());

		}

		if (StringUtils.isNotBlank(invite.getOwnerId())) {
		    account.setInviter(invite.getOwnerId());
		}
	    }
	}

	try {
	    accountService.save(account);

	} catch (DuplicateLoginNameException duplicateLoginNameException) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_LOGIN_NAME,
		    LOGIN_NAME_DUPLICATE_ERROR_CODE);

	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();

	} catch (DuplicateNameException duplicateNameException) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, NAME_DUPLICATE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}
	Account u = accountService.getByLoginName(user.getLoginName());

	if (AccountType.TwoCoffeeMobile.compareTo(u.getAccountType()) == 0) {
	    ThirdPartyProfile third = new ThirdPartyProfile();
	    third.setId(new ObjectId().toString());
	    third.setAccountId(u.getId());
	    third.setAccountType(ThirdPartyType.Phone);
	    third.setBind(true);
	    third.setCreateDate(new Date());
	    third.setNickName(u.getName());
	    third.setSyncContent(false);
	    third.setUserId(u.getMobile());
	    thirdpartyService.save(third);
	}
	ResponseUser responseUser = new ResponseUser();
	responseUser.setAccount(reduceAccount(u));
	// support client old version
	if (StringUtils.isEmpty(token) || !TokenUtil.isEmptyAuthToken(token)) {
	    AuthToken authToken = authTokenService.createToken(u.getId());
	    responseUser.setAuthToken(authToken.getId());
	}

	eventService.send(new AuthenticationSuccessEvent(this, u));
	eventService.send(new BindAccountEvent(this, token, account.getId(),UserType.Register));
	eventService.send(new SyncGuestRepositoryEvent(this, token, account
		.getId()));

	if (u.getAccountType().equals(Account.AccountType.TwoCoffeeEmail)) {

	    try {
		accountService.notifyUser(u);

	    } catch (Exception e) {
		LOGGER.error("find error when send email to loginname "
			+ user.getLoginName(), e);

	    }
	}
	// 提醒用户绑定第三方帐户
	BindThirdPartyEvent e = new BindThirdPartyEvent(u);
	eventService.send(e);
	ShowThirdPartyFriendsEvent event = new ShowThirdPartyFriendsEvent(u,
		ThirdPartyType.Phone);

	eventService.send(event);

	if (StringUtils.isNotBlank(account.getInviter())) {
	    InviteeSuggestEvent event2 = new InviteeSuggestEvent(account);
	    eventService.send(event2);
	}
	UserAgentLog log = new UserAgentLog();
	log.setId(new ObjectId().toString());
	log.setAccount(account.getId());
	log.setCreateDate(new Date());
	log.setUserAgent(userAgent);
	accountService.saveUserAgent(log);
	accountService.createDefaultSettings(account.getId());
	return Response.ok(responseUser).build();
    }
}
