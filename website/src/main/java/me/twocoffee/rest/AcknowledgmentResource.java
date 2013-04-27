package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.Acknowledgment;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.service.AcknowledgmentService;
import me.twocoffee.service.AuthTokenService;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service")
public class AcknowledgmentResource extends AbstractResource{

	private static String extractToken(String token) {
		return token == null || !token.startsWith("AuthToken") ? null : token
				.replace("AuthToken ", "");
	}
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private AcknowledgmentService acknowledgmentService;
	
	
	@Path("/acknowledgment/{contentId}")
	@POST
	public Response postAcknowledgment(@HeaderParam("Authorization") String token,
			@PathParam("contentId") String contentId	
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	   
	if(!acknowledgmentService.Available(AccountIdOrGuestId, contentId)){
		  return Response.status(Status.CONFLICT).build();	
		}
		acknowledgmentService.sendAcknowledgmentToQueue(AccountIdOrGuestId, contentId);
		return Response
				.status(Status.NO_CONTENT).build();
	}
	
	
	@Path("/acknowledgment/{a:.*}")
	@GET
	@Produces("application/json")
	public Response getAcknowledgment(@HeaderParam("Authorization") String token,
			@MatrixParam("accountId") String accountId,
			@MatrixParam("contentId") String contentId,
			@QueryParam("limit") int limit,
			@QueryParam("offset") int offset
			) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String AccountIdOrGuestId = this.getAccountIdOrGuestId(token);
	   
		String accId = accountId;
		if(StringUtils.isBlank(accId)){
			accId = AccountIdOrGuestId;
		}
		
		int total = acknowledgmentService.totalByAccountIdAndContent(accId, contentId);
		List<Acknowledgment> result = acknowledgmentService.findByAccountIdAndContentId(accId, contentId, limit, offset);
        PagedResult<Acknowledgment> pageResult = new PagedResult<Acknowledgment>();
        pageResult.setTotal(total);
        pageResult.setLastPage(limit, offset);
        pageResult.setResult(result);
        if (result == null || result.size() < 1) {
        	pageResult.setTotal(0);
		}
        JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcessor());

		 
		return Response.ok(JSONSerializer.toJSON(pageResult, jsonConfig)).build();
	}
	
}
