package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.AccountMailConfig;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.entity.Settings;
import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.Settings.Sharing;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.DeviceSetting;
import me.twocoffee.rest.entity.DeviceSetting.Aps;
import me.twocoffee.rest.entity.DeviceSetting.Authority;
import me.twocoffee.rest.entity.DeviceSetting.MailDrop;
import me.twocoffee.rest.entity.DeviceSetting.MailDrop.AddressVisibility;
import me.twocoffee.rest.entity.DeviceSetting.MailDrop.Personal;
import me.twocoffee.rest.entity.DeviceSetting.MailDrop.Personal.Setting;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.DeviceService;
import me.twocoffee.service.DocumentService;
import me.twocoffee.service.FriendMailConfigService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/setting")
public class DeviceSettingResource extends AbstractResource {

	@Autowired
	private ThirdpartyService thirdPartyService;

	@Autowired
	private FriendMailConfigService friendMailConfigService;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private DocumentService documentService;

	private AuthToken getAuthToken(String token) {
		if (token == null || token.equalsIgnoreCase("")) {
			return null;
		}
		String t = token.substring("AuthToken".length()).trim();
		AuthToken authToken = authTokenService.findById(t);
		if (authToken == null) {
			return null;
		}
		return authToken;
	}

	@Path("/authority/{thirdpartyType}")
	@DELETE
	@Produces("application/json")
	public Response deleteSetting(@HeaderParam("Authorization") String token,
			@PathParam("thirdpartyType") String type) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		ThirdPartyProfile p = thirdPartyService.getByAccountId(accountId, getType(type));

		if (p == null || !p.isBind()) {
			return Response.status(470).build();
		}

