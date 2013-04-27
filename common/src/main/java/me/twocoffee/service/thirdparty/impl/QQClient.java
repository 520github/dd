/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.util.List;

import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.service.entity.ThirdPartyPostMessage;

import org.springframework.stereotype.Component;

/**
 * @author wenjian
 * 
 */
@Component("qqClient")
public class QQClient extends AbstractThridpartyClient {

    @Override
    public List<Contact> getFriends(ThirdPartyProfile profile) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<ThirdPartyContent> getContents(ThirdPartyProfile profile) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void broadcast(ThirdPartyProfile profile,
	    ThirdPartyPostMessage content) {
	// TODO Auto-generated method stub

    }

    @Override
    public void share(ThirdPartyProfile profile, ThirdPartyPostMessage content) {
	// TODO Auto-generated method stub

    }

    @Override
    public void invite(ThirdPartyProfile profile, ThirdPartyPostMessage content) {
	// TODO Auto-generated method stub

    }

}
