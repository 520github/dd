package me.twocoffee.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieTool {
	public static void setCookie(HttpServletResponse response, String name, String value, int expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
		if (domain != null) {
			cookie.setDomain(domain);
		}
		
		response.addCookie(cookie);
	}
	
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] ks = request.getCookies();
		if (ks == null)
			return null;
		
		for (Cookie k : ks) {
			if (k.getName().equals(name))
				return k.getValue();
		}
		
		return null;
	}
}
