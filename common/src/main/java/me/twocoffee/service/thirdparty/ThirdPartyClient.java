/**
 * 
 */
package me.twocoffee.service.thirdparty;

import java.util.List;

import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.service.entity.ThirdPartyPostMessage;

/**
 * @author wenjian
 * 
 */
public interface ThirdPartyClient {

	List<Contact> getFriends(ThirdPartyProfile profile);

	List<ThirdPartyContent> getContents(ThirdPartyProfile profile);
	
	String getContentDisplay(ThirdPartyContent content);
	
	String getSummaryDisplay(ThirdPartyContent content);

	void broadcast(ThirdPartyProfile profile, ThirdPartyPostMessage content);

	void share(ThirdPartyProfile profile, ThirdPartyPostMessage content);

	void invite(ThirdPartyProfile profile, ThirdPartyPostMessage content);

	public abstract int getFriendNumber(ThirdPartyProfile profile);

}
