package me.twocoffee.service;

public interface SendSMSService {
	/**
	 * 发送验证账户的短息
	 * 
	 * @param mobile
	 * @param token
	 */
	public void sendVerifyAccountSMS(String mobile, String token);

	public void sendInvitation(String moble, String code, String name);
}
