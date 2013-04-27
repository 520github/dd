/**
 * 
 */
package me.twocoffee.dao;

import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.ThirdPartyProfile;

/**
 * @author wenjian
 *
 */
public interface ThirdPartyContentSynchronizeLogDao {

	void saveContentSynchronizeLog(
			ThirdPartyContentSynchronizeLog contentSynchronizeLog);

	ThirdPartyContentSynchronizeLog findThirdPartyContentSynchronizeLog(
			ThirdPartyProfile thirdpartyProfile);

}
