package me.twocoffee.common.velocitytool;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.tools.view.ViewContext;

public class PagingTool {
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
    
    private final int defaultPageSize = 20;
    
    public String page(long recordCount) {
    	return page((int)recordCount, defaultPageSize);
    }
    
	public String page(int recordCount) {
		return page(recordCount, defaultPageSize);
	}
	
    public String page(long recordCount, int pageSize) {
    	return page((int)recordCount, pageSize);
    }
    
	public String page(int recordCount, int pageSize) {
		final int maxPage = 8;
		final int halfPage = maxPage / 2;
		int pageIndex = getPageIndex();
		int pageCount = recordCount / pageSize;
		if (recordCount % pageSize > 0) {
			pageCount += 1;
		}
		
		if (pageCount < 2)
			return "";
		
		String page = getPageUrl(request);
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"pageNav\">");
		
		String sp = "?";
		if (page.indexOf("?") > -1) {
			sp = "&";
		}
		int pp = pageIndex - 1;
		if (pp < 0) {
			pp = 0;
		}
		if (pageIndex < 1) {
			sb.append("");
		}
		else {
			sb.append("<span class=\"an\"><a href=\"" + page + sp + "page=" + pp + "\">上一页</a></span>");
		}
		
		int start = pageIndex - halfPage;
		int end = pageIndex + halfPage;
		if (start < 0) {
			end += 0 - start;
			start = 0;
		}
		if (end > pageCount) {
			start -= end - pageCount;
			if (start < 0) {
				start = 0;
			}
			end = pageCount;
		}

		if (start != 0) {
			sb.append("<a href=\"" + page + sp + "page=0\" class=\"pageid\">1</a>");
			if (start != 1)
			{
				sb.append("...");
			}
		}
		for (int i=start; i<end; i++){
			if (i == pageIndex) {
				sb.append("<strong>"+(i + 1)+"</strong>");
			}
			else {
				sb.append("<a href=\"" + page + sp + "page=" + i + "\" class=\"pageid\">" + (i+1) + "</a>");
			}
		}
		
		if (end != pageCount) {
			if (end != (pageCount-1))
			{
				sb.append("...");
			}
			sb.append("<a href=\"" + page + sp + "page=" + (pageCount - 1) + "\" class=\"pageid\">"+pageCount+"</a>");
		}
		if (pageIndex >= (pageCount - 1)) {
			sb.append("");
		}
		else {
			sb.append("<a href=\"" + page + sp + "page=" + (pageIndex+1) + "\" class=\"xia\">下一页></a>");
		}
		
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getPageUrl(HttpServletRequest request) {
		String path = request.getRequestURI();
		int index = path.lastIndexOf("/");
		if (index > -1) {
			path = path.substring(index + 1);
		}
		
		Map map = request.getParameterMap();
		if (map == null || map.size() < 1)
			return path;
		
		path += "?";
		for(Object obj : map.keySet()) {     
		    Object key = obj;     
		    Object value = map.get(obj);  
		
		    String sk = key.toString();
		    if (sk.equals("page"))
				continue;
			
		    if (value instanceof Object[]){
		    	path += sk + "=" + ((Object[])value)[0] + "&";
		    }else{
		    	path += sk + "=" + value + "&";
		    }
			
		} 
		path = path.substring(0, path.length()-1);
		
		return path;
	}
	

	private int getPageIndex() {
		int index = 0;
		if (request.getParameter("page") != null) {
			index = Integer.parseInt(request.getParameter("page"));
		}
		
		return index;
	}
}
