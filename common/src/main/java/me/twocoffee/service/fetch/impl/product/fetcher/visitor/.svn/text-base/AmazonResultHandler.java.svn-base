/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service("amazonResultHandler")
@Scope("prototype")
public class AmazonResultHandler implements ResultHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.fetch.impl.product.fetcher.visitor.ResultHandler
	 * #handle(java.util.Map)
	 */
	@Override
	public Map<String, String> handle(Map<String, String> result) {
		String price = result.get("price");
		price = price.substring(2).trim();
		result.put("price", price);
		return result;
	}

}
