/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.event.BindThirdPartyListener;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时检查第三方授权状态。
 * 
 * @author momo
 * 
 */
@Component
public class ThirdPartyAuthJob {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ThirdpartyService thirdpartyService;

    @Autowired
    private BindThirdPartyListener bindThirdPartyListener;

    private static final int DEFAULT_FETCH_SIZE = 50;

    private Map<ThirdPartyType, Integer> fetchSizes = new HashMap<ThirdPartyType, Integer>(
	    0);

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ThirdPartyContentJob.class);

    public void execute() {

	// for (ThirdPartyType thirdPartyType : ThirdPartyType.values()) {
	LOGGER.debug("do auth job:{}",
		ThirdPartyType.Weibo.toString());

	String displayActionName = "授权";
	String subject = "您的新浪微博帐号授权已过期，请重新授权";
	String href = "iicoffee://view/thirdparty/weibo/authority";
	doAuthJobPerType(ThirdPartyType.Weibo, displayActionName, subject, href);
	LOGGER.debug("do auth job:{}",
		ThirdPartyType.Renren.toString());

	String renren_displayActionName = "授权";
	String renren_subject = "您的人人帐号授权已过期，请重新授权";
	String renren_href = "iicoffee://view/thirdparty/renren/authority";
	doAuthJobPerType(ThirdPartyType.Renren, renren_displayActionName,
		renren_subject, renren_href);

	LOGGER.debug("do auth job:{}",
		ThirdPartyType.Tencent.toString());

	String qq_displayActionName = "授权";
	String qq_subject = "您的腾讯微博授权已过期，请重新授权";
	String qq_href = "iicoffee://view/thirdparty/tencent/authority";
	doAuthJobPerType(ThirdPartyType.Tencent, qq_displayActionName,
		qq_subject, qq_href);
	// }
    }

    /**
     * @param thirdPartyType
     * @param href
     * @param subject
     * @param displayActionName
     */
    private void doAuthJobPerType(ThirdPartyType thirdPartyType,
	    String displayActionName, String subject, String href) {

	int limit = this.fetchSizes.get(thirdPartyType) == null
		|| this.fetchSizes.get(thirdPartyType) <= 0 ? DEFAULT_FETCH_SIZE
		: this.fetchSizes.get(thirdPartyType);

	int offset = 0;
	List<ThirdPartyProfile> profiles = new ArrayList<ThirdPartyProfile>();
	Date min = DateUtil.getBeginDate(new Date());
	Date max = DateUtils.addDays(min, 1);

	while ((profiles = thirdpartyService.getByThirdpartyType(
		thirdPartyType, true, max, min, limit, offset)).size() > 0) {

	    for (ThirdPartyProfile profile : profiles) {
		doAuthJobPerProfile(profile, displayActionName, subject, href);
	    }
	    offset += profiles.size();
	}
    }

    /**
     * @param profile
     * @param href
     * @param subject
     * @param displayActionName
     */
    private void doAuthJobPerProfile(ThirdPartyProfile profile,
	    String displayActionName, String subject, String href) {

	Account account = accountService.getById(profile.getAccountId());
	bindThirdPartyListener.bindThirdPartyWithoutEvent(account, true,
		subject, displayActionName, href, profile.getAccountType());

	LOGGER.debug("send message : account {}  thirdparty {}  reauth!",
		new Object[] { profile.getAccountId(),
			profile.getAccountType().toString() });

    }
}
