package me.twocoffee.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.twocoffee.controller.AuthUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.WebToken;
import me.twocoffee.i18n.FilterRedirectUrlParser;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.WebTokenService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AuthFilter implements Filter {

	protected String loginPage = "/index";
	private AccountService accountService;
	private AuthTokenService authTokenService;
	private WebTokenService webTokenService;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthFilter.class);
	private static String filterUserAgent[] = {"spider","DNSPod-monitor","Android","iphone","Webluke","libcurl","http:","https:","www.",".com",".info","@","Nutch","Google","larbin","urllib","findlinks","evc","Statsbot","QQDownload",
		                                       "WeikanBot","Apache","Yahoo","alexa","MJ12bot","ahrefs","bsalsa","yandex","netcraft","ezooms","siclab","Dataprovider","Genieo","Exabot"};
	 
	private static String filterServletPath[] = {"."};
	private static int userAgentMixLength = 12;
	
	protected boolean needLocalization = true;

	protected void showDomainView(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.sendRedirect("/account/default/domain");

	}

	protected void showLoginView(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {

		if (needLocalization) {
			response.sendRedirect(FilterRedirectUrlParser.getI18nUrl(loginPage));

		} else {
			response.sendRedirect(loginPage);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String userAgent = request.getHeader("User-Agent");
		boolean isFilter = this.isFilter(request.getServletPath(), userAgent);
		String webToken = AuthUtil.getWebTokenFromCookie(request);
		//not found webToken
		if(!isFilter && StringUtils.isBlank(webToken)) {
			WebToken wToken = webTokenService.createWebToken(userAgent,request.getServletPath());
			AuthUtil.writeWebToken(response, wToken.getId());
			
			//not found webToken,but found authToken
			String authToken = AuthUtil.getAuthTokenFromCookie(request);
			if (StringUtils.isNotBlank(authToken)) {
				LOGGER.debug("not found webToken,but found authToken[{}],now is redirect login page....",authToken);
				AuthUtil.deleteAuthToken(response, authToken);
				showLoginView(request, response);
				return;
			}
			response.sendRedirect(request.getServletPath());
			return;
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loginPage = filterConfig.getInitParameter("loginPage");
		String needLocalization = filterConfig
				.getInitParameter("needLocalization");

		if (StringUtils.isNotBlank(needLocalization)) {

			try {
				this.needLocalization = Boolean.valueOf(needLocalization);

			} catch (Exception e) {
				LOGGER
						.error("needLocalization parameter must true or false",
								e);

			}
		}
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());

		accountService = (AccountService) applicationContext
				.getBean("accountServiceImpl");

		authTokenService = (AuthTokenService) applicationContext
				.getBean("authTokenServiceImpl");
		
		webTokenService = (WebTokenService) applicationContext
		.getBean("webTokenServiceImpl");

	}
	
	private boolean isFilterString(String str,String filterArray[]) {
		boolean isFilter = false;
		if(StringUtils.isBlank(str)) {
			return isFilter;
		}
		if(filterArray == null || filterArray.length<1) {
			return isFilter;
		}
		str = str.toLowerCase();
		for (int i = 0; i < filterArray.length; i++) {
			String tempFilter = filterArray[i];
			tempFilter = StringUtils.trimToEmpty(tempFilter).toLowerCase();
			if(str.indexOf(tempFilter) > -1) {
				return true;
			}
		}
		
		return isFilter;
	}
	
	private boolean isFilter(String servletPath, String userAgent) {
		boolean isFilter = false;
		if(userAgent == null || userAgent.length() < userAgentMixLength) {
			return true;
		}
		isFilter = this.isFilterServletPath(servletPath);
		if(!isFilter) {
			isFilter = this.isFilterUserAgent(userAgent);
		}
		
		return isFilter;
	}
	
	private boolean isFilterUserAgent(String userAgent) {
		return isFilterString(userAgent,filterUserAgent);
	}
	
	private boolean isFilterServletPath(String servletPath) {
		return isFilterString(servletPath,filterServletPath);
	}
}
