package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import me.twocoffee.common.AllTagContentVisitor;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.rest.utils.UserAgentUtils;
import me.twocoffee.rest.utils.UserAgentUtils.ClientInfo;
import me.twocoffee.rest.utils.UserAgentUtils.ClientType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.entity.ContentDetail.Avatar;
import me.twocoffee.service.entity.ContentDetail.From;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author xiangjun.yu
 * 
 */
@Controller
@Path("/service/content/public")
public class ThirdPartyContentResource extends AbstractContentResource {

    private final static Logger logger = LoggerFactory
	    .getLogger(ThirdPartyContentResource.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private TagService tagService;

    private final SystemTagEnum[] typeTags = new SystemTagEnum[] {
	    SystemTagEnum.Type_HtmlClip, SystemTagEnum.Type_Image,
	    SystemTagEnum.Type_Product, SystemTagEnum.Type_Web,
	    SystemTagEnum.Type_Video, SystemTagEnum.Type_File
    };

    private ResponseContent getResponseContent(ContentDetail contentDetail,
	    String userAgent) {
    ClientInfo clientInfo = UserAgentUtils.getClientInfo(userAgent);
	ResponseContent responseContent = new ResponseContent();
	responseContent = this.setCommonResponseContent(responseContent,contentDetail);
	responseContent = this.setClient(clientInfo, responseContent, contentDetail);
	responseContent.setResponseId("");
	return responseContent;
    }


    public Parser createParser(String inputHTML) {
	Lexer mLexer = new Lexer(new Page(inputHTML));
	return new Parser(mLexer, new DefaultParserFeedback(
		DefaultParserFeedback.QUIET));

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

    /**
     * 查询公开内容接口：http://www.2coffee.com/service/content/public/search/;accountId=
     * ;contentId=; contentId:""} 接口说明：根据accountId和contentId查询内容；
     * 
     * @param accountId
     * @param contentId
     * @param userAgent
     * @return
     */
    @GET
    @Path("/search/{a:.*}")
    @Produces({ "application/json" })
    public Response queryPublicContent(
	    @MatrixParam("accountId") String accountId,
	    @MatrixParam("contentId") String contentId,
	    @HeaderParam("User-Agent") String userAgent) {

	ContentDetail contentDetail = new ContentDetail();
	Content content = contentService.getById(contentId);

	if (content == null) {
	    return Response.status(404).build();
	}
	Repository repository = repositoryService
		.getRepositoryByContentIdAndAccountId(contentId, accountId);

	if (repository == null) {
	    repository = new Repository();
	    repository.setDate(new Date());
	    repository.setLastModified(new Date());
	}
	contentDetail.setContent(content);
	contentDetail.setRepository(repository);
	From from = new From();
	Account account = accountService.getById(accountId);

	if (account != null) {
	    Avatar avatar = new Avatar();
	    avatar.setLarge(account.getPhotos().get(Account.PHOTO_SIZE_BIG));
	    avatar.setMedium(account.getPhotos().get(
		    Account.PHOTO_SIZE_MIDDLE));

	    avatar.setSmall(account.getPhotos().get(
		    Account.PHOTO_SIZE_SMALL));

	    from.setAccountId(accountId);
	    from.setAvatar(avatar);
	    from.setName(account.getName());
	    from.setDate(DateUtil.FormatDateUTC(repository.getDate()));
	    List<From> fromList = new ArrayList<From>();
	    fromList.add(from);
	    contentDetail.setFrom(fromList);
	}
	contentDetail.setType(getRepositoryType(content));
	return Response.ok(this.getResponseContent(contentDetail, userAgent))
		.build();

    }
    
    @GET
    @Path("/{repositoryId}")
    @Produces({ "application/json" })
    public Response getContentByRepositoryId(
    		@PathParam("repositoryId") String repositoryId,
    		@HeaderParam("User-Agent") String userAgent) {
    	ContentDetail contentDetail = repositoryService.findContentDetailById(repositoryId);
    	if (contentDetail == null) {
    	    return Response.status(404).build();
    	}
    	userAgent = ClientType.Duoduo_client_browser.toString();
    	return Response.ok(this.getResponseContent(contentDetail, userAgent)).build();
    }

    private SystemTagEnum getRepositoryType(Content r) {

	if (r == null || r.getContentType() == null)
	    return null;

	for (SystemTagEnum te : typeTags) {

	    if (r.getContentType().toString()
		    .equals(tagService.getSystemTagName(te).substring(5)))

		return te;
	}
	return null;
    }

}
