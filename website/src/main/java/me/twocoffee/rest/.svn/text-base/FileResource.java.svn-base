package me.twocoffee.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.controller.AuthUtil;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Document;
import me.twocoffee.entity.Settings;
import me.twocoffee.rest.utils.UserAgentUtils;
import me.twocoffee.rest.utils.UserAgentUtils.ClientInfo;
import me.twocoffee.rest.utils.UserAgentUtils.ClientType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DocumentService;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service")
public class FileResource extends AbstractResource{

	class RangeInfo {

		private Long pastLength;

		private Long toLength;

		private Long contentLength;

		public Long getContentLength() {
			return contentLength;
		}

		public Long getPastLength() {
			return pastLength;
		}

		public Long getToLength() {
			return toLength;
		}

		public void setContentLength(Long contentLength) {
			this.contentLength = contentLength;
		}

		public void setPastLength(Long pastLength) {
			this.pastLength = pastLength;
		}

		public void setToLength(Long toLength) {
			this.toLength = toLength;
		}

	}

	private final static Logger log = LoggerFactory
			.getLogger(FileResource.class);

	private static final String COOKIE_AUTH_TOKEN = "authToken";

	@Autowired
	private FileOperator fileOperator;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private AccountService accountService;

	@Autowired
	protected final AuthTokenService authTokenService = null;

