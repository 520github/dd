package me.twocoffee.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.service.DeviceService;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected ObjectMapper objectMapper = new ObjectMapper();

	protected final String COOKIE_USER = "user";
	
	@Autowired
	protected TokenUtil tokenUtil;
	
	@Autowired
	protected DeviceService deviceService;

	protected String addQueryParams(String base, String... params) {

		if (params == null || params.length == 0) {
			return base;
		}

		if (params.length % 2 != 0) {
			throw new InvalidParameterException("query parameter error!");
		}
		StringBuilder builder = new StringBuilder(base);
		builder.append("?");

		for (int i = 0; i < params.length; i += 2) {
			builder.append(params[i]).append("=")
					.append(urlEncode(params[i + 1], "UTF-8"))
					.append("&");

		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	protected ResponseEntity<String> buildJSONResult(Map<String, String> map) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		return new ResponseEntity<String>(json(map), responseHeader,
				HttpStatus.OK);

	}

	protected ResponseEntity<String> buildJSONResult(Object object) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		return new ResponseEntity<String>(json(object), responseHeader,
				HttpStatus.OK);

	}

	protected ResponseEntity<String> buildJSONResult(String... params) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		if (params == null || params.length == 0) {
			return new ResponseEntity<String>("", responseHeader,
					HttpStatus.OK);

		}

		if (params.length % 2 != 0) {
			throw new InvalidParameterException("can not get result");
		}
		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < params.length; i += 2) {
			map.put(params[i], params[i + 1]);
		}
		return new ResponseEntity<String>(json(map), responseHeader,
				HttpStatus.OK);

	}

	protected ResponseEntity<String> buildJSONResult1(Map<String, Object> map) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		return new ResponseEntity<String>(json1(map), responseHeader,
				HttpStatus.OK);

	}

	protected String getEncodingString(String str, String encoding) {
		try {
			return new String(str.getBytes("ISO-8859-1"), encoding);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	protected String getMessage(String key) {
		return SpringContext.getMessageByRequestLocale(key, null);
	}

	protected String getMessage(String key, Object[] args) {
		return SpringContext.getMessageByRequestLocale(key, args);
	}

	protected String json(Object t) {
		if (t == null)
			throw new IllegalArgumentException();

		return JSONSerializer.toJSON(t).toString();
	}

	protected String json1(Object t) {
		if (t == null)
			throw new IllegalArgumentException();

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));

		return JSONSerializer.toJSON(t, jsonConfig).toString();
	}

	protected void setLoginUser(HttpServletResponse response, String userId)
			throws Exception {
		// TODO: set domain, set key
		CookieTool.setCookie(response, COOKIE_USER, userId, -1, null);
	}

	protected String urlEncode(String text, String charSet) {

		try {
			return URLEncoder.encode(text, charSet);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String isHaveClient(String accountId) {
		String isHaveClient = "no";
		List list = deviceService.getByAccountId(accountId);
		if(list != null && list.size()>0) {
			isHaveClient = "yes";
		}
		return isHaveClient;
	}
	
	 protected boolean isMobile(String agent) {
		if (StringUtils.isNotBlank(agent)
			&& (agent.toLowerCase().contains("iphone")
				|| agent
					.toLowerCase().contains("android") || agent
				.toLowerCase().contains("ipad"))) {
		
		    return true;
		}
		return false;
    }
	 
	 protected String getUserAgentType(String agent) {
		 String userAgentType = "";
		 if (StringUtils.isNotBlank(agent)) {
		    if (agent.toLowerCase().contains("iphone"))
		    	userAgentType = "iphone";
		    if (agent.toLowerCase().contains("ipad"))
		    	userAgentType = "ipad";
		    if (agent.toLowerCase().contains("android"))
		    	userAgentType = "android";

		}
		 return userAgentType;
	 }
}