		if (p.isLogin()) {
			return Response.status(Status.CONFLICT).build();
		}
		ThirdPartyContentSynchronizeLog contentSynchronizeLog = new ThirdPartyContentSynchronizeLog();
		contentSynchronizeLog.setId(new ObjectId().toString());
		contentSynchronizeLog.setAccountId(accountId);
		contentSynchronizeLog.setSysnDate(p.getSyncDate());
		contentSynchronizeLog.setThirdPartyType(p.getAccountType());
		contentSynchronizeLog.setUid(p.getUserId());
		thirdPartyService.saveContentSynchronizeLog(contentSynchronizeLog);
		thirdPartyService.removeProfile(p);
		thirdPartyService.removeContact(getType(type), accountId);
		return Response.status(Status.NO_CONTENT).build();
	}
	
	private ThirdPartyType getType(String type) {
		return ThirdPartyType.forName(type);
	}

	@Path("/fileupload/limitation")
	@GET
	@Produces("application/json")
	public Response getFileuploadLimitation(
			@HeaderParam("Authorization") String token) {

		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);
		Settings settings = accountService.getSettings(accountId, true);

		settings.getFileUpload()
				.getLimitation()
				.setDailyUpload(
						Long.valueOf(
								documentService.getDailyUpload(accountId)).intValue());

		return Response.ok().entity(settings.getFileUpload().getLimitation())
				.build();
	}

	@Path("/")
	@GET
	@Produces("application/json")
	public Response getSetting(@HeaderParam("Authorization") String token) {
		Response valid = this.validAuthorizationAndGuestUser(token);
	    if(valid != null) {
	    	return valid;
	    }
	    String accountId = this.getAccountIdOrGuestId(token);

		List<Device> devices = deviceService.getByAccountId(accountId);

		DeviceSetting s = new DeviceSetting();

		if (devices != null && devices.size() > 0) {
			Aps aps = new Aps();
			aps.setAlert(devices.get(0).isPushAlert());
			s.setAps(aps);
		}
		Account account = accountService.getById(accountId);
		MailDrop mailDrop = new MailDrop();

		if (account != null && account.getMailConfig() != null) {
			mailDrop.setAddressVisibility(account.getMailConfig()
					.isFriendsVisible() ? AddressVisibility.Friend
					: AddressVisibility.Public);

			Personal personal = new Personal();

			if (account.getMailConfig().getInTags() != null) {
				Setting setting = new Setting();
				setting.setAutoTag(account.getMailConfig().getInTags());

				personal.setSetting(setting);
			}

			if (account.getMailConfig().getMails() != null) {
				personal.setAddress(account.getMailConfig().getMails());
			}
			mailDrop.setPersonal(personal);
		}

		List<FriendMailConfig> mailConfigList = friendMailConfigService
				.getFriendMailConfigList(accountId);

		if (mailConfigList != null && mailConfigList.size() > 0) {
			List<String> friendIds = new ArrayList<String>();

			for (FriendMailConfig temp : mailConfigList) {

				if (!temp.isBlocked()) {
					friendIds.add(temp.getFriendId());
				}
			}
			mailDrop.setCoffeeFriend(friendIds);
		}
		s.setMailDrop(mailDrop);
		Map<ThirdPartyType, Authority> authority = new HashMap<ThirdPartyType, DeviceSetting.Authority>();
		List<ThirdPartyProfile> profiles = thirdPartyService
				.getByAccountId(account.getId());

		if (profiles != null) {

			for (ThirdPartyProfile p : profiles) {

				if (p.isBind()) {
					Authority a = new Authority();
					a.setUid(p.getUserId());
					a.setSynchronous(p.isSyncContent());
					a.setName(p.getNickName());
					a.setAvatar(p.getAvatar());
					authority.put(p.getAccountType(), a);
				}
			}
		}
		s.setAuthority(authority);
		Settings settings = accountService.getSettings(accountId, true);

		settings.getFileUpload()
				.getLimitation()
				.setDailyUpload(
						Long.valueOf(
								documentService.getDailyUpload(accountId)).intValue());

		s.setFileupload(settings.getFileUpload());
		s.setSharing(settings.getSharing());
		return Response.ok(s).build();
	}

	@Path("/sharing")
	@GET
	@Produces("application/json")
	public Response getSharingSetting(@HeaderParam("Authorization") String token) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);

		Settings settings = accountService.getSettings(accountId, true);

		return Response.ok().entity(settings.getSharing()).build();
	}

	@Path("/authority/{thirdpartyType}/content_synchronize")
	@GET
	@Produces("application/json")
	public Response getThirdPartyContentSetting(
			@HeaderParam("Authorization") String token,
			@PathParam("thirdpartyType") String type) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);

		ThirdPartyProfile p = thirdPartyService.getByAccountId(
				accountId, getType(type));

		if (p == null) {
			return Response.status(404).build();
		}

		if (p.isBind()) {
			Map<String, String> authority = new HashMap<String, String>();
			authority.put("synchronous", String.valueOf(p.isSyncContent()));
			return Response.ok(authority).build();
		}
		return Response.status(470).build();
	}

	@Path("/authority/{thirdpartyType}")
	@GET
	@Produces("application/json")
	public Response getThirdPartySetting(
			@HeaderParam("Authorization") String token,
			@PathParam("thirdpartyType") String type) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);

		ThirdPartyProfile p = thirdPartyService.getByAccountId(
				accountId, getType(type));

		if (p != null && p.isBind()) {
			Authority a = new Authority();
			a.setUid(p.getUserId());
			a.setSynchronous(p.isSyncContent());
			a.setName(p.getNickName());
			a.setAvatar(p.getAvatar());
			return Response.ok(a).build();
		}
		return Response.status(470).build();
	}

	@Path("/")
	@PUT
	@Consumes("application/json")
	public Response putSetting(@HeaderParam("Authorization") String token,
			DeviceSetting s) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accoutId = this.getAccountIdOrGuestId(token);
		List<Device> devices = deviceService.getByAccountId(accoutId);

		if (devices != null && devices.size() > 0) {

			for (Device d : devices) {

				if (s.getAps() != null) {
					d.setPushAlert(s.getAps().isAlert());
					deviceService.save(d);
				}
			}
		}
		Account account = accountService.getById(accoutId);

		if (account != null) {

			if (s.getMailDrop() != null) {
				AccountMailConfig mailConfig = account.getMailConfig();

				if (mailConfig == null) {
					mailConfig = new AccountMailConfig();
					mailConfig.setFriendsVisible(true);
				}

				if (s.getMailDrop().getAddressVisibility() == AddressVisibility.Friend) {
					mailConfig.setFriendsVisible(true);

				} else if (s.getMailDrop().getAddressVisibility() == AddressVisibility.Public) {
					mailConfig.setFriendsVisible(false);
				}

				if (s.getMailDrop().getPersonal() != null) {

					if (s.getMailDrop().getPersonal().getAddress() != null) {
						mailConfig.setMails(s.getMailDrop().getPersonal()
								.getAddress());

					}

					if (s.getMailDrop().getPersonal().getSetting() != null) {

						if (s.getMailDrop().getPersonal().getSetting()
								.getAutoTag() != null) {

							mailConfig.setInTags(s.getMailDrop().getPersonal()
									.getSetting().getAutoTag());

						}
					}
				}
				accountService.save(account);
			}
		}
		return Response.noContent().build();
	}

	@Path("/sharing")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Response setSharingSetting(
			@HeaderParam("Authorization") String token, Sharing sharing) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);

		Settings oldSettings = accountService.getSettings(
				accountId, true);

		oldSettings.setSharing(sharing);
		accountService.saveSettings(oldSettings);
		return Response.noContent().build();
	}

	@Path("/authority/{thirdpartyType}/content_synchronize")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Response setThirdPartyContentSetting(
			@HeaderParam("Authorization") String token, Authority authority,
			@PathParam("thirdpartyType") String type) {
		Response valid = this.validAuthorizationAndGuestUser(token);
		if(valid != null) {
			return valid;
		}
		String accountId = this.getAccountIdOrGuestId(token);

		ThirdPartyProfile p = thirdPartyService.getByAccountId(
				accountId, ThirdPartyType.forName(type));

		if (p == null) {
			return Response.status(404).build();
		}

		if (p.isBind()) {
			p.setSyncContent(authority.isSynchronous());
			thirdPartyService.save(p);
			return Response.status(204).build();
		}
		return Response.status(470).build();
	}
}
