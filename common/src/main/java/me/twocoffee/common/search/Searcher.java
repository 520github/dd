package me.twocoffee.common.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

public class Searcher {
	protected SolrServer solr = null;

	/**
	 * solr search
	 * 
	 * @param path
	 */
	public Searcher(String url) {
		try {
			CommonsHttpSolrServer server = new CommonsHttpSolrServer(url);
			server.setSoTimeout(10000); // socket read timeout
			server.setConnectionTimeout(100);
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
			server.setFollowRedirects(false); // defaults to false
			// allowCompression defaults to false.
			// Server side must support gzip or deflate for this to have any
			// effect.
			server.setAllowCompression(true);
			server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
			solr = server;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			solr = null;
		}
	}

	/**
	 * 添加索引
	 * 
	 * @param obj
	 */
	public void index(Object obj) {
		try {
			solr.addBean(obj);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加索引
	 * 
	 * @param obj
	 */
	public void index(Map<String, Object> map) {
		if (map == null || map.size() < 1)
			return;

		SolrInputDocument sid = new SolrInputDocument();
		for (String key : map.keySet()) {
			sid.addField(key, map.get(key));
		}

		try {
			solr.add(sid);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param id
	 */
	public void unindex(String id) {
		try {
			solr.deleteById(id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交
	 */
	public void commit() {
		try {
			solr.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 优化
	 */
	public void optimize() {
		try {
			solr.optimize();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 搜索
	 * 
	 * @param cl
	 * @param queryString
	 * @param startRow
	 * @param rowSize
	 * @return
	 */
	public List search(Class cl, String queryString, int startRow, int rowSize) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		query.setStart(startRow);
		query.setFacetLimit(rowSize);
		QueryResponse response = null;
		try {
			response = solr.query(query);
			return response.getBeans(cl);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 搜索
	 * 
	 * @param cl
	 * @param queryString
	 * @param startRow
	 * @param rowSize
	 * @return
	 */
	public List search(Class cl, String queryString, String filterQuery,
			String sort, int startRow, int rowSize) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		query.setFilterQueries(filterQuery);
		query.setStart(startRow);
		query.setFacetLimit(rowSize);
		setSortField(query, sort);
		QueryResponse response = null;
		try {
			response = solr.query(query);
			return response.getBeans(cl);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setSortField(SolrQuery query, String sort) {
		ORDER o = ORDER.asc;
		String f = sort;
		int index = sort.indexOf(" ");
		if (index > -1) {
			String[] ss = sort.split(" ");
			f = ss[0];
			if (ss[1].toLowerCase().equals("des")) {
				o = ORDER.desc;
			}
		}

		query.setSortField(f, o);
	}

	/**
	 * 搜索
	 * 
	 * @param queryString
	 * @param startRow
	 * @param rowSize
	 * @return
	 */
	public QueryResponse searchResult(String queryString, int startRow,
			int rowSize) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		query.setStart(startRow);
		query.setFacetLimit(rowSize);
		try {
			return solr.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 搜索
	 * 
	 * @param queryString
	 * @param startRow
	 * @param rowSize
	 * @return
	 */
	public QueryResponse searchResult(String queryString, String filterQuery,
			String sort, int startRow, int rowSize) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString).setFilterQueries(filterQuery)
				.setStart(startRow).setRows(rowSize);
		setSortField(query, sort);
		try {
			return solr.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void removeById(String id) {
		try {
			solr.deleteById(id);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
