package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.CommentDao;
import me.twocoffee.entity.Comment;

import org.bson.types.ObjectId;

@org.springframework.stereotype.Repository
public class CommentDaoImpl extends BaseDao<Comment> implements CommentDao {

	@Override
	public void deleteComment(Comment comment) {
		this.dataStore.delete(comment);

	}

	@Override
	public List<Comment> getAllCommentsByContentId(String contentId) {
		return this.createQuery().filter("contentId", contentId).asList();
	}

	@Override
	public int getAllCommentsizeByContentId(String contentId) {
		return (int) this.createQuery().filter("contentId", contentId)
				.countAll();
	}

	@Override
	public Comment getByCommentId(String commentId) {
		return this.createQuery().filter("id", commentId).get();
	}

	@Override
	public List<Comment> getCommentsByContentId(String contentId,String orderby, int PageSize,
			int PageNum) {
		if(null==orderby||"".equals(orderby)){
			return this.createQuery().filter("contentId", contentId).offset(
					PageSize * PageNum).limit(PageSize).asList();
		}
		else{
			return this.createQuery().filter("contentId", contentId).order(orderby).offset(
					PageSize * PageNum).limit(PageSize).asList();
		}
	}

	@Override
	public void save(Comment comment) {

		if (comment != null && (comment.getId() == null ||
				comment.getId().equals(""))) {
			comment.setId(new
					ObjectId().toString());
		}
		super.save(comment);

	}

	@Override
	public void updateComment(Comment comment) {
		super.save(comment);

	}

	@Override
	public void deleteComment(String contentId, String accountId) {
		this.dataStore.delete(this.createQuery().filter("contentId", contentId)
				.filter("accountId", accountId));

	}

	@Override
	public Comment getByAccountIdAndContentId(String accountId, String contentId) {
		return createQuery().filter("accountId =", accountId)
				.filter("contentId =", contentId)
				.get();
	}

	@Override
	public List<Comment> getCommentsByContentIdllimitoffset(String contentId,
			String orderby, int limit, int offset) {
		if(null==orderby||"".equals(orderby)){
			return this.createQuery().filter("contentId", contentId).offset(
					offset).limit(limit).asList();
		}
		else{
			return this.createQuery().filter("contentId", contentId).order(orderby).offset(
					offset).limit(limit).asList();
		}
	}

}
