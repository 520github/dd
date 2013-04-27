/**
 * 
 */
package me.twocoffee.common.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author xuehui.miao
 *
 */
public class SendMail {
	private Session mailSession;
	private Properties prop;
	private Message message;
	
	private Mail mail = null;
	
	public SendMail(Mail mail) {
		this.mail = mail;
		if(this.mail == null) {
			this.mail = new Mail();
		}
	}
	
	
	public String sendMail() {
		String mes = "";
		try {
			EmailAuthenticator mailauth = new EmailAuthenticator(mail.getUsername(), mail.getPassword());
			// 设置邮件服务器
			prop = System.getProperties();
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.host", mail.getMailServer());
			// 产生新的Session服务
			mailSession = mailSession.getDefaultInstance(prop, mailauth);//(Authenticator) mailauth
			message = new MimeMessage(mailSession);
			
			message.setFrom(new InternetAddress(mail.getFrom())); // 设置发件人
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(
					mail.getTo()));// 设置收件人
			message.setSubject(mail.getSubject());// 设置主题
			message.setContent(mail.getContent(), "text/plain");// 设置内容
			message.setSentDate(new Date());// 设置日期
			Transport tran = mailSession.getTransport("smtp");
			tran.connect(mail.getMailServer(), mail.getUsername(), mail.getPassword());
			tran.send(message, message.getAllRecipients());
			tran.close();
			mes = "邮件发送成功";
		} catch (Exception e) {
			e.printStackTrace();
			mes = "邮件发送失败："+e.getMessage();
		}
		return mes;
	}
	
	public static void main(String[] args) {
		try {
			SendMail mail = new SendMail(null);
			mail.sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
