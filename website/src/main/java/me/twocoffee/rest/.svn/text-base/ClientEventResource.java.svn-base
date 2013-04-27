package me.twocoffee.rest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Badge;
import me.twocoffee.rest.entity.ClientEvent;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.FriendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/event/client")
public class ClientEventResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ClientEventResource.class);

    private static String extractToken(String token) {
	return token == null || !token.startsWith("AuthToken") ? null : token
		.replace("AuthToken ", "");
    }

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private FriendService friendService;

    @Path("/")
    @POST
    public Response postEvent(@HeaderParam("Authorization") String token,
	    ClientEvent clientEvent) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	switch (clientEvent.getEvent().getAction()) {
	case visit:
	    badgeService
		    .resetBadge(AccountIdOrGuestId, Badge.BadgeName
			    .valueOf(clientEvent.getEvent().getTarget()));

	    break;
	case Complete:

	    if (clientEvent.getEvent().getTarget()
		    .equals("ThirdpartyAuthorization")) {

		Account account = accountService.getById(AccountIdOrGuestId);

		if (!account.hasStatus(Account.Status.KnowDuoduoPublic)) {
		    Account recommandFriends = accountService
			    .getRecommendPublicAccount();

		    if (recommandFriends != null) {
			LOGGER.debug(
				"public account {} send add friend request to {}",
				recommandFriends.getId(), account.getId());

			friendService.addFriend(recommandFriends.getId(),AccountIdOrGuestId, "", "", "");

		    }
		}

	    } else if (clientEvent.getEvent().getTarget()
		    .equals("ThirdpartySharing")) {

		LOGGER.info(
			"account {} ThirdpartySharing {}",
			new Object[] {AccountIdOrGuestId,
				clientEvent.getEvent().getArgument()
					.get("thirdpartyType") });
	    }
	    break;
	case Change:

	    if (clientEvent.getEvent().getArgument() != null
		    && clientEvent.getEvent().getArgument().size() > 0) {

		for (String key : clientEvent.getEvent().getArgument().keySet()) {
		    LOGGER.info("account {} change {} to {}", new Object[] {AccountIdOrGuestId, key,
			    clientEvent.getEvent().getArgument().get(key) });
		}
	    }

	default:
	    break;
	}

	return Response.noContent().build();
    }
}
