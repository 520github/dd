/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher;

/**
 * @author momo
 * 
 */
public interface IVisitorFactory {

	IProductVisitor getByBeanName(String modelName);

}
