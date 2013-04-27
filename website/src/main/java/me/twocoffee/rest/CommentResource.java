/**
 * 
 */
package me.twocoffee.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Comment;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.Counter;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.CommentService;
import me.twocoffee.service.ContentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author xiangjun.yu@babeeta.com
 * 
 */
@Controller
@Path("/service")
public class CommentResource extends AbstractResource{

	private final static Logger logger = LoggerFactory
			.getLogger(ContentResource.class);
	
	private final static int DAYS_INTERVAL_COMMENTS = 1;  
	
	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private ContentService contentService;
	
	@Autowired
	private AccountService accountService;
	
	@Path("/comment/{contentid}")
	@GET
	@Produces("application/json")
	public Response comment(@HeaderParam("Authorization") String token,
			@PathParam("contentid") String contentid,
			@QueryParam("limit") int limit,
			@QueryParam("offset") int offset) {
		
		
//		int PageSize = 10;
//		int PageNum = 0;
//		if (null != limit && !"".equals(limit))
//			PageSize = Integer.parseInt(limit);
//		if (null != offset && !"".equals(offset))
//			PageNum = Integer.parseInt(offset);
		
		int total = commentService.getAllCommentsizeByContentId(contentid);
		List<Comment> comments = commentService.getCommentsByContentIdlimitoffset(contentid,null, limit, offset);
		
		PagedResult<Comment> sr = new PagedResult<Comment>();	//	使用SearchResult包装一下查询结果
//		boolean lastPage = false;
//		if(PageSize*(PageNum+1)>=total){
//			lastPage = true;
//		}
		sr.setResult(comments);
		sr.setLastPage( limit,  offset,  total);
		sr.setTotal(total);
//		sr.setLastPage(limit, offset);
		if(total== 0)	return Response.status(Status.NOT_FOUND).build();
		return Response.ok(sr).build();

	}

	@Path("/getcomment/{contentid}")
	@GET
	@Produces("application/json")
	public Response getcomment(@HeaderParam("Authorization") String token,
			@PathParam("contentid") String contentid,
			@QueryParam("limit") String limit,
			@QueryParam("offset") String offset) {
		
		
		int PageSize = 10;
		int PageNum = 0;
		if (null != limit && !"".equals(limit))
			PageSize = Integer.parseInt(limit);
		if (null != offset && !"".equals(offset))
			PageNum = Integer.parseInt(offset);
		
		int total = commentService.getAllCommentsizeByContentId(contentid);
		List<Comment> comments = commentService.getCommentsByContentId(contentid,null, PageSize, PageNum);
		
		// convertId2loginame(comments);
		PagedResult<Comment> sr = new PagedResult<Comment>();	//	使用SearchResult包装一下查询结果
		boolean lastPage = false;
		if(PageSize*(PageNum+1)>=total){
			lastPage = true;
		}
		sr.setResult(comments);
		sr.setLastPage(lastPage);
		sr.setTotal(total);
//		sr.setLastPage(limit, offset);
		
		return Response.ok(sr).build();
		
	}
	
	/**
	 * @param token
	 * @param contentid
	 * @param comment
	 * @param score
	 * @return
	 * 发表评分评论的逻辑
	 */
	@Path("/comment/{contentid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post_comment(@HeaderParam("Authorization") String token,
			@PathParam("contentid") String contentid,
			Comment comment) {
		
		
		boolean isScored = true;	//	是否有评分
		boolean isCommentd = true;	//	是否有评论
		int i_score = comment.getScore();

		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
		
		if(i_score == 0){	//	0表示没有给评分
			isScored = false;			
		}
		
		if("".equals(comment.getText())){
			isCommentd = false;			
		}
		
		
		/**
		 * 评分次数是否需要加1 
		 * 若是修改评分 则评分次数不加1
		 * 若是新的评分 则评分次数需要加1 
		 */
		int vote_inc = 0;
		
		/**
		 * 评论次数是否需要加1 
		 * 若是修改评论 则评论次数不加1
		 * 若是新的评论 则评论次数需要加1 
		 */
		int comment_inc = 0;
		int score_inc = 0;
		Comment old_comment = commentService.getByAccountIdAndContentId(
				AccountIdOrGuestId, contentid);
		if(null == old_comment){	//	如果是新的评分或者评论
			if (isScored) {	//	且有新评分
				vote_inc = 1;
				score_inc = i_score;
				comment.setLastScore(new Date());
			}
			if (isCommentd) {	//	且有新评论
				comment_inc = 1;
				comment.setLastComment(new Date());
			}
		}else{
			
			Date date = old_comment.getLastScore();	//
			// compare current time with last score time
			
			if (null!=date) {
				Date currentdate = new Date();
				long time1 = date.getTime();
				long time2 = currentdate.getTime();
				if ((time2 - time1) / (24 * 60 * 60 * 1000) < DAYS_INTERVAL_COMMENTS) {
					comment.setText("nomoreoneday");
					return Response.ok(comment).build();
				}
			}
			comment.setId(old_comment.getId());
			if (isScored) {	//	且有新评分
				if (old_comment.getScore()==0) {
					score_inc = i_score;
					vote_inc = 1;
				}else{
					score_inc = i_score - old_comment.getScore(); //	如果是修改的评论 则加分应该是本次评分与前次评分的差
				}
				comment.setLastScore(new Date());
			}
			
			if (isCommentd) {	//	且有新评论
				if ("".equals(old_comment.getText())) {
					comment_inc = 1;
				}
				else{
					comment_inc = 0;
				}
				comment.setLastComment(new Date());
			}else{
				comment_inc = 0;
			}
		}
		comment.setAccountId(AccountIdOrGuestId);
		comment.setContentId(contentid);
			
		// 更新这篇文章的计数器中评论次数及总分
		Content content = contentService.getById(contentid);
		int vote;
		int i_comment;
		int totalScore;
		if(null == content)	return Response.status(Status.NOT_FOUND).build();
		Counter counter = null;
		if(null == content.getCounter()){
			vote = 0;
			i_comment = 0;
			totalScore = 0;
			counter = new Counter();
		}
		else{
			counter = content.getCounter();
			vote = content.getCounter().getComment();
			i_comment = content.getCounter().getComment();
			totalScore = counter.getTotalScore();
		}
		
		vote+=vote_inc;
		i_comment+=comment_inc;
		counter.setVote(vote);
		counter.setComment(i_comment);
		
		totalScore = totalScore + score_inc;
		counter.setTotalScore(totalScore);
		content.setCounter(counter);	//    多多服务端	    DDS-196    "评论图标"的右侧的"评论计数"永远为0
		commentService.save(comment);
		contentService.save(content);		
		
		return Response.ok(comment).build();
		
	}

	private void convertId2loginame (List<Comment> comments){
		
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			String accountId = comment.getAccountId();
			String accountName = accountService.getById(accountId).getAccountName();
			comment.setAccountId(accountName);			
		}
	}
	
}

