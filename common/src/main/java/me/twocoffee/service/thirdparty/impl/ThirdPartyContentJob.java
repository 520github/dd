/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.util.ArrayList;
import java.util.List;

import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class ThirdPartyContentJob {

    @Autowired
    private ThirdpartyService thirdpartyService;

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ThirdPartyContentJob.class);

    public void execute() {
	LOGGER.debug("execute ThirdPartyContentJob!");
	int limit = 50;
	int offset = 0;
	List<ThirdPartyProfile> profiles = new ArrayList<ThirdPartyProfile>();

	while ((profiles = getProfiles(limit, offset)).size() > 0) {

	    for (ThirdPartyProfile profile : profiles) {

		try {
		    LOGGER.debug(
			    "execute fetch {} content. accountId {} uid {}",
			    new Object[] { profile.getAccountType(),
				    profile.getAccountId(), profile.getUserId() });

		    thirdpartyService.syncThirdPartyContent(profile);

		} catch (ThirdPartyAuthExpiredException e) {
		    LOGGER.error(ThirdPartyContentJob.class.getName(), e);

		} catch (ThirdPartyAccessException e) {
		    LOGGER.error(ThirdPartyContentJob.class.getName(), e);

		} catch (ThirdPartyUnbindException e) {
		    LOGGER.error(ThirdPartyContentJob.class.getName(), e);

		} catch (Exception e) {
		    LOGGER.error(ThirdPartyContentJob.class.getName(), e);

		} finally {
		    LOGGER.debug("end execute fetch thirdparty content!");
		}
	    }
	    offset += profiles.size();
	}
    }

    private List<ThirdPartyProfile> getProfiles(int limit, int offset) {
	List<ThirdPartyType> types = new ArrayList<ThirdPartyProfile.ThirdPartyType>();
	types.add(ThirdPartyType.Renren);
	types.add(ThirdPartyType.Weibo);
	types.add(ThirdPartyType.Tencent);
	return thirdpartyService.getByThirdpartyTypes(types, limit, offset);
    }
}
