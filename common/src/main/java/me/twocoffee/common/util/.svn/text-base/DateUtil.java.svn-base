/**
 * 
 */
package me.twocoffee.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 日期处理类
 * 
 * @author xuehui.miao
 * 
 */
public class DateUtil extends DateFormatUtils {

	/**
	 * 获取日期格式化对象
	 * 
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = null;
		Locale locale = LocaleContextHolder.getLocale();

		if (pattern == null || pattern.trim().length() < 1) {
			Locale.setDefault(locale);
			sdf = new SimpleDateFormat();
		} else {
			sdf = new SimpleDateFormat(pattern, locale);
		}

		return sdf;
	}

	public static Date FormatDateStringUTC(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.parse(date);
	}

	public static String FormatDateUTC(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}

	public static String formatRFC1123(Date lastModified) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(lastModified);
	}

	public static String getBirthdayStr(String birthday) {
		String postfix = "T00:00:00Z";

		if (StringUtils.isNotBlank(birthday)) {

			if (!birthday.endsWith(postfix)) {
				birthday = birthday + postfix;
			}

		} else {
			birthday = "";
		}
		return birthday;
	}

	public static Date getTimestamp(Date date) {
		String timestamp = getTimestampStr(date);
		try {
			return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return date;
	}
	
	public static Date parse(String pattern, String dataStr) throws Exception {
		return getSimpleDateFormat(pattern).parse(dataStr);
	}

	public static String getTimestampStr(Date date) {
		return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static Date parseRFC1123Date(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.parse(date);
	}

	public static Date parseWeiboDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.US);

		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return sdf.parse(date);
	}

	public String formatUTC2String(String date) {
		try {
			return getTimestampStr(FormatDateStringUTC(date));
		} catch (Exception e) {
			return date;
		}
	}
	
	public static Date getBeginDate(Date date) {
		Date d = DateUtils.setHours(date, 0);
		d = DateUtils.setMinutes(d, 0);
		d = DateUtils.setSeconds(d, 0);
		return d;
	}
}
