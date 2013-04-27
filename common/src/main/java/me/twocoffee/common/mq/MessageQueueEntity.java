package me.twocoffee.common.mq;

import java.io.Serializable;
import java.util.Map;

public class MessageQueueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String queueName;
	
	private Object data;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
