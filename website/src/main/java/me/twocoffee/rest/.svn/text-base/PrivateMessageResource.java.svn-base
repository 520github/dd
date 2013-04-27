package me.twocoffee.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.exception.SendSameMessageInSessionException;
import me.twocoffee.rest.entity.PrivateMessageInfo;
import me.twocoffee.rest.utils.InfoConverter;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.PrivateMessageService;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/chat")
public class PrivateMessageResource extends AbstractResource{

	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private FriendService friendService;

	private String getVersion(String ua) {
		return InfoConverter.getVersionFromUserAgent(ua);
	}

	private boolean isLastPage(
			List<PrivateMessageSession> privateMessageSessions, long total,
			int limit, int offset) {

		return privateMessageSessions == null
				|| privateMessageSessions.size() < limit
				|| total <= limit + offset;
	}

	protected String json1(Object t) {
		if (t == null)
			throw new IllegalArgumentException();

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor());

		return JSONSerializer.toJSON(t, jsonConfig).toString();
	}

	@Path("/session/")
	@DELETE
	@Produces("application/json")
	public Response deletePrivateMessage(
			@MatrixParam("content_id") String contentId,
			@MatrixParam("account_id") String accountId,
			@HeaderParam("Authorization") String token
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String myAccountId = this.getAccountIdOrGuestId(token);
		privateMessageService.deleteSession(myAccountId,
				accountId, contentId);

		return Response
				.status(Status.NO_CONTENT).build();
	}

	/**
	 * 获取会话内消息列表
	 * 
	 * @param contentId
	 * @param accountId
	 * @param token
	 * @param sinceId
	 * @return
	 */
	@Path("/session/")
	@GET
	@Produces("application/json")
	public Response getPrivateMessageList(
			@MatrixParam("content_id") String contentId,
			@MatrixParam("account_id") String accountId,
			@HeaderParam("Authorization") String token,
			@QueryParam("since_id") String sinceId
			) {

		if ("null".equals(sinceId)) {
			sinceId = "";
		}
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}

		String ownerId = this.getAccountIdOrGuestId(token);
		PrivateMessageSession session = privateMessageService.getSession(
				ownerId, accountId, contentId);

		if (session == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<PrivateMessage> privateMessages = privateMessageService
				.getLatestMessagesInSession(session.getId(), sinceId);

		int total = session.getMessageCount();
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("total", total);
		r.put("result", privateMessages);
		return Response
				.ok(json1(r)).build();

	}

	@Path("/session/index/")
	@GET
	@Produces("application/json")
	public Response getPrivateMessageSessionList(
			@MatrixParam("content_id") String contentId,
			@HeaderParam("Authorization") String token,
			@QueryParam("offset") int offset,
			@QueryParam("limit") int limit
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		List<PrivateMessageSession> privateMessageSessions;
		long total;
		if (null != contentId && !"".equals(contentId)) {
			privateMessageSessions = privateMessageService.getSessions(
					accountId, contentId, offset, limit);
			total = privateMessageService.countSessions(accountId, contentId);
		} else {
			privateMessageSessions = privateMessageService.getSessions(
					accountId, offset, limit);
			total = privateMessageService.countSessions(accountId);

		}
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("total", total);
		r.put("lastPage",
				isLastPage(privateMessageSessions, total, limit, offset));
		r.put("result", privateMessageSessions);
		return Response
				.ok(json1(r)).build();
	}

	@Path("/session/")
	@POST
	@Consumes({ "application/json" })
	@Produces("application/json")
	public Response postPrivateMessage(
			PrivateMessageInfo messageInfo,
			// @MatrixParam("content_id") String contentId,
			// @MatrixParam("account_id") String accountId,
			@HeaderParam("Authorization") String token,
			@HeaderParam("User-Agent") String ua
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String myAccountId = this.getAccountIdOrGuestId(token);
		
		if (messageInfo.getAccountId() == null
				|| messageInfo.getAccountId().length == 0) {

			return Response
					.status(Status.BAD_REQUEST).build();
		}

		String msg = messageInfo.getMessage();

		try {
			msg = URLDecoder.decode(messageInfo.getMessage(), "utf-8");

		} catch (UnsupportedEncodingException e1) {

		}

		Map<String, List<Map<String, String>>> results = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> result = new LinkedList<Map<String, String>>();
		results.put("result", result);

		for (String accountId : messageInfo.getAccountId()) {
			PrivateMessage message = new PrivateMessage();
			message.setMessage(msg);
			message.setUuid(messageInfo.getUuid());
			message.setAccountId(myAccountId);
			message.setToAccountId(accountId);

			if (friendService.getByAccountIdAndFriendId(
					myAccountId, accountId) == null) {

				Map<String, String> r = new HashMap<String, String>();
				r.put("accountId", accountId);
				r.put("status", "Failed");
				r.put("cause", "NotFriends");
				result.add(r);
				continue;
			}

			try {
				privateMessageService.sendMessage(message,
							messageInfo.getContentId(), true);

			} catch (SendSameMessageInSessionException e) {
				// return Response
				// .status(Status.CONFLICT).build();
				Map<String, String> r = new HashMap<String, String>();
				r.put("accountId", accountId);
				r.put("status", "Failed");
				result.add(r);
				continue;

			} catch (Exception e) {
				Map<String, String> r = new HashMap<String, String>();
				r.put("accountId", accountId);
				r.put("status", "Failed");
				result.add(r);
				continue;
			}
			Map<String, String> r = new HashMap<String, String>();
			r.put("accountId", accountId);
			r.put("status", "Accepted");
			result.add(r);
			continue;
		}
		return Response
					.status(Status.ACCEPTED).entity(results).build();
	}

}
