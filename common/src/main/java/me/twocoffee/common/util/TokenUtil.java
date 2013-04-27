package me.twocoffee.common.util;

import java.text.ParseException;
import java.util.Date;
import javax.ws.rs.core.Response;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.WebToken;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.WebTokenService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenUtil {
	public static final String AUTH_TOKEN = "AuthToken";
	public static final String WEB_TOKEN = "WebToken";
	public static final String CLIENT_ID = "ClientId";
	public static final String ACCOUNT_ID = "AccountId";//支持第三方平台直接接入，如微信公共账号
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthTokenService authTokenService;
	@Autowired
	private WebTokenService webTokenService;
	
	public String validAuthorizationInController(String authorization) {
		String valid = "";
		int code = getAuthorizationErrorCode(authorization);
		if(code > 200) {
			valid = "redirect:/home";
		}
		return valid;
	}
	
	public String validAuthorizationAndGuestInController(String authorization) {
		String valid = "";
		int code = getGuestAuthorizationErrorCode(authorization,true);
		if(code > 200) {
			valid = "redirect:/home";
		}
		return valid;
	}
	
	/**
	 * 获取校验authorization的错误代码
	 * @param authorization
	 * @return
	 */
	public int getAuthorizationErrorCode(String authorization) {
		return this.getGuestAuthorizationErrorCode(authorization, false);
	}
	
	public int getGuestAuthorizationErrorCode(String authorization,boolean checkGuestUser) {
		int code = 200;
		if(isEmptyAuthorization(authorization)) {
			code = 401;
		}
		if(!isEmptyAuthToken(authorization)) {
			AuthToken authToken = authTokenService.findById(extractAuthToken(authorization));
			if(authToken == null) {
				code = 403;
			}
		}
		else if(!isEmptyWebToken(authorization)) {
			WebToken webToken = webTokenService.findById(extractWebToken(authorization));
			if(webToken == null) {
				code = 403;
			}
			//is Guest user
			else if(checkGuestUser && StringUtils.isBlank(webToken.getAccountId())) {
				code = 410;
			}
		}
		else if(!isEmptyClientId(authorization)) {
			Device device = deviceService.getById(extractClientId(authorization));
			if(device == null) {
				code = 403;
			}
			//is Guest user
			else if(checkGuestUser && StringUtils.isBlank(device.getAccountId())) {
				code = 410;
			}
		}
		else if(!isEmptyAccountId(authorization)) {
			String accountId = extractAccoutnId(authorization);
			Account account = accountService.getById(accountId);
			if(account == null) {
				code = 403;
			}
		}
		return code;
	}
	
	/**
	 * 校验authorization是否是合法有效
	 * @param authorization
	 * @return
	 */
	public boolean isInvalidAuthorization(String authorization) {
		if(isEmptyAuthorization(authorization)) {
			return true;
		}
		if(!isEmptyAuthToken(authorization)) {
			AuthToken authToken = authTokenService.findById(extractAuthToken(authorization));
			if(authToken == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断authorization是否为空
	 * @param authorization
	 * @return
	 */
	public static boolean isEmptyAuthorization(String authorization) {
		boolean isEmpty = true;
		if(!isEmptyAuthToken(authorization)) {
			isEmpty = false;
		}
		if(isEmpty && !isEmptyWebToken(authorization)) {
			isEmpty = false;
		}
		if(isEmpty && !isEmptyClientId(authorization)) {
			isEmpty = false;
		}
		if(isEmpty && !isEmptyAccountId(authorization)) {
			isEmpty = false;
		}
		
		return isEmpty;
	}
	
	/**
	 * 判断是否是匿名用户
	 * @param authorization
	 * @return
	 */
	public boolean isGuestUser(String authorization) {
		boolean isGuest = false;
		if(!isEmptyWebToken(authorization)) {
			WebToken webToken = webTokenService.findById(extractWebToken(authorization));
			if(webToken == null || StringUtils.isBlank(webToken.getAccountId())) {
				isGuest = true;
			}
		}
		else if(!isEmptyClientId(authorization)) {
			Device device = deviceService.getById(extractClientId(authorization));
			if(device == null || StringUtils.isBlank(device.getAccountId())) {
				isGuest = true;
			}
		}
		else if(!isEmptyAccountId(authorization)) {
			String accountId = extractAccoutnId(authorization);
			Account account = accountService.getById(accountId);
			if(account == null) {
				isGuest = true;
			}
		}
		
		return isGuest;
	}
	
	public String getAccountIdOrGuestId(String authorization) {
		String userId = null;
		if(!isEmptyAccountId(authorization)) {
			userId = extractAccoutnId(authorization);
		}
		else if(this.isGuestUser(authorization)) {
			userId = this.getGuestUserId(authorization);
		}
		else {
			Account account = this.getLoginUserAccontByAuthorization(authorization);
			if(account != null)userId = account.getId();
		}
		
		return userId;
	}
	
	public static String getWebTokenAuthorization(String webToken) {
		if(webToken == null) {
			return null;
		}
		if(webToken.indexOf(ACCOUNT_ID) >-1) {
			return webToken;
		}
		if(webToken.indexOf(WEB_TOKEN) >-1) {
			return webToken;
		}
		webToken = WEB_TOKEN + " " +webToken;
		return webToken;
	}
	
	public String getGuestUserId(String authorization) {
		String guestUserId = TokenUtil.extractWebToken(authorization);
		if(guestUserId == null || guestUserId.trim().length() < 1) {
			guestUserId = TokenUtil.extractClientId(authorization);
		}
		return guestUserId;
	}
	
	/**
	 * 根据authorization获取登录账户信息
	 * @param authorization
	 * @return
	 */
	public Account getLoginUserAccontByAuthorization(String authorization) {
		String token = null;
		if(!isEmptyAuthToken(authorization)) {//AuthToken for before version
			token = extractAuthToken(authorization);
			AuthToken authToken = authTokenService.findById(token);
			if(authToken != null && !StringUtils.isBlank(authToken.getAccountId()))
			return accountService.getById(authToken.getAccountId());
		}
		else if(!isEmptyWebToken(authorization)) {//for server
			token = extractWebToken(authorization);
			WebToken webToken = webTokenService.findById(token);
			if(webToken != null && !StringUtils.isBlank(webToken.getAccountId())) {
				return accountService.getById(webToken.getAccountId());
			}
		}
		else if(!isEmptyClientId(authorization)) {//for client
			token = extractClientId(authorization);
			Device device = deviceService.getById(token);
			if(device != null && !StringUtils.isBlank(device.getAccountId())) {
				return accountService.getById(device.getAccountId());
			}
		}
		else if(!isEmptyAccountId(authorization)) {
			String accountId = extractAccoutnId(authorization);
			return accountService.getById(accountId);
		}
		return null;
	}
	
	/**
	 * 请求内容未发生改变
	 * @param modifiedSince
	 * @param account
	 * @return
	 */
	public Response getNotMondifiedResponse(String modifiedSince,Account account) {
		if (StringUtils.isBlank(modifiedSince) || account == null) {
			return null;
		}
    	try {
    		Date sinceDate = DateUtil.parseRFC1123Date(modifiedSince);
    		if (!sinceDate
    			.before(account.getLastModified() == null ? new Date(): account.getLastModified())) {
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
	    }
    	return null;
    }
	
	public static String extractAuthToken(String token) {
		return token == null || !token.startsWith(AUTH_TOKEN) ? null : token.replace(AUTH_TOKEN+" ", "").trim();
	}
	
	public static boolean isEmptyAuthToken(String token) {
		token  = extractAuthToken(token);
		return token == null || token.trim().length()<1 ? true : false;
	}
	
	public static String extractWebToken(String token) {
		return token == null || !token.startsWith(WEB_TOKEN) ? null : token.replace(WEB_TOKEN+" ", "").trim();
	}
	
	public static boolean isEmptyWebToken(String token) {
		token  = extractWebToken(token);
		return token == null || token.trim().length()<1 ? true : false;
	}
	
	public static String extractClientId(String token) {
		return token == null || !token.startsWith(CLIENT_ID) ? null : token.replace(CLIENT_ID+" ", "").trim();
	}
	
	public static boolean isEmptyClientId(String token) {
		token  = extractClientId(token);
		return token == null || token.trim().length()<1 ? true : false;
	}
	
	public static String extractAccoutnId(String token) {
		return token == null || !token.startsWith(ACCOUNT_ID) ? null : token.replace(ACCOUNT_ID+" ", "").trim();
	}
	
	public static boolean isEmptyAccountId(String token) {
		token  = extractAccoutnId(token);
		return token == null || token.trim().length()<1 ? true : false;
	}
}
