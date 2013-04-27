package me.twocoffee.service.weixin.type;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicConstant.ServiceType;

public abstract class AbstractServiceType implements WeixinServiceType {
	protected ServiceType serviceType = null;
	protected String content = null;
	protected static Map<String,ServiceType> exactMatchContent = new HashMap<String,ServiceType>();
	protected static Map<String,ServiceType> fuzzyMatchContent = new HashMap<String,ServiceType>();
	protected static Map<String,ServiceType> startMatchContent = new HashMap<String,ServiceType>();
	
	static {
		exactMatchContent.put(WeiXinPublicRequestEntity.EventKey.subscribe.toString(), ServiceType.Hello);
		exactMatchContent.put(WeiXinPublicRequestEntity.EventKey.unsubscribe.toString(), ServiceType.Unsubscribe);
		exactMatchContent.put(WeiXinPublicConstant.Hello2BizUser, ServiceType.Hello);
		exactMatchContent.put(WeiXinPublicConstant.GetLastestContent, ServiceType.GetLastestContent);
		exactMatchContent.put(WeiXinPublicConstant.HELP, ServiceType.Help);
		exactMatchContent.put(WeiXinPublicConstant.DOWNLOAD, ServiceType.Download);
	}
	static {
		//fuzzyMatchContent.put("", null);
	}
	static {
		startMatchContent.put("http://", ServiceType.SaveLink);
		startMatchContent.put("https://", ServiceType.SaveLink);
	}
	
	@Override
	public ServiceType getServiceType(WeiXinPublicRequestEntity request) {
		this.setContent(request);
		this.serviceType = this.getServiceTypeByExactMatchContent();
		if(this.serviceType == null) {
			this.serviceType = this.getServiceTypeByStartMatchContent();
		}
		if(this.serviceType == null) {
			this.serviceType = this.getServiceTypeByFuzzyMatchContent();
		}
		return null;
	}
	
	protected void setContent(WeiXinPublicRequestEntity request) {
		this.content = request.getContent();
		this.content = StringUtils.trimToEmpty(this.content);
		this.handleRepeatCommand();
	}
	
	private void handleRepeatCommand() {
		if(this.content.length()<1)return;
		if(this.content.length() > WeiXinPublicConstant.REPEAT_COMMAND_NUM)return;
		this.resetCommand(WeiXinPublicConstant.HELP);
		this.resetCommand(WeiXinPublicConstant.GetLastestContent);
		this.resetCommand(WeiXinPublicConstant.DOWNLOAD);
	}
	
	private void resetCommand(String command) {
		if(StringUtils.isEmpty(command))return;
		if(this.content.toLowerCase().startsWith(command.toLowerCase())) {
			if(this.content.equalsIgnoreCase(this.cycleRepeatCommand(command, this.content.length()))) {
				this.content = command;
			}
		}
	}
	
	private String cycleRepeatCommand(String command, int length) {
		String temp = "";
		for(int i=0;i<length;i++) {
			temp = temp + command;
		}
		return temp;
	}
	
	protected ServiceType getServiceTypeByExactMatchContent() {
		ServiceType serviceType = null;
		for(String key:exactMatchContent.keySet()) {
			if(key == null)continue;
			if(key.equalsIgnoreCase(this.content)) {
				serviceType = exactMatchContent.get(key);
				break;
			}
		}
		return serviceType;
	}
	
	protected ServiceType getServiceTypeByFuzzyMatchContent() {
		ServiceType serviceType = null;
		if(this.content == null)return null;
		
		for(String key:fuzzyMatchContent.keySet()) {
			if(key == null)continue;
			if(this.content.indexOf(key) > -1) {
				serviceType = fuzzyMatchContent.get(key);
				break;
			}
		}
		return serviceType;
	}
	
	protected ServiceType getServiceTypeByStartMatchContent() {
		ServiceType serviceType = null;
		if(this.content == null)return null;
		
		for(String key:startMatchContent.keySet()) {
			if(key == null)continue;
			if(this.content.toLowerCase().startsWith(key.toLowerCase())) {
				serviceType = startMatchContent.get(key);
				break;
			}
		}
		return serviceType;
	} 
}
