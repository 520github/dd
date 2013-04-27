package me.twocoffee.common;

import java.io.File;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;

public class SpringContext implements ApplicationContextAware {

	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {

		context = ac;
	}

	private static ApplicationContext context = null;

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public static Object getBeanByClass(Class clz) {
		return getApplicationContext().getBean(clz);
	}

	protected static ApplicationContext getApplicationContext() {
		return context;
	}

	public static String getRealPath(String file) {
		ApplicationContext ac = getApplicationContext();
		if (ac instanceof WebApplicationContext) {
			WebApplicationContext wc = (WebApplicationContext) getApplicationContext();
			if (wc == null)
				return file;
	
			return wc.getServletContext().getRealPath(file);
		}
		
		String path = System.getProperty("user.dir");
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		return path + file;
	}

	public static String getMessage(String key, Object[] args, Locale locale) {
		return context.getMessage(key, args, locale);
	}

	/**
	 * get message by locale from request context.
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public static String getMessageByRequestLocale(String key, Object[] args) {
		return context.getMessage(key, args, LocaleContextHolder.getLocale());
	}

	public static Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}

}
