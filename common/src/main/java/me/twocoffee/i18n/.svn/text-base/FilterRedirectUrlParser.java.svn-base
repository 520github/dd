/**
 * 
 */
package me.twocoffee.i18n;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 
 * 
 * @author momo
 * 
 */
public class FilterRedirectUrlParser {

	public static String getI18nUrl(String url) {

		if (isStaticResource(url)) {
			Locale locale = LocaleContextHolder.getLocale();
			String spChar = "";
			StringBuilder builder = new StringBuilder("/");
			builder.append(locale.toString());

			if (!url.startsWith("/")) {
				spChar = "/";
			}
			return builder.append(spChar).append(url).toString();
		}
		return url;
	}

	private static boolean isStaticResource(String url) {
		return url.endsWith(".html") ? true : false;
	}
}
