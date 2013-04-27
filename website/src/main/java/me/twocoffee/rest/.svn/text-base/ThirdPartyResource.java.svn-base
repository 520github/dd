/**
 * 
 */
package me.twocoffee.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.DuplicateThirdPartyMessageException;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.rest.generic.PartialList;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.thirdparty.ThirdpartyService;
import me.twocoffee.service.thirdparty.impl.ThirdPartyServiceImpl.MessageOperateType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author momo
 * 
 */
@Controller
@Path("/service/accounts")
public class ThirdPartyResource extends AbstractResource {

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private final InviteService inviteService = null;

	@Autowired
	private ThirdpartyService thirdPartyService;

	@Autowired
	private AccountService accountService;

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

	private boolean isLastPage(
			List<Contact> contacts, long total,
			int limit, int offset) {

		return contacts == null
				|| contacts.size() < limit
				|| total <= limit + offset;

	}

	@GET
	@Path("/thirdparty/contact/{thirdPartyType}")
	@Produces({ "application/json" })
	public Response getThirdPartyFriend(
			@HeaderParam("Authorization") String token,
			@QueryParam("limit") int limit, @QueryParam("offset") int offset, @PathParam("thirdPartyType") String type) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		ThirdPartyProfile profile = thirdPartyService.getByAccountId(
				accountId, ThirdPartyType.forName(type));

		if (profile == null || !profile.isBind()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Contact> contacts = null;

		try {
			contacts = thirdPartyService.getThirdPartyFriends(
					profile, limit, offset);

		} catch (ThirdPartyAuthExpiredException e) {
			return Response.status(470).build();

		} catch (ThirdPartyAccessException e) {
			return Response.status(470).build();

		} catch (ThirdPartyUnbindException e) {
			return Response.status(470).build();
		}
		long total = thirdPartyService.countFriends(profile);
		PartialList<Contact> r = new PartialList<Contact>(contacts, isLastPage(
				contacts, total, limit, offset), total);

		return Response.status(Status.OK).entity(r).build();
	}

	@POST
	@Path("/broadcast/{thirdPartyType}")
	@Produces({ "application/json" })
	public Response sendBroadcastWeibo(
			@HeaderParam("Authorization") String token, @PathParam("thirdPartyType") String thirdPartyType) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		ThirdPartyType type = ThirdPartyType.forName(thirdPartyType);
		
		Invite invite = inviteService.getInviteByAccount(
				accountId, true);

		StringBuilder inviteUrlBuilder = new StringBuilder("http://");
		inviteUrlBuilder.append(SystemConstant.domainName).append("/account/invite/").append(invite.getId()).append("?r=").append(RandomUtils.nextInt());
		String shortUrl = thirdPartyService.getShortUrl(inviteUrlBuilder.toString());
		String content = "我正在使用多多。它可以一键推送网上内容给自己或好友的手机，并且轻松同步电脑和手机里的照片、文件、视频。你加入多多后，咱两就可以相互分享喜欢的内容了。快来体验吧："
				+ shortUrl;
		
		ThirdPartyPostMessage message = new ThirdPartyPostMessage();
		message.setContent(content);
		message.setMessage("正在使用－多多藏享手机客户端");
		message.setThirdPartyType(type);
		message.setTitle("多多藏享，我的网络碎片信息收集管理工具");
		message.setUrl("http://www.mduoduo.com");
		
		try {
			thirdPartyService.send2ThirdParty(accountId, message, MessageOperateType.Broadcast);

		} catch (ThirdPartyAuthExpiredException e) {
			return Response.status(470).build();

		} catch (ThirdPartyUnbindException e) {
			return Response.status(470).build();

		} catch (DuplicateThirdPartyMessageException e) {
			return Response.status(409).build();
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@POST
	@Path("/invite/{thirdPartyType}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response sendInviteWeibo(
			@HeaderParam("Authorization") String token, @PathParam("thirdPartyType") String thirdPartyType,
			ThirdPartyPostMessage content) {	
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		ThirdPartyType thirdpartyType = ThirdPartyType.forName(thirdPartyType);

		Invite invite = inviteService.getInviteByAccount(
				accountId, true);

		String url = "http://" + SystemConstant.domainName + "/account/invite/"
				+ invite.getId();

		String shortUrl = thirdPartyService.getShortUrl(url);
		String contentStr = content.getContent() + shortUrl;
		content.setContent(contentStr);
		content.setThirdPartyType(thirdpartyType);
		
		try {
			thirdPartyService.send2ThirdParty(accountId, content, MessageOperateType.Invite);

		} catch (ThirdPartyAuthExpiredException e) {
			return Response.status(470).build();

		} catch (ThirdPartyAccessException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.build();

		} catch (ThirdPartyUnbindException e) {
			return Response.status(470).build();

		} catch (DuplicateThirdPartyMessageException e) {
			return Response.status(409).build();
		}
		Map<String, String> attributes = new HashMap<String, String>(1);
		attributes.put("thirdpartyType", thirdPartyType);
		accountService
				.handlerMessage(accountId,
						Message.MessageType.FriendInvitation,
						CollapseKey.FriendMessage, attributes);

		return Response.status(Status.NO_CONTENT).build();
	}
}
