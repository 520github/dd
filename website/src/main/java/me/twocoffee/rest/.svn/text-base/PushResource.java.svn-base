package me.twocoffee.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/push")
public class PushResource extends AbstractResource{
	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PushService pushService;

	@Path("")
	@GET
	public Response deleteNotification(
			@HeaderParam("Authorization") String token) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
		
		Notification notification = new Notification();// 生成系统通知并存储 notification
		notification.setKey(Notification.CollapseKey.friend);
		notification.setAccountId(AccountIdOrGuestId);
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		notification.setSubject("你有" + 1 + "条好友消息。");
		pushService.push(notification, AccountIdOrGuestId);
		return Response.noContent().build();
	}

}
