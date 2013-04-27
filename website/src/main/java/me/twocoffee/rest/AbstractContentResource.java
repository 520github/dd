package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.AllTagContentVisitor;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.common.util.FileImageUtil;
import me.twocoffee.entity.Comment;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.Content.Counter;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.ResponseContent.Web;
import me.twocoffee.rest.utils.UserAgentUtils;
import me.twocoffee.rest.utils.UserAgentUtils.ClientInfo;
import me.twocoffee.rest.utils.UserAgentUtils.ClientType;
import me.twocoffee.service.AcknowledgmentService;
import me.twocoffee.service.CommentService;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.entity.ContentDetail;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.VelocityEngine;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class AbstractContentResource extends AbstractResource {
	private final static Logger logger = LoggerFactory
			.getLogger(AbstractContentResource.class);
	@Autowired
	private PrivateMessageService privateMessageService;
	@Autowired
	private AcknowledgmentService acknowledgmentService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private VelocityEngine velocityEngine;

	protected Map<String, String> versions = new HashMap<String, String>();

	protected Map<String, String> downloadUrls = new HashMap<String, String>();

	protected ResponseContent getResponseContent(ContentDetail contentDetail,
				String userAgent, String isClient) {

		ClientInfo clientInfo = UserAgentUtils.getClientInfo(userAgent);
		if (contentDetail.getType() == SystemTagEnum.Type_File) {
			logger.debug("userAgent [" + userAgent + "]");
			if ((clientInfo.getClientType() == ClientType.Duoduo_ios
					|| clientInfo.getClientType() == ClientType.Duoduo_android)
					&& isOldVersion(clientInfo)) {

				logger.debug("find old version [" + clientInfo.getVersion()
						+ "] ");
				ResponseContent responseContent = new ResponseContent();
				responseContent = this.setCommonResponseContent(responseContent, contentDetail);
				responseContent.setUrl("http://" + SystemConstant.domainName+ "/about/tools");
				if (clientInfo.getClientType() == ClientType.Duoduo_ios) {
					responseContent.setUrl(this.downloadUrls.get(clientInfo.getClientType().toString()));
				}
				responseContent.setType(SystemTagEnum.Type_Web);
				Web web = new Web();
				web.setContent(getWebContent(this.downloadUrls.get(clientInfo.getClientType().toString())));

				Attachment atta = new Attachment();
				atta.setArchiveUrl(FileImageUtil.getUnkownFileImage());
				List<Attachment> attas = new ArrayList<Attachment>();
				attas.add(atta);
				web.setAttachment_trans(attas);
				responseContent.setWeb(web);

				return responseContent;
			}
		}
		ResponseContent responseContent = new ResponseContent();
		responseContent = this.setCommonResponseContent(responseContent,contentDetail);
		responseContent = this.setClient(clientInfo, responseContent, contentDetail);
		return responseContent;
	}
	
	protected ResponseContent setClient(ClientInfo clientInfo,ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		if(UserAgentUtils.isClient(clientInfo)) {
			responseContent.setSummary(StringEscapeUtils
					.unescapeHtml(getClientSummary(contentDetail
							.getContent()
							.getSummary())));
		}
		if(clientInfo.getClientType() == ClientType.Duoduo_ios 
				&& contentDetail.getType() == SystemTagEnum.Type_Product
				&& contentDetail.getSource() == SystemTagEnum.Source_Plugin
				&& !responseContent.getTag().contains(SystemTagEnum.Collect.toString())) {
			responseContent.getTag().add(SystemTagEnum.Collect.toString());
		}
		return responseContent;
	}
	
	protected ResponseContent setCommonContent(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		//content
		responseContent.setId(contentDetail.getContent().getId());
		responseContent.setUrl(contentDetail.getContent().getUrl());
		String title = contentDetail.getContent().getTitle() == null?"无标题":contentDetail.getContent().getTitle();
		responseContent.setTitle(StringEscapeUtils.unescapeHtml(title.trim()));
		responseContent.setSummary(contentDetail.getContent().getSummary());
		responseContent = this.setContent(responseContent, contentDetail);
		
		return responseContent;
	}
	
	protected ResponseContent setContent(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		responseContent.setProduct_trans(contentDetail.getContent().getProductPayload());//商品
		responseContent.setImage_trans(contentDetail.getContent().getImagePayload());//图片
		responseContent.setFile_trans(contentDetail.getContent().getFilePayload());//文件
		responseContent.setVideo_trans(contentDetail.getContent().getVideoPayload());//视频
		Web web = new Web();
		web.setContent(contentDetail.getContent().getHtmlPayload().getContent());
		web.setAttachment_trans(contentDetail.getContent().getHtmlPayload().getAttachment());
		responseContent.setWeb(web);
		
		return responseContent;
	}
	
	protected ResponseContent setCommonRepository(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		//repository
		responseContent.setResponseId(contentDetail.getRepository().getId());
		responseContent.setSource(contentDetail.getSource());
		responseContent.setFolders(contentDetail.getFolders());
		responseContent.setType(contentDetail.getType());
		responseContent.setDate_i18n(contentDetail.getDate_i18n());
		responseContent.setDate(DateUtil.FormatDateUTC(contentDetail.getRepository().getDate()));
		responseContent.setLastModified(DateUtil
				.FormatDateUTC(contentDetail.getRepository().getLastModified()));
		responseContent.setTag(contentDetail.getRepository().getTag());
		if (contentDetail.getType() == SystemTagEnum.Type_Image &&
				!responseContent.getTag().contains(SystemTagEnum.Original_None.toString())) {
			responseContent.getTag().add(SystemTagEnum.Original_None.toString());
		}
		responseContent.setUserTag(contentDetail.getRepository().getUserTag());
		responseContent.setShareLink(contentDetail.getRepository().getShareLink());
		
		return responseContent;
	}
	
	protected ResponseContent setMessageSession(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		List<PrivateMessageSession> chatSession = privateMessageService
		.getSessions(contentDetail
				.getRepository().getAccountId(), contentDetail
				.getRepository().getContentId(), 0, 100);
		responseContent.setChatSession(chatSession);
		
		return responseContent;
	}
	
	protected ResponseContent setComment(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		Comment comment = commentService.getByAccountIdAndContentId(
				contentDetail.getRepository().getAccountId(), contentDetail
						.getRepository().getContentId());
		if (comment != null)responseContent.setComment_trans(comment);
		responseContent.setScore_trans(contentDetail);
		responseContent.setFrom(contentDetail.getFrom());
		
		return responseContent;
	}
	
	protected ResponseContent setAcknowledgemen(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		//添加答谢数
		Counter counter = contentDetail.getContent().getCounter();
		int acknowledgement = acknowledgmentService.totalByAccountIdAndContent(
				contentDetail.getRepository().getAccountId(), contentDetail
						.getRepository().getContentId());
		counter.setAcknowledgment(acknowledgement);
		responseContent.setCounter(counter);
		
		return responseContent;
	}
	
	protected ResponseContent setNeedCommonResponseContent(ResponseContent responseContent,ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		responseContent.setImageUrl(contentDetail.getImageUrl());
		responseContent = this.setCommonContent(responseContent,contentDetail);
		responseContent = this.setCommonRepository(responseContent,contentDetail);
		
		return responseContent;
	}
	
	protected ResponseContent setCommonResponseContent(ResponseContent responseContent, ContentDetail contentDetail) {
		if(responseContent == null) {
			responseContent = new ResponseContent();
		}
		responseContent = this.setNeedCommonResponseContent(responseContent,contentDetail);
		responseContent = this.setAcknowledgemen(responseContent, contentDetail);
		responseContent = this.setComment(responseContent, contentDetail);
		responseContent = this.setMessageSession(responseContent, contentDetail);
		
		return responseContent;
	}

	private boolean isOldVersion(ClientInfo clientInfo) {

		if (clientInfo.getVersion() == null
				|| "".equals(clientInfo.getVersion())) {

			return true;
		}

		try {
			String[] versionParts = clientInfo.getVersion().split("\\.");
			String[] standerParts = this.versions.get(
					clientInfo.getClientType().toString()).split("\\.");

			int length = versionParts.length < standerParts.length ? versionParts.length
					: standerParts.length;

			if (length == 0) {
				return true;
			}

			for (int i = 0; i < length; i++) {

				if (Integer.parseInt(versionParts[i]) < Integer
						.parseInt(standerParts[i])) {

					return true;

				} else if (Integer.parseInt(versionParts[i]) > Integer
						.parseInt(standerParts[i])) {

					return false;
				}
			}
			return false;

		} catch (Exception e) {
			return true;
		}
	}

	private String getWebContent(String url) {
		Map<String, String> model = new HashMap<String, String>();
		model.put("url", url);
		String view = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,
				LocaleContextHolder.getLocale()
						+ "/views/drag/oldversion.vm",
				"utf-8", model);

		view = view.replaceAll("\r\n", "");
		view = view.replaceAll("\n", "");
		return view;
	}

	public String getClientSummary(String document) {
		if (document == null) {
			return "";
		}
		Parser parser = createParser(document);
		parser.reset();
		try {
			AllTagContentVisitor visitor = new AllTagContentVisitor();
			parser.visitAllNodesWith(visitor);
			return visitor.getClientSummary().trim().replaceAll("&nbsp", "");
		} catch (ParserException e) {
			logger.debug(e.getMessage());
		}
		return document.trim().replaceAll("&nbsp", "");
	}

	public Parser createParser(String inputHTML) {
		Lexer mLexer = new Lexer(new Page(inputHTML));
		return new Parser(mLexer, new DefaultParserFeedback(
				DefaultParserFeedback.QUIET));

	}

	public Map<String, String> getVersions() {
		return versions;
	}

	public void setVersions(Map<String, String> versions) {
		this.versions = versions;
	}

	public Map<String, String> getDownloadUrls() {
		return downloadUrls;
	}

	public void setDownloadUrls(Map<String, String> downloadUrls) {
		this.downloadUrls = downloadUrls;
	}
	
}
