package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.BadgeDao;
import me.twocoffee.entity.Badge;
import me.twocoffee.entity.Badge.BadgeModel;
import me.twocoffee.entity.Badge.BadgeName;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@org.springframework.stereotype.Repository
public class BadgeDaoImpl extends BaseDao<Badge> implements BadgeDao {

	private final static Logger logger = LoggerFactory
			.getLogger(BadgeDaoImpl.class);

	private static Object getFieldValue(Object obj, String field) {
		Object value = 0;
		try {
			value = obj
					.getClass()
					.getMethod(
							"get" + field.substring(0, 1).toUpperCase()
									+ field.substring(1))
					.invoke(obj, new Object[] {});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return value;
	}

	@Override
	public Badge getBadgeByAccountId(String accountId) {
		return this.dataStore.createQuery(Badge.class).field("accountId")
				.equal(accountId).get();
	}

	@Override
	public int getBadgeByAccountId(String accountId, BadgeName badgeName) {
		Badge badge = getBadgeByAccountId(accountId);
		if (badge != null) {
			return (Integer) getFieldValue(badge, badgeName.name());
		}
		return 0;
	}

	@Override
	public void increaseBadge(String accountId, BadgeName badgeName) {
		BadgeModel bm = Badge.badgeModelMap.get(badgeName.name());
		if (bm == null || !bm.isLeaf()) {
			return;
		}
		List<String> names = new ArrayList<String>();
		Badge.findParentName(bm.getParent(), names);// 寻找这个badgeName的所有父级
		Query<Badge> query = this.dataStore.createQuery(Badge.class)
				.field("accountId").equal(accountId);
		Badge org = query.get();
		if (org == null) {
			// 之前不存在，先保存一个新的对象
			org = new Badge();
			org.setAccountId(accountId);
			save(org);
		}
		UpdateOperations<Badge> ops = this.dataStore
				.createUpdateOperations(
						Badge.class).inc(badgeName.name());// 这个badgeName++
		for (String name : names) {// 迭代所有父级，父级上的数字都要++
			ops.inc(name, 1);
		}
		this.dataStore.update(query, ops);
	}

	@Override
	public void increaseBadge(String accountId, BadgeName badgeName, int i) {
		BadgeModel bm = Badge.badgeModelMap.get(badgeName.name());
		if (bm == null || !bm.isLeaf()) {
			return;
		}
		List<String> names = new ArrayList<String>();
		Badge.findParentName(bm.getParent(), names);// 寻找这个badgeName的所有父级
		Query<Badge> query = this.dataStore.createQuery(Badge.class)
				.field("accountId").equal(accountId);
		Badge org = query.get();
		if (org == null) {
			// 之前不存在，先保存一个新的对象
			org = new Badge();
			org.setAccountId(accountId);
			save(org);
		}
		UpdateOperations<Badge> ops = this.dataStore
				.createUpdateOperations(
						Badge.class).inc(badgeName.name());// 这个badgeName++
		for (String name : names) {// 迭代所有父级，父级上的数字都要++
			ops.inc(name, i);
		}
		this.dataStore.update(query, ops);
	}

	@Override
	public void resetBadge(String accountId, BadgeName badgeName) {
		BadgeModel bm = Badge.badgeModelMap.get(badgeName.name());
		if (bm == null || !bm.isLeaf()) {
			return;
		}
		List<String> names = new ArrayList<String>();
		Badge.findParentName(bm.getParent(), names);// 寻找这个badgeName的所有父级
		Query<Badge> query = this.dataStore.createQuery(Badge.class)
				.field("accountId").equal(accountId);
		Badge org = query.get();
		if (org == null) {
			// 之前不存在，直接保存一个新的对象
			org = new Badge();
			org.setAccountId(accountId);
			save(org);
			return;
		}
		int orgVal = (Integer) getFieldValue(org, badgeName.name());// 当前这个badgeName对应的值
		UpdateOperations<Badge> ops = this.dataStore
				.createUpdateOperations(
						Badge.class).set(badgeName.name(), 0);// 这个badgeName重置成0
		for (String name : names) {// 迭代所有父级，父级上的数字都要减去orgVal
			int val = (Integer) getFieldValue(org, name);
			ops.set(name, val - orgVal < 0 ? 0 : val - orgVal);
		}
		this.dataStore.update(query, ops);
	}

	@Override
	public void save(Badge badge) {
		if (badge != null && badge.getId() == null) {
			badge.setId(new ObjectId().toString());
		}
		this.dataStore.save(badge);
	}
}
