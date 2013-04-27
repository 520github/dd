package me.twocoffee.mail.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import net.sf.json.JSONObject;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.FriendDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.entity.SystemTagEnum;

public class AccountService extends AbstractHttpRPCService {
	private AccountDao accountDao = null;
	private FriendDao friendDao = null;
	private String globalAuthPassword;
	
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractHttpRPCService.class);
	
	public AccountService(AccountDao dao, FriendDao fd, String pwd) {
		accountDao = dao;
		globalAuthPassword = pwd;
		friendDao = fd;
	}

	public Account getByDuoduoEmail(String mail) {
		
		return accountDao.getByDuoduoEmail(mail);
	}

	public AccountMailConfig getMailConfig(String accountId) {
		Account a = accountDao.getById(accountId);
		if (a == null)
			return null;
		
		if (a.getMailConfig() != null)
			return a.getMailConfig();
		
		if (a.getEmail() == null)
			return null;
		
		return getDefaultMailConfig(a);
	}

	private AccountMailConfig getDefaultMailConfig(Account a) {
		AccountMailConfig ac = new AccountMailConfig();
		ac.setFriendsVisible(true);
		List<String> tags = new ArrayList();
		tags.add(SystemTagEnum.Later.toString());
		ac.setInTags(tags);
		List<String> mails = new ArrayList();
		mails.add(a.getEmail());
		ac.setMails(mails);
		return ac;
	}

	public List<FriendMailConfig> findFriendMailByDuoduoMail(String mail) {
		List<FriendMailConfig> list = accountDao.findFriendMailConfigsByDuoduoMail(mail);
		if (list != null)
			return list;
		
		return getDefaultFriendMailConfigs(mail);
	}

	private List<String> createFriendIdList(List<Friend> list) {
		List<String> ids = new ArrayList();
		for (Friend f : list) {
			ids.add(f.getFriendId());
		}
		return ids;
	}
	
	private List<FriendMailConfig> getDefaultFriendMailConfigs(String mail) {
		Account a = accountDao.getByDuoduoEmail(mail);
		if (a == null)
			return null;
		
		List<Friend> fs = friendDao.findFriends(a.getId());
		if (fs == null)
			return null;
		
		List<String> ids = createFriendIdList(fs);
		List<Account> ass = accountDao.findByAccountIdList(ids);
		if (ass == null)
			return null;
		
		List<FriendMailConfig> fcs = new ArrayList();
		for (Account s : ass) {
			if (s.getMailConfig() == null) {
				s.setMailConfig(getDefaultMailConfig(s));
			}
			if (s.getMailConfig() == null)
				continue;
			
			FriendMailConfig fc = new FriendMailConfig();
			fc.setAccountId(a.getId());
			fc.setFriendId(s.getId());
			fc.setBlocked(false);
			fcs.add(fc);
		}
		
		return fcs;
	}

	public Account getAccountByFriendMail(List<FriendMailConfig> fs, String mail) {
		if (fs == null || fs.size() < 1)
			return null;
		
		for (FriendMailConfig f : fs) {
			if (f.isBlocked())
				continue;
			AccountMailConfig c = getMailConfig(f.getFriendId());
			if (c == null || c.getMails() == null || c.getMails().size() < 1)
				continue;
			for (String m : c.getMails()) {
				if (m.equals(mail))
					return accountDao.getById(f.getFriendId());
			}
		}
		return null;
	}
	
	public AccountWithToken auth(String account) {
		HttpPost httpPost = new HttpPost("/service/accounts/smtp/auth");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("account", account);
		jsonObject.put("password", globalAuthPassword);

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
}
