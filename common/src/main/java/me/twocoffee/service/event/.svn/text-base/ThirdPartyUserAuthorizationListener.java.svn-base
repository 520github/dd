/**
 * 
 */
package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.EventService;
import me.twocoffee.service.thirdparty.ThirdPartyContactMatcherService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class ThirdPartyUserAuthorizationListener implements
	ApplicationListener<ThirdPartyUserAuthorizationEvent> {

    static class FetchFriendHander implements Runnable {

	private ThirdPartyProfile thirdpartyProfile;

	private ThirdpartyService thirdpartyService;

	private ThirdPartyContactMatcherService thirdPartyContactService;

	private boolean first;

	public FetchFriendHander(ThirdPartyProfile thirdpartyProfile) {
	    this.thirdpartyProfile = thirdpartyProfile;
	}

	public ThirdPartyContactMatcherService getThirdPartyContactService() {
	    return thirdPartyContactService;
	}

	public ThirdPartyProfile getThirdpartyProfile() {
	    return thirdpartyProfile;
	}

	public ThirdpartyService getThirdpartyService() {
	    return thirdpartyService;
	}

	public boolean isFirst() {
	    return first;
	}

	@Override
	public void run() {
	    fetchFriend();
	}

	private void fetchFriend() {

	    try {
		List<Contact> contacts = thirdpartyService
			.getThirdPartyFriends(thirdpartyProfile);

		thirdPartyContactService.matchThirdPartyFriend(contacts,
			thirdpartyProfile.getAccountType(),
			thirdpartyProfile.getAccountId(), first);

	    } catch (ThirdPartyAuthExpiredException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);

		// thirdpartyProfile.setContactErrorType(ErrorType.Authexpired);
		// thirdpartyService.save(thirdpartyProfile);

	    } catch (ThirdPartyAccessException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);
		// thirdpartyProfile.setContactErrorType(ErrorType.AccessError);
		// thirdpartyService.save(thirdpartyProfile);

	    } catch (ThirdPartyUnbindException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);

		// thirdpartyProfile.setContactErrorType(ErrorType.Unbind);
		// thirdpartyService.save(thirdpartyProfile);
	    }
	}

	public void setFirst(boolean first) {
	    this.first = first;
	}

	public void setThirdPartyContactService(
		ThirdPartyContactMatcherService thirdPartyContactService) {

	    this.thirdPartyContactService = thirdPartyContactService;
	}

	public void setThirdpartyProfile(ThirdPartyProfile thirdpartyProfile) {
	    this.thirdpartyProfile = thirdpartyProfile;
	}

	public void setThirdpartyService(ThirdpartyService thirdpartyService) {
	    this.thirdpartyService = thirdpartyService;
	}
    }

    public static class FetchContentHander implements Runnable {

	private ThirdPartyProfile thirdpartyProfile;

	private ThirdpartyService thirdpartyService;

	private ThirdPartyContactMatcherService thirdPartyContactService;

	public FetchContentHander(ThirdPartyProfile thirdpartyProfile) {
	    this.thirdpartyProfile = thirdpartyProfile;
	}

	public ThirdPartyContactMatcherService getThirdPartyContactService() {
	    return thirdPartyContactService;
	}

	public ThirdPartyProfile getThirdpartyProfile() {
	    return thirdpartyProfile;
	}

	public ThirdpartyService getThirdpartyService() {
	    return thirdpartyService;
	}

	@Override
	public void run() {

	    try {
		thirdpartyService.syncThirdPartyContent(thirdpartyProfile);

	    } catch (ThirdPartyAuthExpiredException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);

		// thirdpartyProfile.setContentErrorType(ErrorType.Authexpired);
		// thirdpartyService.save(thirdpartyProfile);

	    } catch (ThirdPartyAccessException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);

		// thirdpartyProfile.setContentErrorType(ErrorType.AccessError);
		// thirdpartyService.save(thirdpartyProfile);

	    } catch (ThirdPartyUnbindException e) {
		LOGGER.error(
			ThirdPartyUserAuthorizationListener.class.getName(), e);

		// thirdpartyProfile.setContentErrorType(ErrorType.Unbind);
		// thirdpartyService.save(thirdpartyProfile);
	    }
	}

	public void setThirdPartyContactService(
		ThirdPartyContactMatcherService thirdPartyContactService) {

	    this.thirdPartyContactService = thirdPartyContactService;
	}

	public void setThirdpartyProfile(ThirdPartyProfile thirdpartyProfile) {
	    this.thirdpartyProfile = thirdpartyProfile;
	}

	public void setThirdpartyService(ThirdpartyService thirdpartyService) {
	    this.thirdpartyService = thirdpartyService;
	}
    }

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ThirdPartyUserAuthorizationListener.class);

    @Autowired
    private ThirdpartyService thirdpartyService;

    @Autowired
    private ThirdPartyContactMatcherService thirdPartyContactService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AccountService accountService;

    private List<ThirdPartyType> types = new ArrayList<ThirdPartyType>();

    public ThirdPartyUserAuthorizationListener() {
	types.add(ThirdPartyType.Renren);
	types.add(ThirdPartyType.Weibo);
	types.add(ThirdPartyType.Tencent);
    }

    @Override
    public void onApplicationEvent(ThirdPartyUserAuthorizationEvent event) {
	ThirdPartyProfile thirdpartyProfile = (ThirdPartyProfile) event
		.getSource();

	fetchThirdPartyContent(thirdpartyProfile);
	fetchThirdPartyFriends(event, thirdpartyProfile);

	if (event.isFirst()) {

	    if (thirdpartyProfile.isLogin()) {
		loginEventHandle(thirdpartyProfile);

	    } else {
		bindEventHandle(thirdpartyProfile);
	    }
	}

	if (event.isRefreshToken()) {
	    thirdpartyService.updateToken(thirdpartyProfile);
	}
    }

    private void loginEventHandle(ThirdPartyProfile thirdpartyProfile) {
	Account account = accountService.getById(thirdpartyProfile
		.getAccountId());

	ShowThirdPartyFriendsEvent phoneEvent = new ShowThirdPartyFriendsEvent(
		account, ThirdPartyType.Phone);

	eventService.send(phoneEvent);
	ShowThirdPartyFriendsEvent showThirdPartyFriendsEvent = new ShowThirdPartyFriendsEvent(
		thirdpartyProfile, thirdpartyProfile.getAccountType());

	eventService.send(showThirdPartyFriendsEvent);

	for (ThirdPartyType type : types) {

	    if (type != thirdpartyProfile.getAccountType()) {
		BindThirdPartyEvent bindThirdPartyEvent = new BindThirdPartyEvent(
			account, type);

		eventService.send(bindThirdPartyEvent);
	    }
	}
    }

    private void bindEventHandle(ThirdPartyProfile thirdpartyProfile) {
	Map<String, String> attributes = new HashMap<String, String>(1);
	attributes.put("thirdpartyType", thirdpartyProfile.getAccountType()
		.toString());

	accountService.handlerMessage(thirdpartyProfile.getAccountId(),
		Message.MessageType.Binding, CollapseKey.FriendMessage,
		attributes);

	ShowThirdPartyFriendsEvent e = new ShowThirdPartyFriendsEvent(
		thirdpartyProfile, thirdpartyProfile.getAccountType());

	eventService.send(e);
    }

    private void fetchThirdPartyFriends(ThirdPartyUserAuthorizationEvent event,
	    ThirdPartyProfile thirdpartyProfile) {

	FetchFriendHander friendHander = new FetchFriendHander(
		thirdpartyProfile);

	friendHander.setThirdPartyContactService(thirdPartyContactService);
	friendHander.setThirdpartyService(thirdpartyService);
	friendHander.setFirst(event.isFirst());
	Thread friendHandlerThread = new Thread(friendHander);
	friendHandlerThread.start();
    }

    private void fetchThirdPartyContent(ThirdPartyProfile thirdpartyProfile) {
	FetchContentHander contentHander = new FetchContentHander(
		thirdpartyProfile);

	contentHander.setThirdPartyContactService(thirdPartyContactService);
	contentHander.setThirdpartyService(thirdpartyService);
	Thread contentHandlerThread = new Thread(contentHander);
	contentHandlerThread.start();
    }
}