	private String getAuthTokenFromCookie(HttpServletRequest request) {

		if (request.getCookies() == null || request.getCookies().length == 0) {
			return null;
		}

		for (Cookie cookie : request.getCookies()) {

			if (cookie.getName().equals(COOKIE_AUTH_TOKEN)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	private ClientType getClientType(String userAgent) {

		if (userAgent == null || "".equals(userAgent)) {
			return ClientType.Duoduo;
		}

		if (userAgent.indexOf("MSIE") >= 0) {
			return ClientType.IE;

		} else if (userAgent.indexOf("Firefox") >= 0) {
			return ClientType.Firefox;

		} else if (userAgent.indexOf("Chrome") >= 0) {
			return ClientType.Chrome;
		}
		return ClientType.Duoduo;
	}

	private String getDownloadUrl(String id) {
		StringBuffer buffer = new StringBuffer("http://");
		buffer.append(SystemConstant.docDomainName)
				.append("/service/content/file/").append(id);

		return buffer.toString();
	}

	@SuppressWarnings("unchecked")
	private FileUploadForm getForm(HttpServletRequest request) throws Exception {
		FileUploadForm form = new FileUploadForm();
		List<FileItem> fileList = null;
		final DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		{ // 采用系统临时文件目录作为上传的临时目录
			final File tempfile = new File(System.getProperty("java.io.tmpdir"));
			diskFileItemFactory.setRepository(tempfile);
		}
		ServletFileUpload fileUpload = new ServletFileUpload(
				diskFileItemFactory);
		fileUpload.setSizeMax(1024 * 1024 * 10);
		fileUpload.setHeaderEncoding(request.getCharacterEncoding());

		try {
			fileList = fileUpload.parseRequest(request);

		} catch (FileUploadException e) {
			log.error("recieve file exception", e);
		}

		for (FileItem file : fileList) {

			if (file.isFormField()) {
				String fieldName = file.getString();
				form.setFileName(fieldName);

			} else {
				form.setData(file.get());
			}
		}
		return form;
	}

	private FileUploadForm getForm(MultipartFormDataInput input)
			throws IOException {

		FileUploadForm form = new FileUploadForm();
		List<InputPart> inputParts = input.getFormDataMap().get("uploadedFile");

		if (inputParts != null && !inputParts.isEmpty()) {
			InputPart inputPart = inputParts.get(0);
			for (String key : inputPart.getHeaders().keySet()) {
				System.out.println("key " + key);
				System.out.println("value " + inputPart.getHeaders().get(key));
			}
			String fileContent = inputPart.getBodyAsString();
			System.out.println(fileContent);
			InputStream in = inputPart.getBody(new GenericType<InputStream>() {
			});

			File file = inputPart.getBody(new GenericType<File>() {
			});
			String fileId = fileOperator.putFile(in, file.length(), "test");
			form.setFile(file);
			MultivaluedMap<String, String> headers = inputPart.getHeaders();

			for (String key : headers.keySet()) {
				log.debug(key);
				log.debug(headers.get(key).toString());
			}
			// form.setSize(size);
		}

		List<InputPart> inputFileNameParts = input.getFormDataMap().get(
				"fileName");

		if (inputFileNameParts != null && !inputFileNameParts.isEmpty()) {
			InputPart inputPart = inputFileNameParts.get(0);
			String fileName = inputPart.getBodyAsString();
			form.setFileName(fileName);
			MultivaluedMap<String, String> headers = inputPart.getHeaders();

			for (String key : headers.keySet()) {
				log.debug(key);
				log.debug(headers.get(key).toString());
			}
			// form.setSize(size);
		}
		return form;
	}

	private boolean isImage(Document doc) {

		try {

			if (doc.getName() == null || "".equals(doc.getName())) {

				if (doc.getName().trim().endsWith("bmp")) {
					return true;
				}

				if (doc.getName().trim().endsWith("gif")) {
					return true;
				}

				if (doc.getName().trim().endsWith("jpeg")) {
					return true;
				}

				if (doc.getName().trim().endsWith("jpg")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	protected AuthToken getAuthToken(String token) {

		if (token == null || token.equalsIgnoreCase("")) {
			return null;
		}
		String t = token;

		if (token.startsWith("AuthToken")) {
			t = token.substring("AuthToken".length()).trim();
		}
		AuthToken authToken = authTokenService.findById(t);

		if (authToken == null) {
			return null;
		}
		return authToken;
	}

	@Path("/content/file/{id}")
	@GET
	public Response downloadfile(
			@PathParam("id") String id,
			@HeaderParam("RANGE") String range,
			@HeaderParam("Range") String range_lowcase,
			@HeaderParam("Authorization") String authorization,
			// @QueryParam("disposition") String disposition,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@HeaderParam("User-Agent") String userAgent) {

		log.debug("user-agent " + userAgent);
		String dispositionType = "attachment";
		String disposition = request.getParameter("disposition");

		if ("inline".equals(disposition)) {
			dispositionType = "inline";
		}
		String token = authorization;
		if(StringUtils.isEmpty(authorization)) {
			token = AuthUtil.getWebTokenAuthorization(request);
		}

		Document doc = documentService.getDocumentById(id);

		if (doc == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		ClientInfo clientInfo = UserAgentUtils.getClientInfo(userAgent);
		if (!isImage(doc) && !UserAgentUtils.isClient(clientInfo)) {
			if (this.isGuestUser(token)) {
				return Response.status(403).build();
			}
		}
		if (range == null || "".equals(range)) {
			range = range_lowcase;
		}
		String charset = "utf-8";
		ClientType clientType = getClientType(userAgent);
		switch (clientType) {
		case IE:
			charset = "gb2312";
			break;

		default:
			break;
		}
		long pastLength = 0;// 记录已下载文件大小
		int rangeSwitch = 0;// 0：从头开始的全文下载；1：从某字节开始的下载（bytes=27000-）；2：从某字节开始到某字节结束的下载（bytes=27000-39000）;
		long toLength = 0;// 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
		long contentLength = 0;// 客户端请求的字节总量
		String rangeBytes = "";// 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容

		if (range != null) {// 客户端请求的下载的文件块的开始字节
			response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
			log.info("request.getHeader(\"Range\")=" + range);

			rangeBytes = range.replaceAll("bytes=", "");

			try {

				if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {// bytes=969998336-
					rangeSwitch = 1;
					rangeBytes = rangeBytes.substring(0,
							rangeBytes.indexOf('-'));
					pastLength = Long.parseLong(rangeBytes.trim());

					if (pastLength >= doc.getLength()) {
						return Response.status(416).build();
					}
					contentLength = doc.getLength() - pastLength + 1;// 客户端请求的是
																		// 969998336
					// 之后的字节
				} else {// bytes=1275856879-1275877358
					rangeSwitch = 2;
					String temp0 = rangeBytes.substring(0,
							rangeBytes.indexOf('-'));

					String temp2 = rangeBytes.substring(
							rangeBytes.indexOf('-') + 1, rangeBytes.length());

					pastLength = Long.parseLong(temp0.trim());// bytes=1275856879-1275877358，从第
																// 1275856879
																// 个字节开始下载
					toLength = Long.parseLong(temp2);// bytes=1275856879-1275877358，到第
														// 1275877358 个字节结束
					contentLength = toLength - pastLength + 1;// 客户端请求的是
																// 1275856879-1275877358
																// 之间的字节

					if (pastLength >= doc.getLength()
							|| toLength >= doc.getLength()
							|| contentLength > doc.getLength()) {

						return Response.status(416).build();
					}
				}

			} catch (Exception e) {
				log.error("download error!range " + range, e);
				return Response.status(416).build();
			}

		} else {// 从开始进行下载
			response.setStatus(200);
			contentLength = doc.getLength();// 客户端要求全文下载
		}
		/**
		 * 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。 响应的格式是:
		 * Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
		 * ServletActionContext.getResponse().setHeader("Content-Length", new
		 * Long(file.length() - p).toString());
		 */
		response.reset();// 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes

		if (clientInfo.getClientType() == ClientType.Duoduo_android
				|| clientInfo.getClientType() == ClientType.Duoduo_ios) {

			response.setHeader("Accept-Ranges", "bytes");
		}

		if (pastLength != 0) {
			// 不是从最开始下载,
			// 响应的格式是:
			// Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
			log.info("----------------------------不是从开始进行下载！服务器即将开始断点续传...");
			switch (rangeSwitch) {
			case 1: {// 针对 bytes=27000- 的请求
				log.debug("download from " + pastLength + " to all");
				String contentRange = new StringBuffer("bytes ")
						.append(new Long(pastLength).toString()).append("-")
						.append(new Long(doc.getLength() - 1).toString())
						.append("/")
						.append(new Long(doc.getLength()).toString())
						.toString();

				response.setHeader("Content-Range", contentRange);
				log.debug(contentRange);
				break;
			}
			case 2: {// 针对 bytes=27000-39000 的请求
				log.debug("download from " + pastLength + " to " + toLength);
				StringBuffer contentRange = new StringBuffer("bytes ")
						.append(rangeBytes).append("/")
						.append(new Long(doc.getLength()));

				response.setHeader("Content-Range", contentRange.toString());
				log.debug(contentRange.toString());
				break;
			}
			default: {
				break;
			}
			}

		} else if (pastLength == 0 && toLength != 0) {
			log.debug("download from 0 to " + toLength);
			StringBuffer contentRange = new StringBuffer("bytes ")
					.append(rangeBytes).append("/")
					.append(new Long(doc.getLength()));

			response.setHeader("Content-Range", contentRange.toString());
			log.debug(contentRange.toString());

		} else {
			log.debug("download from 0 to all");
		}

		try {
			StringBuffer buffer = new StringBuffer(dispositionType);
			buffer.append("; filename=\"")
					// .append(doc.getName()).append("\";");
					.append(new String(doc.getName().getBytes(charset),
							"ISO8859-1")).append("\";");

			if (clientType != ClientType.IE) {
				buffer.append("filename*=\"utf-8''")
						.append(URLEncoder.encode(doc.getName(), "utf-8"))
						.append("\"");

			}
			response.addHeader("Content-Disposition", buffer.toString());
			// + URLEncoder.encode(doc.getName(), "utf-8") + "\"");

			log.debug(buffer.toString());
			response.addHeader("Content-Type", doc.getMimeType());// set the
			// MIME
			// type.
			response.addHeader("Content-Length", String.valueOf(contentLength));
			switch (rangeSwitch) {
			case 0: {// 普通下载，或者从头开始的下载
				// 同1
			}
			case 1: {// 针对 bytes=27000- 的请求
				fileOperator.getFile(doc.getFileId(), pastLength, 0l,
						new DownloadResponseWriter(response));

				response.getOutputStream().close();
				break;
			}
			case 2: {// 针对 bytes=27000-39000 的请求
				fileOperator.getFile(doc.getFileId(), pastLength,
						contentLength, new DownloadResponseWriter(response));

				break;
			}
			default: {
				break;
			}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(500).build();
		}
		log.debug("success");
		return null;
	}

	@Path("/content/filetype/{postfix}")
	@GET
	public Response getFiletypeIcon(@PathParam("postfix") String postfix)
			throws URISyntaxException {

		if (StringUtils.isBlank(postfix)) {
			postfix = "UNKNOWN";
		}
		return Response.temporaryRedirect(
				new URI("/zh_CN/images1/filetype/" + postfix.toUpperCase()
						+ ".png")).build();

	}

	@Path("/content/file")
	@POST
	@Consumes("multipart/form-data")
	@Produces("text/html")
	public Response upload(
			// MultipartFormDataInput input,
			@HeaderParam("Authorization") String authorization,
			@Context HttpServletRequest request) {
		Response valid = this.validAuthorizationAndGuestUser(authorization);
	    if(valid != null) {
	    	return valid;
	    }
	    String accountId = this.getAccountIdOrGuestId(authorization);
	    
		Map<String, Object> result = new HashMap<String, Object>();
		FileUploadForm form;

		try {
			form = getForm(request);

		} catch (Exception e1) {
			log.debug("find error when post file!", e1);
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}

		if (form.getData() == null || form.getData().length == 0) {
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}

		if (form.getFileName() == null || "".equals(form.getFileName())) {

			result.put("result", "noFileName");
			return Response.status(400)
					.entity(JSONObject.fromObject(result).toString()).build();

		}
		Settings settings = accountService.getSettings(accountId,
				true);

		settings.getFileUpload()
				.getLimitation()
				.setDailyUpload(
						Long.valueOf(
								documentService.getDailyUpload(accountId)).intValue());

		if (form.getSize() > settings.getFileUpload().getLimitation()
				.getMaxFileLength()) {

			result.put("maxFileLength", settings.getFileUpload()
					.getLimitation().getMaxFileLength());

			result.put("fileLength", form.getSize());
			return Response.status(413)
					.entity(JSONObject.fromObject(result).toString()).build();

		}
		String suffix = FilenameUtils.getExtension(form.getFileName());
		if (suffix == null)
			suffix = "";
		suffix = suffix.toLowerCase();
		if (!settings.getFileUpload().getLimitation().getWhitelist()
				.contains(suffix)) {

			result.put("whitelist", settings.getFileUpload().getLimitation()
					.getWhitelist().toArray());

			return Response.status(409)
					.entity(JSONObject.fromObject(result).toString()).build();
		}

		if (settings.getFileUpload().getLimitation().getDailyQuota() <= settings
				.getFileUpload().getLimitation().getDailyUpload()) {

			result.put("dailyQuota", settings.getFileUpload().getLimitation()
					.getDailyQuota());

			return Response.status(423)
					.entity(JSONObject.fromObject(result).toString()).build();
		}

		try {
			String fileId = fileOperator.putFile(form.getData(),
					form.getFileName());

			Document doc = new Document();
			doc.setAccountId(accountId);
			doc.setCreateDate(new Date());
			doc.setFileId(fileId);
			doc.setId(new ObjectId().toString());
			doc.setLength(form.getSize());
			doc.setName(form.getFileName());
			doc.setMimeType(documentService.getMimeType(form.getFileName()
					.substring(form.getFileName().indexOf(".") + 1)));

			doc.setUrl(getDownloadUrl(doc.getId()));
			documentService.save(doc);
			Map<String, Integer> dailyUploadNumbers = settings.getFileUpload()
					.getLimitation().getDailyUploadNumber();
			
			for (String dateKey : dailyUploadNumbers.keySet()) {
				dailyUploadNumbers.put(dateKey, dailyUploadNumbers.get(dateKey) + 1);
			}
			accountService.saveSettings(settings);
			result.put("fileId", doc.getId());
			result.put("fileLength", doc.getLength());
			return Response.status(200)
					.entity(JSONObject.fromObject(result).toString()).build();

		} catch (Exception e) {
			log.error("upload file error!", e);
			return Response.status(500).build();
		}
	}
}
