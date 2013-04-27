/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.SpringContext;

/**
 * @author momo
 * 
 */
public class SpringContextVisitorFactory implements IVisitorFactory {

	private Map<String, String> visitorNames = new HashMap<String, String>();

	public Map<String, String> getVisitorNames() {
		return visitorNames;
	}

	public void setVisitorNames(Map<String, String> visitorNames) {
		this.visitorNames = visitorNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.fetch.impl.product.fetcher.IVisitorFactory#getByBeanName
	 * (java.lang.String)
	 */
	@Override
	public IProductVisitor getByBeanName(String modelName) {
		return (IProductVisitor) SpringContext.getBean(visitorNames
				.get(modelName));

	}

}
