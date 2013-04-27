package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import me.twocoffee.common.auth.AuthClient;
import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.SettingsDao;
import me.twocoffee.dao.ThirdPartyProfileDao;
import me.twocoffee.dao.TokenDao;
import me.twocoffee.dao.UserAgentLogDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.LoginToken.DisplayType;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.entity.Settings;
import me.twocoffee.entity.Settings.FileUploadSettings;
import me.twocoffee.entity.Settings.FriendGroup;
import me.twocoffee.entity.Settings.Limitation;
import me.twocoffee.entity.Settings.Sharing;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.Token;
import me.twocoffee.entity.UserAgentLog;
import me.twocoffee.exception.AccountNotExistException;
import me.twocoffee.exception.AccountVerifyOutnumberException;
import me.twocoffee.exception.DuplicateCoffeemailException;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;
import me.twocoffee.exception.TooShortVerifyIntervalException;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.DocumentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.SendEmailService;
import me.twocoffee.service.SendSMSService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

	private static String generateRandomSMSToken() {
		String result = "";

		Random random = new Random();

		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	@Autowired
	private AuthClient authClient;
	@Autowired
	private AccountDao dao = null;
	@Autowired
	private ThirdPartyProfileDao thirdProfileDao;

	private TokenDao tokenDao = null;
	@Autowired
	private SendEmailService sendEmailService = null;
	@Autowired
	private SendSMSService sendSMSService = null;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MessageService messageService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SettingsDao settingsDao;

	@Autowired
	private UserAgentLogDao userAgentLogDao;

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private FriendService friendService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountServiceImpl.class);
	/**
	 * 两次邮件验证之间最小时间间隔
	 */
	private int minEmailVerifyIntervalInMinutes = 2;

	/**
	 * 两次手机验证之间最小时间间隔
	 */
	private int minMobileVerifyIntervalInMinutes = 1;
	/**
	 * 在某一时间段(emailVerifyTimesLimitCycleInHours)内使用邮件验证的次数限制
	 */
	private int maxEmailVerifyTimes = 10;

	/**
	 * 在某一时间段(mobileVerifyTimesLimitCycleInHours)内使用手机验证的次数限制
	 */
	private int maxMobileVerifyTimes = 10;

	/**
	 * 邮件验证次数限制的时间段
	 */
	private int emailVerifyTimesLimitCycleInHours = 24;

	/**
	 * 手机验证次数限制的时间段
	 */
	private int mobileVerifyTimesLimitCycleInHours = 24;
	/**
	 * 邮件验证时token的有效期
	 */
	private int emailTokenLifeInHour = 48;

	/**
	 * 手机验证时token的有效期
	 */
	private int mobileTokenLifeInMinutes = 30;

	private String recommendAccountName;

	private void applyforVerifySMS(String accountId, String mobile)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException {

		// if (dao.getByLoginName(mobile) != null) {
		// throw new IllegalArgumentException("mobile " + mobile
		// + " is exists");
		//
		// }
		List<Token> tokenList = tokenDao
				.listByReferAndCreateTime(mobile, DateUtils
						.addHours(new Date(),
								-mobileVerifyTimesLimitCycleInHours));

		Date now = new Date();
		// 验证此次发送验证短信的时间距离上次发送的时间是不是太短
		if (tokenList != null
				&& tokenList.size() > 0
				&& tokenList.get(0).getCreateTime() != null
				&& new Date().before(DateUtils.addMinutes(tokenList.get(0)
						.getCreateTime(), minMobileVerifyIntervalInMinutes))) {

			LOGGER
					.debug(
							"[{}] applyfor verify SMS too frequently,verify interval must be greate than {} minutes",
							mobile, minEmailVerifyIntervalInMinutes);

			int retryAfterInSeconds = (int) (now.getTime() - tokenList.get(0)
					.getCreateTime().getTime()) / 1000;

			throw new TooShortVerifyIntervalException(retryAfterInSeconds);
		}
		// 验证是否发送了过多的验证短信
		if (tokenList != null && tokenList.size() >= maxMobileVerifyTimes) {
			LOGGER
					.debug(
							"[{}] applyfor verify SMS too many times in past {} hours,must be less than {} times.",
							new Object[] { mobile,
									mobileVerifyTimesLimitCycleInHours,
									maxMobileVerifyTimes });

			Date shouldBeRetryTime = DateUtils.addHours(tokenList.get(0)
					.getCreateTime(), 24);

			int retryAfterInSeconds = (int) (shouldBeRetryTime.getTime() - tokenList
					.get(0).getCreateTime().getTime()) / 1000;

			throw new AccountVerifyOutnumberException(retryAfterInSeconds);
		}
		Token token = new Token();
		token.setId(generateRandomSMSToken());
		token.setAccountId(accountId);
		token.setCreateTime(new Date());
		token.setExpiredTime(DateUtils.addHours(token.getCreateTime(),
				emailTokenLifeInHour));

		token.setRefer(mobile);
		tokenDao.createToken(token);
		sendSMSService.sendVerifyAccountSMS(mobile, token.getId());
		LOGGER.debug("{} send verify sms finished.", mobile);
	}

	private void applyforVeriyEmail(String accountId, String email,
			String invite)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException {

		// if (dao.getByLoginName(email) != null) {
		// throw new IllegalArgumentException("email " + email + " is exists");
		// }
		List<Token> tokenList = tokenDao
				.listByReferAndCreateTime(email, DateUtils
						.addHours(new Date(),
								-emailVerifyTimesLimitCycleInHours));

		// 验证此次发送验证邮件的时间距离上次发送的时间是不是太短
		Date now = new Date();

		if (tokenList != null
				&& tokenList.size() > 0
				&& tokenList.get(0).getCreateTime() != null
				&& now.before(DateUtils.addMinutes(tokenList.get(0)
						.getCreateTime(), minEmailVerifyIntervalInMinutes))) {

			LOGGER
					.debug(
							"[{}] applyfor verify email too frequently,verify interval must be greate than {} minutes",
							email, minEmailVerifyIntervalInMinutes);

			int retryAfterInSeconds = (int) (now.getTime() - tokenList.get(0)
					.getCreateTime().getTime()) / 1000;

			throw new TooShortVerifyIntervalException(retryAfterInSeconds);
		}
		// 验证是否发送了过多的验证邮件
		if (tokenList != null && tokenList.size() >= maxEmailVerifyTimes) {
			Date shouldBeRetryTime = DateUtils.addHours(tokenList.get(0)
					.getCreateTime(), 24);

			int retryAfterInSeconds = (int) (shouldBeRetryTime.getTime() - tokenList
					.get(0).getCreateTime().getTime()) / 1000;

			LOGGER
					.debug(
							"[{}] applyfor verify email too many times in past {} hours,must be less than {} times.",
							new Object[] { email,
									emailVerifyTimesLimitCycleInHours,
									maxEmailVerifyTimes });

			throw new AccountVerifyOutnumberException(retryAfterInSeconds);
		}
		Token token = new Token();
		token.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		token.setAccountId(accountId);
		token.setCreateTime(new Date());
		token.setExpiredTime(DateUtils.addHours(token.getCreateTime(),
				emailTokenLifeInHour));

		token.setRefer(email);
		tokenDao.createToken(token);
		sendEmailService.sendVerifyAccountEmail(email, token, invite);
		LOGGER.debug("{} send verify email finished.", email);
	}

	private String getUniqueName(String nickName) {
		Random random = new Random();
		StringBuilder b = new StringBuilder(nickName);

		for (int i = 0; i < 4; i++) {
			b.append(random.nextInt(10));
		}
		return b.toString();
	}

	private void saveWithUniqueName(Account account, int times)
			throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException {

		try {
			save(account);

		} catch (DuplicateNameException e1) {
			times -= 1;

			if (times >= 0) {
				account.setName(getUniqueName(account.getName()));
				saveWithUniqueName(account, times);

			} else {
				throw e1;
			}
		}
	}

	Token verifyAccount(String tokenIdentifier, String reffer,
			boolean isMobileVerify) {

		Token token = tokenDao.findById(tokenIdentifier);

		if (token == null) {
			LOGGER.debug("can't found any verify token with id {}",
					tokenIdentifier);

			return null;
		}

		// 检查token是否过期
		if (token.getExpiredTime() != null
				&& token.getExpiredTime().before(new Date())) {

			LOGGER.debug("token {} is expired,it's expiredTime is {}",
					tokenIdentifier, token.getExpiredTime());

			return null;
		}

		if (!token.getRefer().equals(reffer)) {
			LOGGER.debug("token {} is error",
					tokenIdentifier);

			return null;
		}

		// if (getByLoginName(reffer) != null) {
		// LOGGER.debug("token {} is used",
		// tokenIdentifier);
		//
		// return null;
		// }

		// 检查使用的是不是最后一次发送的token
		Token lastToken = tokenDao.findLastTokenByRefer(token.getRefer());

		if (lastToken != null && !tokenIdentifier.equals(lastToken.getId())) {
			LOGGER.debug("token {} is not fresh", tokenIdentifier);
			return null;
		}
		return token;
	}

	@Override
	public void applyforVerifySMS(String mobile, Map<String, String> attchment)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException {

		applyforVerifySMS("", mobile);
	}

	@Override
	public void applyforVeriyEmail(Map<String, String> attchment, String email)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException {

		String token = "";
		String inviteCode = "";

		if (attchment != null) {
			token = attchment.get("token");
			inviteCode = attchment.get("inviteCode");
		}
		applyforVeriyEmail(token, email, inviteCode);
	}

	@Override
	public long countByDuoduoEmail(String email) {

		return dao.countByDuoduoEmail(email);
	}

	@Override
	public Settings createDefaultSettings(String account) {
		Settings s = new Settings();
		Sharing sharing = new Sharing();
		FileUploadSettings uploadSetting = new FileUploadSettings();
		Limitation l = new Limitation();
		l.setDailyQuota(50);
		l.setMaxFileLength(Long.valueOf(10485760));
		sharing.setDefaultGroup(FriendGroup.Me.toString());
		uploadSetting.setLimitation(l);
		s.setFileUpload(uploadSetting);
		s.setSharing(sharing);
		s.setAccount(account);
		s.setId(new ObjectId().toString());
		saveSettings(s);
		return s;
	}

	@Override
	public boolean delete(String id) {
		dao.delete(id);
		return true;
	}

	public ThirdPartyProfile doAuthCallback(AccountType weibo, String queryStr) {
		return authClient.doCallback(weibo.toString(), queryStr);
	}

	@Override
	public List<Account> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Account> findByIdList(List<String> ids) {
		return dao.findByAccountIdList(ids);
	}

	@Override
	public List<Account> findByName(String name) {
		return dao.findByName(name.replace("*", "\\*").replace(")", "\\)")
				.replace("(", "\\(").replace("^", "\\^").replace("$", "\\$")
				.replace(".", "\\."));
	}

	public AuthClient getAuthClient() {
		return authClient;
	}

	@Override
	public String getAuthLocation(String consumer, String state,
			DisplayType type) {

		return authClient.getAuthLocation(consumer, state, type);
	}

	@Override
	public Account getByAccountName(String domain) {
		return dao.getByAccountName(domain);
	}

	@Override
	public Account getByDuoduoEmail(String mail) {
		return dao.getByDuoduoEmail(mail);
	}

	@Override
	public Account getByEmail(String email) {
		return dao.getByEmail(email);
	}

	@Override
	public Account getById(String id) {
		return dao.getById(id);
	}

	@Override
	public Account getByLoginName(String loginName) {
		return dao.getByLoginName(loginName);
	}

	@Override
	public Account getByName(String name) {
		return dao.getByName(name);
	}

	public int getEmailTokenLifeInHour() {
		return emailTokenLifeInHour;
	}

	public int getEmailVerifyTimesLimitCycleInHours() {
		return emailVerifyTimesLimitCycleInHours;
	}

	@Override
	public AccountMailConfig getMailConfig(String accountId) {
		AccountMailConfig ac = dao.getMailConfig(accountId);
		if (ac != null)
			return ac;

		Account a = dao.getById(accountId);
		if (a == null || a.getEmail() == null)
			return null;

		ac = new AccountMailConfig();
		ac.setFriendsVisible(true);
		ac.setInTags(null);
		List<String> mails = new ArrayList<String>();
		mails.add(a.getEmail());
		ac.setMails(mails);
		return ac;
	}

	public int getMaxEmailVerifyTimes() {
		return maxEmailVerifyTimes;
	}

	public int getMaxMobileVerifyTimes() {
		return maxMobileVerifyTimes;
	}

	public int getMinEmailVerifyIntervalInMinutes() {
		return minEmailVerifyIntervalInMinutes;
	}

	public int getMinMobileVerifyIntervalInMinutes() {
		return minMobileVerifyIntervalInMinutes;
	}

	public int getMobileTokenLifeInMinutes() {
		return mobileTokenLifeInMinutes;
	}

	public int getMobileVerifyTimesLimitCycleInHours() {
		return mobileVerifyTimesLimitCycleInHours;
	}

	@Override
	public Account getRecommendAccount() {
		if (recommendAccountName == null || recommendAccountName.equals(""))
			return null;
		return dao.getByLoginName(recommendAccountName);
	}

	// public ThirdPartyProfile getThirdPartyProfile(String accountId) {
	//
	// if (StringUtils.isBlank(accountId)) {
	// return null;
	// }
	// return thirdProfileDao.findByAccountId(accountId);
	// }

	public String getRecommendAccountName() {
		return recommendAccountName;
	}

	@Override
	public Settings getSettings(String account) {
		Settings s = settingsDao.getSettings(account);

		if (s != null) {
			s.getFileUpload().getLimitation()
					.setWhitelist(documentService.getWhitlist());

		}
		return s;
	}

	@Override
	public Settings getSettings(String account, boolean create)
			throws AccountNotExistException {

		Settings settings = getSettings(account);

		if (settings != null) {
			return settings;
		}

		if (getById(account) != null) {
			Settings s = createDefaultSettings(account);

			if (s != null) {
				s.getFileUpload().getLimitation()
						.setWhitelist(documentService.getWhitlist());

			}
			return s;
		}
		throw new AccountNotExistException();
	}

	public ThirdPartyProfileDao getThirdProfileDao() {
		return thirdProfileDao;
	}

	@Override
	public void handlerMessage(String accountId, MessageType binding,
			CollapseKey key, Map<String, String> map) {

		messageService.deleteForAttribute(accountId, binding, map);
		notificationService.deleteOneByAccountIdAndKey(accountId,
				key);
	}

	@Override
	public void notifyUser(Account account) {
		sendEmailService.sendWelcomeEmail(account);
	}

	@Override
	public boolean removeRole(Account account, RoleType roleType) {

		if (account.hasRole(roleType)) {
			account.removeRole(roleType);
			Account newAccount = new Account();
			newAccount.setId(account.getId());
			newAccount.setRole(account.getRole());
			return dao.updateAccount(account) != null;
		}
		return false;
	}

	@Override
	public boolean save(Account u) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException {

		if (StringUtils.isBlank(u.getId())) {
			u.addRole(RoleType.NewBie);
		}
		dao.save(u);
		return u.getId() != null;
	}

	public void saveProfile(ThirdPartyProfile profile) {
		thirdProfileDao.save(profile);
	}

	@Override
	public void saveSettings(Settings s) {
		settingsDao.save(s);
	}

	@Override
	public void saveUserAgent(UserAgentLog log) {
		userAgentLogDao.save(log);
	}

	@Override
	// TODO: 最多尝试2次生成随机数昵称
	public void saveWithUniqueName(Account account)
			throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException {

		saveWithUniqueName(account, 2);
	}

	@Override
	public void sendEvent(ApplicationEvent e) {
		applicationContext.publishEvent(e);
	}

	public void setAccountDao(AccountDao accountdao) {
		this.dao = accountdao;
	}

	public void setAuthClient(AuthClient authClient) {
		this.authClient = authClient;
	}

	public void setEmailTokenLifeInHour(int emailTokenLifeInHour) {
		this.emailTokenLifeInHour = emailTokenLifeInHour;
	}

	public void setEmailVerifyTimesLimitCycleInHours(
			int emailVerifyTimesLimitCycleInHours) {
		this.emailVerifyTimesLimitCycleInHours = emailVerifyTimesLimitCycleInHours;
	}

	public void setMaxEmailVerifyTimes(int maxEmailVerifyTimes) {
		this.maxEmailVerifyTimes = maxEmailVerifyTimes;
	}

	public void setMaxMobileVerifyTimes(int maxMobileVerifyTimes) {
		this.maxMobileVerifyTimes = maxMobileVerifyTimes;
	}

	public void setMinEmailVerifyIntervalInMinutes(
			int minEmailVerifyIntervalInMinutes) {
		this.minEmailVerifyIntervalInMinutes = minEmailVerifyIntervalInMinutes;
	}

	public void setMinMobileVerifyIntervalInMinutes(
			int minMobileVerifyIntervalInMinutes) {
		this.minMobileVerifyIntervalInMinutes = minMobileVerifyIntervalInMinutes;
	}

	public void setMobileTokenLifeInMinutes(int mobileTokenLifeInMinutes) {
		this.mobileTokenLifeInMinutes = mobileTokenLifeInMinutes;
	}

	public void setMobileVerifyTimesLimitCycleInHours(
			int mobileVerifyTimesLimitCycleInHours) {
		this.mobileVerifyTimesLimitCycleInHours = mobileVerifyTimesLimitCycleInHours;
	}

	public void setRecommendAccountName(String recommendAccountName) {
		this.recommendAccountName = recommendAccountName;
	}

	public void setSendEmailService(SendEmailService sendEmailService) {
		this.sendEmailService = sendEmailService;
	}

	public void setSendSMSService(SendSMSService sendSMSService) {
		this.sendSMSService = sendSMSService;
	}

	public void setThirdProfileDao(ThirdPartyProfileDao thirdProfileDao) {
		this.thirdProfileDao = thirdProfileDao;
	}

	public void setTokenDao(TokenDao tokenDao) {
		this.tokenDao = tokenDao;
	}

	@Override
	public Account updateAccount(Account account)
			throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException {

		return dao.updateAccount(account);
	}

	@Override
	public Token verifyAccountByEmail(String tokenIdentifier, String reffer) {
		return verifyAccount(tokenIdentifier, reffer, false);
	}

	@Override
	public Token verifyAccountByMobile(
			String tokenIdentifier, String reffer) {

		return verifyAccount(tokenIdentifier, reffer, true);
	}
	
	@Override
	public List<Account> getPublicAccounts() {
		return dao.getPublicAccounts();
	}

	@Override
	public Account getRecommendPublicAccount() {
		List<Account> publicAccounts = getPublicAccounts();
		Account recommendAccount = null;
		int friendsNum = Integer.MAX_VALUE;
		
		if (publicAccounts != null && publicAccounts.size() > 0) {
			
			for (Account thePublicAccount : publicAccounts) {
				int tempFriendsNum = friendService.countByAccountId(thePublicAccount.getId());
				LOGGER.debug("public account {} friends number {} ", thePublicAccount.getId(), tempFriendsNum);
				
				if (tempFriendsNum < friendsNum) {
					friendsNum = tempFriendsNum;
					recommendAccount = thePublicAccount;
				}
			}
		}
		return recommendAccount;
	}
	
	@Override
	public Account getPublicAccountById(String id) {
		return dao.getPublicAccountById(id);
	}
}
