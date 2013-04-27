package me.twocoffee.service.impl;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.TokenDao;
import me.twocoffee.entity.Token;
import me.twocoffee.exception.AccountVerifyOutnumberException;
import me.twocoffee.exception.TooShortVerifyIntervalException;
import me.twocoffee.service.SendEmailService;

import org.apache.commons.lang.time.DateUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lisong
 * 
 */
public class AccountServiceImlTest {
	private IMocksControl control;
	private AccountServiceImpl service;
	private TokenDao tokenDao;
	private AccountDao accountDao;
	private SendEmailService sendEmailService;

	private void verifyAccount验证Token不存在的情况(boolean isMobileVerify) {

		// 录制tokenDao.findById
		Capture<String> tokenIdCapture = new Capture();
		tokenDao.findById(capture(tokenIdCapture));
		expectLastCall().andReturn(null);

		// 开始播放
		control.replay();
		String tokenId = "testTokenIdentifier";
		Token result = service.verifyAccount(tokenId, "",
				isMobileVerify);

		// 验证预期结果
		control.verify();
		Assert.assertNull(result);
		assertEquals(tokenId, tokenIdCapture.getValue());

	}

	private void verifyAccount验证Token不是最新的情况(boolean isMobileVerify) {
		// 录制tokenDao.findById
		Capture<String> tokenIdCapture = new Capture<String>();
		tokenDao.findById(capture(tokenIdCapture));

		String testTokenId = "testTokenId";
		Token token = new Token();
		token.setId(testTokenId);
		token.setExpiredTime(DateUtils.addMinutes(new Date(), 2));
		token.setRefer("testRefer");
		expectLastCall().andReturn(token);

		// Capture<String> loginNameCapture = new Capture<String>();
		// service.getByLoginName(capture(loginNameCapture));
		// expectLastCall().andReturn(null);
		// 录制tokenDao.findLastTokenByRefer
		Capture<String> referCapture = new Capture<String>();
		tokenDao.findLastTokenByRefer(capture(referCapture));

		Token lastToken = new Token();
		lastToken.setId(testTokenId + "diff");
		expectLastCall().andReturn(lastToken);

		// 播放
		control.replay();
		Token result = service.verifyAccount(testTokenId, "testRefer",
				isMobileVerify);

		// 验证
		control.verify();
		Assert.assertNull(result);
		assertEquals(testTokenId, tokenIdCapture.getValue());
		assertEquals(token.getRefer(), referCapture.getValue());
	}

	private void verifyAccount验证Token过期的情况(boolean isMobileVerify) {
		// 录制
		Capture<String> tokenIdCapture = new Capture<String>();
		tokenDao.findById(capture(tokenIdCapture));

		String testTokenId = "testTokenId";
		Token expiredToken = new Token();
		expiredToken.setId(testTokenId);
		expiredToken.setExpiredTime(DateUtils.addMinutes(new Date(), -1));

		expectLastCall().andReturn(expiredToken);

		// 播放
		control.replay();
		Token result = service.verifyAccount(testTokenId, "",
				isMobileVerify);

		// 验证预期结果
		control.verify();
		Assert.assertNull(result);
		assertEquals(testTokenId, tokenIdCapture.getValue());

	}

	private void verifyAccount验证正常认证的情况(boolean isMobileVerify) {

		// 录制tokenDao.findById
		Capture<String> tokenIdCapture = new Capture<String>();
		tokenDao.findById(capture(tokenIdCapture));

		String testTokenId = "testTokenId";
		Token token = new Token();
		token.setId(testTokenId);
		token.setExpiredTime(DateUtils.addMinutes(new Date(), 2));
		token.setRefer("testRefer");
		expectLastCall().andReturn(token);
		// Capture<String> loginNameCapture = new Capture<String>();
		// service.getByLoginName(capture(loginNameCapture));
		// expectLastCall().andReturn(null);
		// 录制tokenDao.findLastTokenByRefer
		Capture<String> referCapture = new Capture<String>();
		tokenDao.findLastTokenByRefer(capture(referCapture));

		Token lastToken = new Token();
		lastToken.setId(testTokenId);
		expectLastCall().andReturn(lastToken);

		// 录制accountDao.updateMobileVerifiedById或accountDao.updateEmailVerifiedById
		// Capture<String> accountIdCapture = new Capture<String>();
		// if (isMobileVerify) {
		// accountDao
		// .updateMobileVerifiedById(capture(accountIdCapture),
		// EasyMock.anyBoolean());
		// } else {
		// accountDao.updateEmailVerifiedById(capture(accountIdCapture),
		// EasyMock.anyBoolean());
		// }

		// 播放
		control.replay();
		Token result = service.verifyAccount(testTokenId, "testRefer",
				isMobileVerify);

		// 验证
		control.verify();
		Assert.assertNotNull(result);
		assertEquals(testTokenId, tokenIdCapture.getValue());
		assertEquals(token.getRefer(), referCapture.getValue());
	}

