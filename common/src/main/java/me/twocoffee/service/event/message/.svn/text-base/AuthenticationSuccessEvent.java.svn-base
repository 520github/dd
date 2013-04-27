package me.twocoffee.service.event.message;

import me.twocoffee.entity.Account;

import org.springframework.context.ApplicationEvent;

/**
 * 用户登录成功时会触发的事件
 * 
 * @author Dante
 * 
 */
public class AuthenticationSuccessEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -319670038292231112L;

    private final Account account;

    public AuthenticationSuccessEvent(Object source, Account account) {
	super(source);
	this.account = account;
    }

    /**
     * 登录成功用户的Account对象
     * 
     * @return
     */
    public Account getAccount() {
	return account;
    }

}
