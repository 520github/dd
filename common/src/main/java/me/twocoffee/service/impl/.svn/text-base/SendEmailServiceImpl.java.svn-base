package me.twocoffee.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Token;
import me.twocoffee.service.SendEmailService;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class SendEmailServiceImpl implements SendEmailService {
	private VelocityEngine velocityEngine = null;
	private JavaMailSender javaMailSender = null;
	private String supportEmail = "support@"+SystemConstant.mailSuffix;

	@Override
	public void sendInvitation(final String email, final String code,
			final String name) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				Map<String, Object> context = new HashMap<String, Object>();
				context.put("code", code);
				context.put("name", name);
				context = SendEmailServiceImpl.setDomainName(context);
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

				message.setTo(new InternetAddress(email));
				message.setFrom(new InternetAddress(supportEmail));
				message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/invite_from_email_subject.vm",
						"utf-8", context));

				message.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/invite_from_email_text.vm",
						"utf-8", context), true);

			}

		};

		javaMailSender.send(preparator);

	}

	@Override
	public void sendVerifyAccountEmail(final String email,
			final Token token,
			final String invite) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("token", token.getId());
				context.put("u", token.getAccountId());
				context.put("invite", invite);
				context.put("email", URLEncoder.encode(email, "UTF-8"));
				context = SendEmailServiceImpl.setDomainName(context);
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(new InternetAddress(email));
				message.setFrom(new InternetAddress(supportEmail));
				message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/verify_account_email_subject.vm",
						"utf-8", context));

				message.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/verify_account_email.vm",
						"utf-8", context), true);

			}
		};
		javaMailSender.send(preparator);
	}

	@Override
	public void sendWelcomeEmail(final Account account) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				Map<String, Object> context = new HashMap<String, Object>();
				context.put("nickname", account.getName());
				context = SendEmailServiceImpl.setDomainName(context);
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

				message.setTo(new InternetAddress(account.getEmail()));
				message.setFrom(new InternetAddress(supportEmail));
				message.setSubject(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/welcome_account_email_subject.vm",
						"utf-8", context));

				message.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"template/welcome_account_email.vm",
						"utf-8", context), true);

			}

		};

		javaMailSender.send(preparator);
	}
	
	private static Map<String,Object> setDomainName(Map<String,Object> context) {
		if(context == null) {
			return context;
		}
		context.put("domainName", SystemConstant.domainName);
		return context;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}