package me.twocoffee.daemon;

import me.twocoffee.service.thirdparty.impl.ThirdPartyAuthJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServerMain {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
	
	public static void main(String[] argv) {
		
		if (argv != null && argv.length > 0 && "test".equals(argv[0])) {
			logger.debug("test daemon");
			ApplicationContext ctx = new ClassPathXmlApplicationContext("test_applicationContext.xml");
			ThirdPartyAuthJob thirdPartyAuthJob = ctx.getBean(ThirdPartyAuthJob.class);
			thirdPartyAuthJob.execute();
			return;
		}
		logger.debug("run daemon");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ServerMain main = new ServerMain();
		try {
			synchronized(main) {
				main.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
