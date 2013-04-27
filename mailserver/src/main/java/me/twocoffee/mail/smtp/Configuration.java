package me.twocoffee.mail.smtp;

import org.apache.james.protocols.api.ProtocolConfigurationImpl;
import org.apache.james.protocols.smtp.SMTPConfiguration;

public class Configuration extends ProtocolConfigurationImpl implements SMTPConfiguration {
	//public String helloName = "localhost";
    private long maxMessageSize = 0;
    private boolean bracketsEnforcement = true;
    private boolean enforceHeloEhlo = true;

    public Configuration() {
        setSoftwareName("SMTP Server");
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#getMaxMessageSize()
     */
    public long getMaxMessageSize() {
        return maxMessageSize;
    }

    /**
     * Return <code>false</code>
     */
    public boolean isRelayingAllowed(String remoteIP) {
        return false;
    }

    /**
     * Return <code>false</code>
     */
    public boolean isAuthRequired(String remoteIP) {
        return true;
    }

    public void setHeloEhloEnforcement(boolean enforceHeloEhlo) {
        this.enforceHeloEhlo = enforceHeloEhlo;
    }
    
    
    /*
     * (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#useHeloEhloEnforcement()
     */
    public boolean useHeloEhloEnforcement() {
        return enforceHeloEhlo;
    }
    
    
    /*
     * (non-Javadoc)
     * @see org.apache.james.protocols.smtp.SMTPConfiguration#useAddressBracketsEnforcement()
     */
    public boolean useAddressBracketsEnforcement() {
        return bracketsEnforcement;
    }

    
    public void setUseAddressBracketsEnforcement(boolean bracketsEnforcement) {
        this.bracketsEnforcement = bracketsEnforcement;
    }
}