	@Test(expected = TooShortVerifyIntervalException.class)
	public void applyforVeriyEmail验证两次发送时间间隔太短的情况()
			throws TooShortVerifyIntervalException {

		service.setMinEmailVerifyIntervalInMinutes(5);

		// 录制
		Capture<String> referCapture = new Capture<String>(CaptureType.ALL);

		// accountDao.getByLoginName(capture(referCapture));
		// expectLastCall().andReturn(null);

		tokenDao.listByReferAndCreateTime(capture(referCapture),
				isA(Date.class));

		Token token = new Token();
		token.setCreateTime(DateUtils.addMinutes(new Date(), -4));
		List<Token> tokenList = new ArrayList<Token>();
		tokenList.add(token);

		EasyMock.expectLastCall().andReturn(tokenList);

		// 回放
		control.replay();

		String email = "testEmail";
		try {
			service.applyforVeriyEmail(null, email);

		} catch (TooShortVerifyIntervalException e) {
			// 验证
			control.verify();
			for (String value : referCapture.getValues()) {
				Assert.assertEquals(email, value);
			}

			throw e;
		} catch (AccountVerifyOutnumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(expected = AccountVerifyOutnumberException.class)
	public void applyforVeriyEmail验证一段时间内发送次数过多的情况()
			throws AccountVerifyOutnumberException {
		service.setMaxEmailVerifyTimes(2);
		service.setMinEmailVerifyIntervalInMinutes(5);
		service.setEmailVerifyTimesLimitCycleInHours(24);

		Capture<String> referCapture = new Capture<String>(CaptureType.ALL);

		// accountDao.getByLoginName(capture(referCapture));
		// expectLastCall().andReturn(null);

		// 录制tokenDao.listByReferAndCreateTime
		tokenDao.listByReferAndCreateTime(capture(referCapture),
				isA(Date.class));

		Token tokenOne = new Token();
		tokenOne.setCreateTime(DateUtils.addMinutes(new Date(), -6));

		Token tokenTwo = new Token();
		tokenTwo.setCreateTime(DateUtils.addMinutes(new Date(), -16));

		Token tokenThree = new Token();
		tokenThree.setCreateTime(DateUtils.addMinutes(new Date(), -23 * 60));

		List<Token> tokenList = new ArrayList<Token>();
		tokenList.add(tokenOne);
		tokenList.add(tokenTwo);
		tokenList.add(tokenThree);

		EasyMock.expectLastCall().andReturn(tokenList);

		// 回放
		control.replay();

		String email = "testEmail";
		try {
			service.applyforVeriyEmail(null, email);
		} catch (TooShortVerifyIntervalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountVerifyOutnumberException e) {
			// 验证
			control.verify();
			for (String value : referCapture.getValues()) {
				Assert.assertEquals(email, value);
			}

			throw e;
		}
	}

	@Test
	public void applyforVeriyEmail正常情况() {

		service.setMaxEmailVerifyTimes(2);
		service.setMinEmailVerifyIntervalInMinutes(5);

		Capture<String> referCapture = new Capture<String>(CaptureType.ALL);
		// accountDao.getByLoginName(capture(referCapture));
		// expectLastCall().andReturn(null);

		// 录制tokenDao.listByReferAndCreateTime

		tokenDao.listByReferAndCreateTime(capture(referCapture),
				isA(Date.class));

		Token tokenOne = new Token();
		tokenOne.setCreateTime(DateUtils.addMinutes(new Date(), -10));

		List<Token> tokenList = new ArrayList<Token>();
		tokenList.add(tokenOne);

		EasyMock.expectLastCall().andReturn(tokenList);

		// 录制tokenDao.createToken
		Token newToken = new Token();
		newToken.setId("testTokenId");

		Capture<Token> newTokenCapture = new Capture<Token>();
		tokenDao.createToken(capture(newTokenCapture));

		Capture<String> emailCapture = new Capture<String>();
		Capture<String> inviteCapture = new Capture<String>();
		Capture<String> tokenIDCapture = new Capture<String>();
		sendEmailService.sendVerifyAccountEmail(capture(emailCapture),
				capture(newTokenCapture), capture(inviteCapture));

		// 回放
		control.replay();

		String email = "testEmail";
		try {
			service.applyforVeriyEmail(null, email);
		} catch (TooShortVerifyIntervalException e) {
			e.printStackTrace();
		} catch (AccountVerifyOutnumberException e) {
			e.printStackTrace();
		}

		// 验证
		control.verify();

		for (String value : referCapture.getValues()) {
			Assert.assertEquals(email, value);
		}
		assertEquals(email, emailCapture.getValue());
		// assertEquals(newTokenCapture.getValue().getId(), tokenIDCapture
		// .getValue());
	}

	@Before
	public void before() {
		control = EasyMock.createControl();

		service = new AccountServiceImpl();
		service.setTokenDao(tokenDao = control.createMock(TokenDao.class));
		service.setSendEmailService(sendEmailService = control
				.createMock(SendEmailService.class));
		service
				.setAccountDao(accountDao = control
						.createMock(AccountDao.class));

		control.resetToStrict();
	}

	@Test
	public void verifyAccount验证EmailToken不存在的情况() {
		verifyAccount验证Token不存在的情况(false);
	}

	@Test
	public void verifyAccount验证EmailToken不是最新的情况() {
		verifyAccount验证Token不是最新的情况(false);
	}

	@Test
	public void verifyAccount验证EmailToken过期的情况() {
		verifyAccount验证Token过期的情况(false);
	}

	@Test
	public void verifyAccount验证MobileToken不存在的情况() {
		verifyAccount验证Token不存在的情况(true);
	}

	@Test
	public void verifyAccount验证MobileToken不是最新的情况() {
		verifyAccount验证Token不是最新的情况(true);
	}

	@Test
	public void verifyAccount验证MobileToken过期的情况() {
		verifyAccount验证Token过期的情况(true);
	}

	@Test
	public void verifyAccount验证正常使用email认证的情况() {
		verifyAccount验证正常认证的情况(false);
	}

	@Test
	public void verifyAccount验证正常使用手机认证的情况() {
		verifyAccount验证正常认证的情况(true);
	}
}
