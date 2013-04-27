package me.twocoffee.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.dao.BadgeDao;
import me.twocoffee.dao.NotificationDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Badge;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.service.NotificationService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private BadgeDao badgeDao;
	@Autowired
	private NotificationDao notificationDao;

	@Override
	public int countUnread(String accountId) {
		return notificationDao.countUnRead(accountId);
	}

	@Override
	public void deleteByAccountIdAndKey(String accountId,
			Notification.CollapseKey key) {
		Notification n = notificationDao.findByAccountIdAndKey(accountId, key);
		if (n != null) {
			notificationDao.updateStatus(n.getId(),
					Notification.Status.deleted);
			badgeDao.resetBadge(accountId, n.getKey().getBadgeName());
		}
	}

	@Override
	public void deleteOneByAccountIdAndKey(String accountId,
			CollapseKey friendmessage) {
/*
		Notification n = notificationDao.findByAccountIdAndKey(accountId,
				friendmessage);*/

		Badge badge = badgeDao.getBadgeByAccountId(accountId);
		boolean flag = false;

		if (badge !=null && badge.getFlag() != null
				&& StringUtils.isNotBlank(String.valueOf(badge.getFlag().get(
						friendmessage.getBadgeName().toString())))) {

			flag = (Boolean) badge.getFlag().get(
					friendmessage.getBadgeName().toString());
		}

		/*if (n != null && flag == false
				&& n.getStatus().equals(Notification.Status.normal)) {*/
			if (badgeDao.getBadgeByAccountId(accountId,
					friendmessage.getBadgeName()) > 0) {

				badgeDao.increaseBadge(accountId, friendmessage.getBadgeName(),
						-1);

				Map<String, Object> flags = new HashMap<String, Object>();
				flags.put(friendmessage.getBadgeName().toString(), true);
				badge.setFlag(flags);
				badgeDao.save(badge);
				/*String sub = n.getSubject().substring(1);
				n.setSubject(badgeDao.getBadgeByAccountId(accountId,
						friendmessage.getBadgeName()) + sub);
				int bg = badgeDao.getBadgeByAccountId(accountId,
						friendmessage.getBadgeName());
				/*if (bg == 0)
					notificationDao.updateStatus(n.getId(),
							Notification.Status.deleted);
				else
					notificationDao.save(n);

			}*/

		}
	}

	@Override
	public List<Notification> getLatestNotifications(String accountId,
			String prevousId, String displayType) {
		return notificationDao.getUnreadNotificationsByPrevousId(accountId,
				prevousId, displayType);
	}

	@Override
	public Notification getNotificationById(String id) {
		return notificationDao.getById(id);
	}

	@Override
	public void read(String accountId, String notificationId) {
		notificationDao.updateStatus(notificationId,
				Notification.Status.deleted);
		Notification n = notificationDao.getById(notificationId);

		// TODO 如果已读，
		badgeDao.resetBadge(accountId, n.getKey().getBadgeName());

	}

	@Override
	public void save(Notification notification) {
		// upsert by account+key
		notificationDao.save(notification);
	}
	
	@Override
	public Notification createAndSave(Notification.CollapseKey collapseKey, Account targetAccount, Account account, Content content) {
		
		if (collapseKey == Notification.CollapseKey.RecommendByFriend) {
			
			if (StringUtils.isBlank(content.getTitle())) {
				
				if (content.getContentType() == ContentType.Image) {
					content.setTitle("分享图片");
				}
			}
			Notification notification = new Notification();// 生成系统通知并存储
															// notification
			notification.setKey(collapseKey);
			// badgeService.increaseBadge(targetAccount.getId(),
			// notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
			notification.setAccountId(targetAccount.getId());
			notification.setAction(notification.getKey().getAction()
					+ content.getId());
			notification.setStatus(Notification.Status.normal);
			// notification.setSubject(badge + "条好友推荐内容");
			notification.setSubject(account.getName() + "给你分享了《"
					+ content.getTitle() + "》，点击查看详情!");

			save(notification);
			return notification;
		}
		return null;
	}

}
