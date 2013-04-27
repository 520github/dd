package me.twocoffee.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送短信基础类
 * 
 * @author administration
 * 
 */
public class SmsBase implements SmsTunnel{
	private String xeid = "0";
	private String xac = "10";
	private String xuid = "duoduo";
	private String xpwdmd5 = "e10adc3949ba59abbe56e057f20f883e";	// 123456
	private String xgateid = "300";
	private String xurl = "http://gateway.woxp.cn:6630/utf8/web_api/";
	private String fixPartsendSMS;
	
	

	public String getXeid() {
		return xeid;
	}

	public void setXeid(String xeid) {
		this.xeid = xeid;
	}

	public String getXac() {
		return xac;
	}

	public void setXac(String xac) {
		this.xac = xac;
	}

	public String getXuid() {
		return xuid;
	}

	public void setXuid(String xuid) {
		this.xuid = xuid;
	}

	public String getXpwdmd5() {
		return xpwdmd5;
	}

	public void setXpwdmd5(String xpwdmd5) {
		this.xpwdmd5 = xpwdmd5;
	}

	public String getXgateid() {
		return xgateid;
	}

	public void setXgateid(String xgateid) {
		this.xgateid = xgateid;
	}

	public String getXurl() {
		return xurl;
	}

	public void setXurl(String xurl) {
		this.xurl = xurl;
	}

	public SmsBase() {
		super();
	}

	public SmsBase(String xeid, String xac, String xuid, String xpwdmd5,
			String xgateid, String xurl) {
		super();
		this.xeid = xeid;
		this.xac = xac;
		this.xuid = xuid;
		this.xpwdmd5 = xpwdmd5;
		this.xgateid = xgateid;
		this.xurl = xurl;
		fixPartsendSMS = xurl + "?x_eid=" + xeid + "&x_uid=" + xuid + "&x_pwd_md5="
			+ xpwdmd5 + "&x_ac=" + xac + "&x_gate_id=" + xgateid
			+ "&x_target_no=";
	}

	public static void main(String[] args) {
    	SmsBase test = new SmsBase();
    	try {
//    		String mobiles = "13911508796,13466549249,15910318250,13911185940";
    		String mobiles = "13466549249";
    		String content = "东时方 短信通道 发于";
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

	public String sendSms(String mobiles, String content){
//		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sendTime=dateformat1.format(new Date());
//		content = content + "  " +sendTime;
		Integer x_ac = 10;// 发送信息
		HttpURLConnection httpconn = null;
		String result = "-20";
		String memo = content.trim();
		StringBuilder sb = new StringBuilder(fixPartsendSMS);
//		sb.append("http://gateway.woxp.cn:6630/utf8/web_api/?x_eid=");
//		sb.append(x_eid);
//		sb.append("&x_uid=").append(x_uid);
//		sb.append("&x_pwd_md5=").append(x_pwd_md5);
//		sb.append("&x_ac=").append(x_ac);
//		sb.append("&x_gate_id=").append(x_gate_id);
		sb.append(mobiles);
		try {
			sb.append("&x_memo=").append(URLEncoder.encode(memo, "utf-8"));
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
			result = rd.readLine();
			rd.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}
			System.out
			.println("===================send token "
					+ content
					+ " to "
					+ mobiles
					+ " result======= dongshifang ========"
					+ result);
		}
		return result;
	}
}