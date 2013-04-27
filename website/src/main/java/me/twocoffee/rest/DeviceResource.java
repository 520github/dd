package me.twocoffee.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Device;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DeviceService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/device")
public class DeviceResource {
	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private DeviceService deviceService;

	@Path("/")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response postDevice(
			@HeaderParam("Authorization") String token, 
			@HeaderParam("User-Agent") String userAgent, 
			Device device) {
		if(device == null ||  StringUtils.isBlank(device.getMac())) {
			return Response.status(Status.FORBIDDEN).build();
		}
		//老版本,登录后注册设备
		if(!TokenUtil.isEmptyAuthToken(token)) {
			AuthToken authToken = authTokenService.findById(TokenUtil.extractAuthToken(token));
			if (authToken == null) {
				return Response.status(Status.FORBIDDEN).build();
			}
			device.setAccountId(authToken.getAccountId());// 设置accountId
		}
		//clientId
		else if(!TokenUtil.isEmptyClientId(token)) {
			if(StringUtils.isEmpty(device.getId())) {
				device.setId(TokenUtil.extractClientId(token));
			}
		}
		else {
			device.setAccountId("");
		}
		device.setUserAgent(userAgent);
		device.setPushAlert(true);// 客户端要求默认是可以推送
		device = deviceService.createOrSave(device);
		return Response.ok("{\"id\":\""+device.getId()+"\"}").build();
	}
}
