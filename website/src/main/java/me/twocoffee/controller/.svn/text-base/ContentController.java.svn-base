package me.twocoffee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.entity.Comment;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.CommentService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class ContentController extends BaseController {
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private ContentSearcher contentSearcher;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("personal/favorite")
	public String getFavorite(HttpServletRequest request, Model model) {
		model.addAttribute("token", this.getToken(request));
		model.addAttribute("menuType","favorite");
		return "content/personal/favorite";
	}
	
	@RequestMapping("personal/laterlook")
	public String getLaterLook(HttpServletRequest request, Model model) {
		model.addAttribute("token", this.getToken(request));
		model.addAttribute("menuType","laterlook");
		return "content/personal/laterlook";
	}
	
	
	@RequestMapping("personal/item") 
	public String getItem(String id,HttpServletRequest request, Model model) {
		
		// 取出本篇文章的头6条评论
		
		int total = 0; 
		int PageSize = 6; 
		int PageNum = 0; 
		List<Comment> comment_list = null;
		
		
		ContentDetail result = repositoryService.findContentDetailById(id);
		if(result == null){
			result = new ContentDetail();
			//TODO go to not found page
		}
		
		total = commentService.getAllCommentsizeByContentId(result.getContent().getId());
//		comment_list = commentService.getAllCommentsByContentId(result.getContent().getId());
		comment_list = commentService.getCommentsByContentId(result.getContent().getId(),null, PageSize, PageNum);
		
		//更新系统标签,删除系统未读tag
		Repository repository = result.getRepository();
		repository.setTag(repositoryService.getTag(SystemTagEnum.Read.toString(), repository.getTag(), null));
		repositoryService.save(repository);
		
		//增加阅读次数
		Content content = result.getContent();
		content = content.setVisit(1);
		contentService.save(content);
		
		//更新索引
		contentSearcher.updateIndex(id);
		
		model.addAttribute("total", total);
		model.addAttribute("comment_list", comment_list);
		model.addAttribute("item", result);
		model.addAttribute("token", this.getToken(request));
		return "content/personal/item";
	}
	
	@RequestMapping("personal/url") 
	public String forwardUrl(String url,HttpServletRequest request, Model model) {
		model.addAttribute("url", url);
		return "content/personal/url";
	}
	
	@RequestMapping("personal/screw") 
	public String screw(HttpServletRequest request, Model model) {
		return "content/personal/screw";
	}
	
	@RequestMapping("personal/screw_data") 
	public String screw_data(HttpServletRequest request, Model model) {
		return "content/personal/screw_data";
	}
	
	@RequestMapping("personal/scrollpagination") 
	public String scrollpagination(HttpServletRequest request, Model model) {
		model.addAttribute("token", this.getToken(request));
		return "content/personal/scrollpagination";
	}
	
	@RequestMapping("personal/favorite_data") 
	public String scrollpagination_data(String refresh,HttpServletRequest request, Model model) {
		System.out.println("refresh:" + refresh);
		model.addAttribute("refresh", refresh);
		model.addAttribute("token", this.getToken(request));
		return "content/personal/favorite_data";
	}
	
	private String getToken(HttpServletRequest request) {
		return AuthUtil.getWebTokenFromCookie(request);
	}
}
