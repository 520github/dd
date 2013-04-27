package me.twocoffee.dao;

import me.twocoffee.entity.DocQueue;

public interface DocQueueDao {
	/**
	 * 根据id删除一个doc
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 从队列里取出第一个并修改状态为已取出
	 * 
	 * @param serviceId
	 * @return
	 */
	public DocQueue peek(String serviceId);

	/**
	 * 根据服务id重置所有相关的Doc，把正在处理的Doc置为未处理
	 * 
	 * @param serviceId
	 */
	public void resetQueueByServiceId(String serviceId);

	/**
	 * 保存
	 * 
	 * @param attachmentDoc
	 */
	public void save(DocQueue docQueue);

	public void delete(DocQueue doc);
}
