package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service("taobaoAPIResultHandler")
public class TaobaoAPIResultHandler extends TaobaoResultHandler {
	private static String itemIdFlag[] = new String[]{"&mallstItemId","?mallstItemId","&id","?id"};
	@Override
	public Map<String, String> handle(Map<String, String> result) {
		String itemId = getItemIdFromUrl(result.get("url"));
		Map<String, String> itemMap = TaobaoItemAPICall.getItemMap(itemId);
		if(itemMap == null || itemMap.size()<1) {
			return super.handle(result);
		}
		if(result == null) {
			result = new  HashMap<String, String>();
		}
		
		String pic_url = itemMap.get(TaobaoItemAPICall.FIELD_PIC_URL);
		if(StringUtils.isNotBlank(pic_url)) {
			result.put("image", pic_url);
		}
		String price = itemMap.get(TaobaoItemAPICall.FIELD_PRICE);
		if(StringUtils.isNotBlank(price)) {
			result.put("price", price);
		}
		String title = itemMap.get(TaobaoItemAPICall.FIELD_TITLE);
		if(StringUtils.isNotBlank(title)) {
			result.put("title", title);
		}
		
		return result;
	}
	
	private String getItemIdFromUrl(String url) {
		if(StringUtils.isBlank(url)) {
			return "";
		}
		String id = "";
		for (int i = 0; i < itemIdFlag.length; i++) {
			String itemId = itemIdFlag[i];
			id = this.getItemId(url, itemId);
			if(StringUtils.isNotBlank(id)) {
				break;
			}
		}
		return id;
	}
	
	private String getItemId(String url,String itemIdFlag) {
		if(StringUtils.isBlank(url)) {
			return "";
		}
		String idFlag = itemIdFlag+"=";
		int index = url.indexOf(idFlag);
		if(index == -1) {
			return "";
		}
		String id = "";
		id = url.substring(index+idFlag.length(), url.length());
		if(id.indexOf("&")>-1)id = id.substring(0, id.indexOf("&"));
		return id;
	}

}
