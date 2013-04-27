/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class PropertiesFileHodler implements ProductWhitelistHolder {

	private static final String PRODUCT_WHITELIST_WHITELIST_PROPERTIES = "/product/whitelist/whitelist.properties";

	private String path = PRODUCT_WHITELIST_WHITELIST_PROPERTIES;

	private Properties properties;

	private List<ProductUrlInfo> productUrlInfos = new ArrayList<ProductUrlInfo>();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@PostConstruct
	public void init() {
		properties = new Properties();
		InputStream in = null;

		try {
			in = PropertiesFileHodler.class.getResourceAsStream(path);
			properties.load(in);
			initWhitelist();

		} catch (IOException e) {

		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private void initWhitelist() {

		for (String key : properties.stringPropertyNames()) {
			ProductUrlInfo info = new ProductUrlInfo();
			info.setModelName(key);
			info.setUrlStr(properties.getProperty(key));
			productUrlInfos.add(info);
		}
	}

	@Override
	public List<ProductUrlInfo> getWithlist() {
		return productUrlInfos;
	}

}
