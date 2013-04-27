package me.twocoffee.service.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.RepositoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SyncGuestRepositoryListener implements ApplicationListener<SyncGuestRepositoryEvent> {
	@Autowired
    private RepositoryService repositoryService;
	@Autowired
    private ContentSearcher contentSearcher;
	@Override
	public void onApplicationEvent(SyncGuestRepositoryEvent event) {
		if(TokenUtil.isEmptyAuthorization(event.getAuthorization())) {
			return;
		}
		List<Repository> guestRepositoryList = null; 
		//sync webToken
		if(!TokenUtil.isEmptyWebToken(event.getAuthorization())) {
			guestRepositoryList = repositoryService.findRepositoryByAccountId(
					TokenUtil.extractWebToken(event.getAuthorization()));
		}
		//sync clientId
		else if(!TokenUtil.isEmptyClientId(event.getAuthorization())) {
			guestRepositoryList = repositoryService.findRepositoryByAccountId(
					TokenUtil.extractClientId(event.getAuthorization()));
		}
		if(guestRepositoryList == null || guestRepositoryList.size() < 1) {
			return;
		}
		List<Repository> userRepositoryList =repositoryService.findRepositoryByAccountId(event.getAccountId());
		Map<String,String> userRepositoryMap = this.getUserRepositoryMap(userRepositoryList);
		for(Repository repo:guestRepositoryList) {
			if(repo == null)continue;
			//content exesit
			if(userRepositoryMap.containsValue(repo.getContentId())) {
				continue;
			}
			repo.setAccountId(event.getAccountId());
			List<String> tagList = repo.getTag();
			tagList.add(SystemTagEnum.Unread.toString());
			repo.setTag(this.getTag(repo.getTag()));
			repo.setLastModified(new Date());
			try {
				repositoryService.save(repo);
				contentSearcher.updateIndex(repo.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<String> getTag(List<String> tagList) {
		if(tagList == null) return null;
		
		if(repositoryService.isContainTag(tagList, SystemTagEnum.Unread.toString())) {
			return tagList;
		}
		if(repositoryService.isContainTag(tagList, SystemTagEnum.Collect.toString())) {
			tagList.add(SystemTagEnum.Unread.toString());
		}
		else if(repositoryService.isContainTag(tagList, SystemTagEnum.Later.toString())) {
			tagList.add(SystemTagEnum.Unread.toString());
		}
		return tagList;
	}
	
	private Map<String,String> getUserRepositoryMap(List<Repository> userRepositoryList) {
		Map<String,String> userRepositoryMap = new HashMap<String, String>();
		if(userRepositoryList == null || userRepositoryList.size()<1) {
			return userRepositoryMap;
		}
		for(Repository repo:userRepositoryList) {
			if(repo == null)continue;
			userRepositoryMap.put(repo.getId(), repo.getContentId());
		}
		return userRepositoryMap;
	}

}
