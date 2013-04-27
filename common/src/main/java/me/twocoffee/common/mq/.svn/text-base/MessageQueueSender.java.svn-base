package me.twocoffee.common.mq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.surftools.BeanstalkClient.Client;

public class MessageQueueSender {
	private Client queueClient = null;
	
	public MessageQueueSender(Client c) {
		queueClient = c;
	}
	
	//默认优先级
	public static final long defaultPriority = 1024;
	//失败后的优先级
	public static final long faultPriority = 2048;
	//延迟时间 
	public static final int delaySeconds = 5000;
	//获取数据的超时时间
	public static final int timeoutSeconds = 3000;
	
	//获取数据的超时时间
	public static final int defaultTimeToRun = 10000;
		
	public void send(String queueName, Object obj) {
		send(queueName, obj, defaultTimeToRun);
	}
	
	public void send(String queueName, Object obj, int timeToRun) {
		MessageQueueEntity e = new MessageQueueEntity();
		e.setQueueName(queueName);
		e.setData(obj);
		
		byte[] buffer = serializableForJob(e);
		if (buffer == null) {
			return;
		}
		queueClient.put(defaultPriority, 0, timeToRun, buffer);
	}
	
	private byte[] serializableForJob(MessageQueueEntity e) {
		ByteArrayOutputStream bs = null;
		ObjectOutputStream out = null;
		try {
			bs = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bs);
			out.writeObject(e);
			
			return bs.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				out.close();
				bs.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
