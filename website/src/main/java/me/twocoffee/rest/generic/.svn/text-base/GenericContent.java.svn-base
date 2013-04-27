package me.twocoffee.rest.generic;

import java.util.List;

import me.twocoffee.entity.Comment;
import me.twocoffee.entity.Content;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 通用的一个收藏内容的临时类
 * 
 * 1、用来映射body中对象
 * 
 * 2、也可以用来封装发送给客户端的对象
 * 
 * @author chongf
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GenericContent extends Content {
	
	/** 系统 tag 列表 */
	private List<String> tag;
	
	/** 用户 tag 列表 */
	private List<String> userTag;
	
	//评论
	private Comment comment;
	
	//好友分享
	private List<String> share;
	
	//左侧菜单类型,如：收藏、稍后看、已读
	private String menuType;
	
	//是否是新建的关系
	private boolean isNewRepository = false;
	

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}

	public List<String> getUserTag() {
		return userTag;
	}

	public void setUserTag(List<String> userTag) {
		this.userTag = userTag;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public List<String> getShare() {
		return share;
	}

	public void setShare(List<String> share) {
		this.share = share;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public boolean isNewRepository() {
		return isNewRepository;
	}

	public void setNewRepository(boolean isNewRepository) {
		this.isNewRepository = isNewRepository;
	}
}