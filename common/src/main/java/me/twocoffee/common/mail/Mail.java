/**
 * 
 */
package me.twocoffee.common.mail;

import me.twocoffee.common.constant.SystemConstant;

/**
 * @author xuehui.miao
 *
 */
public class Mail {
	private String username = "xuehui.miao";
	private String password = "111111";
	private String mailServer = "localhost";
	private String from = "xuehui.miao@babeeta.com";
	private String to = "xuehui.miao@" + SystemConstant.mailSuffix;
	private String subject = "hello,I see you";
	private String content = "ok,you are right";
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMailServer() {
		return mailServer;
	}
	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
