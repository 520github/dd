/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class StringTokenURLParser implements URLParser {

	@Autowired
	private ProductWhitelistHolder productWhitelistHoder;

	public ProductWhitelistHolder getProductWhitelistHoder() {
		return productWhitelistHoder;
	}

	public void setProductWhitelistHoder(
			ProductWhitelistHolder productWhitelistHoder) {

		this.productWhitelistHoder = productWhitelistHoder;
	}

	@Override
	public String parse(String url) {
		String fetcherStr = getFetcherStr(url);

		if (StringUtils.isBlank(fetcherStr)) {
			return null;
		}
		return fetcherStr;
	}

	private String getFetcherStr(String url) {
		List<ProductUrlInfo> whitelist = productWhitelistHoder.getWithlist();

		for (ProductUrlInfo white : whitelist) {

			if (checkURL(url, white.getUrlStr())) {
				return white.getModelName();
			}
		}
		return null;
	}

	private boolean checkURL(String url, String white) {

		if (url.trim().startsWith(white)) {
			return true;
		}
		return false;
	}

}
