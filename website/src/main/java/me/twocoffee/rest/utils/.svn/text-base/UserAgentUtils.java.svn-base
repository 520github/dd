/**
 * 
 */
package me.twocoffee.rest.utils;

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
		Duoduo_client_browser,
		Duoduo_ios,
		Duoduo_android
	}

	public static ClientInfo getClientInfo(String agent) {

		if (agent == null || "".equals(agent)) {
			return new ClientInfo("", ClientType.Duoduo);
		}
		
		if (agent.equalsIgnoreCase(ClientType.Duoduo_client_browser.toString())) {
			return new ClientInfo(InfoConverter.getVersionFromUserAgent(agent),
					ClientType.Duoduo_client_browser);
		}
		
		if (agent.toLowerCase().indexOf("iphone") >= 0) {
			return new ClientInfo(InfoConverter.getVersionFromUserAgent(agent),
					ClientType.Duoduo_ios);
		}

		if (agent.toLowerCase().indexOf("android") >= 0) {
			return new ClientInfo(InfoConverter.getVersionFromUserAgent(agent),
					ClientType.Duoduo_android);
		}
		return new ClientInfo("", ClientType.Duoduo);
	}
	
	public static boolean isClient(ClientInfo clientInfo) {
		boolean isClient = false;
		if(ClientType.Duoduo_ios.equals(clientInfo.clientType) 
				|| ClientType.Duoduo_android.equals(clientInfo.clientType)
				|| ClientType.Duoduo_client_browser.equals(clientInfo.clientType)) {
			isClient = true;
		}
		return isClient;
	}
}
