package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Acknowledgment;


public interface AcknowledgmentService {

	/**
	 * 发送答谢
	 * @param accountId
	 * @param contentId
	 */
	boolean sendAcknowledgment(String accountId,String contentId);
	
	/**
	 * 判断答谢是否可用
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	boolean Available(String accountId,String contentId);
	
	
	/**
	 * 查询答谢列表总数
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	int totalByAccountIdAndContent(String accountId, String contentId);
	
	/**
	 * 查询答谢列表
	 * @param accountId
	 * @param contentId
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<Acknowledgment> findByAccountIdAndContentId(String accountId,String contentId,int limit,int offset);
	
	/**
	 * 删除答谢关系
	 * @param acknowledgment
	 */
	void delete(Acknowledgment acknowledgment);
	
	boolean sendAcknowledgmentToQueue(String accountId,String contentId);
}
