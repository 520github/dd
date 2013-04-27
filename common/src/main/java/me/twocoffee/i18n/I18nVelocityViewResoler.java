package me.twocoffee.i18n;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * custom VelocityViewResolver. return view with i18n url.
 * 
 * @author momo
 * 
 */
public class I18nVelocityViewResoler extends VelocityViewResolver {

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		View view = super.createView(viewName, locale);

		if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
			RedirectView rv = (RedirectView) view;
			rv.setUrl(getI18nRedirctUrl(rv.getUrl()));
		}
		return view;
	}

	private String getI18nRedirctUrl(String url) {

		if (isStaticResource(url)) {
			return getI18nUrl(url, true);
		}
		return url;
	}

	private boolean isStaticResource(String url) {
		return url.endsWith(".html") || url.contains(".html?") ? true : false;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		view.setUrl(getI18nUrl(view.getUrl()));
		return view;
	}

	@Override
	protected Object getCacheKey(String viewName, Locale locale) {
		return viewName + "_" + locale;
	}

	private String getI18nUrl(String url) {
		Locale locale = LocaleContextHolder.getLocale();
		String spChar = "";
		StringBuilder builder = new StringBuilder(locale.toString());

		if (!url.startsWith("/")) {
			spChar = "/";
		}
		return builder.append(spChar).append(url).toString();
	}

	private String getI18nUrl(String url, boolean fromRoot) {
		Locale locale = LocaleContextHolder.getLocale();
		String spChar = "";
		String base;

		if (fromRoot) {
			base = "/" + locale.toString();

		} else {
			base = locale.toString();
		}
		StringBuilder builder = new StringBuilder(base);

		if (!url.startsWith("/")) {
			spChar = "/";
		}
		return builder.append(spChar).append(url).toString();
	}

}
