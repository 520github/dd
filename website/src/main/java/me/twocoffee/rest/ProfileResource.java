package me.twocoffee.rest;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.common.util.Pinyin4jUtils;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.entity.Token;
import me.twocoffee.exception.AccountVerifyOutnumberException;
import me.twocoffee.exception.DuplicateCoffeemailException;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;
import me.twocoffee.exception.TooShortVerifyIntervalException;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.AccountPerfictInfo;
import me.twocoffee.rest.entity.AccountUpdateInfo;
import me.twocoffee.rest.entity.AvatarInfo;
import me.twocoffee.rest.entity.FriendInfo;
import me.twocoffee.rest.entity.ListResult;
import me.twocoffee.rest.utils.InfoConverter;
import me.twocoffee.rest.utils.Validator;
import me.twocoffee.service.InviteRecordService;
import me.twocoffee.service.impl.InviteServiceImpl.InviteVerifyResult;
import me.twocoffee.service.rpc.ImageService;
import me.twocoffee.service.thirdparty.ThirdpartyService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

@Controller
@Path("/service/accounts")
public class ProfileResource extends AbstractAccountResource {

    @Autowired
    private ImageService imageService;

    @Autowired
    private InviteRecordService inviteRecordService;

    @Autowired
    private ThirdpartyService thirdpartyService;

