package me.twocoffee.dao;

import me.twocoffee.entity.Badge;
import me.twocoffee.entity.Badge.BadgeName;

public interface BadgeDao {
	/**
	 * 根据账户获取全部的badge信息
	 * 
	 * @param accountId
	 * @return
	 */
	public Badge getBadgeByAccountId(String accountId);

	/**
	 * 根据账户获取某个badge信息
	 * 
	 * @param accountId
	 * @return
	 */
	public int getBadgeByAccountId(String accountId, Badge.BadgeName badgeName);

	/**
	 * 某个badge数目加一
	 * 
	 * @param badgeName
	 * @return
	 */
	public void increaseBadge(String accountId, BadgeName badgeName);

	public void increaseBadge(String accountId, BadgeName badgeName, int i);

	/**
	 * 重置某个badge 这个badge数会被清零，同时其父级的badge会减去badge之前的数目
	 * 
	 * @param badgeName
	 * @return
	 */
	public void resetBadge(String accountId, Badge.BadgeName badgeName);

	/**
	 * 保存一个badge统计对象
	 * 
	 * @param badge
	 */
	public void save(Badge badge);
}
