package me.twocoffee.service.fetch.impl;

import me.twocoffee.entity.Content;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.fetch.ProductWhiteListService;
import me.twocoffee.service.fetch.impl.product.fetcher.ProductFetcher;
import me.twocoffee.service.fetch.impl.product.parser.URLParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductWhiteListServiceImpl implements ProductWhiteListService {

	@Autowired
	private FetchService fetchService;

	public FetchService getFetchService() {
		return fetchService;
	}

	public void setFetchService(FetchService fetchService) {
		this.fetchService = fetchService;
	}

	@Autowired
	private URLParser urlParser;

	@Autowired
	private ProductFetcher productFetcher;

	public ProductFetcher getProductFetcher() {
		return productFetcher;
	}

	public void setProductFetcher(ProductFetcher productFetcher) {
		this.productFetcher = productFetcher;
	}

	public URLParser getUrlParser() {
		return urlParser;
	}

	public void setUrlParser(URLParser urlParser) {
		this.urlParser = urlParser;
	}

	@Override
	public String getModelName(String url) {
		return urlParser.parse(url);
	}

	@Override
	public Content getProductDetail(String url, String modelName) {
		String html = fetchService.getStringResource(url);
		Content c = productFetcher.fetcher(url, html, modelName);
		handleImage(c);
		return c;
	}

	private void handleImage(Content c) {
		String url = fetchService.getResource(c.getProductPayload()
				.getPicture());

		c.getProductPayload().setPicture(url);
	}

}
