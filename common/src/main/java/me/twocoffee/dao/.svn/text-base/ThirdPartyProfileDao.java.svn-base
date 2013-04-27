/**
 * 
 */
package me.twocoffee.dao;

import java.util.Date;
import java.util.List;

import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

/**
 * @author momo
 * 
 */
public interface ThirdPartyProfileDao {

	List<ThirdPartyProfile> findByAccountId(String accountId);

	ThirdPartyProfile findByTypeAndAccountId(ThirdPartyType type,
			String accountId);

	ThirdPartyProfile findByTypeAndId(ThirdPartyType type, String userId);

	ThirdPartyProfile findByTypeAndId(ThirdPartyType type, String userId,
			boolean bind);

	ThirdPartyProfile getByTypeAndID(ThirdPartyType type, String userId,
			boolean b);

	void save(ThirdPartyProfile profile);

	/**
	 * @param type
	 * @param bind
	 * @param max
	 * @param min
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<ThirdPartyProfile> findByTypeAndAccountId(ThirdPartyType type,
			boolean bind, Date max, Date min, int limit, int offset);

	/**
	 * @param thirdpartyProfile
	 */
	void updateToken(ThirdPartyProfile thirdpartyProfile);

	void delete(ThirdPartyProfile p);

	List<ThirdPartyProfile> getByTypes(List<ThirdPartyType> types, int limit,
			int offset);

}
