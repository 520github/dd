package me.twocoffee.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import me.twocoffee.common.article.ArticleExtractor;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.ImagePayload;
import me.twocoffee.rest.entity.ContentTag;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.fetch.ArticleSummaryService;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.fetch.HtmlArticleExtractionService;
import me.twocoffee.service.fetch.HtmlAttachmentService;
import me.twocoffee.service.fetch.HtmlWithoutArticleException;
import me.twocoffee.service.fetch.ProductWhiteListService;
import me.twocoffee.service.fetch.VideoFetchService;
import me.twocoffee.service.rpc.FriendRpcService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/content/archive")
public class ArchiveResource {

	private final static Logger logger = LoggerFactory
			.getLogger(ArchiveResource.class);

	private static String decodeUrl(String url) {
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return url;
	}

	@Autowired
	private ArticleSummaryService articleSummaryService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private FetchService fetchService;

	@Autowired
	private FriendRpcService friendRpcService;

	@Autowired
	private HtmlArticleExtractionService htmlArticleExtractionService;

	@Autowired
	private HtmlAttachmentService htmlAttachmentService;

	@Autowired
	private ProductWhiteListService productWhiteListService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private VideoFetchService videoFetchService;

	private void processHtmlArticle(String url, Content content) {
		String resource = null;
		try {
			resource = fetchService.getStringResource(url);
			if (resource != null) {
				ArticleExtractor.Article article = htmlArticleExtractionService
						.getArticle(resource);
				content.setTitle(article.getTitle());
				List<String> images = htmlAttachmentService
						.getImages(url, article.getContent());

				List<HtmlPayload.Attachment> attachments = new ArrayList<HtmlPayload.Attachment>();
				for (String image : images) {
					HtmlPayload.Attachment a = new HtmlPayload.Attachment();
					a.setOrgUrl(image);
					a.setSize(article.getContent() == null ? 0l : article
							.getContent().getBytes().length);
					attachments.add(a);
				}
				HtmlPayload htmlPayload = new HtmlPayload();
				htmlPayload.setContent(article.getContent());
				htmlPayload.setAttachment(attachments);
				content.setHtmlPayload(htmlPayload);
				String summary = articleSummaryService.getSummary(article
						.getContent());
				content.setSummary(summary);
			} else {
				content.setUrl(url);
				content.setContentType(ContentType.Web);
			}
		} catch (HtmlWithoutArticleException e) {
			logger.info("Html url:{} has no article.", url);
			content.setUrl(url);
			content.setContentType(ContentType.Web);
		}
	}

	private JsonObject reduceContentForImage(final Content content) {
		@SuppressWarnings("unused")
		JsonObject o = new JsonObject() {
			public String getArchiveUrl() {
				return content.getImagePayload().getUrl();
			}

			public String getContentType() {
				return content.getContentType().name();
			}

			public String getId() {
				return content.getId();
			}

			public String getUrl() {
				return content.getUrl();
			}
		};
		return o;
	}

	private JsonObject reduceContentForProduct(final Content content) {
		@SuppressWarnings("unused")
		JsonObject o = new JsonObject() {

			public String getContentType() {
				return content.getContentType().name();
			}

			public String getId() {
				return content.getId();
			}

			public JsonObject getProduct() {
				if (content.getProductPayload() == null) {
					return null;
				}
				JsonObject product = new JsonObject() {
					public int getAmount() {
						return content.getProductPayload().getAmount();
					}

					public String getDescription() {
						return content.getProductPayload().getDescription();
					}

					public String getName() {
						return content.getProductPayload().getName();
					}

					public String getPicture() {
						return content.getProductPayload().getPicture();
					}

					public String getPrice() {
						return content.getProductPayload().getPrice();
					}
				};
				return product;
			}

			public String getTitle() {
				return content.getTitle();
			}

			public String getUrl() {
				return content.getUrl();
			}
		};
		return o;
	}

