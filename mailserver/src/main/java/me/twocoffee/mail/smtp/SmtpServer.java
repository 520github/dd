package me.twocoffee.mail.smtp;

import java.net.InetSocketAddress;
import java.util.Arrays;

import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.dao.AccountDao;
import me.twocoffee.mail.ServerLogger;
import me.twocoffee.mail.service.AccountService;
import me.twocoffee.mail.service.ContentServiceHttpImpl;

import org.apache.james.protocols.api.Protocol;
import org.apache.james.protocols.api.ProtocolServer;
import org.apache.james.protocols.api.handler.WiringException;
import org.apache.james.protocols.netty.NettyServer;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.apache.james.protocols.smtp.hook.Hook;
import org.apache.james.protocols.smtp.hook.SimpleHook;
import org.springframework.context.ApplicationContext;

public class SmtpServer {
	private ApplicationContext context = null;
	
	private ProtocolServer server = null;
	
	public SmtpServer(ApplicationContext ac) {
		context = ac;
	}
	
	public void start() {
		InetSocketAddress address = new InetSocketAddress(25);
        
        try {
			server = createServer(createProtocol(), address);
			server.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		server.unbind();
	}
	
	protected ProtocolServer createServer(Protocol protocol, InetSocketAddress address) {
        NettyServer server =  new NettyServer(protocol);
        server.setListenAddresses(address);
        return server;
    }
	
	protected Protocol createProtocol() throws WiringException {
		Hook[] handlers = new Hook[]{
			new SmtpHook(context.getBean(AccountService.class), context.getBean(FileOperator.class), context.getBean(ContentServiceHttpImpl.class))
		};
        SMTPProtocolHandlerChain chain = new SMTPProtocolHandlerChain();
        chain.addAll(0, Arrays.asList(handlers));
        chain.wireExtensibleHandlers();
        return new SMTPProtocol(chain, new Configuration(), new ServerLogger());
    }
	
	public void waitServer() throws InterruptedException {
		server.wait();
	}
}
