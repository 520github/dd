package me.twocoffee.service.event;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.WebTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UnbindAccountListener implements ApplicationListener<UnbindAccountEvent> {
	@Autowired
	private WebTokenService webTokenService;
	@Autowired
	private DeviceService deviceService;
	
	@Override
	public void onApplicationEvent(UnbindAccountEvent event) {
		if(TokenUtil.isEmptyAuthorization(event.getAuthorization())) {
			return;
		}
		//unbind webToken
		if(!TokenUtil.isEmptyWebToken(event.getAuthorization())) {
			webTokenService.unbingAccount(
					TokenUtil.extractWebToken(event.getAuthorization()));
		}
		//unbind clientId
		else if(!TokenUtil.isEmptyClientId(event.getAuthorization())) {
			deviceService.unbindAcount(
					TokenUtil.extractClientId(event.getAuthorization()));
		}
	}
}
