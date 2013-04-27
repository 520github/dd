package me.twocoffee.rest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

@Controller
@Path("/service")
public class MockResource {
	@Path("/content/archive/mock/{encodedUrl}")
	@GET
	@Produces( { "application/meta-web+json", "application/meta-product+json" })
	public Response getContentByUrl(@PathParam("encodedUrl") String encodedUrl) {
		String url = null;
		try {
			url = URLDecoder.decode(encodedUrl, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400).build();
		}
		Map content = new HashMap();
		content.put("id", "001");
		content.put("url", url);
		content.put("contentType", "Web");
		content.put("content", "<h2>hello world</h2>");

		return Response.ok(content).build();
	}

	@Path("/content/archive/mock/{encodedUrl}")
	@GET
	@Produces( { "application/meta-image+json" })
	public Response getImageByUrl(@PathParam("encodedUrl") String encodedUrl) {

		String url = null;
		try {
			url = URLDecoder.decode(encodedUrl, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400).build();
		}
		Map content = new HashMap();
		content.put("id", "002");
		content.put("contentType", "Image");
		content.put("archivedUrl", url);

		return Response.ok(content).build();

	}
}
