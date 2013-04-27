package me.twocoffee.common.push.apns;

import com.notnoop.apns.ApnsService;

/**
 * ApnsService对象的创建工厂
 * 
 * @author chongf
 */
public interface ApnsServiceFactory {
	ApnsService getInstance();
}
