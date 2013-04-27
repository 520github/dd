/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher;

import java.util.Map;

import me.twocoffee.entity.Content;
import me.twocoffee.entity.ProductPayload;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class DefaultProductFetcher implements ProductFetcher {

	@Autowired
	private IVisitorFactory visitorFactory;

	public IVisitorFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(IVisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	@Autowired
	private ModelExecutor modelExecutor;

	public ModelExecutor getModelExecutor() {
		return modelExecutor;
	}

	public void setModelExecutor(ModelExecutor modelExecutor) {
		this.modelExecutor = modelExecutor;
	}

	@Override
	public Content fetcher(String url, String resource, String modelName) {
		IProductVisitor visitor = visitorFactory.getByBeanName(modelName);

		if (visitor == null) {
			throw new RuntimeException("can not get product!");
		}
		Map<String, String> result = modelExecutor.execute(url, visitor,
				resource);

		Content content = new Content();
		content.setContentType(Content.ContentType.Product);
		content.setUrl(url);
		content.setTitle(result.get("title"));
		ProductPayload productPayload = new ProductPayload();

		if (StringUtils.isNotBlank(result.get("name"))) {
			productPayload.setName(result.get("name"));

		} else {
			productPayload.setName(result.get("title"));
		}
		productPayload.setPicture(result.get("image"));
		productPayload.setPrice(result.get("price"));
		content.setProductPayload(productPayload);
		return content;
	}

}
