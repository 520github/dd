/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher;

import java.util.Map;

import me.twocoffee.service.fetch.impl.product.fetcher.visitor.ResultHandler;

/**
 * @author momo
 * 
 */
public interface IProductVisitor {

	Map<String, String> getResult(String url);

	void setResultHandler(ResultHandler resultHandler);
}
