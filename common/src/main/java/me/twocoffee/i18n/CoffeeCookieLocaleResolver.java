/**
 * 
 */
package me.twocoffee.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * custom localeResolver. only return zh_CN or en_US.
 * 
 * @author momo
 * 
 */
public class CoffeeCookieLocaleResolver extends CookieLocaleResolver {

	private Locale default_zh = new Locale("zh", "CN");

	private Locale default_en = new Locale("en", "US");

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = super.resolveLocale(request);

		if ("CN".equals(locale.getCountry())) {
			return default_zh;
		}
		return default_zh;
	}

}
