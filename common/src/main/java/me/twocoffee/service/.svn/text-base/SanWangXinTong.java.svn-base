package me.twocoffee.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SanWangXinTong  implements SmsTunnel{
	
	public  String mtUrl;
	public  String username;
	public  String password;
	public  String indicate;
	
	
	public SanWangXinTong(String mtUrl, String username, String password,
			String indicate) {
		super();
		this.mtUrl = mtUrl;
		this.username = username;
		this.password = password;
		this.indicate = indicate;
	}
	
	public static void main(String[] args) {
		SanWangXinTong test = new SanWangXinTong("http://sms.3wxt.cn/servlet/SendSMSmt","zs00129","a1234129","true");
    	try {
//    		String mobiles = "13911508796,13466549249,15910318250,13911185940";
    		String mobiles = "13520477332";
    		String content = "13520477332三网信通短信通道 发于";
    		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		String sendTime=dateformat1.format(new Date());
    		content = content + sendTime;
    		System.out.println("时间:"+content);
			String yyy = test.sendSms(mobiles, content);
			System.out.println("yyy="+yyy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    }

	public String sendSms(String mobiles,String content) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sendTime = dateformat1.format(new Date());
//		content = content + " -" +sendTime;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(mtUrl);
		postMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTf-8");
		NameValuePair[] data = { new NameValuePair("method", "batch"),
				new NameValuePair("username", username),
				new NameValuePair("mobiles", mobiles),
				new NameValuePair("password", password),
				new NameValuePair("contents", content),
				new NameValuePair("indicate", indicate) };

		postMethod.setRequestBody(data);
		String result = "";
		try {
			client.executeMethod(postMethod);
			int statusCode = postMethod.getStatusCode();
			System.out.println("SanWangXinTong statusCode="+statusCode);
			result = new String(postMethod.getResponseBodyAsString()
					.getBytes(
							"utf-8"));
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("SanWangXinTong result="+result);
		}
		return result;

	}

}
