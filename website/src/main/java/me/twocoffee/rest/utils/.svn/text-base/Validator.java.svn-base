/**
 * 
 */
package me.twocoffee.rest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author momo
 * 
 */
public class Validator {

	public static final String EMAIL_PATTERN = "^[0-9a-zA-Z_\\.-]*[0-9a-zA-Z_-]@[0-9a-zA-Z_-]+(\\.[0-9a-zA-Z_-]+)+$";

	public static final String MOBILE_PATTERN = "^(13[0-9]|15[0-9]|18[0-9])\\d{8}$";

	public static final String EMAILNAME_PATTERN = "^[0-9a-zA-Z_\\-\\.]*[0-9a-zA-Z_\\-]$";

	public static final String NAME_PATTERN = "^(-|\\w|[\\u4e00-\\u9fa5]){2,16}$";

	public static final String PASSWORD_PATTERN = "^.{6,16}$";

	private static Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);

	private static Pattern MOBILE = Pattern.compile(MOBILE_PATTERN);

	private static Pattern EMAILNAME = Pattern.compile(EMAILNAME_PATTERN);

	private static Pattern NAME = Pattern.compile(NAME_PATTERN);

	private static Pattern PASSWORD = Pattern.compile(PASSWORD_PATTERN);

	private static String[] GENDER = new String[] { "male", "female", "unkown" };

	public static boolean validateEmail(String input) {
		Matcher m = EMAIL.matcher(input);
		return m.find();
	}

	public static boolean validateEmailName(String input) {
		Matcher m = EMAILNAME.matcher(input);
		return m.find();
	}

	public static boolean validateGender(String gender) {

		for (String s : GENDER) {

			if (s.equals(gender)) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateMobile(String input) {
		Matcher m = MOBILE.matcher(input);
		return m.find();
	}

	public static boolean validateName(String input) {
		Matcher m = NAME.matcher(input);
		return m.find();
	}

	public static boolean validatePassword(String input) {
		Matcher m = PASSWORD.matcher(input);
		return m.find();
	}
}
