package me.twocoffee.service;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Token;

public interface SendEmailService {
	public void sendInvitation(String email, String code, String name);

	public void sendVerifyAccountEmail(String email, Token token, String invite);

	public void sendWelcomeEmail(Account account);
}
