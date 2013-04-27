package me.twocoffee.rest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.AvatarInfo;
import me.twocoffee.rest.entity.FriendLogInfo;
import me.twocoffee.rest.entity.SimpleAccountInfo;
import me.twocoffee.service.AccountService;

import org.apache.commons.lang.StringUtils;

public class InfoConverter {

    private static final String VERSION_PATTERN = "DuoDuo/([\\d\\.]+?)[\\s(]";
    private static Pattern VERSION = Pattern.compile(VERSION_PATTERN);

    private static Account getAccountByList(List<Account> accounts,
	    String accountId) {

	if (accounts == null || accounts.size() < 1)
	    return null;

	for (Account a : accounts) {

	    if (accountId.equals(a.getId()))
		return a;
	}
	return null;
    }

    public static Account convertToAccount(AccountInfo value) {
	Account account = new Account();
	account.setId(value.getId());
	// account.setAccountName(value.getAccountName());
	account.setAccountType(value.getAccountType());
	account.setBirthday(value.getBirthday());
	account.setDescribe(value.getIntroduction());
	account.setEmail(value.getEmail());
	account.setGender(value.getGender());
	account.setLoginName(value.getLoginName());
	account.setMobile(value.getMobile());
	account.setName(value.getName());
	account.setDuoduoEmail(value.getCoffeeMail());
	AvatarInfo avatar = value.getAvatar();

	if (avatar != null) {
	    account.setAvatar(Account.PHOTO_SIZE_BIG, avatar.getLarge());
	    account.setAvatar(Account.PHOTO_SIZE_MIDDLE, avatar.getMedium());
	    account.setAvatar(Account.PHOTO_SIZE_SMALL, avatar.getSmall());
	}
	account.setRoleByArray(value.getRole());
	account.setStatByArray(value.getStat());
	return account;
    }

    public static AccountInfo convertToAccountInfo(Account a) {
	AccountInfo ai = new AccountInfo();
	ai.setId(a.getId());
	ai.setEmail(a.getEmail());
	ai.setGender(StringUtils.isBlank(a.getGender()) ? "unknow" : a
		.getGender());

	ai.setMobile(a.getMobile());
	ai.setName(a.getName());
	ai.setNameInPinyin(a.getNameInPinyin());
	ai.setIntroduction(a.getDescribe());
	ai.setAccountType(a.getAccountType());

	if (StringUtils.isNotBlank(a.getBirthday())) {
	    ai.setBirthday(DateUtil.getBirthdayStr(a.getBirthday()));
	}
	ai.setLoginName(a.getLoginName());
	ai.setRole(a.getRole().toArray(new RoleType[] {}));
	ai.setStat(a.getStat().toArray(new Status[] {}));
	ai.setAccountName(a.getAccountName());
	ai.setCoffeeMail(a.getDuoduoEmail());

	if (a.getMailConfig() != null) {
	    ai.setEmailWhiteList(a.getMailConfig().getMails());
	}

	if (a.getPhotos() != null) {
	    AvatarInfo avatar = new AvatarInfo();
	    avatar.setLarge(a.getPhotos().get(Account.PHOTO_SIZE_BIG));
	    avatar.setMedium(a.getPhotos().get(Account.PHOTO_SIZE_MIDDLE));
	    avatar.setSmall(a.getPhotos().get(Account.PHOTO_SIZE_SMALL));
	    ai.setAvatar(avatar);
	}
	return ai;
    }

    public static List<AccountInfo> convertToAccounts(AccountService service,
	    List<String> accountIds) {

	List<Account> list = service.findByIdList(accountIds);

	if (list == null || list.size() < 1)
	    return null;

	List<AccountInfo> accounts = new ArrayList<AccountInfo>();

	for (Account a : list) {
	    AccountInfo ai = convertToAccountInfo(a);
	    accounts.add(ai);
	}
	return accounts;
    }

    public static List<AccountInfo> convertToAccounts(List<Account> list) {

	if (list == null || list.size() < 1)
	    return null;

	List<AccountInfo> accounts = new ArrayList<AccountInfo>();

	for (Account a : list) {
	    AccountInfo ai = convertToAccountInfo(a);
	    accounts.add(ai);
	}
	return accounts;
    }

    public static FriendLogInfo convertToFriendLog(FriendLog f,
	    List<Account> accounts) {

	FriendLogInfo log = new FriendLogInfo();
	log.setDate(f.getCreateTime());
	Account a = getAccountByList(accounts, f.getAccountId());
	SimpleAccountInfo sa = new SimpleAccountInfo();
	sa.setAccountId(a.getId());
	sa.setName(a.getName());
	log.setFrom(sa);
	a = getAccountByList(accounts, f.getFriendId());
	sa = new SimpleAccountInfo();
	sa.setAccountId(a.getId());
	sa.setName(a.getName());
	log.setTo(sa);
	return log;
    }

    public static List<FriendLogInfo> convertToFriendLogs(List<FriendLog> list,
	    List<Account> accounts) {

	if (list == null || list.size() < 1)
	    return null;

	List<FriendLogInfo> logs = new ArrayList<FriendLogInfo>();

	for (FriendLog f : list) {
	    FriendLogInfo fl = convertToFriendLog(f, accounts);
	    logs.add(fl);
	}
	return logs;
    }

    public static String getVersionFromUserAgent(String ua) {

	if (StringUtils.isBlank(ua)) {
	    return "";
	}
	Matcher m = VERSION.matcher(ua);

	if (m.find()) {
	    return m.group(1);
	}
	return "";
    }

    public static void main(String[] args) {
	String u1 = "DuoDuo/1.1.0 Build:404 (iPhone; iPhone OS 5.1; en_US)";
	String u2 = "DuoDuo/1.1.0 (iPhone; iPhone OS 5.1; en_US)";
	String u3 = "DuoDuo/1.1.0(iPhone; iPhone OS 5.1; en_US)";
	String u4 = "DuoDuo/1 Build:404 (iPhone; iPhone OS 5.1; en_US)";
	String u5 = "DuoDuo/1 (iPhone; iPhone OS 5.1; en_US)";
	String u6 = "DuoDuo/1(iPhone; iPhone OS 5.1; en_US)";
	String u7 = "DuoDuo/1.0.0(Android)";
	System.out.println(getVersionFromUserAgent(u1));
	System.out.println(getVersionFromUserAgent(u2));
	System.out.println(getVersionFromUserAgent(u3));
	System.out.println(getVersionFromUserAgent(u4));
	System.out.println(getVersionFromUserAgent(u5));
	System.out.println(getVersionFromUserAgent(u6));
	System.out.println(getVersionFromUserAgent(u7));

    }
}
