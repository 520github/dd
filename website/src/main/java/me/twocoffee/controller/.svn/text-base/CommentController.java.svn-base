/**
 * 
 */
package me.twocoffee.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Comment;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.CommentService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiangjun.yu@babeeta.com
 * 
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private AuthTokenService authTokenService;

	@RequestMapping("/detail")
	public String detail(String contentId, int PageSize, int PageNum, Model model) {
//		PageSize = 10;
//		PageNum = 0;
		List<Comment> list =commentService.getCommentsByContentId(contentId,null, PageSize, PageNum);
		int totalNumber = commentService.getAllCommentsizeByContentId(contentId);
		boolean lastPage = false;
		if(PageSize*(PageNum+1)>=totalNumber){
			lastPage = true;
		}
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("totalNumber", totalNumber);
		model.addAttribute("contentId", contentId);
		model.addAttribute("comments", list);
		return "content/personal/comment_flow";
	}
	
	/**
	 * @param request
	 * @param contentId
	 *            内容的id
	 * @param score
	 *            给内容打的分
	 * @param remark
	 *            具体的评论内容
	 * @param model
	 * @return 发表一个评论
	 */
	@RequestMapping("/comment")
	@ResponseBody
	public String add(HttpServletRequest request, String contentId, int score,
			String remark, Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

		Comment comment = new Comment();
		comment.setAccountId(accountId);
		comment.setContentId(contentId);
		comment.setScore(score);
		comment.setText(remark);
		comment.setLastComment(new Date());
		commentService.save(comment);

		return "1";
	}

	/**
	 * @param request
	 * @param contentId
	 * @param model
	 * @return 删除评论
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String remove(HttpServletRequest request, String contentId,
			Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);

		commentService.deleteComment(contentId, accountId);

		return "1";
	}

}
