package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageFrom;
import me.twocoffee.rest.entity.FriendRequestInfo;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/message")
public class MessageResource extends AbstractResource {
	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private MessageService messageService;

	private Map<String, Object> getAccountInfo(Map<String, Object> map) {
		if (map == null) {
			return map;
		}
		String accountId = (String) map.get("id");
		if (accountId == null) {
			return null;
		}
		Account account = accountService.getById(accountId);
		if (account == null) {
			return null;
		}
		map.put("loginName", account.getLoginName());
		map.put("avatar", account.getAvatar());
		map.put("name", account.getName());
		map.put("gender", account.getGender());
		map.put("city", account.getCity());
		return map;
	}

	private Map<String, Object> getAccountInfoForNewObject(
			Map<String, Object> map, String messageType) {
		if (map == null) {
			return map;
		}
		String accountId = (String) map.get("id");
		if (accountId == null) {
			return map;
		}
		Account account = accountService.getById(accountId);
		if (account == null) {
			return map;
		}
		if (messageType.equals(Message.MessageType.FriendRequest.toString())) {
			FriendRequestInfo fq = new FriendRequestInfo();
			Object message = map.get("message");
			String id = map.get("id").toString();
			map.clear();
			fq.setId(id);
			fq.setAvatar(account.getAvatarForMap());
			fq.setName(account.getName());
			map.put("account", fq);
			map.put("message", message);
		} else if (messageType.equals(Message.MessageType.FriendRequestFeedback
				.toString())) {
			Object alias = map.get("alias");
			Object id = map.get("id");
			map.clear();
			if (alias != null) {
				map.put("alias", alias);
			}
			map.put("accountId", id);
			map.put("avatar", account.getAvatarForMap());
			map.put("name", account.getName());
		} else {
			Object id = map.get("id");
			map.clear();
			map.put("accountId", id);
			map.put("avatar", account.getAvatarForMap());
			map.put("name", account.getName());

		}
		return map;
	}

	private List<JsonObject> reduceMessages(final List<Message> messages) {
		List<JsonObject> ret = new ArrayList<JsonObject>(messages.size());
		for (final Message m : messages) {
			@SuppressWarnings("unused")
			JsonObject o = new JsonObject() {
				public List<Message.MessageAction> getAction() {
					return m.getAction();
				}

				public Map<String, Object> getAttribute() {
					return getAccountInfo(m.getAttribute());
				}

				public String getDate() {
					return DateUtil.FormatDateUTC(m.getDate());
				}

				public String getId() {
					return m.getId();
				}

				public Message.MessageType getMessageType() {
					return m.getMessageType();
				}

				public String getSubject() {
					return m.getSubject();
				}

			};
			ret.add(o);
		}
		return ret;
	}

	private List<JsonObject> reduceMessagesForNewObject(
			final List<Message> messages) {
		List<JsonObject> ret = new ArrayList<JsonObject>(messages.size());
		for (final Message m : messages) {
			@SuppressWarnings("unused")
			JsonObject o = new JsonObject() {

				public List<Message.MessageAction> getAction() {
					return m.getAction();
				}

				public Map<String, Object> getAttribute() {
					return getAccountInfoForNewObject(m.getAttribute(), m
							.getMessageType().toString());
				}

				public String getDate() {
					return DateUtil.FormatDateUTC(m.getDate());
				}

				public MessageFrom getFrom() {
					return m.getFrom();
				}

				public String getId() {
					return m.getId();
				}

				public Message.MessageType getMessageType() {
					return m.getMessageType();
				}

				public String getSubject() {
					return m.getSubject();
				}

			};
			ret.add(o);
		}
		return ret;
	}

	@Path("/{messageId}")
	@POST
	public Response actionMessage(@HeaderParam("Authorization") String token,
			@PathParam("messageId") String messageId,
			Message.MessageAction action) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
		messageService.actionMessage(messageId, action);
		return Response.noContent().build();
	}

	@Path("/{messageId}")
	@DELETE
	public Response deleteAllMessage(
			@HeaderParam("Authorization") String token,
			@PathParam("messageId") String messageId) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
		messageService.delete(messageId);
		return Response.noContent().build();
	}

	@Path("/")
	@DELETE
	public Response deleteMessage(@HeaderParam("Authorization") String token) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
	    String accountId = this.getAccountIdOrGuestId(token);
		messageService.deleteAll(accountId);
		return Response.noContent().build();
	}

	@Path("/catalog/{catalogId}")
	@GET
	@Produces("application/json")
	public Response getMessages(@Context HttpServletRequest request,
			@HeaderParam("Authorization") String token,
			@QueryParam("limit") int limit, @QueryParam("offset") int offset,
			@PathParam("catalogId") CatalogId catalogId,
			@QueryParam("displayType") String displayType
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
		String accountId = this.getAccountIdOrGuestId(token);
		limit = limit == 0 ? 10 : limit;
		List<Message> messages = messageService
				.getMessages(
						accountId, catalogId, displayType,
						limit, offset);
		PagedResult<JsonObject> result = new PagedResult<JsonObject>();

		result.setResult(reduceMessagesForNewObject(messages));

		long total = messageService.countMessageByAccountId(
				accountId, catalogId);
		result.setTotal(total);
		result.setLastPage(limit, offset);
		return Response.ok(result).build();
	}
}
