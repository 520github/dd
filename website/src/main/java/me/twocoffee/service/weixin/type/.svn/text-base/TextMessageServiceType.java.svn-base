package me.twocoffee.service.weixin.type;

import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicConstant.ServiceType;

public class TextMessageServiceType extends AbstractServiceType {
	@Override
	public ServiceType getServiceType(WeiXinPublicRequestEntity request) {
		super.getServiceType(request);
		if(this.serviceType == null) {
			this.serviceType = ServiceType.SaveText;
		}
		return this.serviceType;
	}
}
