/**
 * 
 */
package me.twocoffee.dao;

import java.util.Date;
import java.util.List;

import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

/**
 * @author momo
 * 
 */
public interface ThirdPartyContentDao {

	List<ThirdPartyContent> findAfter(
			ThirdPartyType accountType,
			String userId, Date syncDate);

	ThirdPartyContent getLatestContent(
			ThirdPartyType accountType, String userId);

	void save(List<ThirdPartyContent> contents);

}
