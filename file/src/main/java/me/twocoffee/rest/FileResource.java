package me.twocoffee.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.CookieTool;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Document;
import me.twocoffee.entity.Settings;
import me.twocoffee.rest.utils.UserAgentUtils;
import me.twocoffee.rest.utils.UserAgentUtils.ClientInfo;
import me.twocoffee.rest.utils.UserAgentUtils.ClientType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DocumentService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
public class FileResource extends BaseController {

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

	private FileUploadForm getForm(MultipartFormDataInput input)
			throws IOException {

		FileUploadForm form = new FileUploadForm();
		List<InputPart> inputParts = input.getFormDataMap().get("uploadedFile");

		if (inputParts != null && !inputParts.isEmpty()) {
			InputPart inputPart = inputParts.get(0);
			File file = inputPart.getBody(new GenericType<File>() {
			});
			form.setFile(file);
		}

		List<InputPart> inputFileNameParts = input.getFormDataMap().get(
				"fileName");

		if (inputFileNameParts != null && !inputFileNameParts.isEmpty()) {
			InputPart inputPart = inputFileNameParts.get(0);
			String fileName = inputPart.getBodyAsString();
			form.setFileName(fileName);
		}
		return form;
	}

	private boolean isImage(Document doc) {

		try {

			if (doc.getName() != null && !"".equals(doc.getName())) {

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
	
	private String getAuthorization(HttpServletRequest request) {
		return TokenUtil.WEB_TOKEN + "" +CookieTool.getCookie(request, TokenUtil.WEB_TOKEN);
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
		if(StringUtils.isBlank(authorization)) {
			authorization = getAuthorization(request);
		}
//		int code = tokenUtil.getAuthorizationErrorCode(authorization);
//		if(code > 200) {
//			return Response.status(code).build();
//		}
		String dispositionType = "attachment";
		String disposition = request.getParameter("disposition");

		if ("inline".equals(disposition)) {
			dispositionType = "inline";
		}

		Document doc = documentService.getDocumentById(id);

		if (doc == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

//		if (!isImage(doc)) {
//
//			if (auth == null) {
//				return Response.status(403).build();
//			}
//		}
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
		ClientInfo clientInfo = UserAgentUtils.getClientInfo(userAgent);
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
	@Produces("application/json")
	public Response upload(MultipartFormDataInput input,
			@HeaderParam("Authorization") String authorization,
			@Context HttpServletRequest request) {
		if(StringUtils.isBlank(authorization)) {
			authorization = this.getAuthorization(request);
		}
		int code = tokenUtil.getAuthorizationErrorCode(authorization);
		if(code > 200) {
			return Response.status(code).build();
		}
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
		Map<String, Object> result = new HashMap<String, Object>();
		FileUploadForm form;

		try {
			form = getForm(input);

		} catch (IOException e1) {
			log.debug("find error when post file!", e1);
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}

		if (form.getFile() == null) {
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}

		if (form.getFileName() == null || "".equals(form.getFileName())) {

			result.put("result", "noFileName");
			return Response.status(400).entity(result).build();

		}
		Settings settings = accountService.getSettings(accountId,
				true);

		if (form.getSize() > settings.getFileUpload().getLimitation()
				.getMaxFileLength()) {

			result.put("maxFileLength", settings.getFileUpload()
					.getLimitation().getMaxFileLength());

			result.put("fileLength", form.getSize());
			return Response.status(413).entity(result).build();
		}
		String suffix = FilenameUtils.getExtension(form.getFileName());
		if (suffix == null)
			suffix = "";
		suffix = suffix.toLowerCase();
		if (!settings.getFileUpload().getLimitation().getWhitelist()
				.contains(suffix)) {

			result.put("whitelist", settings.getFileUpload().getLimitation()
					.getWhitelist().toArray());

			return Response.status(409).entity(result).build();
		}

		// if (settings.getFileUpload().getLimitation().getDailyQuota() <=
		// settings
		// .getFileUpload().getLimitation().getDailyUpload()) {
		//
		// result.put("dailyQuota", settings.getFileUpload().getLimitation()
		// .getDailyQuota());
		//
		// return Response.status(423)
		// .entity(JSONObject.fromObject(result).toString()).build();
		// }

		try {
			String fileId = fileOperator.putFile(form.getFile());
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
			result.put("fileId", doc.getId());
			result.put("fileLength", doc.getLength());
			result.put("url", doc.getUrl());
			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			log.error("upload file error!", e);
			return Response.status(500).build();
		}
	}

	@Path("/content/file")
	@POST
	@Produces("application/json")
	public Response uploadByStream(
			@HeaderParam("Authorization") String authorization,
			@HeaderParam("Content-Type") String type,
			@HeaderParam("Content-Length") int size,
			@Context HttpServletRequest request) {
		if(StringUtils.isBlank(authorization)) {
			authorization = this.getAuthorization(request);
		}
		int code = tokenUtil.getAuthorizationErrorCode(authorization);
		if(code > 200) {
			return Response.status(code).build();
		}
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream in = null;

		try {
			in = request.getInputStream();

		} catch (IOException e2) {
			log.error("find error when post file!", e2);
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}

		if (size == 0) {
			log.error("content length is 0!");
			result.put("result", "emptyContent");
			return Response.status(400).entity(result).build();
		}
		Settings settings = accountService.getSettings(accountId,
				true);

		// if (form.getSize() > settings.getFileUpload().getLimitation()
		// .getMaxFileLength()) {
		//
		// result.put("maxFileLength", settings.getFileUpload()
		// .getLimitation()
		// .getMaxFileLength());
		//
		// result.put("fileLength", form.getSize());
		// return Response.status(413)
		// .entity(JSONObject.fromObject(result).toString()).build();
		// }
		// String suffix = FilenameUtils.getExtension(form.getFileName());
		// if (suffix == null)
		// suffix = "";
		// suffix = suffix.toLowerCase();
		// if (!settings.getFileUpload().getLimitation().getWhitelist()
		// .contains(suffix)) {

		if (type == null || "".equals(type)
				|| !documentService.checkMimeType(type)) {
			
			result.put("whitelist", settings.getFileUpload().getLimitation()
					.getWhitelist().toArray());

			return Response.status(409).entity(result).build();
		}

		try {
			String objectId = new ObjectId().toString();
			String tempName = objectId + "." + type.substring(6);
			String fileId = fileOperator.putFile(in, size, tempName);
			Document doc = new Document();
			doc.setAccountId(accountId);
			doc.setCreateDate(new Date());
			doc.setFileId(fileId);
			doc.setId(objectId);
			doc.setLength((long) size);
			doc.setName(doc.getId());
			doc.setMimeType(type);

			doc.setUrl(getDownloadUrl(doc.getId()));
			documentService.save(doc);
			result.put("fileId", doc.getId());
			result.put("fileLength", doc.getLength());
			result.put("url", doc.getUrl());
			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			log.error("upload file error!", e);
			return Response.status(500).build();
		}
	}

	public static void main(String[] args) {
		String urlResouce = "http://localhost:7070/service/content/file"; // create
																			// URL
		File localFile = new File("C:\\Users\\momo\\Downloads\\aaa.jpg");
		try {

			HttpURLConnection urlConnection = (HttpURLConnection) (new URL(
					urlResouce)).openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.addRequestProperty("Authorization",
					"AuthToken 50908cf08eb5dc829b7e413e");
			urlConnection.addRequestProperty("Content-Type", "image/jpg1");
			urlConnection.addRequestProperty("Content-Length",
					String.valueOf(localFile.length()));
			OutputStream urlOutputStream = urlConnection.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(localFile);
			IOUtils.copy(fileInputStream, urlOutputStream);
			fileInputStream.close();
			urlOutputStream.close();
			System.out.println(urlConnection.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
