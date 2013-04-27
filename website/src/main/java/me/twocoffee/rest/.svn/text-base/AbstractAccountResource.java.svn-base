/**
 * 
 */
package me.twocoffee.rest;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.AccountInfo.InvitationInfo;
import me.twocoffee.rest.entity.CounterInfo;
import me.twocoffee.rest.utils.InfoConverter;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AcknowledgmentService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.EventService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author momo
 * 
 */
public abstract class AbstractAccountResource extends AbstractResource {
	protected static final String FIELD_EMAIL = "email";
	protected static final String FIELD_MOBILE = "mobile";
	protected static final String FIELD_NAME = "name";
	protected static final String FIELD_LOGIN_NAME = "loginName";
	protected static final String FIELD_PASSWORD = "password";
	protected static final String FIELD_INVITE_CODE = "inviteCode";
	protected static final String FIELD_LOGINTYPE = "loginType";
	protected static final String FIELD_GENDER = "gender";
	protected static final String FIELD_BIRTHDAY = "birthday";
	protected static final String FIELD_COFFEEMAIL = "coffeeMail";
	// private static final String FIELD_VERIFY = "verify";
	protected static final String EMAIL_ERROR_CODE = "emailIsEmpty";
	protected static final String MOBILE_ERROR_CODE = "mobileIsEmpty";
	protected static final String LOGIN_NAME_ERROR_CODE = "loginNameIsEmpty";
	protected static final String NAME_ERROR_CODE = "reg.name.empty";
	protected static final String LOGIN_NAME_DUPLICATE_ERROR_CODE = "loginNameDuplicate";
	protected static final String password_ERROR_CODE = "passwordIsEmpty";
	protected static final String INVITE_CODE_INVALID_CODE = "inviteCodeInvalid";
	protected static final String INVITE_CODE_EXPIRED_CODE = "inviteCodeExpired";
	protected static final String MOBILE_ERROR_VERIFY_CODE = "reg.mobile.verifyFailed";
	protected static final String MOBILE_ERROR_INCONSISTENT_CODE = "reg.mobile.inconsistent";
	protected static final String EMAIL_ERROR_VERIFY_CODE = "reg.email.verifyFailed";
	protected static final String REG_EMAIL_INVALID = "reg.email.invalid";
	protected static final String EMAIL_ERROR_INCONSISTENT_CODE = "reg.email.inconsistent";
	protected static final String NAME_DUPLICATE_ERROR_CODE = "reg.name.duplicate";
	protected static final String LOGINTYPE_EMPTY_ERROR_CODE = "reg.logintype.empty";
	protected static final String PROFILE_LOGINNAME_ERROR_CODE = "profile.loginname.error";
	protected static final String PROFILE_PASSWORD_ERROR_CODE = "profile.password.error";
	protected static final String PROFILE_EMAIL_ERROR_CODE = "profile.email.error";
	protected static final String PROFILE_NAME_ERROR_CODE = "profile.name.error";
	protected static final String PROFILE_MOBILE_ERROR_CODE = "profile.mobile.error";
	protected static final String PROFILE_INVITECODE_ERROR_CODE = "profile.invitecode.error";
	protected static final String PROFILE_LOGINNAME_INVALID_CODE = "profile.loginname.invalid";
	protected static final String PROFILE_PASSWORD_INVALID_CODE = "profile.password.invalid";
	protected static final String PROFILE_PASSWORD_INCONSISTENT_CODE = "profile.password.inconsistent";
	protected static final String MOBILE_INVALID_CODE = "reg.mobile.invalid";
	protected static final String NAME_INVALID_CODE = "reg.name.invalid";
	protected static final String GENDER_INVALID_CODE = "profile.gender.invalid";
	protected static final String BIRTHDAY_INVALID_CODE = "profile.birthday.invalid";
	protected static final String PROFILE_COFFEEMAIL_ERROR = "profile.coffeemail.error";
	protected static final String PROFILE_COFFEEMAIL_INVALID = "profile.coffeemail.invalid";
	protected static final String COFFEEMAIL_DUPLICATE_ERROR_CODE = "profile.coffeemail.duplicate";

	// private static final String VERIFY_EMPTY_ERROR_CODE = "reg.verify.empty";
	@Autowired
	protected final AccountService accountService = null;

	@Autowired
	protected final AuthTokenService authTokenService = null;

	@Autowired
	protected FriendService friendService;

	@Autowired
	protected MessageService messageService;
	
	@Autowired
	protected AcknowledgmentService acknowledgmentService;

	@Autowired
	protected InviteService inviteService;

	@Autowired
	protected ThirdpartyService thirdpartyService;
	
	@Autowired
	protected EventService eventService;

	protected AuthToken getAuthToken(String token) {

		if (token == null || token.equalsIgnoreCase("")) {
			return null;
		}
		String t = token.substring("AuthToken".length()).trim();
		AuthToken authToken = authTokenService.findById(t);

		if (authToken == null) {
			return null;
		}
		return authToken;
	}

	protected AccountInfo reduceAccount(final Account account) {
		AccountInfo info = InfoConverter
				.convertToAccountInfo(account);

		setCounterInfo(info,
				friendService.countByAccountId(account.getId()),
				friendService.countSharedByAccountId(account.getId()),
				acknowledgmentService.totalByAccountIdAndContent(account.getId(), null));

		InvitationInfo info2 = new InvitationInfo();
		Invite invite = inviteService.getInviteByAccount(account.getId(), true);
		info2.setCode(invite.getId());
		info2.setLink(thirdpartyService.getShortUrl("http://"
				+ SystemConstant.domainName
				+ "/account/invite/"
				+ invite.getId()));

		info.setInvitation(info2);
		return info;
	}

	protected void setCounterInfo(AccountInfo info, int countByAccountId,
			int countSharedByAccountId,int countAcknowledgment) {

		CounterInfo counter = new CounterInfo(countSharedByAccountId,
				countByAccountId,countAcknowledgment);

		info.setCounter(counter);
	}

	protected void setFriendRequestPending(AccountInfo info, String id,
			String myId) {

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("id", id);
		long num = 0;

		try {
			num = messageService.countNormalMessage(myId,
					MessageType.FriendRequest,
					attributes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		info.setFriendRequestPending(num > 0);
	}
}
