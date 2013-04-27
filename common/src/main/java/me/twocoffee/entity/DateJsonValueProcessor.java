package me.twocoffee.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.twocoffee.common.util.DateUtil;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private DateFormat dateFormat;

	public DateJsonValueProcessor() {
		dateFormat = null;
	}

	/**
	 * 构造方法.
	 * 
	 * @param datePattern
	 *            日期格式
	 */
	public DateJsonValueProcessor(String datePattern) {
		try {
			dateFormat = new SimpleDateFormat(datePattern);
		} catch (Exception ex) {
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		}
	}

	private Object process(Object value) {

		if (value == null)
			return null;

		if (dateFormat == null) {
			return DateUtil.FormatDateUTC((Date) value);
		}
		return dateFormat.format((Date) value);
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}
}
