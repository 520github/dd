package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.FriendInfo;
import me.twocoffee.rest.entity.ListResult;
import me.twocoffee.rest.utils.InfoConverter;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/accounts")
public class CheckResource extends AbstractResource{

	@Autowired
	private final AccountService accountService = null;

	@Autowired
	private final AuthTokenService authTokenService = null;

	@Autowired
	private FriendService friendService;

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

	private AccountInfo[] getFriendInfos(AccountInfo[] fs, String visitAccountId) {
		List<Account> visitFriends = friendService.findFriends(visitAccountId);

		for (AccountInfo a : fs) {
			FriendInfo fi = new FriendInfo();
			fi.setIsFriend(hasFriend(a.getId(), visitFriends));
			fi.setShared(friendService.countSharedTo(visitAccountId, a.getId()));

			a.setFriend(fi);
		}
		return fs;
	}

	private boolean hasFriend(String id, List<Account> friends) {
		if (friends == null || friends.size() < 1)
			return false;
		for (Account a : friends) {
			if (a.getId().equals(id))
				return true;
		}
		return false;
	}

	@GET
	@Path("/domain/{accountName}")
	public Response checkDomain(@PathParam("accountName") String accountName)
			throws Exception {
		Account account = accountService.getByAccountName(accountName);
		if (account != null) {
			return Response.ok().build();
		} else {
			return Response.status(404).build();
		}
	}

	@HEAD
	@Path("/domain/{accountName}")
	public Response checkDomainHead(@PathParam("accountName") String accountName)
			throws Exception {

		Account account = accountService.getByAccountName(accountName);
		if (account != null) {
			return Response.ok().build();
		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/login/{loginName}")
	public Response checkEmail(@PathParam("loginName") String loginName)
			throws Exception {

		Account account = accountService.getByLoginName(loginName);

		if (account != null) {
			return Response.ok().build();

		} else {
			return Response.status(404).build();
		}
	}

	@HEAD
	@Path("/login/{loginName}")
	public Response checkEmailHead(@PathParam("loginName") String loginName)
			throws Exception {

		Account account = accountService.getByLoginName(loginName);

		if (account != null) {
			return Response.ok().build();

		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Path("/name/{name}")
	public Response checkName(@PathParam("name") String name)
			throws Exception {

		Account account = accountService.getByName(name);

		if (account != null) {
			return Response.ok().build();

		} else {
			return Response.status(404).build();
		}
	}

	@HEAD
	@Path("/name/{name}")
	public Response checkNameHead(@PathParam("name") String name)
			throws Exception {

		Account account = accountService.getByName(name);

		if (account != null) {
			return Response.ok().build();

		} else {
			return Response.status(404).build();
		}
	}

	/**
	 * 站内找人
	 * 
	 * @param token
	 * @param scope
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/")
	@Produces({ "application/json" })
	public Response find(
			@HeaderParam("Authorization") String token,
			@QueryParam("scope") String scope,
			@QueryParam("keyword") String keyword)
			throws Exception {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
		
		List<Account> list = null;
		if (scope != null && scope.equals("name")) {
			list = accountService.findByName(keyword);
		} else if (scope != null && scope.equals("login_name")) {
			Account a = accountService.getByLoginName(keyword);
			if (a != null) {
				list = new ArrayList();
				list.add(a);
			}
		} else {
			list = accountService.findByName(keyword);
			if (list == null) {
				list = new ArrayList();
			}
			Account a = accountService.getByLoginName(keyword);
			if (a != null) {
				list.add(a);
			}
		}

		List<AccountInfo> fs = InfoConverter.convertToAccounts(list);
		ListResult r = new ListResult();
		AccountInfo[] ais = null;
		if (fs != null) {
			ais = new AccountInfo[fs.size()];
			r.setResult(fs.toArray(ais));
		}

		if (fs != null && fs.size() > 0) {
			r.setResult(getFriendInfos(r.getResult(), AccountIdOrGuestId));
		}
		return Response.ok(r).build();
	}
}
