package me.twocoffee.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.HtmlClearVisitor;
import me.twocoffee.common.SpringContext;
import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.BinarySearch;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.entity.Document;
import me.twocoffee.entity.FriendSharedLog;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.ProductPayload;
import me.twocoffee.entity.PublicRepository;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.StatisticTag;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.Tag;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.AccountId;
import me.twocoffee.rest.entity.FriendShareInfo;
import me.twocoffee.rest.entity.ThirdParty;
import me.twocoffee.rest.entity.UserTag;
import me.twocoffee.rest.entity.UserTag.MessageType;
import me.twocoffee.rest.generic.GenericContent;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.rest.utils.UserAgentUtils;
import me.twocoffee.rest.utils.UserAgentUtils.ClientInfo;
import me.twocoffee.rest.utils.UserAgentUtils.ClientType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.DocumentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.fetch.ArticleSummaryService;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.fetch.HtmlAttachmentService;
import me.twocoffee.service.rpc.RpcConfiguration;
import me.twocoffee.service.thirdparty.ThirdpartyService;
import me.twocoffee.service.thirdparty.impl.ThirdPartyServiceImpl.MessageOperateType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Controller
@Path("/service/content")
public class ContentResource extends AbstractContentResource{
    private final static Logger logger = LoggerFactory
	    .getLogger(ContentResource.class);
    private final static int LIMIT = 100000;
    
    private static String mappingObject(Object o) {
	ObjectMapper mapper = new ObjectMapper();
	String ret = null;
	try {
	    ret = mapper.writeValueAsString(o);
	} catch (IOException e) {
	    logger.error(e.getMessage());
	}
	return ret;
    }

    @Autowired
    private InviteService inviteService;

    @Autowired
    private ThirdpartyService thirdPartyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ArticleSummaryService articleSummaryService;

    @Autowired
    private ContentSearcher contentSearcher;

    @Autowired
    private ContentService contentService;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private HtmlAttachmentService htmlAttachmentService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private DocumentService documentService;

    /**
     * @param uid
     *            微博id
     * @return 若微博id为uid是2coffee用户则返回其accountId 否则返回"0"
     */
    // TODO SNS 返回0还是返回uid
    private String get2CoffeeId(String uid) {
	String uid_accountId = "0";
	Account account = thirdPartyService.getByThirdParty(
		ThirdPartyType.Weibo, uid);
	if (null != account) {
	    uid_accountId = account.getId();

	}
	return uid_accountId;
    }

    private void getAndSaveContent(Content content) {
	// 从内容中提取摘要
	content.setSummary(articleSummaryService.getSummary(
		content.getHtmlPayload().getContent()));
	// 从内容中提取图片列表
	List<String> images = htmlAttachmentService.clearAndRecieveImageUrl(
		content.getUrl(),
		content);

	if (images != null && images.size() > 0) {
	    // 第一张图片本地服务存储的路径
	    // String firstImage = fetchService.getResource(images.get(0));
	    // 设置图片url
	    content.setHtmlPayload(content.getHtmlPayload()
		    .setAttachment(
			    images, ""));

	}
	contentService.save(content);
	// 加入抓取队列，等待抓
	fetchService.addToFetch(content);
    }

    private List<String> getFriendList(AccountId[] accounts) {
	List<String> fs = new ArrayList<String>();
	for (AccountId aid : accounts) {
	    fs.add(aid.getAccountId());
	}
	return fs;
    }

    /**
     * 将内容渲染成完整的HTML，只针对客户端调
     * 
     * @param responseContent
     * @return
     */
    private String getHtmlContent(ResponseContent responseContent) {
	String html = "";
	String filePath = "vm/clientItem.vm";
	try {
	    VelocityEngine velocityEngine = null;
	    VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
	    velocityEngineFactoryBean.setResourceLoaderPath("/");
	    velocityEngine = velocityEngineFactoryBean.createVelocityEngine();

	    Map<String, Object> content = new HashMap<String, Object>();
	    content.put("responseContent", responseContent);
	    html = VelocityEngineUtils.mergeTemplateIntoString(
		    velocityEngine,
		    filePath,
		    "utf-8", content);
	    logger.debug("{}", html);
	} catch (ResourceNotFoundException e) {
	    html = "ResourceNotFoundException";
	    logger.error("ResourceFile{} NotFound", filePath, e);
	} catch (Exception e) {
	    html = e.getMessage();
	    logger.error("read ResourceFile{} exception", filePath, e);
	}
	return html;
    }

    private String getReplaceUrl(String url) {
	if (url == null) {
	    return url;
	}
	url = url.replaceAll("&", "!AND!");
	url = url.replaceAll("=", "!EQUAL!");
	return url;
    }

   

