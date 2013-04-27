package me.twocoffee.common.article;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.lexer.Page;

public class ContextUtils {
	private final static Map<String, Matcher> matcherMap = new HashMap<String, Matcher>();
	public final static String REGEX_DIGITAL = "[\t]|[\r]|[\n]|[ ]|[\\d]";// 匹配空格、tab、换行、回车、数字
	public final static String REGEX_Html_ESC = "(&[^(&|;)]+;)+";// 匹配html转义符的正则(&copy;：版权符号除外)
	public final static String REGEX_HTML_HIDDEN_STYLE = "display:none|visibility:hidden";// 网页隐藏属性
	public final static String REGEX_LETTER = "[\u4e00-\u9fa5a-zA-Z]+";// 匹配文字（汉字或英文）
	public final static String REGEX_SPACE = "[\t]|[\r]|[\n]|[ ]";// 匹配空格、tab、换行、回车
	public final static String REPLACEMENT = "_";
	public static Map<String, String> attriMap = new HashMap<String, String>();
	static {
		attriMap.put("src", "");
		attriMap.put("href", "");
		attriMap.put("title", "");
		attriMap.put("alt", "");
		attriMap.put("width", "");
		attriMap.put("height", "");
	}

	public static Matcher getMatcher(String pattern, String input) {
		if (pattern == null) {
			throw new IllegalArgumentException(
					"String 'pattern' must not be null");
		}
		Matcher m = matcherMap.get(pattern);
		if (m != null) {
			m.reset(input);
		} else {
			m = Pattern.compile(pattern).matcher(input);
			matcherMap.put(pattern, m);
		}
		return m;
	}

	// 截取结点字串
	public static String nodeString(Node node) {
		return subString(node.getPage(), node.getStartPosition(),
				node.getEndPosition() - node.getStartPosition());
	}

	/**
	 * 截取从位置start开始，长度为length的字串
	 * 
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subString(Page page, int start, double length) {
		String value = null;
		try {
			value = page.getSource().getString(start, (int) length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

}
