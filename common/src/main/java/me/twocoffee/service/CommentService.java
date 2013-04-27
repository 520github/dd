/**
 * 
 */
package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Comment;

/**
 * @author xiangjun.yu@babeeta.com
 * 
 */
public interface CommentService {

	/**
	 * @param commentid
	 * @return 得到评论
	 */
	Comment getByCommentId(String commentId);

	/**
	 * @param contentId
	 * @return 取某个content的所有评论
	 */
	List<Comment> getAllCommentsByContentId(String contentId);

	/**
	 * @param contentId
	 * @return 某个content的所有评论条数
	 */
	int getAllCommentsizeByContentId(String contentId);

	/**
	 * @param contentId
	 * @param PageSize
	 * @param PageNum
	 * @return 取某个content的第PageNum页评论
	 */
	List<Comment> getCommentsByContentId(String contentId,String orderby, int PageSize,
			int PageNum);

	/**
	 * @param contentId
	 * @param limit
	 * @param offset
	 * @return 取某个content的第PageNum页评论
	 */
	List<Comment> getCommentsByContentIdlimitoffset(String contentId,String orderby, int limit,
			int offset);
	
	/**
	 * @param comment
	 *            保存评论
	 */
	void save(Comment comment);

	/**
	 * @param comment
	 *            修改评论
	 */
	void updateComment(Comment comment);

	/**
	 * @param comment
	 *            删除评论
	 */
	void deleteComment(Comment comment);

	/**
	 * @param contentId
	 *            内容id
	 * @param accountId
	 *            账户id 删除评论
	 */
	void deleteComment(String contentId, String accountId);

	/**
	 * 得到某人对谋篇内容的评论
	 * 
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	Comment getByAccountIdAndContentId(String accountId, String contentId);

}
