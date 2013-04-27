package me.twocoffee.daemon;

public interface MessageQueueDisposer {
	String getQueueName();
	
	boolean dispose(Object data);
}
