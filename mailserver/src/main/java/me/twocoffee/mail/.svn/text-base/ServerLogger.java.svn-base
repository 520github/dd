package me.twocoffee.mail;

import org.apache.james.protocols.api.logger.Logger;
import org.slf4j.LoggerFactory;

public class ServerLogger implements Logger {
	protected final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String getName() {
        return "server logger";
    }

    public boolean isTraceEnabled() {
    	return log.isTraceEnabled();
    }

    public void trace(String msg) {
    	log.trace(msg);
    }

    public void trace(String msg, Throwable t) {
        log.trace(msg, t);
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public void debug(String msg) {
        log.debug(msg);
    }

    public void debug(String msg, Throwable t) {
        log.debug(msg, t);
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public void info(String msg) {
        log.info(msg);
    }

    public void info(String msg, Throwable t) {
        log.info(msg, t);
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public void warn(String msg) {
        log.warn(msg);
    }

    public void warn(String msg, Throwable t) {
        log.warn(msg, t);
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public void error(String msg) {
        log.error(msg);
    }

    public void error(String msg, Throwable t) {
        log.error(msg, t);
    }
    
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }

    public void fatal(String message) {
        log.error(message);
    }

    public void fatal(String message, Throwable t) {
        log.error(message, t);
    }
}
