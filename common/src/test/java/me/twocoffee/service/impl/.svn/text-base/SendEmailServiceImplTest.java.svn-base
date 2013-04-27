package me.twocoffee.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.twocoffee.entity.Token;

import org.apache.commons.codec.binary.Base64;
import org.apache.velocity.exception.VelocityException;
import org.junit.Ignore;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

/**
 * @author lisong
 * 
 */
@Ignore
public class SendEmailServiceImplTest {

	public static void main(String[] args) throws VelocityException,
			IOException {

		SendEmailServiceImpl service = new SendEmailServiceImpl();

		VelocityEngineFactoryBean velocityEngineFactory = new VelocityEngineFactoryBean();
		velocityEngineFactory.setResourceLoaderPath("/");

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("input.encoding", "UTF-8");
		properties.put("output.encoding", "UTF-8");
		velocityEngineFactory.setVelocityPropertiesMap(properties);
		// init velocityEngine
		service.setVelocityEngine(velocityEngineFactory.createVelocityEngine());

		// init JavaMailSender
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost("smtp.163.com");
		javaMailSender.setPort(25);
		javaMailSender.setUsername("babeetatest");
		javaMailSender.setPassword("bbtbbt");
		javaMailSender.setDefaultEncoding("utf-8");

		service.setJavaMailSender(javaMailSender);
		service
				.setSupportEmail("\"=?UTF-8?B?"
						+ new String(Base64
								.encodeBase64("多多".getBytes("utf-8")))
						+ "?=\" <babeetatest@163.com>");

		Token token = new Token();
		token.setId("testTokennnn");
		service.sendVerifyAccountEmail("song.li@babeeta.com", token,
				"");

		System.out.println("verify email sent");
	}
}
