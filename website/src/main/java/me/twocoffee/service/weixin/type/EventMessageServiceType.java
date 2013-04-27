package me.twocoffee.service.weixin.type;

import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicConstant.ServiceType;

public class EventMessageServiceType extends AbstractServiceType {
	@Override
	public ServiceType getServiceType(WeiXinPublicRequestEntity request) {
		super.getServiceType(request);
		if(this.serviceType == null) {
			this.serviceType = ServiceType.Hello;
		}
		return this.serviceType;
	}
	
	protected void setContent(WeiXinPublicRequestEntity request) {
		this.content = request.getEventKey();
	}
}
