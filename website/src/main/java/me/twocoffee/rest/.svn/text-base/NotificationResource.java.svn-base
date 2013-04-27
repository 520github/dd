package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Badge;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.entity.Notification;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/notification")
public class NotificationResource extends AbstractResource{
	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private BadgeService badgeService;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private PushService PushServiceImpl;

	private JsonObject reduceBadge(final Badge badge) {
		@SuppressWarnings("unused")
		JsonObject o = new JsonObject() {
			public Object getFolder() {
				return badge.getFld();
			}
/**
 * 客户端需要数据，网站只用notification
 * 
 */
		/*	public int getFriend() {
				return badge.getFriend();
			}

			public int getFriendRequest() {
				return badge.getFriendRequest();
			}

  
			public int getMessage() {
				return badge.getMessage();
			}*/
 
			public int getChat(){
				return badge.getChat();
			}
			
			public Object getMessage(){
			 return badge.getMsg();	
			}
		    
			public int getAcknowledgment(){
				return badge.getAcknowledgment();
			}
			
			/*public int getReadLater() {
				return badge.getMessage();
			}

			public int getRecommendedByFriend() {
				return badge.getRecommendedByFriend();
			}

			public int getSystemMessage() {
				return badge.getSystemMessage();
			}
			*/

			public int getTotal() {
				return badge.getTotal();
			}
			
			
			
		};
		return o;
	}

	private List<JsonObject> reduceNotifications(final List<Notification> ns) {
		List<JsonObject> ret = new ArrayList<JsonObject>(ns.size());
		for (final Notification n : ns) {
			@SuppressWarnings("unused")
			JsonObject o = new JsonObject() {
				public String getAction() {
					return n.getAction();
				}

				public String getDate() {
					return DateUtil.FormatDateUTC(n.getDate());
				}

				public String getId() {
					return n.getId();
				}

				public Notification.CollapseKey getKey() {
					return n.getKey();
				}

				public Notification.Status getStatus() {
					return n.getStatus();
				}

				public String getSubject() {
					return n.getSubject();
				}
				

			};
			ret.add(o);
		}
		return ret;
	}

	private JsonObject reducePollResult(final List<Notification> ns,
			final Badge badge) {
		@SuppressWarnings("unused")
		JsonObject jo = new JsonObject() {
			public JsonObject getBadge() {
				return reduceBadge(badge);
			}

			public List<JsonObject> getNotification() {
				return reduceNotifications(ns);
			}
		};

		return jo;
	}

	@Path("/{notificationId}")
	@DELETE
	public Response deleteNotification(
			@HeaderParam("Authorization") String token,
			@PathParam("notificationId") String notificationId) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
	    String accountId = this.getAccountIdOrGuestId(token);
	    
		notificationService.read(accountId, notificationId);
		return Response.noContent().build();
	}

	@Path("/{notificationId}")
	@GET
	@Produces("application/json")
	public Response getNotification(
			@HeaderParam("Authorization") String token,
			@PathParam("notificationId") String notificationId) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }

		Notification notification = notificationService.getNotificationById(notificationId);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd'T'HH:mm:ss'Z'"));
		return Response.ok(JSONSerializer.toJSON(notification, jsonConfig).toString()).build();
	}
	
	

	@Path("/")
	@GET
	@Produces("application/json")
	public Response pollNotifications(
			@HeaderParam("Authorization") String token,
			@QueryParam("since_id") String prevousId,
			@QueryParam("displayType") String displayType) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
	    String accountId = this.getAccountIdOrGuestId(token);
	    
		List<Notification> ns = notificationService.getLatestNotifications(
				accountId, prevousId == null ? "" : prevousId,displayType);
		Badge badge = badgeService.getBadge(accountId);
		return Response
				.ok(reducePollResult(ns, badge == null ? new Badge() : badge))
				.header("X-Next-Request-After", 120).build();
	}
	

	@Path("/test")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response testPush(
			@HeaderParam("Authorization") String token,
			@QueryParam("badge") int badge,
			Notification notification
			
			){
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
		   String accountId = AccountIdOrGuestId;
		   PushServiceImpl.testPush(notification, accountId, badge);
		   return Response.ok().build();
		
	}
	
	
}
