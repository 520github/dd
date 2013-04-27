/**
 * 
 */
package me.twocoffee.rest.entity;

/**
 * @author xuehui.miao
 *
 */
public class UserTag {
	//错误消息类型
	public static enum MessageType {
		//为空
		Empty,
		//长度太长，超过了32个字符
		LengthExceed,
		//不存在标签
		NotExist,
		
	}
	
	//原来的tag名称
	private String name;
	//新的tag名称
	private String newName;
	//错误信息
	private MessageType message;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public MessageType getMessage() {
		return message;
	}
	public void setMessage(MessageType message) {
		this.message = message;
	}
	
}
