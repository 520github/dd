package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.search.Searcher;
import me.twocoffee.entity.Account;
import me.twocoffee.service.AccountSearcher;
import me.twocoffee.service.AccountService;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountSearcherImpl implements AccountSearcher {
	@Autowired
	private AccountService accountService;

	private Searcher searcher = null;

	public AccountSearcherImpl(String url) {
		searcher = new Searcher(url);
	}

	@Override
	public PagedResult searchByName(String name, int offset, int limit) {
		QueryResponse r = searcher.searchResult("name:" + name, offset, limit);
		PagedResult result = new PagedResult();
		if (r != null) {
			result = new PagedResult();
			result.setTotal(r.getResults().getNumFound());
			if (result.getTotal() != 0) {
				List<String> list = new ArrayList();
				for (SolrDocument d : r.getResults()) {
					list.add(d.getFieldValue("id").toString());
				}
				result.setResult(list);
			}
		}

		return result;
	}

	@Override
	public PagedResult searchByLoginName(String loginName, int offset,
			int limit) {
		QueryResponse r = searcher.searchResult("loginName:" + loginName,
				offset, limit);
		PagedResult result = new PagedResult();
		if (r != null) {
			result = new PagedResult();
			result.setTotal(r.getResults().getNumFound());
			if (result.getTotal() != 0) {
				List<String> list = new ArrayList();
				for (SolrDocument d : r.getResults()) {
					list.add(d.getFieldValue("id").toString());
				}
				result.setResult(list);
			}
		}

		return result;
	}

	@Override
	public PagedResult search(String key, int sort, int offset, int limit) {
		QueryResponse r = searcher.searchResult(key, null, getSortString(sort),
				offset, limit);
		PagedResult result = new PagedResult();
		if (r != null) {
			result = new PagedResult();
			result.setTotal(r.getResults().getNumFound());
			if (result.getTotal() != 0) {
				List<String> list = new ArrayList();
				for (SolrDocument d : r.getResults()) {
					list.add(d.getFieldValue("id").toString());
				}
				result.setResult(list);
			}
		}

		return result;
	}

	private String getSortString(int sort) {
		return null;
	}

	@Override
	public void addIndex(String accountId) {
		addIndex(accountService.getById(accountId));
	}

	@Override
	public void addIndex(Account a) {
		if (a == null)
			return;

		searcher.index(toMap(a));
		searcher.commit();
	}

	private Map<String, Object> toMap(Account a) {
		Map<String, Object> map = new HashMap();
		map.put("id", a.getId());
		map.put("email", a.getEmail());
		map.put("loginName", a.getLoginName());
		map.put("accountType", a.getAccountType().toString());
		map.put("name", a.getName());
		map.put("moblie", a.getMobile());
		map.put("accountName", a.getAccountName());
		map.put("occupation", a.getOccupation());
		map.put("gender", getGender(a.getGender()));
		map.put("interest", getInterestString(a.getInterest()));
		return map;
	}

	private int getGender(String gender) {
		if (gender == null)
			return 3;
		if (gender.equals("male"))
			return 1;
		if (gender.equals("female"))
			return 2;
		return 3;
	}

	private String getInterestString(List<String> interest) {
		if (interest == null || interest.size() < 1)
			return "";
		StringBuffer sb = new StringBuffer();
		for (String s : interest) {
			sb.append(s);
			sb.append(",");
		}
		return sb.toString();
	}
}
