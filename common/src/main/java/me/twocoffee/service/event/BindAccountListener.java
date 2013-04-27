package me.twocoffee.service.event;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.WebTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BindAccountListener implements ApplicationListener<BindAccountEvent> {
	@Autowired
	private WebTokenService webTokenService;
	@Autowired
	private DeviceService deviceService;
	
	@Override
	public void onApplicationEvent(BindAccountEvent event) {
		if(TokenUtil.isEmptyAuthorization(event.getAuthorization())) {
			return;
		}
		//bind webToken
		if(!TokenUtil.isEmptyWebToken(event.getAuthorization())) {
			webTokenService.bindAccount(
					TokenUtil.extractWebToken(event.getAuthorization()), event.getAccountId(), event.getUserType());
		}
		//bind clientId
		else if(!TokenUtil.isEmptyClientId(event.getAuthorization())) {
			deviceService.bindAccount(
					TokenUtil.extractClientId(event.getAuthorization()), event.getAccountId(), event.getUserType());
		}
	}

}
