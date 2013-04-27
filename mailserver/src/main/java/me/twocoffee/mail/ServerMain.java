package me.twocoffee.mail;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import me.twocoffee.mail.smtp.SmtpServer;

public class ServerMain {
	public static void main(String[] argv) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		SmtpServer s = new SmtpServer(ctx);
		s.start();
		
		try {
			synchronized(s) {
				s.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			s.stop();
		}
	}

	private static String getContextPath() {
		String path = System.getProperty("user.dir");
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		return path + "applicationContext.xml";
	}
}
