package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.search.Searcher;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.OrderEnum;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ContentDetail;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentSearcherImpl implements ContentSearcher {
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TagService tagService;

	private Searcher searcher = null;

	public ContentSearcherImpl(String url) {
		searcher = new Searcher(url); 
	}

	@Override
	public void addIndex(String repositoryId) {
		ContentDetail detail = getContentDetail(repositoryId);
		if (detail == null)
			return;

		searcher.index(toMap(detail));
		searcher.commit();
	}

	private Map<String, Object> toMap(ContentDetail detail) {
		Map<String, Object> map = new HashMap();
		map.put("id", detail.getRepository().getId());
		map.put("accountId", detail.getRepository().getAccountId());
		map.put("source", 0);
		map.put("contentType", getContentType(detail.getContent()
				.getContentType()));
		map.put("created", detail.getRepository().getDate());
		if (detail.getRepository().getLastModified() == null) {
			map.put("lastModified", detail.getRepository().getDate());
		}
		else {
			map.put("lastModified", detail.getRepository().getLastModified());
		}
		map.put("title", detail.getContent().getTitle());
		map.put("content", getContent(detail.getContent()));
		map.put("tag", getTagString(detail.getRepository().getUserTag()));
		map.put("systemTag", getTagString(detail.getRepository().getTag()));
		map.put("language", detail.getContent().getLanguage());
		map.put("sharedFrom", getSharedString(detail.getRepository()));
		map.put("sharedTime", getSharedTime(detail.getRepository()));
		map.put("updateTime", getUpdateTime(map));
		if (detail.getContent().getCounter() != null) {
			map.put("commentCount", detail.getContent().getCounter()
					.getComment());
			map.put("visitCount", detail.getContent().getCounter().getVisit());
			map.put("collectCount", detail.getContent().getCounter()
					.getCollect());
			map.put("shareCount", detail.getContent().getCounter().getShare());
		} else {
			map.put("commentCount", 0);
			map.put("visitCount", 0);
			map.put("collectCount", 0);
			map.put("shareCount", 0);
		}
		return map;
	}

	private Date getUpdateTime(Map<String, Object> map) {
		Date d1 = (Date)map.get("created");
		Date d2 = (Date)map.get("sharedTime");
		if (d1 == null)
			return d2;
		if (d2 == null)
			return d1;
		return d1.after(d2) ? d1 : d2;
	}

	private Date getSharedTime(Repository r) {
		if (r.getFromFriends() == null || r.getFromFriends().size() < 1)
			return null;
		Date dt = null;
		for (Repository.FriendShare f : r.getFromFriends()) {
			if (dt == null)
				dt = f.getShareTime();
			else if (f.getShareTime().after(dt))
				dt = f.getShareTime();
		}
		return dt;
	}

	private String getSharedString(Repository r) {
		if (r.getFromFriends() == null || r.getFromFriends().size() < 1)
			return "";
		
		StringBuffer sb = new StringBuffer();
		for (Repository.FriendShare f : r.getFromFriends()) {
			sb.append(f.getFriendId());
			sb.append(" ");
		}
		return sb.toString();
	}

	private String getContent(Content content) {
		if (content.getContentType() == ContentType.HtmlClip)
			return content.getHtmlPayload().getContent();

		return content.getSummary();
	}

	public int getContentType(ContentType contentType) {
		if (contentType == Content.ContentType.HtmlClip)
			return 2;
		if (contentType == Content.ContentType.Product)
			return 4;
		if (contentType == Content.ContentType.Web)
			return 1;
		if (contentType == Content.ContentType.Image)
			return 3;
		return 0;
	}

	private String getTagString(List<String> tags) {
		if (tags == null)
			return "";
		String tag = "";
		for (String t : tags) {
			//tag += t.replace("_", "") + " ";
			tag += t + " ";
		}
		return tag;
	}
	private ContentDetail getContentDetail(String repositoryId) {
		List<String> ids = new ArrayList();
		ids.add(repositoryId);
		List<ContentDetail> cs = repositoryService.findContentDetailsById(ids);
		if (cs == null || cs.size() < 1)
			return null;
		return cs.get(0);
	}

	@Override
	public PagedResult search(String accountId, String tag, String key,
			String userTag, int sortType, String language,
			int offset, int limit) {
		return search(accountId, tag, key, userTag, null, sortType, language, offset, limit);
	}

	/**
	 * 
	 * @param sortType
	 *            排序类型，0时间倒序，1评论倒序，2阅读倒序，3分享倒序，4收藏倒序
	 * @param tag 
	 * @return
	 */
	private String getSortString(int sortType, String tag) {
		if (sortType == 1)
			return "commentCount des";
		if (sortType == 2)
			return "visitCount des";
		if (sortType == 3)
			return "shareCount des";
		if (sortType == 4)
			return "collectCount des";
		if (sortType == 5)
			return "lastModified des";
		if(tag == null) {
			return "created des";
		}
		boolean f = tag.indexOf(tagService.getSystemTagName(SystemTagEnum.Source_Friend)) > -1;
		boolean c = tag.indexOf(tagService.getSystemTagName(SystemTagEnum.Collect)) > -1
				|| tag.indexOf(tagService.getSystemTagName(SystemTagEnum.Later)) > -1;
		if (f && c)
			return "updateTime des";
		
		if (f)
			return "sharedTime des";
		
		return "created des";
	}
	
	/**
	 * 获取排序数值
	 * 
	 * @param order  排序枚举
	 * @return
	 */
	public int getSortType(String order) {
		int sortType = 0;
		if(OrderEnum.timeline.toString().equalsIgnoreCase(order)) {
			sortType = 0;
		}
		else if(OrderEnum.comment.toString().equalsIgnoreCase(order)) {
			sortType = 1;
		}
		else if(OrderEnum.visit.toString().equalsIgnoreCase(order)) {
			sortType = 2;
		}
		else if(OrderEnum.share.toString().equalsIgnoreCase(order)) {
			sortType = 3;
		}
		else if(OrderEnum.collect.toString().equalsIgnoreCase(order)) {
			sortType = 4;
		}
		return sortType;
	}
	
	private String getFilterQuery(String publicCond,String accountId, String orTags, String tag, 
			String userTag, String language, String friend) {
		String fq = this.getFilterQuery(accountId, orTags, tag, userTag, language, friend);
		if(StringUtils.isBlank(fq)) {
			if(StringUtils.isNotBlank(publicCond)) {
				return "systemTag:" +publicCond;
			}
		}
		else if(StringUtils.isNotBlank(publicCond)) {
			if(publicCond.indexOf(":") >-1) {
				fq = "(" + publicCond + " OR " + "(" + fq + "))";
			}
			else {
				fq = "(systemTag:" + publicCond + " OR " + "(" + fq + "))";
			}
		}
		return fq;
	}

	private String getFilterQuery(String accountId, String orTags, String tag, 
			String userTag, String language, String friend) {
		String fq = "";
		if (accountId != null && !accountId.equals("")) {
			fq += "+accountId:" + accountId + " ";
		}
		if (orTags != null && !orTags.equals("")) {
			String or ="+(";
			String ots[] = orTags.split(",");
			for (String orTag:ots) {
				if(orTag.trim().length() < 1)continue;
				or+= "systemTag:" + orTag + " ";
			}
			or+=") ";
			fq += or;
		}
		if(StringUtils.isBlank(tag)) {//排除已删除
			fq += "-systemTag:" + SystemTagEnum.Delete;
		}
		if (tag != null && !tag.equals("")) {
			String tagName = "+systemTag";
			String[] ss = tag.split(",");
			int i = 0;
			for (String s : ss) {
				
				if (s == null || s.equals(""))
					continue;
				if(s.contains("Link")){
					if(i==0){
						fq+="systemTag:" + s + " ";
						i++;
					}else{
						fq+="systemTag:" + s + " ";
					}
					
				     
				}else{
				fq += "+systemTag:\"" + s + "\" ";
				}
			}
		}
		if (userTag != null && !userTag.equals("")) {
			fq += "+tag:" + userTag + " ";
		}
		if (language != null && !language.equals("")) {
			fq += "+language:" + language;
		}
		if (friend != null && !friend.equals("")) {
			fq += "+sharedFrom:" + friend;
		}
		return fq;
	}

	@Override
	public void addIndex(Map<String, Object> map) {
		if (map == null)
			return;

		searcher.index(map);
		searcher.commit();
	}

	@Override
	public PagedResult list(String accountId, String tag, String key,
			String userTag, int sortType,
			String language, int offset, int limit) {
		return list(accountId, tag, key, userTag, null,
				sortType,
				language, offset, limit);
	}

	@Override
	public void addIndex(ContentDetail detail) {
		if (detail == null)
			return;

		searcher.index(toMap(detail));
		searcher.commit();
	}

	@Override
	public void updateIndex(String repositoryId) {
		addIndex(repositoryId);
	}

	@Override
	public void updateIndex(ContentDetail detail) {
		addIndex(detail);
	}

	@Override
	public void removeIndex(String repositoryId) {
		searcher.removeById(repositoryId);
		searcher.commit();
	}

	@Override
	public void addIndexs(String[] ids) {
		if (ids == null || ids.length < 1)
			return;
		
		for (String id : ids) {
			if (id == null)
				continue;
			ContentDetail detail = getContentDetail(id);
			if (detail == null)
				continue;
	
			searcher.index(toMap(detail));
		}
		searcher.commit();
	}

	@Override
	public PagedResult search(String accountId, String tag, String key,
			String userTag, String friend, int sortType, String language,
			int offset, int limit) {
		return search(accountId, null, tag, key, userTag, friend,
				sortType,
				language, offset, limit);
	}

	@Override
	public PagedResult list(String accountId, String tag, String key,
			String userTag, String friend, int sortType, String language,
			int offset, int limit) {
		return list(accountId, null, tag, key, userTag, friend,
				sortType,
				language, offset, limit);
	}

	@Override
	public PagedResult search(String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit) {
		return this.search(null,accountId, orTags, tag, key, userTag, friend, sortType, language, offset, limit);
	}
	
	@Override
	public PagedResult search(String publicCond, String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit) {
		String filterQuery = getFilterQuery(publicCond, accountId, orTags, tag, userTag, language, friend);
		String sortString = getSortString(sortType, tag);
		QueryResponse r = searcher.searchResult(key, filterQuery, sortString,
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
	public PagedResult list(String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit) {
		String tmpKey = key;
		if (tmpKey == null || tmpKey.equals("")) {
			tmpKey = "*:*";
		}
		return search(accountId, orTags, tag, tmpKey, userTag, friend,
				sortType,
				language, offset, limit);
	}
	
	@Override
	public PagedResult list(String publicCond,String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit) {
		String tmpKey = key;
		if (tmpKey == null || tmpKey.equals("")) {
			tmpKey = "*:*";
		}
		return search(publicCond, accountId, orTags, tag, tmpKey, userTag, friend,
				sortType,
				language, offset, limit);
	}
}