    private String getResponseResult(List<ContentDetail> result, long date,
	    boolean isAfter, int limit, String userAgent,String token) {
    boolean isGuestUser = this.isGuestUser(token);
	List<ResponseContent> contentList = new ArrayList<ResponseContent>();
	int index = -1;
	ContentDetail cd = new ContentDetail();
	Repository r = new Repository();
	r.setLastModified(new Date(date));
	cd.setRepository(r);
	BinarySearch<ContentDetail> binSearch = new BinarySearch<ContentDetail>(
		result);
	index = binSearch.search(cd);
	if ((index >= 0) && (isAfter)) {
	    index--;
	} else if ((index >= 0) && (!isAfter)) {
	    index++;
	} else if ((index < 0) && (isAfter)) {
	    if (binSearch.getHigh() < 0) {
		JSONArray json = JSONArray.fromObject(contentList);
		return json.toString();
	    }
	    index = binSearch.getLow() > result.size() - 1 ? result.size() - 1
		    : binSearch.getLow() - 1;
	} else if ((index < 0) && (!isAfter)) {
	    if (binSearch.getLow() > result.size()) {
		JSONArray json = JSONArray.fromObject(contentList);
		return json.toString();
	    }
	    index = binSearch.getHigh() + 1;
	}

	ContentDetail contentDetail = null;
	for (int i = 0; i < limit; i++) {
	    if (isAfter) {
		if (index - i < 0) {
		    break;
		}
		contentDetail = result.get(i);
		ResponseContent responseContent = this
			.getResponseContent(contentDetail, userAgent, "yes");
		if(isGuestUser) {
			responseContent.setTag(this.getTagListForGuestUser(responseContent.getTag()));
		}
		contentList.add(responseContent);
	    } else {
		if (index + i > result.size() - 1) {
		    break;
		}
		contentDetail = result.get(index + i);
		ResponseContent responseContent = this
			.getResponseContent(contentDetail, userAgent, "yes");
		if(isGuestUser) {
			responseContent.setTag(this.getTagListForGuestUser(responseContent.getTag()));
		}
		contentList.add(responseContent);
	    }
	}
	return json1(contentList);
    }
    
    private List<String> getTagListForGuestUser(List<String> tagList) {
    	if(tagList == null || tagList.size() < 1) {
    		return tagList;
    	}
    	if(!repositoryService.isContainTag(tagList, SystemTagEnum.Public.toString())) {
    		return tagList;
    	}
    	List<String> newTagList = new ArrayList<String>();
    	newTagList.add(SystemTagEnum.Public.toString());
    	if(repositoryService.isContainTag(tagList, SystemTagEnum.Original_None.toString())) {
    		newTagList.add(SystemTagEnum.Original_None.toString());
    	}
    	return newTagList;
    }

    

    

    private List<JsonObject> reduceTags(final List<Tag> tags) {
	List<JsonObject> ret = new ArrayList<JsonObject>(tags.size());
	for (final Tag tag : tags) {
	    @SuppressWarnings("unused")
	    JsonObject o = new JsonObject() {
		public Map<String, Long> getCounter() {
		    Map<String, Long> counterMap = new HashMap<String, Long>();
		    counterMap.put("quantity", (long) tag.getCounter());
		    return counterMap;
		}

		public String getName() {
		    return tag.getId().getName();
		}
	    };
	    ret.add(o);
	}
	return ret;
    }

    /**
     * 如果是分享给微博好友 发微博给好友 即@好友
     * 
     * @param contentId
     *            要发布的内容的id
     * @param authToken
     *            分享这条内容的登录人的authToken
     * @param thirdparth_uids
     *            要@的第三方好友的uid数组
     * @param result
     *            返回的分享结果：{ “result”:[ {“accountId”: “好友的ID”, “status”:
     *            “Accepted”}, {“accountId”: “好友的ID”, “status”: “Failed”,
     *            “cause”: “NotFriends”}, {“accountId”: “好友的ID”, “status”:
     *            “Accepted”}, {“accountId”: “好友的ID”, “status”: “Accepted”} ] }
     * @param friendShareInfo
     * @param repository
     *            分享者关于contentId的Repository
     * @return
     */
    private List<Map<String, String>> share2ThirdParty(String contentId,
	    String myAccountId, ThirdParty thirdParty,
	    List<Map<String, String>> result, FriendShareInfo friendShareInfo,
	    Repository repository) {

	ThirdPartyType type = ThirdPartyType.forName(thirdParty.getType());
	String comment = friendShareInfo.getComment();
	if (comment == null)
	    comment = "";

	String content = thirdParty.getContent();

	if (StringUtils.isBlank(thirdParty.getContent())) {
	    content = " 我觉得这篇文章不错 "; // 要发布的微博内容
	    Invite invite = inviteService
		    .getByOwnerId(myAccountId);

	    String code;

	    if (invite == null) {
		Invite it = new Invite();
		it.setOwnerId(myAccountId);
		it.setCreateTime(new Date());
		code = inviteService.save(it);

	    } else {
		code = invite.getId();
	    }
	    String url = "http://www.mduoduo.com/ocean/" + code + "/"
		    + contentId;

	    url = thirdPartyService.getShortUrl(url);
	    content = content + url;
	}
	List<String> to = new ArrayList<String>(thirdParty.getReceipt().length);

	for (String uid : thirdParty.getReceipt()) {
	    to.add(uid);
	}
	ThirdPartyPostMessage message = new ThirdPartyPostMessage();
	message.setContent(content);
	message.setThirdPartyType(type);
	message.setUserId(to);
	message.setTitle("来自多多藏享的内容");
	// 将内容发给微博好友
	thirdPartyService.send2ThirdParty(myAccountId, message,
		MessageOperateType.Share);
	logger.debug("send2ThirdParty");
	List<String> mduoduoId = new ArrayList<String>(); // 微博uid对应的mduoduo账户id
							  // 若不是mduoduo用户则accountId为0
	for (String uid : thirdParty.getReceipt()) {
	    String accountId = get2CoffeeId(uid);
	    mduoduoId.add(accountId);
	}
	// 剔除mduoduoId中为0 的accountId，因为他们不是mduoduo的用户
	for (int i = mduoduoId.size() - 1; i >= 0; i--) {
	    String string = mduoduoId.get(i);
	    if (string.equals("0")) {
		mduoduoId.remove(i);
	    }
	}

	for (Iterator iterator = mduoduoId.iterator(); iterator.hasNext();) {
	    String accountId = (String) iterator.next();

	    if (friendService.getByAccountIdAndFriendId(
	    		myAccountId, accountId) == null) {

		Map<String, String> item = new HashMap<String, String>();
		item.put("accountId", accountId);
		item.put("status", "Failed");
		item.put("cause", "NotFriends");
		result.add(item);
		mduoduoId.remove(accountId);
		continue;

	    } else {
		Map<String, String> item = new HashMap<String, String>();
		item.put("accountId", accountId);
		item.put("status", "Accepted");
		result.add(item);
		continue;
	    }

	}

	// 如果同时是站内好友 则也进行站内分享 否则不分享 但要置My_Recommend标志
	if (mduoduoId.size() > 0) {
	    friendService
		    .shareToFriends(myAccountId,
			    repository.getId(),
			    mduoduoId, friendShareInfo.getScore(),
			    comment);

	} else {
	    repository.getTag().add(
		    tagService.getSystemTagName(SystemTagEnum.My_Recommend));

	    repository.setLastModified(new Date());
	    repositoryService.save(repository);
	}
	return result;
    }

