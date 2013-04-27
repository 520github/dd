package me.twocoffee.common.util;

/**
 * Id生成器
 * @author Leon
 *
 */
public interface IdGenerator {
	/**
	 * 获取下一个ID
	 * @return 返回下一个ID
	 */
	public long nextId();
}
