package me.twocoffee.common.velocitytool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.tools.view.ViewContext;

public final class SessionTool {
	
    protected HttpServletRequest request;

    /**
     * Sets the current {@link HttpServletRequest}. This is required
     * for this tool to operate and will throw a NullPointerException
     * if this is not set or is set to {@code null}.
     */
    public void setRequest(HttpServletRequest request)
    {
        if (request == null)
        {
            throw new NullPointerException("request should not be null");
        }
        this.request = request;
    }
    
    @Deprecated
    public void init(Object obj)
    {
        if (obj instanceof ViewContext)
        {
            ViewContext ctx = (ViewContext)obj;
            setRequest(ctx.getRequest());
        }
    }
    
	/**
	 * Get value from HttpSession, and then remove it from the session.
	 * @param key key used to get value from HttpSession
	 * @return
	 */
	public Object getValue(String key) {
		HttpSession session = request.getSession(true);
		Object result = session.getAttribute(key);
		session.removeAttribute(key);
		return result;
	}
	
	/**
	 * Set value to HttpSession, then you can get it by getValue(key).
	 * @param key key used to set value to HttpSession
	 * @param value the value to be set
	 * @return
	 */
	public void getValue(String key, String value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(key, value);
		return;
	}
}