	private JsonObject reduceContentForWeb(final Content content) {
		@SuppressWarnings("unused")
		JsonObject o = new JsonObject() {
			public List<String> getAttachment() {
				List<String> list = null;
				if (content.getHtmlPayload() != null) {
					List<Attachment> attachments = content.getHtmlPayload()
							.getAttachment();
					if (attachments != null) {
						list = new ArrayList<String>();
						for (Attachment a : attachments) {
							list.add(a.getArchiveUrl() == null ? a.getOrgUrl()
									: a.getArchiveUrl());
						}
					}
				}
				return list;
			}

			public String getContent() {
				return content.getHtmlPayload() == null ? null : content
						.getHtmlPayload().getContent();
			}

			public String getContentType() {
				return content.getContentType().name();
			}

			public String getId() {
				return content.getId();
			}

			public String getSummary() {
				return content.getSummary();
			}

			public String getTitle() {
				return content.getTitle();
			}

			public String getUrl() {
				return content.getUrl();
			}
		};
		return o;
	}

	@Path("/{url}")
	@GET
	@Produces({ "application/meta-web+json", "application/meta-product+json" })
	public Response getContent(@PathParam("url") String url) {
		url = decodeUrl(url);
		String model = productWhiteListService.getModelName(url);
		ContentType contentType = ContentType.Web;
		if (model != null) {
			contentType = ContentType.Product;
		} else if (videoFetchService.isVideo(url)) {
			contentType = ContentType.Video;
		}
		Content content = contentService.getByUrlAndType(url, contentType);
		if (content == null) {
			content = new Content();
			content.setUrl(url);
			if (contentType.compareTo(ContentType.Product) == 0) {
				content = productWhiteListService.getProductDetail(url, model);
			} else if (contentType.compareTo(ContentType.Video) == 0) {
				content = videoFetchService.getVideoContent(url);
			} else {
				content.setContentType(ContentType.Web);
				processHtmlArticle(url, content);
			}
			if (content.getContentType().equals(ContentType.Web)
					&& content.getHtmlPayload() == null) {
				// 没有抓取到网页，返回空
				return Response.ok().build();
			}
			contentService.save(content);
			if (content.getHtmlPayload() != null
					&& content.getHtmlPayload().getAttachment() != null
					&& content.getHtmlPayload().getAttachment().size() > 0) {
				fetchService.addToFetch(content);// 加入到抓取队列
			}
		}
		JsonObject ret = null;
		if (content.getContentType().compareTo(ContentType.Product) == 0) {
			ret = reduceContentForProduct(content);
		} else {
			ret = reduceContentForWeb(content);
		}
		return Response.ok(ret).build();
	}

	@Path("/zn")
	@GET
	public Response getContentd() {
		// String url = "http://biz.cn.yahoo.com/ypen/20120608/1097730.html";
		String url = "http://bbs.8264.com/thread-665567-1-1.html";
		Content content = new Content();
		content.setUrl(url);
		content.setContentType(ContentType.Web);

		processHtmlArticle(url, content);

		return Response.ok().build();
	}

	@Path("/{contentId}/tags/hot")
	@GET
	@Produces("application/json")
	public Response getContentHotTag(@PathParam("contentId") String contentId) {
		List<ContentTag> hotTagList = new ArrayList<ContentTag>();
		Map<String, String> topTagMap = repositoryService
				.getTopTagMapByContentId(contentId, 5);
		if (topTagMap != null && topTagMap.size() > 0) {
			Iterator<String> it = topTagMap.keySet().iterator();
			while (it.hasNext()) {
				String tag = it.next();
				String weight = topTagMap.get(tag);
				ContentTag ctag = new ContentTag();
				ctag.setTag(tag);
				ctag.setWeight(weight);
				hotTagList.add(ctag);
			}
		}
		return Response.ok(hotTagList).build();
	}

	public HtmlArticleExtractionService getHtmlArticleExtractionService() {
		return htmlArticleExtractionService;
	}

	@Path("/{url}")
	@GET
	@Produces({ "application/meta-image+json" })
	public Response getImageContent(@PathParam("url") String url,
			@HeaderParam("Referer") String referer) {

		url = htmlAttachmentService.getAbsoluteURL(referer, decodeUrl(url));
		Content content = contentService.getByUrl(url);
		if (content == null) {
			content = new Content();
			String archiveUrl = url;
			if (archiveUrl != null && archiveUrl.indexOf("mduoduo") == -1) {
				archiveUrl = fetchService.getResource(url, referer);
			}
			content.setContentType(ContentType.Image);
			content.setUrl(url);
			ImagePayload imagePayload = new ImagePayload();
			imagePayload.setUrl(archiveUrl);
			content.setImagePayload(imagePayload);
			contentService.save(content);
		}
		return Response.ok(reduceContentForImage(content)).build();
	}
}
