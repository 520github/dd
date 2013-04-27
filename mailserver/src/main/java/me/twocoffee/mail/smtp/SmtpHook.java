package me.twocoffee.mail.smtp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.directory.DirContext;
import javax.ws.rs.core.Response;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.mail.service.AccountService;
import me.twocoffee.mail.service.AccountWithToken;
import me.twocoffee.mail.service.ContentServiceHttpImpl;
import me.twocoffee.mail.service.ContentServiceHttpImpl.PostContent.ContentComment;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.MessageImpl;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.protocols.api.ProtocolServer;
import org.apache.james.protocols.api.logger.Logger;
import org.apache.james.protocols.smtp.MailAddress;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.AuthHook;
import org.apache.james.protocols.smtp.hook.HeloHook;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.HookReturnCode;
import org.apache.james.protocols.smtp.hook.MailHook;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.apache.james.protocols.smtp.hook.RcptHook;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

public class SmtpHook implements HeloHook, MailHook, RcptHook, MessageHook, AuthHook {
	private AccountService accountService = null;
	private FileOperator fileOperator = null;
	private ContentServiceHttpImpl contentService;
	
	public SmtpHook(AccountService ass, FileOperator fo, ContentServiceHttpImpl cs) {
		accountService = ass;
		fileOperator = fo;
		contentService = cs;
	}
	
	/**
     * Return {@link HookResult} with {@link HookReturnCode#OK}
     */
    public HookResult onMessage(SMTPSession session, MailEnvelope mail) {
    	if (mail.getRecipients() == null || mail.getRecipients().size() < 1)
    		return new HookResult(HookReturnCode.DENYSOFT);
    	
    	try {
	    	if (session.getUser() == null) {
	    		return retrieveFromServer(session, mail);
	    	}
	    	else {
	    		return retrieveFromUser(session, mail);
	    	}
    	}
    	catch (Exception e) {
    		session.getLogger().error("OnMessage error.", e);
    		return new HookResult(HookReturnCode.DENYSOFT);
    	}
    }
	
	private HookResult retrieveFromUser(SMTPSession session, MailEnvelope mail) {
		Account a = accountService.getByDuoduoEmail(mail.getSender().toString());
		if (a == null) {
			session.getLogger().error("No user." + mail.getSender().toString());
			return new HookResult(HookReturnCode.DENYSOFT);
		}
		if (!a.getLoginName().equals(session.getUser())) {
			session.getLogger().error("Fabricate maill." + mail.getSender().toString());
			return new HookResult(HookReturnCode.DENYSOFT);
		}
		
		transpondMail(mail, session.getLogger());
    	
    	session.getLogger().warn("Incorrect mail." + getMailSummary(mail));
        return new HookResult(HookReturnCode.DENYSOFT);
	}

	private void transpondMail(MailEnvelope mail, Logger log) {
		String host = "10.0.0.86";//getHostByMail(mail.getRecipients().get(0).toString());
		sendToMail(host, mail, log);
	}

