package me.twocoffee.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.service.rpc.ContentRpcService;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.common.base.Strings;

@Controller
@Path("/service/storage")
public class Storage {
	@Autowired
	private StorageService storageService;
	@Autowired
	private FileOperator fileOperator;
	
	@Autowired
	private ContentRpcService contentService;

	private final static class UploadResponse {
		private final String id;
		private final String url;
		private final String contentId;

		private UploadResponse(String id, String url,String contentId) {
			this.id = id;
			this.url = url;
			this.contentId = contentId;
		}

		public String getId() {
			return id;
		}
		public String getContentId() {
			return contentId;
		}
		public String getUrl() {
			return url;
		}
	}

	@Path("/")
	@POST
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public UploadResponse save(MultipartFormDataInput input)
			throws ParseException,
			IOException {
		String url = null;
		String id = null;
		List<InputPart> inputParts = input.getFormDataMap().get("uploadedFile");
		if (inputParts != null && !inputParts.isEmpty()) {
			InputPart inputPart = inputParts.get(0);
			String mediaType = inputPart.getMediaType().toString();
			if(mediaType == null)mediaType = "";
			if(mediaType.indexOf("image") == -1) {
				//return new UploadResponse("errorFileType","","");
			}
			System.out.println("mediaType:" + mediaType);
			File file = inputPart.getBody(new GenericType<File>() {
			});
			System.out.println("path:" +file.getAbsolutePath());
			id = fileOperator.putFile(file);
			url = fileOperator.getFileUrl(id);
			String contentId = "";
			try {
				if(mediaType.indexOf("image") >-1) {
					Map content = contentService.getContentByUrl("Image", url, url);
					contentId = (String)content.get("id");
					System.out.println("contentId:"+ contentId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new UploadResponse(id,url,contentId);
		}
		return null;
	}

	@Path("/")
	@POST
	@Consumes("multipart/form-data")
	@Produces("text/html")
	public String saveAndRenderAsHTML(MultipartFormDataInput input)
			throws ParseException,
			IOException {
		UploadResponse o = save(input);

		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		writer.println("<script>");
		writer.println("window.parent.onuploadcomplete('" + o.getId() + "');");
		writer.println("</script>");
		writer.close();
		return out.toString();
	}

	private String getName(InputPart inputPart) throws ParseException {
		String contentDisposition = inputPart.getHeaders().getFirst(
				"Content-Disposition");

		if (!Strings.isNullOrEmpty(contentDisposition)) {
			ContentDisposition cd = new ContentDisposition(contentDisposition);
			return cd.getParameter("name");
		} else {
			return null;
		}
	}

	@Path("/{id}")
	@GET
	public Response load(@PathParam("id") String id) throws IOException {
		StorageRecord record = storageService.load(id);
		if (record == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			InputStream in = record.getData();
			return Response.ok().header("Content-Type", record.getMediaType())
					.header("Content-Length", in.available()).entity(in)
					.build();
		}
	}
}
