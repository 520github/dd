package me.twocoffee.service.event;

import me.twocoffee.entity.Notification;
import me.twocoffee.service.BadgeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 请求加好友监听器
 * 
 * @author chongf
 * 
 */
@Component
public class ReadLaterListener implements
		ApplicationListener<ReadLaterEvent> {

	@Autowired
	private BadgeService badgeService;

	@Override
	public void onApplicationEvent(ReadLaterEvent event) {
		Notification notification = new Notification();
		notification.setKey(Notification.CollapseKey.readLater);

		// TODO 统计badge数字，相应字段badge数++
		badgeService.increaseBadge(event.getAccountId(),
				notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
		int badge = badgeService.getBadge(
				event.getAccountId(), notification.getKey()
						.getBadgeName());// badge 数字
		if (badge < 1)
			badge = 1;

		// TODO 生成系统通知并存储 notification
		notification.setAccountId(event.getAccountId());
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		notification.setSubject(badge + "条未读系统消息");
		// notificationService.save(notification);

		// TODO 生成消息并存储 message

		// TODO push消息
		// pushService.push(notification, event.getAccountId());
	}

}
