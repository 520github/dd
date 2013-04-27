package me.twocoffee.service.rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.User;
import me.twocoffee.exception.RegisterFaildException;
import me.twocoffee.exception.TooManyVerifyRequestException;
import me.twocoffee.exception.UpdateAccountFailedException;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.AccountPerfictInfo;
import me.twocoffee.rest.entity.AccountUpdateInfo;
import me.twocoffee.rest.utils.InfoConverter;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceHttpImpl extends AbstractHttpRPCService implements
		AccountRpcService {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(AccountServiceHttpImpl.class);

	private static final String RETRY_AFTER_HEADER = "Retry-After";

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

	private void applyVerify(String authToken, String verifyType,
			String verifyAddress, String invite)
			throws
			TooManyVerifyRequestException {

		HttpPost httpPost = new HttpPost("/service/accounts/verify/"
				+ verifyType);

		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		JSONObject postData = new JSONObject();
		postData.put(verifyType, verifyAddress);
		Map<String, String> attchMent = new HashMap<String, String>();
		attchMent.put("inviteCode", invite);
		attchMent.put("token", authToken);
		postData.put("attachment", attchMent);

		try {
			httpPost
					.setEntity(new StringEntity(postData.toString(), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();

			if (responseCode == 409) {
				LOGGER.debug("can not found {}", verifyAddress);
				throw new IllegalArgumentException("can not found {} "
						+ verifyAddress);

			} else if (responseCode == 429) {
				int retryAfterInSeconds = 60;

				try {
					retryAfterInSeconds = Integer.parseInt(httpResponse
							.getFirstHeader(RETRY_AFTER_HEADER).getValue());

				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				LOGGER.debug("[{}] too many request send verify {}",
						verifyAddress, verifyType);

				throw new TooManyVerifyRequestException(retryAfterInSeconds);

			} else if (responseCode != 204) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpPost.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity()) });

				throw new RuntimeException();
			}

		} catch (IllegalArgumentException e) {
			throw e;

		} catch (TooManyVerifyRequestException e) {
			throw e;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException();

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPost.abort();
		}
	}

	@Override
	public void applyVerifyEmail(String email, String authToken, String invite)
			throws
			TooManyVerifyRequestException {

		try {
			applyVerify(authToken, "email", email, invite);

		} catch (TooManyVerifyRequestException e) {
			e.setMaxVerifyTimes(maxEmailVerifyTimes);
			e.setMinVerifyIntervalInMinutes(minEmailVerifyIntervalInMinutes);
			e.setVerifyTimesLimitCycleInHours(emailVerifyTimesLimitCycleInHours);
			throw e;
		}
	}

	@Override
	public void applyVerifyMobile(String mobile, String authToken, String invite)
			throws
			TooManyVerifyRequestException {

		try {
			applyVerify(authToken, "mobile", mobile, invite);

		} catch (TooManyVerifyRequestException e) {
			e.setMaxVerifyTimes(maxMobileVerifyTimes);
			e.setMinVerifyIntervalInMinutes(minMobileVerifyIntervalInMinutes);
			e.setVerifyTimesLimitCycleInHours(mobileVerifyTimesLimitCycleInHours);
			throw e;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AccountWithToken auth(String account, String password, String webToken) {
		HttpPost httpPost = new HttpPost("/service/accounts/default/auth");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		httpPost.setHeader("Authorization", TokenUtil.getWebTokenAuthorization(webToken));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("account", account);
		jsonObject.put("password", password);

		try {
			httpPost
					.setEntity(new StringEntity(jsonObject.toString(),
							DEFAULT_CHARSET));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} ****** auth failed", account);
				return null;
			}

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpPost.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });

				return null;
			}
			String httpResponseStr = EntityUtils.toString(httpResponse
					.getEntity(), DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpPost.getRequestLine().getUri().toString(),
					httpResponseStr);

			Map<String, Object> result = objectMapper.readValue(
					httpResponseStr, new TypeReference<Map<String, Object>>() {
					});

			AccountWithToken accountWithToken = new AccountWithToken();
			accountWithToken.setAccount(InfoConverter
					.convertToAccount(objectMapper.convertValue(
							result.get("account"), AccountInfo.class)));

			accountWithToken.setAuthToken((String) result.get("authToken"));
			return accountWithToken;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPost.abort();
		}
	}

	@Override
	public AccountWithToken authForSmtp(String account, String password) {
		HttpPost httpPost = new HttpPost("/service/accounts/smtp/auth");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("account", account);
		jsonObject.put("password", password);

		try {
			httpPost
					.setEntity(new StringEntity(jsonObject.toString(),
							DEFAULT_CHARSET));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} ****** auth failed", account);
				return null;
			}

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpPost.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });

				return null;
			}
			String httpResponseStr = EntityUtils.toString(httpResponse
					.getEntity(), DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpPost.getRequestLine().getUri().toString(),
					httpResponseStr);

			Map<String, Object> result = objectMapper.readValue(
					httpResponseStr, new TypeReference<Map<String, Object>>() {
					});

			AccountWithToken accountWithToken = new AccountWithToken();
			accountWithToken.setAccount(InfoConverter
					.convertToAccount(objectMapper.convertValue(
							result.get("account"), AccountInfo.class)));

			accountWithToken.setAuthToken((String) result.get("authToken"));
			return accountWithToken;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPost.abort();
		}
	}

	@Override
	public boolean checkDomain(String domain) {
		HttpGet httpGet = new HttpGet("/service/accounts/domain/" + domain);
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 404) {
				return false;

			} else {
				return true;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return true;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@Override
	public boolean checkName(String name) {
		HttpGet httpGet = null;
		HttpResponse httpResponse = null;
		try {
			httpGet = new HttpGet("/service/accounts/name/"
					+ URLEncoder.encode(name, "utf-8"));
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 404) {
				return false;

			} else {
				return true;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return true;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (httpGet != null) {
				httpGet.abort();
			}
		}
	}

	public int getEmailVerifyTimesLimitCycleInHours() {
		return emailVerifyTimesLimitCycleInHours;
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

	public int getMobileVerifyTimesLimitCycleInHours() {
		return mobileVerifyTimesLimitCycleInHours;
	}

	@Override
	public Account getUserProfile(String token) {
		HttpGet httpGet = new HttpGet("/service/accounts/profile");
		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return null;
			}

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpGet.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });

				return null;
			}
			String responseStr = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpGet.getRequestLine().getUri().toString(), responseStr);

			return InfoConverter.convertToAccount(objectMapper.readValue(
					responseStr, AccountInfo.class));

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long perfectInformation(AccountPerfictInfo user, String authToken)
			throws UpdateAccountFailedException {

		HttpPut httpPut = new HttpPut("/service/accounts/basic-profile");
		httpPut.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		httpPut.setHeader("Authorization", this.getWebTokenAuthorization(authToken));
		JSONObject jsonObject = JSONObject.fromObject(user);

		try {
			httpPut.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPut);
			switch (httpResponse.getStatusLine().getStatusCode()) {
			case 204:
				return 204;

			case 409:
				Map<String, Object> responseEntity = objectMapper.readValue(
						EntityUtils.toString(httpResponse
								.getEntity()),
						new TypeReference<Map<String, Object>>() {
						});

				Map<String, String> fieldErrors = (Map<String, String>) responseEntity
						.get("fieldError");

				LOGGER.debug(
						"update account(basic-profile) failed with error code 409 and fieldErrors {}",
						fieldErrors);

				throw new UpdateAccountFailedException(httpResponse
						.getStatusLine()
						.getStatusCode(), fieldErrors);

			default:
				throw new UpdateAccountFailedException(httpResponse
						.getStatusLine()
						.getStatusCode(), null);

			}

		} catch (UpdateAccountFailedException e) {
			throw e;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPut.abort();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long postInformation(AccountUpdateInfo user, String authToken)
			throws UpdateAccountFailedException {

		HttpPut httpPut = new HttpPut("/service/accounts/profile");
		httpPut.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		httpPut.setHeader("Authorization", this.getWebTokenAuthorization(authToken));
		JSONObject jsonObject = JSONObject.fromObject(user);

		try {
			httpPut.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPut);

			switch (httpResponse.getStatusLine().getStatusCode()) {
			case 204:
				return 204;

			case 409:
				Map<String, Object> responseEntity = objectMapper.readValue(
						EntityUtils.toString(httpResponse
								.getEntity()),
						new TypeReference<Map<String, Object>>() {
						});

				Map<String, String> fieldErrors = (Map<String, String>) responseEntity
						.get("fieldError");

				LOGGER.debug(
						"update account failed with error code 409 and fieldErrors {}",
						fieldErrors);

				throw new UpdateAccountFailedException(httpResponse
						.getStatusLine()
						.getStatusCode(), fieldErrors);

			default:
				throw new UpdateAccountFailedException(httpResponse
						.getStatusLine()
						.getStatusCode(), null);

			}

		} catch (UpdateAccountFailedException e) {
			throw e;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPut.abort();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AccountWithToken register(User user, String authorization)
			throws RegisterFaildException {

		HttpPost httpPost = new HttpPost("/service/accounts/default");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		httpPost.setHeader("Authorization", this.getWebTokenAuthorization(authorization));
		JSONObject jsonObject = JSONObject.fromObject(user);

		try {
			httpPost
					.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 409) {
				Map<String, Object> responseEntity = objectMapper.readValue(
						EntityUtils.toString(httpResponse
								.getEntity()),
						new TypeReference<Map<String, Object>>() {
						});

				Map<String, String> fieldErrors = (Map<String, String>) responseEntity
						.get("fieldError");

				LOGGER.debug(
						"register failed with error code 409 and fieldErrors {}",
						fieldErrors);

				throw new RegisterFaildException(httpResponse.getStatusLine()
						.getStatusCode(), fieldErrors);

			}

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpPost.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity()) });

				throw new Exception();
			}
			String httpResponseStr = EntityUtils.toString(httpResponse
					.getEntity());

			LOGGER.debug("http get request to {} ok,response is {}",
					httpPost.getRequestLine().getUri().toString(),
					httpResponseStr);

			Map<String, Object> result = objectMapper.readValue(
					httpResponseStr, new TypeReference<Map<String, Object>>() {
					});

			AccountInfo accountInfo = objectMapper.convertValue(
					result.get("account"), AccountInfo.class);

			AccountWithToken accountWithToken = new AccountWithToken();
			accountWithToken.setAccount(InfoConverter
					.convertToAccount(accountInfo));

			accountWithToken.setAuthToken((String) result.get("authToken"));
			return accountWithToken;

		} catch (RegisterFaildException e) {
			throw e;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RegisterFaildException(httpResponse.getStatusLine()
					.getStatusCode(), null);

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPost.abort();
		}
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

	public void setMobileVerifyTimesLimitCycleInHours(
			int mobileVerifyTimesLimitCycleInHours) {

		this.mobileVerifyTimesLimitCycleInHours = mobileVerifyTimesLimitCycleInHours;
	}

	@Override
	public int verifyAccountByMobile(String mobileCode, String mobile) {
		HttpGet get = new HttpGet("/service/accounts/verify/mobile/"
				+ mobile + "/" + mobileCode);

		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(get);
			return httpResponse.getStatusLine().getStatusCode();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			get.abort();
		}
	}
}
