package me.twocoffee.service.impl;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.service.SanWangXinTong;
import me.twocoffee.service.SendSMSService;
import me.twocoffee.service.SmsBase;
import me.twocoffee.service.SmsTunnel;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class SendSMSServiceImpl implements SendSMSService {

	private VelocityEngine velocityEngine = null;

	private SmsBase smsBase = null;

	private SanWangXinTong sanWangXinTong = null;

	private SmsTunnel primarySMSTunnel = null;

	private SmsTunnel secondaeySMSTunnel = null;

	private String primarySMSTunnelflag = "dongshifang";

	public SendSMSServiceImpl() {
		super();
	}

	public SendSMSServiceImpl(VelocityEngine velocityEngine, SmsBase smsBase,
			SanWangXinTong sanWangXinTong, String primarySMSTunnelflag) {
		super();
		this.velocityEngine = velocityEngine;
		this.smsBase = smsBase;
		this.sanWangXinTong = sanWangXinTong;
		this.primarySMSTunnelflag = primarySMSTunnelflag;

		if ("dongshifang".equals(primarySMSTunnelflag)) {
			primarySMSTunnel = smsBase;
			secondaeySMSTunnel = sanWangXinTong;
		} else {
			primarySMSTunnel = sanWangXinTong;
			secondaeySMSTunnel = smsBase;

		}

	}

	public SmsBase getSmsBase() {
		return smsBase;
	}

	public void setSmsBase(SmsBase smsBase) {
		this.smsBase = smsBase;
	}

	public SanWangXinTong getSanWangXinTong() {
		return sanWangXinTong;
	}

	public void setSanWangXinTong(SanWangXinTong sanWangXinTong) {
		this.sanWangXinTong = sanWangXinTong;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	public void sendVerifyAccountSMS(String mobile, String token) {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("code", token);
		String des = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,
				"template/verify_account_mobile.vm",
				"utf-8", context);

		String result = primarySMSTunnel.sendSms(mobile, des);
		secondaeySMSTunnel.sendSms(mobile, des);
		if ("fali".equals(result)) {
			//	TODO 
			//	发送失败次数加1
			//	如果失败次数大于threshold 且是在5小时之内    需要记录上次归零的时间    主备通道切换 计数器归0
			//	在一定时间之内 失败次数大于10
			
			//  能不能做到热切换?

		}
	}

	@Override
	public void sendInvitation(String mobile, String code, String name) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("domainName", SystemConstant.domainName);
		context.put("code", code);
		context.put("name", name);
		String des = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,
				"template/invite_from_mobile_text.vm",
				"utf-8", context);
		// TODO 用两个发送通道发送 可以考虑用一个发送 这样发送太浪费
		donshifang(mobile, des);
		sanwangxintong(mobile, des);
	}

	public void donshifang(String mobile, String token) {
		String result = "";
		try {
			result = smsBase.sendSms(mobile, token);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("===================send token "
						+ token
						+ " to "
						+ mobile
						+ " result=======[http://www.dongshifang.com/jiekou.aspx#]=============="
						+ result);

	}

	public void sanwangxintong(String mobile, String token) {
		String result = "";
		String mtUrl = "http://sms.3wxt.cn/servlet/SendSMSmt";
//		String username = "ceshi01";
//		String password = "12346789";
		String username = "zs00129";
		String password = "a1234129";
		String indicate = "true";
		try {
			SanWangXinTong swxt = new SanWangXinTong(mtUrl, username, password,
						indicate);
			result = swxt.sendSms(mobile, token);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("===================send token "
						+ token
						+ " to "
						+ mobile
						+ " result================"
						+ result);

	}
}
