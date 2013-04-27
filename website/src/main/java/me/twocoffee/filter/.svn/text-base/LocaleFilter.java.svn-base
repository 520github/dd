/**
 * 
 */
package me.twocoffee.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.twocoffee.i18n.FilterRedirectUrlParser;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.NestedServletException;

/**
 * @author momo
 * 
 */
public class LocaleFilter implements Filter {

    private LocaleResolver localeResolver;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	ApplicationContext applicationContext = WebApplicationContextUtils
		.getWebApplicationContext(filterConfig.getServletContext());

	localeResolver = (LocaleResolver) applicationContext
		.getBean("localeResolver");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {

	HttpServletRequest req = (HttpServletRequest) request;
	HttpServletResponse res = (HttpServletResponse) response;
	LocaleContext previousLocaleContext = LocaleContextHolder
		.getLocaleContext();

	LocaleContextHolder
		.setLocaleContext(buildLocaleContext(req));

	RequestHeaderHolder.set(req);

	try {

	    if ("get".equalsIgnoreCase(req.getMethod())
		    && !req.getRequestURI().startsWith(
			    "/service/content/archive")
		    && (req.getRequestURI().endsWith(".html") || req
			    .getRequestURI().indexOf(".html?") > 0)
		    && !req.getRequestURI().startsWith("/" +
			    LocaleContextHolder.getLocale().toString() + "/")) {

		StringBuilder url = new StringBuilder(req.getRequestURI());

		if (StringUtils.isNotBlank(req.getQueryString())) {
		    url.append("?").append(req.getQueryString());
		}
		res.sendRedirect(FilterRedirectUrlParser.getI18nUrl(url
			.toString()));

		return;
	    }
	    chain.doFilter(request, response);

	} catch (ServletException ex) {
	    throw ex;

	} catch (IOException ex) {
	    throw ex;

	} catch (Throwable ex) {
	    throw new NestedServletException("Request processing failed", ex);

	} finally {
	    // Clear request attributes and reset thread-bound context.
	    LocaleContextHolder.setLocaleContext(previousLocaleContext);
	    RequestHeaderHolder.reset();
	}
    }

    protected LocaleContext buildLocaleContext(final HttpServletRequest request) {
	return new LocaleContext() {

	    @Override
	    public Locale getLocale() {
		return localeResolver.resolveLocale(request);
	    }

	    @Override
	    public String toString() {
		return getLocale().toString();
	    }
	};
    }

    @Override
    public void destroy() {

    }

    public static class RequestHeaderHolder {

	private static final ThreadLocal<Map<String, Object>> requestHeaderThreadLocal = new ThreadLocal<Map<String, Object>>();

	public static void set(HttpServletRequest request) {
	    String userAgent = request.getHeader("User-Agent");
	    Map<String, Object> headers = new HashMap<String, Object>(1);
	    headers.put("User-Agent", userAgent);
	    requestHeaderThreadLocal.set(headers);
	}

	public static void reset() {
	    requestHeaderThreadLocal.remove();
	}

	public static Object get(String key) {

	    if (requestHeaderThreadLocal.get() == null) {
		return null;
	    }
	    return requestHeaderThreadLocal.get().get(key);
	}
    }
}
