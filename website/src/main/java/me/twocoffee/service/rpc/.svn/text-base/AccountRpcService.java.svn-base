package me.twocoffee.service.rpc;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.User;
import me.twocoffee.exception.RegisterFaildException;
import me.twocoffee.exception.TooManyVerifyRequestException;
import me.twocoffee.exception.UpdateAccountFailedException;
import me.twocoffee.rest.entity.AccountPerfictInfo;
import me.twocoffee.rest.entity.AccountUpdateInfo;

public interface AccountRpcService {

	/**
	 * @param email
	 * @param authToken
	 * @throws TooManyVerifyRequestException
	 */
	public void applyVerifyEmail(String email, String authToken, String invite)
			throws
			TooManyVerifyRequestException;

	/**
	 * @param mobile
	 * @param authToken
	 * @throws TooManyVerifyRequestException
	 */
	public void applyVerifyMobile(String mobile, String authToken, String invite)
			throws
			TooManyVerifyRequestException;

	/**
	 * @param account
	 * @param password
	 * @return
	 */
	public AccountWithToken auth(String account, String password,String webToken);

	public AccountWithToken authForSmtp(String account, String password);

	/**
	 * 
	 * @param domain
	 * @return
	 */
	public boolean checkDomain(String domain);

	/**
	 * @param name
	 * @return
	 */
	public boolean checkName(String name);

	/**
	 * @param token
	 * @return
	 */
	public Account getUserProfile(String token);

	/**
	 * 完善用户信息
	 * 
	 * @param user
	 * @param authToken
	 * @return
	 */
	public long perfectInformation(AccountPerfictInfo user, String authToken)
			throws UpdateAccountFailedException;

	/**
	 * 
	 * @param user
	 * @return
	 */
	public long postInformation(AccountUpdateInfo user, String authToken)
			throws UpdateAccountFailedException;

	/**
	 * @param user
	 * @param authorization
	 * @return
	 * @throws RegisterFaildException
	 */
	public AccountWithToken register(User user,String authorization)
			throws RegisterFaildException;

	/**
	 * 短信码验证
	 * 
	 * @param mobileCode
	 * @param mobile
	 * @return
	 */
	public int verifyAccountByMobile(String mobileCode, String mobile);
}