/**
 * 
 */
package me.twocoffee.service.impl;

import java.util.List;

import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.CommentDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Comment;
import me.twocoffee.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiangjun.yu@babeeta.com
 * 
 */
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentDao commentDao;

	@Autowired
	AccountDao accounttDao;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#deleteComment(me.twocoffee.entity
	 * .Comment)
	 */
	@Override
	public void deleteComment(Comment comment) {
		commentDao.deleteComment(comment);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#getAllCommentsByContentId(java.lang
	 * .String)
	 */
	@Override
	public List<Comment> getAllCommentsByContentId(String contentId) {
		return commentDao.getAllCommentsByContentId(contentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#getAllCommentsizeByContentId(java
	 * .lang.String)
	 */
	@Override
	public int getAllCommentsizeByContentId(String contentId) {
		return commentDao.getAllCommentsizeByContentId(contentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#getByCommentId(me.twocoffee.entity
	 * .Comment.CommentId)
	 */
	@Override
	public Comment getByCommentId(String commentId) {
		return commentDao.getByCommentId(commentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#getCommentsByContentId(java.lang.
	 * String, int, int)
	 */
	@Override
	public List<Comment> getCommentsByContentId(String contentId,String orderby, int PageSize,
			int PageNum) {
		List<Comment> comments = commentDao.getCommentsByContentId(contentId, orderby,PageSize, PageNum);
		convertId2loginame(comments);
		return comments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#save(me.twocoffee.entity.Comment)
	 */
	@Override
	public void save(Comment comment) {
		commentDao.save(comment);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.CommentService#updateComment(me.twocoffee.entity
	 * .Comment)
	 */
	@Override
	public void updateComment(Comment comment) {
		commentDao.updateComment(comment);

	}

	@Override
	public void deleteComment(String contentId, String accountId) {
		commentDao.deleteComment(contentId, accountId);

	}

	@Override
	public Comment getByAccountIdAndContentId(String accountId, String contentId) {
		return commentDao.getByAccountIdAndContentId(accountId, contentId);
	}

	private void convertId2loginame (List<Comment> comments){
		if(comments == null)return ;
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			if(comment == null)continue;
			String accountId = comment.getAccountId();
			//	TODO YUXJ	
			//	当account被删掉的时候
			Account account = accounttDao.getById(accountId);
			if(account !=null) {
				String accountName = accounttDao.getById(accountId).getAccountName();
				comment.setAccountId(accountName);
			}			
		}
	}

	@Override
	public List<Comment> getCommentsByContentIdlimitoffset(String contentId,
			String orderby, int limit, int offset) {

		List<Comment> comments = commentDao.getCommentsByContentId(contentId, orderby,limit, offset);
		convertId2loginame(comments);
		return comments;
	
	}
	
}
