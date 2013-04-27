/**
 * 
 */
package me.twocoffee.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author momo
 * 
 */
public class Pinyin4jUtils {

    private static final Logger logger = LoggerFactory
	    .getLogger(Pinyin4jUtils.class);

    public static String getPinYin(String inputString) {

	if (StringUtils.isBlank(inputString)) {
	    return "";
	}
	HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	format.setVCharType(HanyuPinyinVCharType.WITH_V);
	char[] input = inputString.trim().toCharArray();
	StringBuffer output = new StringBuffer("");

	for (int i = 0; i < input.length; i++) {

	    try {

		if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
		    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
			    input[i], format);

		    output.append(temp[0]);
		    // output.append(" ");

		} else {
		    output.append(Character.toString(input[i]));
		}

	    } catch (Exception e) {
		logger.error("can not get pinyin from[" + input[i] + "]");
	    }
	}
	logger.debug("from [" + inputString + "] to [" + output.toString()
		+ "]");

	return output.toString();
    }
}
