package me.twocoffee.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.WebToken.UserType;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.ResponseUser;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.User;
import me.twocoffee.service.event.BindAccountEvent;
import me.twocoffee.service.event.SyncGuestRepositoryEvent;
import me.twocoffee.service.event.ThirdPartyUserAuthorizationEvent;
import me.twocoffee.service.event.UnbindAccountEvent;
import me.twocoffee.service.event.message.AuthenticationSuccessEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

@Controller
@Path("/service/accounts")
public class AuthResource extends AbstractAccountResource {
    @Autowired
    private final AuthConfiguration authConfig = null;

    @Path("/default/auth")
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response auth(User user, @HeaderParam("Authorization") String token) {

	if (user.getAccount() == null || user.getAccount().equals("")
		|| user.getPassword() == null || user.getPassword().equals("")) {

	    return Response.status(403).build();
	}
	Account account = accountService.getByLoginName(user.getAccount());

	if (account == null) {
	    return Response.status(403).build();
	}

	if (!account.getPasswordHash().equals(
		DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {

	    return Response.status(403).build();
	}

	if (account.hasStatus(Status.ShowThirdpartyFriends)) {
	    account.removeStatus(Status.ShowThirdpartyFriends);
	    accountService.save(account);
	}

	eventService.send(new AuthenticationSuccessEvent(this, account));
	eventService.send(new BindAccountEvent(this, token, account.getId(),UserType.LoginDuoduo));
	eventService.send(new SyncGuestRepositoryEvent(this, token, account
		.getId()));

	List<ThirdPartyProfile> profiles = thirdpartyService
		.getByAccountId(account.getId());

	if (profiles != null) {

	    for (ThirdPartyProfile p : profiles) {

		if (p.isBind()) {
		    ThirdPartyUserAuthorizationEvent event = new ThirdPartyUserAuthorizationEvent(
			    p, false, false);

		    eventService.send(event);
		}
	    }
	}
	ResponseUser responseUser = new ResponseUser();
	responseUser.setAccount(reduceAccount(account));
	// support client old version
	if (StringUtils.isEmpty(token) || !TokenUtil.isEmptyAuthToken(token)) {
	    AuthToken authToken = authTokenService.createToken(account.getId());
	    responseUser.setAuthToken(authToken.getId());
	}
	return Response.ok(responseUser).build();
    }

    /**
     * 使用全局密码登录
     * 
     * @param user
     * @return
     */
    @Path("/smtp/auth")
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response authPro(User user) {
	if (user.getAccount() == null || user.getAccount().equals("")
		|| user.getPassword() == null || user.getPassword().equals("")) {
	    return Response.status(403).build();
	}
	Account account = accountService.getByLoginName(user.getAccount());

	if (account == null) {
	    return Response.status(403).build();
	}

	if (!user.getPassword().equals(authConfig.getGlobalAuthPassword())) {
	    return Response.status(403).build();
	}
	AuthToken authToken = authTokenService.createToken(account.getId());
	ResponseUser responseUser = new ResponseUser();
	responseUser.setAccount(reduceAccount(account));
	responseUser.setAuthToken(authToken.getId());
	return Response.ok(responseUser).build();
    }

    @Path("/logout")
    @PUT
    public Response logout(@HeaderParam("Authorization") String token) {
	int code = this.getAuthorizationErrorCode(token);
	if (code > 200) {
	    return Response.status(code).build();
	}
	eventService.send(new UnbindAccountEvent(this, token));
	return Response.status(204).build();
    }
}
