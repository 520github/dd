package me.twocoffee.controller;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.CookieTool;
import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Repository;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentDetail;

@Controller
@RequestMapping("search")
public class SearchController extends BaseController {
	@Autowired
	private ContentSearcher contentSearcher = null;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add() {
		return "search/add";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) 
			throws Exception {
		Map<String, Object> map = new HashMap();
		try {
			map.put("id", request.getParameter("id"));
			map.put("accountId", request.getParameter("accountId"));
			map.put("systemTag", getSystemTag(Integer.valueOf(request.getParameter("indexType"))));
			map.put("source", Integer.valueOf(request.getParameter("source")));
			map.put("contentType", Integer.valueOf(request.getParameter("contentType")));
			map.put("created", getDateFormString(request.getParameter("created"), "yyyy-MM-dd"));
			map.put("title", request.getParameter("title"));
			map.put("language", request.getParameter("language"));
			map.put("content", request.getParameter("content"));
			map.put("tag", request.getParameter("tag"));
			map.put("commentCount", Integer.valueOf(request.getParameter("commentCount")));
			map.put("visitCount", Integer.valueOf(request.getParameter("visitCount")));
			map.put("collectCount", Integer.valueOf(request.getParameter("collectCount")));
			map.put("shareCount", Integer.valueOf(request.getParameter("shareCount")));
			
			contentSearcher.addIndex(map);
		}
		catch (Exception ex) {
			model.addAttribute("error", ex.getMessage());
		}
		return "search/add";
	}
	
	private String getSystemTag(int indexType) {
		if (indexType == 1)
			return "Collect";
		if (indexType == 2)
			return "Later";
		return "Unread";
	}
	
	@RequestMapping("query")
	public String query(String q, String accountId, Integer indexType, 
			Integer source, Integer contentType, String tag, String userTag, Integer sort, String language, Model model) {
		PagedResult r = contentSearcher.list(accountId, userTag,q, tag,
				sort == null ? 0 : sort, language, 0, 100);
		model.addAttribute("result", r);
		return "search/query";
	}
	
	private Date getDateFormString(String dateString, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);
			return df.parse(dateString);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping
	public String search(String q, Integer type, Model model) {
		if (q == null || q.equals(""))
			return "search/search";
		
		if (type == null || type.intValue() == 0) {
			return searchContent(q, model);
		}
		else if (type.intValue() == 1) {
			return searchPeople(q, model);
		}
		return "search/search";
	}

	private String searchPeople(String q, Model model) {
		List<Account> list = accountService.findByName(q);
		model.addAttribute("q", q);
		model.addAttribute("accounts", list);
		return "search/search_people";
	}

	private String searchContent(String q, Model model) {
		PagedResult r = contentSearcher.search(null, null, q, null, 0, null, 0, 50);
		model.addAttribute("result", r);
		model.addAttribute("q", q);
		return "search/search_content";
	}
	
	
	@RequestMapping(value = "addIndex", method = RequestMethod.POST)
	public String addIndex(HttpServletRequest request, Model model){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if("twocoffee_babeeta".equals(username)&&"500509bb210c9856981f3992".equals(password)){
			new Thread(){

				@Override
				public void run() {
					super.run();
					List<Repository> list = repositoryService.getAllRepository();
					for (Repository repository : list) {
						contentSearcher.addIndex(repository.getId());
					}
					
				}
			
			}.start();
		
			System.out.println("更新完毕......");
			
		}
		
			
		
	return "search/search";
	}
}
