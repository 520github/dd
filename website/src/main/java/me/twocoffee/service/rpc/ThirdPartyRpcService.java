/**
 * 
 */
package me.twocoffee.service.rpc;

import java.util.List;

import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.ContactResult;

/**
 * @author xiangjun.yu
 * 
 */
public interface ThirdPartyRpcService {

	List<ContactResult> getSuggestThirdPartyFriends(String token, int limit,
			int offset);

	List<ContactResult> getThirdPartyFriends(String token, ThirdPartyType weibo);

	/**
	 * 设置第三方帐户微博同步状态
	 * 
	 * @param token
	 * @param contentSynchronize
	 * @return
	 */
	int setSyncStatus(String token, String contentSynchronize);

	int unbind(String token);

}
