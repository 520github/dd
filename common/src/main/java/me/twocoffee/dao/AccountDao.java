package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.exception.DuplicateCoffeemailException;
import me.twocoffee.exception.DuplicateLoginNameException;
import me.twocoffee.exception.DuplicateNameException;

public interface AccountDao {

	long countByDuoduoEmail(String email);

	void delete(String id);

	List<Account> findAll();

	List<Account> findByAccountIdList(List<String> ids);

	List<Account> findByName(String name);

	List<FriendMailConfig> findFriendMailConfigsByDuoduoMail(String mail);

	Account getByAccountName(String domain);

	Account getByDuoduoEmail(String mail);

	Account getByEmail(String email);

	Account getById(String id);

	Account getByLoginName(String loginName);

	Account getByName(String name);

	AccountMailConfig getMailConfig(String accountId);

	void save(Account u) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException;

	Account updateAccount(Account account) throws DuplicateNameException,
			DuplicateLoginNameException, DuplicateCoffeemailException;

	List<Account> getPublicAccounts();
	
	Account getPublicAccountById(String id);
}