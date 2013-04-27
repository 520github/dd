package me.twocoffee.mail.smtp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.twocoffee.common.dfs.FileOperator;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.MessageImpl;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailContentParser {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(MailContentParser.class);
	
	private String subject = null;
	private String textBody = null;
	private String htmlBody = null;
	
	private boolean hasImage = false;
	private Map images = null;
	
	private FileOperator fileOperator = null;
	
	public MailContentParser(InputStream s, FileOperator fo) throws MimeIOException, IOException {
		fileOperator = fo;
		images = new HashMap();
		parser(s);
	}
	
	private void parser(InputStream s) throws MimeIOException, IOException {
		MimeConfig config = new MimeConfig();
        config.setMaxLineLen(-1);
        DefaultMessageBuilder builder = new DefaultMessageBuilder();
        builder.setMimeEntityConfig(config);
		
		Message m = builder.parseMessage(s);
		
		subject = m.getSubject();
		textBody = parseTextBody(m);
		htmlBody = parseHtmlBody(m);
		htmlBody = replaceImage(htmlBody);
	}
	
	private String replaceImage(String b) {
		if (!hasImage || images.size() < 1)
			return b;
		
		//LOGGER.info("Content:" + b);
		Iterator iter = images.entrySet().iterator();
		List<String> imageNames = new ArrayList();
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String name = entry.getKey().toString();
		    String url = entry.getValue().toString();
		    
		    LOGGER.info("Image:" + name + "," + url);
		    
		    String pname = name.replace("(", "\\(").replace(")", "\\)")
		    		.replace("*", "\\*").replace("^", "\\^").replace("$", "\\$");
		    Pattern p = Pattern.compile("<[Ii][Mm][Gg][^\"]*\"[^\"]*" + pname + "[^>]*>", Pattern.CASE_INSENSITIVE);
		    Matcher m = p.matcher(b);
		    if (m.find()) {
		    	//LOGGER.info("Image:" + name + "," + url);
		    	b = b.replaceAll("[^\"]*" + pname + "[^\"]*", url);
		    }
		    else {
		    	imageNames.add(name);
		    }
		} 
		
		for (String name : imageNames) {
			images.remove(name);
		}
		
		return b;
	}

	private String parseHtmlBody(Entity e) throws IOException {
		StringBuilder sb = new StringBuilder();

		if (e.getBody() instanceof Multipart) {
            Multipart multipart =(Multipart) e.getBody();
            List<Entity> parts = multipart.getBodyParts();

            for (Entity bodyPart : parts) {
                sb.append(parseHtmlBody(bodyPart));
            }
        } else if (e.getBody() instanceof TextBody && isHtmlBody(e)) {
        	TextBody tb = (TextBody)e.getBody();
        	sb.append(removeHtml(IOUtils.toString(tb.getInputStream(), tb.getMimeCharset())));
        } else if (e.getBody() instanceof BinaryBody) {// && isImageBody(e)){
        	BinaryBody bb = (BinaryBody)e.getBody();
        	String name = getImageName(e.getHeader().getField("Content-Type"));
        	if (name != null) {
        		String url = saveImage(name, bb);
        		if (url != null) {
        			hasImage = true;
        			String imageId = getImageId(e.getHeader().getField("Content-ID"));
        			if (imageId != null && !imageId.equals("")) {
        				images.put(imageId, url);
        			}
        			else {
        				images.put(name, url);
        			}
        		}
        	}
        }

        return sb.toString();
	}
	
	private String removeHtml(String b) {
		b = b.replace("\r", "").replace("\n", "");
		b = b.replaceAll("<!DOCTYPE[^>]*>", "");
		b = b.replaceAll("<[Hh][Ee][Aa][Dd][\\s]*>.*</[Hh][Ee][Aa][Dd]>", "");
		b = b.replaceAll("<[/]*[Hh][Tt][Mm][Ll]>", "");
		b = b.replaceAll("<[/]*[Bb][Oo][Dd][Yy][^>]*>", "");
		return b;
	}

	private static Pattern imageIdPattern = Pattern.compile("<([^>]+)", Pattern.CASE_INSENSITIVE);
	
	private String getImageId(Field field) {
		if (field == null)
			return null;
		String img = field.getBody();
		if (img == null || img.equals(""))
			return null;
		Matcher m = imageIdPattern.matcher(field.getBody());
		if (m.find())
			return m.group(1);
		return null;
	}

	private String saveImage(String name, BinaryBody b) {
		try {
			String id = fileOperator.putFile(b.getInputStream(), b.getInputStream().available(), name);
			if (id == null || id.equals(""))
				return null;
			return fileOperator.getFileUrl(id);
		} catch (IOException e) {
			LOGGER.error("save image to fs error.", e);
			return null;
		}
	}

	private boolean isImageBody(Entity e) {
		return e.getHeader().getField("Content-Type").getBody().indexOf("image/") > -1;
	}

	private boolean isHtmlBody(Entity e) {
		return e.getHeader().getField("Content-Type").getBody().indexOf("text/html") > -1;
	}

	private String parseTextBody(Entity e) throws IOException {
		StringBuilder sb = new StringBuilder();

		if (e.getBody() instanceof Multipart) {
            Multipart multipart =(Multipart) e.getBody();
            List<Entity> parts = multipart.getBodyParts();

            for (Entity bodyPart : parts) {
                sb.append(parseTextBody(bodyPart));
            }
        } else if (e.getBody() instanceof TextBody && isTextBody(e)) {
        	TextBody tb = (TextBody)e.getBody();
        	sb.append(IOUtils.toString(tb.getInputStream(), tb.getMimeCharset()));
        }
		
        return sb.toString();
	}
	
	private boolean isTextBody(Entity e) {
		return e.getHeader().getField("Content-Type").getBody().indexOf("text/plain") > -1;
	}

	private static Pattern imagePattern = Pattern.compile("name=\"([^\"]+).*", Pattern.CASE_INSENSITIVE);
	
	private String getImageName(Field field) {
		Matcher m = imagePattern.matcher(field.getBody());
		if (m.find())
			return m.group(1);
		return null;
	}

	private static String escape(String s) {
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        return s.replaceAll(">", "&gt;");
    }

	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		if (htmlBody != null && !htmlBody.equals(""))
			return htmlBody;
		return replaceToHtml(textBody);
	}

	private String replaceToHtml(String b) {
		b = b.replaceAll("&", "&amp;");
        b = b.replaceAll("<", "&lt;");
        b = b.replaceAll(">", "&gt;");
        b = b.replace(" ", "&nbsp;").replace("\n", "<br />").replace("\r", "");
        return "<p>" + b + "</p>";
	}
	
	public List<String> getImages() {
		if (images == null || images.size() < 1)
			return null;
		
		List<String> list = new ArrayList();
		Iterator iter = images.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    list.add(entry.getValue().toString());
		}
		return list;
	}
}
