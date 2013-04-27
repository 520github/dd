package me.twocoffee.common.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {
	private String m_username = null;   
	  
    private String m_userpass = null;   
  
    void setUsername(String username) {   
        m_username = username;   
    }   
  
    void setUserpass(String userpass) {   
        m_userpass = userpass;   
    }   
  
    public EmailAuthenticator(String username, String userpass) {   
        super();   
        setUsername(username);   
        setUserpass(userpass);   
    }   
  
    public PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(m_username, m_userpass);   
    }   
}
