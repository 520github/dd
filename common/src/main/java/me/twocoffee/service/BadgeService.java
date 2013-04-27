package me.twocoffee.service;

import me.twocoffee.entity.Badge;

public interface BadgeService {
	/**
	 * 获取某个用户的全部badge信息
	 * 
	 * @param accountId
	 */
	public Badge getBadge(String accountId);

	/**
	 * 获取某个用户的单个badge信息
	 * 
	 * @param accountId
	 * @param badgeName
	 * @return
	 */
	public int getBadge(String accountId, Badge.BadgeName badgeName);

	/**
	 * 某个badge数目加一
	 * 
	 * @param badgeName
	 * @return
	 */
	public void increaseBadge(String accountId, Badge.BadgeName badgeName);

	/**
	 * 重置某个badge 这个badge数会被清零，同时其父级的badge会减去badge之前的数目
	 * 
	 * @param badgeName
	 * @return
	 */
	public void resetBadge(String accountId, Badge.BadgeName badgeName);
}
