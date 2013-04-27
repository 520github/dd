package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.dao.TagDao;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.Tag;
import me.twocoffee.entity.Tag.TagId;
import me.twocoffee.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;

	@Override
	public Tag getById(TagId tagId) {
		return tagDao.getById(tagId);
	}

	@Override
	public List<Tag> getTagsByAccount(String accountId) {
		return tagDao.getTagsByAccount(accountId);
	}
	
	@Override
	public void removeById(Tag.TagId tagId) {
		tagDao.removeById(tagId);
	}
	
	@Override
	public void updateTagById(Tag.TagId tagId, String newTag) {
		Tag tag = this.getById(tagId);
		tag.getId().setName(newTag);
		this.removeById(tagId);
		tagDao.save(tag);
	}
	
	@Override
	public List<String> getTopHotTagList(String accountId, int top) {
		return this.getTagList(tagDao.getTopTags(accountId, "counter", top));
	}
	
	@Override
	public List<String> getTopLasteUseTagList(String accountId, int top) {
		return this.getTagList(tagDao.getTopTags(accountId, "date", top));
	}
	
	@Override
	public String getTopHotTagStr(String accountId, int top, String joinStr) {
		return this.getTagStr(this.getTopHotTagList(accountId, top), joinStr);
	}
	
	@Override
	public String getTopLasteUseTagStr(String accountId, int top, String joinStr) {
		return this.getTagStr(this.getTopLasteUseTagList(accountId, top), joinStr);
	}
	
	@Override
	public void save(String accountId, List<String> oldTagList,List<String> newTagList) {
		if(newTagList ==null || newTagList.size() < 1)return ;
		TagId tagid = new TagId();
		tagid.setAccountId(accountId);
		//统计tag被用户标记的次数
		for (int i = 0; i < newTagList.size(); i++) {//循环新传入tag
			String newTag = newTagList.get(i);
			tagid.setName(newTag);
			Tag tag = this.getById(tagid);
			if(tag == null) {
				tag = new Tag();
				tag.setId(tagid);
			}
			if(!this.isOldTag(newTag, oldTagList)) {//新打的tag
				tag.setCounter(tag.getCounter()+1);
				tag.setDate(new Date());
			}
			if(tag.getCounter()==0) {
				tag.setCounter(1);
			}
			this.save(tag);
		}
	}
	
	/**
	 * 判断tag是否是原有的tag
	 * 
	 * @param newTag
	 * @param oldTagList
	 * @return
	 */
	private boolean isOldTag(String newTag,List<String> oldTagList) {
		if(oldTagList == null || oldTagList.size() < 1)return false;
		for (int i = 0; i < oldTagList.size(); i++) {
			String oldTag = oldTagList.get(i);
			if(oldTag == null)continue;
			if(oldTag.equals(newTag)) return true;
		}
		return false;
	}
	
	
	/**
	 * 从tag列表只获取tag名称列表
	 * 
	 * @param tagsList
	 * @return
	 */
	private List<String> getTagList(List<Tag> tagsList) {
		if(tagsList == null || tagsList.size() < 1)return null;
		
		List<String> tagStrList = new ArrayList<String>();
		for (int i = 0; i < tagsList.size(); i++) {
			Tag tag = tagsList.get(i);
			if(tag == null)continue;
			if(tag.getId() == null)continue;
			if(tag.getId().getName() == null 
					|| tag.getId().getName().trim().length() < 1)continue;
			tagStrList.add(tag.getId().getName());
		}
		return tagStrList;
	}
	
	/**
	 * 从tag名称列表拼接成tag名称字符串
	 * 
	 * @param tagsList
	 * @param joinStr
	 * @return
	 */
	private String getTagStr(List<String> tagsList,String joinStr) {
		if(tagsList == null || tagsList.size() < 1)return "";
		if(joinStr == null)joinStr = "";
		String tagStr = "";
		for (int i = 0; i < tagsList.size(); i++) {
			String tag = tagsList.get(i);
			if(tag == null || tag.trim().length() < 1)continue;
			tagStr = tagStr + tag + joinStr;
		}
		if(tagStr.endsWith(joinStr))tagStr = tagStr.substring(0, tagStr.length()-joinStr.length());
		return tagStr;
	}

	@Override
	public void save(Tag tag) {
		tagDao.save(tag);
	}

	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	@Override
	public String getSystemTagName(SystemTagEnum tagEnum) {
		return tagEnum.toString();
	}

}
