package me.twocoffee.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Invite;
import me.twocoffee.entity.InviteRecord;
import me.twocoffee.entity.InviteRecord.InviteesType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.InviteRecordService;
import me.twocoffee.service.InviteService;
import me.twocoffee.service.SendEmailService;
import me.twocoffee.service.SendSMSService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/invite")
public class InviteController extends BaseController {

	@Autowired
	private final AuthTokenService authTokenService = null;
	@Autowired
	private final AccountService accountService = null;
	@Autowired
	private final InviteRecordService inviteRecordService = null;
	@Autowired
	private final SendEmailService sendEmailService = null;
	@Autowired
	private final SendSMSService sendSMSService = null;
	@Autowired
	private final InviteService inviteService = null;

	private ResponseEntity<String> getJsonResponseEntity(Map data) {

		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Content-Type",
				"application/json;charset=utf-8");

		return new ResponseEntity<String>(json(data), responseHeader,
				HttpStatus.OK);
	}

	@RequestMapping("/invitecode")
	@ResponseBody
	public ResponseEntity<String> getInviteCode(int number, String role,
			String token,
			HttpServletRequest req) {

		if (!"woaibabieta".equals(token)) {
			return buildJSONResult("error", "invild user!");
		}

		if (number < 0 || number > 500) {
			return buildJSONResult("error", "number is invalid! 0<number<=500");
		}

		if (StringUtils.isBlank(role)) {
			role = "__default__";
		}
		RoleType rt = null;

		for (RoleType type : RoleType.values()) {

			if (type.toString().equals(role)) {
				rt = type;
				break;
			}
		}
		List<Invite> invites = inviteService.getInvites(number, rt);
		return buildJSONResult("success", json1(invites), "number",
				String.valueOf(invites.size()));

	}

	@RequestMapping("/get")
	public List<InviteRecord> getInvitees(int pageSize, int pageNum,
			HttpServletRequest request,
			Model model) {
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			return null;
		}
		String accountId = tokenUtil.getAccountIdOrGuestId(authorization);
		List<InviteRecord> irList = inviteRecordService.findByInvitor(
				accountId,
				pageSize, pageNum);
		return irList;

	}

	@RequestMapping("/outside")
	public String invite(HttpServletRequest request, Model model) {
		String valid = tokenUtil.validAuthorizationAndGuestInController(AuthUtil.getWebTokenAuthorization(request));
		if(StringUtils.isNotBlank(valid)) {
			return valid;
		}
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
		
		Invite invite = inviteService.getByOwnerId(account.getId());
		String code;
		if (invite == null) {
			Invite it = new Invite();
			it.setOwnerId(account.getId());
			it.setCreateTime(new Date());
			code = inviteService.save(it);
		} else {
			code = invite.getId();
		}
		model.addAttribute("code", code);
		model.addAttribute("url",
				"http://"+SystemConstant.domainName+"/account/invite/" + code);
		model.addAttribute(
				"name",
				account.getName() != null ? account.getName() : account
						.getLoginName());
		return "invite/invite";
	}

	@RequestMapping("/post")
	@ResponseBody
	public ResponseEntity<String> inviteInvitees(String url, String invitees,
			String type,
			String code,
			HttpServletRequest request,
			Model model) {
		Map<String, String> result = new HashMap<String, String>();
		if (invitees == null || invitees.equals("")) {
			result.put("invitees", "null");
			getJsonResponseEntity(result);
		}
		
		String authorization = AuthUtil.getWebTokenAuthorization(request);
		String valid = tokenUtil.validAuthorizationAndGuestInController(authorization);
		if(StringUtils.isNotBlank(valid)) {
			result.put("auth", "failure");
			result.put("redirect", "/home");
			getJsonResponseEntity(result);
		}
		Account account = tokenUtil.getLoginUserAccontByAuthorization(authorization);
		if (account == null) {
			result.put("auth", "failure");
			result.put("redirect", "/home");
			getJsonResponseEntity(result);
		}
		String name = account.getName() == null ? account.getLoginName()
				: account.getName();
		Account ac = accountService.getByLoginName(invitees);
		if (ac != null) {
			result.put("auth", "success");
			result.put("result", "falure");
			result.put("reason", "exist");
			return getJsonResponseEntity(result);
		}
		InviteRecord ir = inviteRecordService.findByInvitorAndInvitees(account.getId()
				, invitees);
		if (ir != null) {
			result.put("auth", "success");
			result.put("result", "falure");
			result.put("reason", "duplicate");
			return getJsonResponseEntity(result);
		}
		List<InviteRecord> irList = inviteRecordService
				.findByInvitees(invitees);
		if ((irList != null) && (irList.size() > 0)) {
			result.put("auth", "success");
			result.put("result", "falure");
			result.put("reason", "duplicate");
			return getJsonResponseEntity(result);
		}
		Invite invite = inviteService.getByOwnerId(account.getId());
		if (!invite.getId().equals(code)) {
			result.put("auth", "success");
			result.put("result", "falure");
			result.put("code", "error");
			return getJsonResponseEntity(result);
		}
		InviteRecord inviteRecord = new InviteRecord();
		if (type.equalsIgnoreCase(InviteesType.Email.toString())) {
			sendEmailService.sendInvitation(invitees, code, name);
			inviteRecord.setType(InviteesType.Email);
		} else {
			sendSMSService.sendInvitation(invitees, code, name);
			inviteRecord.setType(InviteesType.Mobile);
		}
		inviteRecord.setCode(code);
		inviteRecord.setInvitees(invitees);
		inviteRecord.setInviteTime(new Date());
		inviteRecord.setInvitor(account.getId());
		inviteRecord.setResult(false);
		inviteRecordService.save(inviteRecord);

		result.put("result", "success");
		return getJsonResponseEntity(result);
	}
}
