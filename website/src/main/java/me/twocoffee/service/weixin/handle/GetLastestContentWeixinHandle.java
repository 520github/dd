package me.twocoffee.service.weixin.handle;

import java.util.ArrayList;
import java.util.List;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.weixin.WeiXinPublicArticle;
import me.twocoffee.service.weixin.WeiXinPublicConstant;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;
import me.twocoffee.service.weixin.WeiXinPublicResponseEntity;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("getLastestContentWeixinHandle")
@Scope("prototype")
public class GetLastestContentWeixinHandle extends AbstractWeixinHandle {
	@Autowired
	private ContentSearcher contentSearcher;
	@Autowired
	private RepositoryService repositoryService;
	private static int offset = 0;
	private static int limit = 4;
	
	
	@Override
	public WeiXinPublicResponseEntity weixinHandle(
			WeiXinPublicRequestEntity request,
			WeiXinPublicResponseEntity response) {
		super.weixinHandle(request, response);
		List<ContentDetail> contentList = this.getLastestContent();
		this.setContent2Response(contentList);
		return response;
	}
	
	private List<ContentDetail> getLastestContent() {
		String tag = null;
		String key =  null;
		String userTag = null;
		String sortType = null;
		String language = "";
		this.setAccountId();
		PagedResult sr = contentSearcher.list(
				this.accountId, tag, key, userTag, contentSearcher.getSortType(sortType), language, offset, limit);
		if(sr == null) return null;
		return repositoryService.findContentDetailsById(sr.getResult());
	}
	
	private void setContent2Response(List<ContentDetail> contentList) {
		if(contentList == null || contentList.size() < 1) {
			response.setContent(WeiXinPublicConstant.EMPTY_CONTENT_TIP);
			response.setWeiXinPublicArticleList(null);
			return ;
		}
		List<WeiXinPublicArticle> articleList = new ArrayList<WeiXinPublicArticle>();//
		if(response.getWeiXinPublicArticleList() != null) {
			//articleList.addAll(response.getWeiXinPublicArticleList());
		}
		int hasImageIndex = -1;
		for (int i = 0; i < contentList.size(); i++) {
			ContentDetail detail = contentList.get(i);
			if(detail == null || detail.getRepository() == null)continue;
			WeiXinPublicArticle article = new WeiXinPublicArticle();
			String url = WeiXinPublicConstant.WEIXIN_CONTENT_ITEM_URL
			            .replaceAll(WeiXinPublicConstant.WEIXIN_TEMPLATE_ID, this.weixinId)
			            .replaceAll(WeiXinPublicConstant.WEIXIN_TEMPLATE_REPOSITORY_ID, detail.getRepository().getId());
			String title = detail.getContent().getTitle() == null?"无标题":detail.getContent().getTitle();
			article.setTitle(title);
			String summary = detail.getContent().getSummary() == null?"":detail.getContent().getSummary();
			if(summary.length() > WeiXinPublicConstant.WEIXIN_CONTENT_MAX_LENGTH)
				summary = summary.substring(0, WeiXinPublicConstant.WEIXIN_CONTENT_MAX_LENGTH);
			article.setDescription(summary);
			article.setUrl(url);
			String imgUrl = StringUtils.trimToEmpty(detail.getImageUrl());
			if(hasImageIndex ==-1 && StringUtils.isNotBlank(imgUrl)) {
				hasImageIndex = i;
			}
			article.setImageURL(imgUrl);
			articleList.add(article);
		}
		if(hasImageIndex > 0) {
			articleList.add(0, articleList.get(hasImageIndex));
			articleList.remove(hasImageIndex+1);
		}
		if(contentList.size() >= limit) {
			WeiXinPublicArticle article = new WeiXinPublicArticle();
			String url = WeiXinPublicConstant.WEIXIN_CONTENT_LIST_URL
                         .replaceAll(WeiXinPublicConstant.WEIXIN_TEMPLATE_ID, this.weixinId);
			article.setTitle(WeiXinPublicConstant.MORE_CONTENT_TIP);
			article.setUrl(url);
			article.setImageURL("");
			article.setDescription(WeiXinPublicConstant.MORE_CONTENT_TIP);
			articleList.add(article);
		}
		response.setMessageType(WeiXinPublicRequestEntity.MessageType.news.toString());
		response.setWeiXinPublicArticleList(articleList);
		response.setContent("");
		response.setFunctionFlag(1);
	}
	
}
