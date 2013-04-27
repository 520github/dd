package me.twocoffee.service.weixin;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.SpringContext;
import me.twocoffee.service.weixin.WeiXinPublicConstant.HandleType;
import me.twocoffee.service.weixin.WeiXinPublicConstant.RequestType;
import me.twocoffee.service.weixin.WeiXinPublicConstant.ResponseType;
import me.twocoffee.service.weixin.WeiXinPublicConstant.ServiceType;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity.MessageType;
import me.twocoffee.service.weixin.handle.WeixinHandle;
import me.twocoffee.service.weixin.request.WeixinRequest;
import me.twocoffee.service.weixin.response.WeixinResponse;
import me.twocoffee.service.weixin.type.EventMessageServiceType;
import me.twocoffee.service.weixin.type.ImageMessageServiceType;
import me.twocoffee.service.weixin.type.LinkMessageServiceType;
import me.twocoffee.service.weixin.type.TextMessageServiceType;
import me.twocoffee.service.weixin.type.UnknownMessageServiceType;
import me.twocoffee.service.weixin.type.WeixinServiceType;

public class WeixinServiceFactory {
	private static Map<ServiceType, String> ServiceTypeMap = new HashMap<ServiceType, String>();
	static {
		ServiceTypeMap.put(ServiceType.Hello,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.generateBindWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.SaveText,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.saveTextContentWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.SaveImage,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.saveImageContentWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.SaveLink,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.saveLinkWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.GetLastestContent,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.getLastestContentWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.Unsubscribe,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.unsubscribeWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.Help,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.helpWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.Download,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.downloadWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
		
		ServiceTypeMap.put(ServiceType.Unknown,
				RequestType.defaultWeixinRequest.toString()+","+
				HandleType.unknownWeixinHandle.toString()+","+
				ResponseType.defaultWeixinResponse.toString());
	}
	
	private static ServiceType getServiceType(WeiXinPublicRequestEntity request) {
		String messageType = request.getMessageType();
		WeixinServiceType weixinServiceType = null;
		ServiceType serviceType = null;
		if(MessageType.text.toString().equalsIgnoreCase(messageType)) {
			weixinServiceType = new TextMessageServiceType();
		}
		else if(MessageType.image.toString().equalsIgnoreCase(messageType)) {
			weixinServiceType = new ImageMessageServiceType();
		}
		else if(MessageType.event.toString().equalsIgnoreCase(messageType)) {
			weixinServiceType = new EventMessageServiceType();
		}
		else if(MessageType.link.toString().equalsIgnoreCase(messageType)) {
			weixinServiceType = new LinkMessageServiceType();
		}
		else {
			weixinServiceType = new UnknownMessageServiceType();
		}
		serviceType = weixinServiceType.getServiceType(request);
		return serviceType;
	}
	
	private static WeixinRequest getRequestObj(String requestType) {
		return (WeixinRequest)SpringContext.getBean(requestType);
	}
	
	private static WeixinHandle getHandleObj(String handleType) {
		return (WeixinHandle)SpringContext.getBean(handleType);
	}
	
	private static WeixinResponse getResponseObj(String responseType) {
		return (WeixinResponse)SpringContext.getBean(responseType);
	}
	
	public static WeixinServiceImpl getWeixinService(WeiXinPublicRequestEntity request) {
		ServiceType type = getServiceType(request);
		String values = ServiceTypeMap.get(type);
		if(values == null)return null;
		String value[] = values.split(",");
		if(value.length < 3)return null;
		return new WeixinServiceImpl(getRequestObj(value[0]), getHandleObj(value[1]), getResponseObj(value[2]));
	}
	
	public static WeiXinPublicResponseEntity handleWeixin(WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		WeixinServiceImpl service  = getWeixinService(request);
		if(service == null)return response;
		response = service.handleWeixin(request, response);
		return response;
	}
}
