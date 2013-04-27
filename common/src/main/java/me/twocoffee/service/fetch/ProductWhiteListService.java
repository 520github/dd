package me.twocoffee.service.fetch;

import me.twocoffee.entity.Content;

public interface ProductWhiteListService {
	/**
	 * 如果url在白名单中，返回相应的模板名称
	 * 
	 * 否则返回null，表示不在白名单内
	 * 
	 * @param url
	 * @return
	 */
	public String getModelName(String url);

	/**
	 * 得到商品的详细信息，封装在content中
	 * 
	 * @param url
	 *            网页url
	 * @param modelName
	 *            模板名称
	 * @return
	 */
	public Content getProductDetail(String url, String modelName);
}
