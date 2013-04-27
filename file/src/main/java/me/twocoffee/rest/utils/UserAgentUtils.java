/**
 * 
 */
package me.twocoffee.rest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author momo
 * 
 */
public class UserAgentUtils {
	public static class ClientInfo {

		private String version;

		private ClientType clientType;

		public ClientInfo(String version, ClientType clientType) {
			this.version = version;
			this.clientType = clientType;
		}

		public ClientType getClientType() {
			return clientType;
		}

		public String getVersion() {
			return version;
		}

		public void setClientType(ClientType clientType) {
			this.clientType = clientType;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	public enum ClientType {
		IE,
		Firefox,
		Chrome,
		Duoduo,
		Duoduo_ios,
		Duoduo_android
	}

	private static final String VERSION_PATTERN = "DuoDuo/([\\d\\.]+?)[\\s(]";

	private static Pattern VERSION = Pattern.compile(VERSION_PATTERN);

	public static ClientInfo getClientInfo(String agent) {

		if (agent == null || "".equals(agent)) {
			return new ClientInfo("", ClientType.Duoduo);
		}

		if (agent.toLowerCase().indexOf("iphone") >= 0) {
			return new ClientInfo(getVersionFromUserAgent(agent),
					ClientType.Duoduo_ios);
		}

		if (agent.toLowerCase().indexOf("android") >= 0) {
			return new ClientInfo(getVersionFromUserAgent(agent),
					ClientType.Duoduo_android);
		}
		return new ClientInfo("", ClientType.Duoduo);
	}

	public static String getVersionFromUserAgent(String ua) {

		if (StringUtils.isBlank(ua)) {
			return "";
		}
		Matcher m = VERSION.matcher(ua);

		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
