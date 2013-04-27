package me.twocoffee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.push.apns.ApnsServiceFactory;
import me.twocoffee.entity.Badge.BadgeName;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.PayloadBuilder;

@Service
public class PushServiceImpl implements PushService {

    private final static Logger logger = LoggerFactory
	    .getLogger(PushServiceImpl.class);
    @Autowired
    private ApnsServiceFactory apnsServiceFactory;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private NotificationService notificationService;

    protected void iosPush(Notification notification, Device device, int badge,
	    boolean alert) {

	Map<String, String> param = new HashMap<String, String>();
	param.put("action", notification.getAction());
	param.put("id", notification.getId());
	PayloadBuilder builder = APNS.newPayload().badge(badge)
		.customFields(param);

	if (alert && device.isPushAlert()) {
	    builder.alertBody(notification.getSubject()).sound("default");
	}
	String content = builder.build();

	try {
	    apnsServiceFactory.getInstance().push(device.getDeviceToken(),
		    content);

	    long begin = System.currentTimeMillis();
	    logger.info("Ios push:Account:[{}],deviceToken[{}],{}, - {}ms",
		    new Object[] { device.getAccountId(),
			    device.getDeviceToken(), content,
			    System.currentTimeMillis() - begin });

	} catch (Exception e) {
	    logger.error("iosPush exception:", e);
	}
    }

    private void wp7Push(Notification notification, Device device, int badge,
	    boolean alert) {
	if (device.getPushURI() == null) {
	    return;
	}
	// TODO wp7 push
    }

    @Override
    public void push(Notification notification, Device device) {
	if (device.getDeviceToken() == null) {
	    return;
	}
	int badge = badgeService.getBadge(device.getAccountId(),
		BadgeName.total);// push的是总数
	// boolean alert = notification.getKey().pushAlert();//
	// 根据key的类型来决定是否alert
	boolean alert = device.getPushAlert();
	iosPush(notification, device, badge, alert);
	wp7Push(notification, device, badge, alert);
    }

    @Override
    public void push(Notification notification, String accountId) {
	List<Device> devices = deviceService.getByAccountId(accountId);
	if (devices != null) {
	    for (Device device : devices) {
		push(notification, device);

	    }
	}

    }

    @Override
    public void testPush(Notification notification, String accountId, int badge) {
	List<Device> devices = deviceService.getByAccountId(accountId);
	notification.setAccountId(accountId);
	notificationService.save(notification);
	if (devices != null) {
	    for (Device device : devices) {
		if (device.getDeviceToken() == null) {
		    return;
		}
		boolean alert = notification.getKey().pushAlert();// 根据key的类型来决定是否alert
		iosPush(notification, device, badge, alert);
		wp7Push(notification, device, badge, alert);
	    }
	}
    }

    public ApnsServiceFactory getApnsServiceFactory() {
	return apnsServiceFactory;
    }

    public void setApnsServiceFactory(ApnsServiceFactory apnsServiceFactory) {
	this.apnsServiceFactory = apnsServiceFactory;
    }

}
