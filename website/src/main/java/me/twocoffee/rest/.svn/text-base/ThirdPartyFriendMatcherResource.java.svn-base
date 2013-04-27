/**
 * 
 */
package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.BasicAccount;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.thirdparty.ThirdPartyContactMatcherService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author momo
 * 
 */
@Controller
@Path("/service/accounts")
public class ThirdPartyFriendMatcherResource extends AbstractResource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ThirdPartyContactMatcherService thirdPartyContactService;

    @Autowired
    private ThirdpartyService thirdpartyService;

    private AuthToken getAuthToken(String token) {

	if (token == null || token.equalsIgnoreCase("")) {
	    return null;
	}
	String t = token.substring("AuthToken".length()).trim();
	AuthToken authToken = authTokenService.findById(t);

	if (authToken == null) {
	    return null;
	}
	return authToken;
    }

    private List<ContactResult> getContactResult(List<Contact> contactInfos) {

	List<ContactResult> results = new ArrayList<ContactResult>();

	if (contactInfos != null) {

	    for (Contact c : contactInfos) {
		ContactResult r = new ContactResult();
		r.init(c);

		if (c.getMatchedAccount() != null) {
		    BasicAccount ba = new BasicAccount();
		    ba.setId(c.getMatchedAccount().getId());
		    ba.setIntroduction(c.getMatchedAccount().getDescribe());
		    ba.setName(c.getMatchedAccount().getName());
		    ba.setAvatar(c.getMatchedAccount().getAvatarForMap());
		    r.setAccount(ba);

		} else if (c.getMatchedAccount() == null
			&& !c.getRelation().equals(
				RelationType.NotDuoduoAccount)) {

		    continue;
		}
		results.add(r);
	    }
	}
	return results;
    }

    private List<Contact> getContacts(List<RelationType> rtypes, String id,
	    List<ThirdPartyType> types, int num) {

	List<Contact> contacts = thirdPartyContactService
		.getMatchedThirdPartyFriend(types, id, rtypes, 0, 0);

	if (num > 0 && contacts != null && contacts.size() == 0) {
	    try {
		Thread.sleep(1000);

	    } catch (InterruptedException e) {
	    }
	}
	return contacts;
    }

    @POST
    @Path("/contact/phone")
    @Produces({ "application/json" })
    public Response matchPhoneFriend(
	    @HeaderParam("Authorization") String token,
	    List<Contact> contactInfos) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	thirdPartyContactService.matchThirdPartyFriend(contactInfos,
		ThirdPartyType.Phone, AccountIdOrGuestId);

	List<ContactResult> results = getContactResult(contactInfos);
	appandDuoduoPublic(results, AccountIdOrGuestId);
	return Response.status(Status.OK).entity(results).build();
    }

    @GET
    @Path("/contact/{thirdpartyType}")
    @Produces({ "application/json" })
    public Response matchThirdpartyFriend(
	    @HeaderParam("Authorization") String token,
	    @PathParam("thirdpartyType") String type) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	ThirdPartyType thirdpartyType = ThirdPartyType.forName(type);
	List<ThirdPartyType> types = new ArrayList<ThirdPartyType>();
	types.add(thirdpartyType);
	List<RelationType> rtypes = new ArrayList<Contact.RelationType>();
	rtypes.add(RelationType.DuoduoAccount);
	rtypes.add(RelationType.NotDuoduoAccount);
	int num = thirdpartyService.getFriendsNumber(AccountIdOrGuestId,
		thirdpartyType);

	List<Contact> contacts = null;

	for (int i = 0; i < 3; i++) {
	    contacts = getContacts(rtypes, AccountIdOrGuestId, types, num);

	    if (contacts != null && contacts.size() > 0) {
		break;
	    }
	}
	List<ContactResult> results = getContactResult(contacts);
	appandDuoduoPublic(results, AccountIdOrGuestId);
	return Response.status(Status.OK).entity(results).build();
    }

    @GET
    @Path("/contact/")
    @Produces({ "application/json" })
    public Response getMatchedThirdpartyFriend(
	    @HeaderParam("Authorization") String token,
	    @QueryParam("type") String type) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);
	List<ThirdPartyType> types = getThirdPartyTypes(type);
	List<RelationType> rtypes = new ArrayList<Contact.RelationType>();
	rtypes.add(RelationType.DuoduoAccount);
	rtypes.add(RelationType.NotDuoduoAccount);
	Map<ThirdPartyType, List<ContactResult>> results = new HashMap<ThirdPartyType, List<ContactResult>>();

	if (types.contains(ThirdPartyType.Public)) {
	    results.put(ThirdPartyType.Public,
		    buildDuoduoPublic(accountId));

	}
	List<Contact> contacts = thirdPartyContactService
		.getMatchedThirdPartyFriend(types, accountId,
			rtypes, 0, 0);

	int retryTime = 2;

	for (; retryTime > 0; retryTime--) {

	    if (contacts == null || contacts.size() == 0) {
		contacts = thirdPartyContactService
			.getMatchedThirdPartyFriend(types,accountId,rtypes, 0, 0);

	    } else {
		break;
	    }
	}
	buildThirdPartyResult(results, contacts);
	return Response.status(Status.OK).entity(results).build();
    }

    private void buildThirdPartyResult(
	    Map<ThirdPartyType, List<ContactResult>> results,
	    List<Contact> contactInfos) {

	if (contactInfos != null) {

	    for (Contact c : contactInfos) {
		ContactResult r = new ContactResult();
		r.init(c);

		if (c.getMatchedAccount() != null) {
		    BasicAccount ba = new BasicAccount();
		    ba.setId(c.getMatchedAccount().getId());
		    ba.setIntroduction(c.getMatchedAccount().getDescribe());
		    ba.setName(c.getMatchedAccount().getName());
		    ba.setAvatar(c.getMatchedAccount().getAvatarForMap());
		    r.setAccount(ba);

		} else if (c.getMatchedAccount() == null
			&& !c.getRelation().equals(
				RelationType.NotDuoduoAccount)) {

		    continue;
		}

		if (results.containsKey(c.getThirdPartyType())) {
		    results.get(c.getThirdPartyType()).add(r);

		} else {
		    List<ContactResult> result = new LinkedList<ContactResult>();
		    result.add(r);
		    results.put(c.getThirdPartyType(), result);
		}
	    }
	}
    }

    private List<ThirdPartyType> getThirdPartyTypes(String type) {
	List<ThirdPartyType> types = new ArrayList<ThirdPartyType>();

	if (StringUtils.isNotBlank(type)) {
	    String[] typestrs = type.split(" ");

	    for (String typestr : typestrs) {
		types.add(ThirdPartyType.forName(typestr));
	    }

	} else {
	    types.add(ThirdPartyType.Renren);
	    types.add(ThirdPartyType.Phone);
	    types.add(ThirdPartyType.Public);
	    types.add(ThirdPartyType.Weibo);
	    types.add(ThirdPartyType.Tencent);
	}
	return types;
    }

    @GET
    @Path("/contact/suggest")
    @Produces({ "application/json" })
    public Response suggestThirdpartyFriend(
	    @HeaderParam("Authorization") String token,
	    @QueryParam("limit") Integer limit,
	    @QueryParam("offset") Integer offset) {
    	
    int code = this.getAuthorizationErrorCode(token);
    if(code > 200) {
    	return Response.status(code).build();
    }
    //guest user
    if(this.isGuestUser(token)) {
    	return Response.status(410).build();
    }
    //login user
    Account account = this.getLoginUserAccontByAuthorization(token);
    if(account == null) {
    	return Response.status(404).build();
    }
	List<Contact> contacts = new LinkedList<Contact>();

	if (limit == null) {
	    limit = 0;
	}

	if (offset == null) {
	    offset = 0;
	}
	List<ThirdPartyType> types = new ArrayList<ThirdPartyType>();
	types.add(ThirdPartyType.Weibo);
	types.add(ThirdPartyType.Phone);
	types.add(ThirdPartyType.Renren);
	types.add(ThirdPartyType.Tencent);
	List<RelationType> rtypes = new ArrayList<Contact.RelationType>();
	rtypes.add(RelationType.DuoduoAccount);
	contacts = thirdPartyContactService.getMatchedThirdPartyFriend(types,
			account.getId(), rtypes, limit, offset);

	List<ContactResult> results = getContactResult(contacts);
	appandDuoduoPublic(results, account.getId());
	return Response.status(Status.OK).entity(results).build();
    }

    private void appandDuoduoPublic(List<ContactResult> results,
	    String accountId) {

	List<Account> publicAccounts = accountService.getPublicAccounts();

	for (Account account : publicAccounts) {

	    if (friendService.getByAccountIdAndFriendId(accountId,
		    account.getId()) == null) {

		results.add(getContactResultByAccount(account));
	    }

	}
    }

    private List<ContactResult> buildDuoduoPublic(String accountId) {
	List<ContactResult> results = new LinkedList<ContactResult>();
	List<Account> publicAccounts = accountService.getPublicAccounts();

	for (Account account : publicAccounts) {

	    if (friendService.getByAccountIdAndFriendId(accountId,
		    account.getId()) == null) {

		results.add(getContactResultByAccount(account));
	    }
	}
	return results;
    }

    private ContactResult getContactResultByAccount(Account account) {
	ContactResult contactResult = new ContactResult();
	BasicAccount basicAccount = new BasicAccount();
	basicAccount.setAvatar(account.getAvatarForMap());
	basicAccount.setId(account.getId());
	basicAccount.setIntroduction(account.getDescribe());
	basicAccount.setName(account.getName());
	contactResult.setAccount(basicAccount);
	contactResult.setAvatar(account.getAvatar(Account.PHOTO_SIZE_SMALL));
	contactResult.setName(account.getName());
	contactResult.setThirdPartyType(ThirdPartyType.Public);
	return contactResult;
    }
}
