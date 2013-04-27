package me.twocoffee.rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Feedback;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FeedbackService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/customer")
public class FeedbackResource {

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private AccountService accountService;

	@Path("/feedback")
	@POST
	@Consumes({ "application/json" })
	public Response recordFeedback(@HeaderParam("Authorization") String token,
			Feedback feedback) {

		String accountId = "";

		if (StringUtils.isNotBlank(token)) {
			String t = token.substring("AuthToken".length()).trim();
			AuthToken authToken = authTokenService.findById(t);

			if (authToken != null) {
				Account account = accountService.getById(authToken
						.getAccountId());

				if (account != null) {
					accountId = account.getId();
				}
			}
		}
		feedback.setAccountId(accountId);
		feedback.setCreateDate(new Date());
		feedbackService.saveFeedback(feedback);
		return Response.status(Status.NO_CONTENT).build();
	}

}
