package me.twocoffee.service.rpc;

import java.util.List;
import java.util.Map;

import me.twocoffee.entity.ResponseContent;
import me.twocoffee.rest.generic.GenericContent;
import me.twocoffee.service.rpc.ContentServiceHttpImpl.PostContent;

public interface ContentRpcService {

	/**
	 * @param authToken
	 * @param content
	 * @return
	 */
	public Map postContent(String authToken, PostContent postContent)
			throws ContentAlreadyCollectException;

	/**
	 * @param url
	 * @return
	 */
	public Map getContentByUrl(String contentType, String url, String referer);

	/**
	 * 分享给朋友
	 * @param token
	 * @param repositoryId
	 * @param friendId
	 * @return
	 */
	public boolean shareToFriend(String token, String repositoryId, List<String> friendId, List<String> thirdPartyfriendId, int score, String comment);
	
	
	/**
	 * 获取一条收藏关系与内容信息
	 * @param repositoryId
	 * @param authToken
	 * @param userAgent
	 * @return
	 */
	public ResponseContent getResponseContentById(String repositoryId, String authToken, String userAgent);
	
	
	
	/**
	 * 更新收藏关系
	 * @param repositoryId
	 * @param authToken
	 * @param genericContent
	 * @return
	 */
	public String updateRepository(String repositoryId, String authToken, GenericContent genericContent);
	
	/**
	 * 获得公共内容
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	public ResponseContent getPublicContent(String accountId,String contentId);
	
	/**
	 * 根据收藏关系ID获取收藏内容信息
	 * @param repositoryId
	 * @return
	 */
	public ResponseContent getResponseContentByRepositoryId(String repositoryId);
}
