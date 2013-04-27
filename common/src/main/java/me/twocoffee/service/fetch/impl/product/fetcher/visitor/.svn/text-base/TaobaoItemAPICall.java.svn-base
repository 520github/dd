package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.util.SignUtil;


@SuppressWarnings("restriction")
public class TaobaoItemAPICall {
	private static String URL = "http://gw.api.taobao.com/router/rest";
	private static String GET_ITEM_METHOD = "taobao.item.get";//商品信息
	private static String GET_PROMOTION_METHOD = "taobao.ump.promotion.get";//商品优惠信息
	private static String APP_KEY = "21421061";
	private static String APP_SECRET = "c28e2642a12310b742fe2ec2c813274f";
	private static String FORMAT = "json";
	public static String FIELD_PIC_URL = "pic_url";
	public static String FIELD_PRICE = "price";
	public static String FIELD_TITLE = "title";
	private static String FIELDS = FIELD_PIC_URL+","+FIELD_PRICE+","+FIELD_TITLE;//
	private static String V = "2.0";
	private static String SIGN_METHOD = "md5";//hmac md5
	private static String NUM_IID = "14078260609";
	
	
	public static Map<String,String> getItemMap(String num_iid) {
		Map<String,String> itemMap = null;
		NUM_IID = num_iid;
		JSONObject obj = AuthUtils.invoke("GET", URL, AuthUtils.GET_PARAM_TYPE_QUERYPARAM, setItemParaMap());
		obj = obj.getJSONObject("item_get_response");
		if(!obj.isNullObject()) obj = obj.getJSONObject("item");
		if(!obj.isNullObject()) {
			itemMap = new HashMap<String, String>();
			itemMap.put(FIELD_PIC_URL, obj.getString(FIELD_PIC_URL));
			itemMap.put(FIELD_PRICE, obj.getString(FIELD_PRICE));
			itemMap.put(FIELD_TITLE, obj.getString(FIELD_TITLE));
		}
		
		obj = AuthUtils.invoke("GET", URL, AuthUtils.GET_PARAM_TYPE_QUERYPARAM, setPromotionParaMap());
		obj = obj.getJSONObject("ump_promotion_get_response");
		if(!obj.isNullObject()) obj = obj.getJSONObject("promotions");
		if(!obj.isNullObject()) obj = obj.getJSONObject("promotion_in_item");
		if(!obj.isNullObject()) {
			if(!obj.containsKey("promotion_in_item"))return itemMap;
			JSONArray array = obj.getJSONArray("promotion_in_item");
			if(!array.isEmpty()) {
				obj = array.getJSONObject(0);
				if(!obj.isNullObject()) {
					String price = obj.getString("item_promo_price");
					if(StringUtils.isNotEmpty(price))itemMap.put(FIELD_PRICE, price);
				}
			}
		}
		
		return itemMap;
	}
	
	private static Map<String,String> setPromotionParaMap() {
		Map<String,String> map = getCommonMap(GET_PROMOTION_METHOD);
		map.put("item_id", NUM_IID);
		map = setSignMap(map);
		return map;
	}
	
	private static Map<String,String> setItemParaMap() {
		Map<String,String> map = getCommonMap(GET_ITEM_METHOD);
		map.put("fields", FIELDS);
		map.put("num_iid", NUM_IID);
		map = setSignMap(map);
		return map;
	}
	
	private static Map<String,String> getCommonMap(String method) {
		Map<String,String> map = new TreeMap<String,String>();
		map.put("method", method);
		map.put("app_key", APP_KEY);
		map.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//"2013-03-14 18:54:13"
		map.put("format", FORMAT);
		map.put("sign_method", SIGN_METHOD);
		map.put("v", V);
		return map;
	}
	
	private static Map<String,String> setSignMap(Map<String,String> map) {
		try {
			String sign = SignUtil.sign((TreeMap<String,String>)map, APP_SECRET);
			map.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	//http://blog.163.com/niwei_258/blog/static/106284882010111021953260/
	public static void main(String[] args) {
		getItemMap("num_iid");
	}
	
	
}
