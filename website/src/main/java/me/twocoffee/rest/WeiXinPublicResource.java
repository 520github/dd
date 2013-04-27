package me.twocoffee.rest;
import java.io.ByteArrayOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXB;

import me.twocoffee.service.WeiXinPublicService;
import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;
import me.twocoffee.service.weixin.WeixinServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/weixin")///weixin
public class WeiXinPublicResource {
	private final static Logger logger = LoggerFactory.getLogger(WeiXinPublicResource.class);
	@Autowired
	private WeiXinPublicService weiXinPublicService;
	
	@GET
	@Produces("text/plain; charset=utf-8")
	public String verify(@QueryParam("signature") String signature,
			@QueryParam("timestamp") String timestamp,
			@QueryParam("nonce") String nonce,
			@QueryParam("echostr") String echostr) {
		return echostr;
	}
	
	@POST
	@Produces("text/xml; charset=utf-8")
	public WeiXinPublicResponseEntity messageArrived(WeiXinPublicRequestEntity request) {
		//String weixinId = request.getFromUserName();
		//String accountId = "5099c845e4b0b1427d4a6a81";
		//weiXinPublicService.bindWeiXinPublic(accountId, weixinId);
		logger.info("recevie weixinpublic user[{}] messageType[{}] request content[{}]",new Object[]{request.getFromUserName(),request.getMessageType(),request.getContent()});
		//未绑定
		if(!WeiXinPublicConstant.HELP.equalsIgnoreCase(request.getContent())
				&&!weiXinPublicService.isBindWeiXinPublicByWeiXinId(request.getFromUserName())) {
			request.setContent("Hello2BizUser");
		}
		WeiXinPublicResponseEntity response = null;
		response = WeixinServiceFactory.handleWeixin(request, response);
		
		logger.debug("{}", marshal(response));
		return response;
	}
	
	private String marshal(Object obj) {
		if(obj == null) return "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXB.marshal(obj, out);
		return new String(out.toByteArray());
	}
}