	private void sendToMail(String host, MailEnvelope mail, Logger log) {
		Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", "false");
        prop.put("mail.smtp.host", host);
		Session mailSession = Session.getDefaultInstance(prop);
        javax.mail.Message message = new MimeMessage(mailSession);
  
        try {
        	MailContent content = getMailContent(mail.getMessageInputStream(), log);
        	if (content == null)
        		return;
        	
        	message.setFrom(new InternetAddress(mail.getSender().toString())); // 设置发件人   
        	message.setRecipient(javax.mail.Message.RecipientType.TO, 
        			new InternetAddress(mail.getRecipients().get(0).toString()));// 设置收件人

	        message.setSubject(content.getSubject());// 设置主题   
	        message.setContent(content.getBody(), "text/plain");// 设置内容   
	        message.setSentDate(new Date());// 设置日期  
	        Transport tran = mailSession.getTransport("smtp");
	        tran.send(message, message.getAllRecipients());
	        tran.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private String getHostByMail(String mail) {
		int index = mail.indexOf("@");
		if (index < 0)
			return null;
		String domain = mail.substring(index + 1);
		return "smtp." + domain;
	}

	private HookResult retrieveFromServer(SMTPSession session, MailEnvelope mail) {
		MailContent content = null;
		try {
			content = getMailContent(mail.getMessageInputStream(), session.getLogger());
		}
		catch (IOException e) {
			content = null;
			session.getLogger().error("onMessage", e);
		}
		if (content == null || content.getBody() == null || content.getBody().equals("")) {
			session.getLogger().error("Parse mail error.from " + mail.getSender().toString());
			return new HookResult(HookReturnCode.DENYSOFT);
		}
		
		for (MailAddress m : mail.getRecipients()) {
			String ma = m.toString();
			if (!isCorrectAccount(ma)) {
	    		session.getLogger().warn("Correnct mail address." + ma);
	    		continue;
	    	}
			
    		Account a = accountService.getByDuoduoEmail(ma);
    		if (a == null) {
    			session.getLogger().error("No user." + ma);
    			continue;
    		}
	    	if (isSelfMail(mail.getSender().toString(), a)) {
	    		saveMail(mail.getSender().toString(), a, content, session.getLogger());
	    		continue;
	    	}
	    	
	    	Account sendAccount = getAccountByFriendMail(mail.getSender().toString(), ma);
	    	if (sendAccount != null) {
	    		shareMail(mail.getSender().toString(), sendAccount, a, content, session.getLogger());
	    	}
		}
		
    	session.getLogger().info("Incorrect mail." + getMailSummary(mail));
    	return new HookResult(HookReturnCode.OK);
	}

	private String getMailSummary(MailEnvelope mail) {
		StringBuffer sb = new StringBuffer();
		sb.append("From:");
		sb.append(mail.getSender().toString());
		sb.append("@To:");
		sb.append(mail.getRecipients().get(0).toString());
		return sb.toString();
	}

	private void shareMail(String sender, Account sendAccount, Account a, MailContent content, Logger log) {
		if (sendAccount == null) {
			log.error("No sender's duoduo mail.from " + sender);
			return;
		}
		
		AccountWithToken token = accountService.auth(sendAccount.getLoginName());
		if (token == null || token.getAuthToken() == null) {
			log.error("Can't get token.from " + sendAccount.getLoginName());
			return;
		}
		
		ContentServiceHttpImpl.PostContent p = new ContentServiceHttpImpl.PostContent();
		p.setContentType(ContentType.HtmlClip);
		p.setTitle(getTitle(content.getSubject()));
		p.setUrl(sender);
		HtmlPayload hp = new HtmlPayload();
		hp.setContent(content.getBody());
		if (content.getImages() != null && content.getImages().size() > 0) {
			hp.setAttachment(content.getImages(), null);
		}
		hp = setHtmlAttachment(hp);
		List<String> tags = new ArrayList();
		tags.add(SystemTagEnum.Source_Mail.toString());
		p.setTag(tags);
		p.setHtmlPayload(hp);
		List<String> list = new ArrayList();
		list.add(a.getId());
		p.setShare(list);
		ContentComment cc = new ContentComment();
		cc.setScore(0);
		cc.setText("");
		p.setComment(cc);
		contentService.postContent(token.getAuthToken(), p);
	}

	private final Pattern imgPattern = Pattern.compile("<[Ii][Mm][Gg][^\"]*\"([^\"]*)\"[^>]*>", Pattern.CASE_INSENSITIVE);
	
	private HtmlPayload setHtmlAttachment(HtmlPayload hp) {
		List<Attachment> list = hp.getAttachment();
		if (list == null) {
			list = new ArrayList();
		}
		String c = hp.getContent();
		Matcher m = imgPattern.matcher(c);
		while (m.find()) {
			String url = m.group(1);
			if (url == null || !url.startsWith("http://"))
				continue;
			
			Attachment a = new Attachment();
			a.setArchiveUrl(m.group(1));
			a.setOrgUrl(m.group(1));
			
			list.add(a);
		}
		hp.setAttachment(list);
		return hp;
	}

	private void saveMail(String sender, Account a, MailContent content, Logger log) {
		AccountWithToken token = accountService.auth(a.getLoginName());
		if (token == null || token.getAuthToken() == null) {
			log.error("No sender's duoduo mail.from " + sender);
			return;
		}
				
		ContentServiceHttpImpl.PostContent p = new ContentServiceHttpImpl.PostContent();
		p.setContentType(ContentType.HtmlClip);
		p.setTitle(getTitle(content.getSubject()));
		p.setUrl(sender);
		p.setTag(getUserTag(a));
		if (p.getTag() == null) {
			log.warn("No shared tags.from " + a.getLoginName());
			return;
		}
		HtmlPayload hp = new HtmlPayload();
		hp.setContent(content.getBody());
		if (content.getImages() != null && content.getImages().size() > 0) {
			hp.setAttachment(content.getImages(), null);
		}
		hp = setHtmlAttachment(hp);
		p.setHtmlPayload(hp);
		ContentComment cc = new ContentComment();
		cc.setScore(0);
		cc.setText("");
		p.setComment(cc);
		contentService.postContent(token.getAuthToken(), p);
	}

	private String getTitle(String subject) {
		String title = subject;
		if (title == null)
			return "来自邮件";
		title = title.trim();
		if (title.equals("")) {
			title = "来自邮件";
		}

		return title;
	}

	private List<String> getUserTag(Account a) {
		AccountMailConfig config = accountService.getMailConfig(a.getId());
		if (config == null)
			return null;
		List<String> tags = config.getInTags();
		if (tags == null) {
			tags = new ArrayList();
		}
		tags.add(SystemTagEnum.Unread.toString());
		tags.add(SystemTagEnum.Source_Mail.toString());
		return tags;
	}

	private MailContent getMailContent(InputStream s, Logger log) {
		MailContentParser p = null;
		
		try {
			p = new MailContentParser(s, fileOperator);
		} catch (Exception e) {
			log.error("Parse mail content error.", e);
			return null;
		}
		MailContent mc = new MailContent();
		mc.setSubject(p.getSubject());
		mc.setBody(p.getBody());
		mc.setImages(p.getImages());
		return mc;
	}

	private Account getAccountByFriendMail(String sender, String receiver) {
		List<FriendMailConfig> fs = accountService.findFriendMailByDuoduoMail(receiver);
		return accountService.getAccountByFriendMail(fs, sender);
	}

	private boolean isSelfMail(String sender, Account a) {
		if (a == null)
			return false;
		AccountMailConfig config = accountService.getMailConfig(a.getId());
		if (config == null || config.getMails() == null || config.getMails().size() < 1)
			return false;
		for (String m : config.getMails()) {
			if (m.equals(sender))
				return true;
		}
		return false;
	}

	private boolean isCorrectAccount(String receiver) {
		int index = receiver.indexOf("@");
		if (index < 0)
			return false;
		
		if (!receiver.substring(index + 1).equals(SystemConstant.mailSuffix))
			return false;
		
		Account a = accountService.getByDuoduoEmail(receiver);
		if (a == null)
			return false;
		return true;
	}

	/**
     * Return {@link HookResult} with {@link HookReturnCode#DECLINED}
     */
    public HookResult doRcpt(SMTPSession session, MailAddress sender, MailAddress rcpt) {
    	session.getLogger().info("doRcpt:" + sender.toString() + "," + rcpt.toString());

        return new HookResult(HookReturnCode.DECLINED);
    }

    /**
     * Return {@link HookResult} with {@link HookReturnCode#DECLINED}
     */
    public HookResult doMail(SMTPSession session, MailAddress sender) {
    	session.getLogger().info("doMail:" + sender.toString());
        return new HookResult(HookReturnCode.DECLINED);

    }

    /**
     * Return {@link HookResult} with {@link HookReturnCode#DECLINED}
     */
    public HookResult doHelo(SMTPSession session, String helo) {
    	session.getLogger().info("doHelo:" + helo);
        return new HookResult(HookReturnCode.DECLINED);
    }
    
    public HookResult doAuth(SMTPSession session, String username, String password) {
    	if (username == null || username.equals("") || password == null || password.equals(""))
    		return new HookResult(HookReturnCode.DENY);
    	
    	Account u = accountService.getByDuoduoEmail(username + "@" +SystemConstant.mailSuffix);

		if (u == null) {
			return new HookResult(HookReturnCode.DENY);
		}

		if (!u.getPasswordHash().equals(
				DigestUtils.md5DigestAsHex(password.getBytes()))) {

			return new HookResult(HookReturnCode.DENY);
		}
		session.setUser(u.getLoginName());
		return new HookResult(HookReturnCode.OK);
	}
}
