package me.twocoffee.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.controller.entity.AccountVO;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthUtil.class);
	private static final String COOKIE_AUTH_TOKEN = "authToken";
	private static final String COOKIE_MESSAGE = "sinceId";
	private static int authTokenMaxAgeByDay = 365;
	private static String authTokenCookieDomain = SystemConstant.cookieDomain;
	
	public static AccountVO covertToAccountVO(Account a) {
		AccountVO av = new AccountVO();
		if (a.getPhotos() != null) {
			av.setLargePhotoUrl(a.getPhotos().get(Account.PHOTO_SIZE_BIG));
			av.setMiddlePhotoUrl(a.getPhotos().get(Account.PHOTO_SIZE_MIDDLE));
			av.setSmallPhotoUrl(a.getPhotos().get(Account.PHOTO_SIZE_SMALL));
		}
		av.setCity(a.getCity());
		av.setGender(a.getGender());
		av.setId(a.getId());
		av.setIntroduce(a.getDescribe());
		av.setName(a.getName());
		av.setProvince(a.getProvince());
		av.setDomain(a.getAccountName());
		av.setDuoduoEmail(a.getDuoduoEmail());
		av.setMailConfig(a.getMailConfig());
		return av;
	}

	public static void deleteAuthToken(HttpServletResponse response,
			String authToken) {
		deleteCookie(response,COOKIE_AUTH_TOKEN,authToken);
	}
	
	public static void deleteWebToken(HttpServletResponse response,
			String authToken) {
		deleteCookie(response,TokenUtil.WEB_TOKEN,authToken);
	}
	
	
	public static void deleteCookie(HttpServletResponse response,
			String cookieName,String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setDomain(authTokenCookieDomain);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static String getAuthTokenFromCookie(HttpServletRequest request) {
		return getTokenFromCookie(request, COOKIE_AUTH_TOKEN);
	}
	
	public static String getWebTokenFromCookie(HttpServletRequest request) {
		return getTokenFromCookie(request, TokenUtil.WEB_TOKEN);
	}
	
	public static String getWebTokenAuthorization(HttpServletRequest request) {
		return TokenUtil.WEB_TOKEN + " " +getWebTokenFromCookie(request);
	}
	
	public static String getTokenFromCookie(HttpServletRequest request,String cookieName) {
		if(StringUtils.isBlank(cookieName)) {
			return null;
		}
		if (request.getCookies() == null || request.getCookies().length == 0) {
			LOGGER.debug("could't find any cookie.");
			return null;
		}

		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static AccountVO getUserInfo(TokenUtil tokenUtil, HttpServletRequest request) {
		Account account = tokenUtil.getLoginUserAccontByAuthorization(getWebTokenAuthorization(request));
		if (account == null) {
			return null;
		}
		return covertToAccountVO(account);
	}

	public static void writeAuthToken(HttpServletResponse response,
			String authToken, boolean isAutoLogin) {
		if(StringUtils.isEmpty(authToken)) {
			return;
		}
		
		Cookie cookie = new Cookie(COOKIE_AUTH_TOKEN, authToken);
		cookie.setDomain(authTokenCookieDomain);
		cookie.setPath("/");
		if (isAutoLogin) {
			cookie.setMaxAge(authTokenMaxAgeByDay * 3600 * 24);
		}
		response.addCookie(cookie);
	}
	
	public static void writeWebToken(HttpServletResponse response,
			String webToken) {
		writeCookie(response,TokenUtil.WEB_TOKEN,webToken,true);
	}

	public static void writeMessageToken(HttpServletResponse response,
			String message, boolean isAutoLogin) {

		Cookie cookie = new Cookie(COOKIE_MESSAGE, message);
		cookie.setDomain(authTokenCookieDomain);
		cookie.setPath("/");

		if (isAutoLogin) {
			cookie.setMaxAge(authTokenMaxAgeByDay * 3600 * 24);
		}
		response.addCookie(cookie);
	}
	
	public static void writeCookie(HttpServletResponse response,String cookieName,
			String cookieValue,boolean isAutoLogin) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setDomain(authTokenCookieDomain);
		cookie.setPath("/");
		if (isAutoLogin) {
			cookie.setMaxAge(authTokenMaxAgeByDay * 3600 * 24);
		}
		response.addCookie(cookie);
	}
}
