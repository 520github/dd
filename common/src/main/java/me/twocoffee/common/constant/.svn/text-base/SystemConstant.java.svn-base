/**
 * 
 */
package me.twocoffee.common.constant;

import me.twocoffee.common.SpringContext;

/**
 * @author xuehui.miao
 * 
 */
public class SystemConstant {
	public static String domainName = "www.mduoduo.com";
	public static String miniDomainName = "minisite.mduoduo.com";
	public static String mailSuffix = "mduoduo.com";
	public static String cookieDomain = ".mduoduo.com";
	public static String docDomainName = "user-content.mduoduo.com";

	static {
		try {
			domainName = SpringContext.getMessageByRequestLocale("domainName",
					null);
		} catch (Exception e) {

		}
		try {
			mailSuffix = SpringContext.getMessageByRequestLocale("mailSuffix",
					null);
		} catch (Exception e) {

		}
		try {
			cookieDomain = SpringContext.getMessageByRequestLocale(
					"cookieDomain", null);
		} catch (Exception e) {

		}
		try {
			miniDomainName = SpringContext.getMessageByRequestLocale(
					"miniDomainName", null);
		} catch (Exception e) {

		}
	}
}
