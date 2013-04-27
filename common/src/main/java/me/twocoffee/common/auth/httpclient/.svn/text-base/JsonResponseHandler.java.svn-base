package me.twocoffee.common.auth.httpclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * response handler.parse response data to JSONObject.
 * 
 * @author wenjian
 * 
 */
public class JsonResponseHandler implements ResponseHandler<JSONObject> {

    private static final Logger logger = LoggerFactory
	    .getLogger(JsonResponseHandler.class);

    @Override
    public JSONObject handleResponse(HttpResponse response)
	    throws ClientProtocolException, IOException {

	HttpEntity entity = response.getEntity();

	if (entity != null) {
	    String result = EntityUtils.toString(entity);
	    result = this.changeString2json(result);
	    logger.debug("recieve {}", result);

	    // if (StringUtils.isNotBlank(result)) {
	    // StringBuilder builder = new StringBuilder(result);
	    //
	    // if (!"{".equals(builder.charAt(0))) {
	    // builder.insert(0, "{");
	    // builder.append("}");
	    // result = builder.toString();
	    // }
	    // }
	    JSON json = JSONSerializer.toJSON(result);

	    if (json.isArray()) {
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("result", json);
		return JSONObject.fromObject(results);
	    }
	    return (JSONObject) json;
	}
	throw new RuntimeException("No entity!");
    }

    private String changeString2json(String result) {
	if (result == null)
	    return result;
	result = result.trim();
	if (result.indexOf("{") == 0 || result.indexOf("[") == 0)
	    return result;
	if (result.startsWith("callback(") && result.indexOf("{") > 0) {
	    result = result.substring(result.indexOf("{"), result.length() - 2);
	    return result;
	}
	String json = "{";
	String results[] = result.split("&");
	for (int i = 0; i < results.length; i++) {
	    String temp = results[i];
	    int index = temp.indexOf("=");
	    if (temp.indexOf("=") > -1) {
		json = json + "\"" + temp.substring(0, index) + "\":\""
			+ temp.substring(index + 1) + "\",";
	    }
	}
	json = json + "}";
	return json;
    }

}
