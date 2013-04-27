package me.twocoffee.service.event;

import java.util.Arrays;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.Repository;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.event.message.AuthenticationSuccessEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 将多多助手默认添加给用户.并且分享默认内容给用户 这个逻辑会在用户第一次登录的时候进行.
 * 如果这个用户没有见过多多助手（stat属性中没有“KnowDuoduoAssistant”），那么系统会默认让他关联多多助手
 * 
 * @author Dante
 * 
 */
@Component
public class DefaultFriendAssigner implements
		ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultFriendAssigner.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private FriendService friendService;
	@Autowired
	private RepositoryService repositoryService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Account account = event.getAccount();
		logger.info("[{}]", account.getLoginName());
		if (!account.hasStatus(Status.KnowDuoduoAssistant)) {

			try {
				Account recommendedAccount = accountService
						.getRecommendAccount();
				if (recommendedAccount != null) {
					if (friendService.addFriendWithoutLog(account.getId(),
							recommendedAccount.getId())) {
						Repository recommendedContent = repositoryService
								.getLatestByAccount(
								recommendedAccount.getId());
						if (recommendedContent != null) {
							friendService.shareToFriends(
									recommendedAccount.getId(),
									recommendedContent.getId(),
									Arrays.asList(account.getId()),
									5,
									"");
						}
					}
				}
				account.addStat(Status.KnowDuoduoAssistant);
				accountService.save(account);
			} catch (Exception e) {
				logger.error("Failed to assign friend to {}",
						account.getLoginName(), e);
			}
		}
	}
}