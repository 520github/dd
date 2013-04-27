package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import me.twocoffee.common.BaseDao;
import me.twocoffee.common.util.Pinyin4jUtils;
import me.twocoffee.dao.AccountDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.exception.DuplicateCoffeemailException;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.mongodb.MongoException.DuplicateKey;

@org.springframework.stereotype.Repository
public class AccountDaoImpl extends BaseDao<Account> implements AccountDao {
    // private static final String ID = "_id";
    // private static final String EMAIL_VERIFIED = "emailVerified";
    // private static final String MOBILE_VERIFIED = "mobileVerified";
    private static final String LOGIN_NAME = "loginName";
    private static final String COFFEEMAIL = "duoduoEmail";

    private void throwException(DuplicateKey e, Account account) {

	if (e.getMessage().contains("nameIndex")) {
	    throw new DuplicateNameException();
	}

	if (e.getMessage().contains("loginNameIndex")) {
	    throw new DuplicateLoginNameException();
	}

	if (e.getMessage().contains("duoduoEmailIndex")) {
	    throw new DuplicateCoffeemailException();
	}
	Account a = getByName(account.getName());

	if (a != null && !a.getId().equals(account.getId())) {
	    throw new DuplicateNameException();
	}
	a = getByLoginName(account.getLoginName());

	if (a != null && !a.getId().equals(account.getId())) {
	    throw new DuplicateLoginNameException();
	}
	a = getByDuoduoEmail(account.getDuoduoEmail());

	if (a != null && !a.getId().equals(account.getId())) {
	    throw new DuplicateCoffeemailException();
	}
	throw new RuntimeException(e);
    }

    @Override
    public long countByDuoduoEmail(String email) {
	return dataStore.getCount(createQuery().filter("duoduoEmail", email));
    }

    @Override
    public List<Account> findAll() {
	return createQuery().asList();
    }

    @Override
    public List<Account> findByAccountIdList(List<String> ids) {
	return createQuery().filter("id in", ids).asList();
    }

    @Override
    public List<Account> findByName(String name) {
	Pattern p = Pattern.compile("^.*" + name + ".*$",
		Pattern.CASE_INSENSITIVE);
	return createQuery().filter("name ", p).asList();
    }

    @Override
    public List<FriendMailConfig> findFriendMailConfigsByDuoduoMail(String mail) {
	Account a = this.getByDuoduoEmail(mail);
	if (a == null)
	    return null;

	return this.dataStore.createQuery(FriendMailConfig.class)
		.filter("accountId =", a.getId()).asList();
    }

    @Override
    public Account getByAccountName(String domain) {
	return createQuery().filter("accountName =", domain).get();
    }

    @Override
    public Account getByDuoduoEmail(String mail) {
	return this.createQuery().filter(COFFEEMAIL, mail).get();
    }

    @Override
    public Account getByEmail(String email) {
	return createQuery().filter("email =", email).get();
    }

    @Override
    public Account getByLoginName(String loginName) {
	return createQuery().filter(LOGIN_NAME, loginName).get();
    }

    @Override
    public Account getByName(String name) {
	return createQuery().filter("name =", name).get();
    }

    @Override
    public AccountMailConfig getMailConfig(String accountId) {
	Account a = this.dataStore.createQuery(Account.class)
		.filter("id =", accountId).get();

	return a == null ? null : a.getMailConfig();
    }

    @Override
    public void save(Account t) throws DuplicateNameException,
	    DuplicateLoginNameException, DuplicateCoffeemailException {

	if (t != null && StringUtils.isBlank(t.getId())) {
	    t.setId(new ObjectId().toString());

	    if (t.getCreateDate() == null) {
		t.setCreateDate(new Date());
	    }
	    t.setLastModified(t.getCreateDate());

	    if (StringUtils.isBlank(t.getNameInPinyin())) {
		t.setNameInPinyin(Pinyin4jUtils.getPinYin(t.getName()));
	    }
	}

	if (t != null && StringUtils.isNotBlank(t.getId())) {
	    t.setLastModified(new Date());
	}

	try {
	    super.save(t);

	} catch (DuplicateKey e) {
	    throwException(e, t);
	}
    }

    @Override
    public Account updateAccount(Account account)
	    throws DuplicateNameException,
	    DuplicateLoginNameException, DuplicateCoffeemailException {

	if ((account != null) && (account.getId() != null)
		&& (!account.getId().equals(""))) {

	    account.setLastModified(new Date());

	    try {
		super.save(account);

	    } catch (DuplicateKey e) {
		throwException(e, account);
	    }
	    return account;
	}
	return null;
    }

    @Override
    public List<Account> getPublicAccounts() {
	return createQuery().filter("role", Account.RoleType.Public).asList();
    }
    
    @Override
    public Account getPublicAccountById(String id) {
    	return createQuery().filter("id", id).filter("role", Account.RoleType.Public).get();
    }

}
