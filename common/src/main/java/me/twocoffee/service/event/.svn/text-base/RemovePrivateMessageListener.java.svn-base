/**
 * 
 */
package me.twocoffee.service.event;

import java.util.List;

import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentAndAccount;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class RemovePrivateMessageListener implements
		ApplicationListener<RemovePrivateMessageEvent> {

	@Autowired
	private RepositoryService repositoryService;

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(RemovePrivateMessageEvent event) {

		if (event.getSource() != null) {
			List<ContentAndAccount> targets = (List<ContentAndAccount>) event
					.getSource();

			if (targets.size() == 1
					&& StringUtils.isNotBlank(targets.get(0).getAccountId())
					&& StringUtils.isBlank(targets.get(0).getContentId())
					&& StringUtils.isBlank(targets.get(0).getTargetId())) {

				repositoryService.clearAllFriendSessionBadge(targets.get(0)
						.getAccountId());

			} else {
				repositoryService
						.clearFriendSessionBadge(targets);

			}
		}
	}
}
