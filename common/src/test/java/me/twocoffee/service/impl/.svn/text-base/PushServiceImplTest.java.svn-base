package me.twocoffee.service.impl;

import me.twocoffee.common.push.apns.ApnsServiceFactoryImpl;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.Notification;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PushServiceImplTest {

    private PushServiceImpl pushService = new PushServiceImpl();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIosPush() {
	ApnsServiceFactoryImpl factory = new ApnsServiceFactoryImpl();
	factory.setPassword("1234");
	factory.setSandBox(false);
	pushService.setApnsServiceFactory(factory);
	Notification notification = new Notification();
	notification.setAction("iicoffee://view/relation/friend/message/");
	notification.setId(new ObjectId().toString());
	notification.setSubject("测试推送");
	Device device = new Device();
	device.setPushAlert(true);
	// device.setAccountId("50650f63e4b0099063b41d98");
	device.setAccountId("507e23d5e4b0493c4a84ba56");
	// device.setDeviceToken("90b1214e3107cbe49e4563ce9d2a84d21ab9e34f560a89c8f204de8eb84095c4");
	device.setDeviceToken("39eb5831deee91e657ce0f7856f02e48d514bc5ab0593c6a1677a6c9ad3f6ba8");
	// device.setDeviceToken("1f9c95ad920d0f9af03243e136962e9ebdf6852c771e71d998b0a4fe0bac8799");
	// device.setDeviceToken("50f7d065e4b0d2445dc30047");
	device.setPushAlert(true);
	// for (int i = 0; i < 20; i++) {
	pushService.iosPush(notification, device, 1, device.getPushAlert());
	// }
    }

}
