/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.Map;

import me.twocoffee.common.util.HttpClientUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Scope("prototype")
@Service("tmallResultHandler")
public class TmallResultHandler implements ResultHandler {

    private String getBigImage(String img) {
	int index = img.indexOf(".jpg_");

	if (index < 0)
	    return img;

	return img.substring(0, index + 4);
    }

    protected String getRealPrice(String api, String url) {

	if (StringUtils.isBlank(api)) {
	    return "";
	}
	HttpGet get = new HttpGet(api);
	get.setHeader("Referer", url);
	get.setHeader("Host", "tbskip.taobao.com");// tbskip.taobao.com
	String prominfo = HttpClientUtils.getStringResource(url, get);
	// String key = getKey(api);
	JSONObject j = JSONObject.fromObject(prominfo.substring(prominfo
		.indexOf("{")));

	String realPrice = "";

	if (j.containsKey("def") && j.getJSONArray("def").size() > 0) {
	    JSONObject o = j.getJSONArray("def").getJSONObject(0);

	    if (o.containsKey("price")) {
		realPrice = o.getString("price");
	    }

	    if (o.containsKey("priceRange")) {
		JSONObject obj = o.getJSONObject("priceRange");
		StringBuffer sb = new StringBuffer(obj.getString("low"));
		sb.append("-").append(obj.getString("high"));
		realPrice = sb.toString();
	    }
	}
	return realPrice;
    }

    // private String getKey(String api) {
    // int s = api.indexOf("skuPvPairs=") + 11;
    // int e = api.indexOf("$", s);
    // return api.substring(s, e);
    // }

    @Override
    public Map<String, String> handle(Map<String, String> result) {
	String image = result.get("image");
	String bigImage = getBigImage(image);
	result.put("image", bigImage);
	return result;
    }
}
