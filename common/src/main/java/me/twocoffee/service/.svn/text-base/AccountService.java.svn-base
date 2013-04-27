package me.twocoffee.service;

import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.LoginToken.DisplayType;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.entity.Settings;
import me.twocoffee.entity.Token;
import me.twocoffee.entity.UserAgentLog;
import me.twocoffee.exception.AccountNotExistException;
import me.twocoffee.exception.AccountVerifyOutnumberException;
import me.twocoffee.exception.DuplicateCoffeemailException;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;
import me.twocoffee.exception.TooShortVerifyIntervalException;

import org.springframework.context.ApplicationEvent;

//TODO:SNS 删除方法doAuthCallback; getByThirdParty;getThirdPartyProfile;saveProfile
public interface AccountService {

	void applyforVerifySMS(String mobile, Map<String, String> attchment)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException;

	void applyforVeriyEmail(Map<String, String> attchment, String email)
			throws TooShortVerifyIntervalException,
			AccountVerifyOutnumberException;

	long countByDuoduoEmail(String email);

	Settings createDefaultSettings(String account)
			throws AccountNotExistException;

	// ThirdPartyProfile doAuthCallback(AccountType weibo, String queryStr);

	boolean delete(String id);

	List<Account> findAll();

	/**
	 * 从id的列表得到账户信息
	 * 
	 * @param ids
	 * @return
	 */
	List<Account> findByIdList(List<String> ids);

	List<Account> findByName(String name);

	/**
	 * 获得oauth协议 授权页面url
	 * 
	 * @param <DisplayType>
	 * 
	 * @param consumer
	 *            平台名称
	 * @param web
	 * @param web
	 * @return
	 */
	String getAuthLocation(String consumer, String state,
			DisplayType type);

	Account getByAccountName(String domain);

	/**
	 * 得到多多邮箱
	 * 
	 * @param mail
	 * @return
	 */
	Account getByDuoduoEmail(String mail);

	Account getByEmail(String email);

	Account getById(String id);

	Account getByLoginName(String loginName);

	/**
	 * 获取使用第三方帐号的用户
	 * 
	 * @param type
	 *            第三方类型
	 * @param userId
	 *            第三方标识
	 * @return
	 */
	// Account getByThirdParty(AccountType type, String userId);

	Account getByName(String name);

	/**
	 * 得到邮箱设置
	 * 
	 * @param accountId
	 * @return
	 */
	AccountMailConfig getMailConfig(String accountId);

	// ThirdPartyProfile getThirdPartyProfile(String accountId);

	/**
	 * 得到注册通知帐号
	 * 
	 * @return
	 */
	Account getRecommendAccount();

	void handlerMessage(String accountId, MessageType binding, CollapseKey key,
			Map<String, String> attributes);

	// void saveProfile(ThirdPartyProfile profile);

	void notifyUser(Account au);

	boolean save(Account u) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException;

	void saveWithUniqueName(Account account) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException;

    /**
     * 发送事件。 应该是EventService 事件应该是一个独立的事件服务
     * 
     * @param e
     */
    @Deprecated
    void sendEvent(ApplicationEvent e);

	/**
	 * 更新Account
	 * 
	 * @param account
	 * @return 新存入的实体
	 * @throws DuplicateNameException
	 * @throws DuplicateLoginNameException
	 * @throws DuplicateCoffeemailException
	 */
	Account updateAccount(Account account) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException;

	/**
	 * 根据邮件对帐号进行验证
	 * 
	 * @param tokenIdentifier
	 *            验证码
	 * @param reffer
	 *            邮件地址
	 * @return
	 */
	Token verifyAccountByEmail(String tokenIdentifier, String reffer);

	/**
	 * 根据短信对帐号进行验证
	 * 
	 * @param tokenIdentifier
	 *            验证码
	 * @param reffer
	 *            手机号
	 * @return
	 */
	Token verifyAccountByMobile(String tokenIdentifier, String reffer);

	public Settings getSettings(String account);

	/**
	 * 获取settings。根据create参数设置，如果没有settings则创建。
	 * 
	 * @param account
	 * @param create
	 * @return
	 */
	public Settings getSettings(String account, boolean create)
			throws AccountNotExistException;

	/**
	 * 删除指定用户 的指定角色。操作成功返回true；如果用户没有该角色，返回false
	 * 
	 * @param account
	 * @param roleType
	 * @return
	 */
	public boolean removeRole(Account account, RoleType roleType);

	public void saveSettings(Settings s);

	public void saveUserAgent(UserAgentLog log);

	List<Account> getPublicAccounts();

	Account getRecommendPublicAccount();
	
	Account getPublicAccountById(String id);

}