    private final static String RETRY_AFTER_HEAD = "Retry-After";

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ProfileResource.class);

    private AccountInfo[] getFriendInfos(AccountInfo[] fs, String accountId,
	    String visitAccountId) {

	List<Account> visitFriends = null;

	if (accountId != null && !accountId.equals(visitAccountId)) {
	    visitFriends = friendService.findFriends(visitAccountId);
	}

	for (AccountInfo a : fs) {
	    FriendInfo fi = new FriendInfo();

	    if (accountId == null || accountId.equals(visitAccountId)) {
		fi.setIsFriend(true);

	    } else {
		fi.setIsFriend(hasFriend(a.getId(), visitFriends));
	    }
	    fi.setShared(friendService.countSharedTo(visitAccountId, a.getId()));
	    a.setFriend(fi);
	}
	return fs;
    }

    private String gravatarmapToJson(Map<String, String> map) {
	JSONObject json = new JSONObject();
	json.accumulate("large", map.get(Account.PHOTO_SIZE_BIG));
	json.accumulate("medium", map.get(Account.PHOTO_SIZE_MIDDLE));
	json.accumulate("small", map.get(Account.PHOTO_SIZE_SMALL));
	return new JSONObject().accumulate("uri", json).toString();
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

    private void update(AccountPerfictInfo user, Account account) {

	if (user.getAvatar() != null) {
	    account.setAvatar(Account.PHOTO_SIZE_BIG, user.getAvatar()
		    .getLarge());

	    account.setAvatar(Account.PHOTO_SIZE_MIDDLE, user.getAvatar()
		    .getMedium());

	    account.setAvatar(Account.PHOTO_SIZE_SMALL, user.getAvatar()
		    .getSmall());

	}

	if (StringUtils.isNotBlank(user.getName())) {
	    account.setName(user.getName());
	}

	if (StringUtils.isNotBlank(user.getGender())) {
	    account.setGender(user.getGender());
	}

	if (user.getPassword() != null) {
	    account.setPasswordHash(DigestUtils.md5DigestAsHex(user
		    .getPassword().getNewValue().getBytes()));

	}

	if (StringUtils.isNotBlank(user.getBirthday())) {
	    account.setBirthday(user.getBirthday());
	}

	if (StringUtils.isNotBlank(user.getIntroduction())) {
	    account.setDescribe(user.getIntroduction());
	}

	if (StringUtils.isNotBlank(user.getEmail())) {
	    account.setEmail(user.getEmail());
	}

	if (StringUtils.isNotBlank(user.getMobile())) {
	    account.setMobile(user.getMobile());
	}

	if (StringUtils.isNotBlank(user.getLoginName())) {
	    account.setLoginName(user.getLoginName());
	}
    }

    private void update(AccountUpdateInfo user, Account account) {

	if (user.getAvatar() != null) {
	    account.setAvatar(Account.PHOTO_SIZE_BIG, user.getAvatar()
		    .getLarge());

	    account.setAvatar(Account.PHOTO_SIZE_MIDDLE, user.getAvatar()
		    .getMedium());

	    account.setAvatar(Account.PHOTO_SIZE_SMALL, user.getAvatar()
		    .getSmall());

	}

	if (StringUtils.isNotBlank(user.getName())) {
	    account.setName(user.getName());
	    account.setNameInPinyin(Pinyin4jUtils.getPinYin(user.getName()));
	}

	if (StringUtils.isNotBlank(user.getGender())) {
	    account.setGender(user.getGender());
	}

	if (user.getPassword() != null) {
	    account.setPasswordHash(DigestUtils.md5DigestAsHex(user
		    .getPassword().getNewValue().getBytes()));

	}

	if (StringUtils.isNotBlank(user.getBirthday())) {
	    account.setBirthday(DateUtil.getBirthdayStr(user.getBirthday()));
	}
	account.setDescribe(user.getIntroduction());

	if (StringUtils.isNotBlank(user.getMobile())) {
	    account.setMobile(user.getMobile());
	}

	if (StringUtils.isNotBlank(user.getCoffeeMail())
		&& StringUtils.isBlank(account.getDuoduoEmail())) {

	    account.setDuoduoEmail(user.getCoffeeMail());
	    AccountMailConfig config = account.getMailConfig();
	    config.setFriendsVisible(true);
	    List<String> tags = config.getInTags();

	    if (tags == null) {
		tags = new ArrayList<String>();
	    }

	    if (!tags.contains(SystemTagEnum.Collect.toString())) {
		tags.add(SystemTagEnum.Collect.toString());
	    }
	    config.setInTags(tags);
	}
    }

    @Path("/profile")
    @GET
    @Produces({ "application/json" })
    public Response getProfile(@HeaderParam("Authorization") String token,
	    @HeaderParam("If-Modified-Since") String modifiedSince) {
    	//check token
    	int code = this.getAuthorizationErrorCode(token);
    	if(code > 200) {
    		return Response.status(code).build();
    	}
    	AccountInfo accountInfo;
    	//guest user
    	if(this.isGuestUser(token)) {
    		accountInfo = this.getGuestUserAccountInfoByAuthorization(token);
    		return Response.ok(accountInfo).build();
    	}
    	//login user
    	Account account = this.getLoginUserAccontByAuthorization(token);
    	if(account == null) {
    		return Response.status(404).build();
    	}
    	//not modify
    	Response response = this.getNotMondifiedResponse(modifiedSince, account);
    	if(response != null) {
    		return response;
    	}
    	return Response
		.ok(reduceAccount(account))
		.header("Last-Modified",
			DateUtil.formatRFC1123(account.getLastModified() == null ? new Date()
				: account.getLastModified()))
		.header("Expire",
			DateUtil.formatRFC1123(DateUtils.addHours(
				new Date(), 1)))
		.build();
    }

    @Path("/profile/{id}")
    @GET
    @Produces({ "application/json" })
    public Response getTheProfile(@PathParam("id") String id,
	    @HeaderParam("Authorization") String token,
	    @HeaderParam("If-Modified-Since") String modifiedSince) {

	if (StringUtils.isBlank(id) && (token == null || token.equalsIgnoreCase(""))) {
	    return Response.status(403).build();
	}

	if (StringUtils.isNotBlank(id) && !ObjectId.isValid(id)) {
	    return Response.status(400).build();
	}

	if (StringUtils.isNotBlank(id)) {
	    Account account = accountService.getById(id);

	    if (account == null) {
		return Response.status(404).build();

	    } else {
		AccountInfo ainfo = reduceAccount(account);

		if (StringUtils.isNotBlank(token)) {
			String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);

		    if (!StringUtils.isBlank(AccountIdOrGuestId)) {
			Account my = accountService.getById(AccountIdOrGuestId);

			if (my != null && !my.getId().equals(id)) {
			    setFriendRequestPending(ainfo, id, my.getId());
			    Friend friend = friendService
				    .getByAccountIdAndFriendId(my.getId(), id);

			    if (friend != null) {
				FriendInfo info = new FriendInfo();
				info.setIsFriend(true);
				info.setShared(friendService.countSharedTo(
					my.getId(), id));

				info.setAlias(friend.getRemarkName());
				info.setAliasInPinyin(friend
					.getRemarkNameInPinyin());

				info.setFrequently(Friend.FriendType.Favorite
					.equals(friend.getFriendType()));

				ainfo.setFriend(info);
			    }

			    if (friend == null
				    && account.getMailConfig()
					    .isFriendsVisible()) {

				ainfo.setCoffeeMail("");
			    }
			}
		    }
		}

		if (StringUtils.isNotBlank(modifiedSince)) {

		    try {
			Date sinceDate = DateUtil
				.parseRFC1123Date(modifiedSince);

			if (!sinceDate
				.before(account.getLastModified() == null ? new Date()
					: account.getLastModified())) {

			    return Response
				    .status(304)
				    .header("Last-Modified",
					    DateUtil.formatRFC1123(account
						    .getLastModified()))
				    .header("Expire",
					    DateUtil.formatRFC1123(DateUtils
						    .addHours(new Date(), 1)))
				    .build();

			}

		    } catch (ParseException e) {
			LOGGER.error("can not parse date " + modifiedSince);
		    }
		}
		return Response
			.ok(ainfo)
			.header("Last-Modified",
				DateUtil.formatRFC1123(account
					.getLastModified() == null ? new Date()
					: account.getLastModified()))
			.header("Expire",
				DateUtil.formatRFC1123(DateUtils.addHours(
					new Date(), 1)))
			.build();
	    }
	}
	
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	Account account = accountService.getById(AccountIdOrGuestId);

	if (account == null) {
	    return Response.status(403).build();
	}

	if (StringUtils.isNotBlank(modifiedSince)) {

	    try {
		Date sinceDate = DateUtil.parseRFC1123Date(modifiedSince);

		if (!sinceDate
			.before(account.getLastModified() == null ? new Date()
				: account.getLastModified())) {

		    return Response
			    .status(304)
			    .header("Last-Modified",
				    DateUtil.formatRFC1123(account
					    .getLastModified()))
			    .header("Expire",
				    DateUtil.formatRFC1123(DateUtils.addHours(
					    new Date(), 1)))
			    .build();

		}

	    } catch (ParseException e) {
		LOGGER.error("can not parse date " + modifiedSince);
	    }
	}
	return Response
		.ok(reduceAccount(account))
		.header("Last-Modified",
			DateUtil.formatRFC1123(account.getLastModified() == null ? new Date()
				: account.getLastModified()))
		.header("Expire",
			DateUtil.formatRFC1123(DateUtils.addHours(
				new Date(), 1)))
		.build();

    }

    /**
     * 查询好友列表
     * 
     * @param token
     * @return
     */
    @Path("/profile/id={accountId}/relation/friend")
    @GET
    @Produces({ "application/json" })
    public Response list(@HeaderParam("Authorization") String token,
	    @PathParam("accountId") String accountId) {
	Response valid = this.validAuthorizationAndGuestUser(token);
    if(valid != null) {
    	return valid;
    }
	String visitAccountId = this.getAccountIdOrGuestId(token);
	List<Account> friends = null;

	if (accountId != null) {
	    friends = friendService.findFriends(accountId);

	} else {
	    friends = friendService.findFriends(visitAccountId);
	}
	List<AccountInfo> fs = InfoConverter.convertToAccounts(friends);
	ListResult r = new ListResult();
	AccountInfo[] ais = null;
	if (fs != null) {
	    ais = new AccountInfo[fs.size()];
	    r.setResult(fs.toArray(ais));
	}

	if (fs != null && fs.size() > 0) {
	    r.setResult(getFriendInfos(r.getResult(), accountId, visitAccountId));
	}
	return Response.ok(r).build();
    }

    @Path("/basic-profile")
    @PUT
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    // TODO:SNS 支持mobile
    public Response perfictProfile(@HeaderParam("Authorization") String token,
	    AccountPerfictInfo user) {

	Map<String, String> fieldErrors = new HashMap<String, String>();
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	Account account = this.getLoginUserAccontByAuthorization(token);

	if (account == null) {
	    return Response.status(403).build();
	}

	if (account.hasStatus(Account.Status.ProfileResolved)) {
	    return Response.status(409).build();
	}
	switch (account.getAccountType()) {
	case TwoCoffeeEmail:
	case TwoCoffeeMobile:
	    // mobile or email type, can not update loginname
	    if (StringUtils.isNotBlank(user.getLoginName())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_LOGIN_NAME, PROFILE_LOGINNAME_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (user.getPassword() != null) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (StringUtils.isNotEmpty(user.getEmail())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_EMAIL, PROFILE_EMAIL_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (StringUtils.isNotEmpty(user.getName())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_NAME, PROFILE_NAME_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (StringUtils.isNotBlank(user.getMobile())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_MOBILE, PROFILE_MOBILE_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    // if (StringUtils.isNotEmpty(user.getInviteCode())) {
	    // Map<String, Object> result = new HashMap<String, Object>();
	    // fieldErrors.put(FIELD_INVITE_CODE,
	    // PROFILE_INVITECODE_ERROR_CODE);
	    //
	    // result.put("fieldError", fieldErrors);
	    // return Response.status(409).entity(result).build();
	    // }
	    break;
	case Facebook:
	case Gmail:
	case QQ:
	case Renren:
	case Twitter:
	case Weibo:

	    // if (StringUtils.isBlank(user.getName())) {
	    // Map<String, Object> result = new HashMap<String, Object>();
	    // fieldErrors.put(FIELD_NAME, PROFILE_NAME_ERROR_CODE);
	    // result.put("fieldError", fieldErrors);
	    // return Response.status(409).entity(result).build();
	    // }

	    if (StringUtils.isNotBlank(user.getLoginName())
		    && (user.getPassword() == null
			    || StringUtils.isBlank(user.getPassword()
				    .getNewValue())
			    || (StringUtils.isBlank(user.getEmail()) && StringUtils
			    .isBlank(user.getMobile())))) {

		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_LOGIN_NAME, PROFILE_LOGINNAME_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    // if (StringUtils.isNotBlank(user.getMobile())) {
	    // Map<String, Object> result = new HashMap<String, Object>();
	    // fieldErrors.put(FIELD_MOBILE, PROFILE_MOBILE_ERROR_CODE);
	    // result.put("fieldError", fieldErrors);
	    // return Response.status(409).entity(result).build();
	    // }

	    if (StringUtils.isBlank(user.getLoginName())
		    && (user.getPassword() != null
		    || StringUtils.isNotBlank(user.getPassword()
			    .getNewValue()))) {

		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (StringUtils.isBlank(user.getLoginName())
		    && (StringUtils.isNotBlank(user.getEmail()) || StringUtils
			    .isNotBlank(user.getMobile()))) {

		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_LOGIN_NAME, PROFILE_LOGINNAME_ERROR_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }
	    break;

	default:
	    return Response.status(409).build();
	}

	if (StringUtils.isNotEmpty(user.getName())
		&& !Validator.validateName(user.getName())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, NAME_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getGender())
		&& !Validator.validateGender(user.getGender())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_GENDER, GENDER_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getEmail())
		&& !Validator.validateEmail(user.getEmail())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_EMAIL, GENDER_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getMobile())
		&& !Validator.validateMobile(user.getMobile())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_GENDER, GENDER_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (user.getPassword() != null
		&& (user.getPassword().getNewValue().length() < 6 || user
			.getPassword().getNewValue().length() > 16)) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getBirthday())) {
	    Date date = null;

	    try {
		date = DateUtil.FormatDateStringUTC(user.getBirthday());

	    } catch (ParseException e) {
		date = null;
	    }

	    if (date == null) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_BIRTHDAY, BIRTHDAY_INVALID_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();

	    }
	}

	if (StringUtils.isNotBlank(user.getInviteCode())) {
	    InviteVerifyResult verifyResult = inviteService.verify(user
		    .getInviteCode());

	    if (verifyResult == InviteVerifyResult.NotExists) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_INVITE_CODE, INVITE_CODE_INVALID_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();

	    } else if (verifyResult == InviteVerifyResult.Expired) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_INVITE_CODE, INVITE_CODE_EXPIRED_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }
	}

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

	if (StringUtils.isNotBlank(user.getEmail())) {
	    AccountMailConfig config = account.getMailConfig();
	    List<String> mails = config.getMails();

	    if (mails == null) {
		mails = new ArrayList<String>();
	    }

	    if (!mails.contains(user.getEmail())) {
		mails.add(user.getEmail());
	    }
	    config.setMails(mails);
	}
	account.addStat(Status.ProfileResolved);
	update(user, account);
	Account au;

	try {
	    au = accountService.updateAccount(account);

	} catch (DuplicateNameException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, NAME_DUPLICATE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();

	} catch (DuplicateLoginNameException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_LOGIN_NAME,
		    LOGIN_NAME_DUPLICATE_ERROR_CODE);

	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();

	} catch (DuplicateCoffeemailException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, COFFEEMAIL_DUPLICATE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotBlank(user.getMobile())) {
	    ThirdPartyProfile third = new ThirdPartyProfile();
	    third.setId(new ObjectId().toString());
	    third.setAccountId(account.getId());
	    third.setAccountType(ThirdPartyType.Phone);
	    third.setBind(true);
	    third.setCreateDate(new Date());
	    third.setNickName(account.getName());
	    third.setSyncContent(false);
	    third.setUserId(user.getMobile());
	    thirdpartyService.save(third);
	}

	/*
	 * try { inviteRecordService.verifyInvitation(user.getLoginName());
	 * 
	 * } catch (Exception e) {
	 * LOGGER.error("find error when verifyInvitation with loginname " +
	 * user.getLoginName(), e);
	 * 
	 * }
	 */

	if (au != null) {

	    if (StringUtils.isNotBlank(au.getEmail())) {

		try {
		    accountService.notifyUser(au);

		} catch (Exception e) {
		    LOGGER.error("find error when welcom user loginname "
			    + user.getLoginName(), e);

		}
	    }
	    return Response.status(204).build();

	} else {
	    return Response.status(500).build();
	}
    }

    @Path("/profile")
    @PUT
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response putProfile(@HeaderParam("Authorization") String token,
	    AccountUpdateInfo user) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	Account account = this.getLoginUserAccontByAuthorization(token);

	if (account == null) {
	    return Response.status(403).build();
	}
	Map<String, String> fieldErrors = new HashMap<String, String>();

	if (StringUtils.isNotBlank(user.getMobile())
		&& account.getAccountType().equals(AccountType.TwoCoffeeMobile)) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_MOBILE, PROFILE_MOBILE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (user.getPassword() != null
		&& (StringUtils
			.isBlank(user.getPassword().getOldValue())
		|| !DigestUtils
			.md5DigestAsHex(
				user.getPassword().getOldValue()
					.getBytes())
			.equals(account.getPasswordHash()))) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_INCONSISTENT_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (user.getPassword() != null
		&& (StringUtils.isBlank(user.getPassword().getNewValue())
			|| user.getPassword().getNewValue().length() < 6 || user
			.getPassword().getNewValue().length() > 16)) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_PASSWORD, PROFILE_PASSWORD_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getName())
		&& !Validator.validateName(user.getName())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, NAME_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getGender())
		&& !Validator.validateGender(user.getGender())) {

	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_GENDER, GENDER_INVALID_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotEmpty(user.getBirthday())) {

	    try {
		DateUtil.FormatDateStringUTC(user.getBirthday());

	    } catch (ParseException e) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_BIRTHDAY, BIRTHDAY_INVALID_CODE);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }
	}

	if (StringUtils.isNotEmpty(user.getCoffeeMail())) {

	    if (StringUtils.isNotBlank(account.getDuoduoEmail())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_COFFEEMAIL, PROFILE_COFFEEMAIL_ERROR);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }

	    if (!Validator.validateEmail(user.getCoffeeMail())) {
		Map<String, Object> result = new HashMap<String, Object>();
		fieldErrors.put(FIELD_COFFEEMAIL, PROFILE_COFFEEMAIL_INVALID);
		result.put("fieldError", fieldErrors);
		return Response.status(409).entity(result).build();
	    }
	}
	update(user, account);
	Account au = null;

	try {
	    au = accountService.updateAccount(account);

	} catch (DuplicateNameException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, NAME_DUPLICATE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();

	} catch (DuplicateLoginNameException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_LOGIN_NAME,
		    LOGIN_NAME_DUPLICATE_ERROR_CODE);

	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();

	} catch (DuplicateCoffeemailException e1) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    fieldErrors.put(FIELD_NAME, COFFEEMAIL_DUPLICATE_ERROR_CODE);
	    result.put("fieldError", fieldErrors);
	    return Response.status(409).entity(result).build();
	}

	if (StringUtils.isNotBlank(user.getMobile())) {
	    ThirdPartyProfile third1 = thirdpartyService.getByAccountId(
		    account.getId(), ThirdPartyType.Phone);

	    if (third1 != null) {
		third1.setUserId(user.getMobile());
		thirdpartyService.save(third1);

	    } else {
		ThirdPartyProfile third = new ThirdPartyProfile();
		third.setId(new ObjectId().toString());
		third.setAccountId(account.getId());
		third.setAccountType(ThirdPartyType.Phone);
		third.setBind(true);
		third.setCreateDate(new Date());
		third.setNickName(account.getName());
		third.setSyncContent(false);
		third.setUserId(user.getMobile());
		thirdpartyService.save(third);
	    }
	}

	if (au != null) {
	    return Response.status(204).build();

	} else {
	    return Response.status(500).build();
	}
    }

    @Path("/avatar")
    @POST
    @Consumes({ "image/jpeg", "image/jpg", "image/png" })
    @Produces({ "application/json" })
    public Response userGravatar(@HeaderParam("Authorization") String token,
	    @Context HttpServletRequest request) throws URISyntaxException {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	try {
	    if (request.getContentLength() > 5 * 1024 * 1024) {
		return Response.status(413).entity("file so big")
			.build();
	    }
	    ServletInputStream in = request.getInputStream();
	    int length = 0;
	    byte[] b = new byte[1024];
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    while ((length = in.read(b, 0, 1024)) != -1) {
		out.write(b, 0, length);
	    }
	    in.close();
	    byte[] outb = out.toByteArray();
	    out.close();

	    String accountId = AccountIdOrGuestId;
	    Account account = accountService.getById(accountId);
	    Map<String, String> map = imageService.compressImage(outb);
	    account.setPhotos(map);
	    accountService.save(account);
	    return Response.ok(gravatarmapToJson(map)).status(201)
		    .location(new URI(map.get(Account.PHOTO_SIZE_SMALL)))
		    .build();
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    return Response.ok("{\"url\",\"\"}").build();
	}

    }

    @Path("/verify/email")
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @SuppressWarnings("unchecked")
    public Response verifyEmail(
	    Map<String, Object> request) {

	if (request == null
		|| StringUtils.isBlank((String) request.get("email"))) {

	    LOGGER.error("email can not be empty");
	    return Response.status(409).entity("email can not be empty")
		    .build();

	}
	String email = (String) request.get("email");
	Map<String, String> attchment = (Map<String, String>) request
		.get("attachment");

	try {
	    accountService.applyforVeriyEmail(attchment,
		    email);

	} catch (IllegalArgumentException e) {
	    LOGGER.error(e.getMessage());
	    return Response
		    .status(409)
		    .entity(e.getMessage()).build();

	} catch (TooShortVerifyIntervalException e) {
	    String errorMsg = email + " should be retry after "
		    + e.getRetryAfterInSeconds() + " seconds";

	    LOGGER.debug(errorMsg);
	    return Response
		    .status(429)
		    .header(RETRY_AFTER_HEAD, e.getRetryAfterInSeconds())
		    .entity(errorMsg).build();

	} catch (AccountVerifyOutnumberException e) {
	    String errorMsg = email + " should be retry after "
		    + e.getRetryAfterInSeconds() + " seconds";

	    LOGGER.debug(errorMsg);
	    return Response
		    .status(429)
		    .header(RETRY_AFTER_HEAD, e.getRetryAfterInSeconds())
		    .entity(errorMsg).build();

	}
	return Response.status(204).build();
    }

    @SuppressWarnings("unchecked")
    @Path("/verify/mobile")
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response verifyMobile(Map<String, Object> request) {

	if (request == null
		|| StringUtils.isBlank((String) request.get("mobile"))) {

	    LOGGER.error("mobile can not be empty");
	    return Response.status(409).entity("mobile can not be empty")
		    .build();

	}
	String mobile = (String) request.get("mobile");
	Map<String, String> attchment = (Map<String, String>) request
		.get("attachment");

	try {
	    accountService.applyforVerifySMS(mobile, attchment);

	} catch (IllegalArgumentException e) {
	    LOGGER.error(e.getMessage());
	    return Response
		    .status(409)
		    .entity(e.getMessage()).build();

	} catch (TooShortVerifyIntervalException e) {
	    String errorMsg = mobile + " should be retry after "
		    + e.getRetryAfterInSeconds() + " seconds";

	    LOGGER.debug(errorMsg);
	    return Response
		    .status(429)
		    .header(RETRY_AFTER_HEAD, e.getRetryAfterInSeconds())
		    .entity(errorMsg).build();

	} catch (AccountVerifyOutnumberException e) {
	    String errorMsg = mobile + " should be retry after "
		    + e.getRetryAfterInSeconds() + " seconds";

	    LOGGER.debug(errorMsg);
	    return Response
		    .status(429)
		    .header(RETRY_AFTER_HEAD, e.getRetryAfterInSeconds())
		    .entity(errorMsg).build();

	}
	return Response.status(204).build();
    }

    @Path("/verify/mobile/{mobileNumber}/{verifyCode}")
    @GET
    @Produces({ "application/json" })
    public Response verifyMobile(
	    @PathParam("mobileNumber") String mobileNumber,
	    @PathParam("verifyCode") String verifyCode) {

	if (StringUtils.isBlank(mobileNumber)
		|| StringUtils.isBlank(verifyCode)) {

	    return Response.status(404).build();
	}
	Token t = accountService
		.verifyAccountByMobile(verifyCode, mobileNumber);

	if (t == null) {
	    return Response.status(404).build();
	}
	return Response.status(204).build();
    }
}