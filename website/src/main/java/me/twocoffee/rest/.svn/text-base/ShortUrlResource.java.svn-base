/**
 * 
 */
package me.twocoffee.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import me.twocoffee.rest.entity.ShortUrlInfo;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author wenjian
 *
 */
@Controller
@Path("/service/short_url")
public class ShortUrlResource {

	@Autowired
	private ThirdpartyService thirdpartyService;
	
	private static final Logger logger = LoggerFactory.getLogger(ShortUrlResource.class);
	
	@Path("/shorten")
	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response shorten(ShortUrlInfo info) {
		
		if (StringUtils.isBlank(info.getLong_url())) {
			logger.error("shorten url error! input parameter invalid {}", info.getLong_url());
			return Response.status(400).build();
		}
		ShortUrlInfo result;
		
		try {
			String shortUrl = thirdpartyService.getShortUrl(info.getLong_url());
			result = new ShortUrlInfo();
			result.setLong_url(info.getLong_url());
			result.setShort_url(shortUrl);
			return Response.ok(result).build();
		
		} catch (Exception e) {
			logger.error("shorten url error!server error!", e);
			return Response.status(500).build();
		}
		
	}
}
