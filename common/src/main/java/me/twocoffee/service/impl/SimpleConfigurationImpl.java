/**
 * 
 */
package me.twocoffee.service.impl;

import me.twocoffee.service.ConfigurationService;

/**
 * @author wenjian
 *
 */
public class SimpleConfigurationImpl implements ConfigurationService {
	
	private String showFriendsViewFlag;
	
	private String latestFetchDate;

	public void setLatestFetchDate(String latestFetchDate) {
		this.latestFetchDate = latestFetchDate;
	}

	@Override
	public String getShowFriendsViewFlag() {
		return showFriendsViewFlag;
	}

	public void setShowFriendsViewFlag(String showFriendsViewFlag) {
		this.showFriendsViewFlag = showFriendsViewFlag;
	}

	@Override
	public String getLatestFetchDate() {
		return latestFetchDate;
	}

}
