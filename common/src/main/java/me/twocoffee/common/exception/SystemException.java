package me.twocoffee.common.exception;

/**
 * 系统异常类，一般都是程序无法处理异常.
 * @author laiss
 *
 */
public class SystemException extends BaseException {
	
	private static final long serialVersionUID = 1L;

	public SystemException(String message, Throwable t) {
		super(message, t);
	}

	public SystemException(Throwable t) {
		super(t);
	}

	public SystemException(String message) {
		super(message);
	}
}