    private Response sync(String token, String tag, String after,
	    String before, int limit, String userAgent) {

	long date;
	boolean isAfter;
	int code = this.getAuthorizationErrorCode(token);
	if(code > 200) {
		return Response.status(code).build();
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	
	String publicCond = null;
	if(StringUtils.isBlank(tag) && this.isGuestUser(token)) {
		publicCond = SystemTagEnum.Public.toString();
	}
	else if(SystemTagEnum.Public.toString().equalsIgnoreCase(tag)) {
		AccountIdOrGuestId = null;
	}
	else if(StringUtils.isNotBlank(tag)){
		tag = tag.trim();
	    tag = tag + ",Delete_" + tag;
	}

	if ((after != null) && (!after.equals(""))) {
	    try {
		date = DateUtil.FormatDateStringUTC(after).getTime();
	    } catch (ParseException e) {
		logger.info("after is not date string!");
		return Response.status(Status.NOT_FOUND).build();
	    }
	    isAfter = true;
	} else if ((before != null) && (!before.equals(""))) {
	    try {
		date = DateUtil.FormatDateStringUTC(before).getTime();
	    } catch (ParseException e) {
		logger.info("before is not date string!");
		return Response.status(Status.NOT_FOUND).build();
	    }
	    isAfter = false;
	} else {
	    date = 0;
	    isAfter = true;
	}

	PagedResult sr = contentSearcher.list(
		publicCond,
		AccountIdOrGuestId,
		tag,
		null,
		null,
		null,
		null,
		5,//sort
		"",// cn
		0, LIMIT);
	logger.info("Search query,{}:{}:{}", new Object[] { token, tag });
	if (sr.getTotal() <= 0) {
	    return Response.ok(
		    "{\"result\":[]}").build();
	}
	List<ContentDetail> result = repositoryService
		.findContentDetailsById(sr.getResult());

	return Response.ok(
		"{\"result\":"
			+ getResponseResult(result, date, isAfter,
				result.size() > limit ? limit : result.size(),
				userAgent,token)
			+ "}").build();

    }

    protected String json1(Object t) {
	if (t == null)
	    throw new IllegalArgumentException();

	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.registerJsonValueProcessor(Date.class,
		new DateJsonValueProcessor());

	return JSONSerializer.toJSON(t, jsonConfig).toString();
    }

    @Path("/personal/item")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addItem(@HeaderParam("Authorization") String token,
	    @HeaderParam("User-Agent") String agent,
	    GenericContent genericContent) {
    int code = this.getAuthorizationErrorCode(token);
    if(code > 200) {
    	return Response.status(code).build();
    }
    String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	// 添加内容
	Content content = genericContent;
	if (content.getId() == null)
	    content.setId("");

	if (ContentType.File.equals(content.getContentType())
		&& content.getId().trim().length() < 1) {
	    contentService.save(content);
	}
	if (ContentType.Image.equals(content.getContentType())
		&& content.getId().trim().length() < 1) {

	    if (content.getImagePayload() != null
		    && (content.getImagePayload().getUrl() == null || ""
			    .equals(content.getImagePayload().getUrl()))) {
		Document doc = documentService.getDocumentById((content
			.getImagePayload().getId()));

		if (doc != null) {
		    content.getImagePayload().setUrl(doc.getUrl());
		}
	    }
	    contentService.save(content);
	} else if (ContentType.Web.equals(content.getContentType())
		&& content.getId().trim().length() < 1) {
	    Content ct = contentService.getByUrlAndType(content.getUrl(),
		    ContentType.Web);
	    if (ct == null) {
		getAndSaveContent(content);
	    } else {
		content = ct;
		genericContent.setId(content.getId());
	    }
	} else if (content.getId() == null
		|| content.getId().trim().length() < 1) {// 片段处理

	    if (genericContent.getTag().contains(
		    SystemTagEnum.Source_Clip.toString())) {
		clearContent(content);
	    }
	    getAndSaveContent(content);

	} else {// url,已经抓取入口情况
	    Content org = contentService.getById(content.getId());
	    // is need reset product data
	    if (content.getProductPayload() != null && org != null
		    && ContentType.Product.equals(org.getContentType())) {
		ProductPayload productPayload = org.getProductPayload();
		ProductPayload paraPayload = content.getProductPayload();
		boolean isChange = false;
		if (productPayload == null)
		    productPayload = new ProductPayload();
		if (productPayload.getName() == null
			|| productPayload.getName().trim().length() < 1) {
		    productPayload.setName(paraPayload.getName());
		    isChange = true;
		}
		if (productPayload.getPicture() == null
			|| productPayload.getPicture().trim().length() < 1) {
		    productPayload.setPicture(paraPayload.getPicture());
		    isChange = true;
		}
		if (productPayload.getPrice() == null
			|| productPayload.getPrice().trim().length() < 1) {
		    productPayload.setPrice(paraPayload.getPrice());
		    isChange = true;
		}
		if (isChange) {
		    org.setProductPayload(productPayload);
		    contentService.save(org);
		}
	    }
	    if (org != null) {
		content = org;
	    }

	    if (content.getTitle() == null || content.getTitle().equals("")) {
		content.setTitle(genericContent.getTitle());
		contentService.save(content);
	    }
	}

	Repository repository = repositoryService
		.getRepositoryByContentIdAndAccountId(
			genericContent.getId(), AccountIdOrGuestId);
	if (repository == null) {// 第一次收藏，添加关系
		genericContent.setNewRepository(true);
	    repository = new Repository();
	    repository.setAccountId(AccountIdOrGuestId);
	    repository.setContentId(content.getId());
	    repository.setTag(genericContent.getTag(), content.getContentType()
		    .toString());// 系统标签
	    //is public account add public tag to repository
	    Account publicAccount = accountService.getPublicAccountById(AccountIdOrGuestId);
	    if(publicAccount != null) {
	    	repository.getTag().add(SystemTagEnum.Public.toString());
	    }
	    // product content type,remove later tag
	    if (ContentType.Product.equals(content.getContentType())) {
		repository.getTag().remove(SystemTagEnum.Later.toString());
	    }
	    if (ContentType.Product.equals(content.getContentType())
		    && repositoryService.isContainTag(genericContent.getTag(),
			    SystemTagEnum.Source_Plugin.toString())) {
		repository.getTag().add(SystemTagEnum.Product_Wish.toString());
	    }
	    if (ContentType.Image.equals(content.getContentType())
		    && repositoryService.isContainTag(genericContent.getTag(),
			    SystemTagEnum.Original_None.toString())) {
		repository.getTag().add(SystemTagEnum.Original_None.toString());
	    }
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Source_Clip.toString())) {
		if (!repositoryService.isContainTag(genericContent.getTag(),
			SystemTagEnum.Original_None.toString())) {
		    repository.getTag().add(
			    SystemTagEnum.Original_None.toString());
		}
	    }
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Source_Upload.toString())
		    &&
		    !repositoryService.isContainTag(genericContent.getTag(),
			    SystemTagEnum.Type_Image.toString())) {
		if (!repositoryService.isContainTag(genericContent.getTag(),
			SystemTagEnum.Original_None.toString())) {
		    repository.getTag().add(
			    SystemTagEnum.Original_None.toString());
		}
	    }
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Type_Image.toString())) {
		if (!repositoryService.isContainTag(genericContent.getTag(),
			SystemTagEnum.Original_None.toString())) {
		    repository.getTag().add(
			    SystemTagEnum.Original_None.toString());
		}
	    }
	    repository.setUserTag(genericContent.getUserTag());// 用户标签
	    repository.setDate(new Date());
	    repository.setLastModified(repository.getDate());
	} else {// 重新收藏，重新设置 日期
	    repository.setDate(new Date());
	    repository.setLastModified(repository.getDate());
	    // 重新设置系统标签
	    List<String> tagList = new ArrayList<String>();
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Collect.toString())) {
		tagList.add(SystemTagEnum.Collect.toString());
	    }
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Later.toString())) {
		tagList.add(SystemTagEnum.Later.toString());
	    }
	    // product content type,remove later tag
	    if (ContentType.Product.equals(content.getContentType())) {
		tagList.remove(SystemTagEnum.Later.toString());
	    }
	    repository.setTag(repositoryService.getTag(tagList,
		    repository.getTag(), "cover"));
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Source_Plugin.toString())) {
		if (!repositoryService.isContainTag(repository.getTag(),
			SystemTagEnum.Source_Plugin.toString())) {
		    repository.getTag().add(
			    SystemTagEnum.Source_Plugin.toString());
		}
	    }
	}
	repositoryService.save(repository);

	// 朋友分享
	if (genericContent.getShare() != null
		&& genericContent.getShare().size() > 0) {
	    friendService.shareToFriends(AccountIdOrGuestId,
		    repository.getId(),// repository.getId()
		    genericContent.getShare(),
		    genericContent.getComment().getScore(),
		    genericContent.getComment().getText());
	}

	boolean isPublicContent = false;

	if (genericContent.getTag() != null
		&& genericContent.getTag().contains("duoduo")) {
	    genericContent.getTag().remove("duoduo");
	    isPublicContent = true;
	}

	if (isPublicContent) {// 公共分享
	    PublicRepository publicRepository = repositoryService
		    .getPublicRepositoryByContentIdAndAccountId(
			    genericContent.getId(), AccountIdOrGuestId);
	    if (repositoryService.isContainPublicRepository(
		    content.getId(), AccountIdOrGuestId)) {
		publicRepository.setDate(new Date());
	    } else {
		publicRepository.setAccountId(AccountIdOrGuestId);
		publicRepository.setContentId(content.getId());
		publicRepository.setDate(new Date());
	    }
	    repositoryService.savePublicRepository(publicRepository);
	}

	logger.info("Repository created.{}", mappingObject(repository));

	// 添加索引
	ContentDetail contentDetail = new ContentDetail();
	contentDetail.setContent(content);
	contentDetail.setRepository(repository);
	contentSearcher.addIndex(contentDetail);
	ClientInfo info = UserAgentUtils.getClientInfo(agent);

	if (info.getClientType() == ClientType.Duoduo_android
		|| info.getClientType() == ClientType.Duoduo_ios) {
	    ContentDetail contentResult = repositoryService
		    .findContentDetailByContentIdAndAccountId(
			    genericContent.getId(), AccountIdOrGuestId);
	    if (contentResult == null) {
		return Response.status(Status.NOT_FOUND).build();
	    }

	    ResponseContent responseContent = this
		    .getResponseContent(contentResult, agent, "");
	    // responseContent = new ResponseContent();
	    // responseContent.setId("8888");

	    JsonConfig jsonConfig = new JsonConfig();
	    jsonConfig.registerJsonValueProcessor(Date.class,
		    new DateJsonValueProcessor("yyyy-MM-dd'T'HH:mm:ss'Z'"));

	    return Response.ok(
		    JSONSerializer.toJSON(responseContent, jsonConfig)
			    .toString())
		    .build();
	}
	return Response.ok(genericContent).build();
    }

    // delete style element and attribute
    private void clearContent(Content content) {
	String html = content.getHtmlPayload().getContent() == null ? ""
		: content.getHtmlPayload().getContent();
	Parser parser = new Parser();
	Page page = new Page(html);
	HtmlClearVisitor visitor = new HtmlClearVisitor();
	Lexer lexer = new Lexer();
	lexer.setPage(page);
	parser.setLexer(lexer);

	try {
	    parser.visitAllNodesWith(visitor);

	} catch (ParserException e) {
	    logger.error("find error when clear html {}", html);
	}
	content.getHtmlPayload().setContent("<p>" + visitor.getHtml() + "</p>");
    }

    @Path("/personal/tags/{tagName}")
    @PUT
    @Consumes("application/json")
    public Response addOrUpdateTag(@HeaderParam("Authorization") String token,
	    @PathParam("tagName") String tagName,
	    UserTag userTag) {

	try {
	    tagName = URLDecoder.decode(tagName, "utf-8");

	} catch (UnsupportedEncodingException e) {
	    // ignore
	}
	if (userTag == null) {
	    userTag = new UserTag();
	}
	Response valid = this.validAuthorization(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	tagName = tagName.replaceAll(" ", "");
	if (tagName.length() > 32) {
	    return Response.status(414).build();
	}

	Tag.TagId tagId = new Tag.TagId(AccountIdOrGuestId, tagName);
	Tag org = tagService.getById(tagId);
	if (org == null) {// 新建
	    Tag tag = new Tag();
	    tag.setId(tagId);
	    tagService.save(tag);
	    return Response.status(Status.CREATED).build();
	}
	if (tagName.equalsIgnoreCase(userTag.getName())) {// 已经存在
	    return Response.status(Status.CONFLICT).build();
	}
	// 修改情况
	String newTag = userTag.getName();
	if (newTag == null) {
	    return Response.status(Status.CONFLICT).build();
	}
	newTag = newTag.replaceAll(" ", "");
	if (newTag.length() > 32) {
	    return Response.status(414).build();
	}

	// 更新用户标签
	tagService.updateTagById(tagId, newTag);
	// 更新用户收藏内容标签含有标签情况
	repositoryService.updateTagByAccountIdAndTag(
			AccountIdOrGuestId, tagName, newTag);
	return Response.status(Status.OK).build();
    }



    @Path("/personal/item/{contentId}")
    @DELETE
    public Response deleteItem(@HeaderParam("Authorization") String token,
	    @PathParam("contentId") String contentId) {
	Response valid = this.validAuthorization(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	Repository repository = repositoryService
		.getRepositoryByContentIdAndAccountId(contentId,AccountIdOrGuestId);
	if (repository == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	List<String> tags = repository.getTag();
	tags.clear();
	tags.add(SystemTagEnum.Delete.toString());
	repository.setTag(tags);
	repositoryService.save(repository);
	logger.info("Repository deleted,{}", mappingObject(repository));
	// 更新索引
	contentSearcher.updateIndex(repository.getId());
	return Response.status(Status.NO_CONTENT).build();
    }

    @Path("/personal/tags/{tagName}")
    @DELETE
    public Response deleteTag(@HeaderParam("Authorization") String token,
	    @PathParam("tagName") String tagName) {
	Response valid = this.validAuthorization(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	try {
	    tagName = URLDecoder.decode(tagName, "utf-8");

	} catch (UnsupportedEncodingException e) {
	    // ignore
	}

	Tag.TagId tagId = new Tag.TagId(AccountIdOrGuestId, tagName);
	Tag org = tagService.getById(tagId);
	if (org == null) {// 标签不存
	    return Response.status(Status.NOT_FOUND).build();
	}

	// 删除用户标签
	tagService.removeById(tagId);

	// 删除用户收藏内容标签含有标签情况
	repositoryService.removeTagByAccountIdAndTag(AccountIdOrGuestId,
		tagName);

	return Response.status(Status.OK).build();
    }

    @Path("/personal/tags/")
    @GET
    @Produces("application/json;charset=utf-8")
    public Response findTags(@HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorization(token);
	if(valid != null) {
		return valid;
	}
	String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	List<Tag> tags = tagService.getTagsByAccount(AccountIdOrGuestId);
	if (tags == null)
	    tags = new ArrayList<Tag>();
	PagedResult sr = new PagedResult();
	sr.setResult(reduceTags(tags));
	sr.setTotal(tags.size());
	sr.setLastPage(true);
	return Response.ok(sr).build();
    }

   

    public ContentSearcher getContentSearcher() {
	return contentSearcher;
    }


    @Path("/personal/item/{contentId}")
    @GET
    @Produces("application/json;charset=utf-8")
    public Response getItemById(@PathParam("contentId") String contentId,
	    @HeaderParam("Authorization") String token,
	    @HeaderParam("User-Agent") String userAgent) {
    int code = this.getAuthorizationErrorCode(token);
    if(code > 200) {
    	return Response.status(code).build();
    }
    String accountIdOrGuestId = this.getAccountIdOrGuestId(token);
    if(StringUtils.isBlank(contentId)) {
    	return Response.status(404).build();
    }
    ContentDetail contentDetail = null;
    //support from public content
    if(contentId.indexOf("_") > -1) {
    	String id = contentId.substring(contentId.indexOf("_")+1);
    	contentDetail = repositoryService.findContentDetailById(id);
    }
    else {
    	contentDetail = repositoryService
		.findContentDetailByContentIdAndAccountId(
			contentId, accountIdOrGuestId);
    }
	if (contentDetail == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}

	ResponseContent responseContent = this
		.getResponseContent(contentDetail, userAgent, "");

	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.registerJsonValueProcessor(Date.class,
		new DateJsonValueProcessor("yyyy-MM-dd'T'HH:mm:ss'Z'"));

	return Response.ok(
		JSONSerializer.toJSON(responseContent, jsonConfig).toString())
		.build();
    }


    @Path("/personal/search/{a:.*}")
    @GET
    // @Consumes("application/json;charset=utf-8")
    @Produces("application/json;charset=utf-8")
    public Response searchItem(@HeaderParam("Authorization") String token,
	    @HeaderParam("User-Agent") String userAgent,
	    @MatrixParam("tag") String tag,
	    @MatrixParam("userTag") String userTag,
	    @MatrixParam("order") String order,
	    @MatrixParam("keyword") String keyword,
	    @MatrixParam("friend") String friend,
	    @QueryParam("limit") int limit, @QueryParam("offset") int offset) {

	// 可配置 solr mongodb

	// 自定义方法，完全的走数据库
    int code = this.getAuthorizationErrorCode(token);
    if(code > 200) {
		return Response.status(code).build();
	}
    if(!TokenUtil.isEmptyAccountId(token)) {
    	userAgent = ClientType.Duoduo_client_browser.toString();;
    }
    String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	tag = tag == null ? "" : tag.trim();
	if (tag.startsWith(",")) {
	    tag = tag.substring(1, tag.length());
	}
	if (tag.endsWith(",")) {
	    tag = tag.substring(0, tag.length() - 1);
	}
	//search public content
	if(tag.contains(SystemTagEnum.Public.toString())) {
		AccountIdOrGuestId = null;
		userTag = null;
		friend = null;
	}
	PagedResult sr = null;
	if (keyword != null) {
	    sr = contentSearcher.list(AccountIdOrGuestId, tag, keyword,
		    userTag, friend, contentSearcher.getSortType(order), "",// cn
		    offset, limit);

	} else {
	    RpcConfiguration conf = (RpcConfiguration) SpringContext
		    .getBean("rpcConfig");
	    if (conf.isDBresource()) {
		sr = repositoryService.search(AccountIdOrGuestId, tag,
			userTag, friend, offset, limit);
	    } else {
		// tag = tag.replace("_", "");
		String accountId = AccountIdOrGuestId;
		if (tag.contains("Link")) {
		    accountId = null;
		}
		sr = contentSearcher.list(accountId, tag,
			keyword, userTag, friend,
			contentSearcher.getSortType(order), "",// cn
			offset, limit);
	    }

	}

	logger.info("Search query,{}:{}:{}:{}:{}", new Object[] { token, tag,
		keyword, friend });
	List<ContentDetail> result = repositoryService
		.findContentDetailsById(sr.getResult());

	if (result == null || result.size() < 1) {
	    sr.setTotal(0);
	    result = new ArrayList();
	}
	if (sr.getResult() != null && sr.getTotal() == sr.getResult().size()) {
	    if (result.size() < sr.getResult().size()) {
		sr.setTotal(result.size());
	    }
	}
	List<ResponseContent> contentResult = new ArrayList<ResponseContent>();
	for (ContentDetail detail : result) {
	    contentResult.add(this.getResponseContent(detail, userAgent, ""));
	}
	sr.setResult(contentResult);
	sr.setLastPage(limit, offset);
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.registerJsonValueProcessor(Date.class,
		new DateJsonValueProcessor("yyyy-MM-dd'T'HH:mm:ss'Z'"));
	return Response.ok(JSONSerializer.toJSON(sr, jsonConfig).toString())
		.build();
    }


    public void setContentSearcher(ContentSearcher contentSearcher) {
	this.contentSearcher = contentSearcher;
    }

    public void setContentService(ContentService contentService) {
	this.contentService = contentService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public void setTagService(TagService tagService) {
	this.tagService = tagService;
    }


    /**
     * 分享给朋友
     * 
     * @param token
     * @param contentId
     * @param friendList
     *            朋友列表，json格式
     *            {"comment":"二层","receipt":[{"accountId":"50066b9a5a6812ef757ced54"
     *            },{"accountId":"500d3151c601d5f64bf0977c"}],"score":0}
     * @param thirdparty
     *            微博朋友列表，json格式
     *            {"thirdpartyType":"Weibo","receipt":[{"uid":"50066b9a5a6812ef757ced54"
     *            },{"uid":"500d3151c601d5f64bf0977c"}],"score":0}
     * @param ua
     *            Apache-HttpClient/4.1.2 (java 1.5)
     * @return
     */
    @Path("/personal/item/{contentId}/share")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response share(@HeaderParam("Authorization") String token,
	    @PathParam("contentId") String contentId,
	    String friendList, @HeaderParam("User-Agent") String ua) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if(valid != null) {
		return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);
	
	JSONObject ja = JSONObject.fromObject(friendList);
	FriendShareInfo r = (FriendShareInfo) JSONObject.toBean(ja,
		FriendShareInfo.class);

	boolean thirdpartyShare = false;

	// 微博好友分享
	if (r.getThirdparty() != null && r.getThirdparty().length > 0) {
	    thirdpartyShare = true;
	}

	if (thirdpartyShare == false
		&& (r.getReceipt() == null || r.getReceipt().length < 1)) {

	    return Response.status(501).build();
	}
	Repository reps = repositoryService
		.getRepositoryByContentIdAndAccountId(
			contentId, accountId);

	if (reps == null || reps.getId() == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	Map<String, List<Map<String, String>>> results = new HashMap<String, List<Map<String, String>>>();
	List<Map<String, String>> result = new LinkedList<Map<String, String>>();
	shareDuoduo(accountId, r, reps, result);

	if (thirdpartyShare) {

	    for (ThirdParty thirdParty : r.getThirdparty()) {

		try {
		    result = share2ThirdParty(contentId, accountId, thirdParty,
			    result, r,
			    reps);

		} catch (Exception e) {
		    logger.error("thirdparty share error!", e);
		}
	    }
	}
	results.put("result", result);
	return Response.status(Status.ACCEPTED).entity(results).build();

    }

    private void shareDuoduo(String myAccountId, FriendShareInfo r,
	    Repository reps, List<Map<String, String>> result) {
	if ((r.getReceipt() != null && r.getReceipt().length > 0)) {

	    for (AccountId accountId : r.getReceipt()) {

		if (friendService.getByAccountIdAndFriendId(
				myAccountId, accountId.getAccountId()) == null) {

		    Map<String, String> item = new HashMap<String, String>();
		    item.put("accountId", accountId.getAccountId());
		    item.put("status", "Failed");
		    item.put("cause", "NotFriends");
		    result.add(item);
		    continue;

		} else {
		    Map<String, String> item = new HashMap<String, String>();
		    item.put("accountId", accountId.getAccountId());
		    item.put("status", "Accepted");
		    result.add(item);
		    continue;
		}
	    }
	    List<String> friendsList = getFriendList(r.getReceipt());
	    friendService
		    .shareToFriends(myAccountId, reps.getId(),
			    friendsList, r.getScore(), r.getComment());
	    // 保存分享日志
	    FriendSharedLog friendSharedLog = friendService
		    .getFriendSharedLogByAccountId(myAccountId);
	    if (friendSharedLog == null) {
		friendSharedLog = new FriendSharedLog();
	    }
	    friendSharedLog.setFriendId(friendsList);
	    friendSharedLog.setSharedTime(new Date());
	    friendSharedLog.setAccountId(myAccountId);
	    friendService.saveFriendSharedLog(friendSharedLog);
	}
    }

    @Path("/shared/{accountId}")
    @GET
    @Produces("application/json")
    public Response shared(@PathParam("accountId") String accoutId,
	    @HeaderParam("User-Agent") String userAgent,
	    @QueryParam("limit") int limit,
	    @QueryParam("offset") int offset) {

	int total = repositoryService.countSharedByAccountId(accoutId);

	// TODO YUXJ 时间和id的关系？ 不显式指定排序字段的时候 按什么排序？ date

	List<ContentDetail> contentDetailList = repositoryService
		.getSharedByAccountId(accoutId, "", limit, offset);
	if (contentDetailList == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	List<ResponseContent> contentResult = new ArrayList<ResponseContent>();
	for (ContentDetail detail : contentDetailList) {
	    contentResult.add(this.getResponseContent(detail, userAgent, ""));
	}
	PagedResult<ResponseContent> sr = new PagedResult<ResponseContent>();
	sr.setResult(contentResult);
	sr.setTotal(total);
	return Response.ok(sr).build();
    }

    @Path("/personal/statistics/tags")
    @GET
    @Produces("application/json")
    public Response statisticTag(@HeaderParam("Authorization") String token) {
    int code = this.getAuthorizationErrorCode(token);
    if(code > 200) {
    	return Response.status(code).build();
    }
    String accountIdOrGuestId = this.getAccountIdOrGuestId(token);
	Map<String, StatisticTag> tagMap = repositoryService
		.statisticTagMap(accountIdOrGuestId);
	if (tagMap == null || tagMap.size() < 1) {
	    return Response.status(Status.NO_CONTENT).build();
	}
	return Response.ok(tagMap).build();
    }

    @Path("/personal/sync/item")
    @GET
    @Produces("application/json;charset=UTF-8")
    public Response syncAll(
	    @HeaderParam("Authorization") String token,
	    @HeaderParam("User-Agent") String userAgent,
	    @QueryParam("before") String before,
	    @QueryParam("after") String after,
	    @QueryParam("limit") @DefaultValue("100") int limit) {

	return sync(token, null, after, before, limit, userAgent);
    }

    @Path("/personal/sync/item/{tagName}")
    @GET
    @Produces("application/json;charset=UTF-8")
    public Response syncByTag(@PathParam("tagName") String tag,
	    @HeaderParam("Authorization") String token,
	    @HeaderParam("User-Agent") String userAgent,
	    @QueryParam("before") String before,
	    @QueryParam("after") String after,
	    @QueryParam("limit") @DefaultValue("100") int limit) {

	return sync(token, tag, after, before, limit, userAgent);
    }

    /**
     * 更新收藏项
     * 
     * @param token
     * @param contentId
     * @param genericContent
     * @return
     */
    @Path("/personal/item/{contentId}")
    @PUT
    @Consumes("application/json")
    public Response updateItem(@HeaderParam("Authorization") String token,
	    @PathParam("contentId") String contentId,
	    GenericContent genericContent) {
    Response valid = this.validAuthorization(token);
    if(valid != null) {
    	return valid;
    }
    String accountIdOrGuestId = this.getAccountIdOrGuestId(token);
	Repository repository = repositoryService
		.getRepositoryByContentIdAndAccountId(contentId,accountIdOrGuestId);
	if (repository == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	if (genericContent.getTag() != null
		&& genericContent.getTag().size() > 0) {
	    if (repositoryService.isExistTag(genericContent.getTag(),
		    repository.getTag())) {// 已经存在tag
		return Response.status(Status.CONFLICT).build();
	    }
	}

	Content content = contentService.getById(repository.getContentId());
	if (content == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}

	List<String> orgTag = repository.getUserTag();// 用户标签
	if (genericContent.getUserTag() != null
		&& genericContent.getUserTag().size() > 0) {
	    repository.setUserTag(genericContent.getUserTag());
	    tagService.save(accountIdOrGuestId, orgTag,
		    genericContent.getUserTag());
	}
	// 设置系统标签
	repository.setTag(repositoryService.getTag(genericContent.getTag(),
		repository.getTag(), genericContent.getMenuType()));
	repository.setLastModified(new Date());
	repositoryService.save(repository);

	// 设置收藏
	if (repositoryService.isContainTag(genericContent.getTag(),
		SystemTagEnum.Collect.toString())
		|| repositoryService.isContainTag(genericContent.getTag(),
			SystemTagEnum.Delete_Collect.toString())) {
	    if (repositoryService.isContainTag(genericContent.getTag(),
		    SystemTagEnum.Collect.toString())) {
		content = content.setCollect(1);
	    } else {// 取消收藏
		content = content.setCollect(-1);
	    }
	    contentService.save(content);
	}

	logger.info("{} Repository updated.Tag changed from:{} to {}.",
		new Object[] {
			repository.getId(), orgTag, repository.getTag() });

	// 更新索引
	contentSearcher.updateIndex(repository.getId());
	return Response.status(Status.NO_CONTENT).build();
    }

    @Path("/personal/tags")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateTag(@HeaderParam("Authorization") String token,
	    List<UserTag> userTagList
	    ) {
	Response valid = this.validAuthorization(token);
	if(valid != null) {
		return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);

	if (userTagList == null || userTagList.size() < 1) {
	    return Response.status(Status.NOT_MODIFIED).build();
	}

	List<UserTag> errorList = new ArrayList<UserTag>();
	for (int i = 0; i < userTagList.size(); i++) {
	    UserTag userTag = userTagList.get(i);
	    if (userTag == null) {
		continue;
	    }
	    String oldTag = userTag.getName() == null ? "" : userTag.getName()
		    .trim();
	    String newTag = userTag.getNewName() == null ? "" : userTag
		    .getNewName().trim();
	    if (oldTag.length() < 1) {
		userTag.setMessage(MessageType.NotExist);
		errorList.add(userTag);
		continue;
	    }
	    if (newTag.length() < 1) {
		userTag.setMessage(MessageType.Empty);
		errorList.add(userTag);
		continue;
	    }
	    if (newTag.length() > 32) {
		userTag.setMessage(MessageType.LengthExceed);
		errorList.add(userTag);
		continue;
	    }

	    Tag.TagId tagId = new Tag.TagId(accountId, oldTag);
	    Tag org = tagService.getById(tagId);
	    if (org == null) {// 标签不存
		userTag.setMessage(MessageType.NotExist);
		errorList.add(userTag);
		continue;
	    }
	    // 更新用户标签
	    tagService.updateTagById(tagId, newTag);
	    // 更新用户收藏内容标签含有标签情况
	    repositoryService.updateTagByAccountIdAndTag(
	    		accountId, oldTag, newTag);

	}
	if (errorList != null && errorList.size() > 0) {
	    return Response.status(Status.CONFLICT).entity(errorList).build();
	}
	return Response.ok().build();
    }

}
