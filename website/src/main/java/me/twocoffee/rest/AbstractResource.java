package me.twocoffee.rest;

import java.util.Map;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.AvatarInfo;

public abstract class AbstractResource {
	@Autowired
	private TokenUtil tokenUtil;
	
	/**
	 * 获取校验authorization的错误代码
	 * @param authorization
	 * @return
	 */
	protected int getAuthorizationErrorCode(String authorization) {
		return tokenUtil.getAuthorizationErrorCode(authorization);
	}
	
	/**
	 * 同时判断是否校验匿名用户
	 * @param authorization
	 * @param checkGuestUser 是否校验匿名用户
	 * @return
	 */
	protected int getGuestAuthorizationErrorCode(String authorization,boolean checkGuestUser) {
		return tokenUtil.getGuestAuthorizationErrorCode(authorization, checkGuestUser);
	}
	
	/**
	 * 校验authorization是否是合法有效
	 * @param authorization
	 * @return
	 */
	protected boolean isInvalidAuthorization(String authorization) {
		return tokenUtil.isInvalidAuthorization(authorization);
	}
	
	/**
	 * 判断authorization是否为空
	 * @param authorization
	 * @return
	 */
	protected boolean isEmptyAuthorization(String authorization) {
		return TokenUtil.isEmptyAuthorization(authorization);
	}
	
	/**
	 * 判断是否是匿名用户
	 * @param authorization
	 * @return
	 */
	protected boolean isGuestUser(String authorization) {
		return tokenUtil.isGuestUser(authorization);
	}
	
	public String getAccountIdOrGuestId(String authorization) {
		return tokenUtil.getAccountIdOrGuestId(authorization);
	}
	
	/**
	 * 根据authorization获取登录账户信息
	 * @param authorization
	 * @return
	 */
	protected Account getLoginUserAccontByAuthorization(String authorization) {
		return tokenUtil.getLoginUserAccontByAuthorization(authorization);
	}
	
	/**
	 * 根据authorization获取匿名用户相关信息
	 * @param authorization
	 * @return
	 */
	protected AccountInfo getGuestUserAccountInfoByAuthorization(String authorization) {
		String guestUserId = tokenUtil.getGuestUserId(authorization);
		AccountInfo info = new AccountInfo();
    	info.setId(guestUserId);
    	info.setName("未登录用户");//
    	info.setRole((new RoleType[] {RoleType.Guest}));
    	Account account = new Account();
    	Map<String,String> avatarMap = account.getAvatarForMap();
    	AvatarInfo avatar = new AvatarInfo();
    	avatar.setLarge(avatarMap.get(Account.PHOTO_LARGE));
    	avatar.setMedium(avatarMap.get(Account.PHOTO_MEDIUM));
    	avatar.setSmall(avatarMap.get(Account.PHOTO_SMALL));
    	info.setAvatar(avatar);
		return info;
	}
	
	/**
	 * 请求内容未发生改变
	 * @param modifiedSince
	 * @param account
	 * @return
	 */
	protected Response getNotMondifiedResponse(String modifiedSince,Account account) {
		return tokenUtil.getNotMondifiedResponse(modifiedSince, account);
    }
	
	
	/**
	 * 校验Authorization
	 * @param authorization
	 * @return
	 */
	protected Response validAuthorization(String authorization) {
		int code  = this.getAuthorizationErrorCode(authorization);
		if(code > 200) {
			return Response.status(code).build();
		}
		return null;
	}
	
	/**
	 * 校验Authorization及匿名用户
	 * @param authorization
	 * @return
	 */
	protected Response validAuthorizationAndGuestUser(String authorization) {
		int code = this.getGuestAuthorizationErrorCode(authorization, true);
		if(code > 200) {
			return Response.status(code).build();
		}
		return null;
	}
}
