package me.twocoffee.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.Pinyin4jUtils;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Friend.FriendType;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.FriendSharedLog;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.CounterInfo;
import me.twocoffee.rest.entity.FriendInfo;
import me.twocoffee.rest.entity.FriendLogInfo;
import me.twocoffee.rest.entity.FriendLogResult;
import me.twocoffee.rest.entity.FriendRelation;
import me.twocoffee.rest.entity.ListResult;
import me.twocoffee.rest.utils.InfoConverter;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.AcknowledgmentService;
import me.twocoffee.service.AuthTokenService;
import me.twocoffee.service.FriendMailConfigService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.FriendSharedLogService;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/service/relation")
public class FriendResource extends AbstractResource {
    @Autowired
    private final AuthTokenService authTokenService = null;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendSharedLogService friendSharedLogService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FriendMailConfigService friendMailConfigService;

    @Autowired
    private AcknowledgmentService acknowledgmentService;

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(FriendResource.class);

    private AuthToken getAuthToken(String token) {
	if (token == null || token.equalsIgnoreCase("")) {
	    LOGGER.debug("403 no token [" + token + "]");
	    return null;
	}
	String t = token.substring("AuthToken".length()).trim();
	AuthToken authToken = authTokenService.findById(t);
	if (authToken == null) {
	    LOGGER.debug("403 no authtoken [" + token + "]");
	    return null;
	}
	return authToken;
    }

    private Friend getFriend(String id, List<Friend> friends) {
	if (friends == null || friends.size() < 1) {
	    return null;
	}
	for (Friend f : friends) {
	    if (f.getFriendId().equals(id)) {
		return f;
	    }
	}
	return null;
    }

    private List<AccountInfo> getFriendInfos(List<AccountInfo> fs,
	    String visitAccountId) {
	if (fs == null || fs.size() < 1)
	    return null;

	List<Friend> visitFriends = friendService
		.findFriendInfos(visitAccountId);

	for (AccountInfo a : fs) {
	    FriendInfo fi = new FriendInfo();
	    Friend f = getFriend(a.getId(), visitFriends);
	    if (f != null) {
		fi.setIsFriend(true);
		fi.setAlias(f.getRemarkName());
		fi.setAliasInPinyin(f.getRemarkNameInPinyin());
		fi.setFrequently(f.getFriendType() != null
			&& f.getFriendType() == FriendType.Favorite);
	    }
	    fi.setShared(friendService.countSharedTo(visitAccountId, a.getId()));

	    a.setFriend(fi);
	}
	return fs;
    }

    private List<String> getFriendList(List<FriendLog> list) {
	if (list == null || list.size() < 1) {
	    return null;
	}

	List<String> ids = new ArrayList<String>();
	for (FriendLog log : list) {
	    boolean found = false;
	    for (String id : ids) {
		if (id.equals(log.getAccountId())) {
		    found = true;
		    break;
		}
	    }
	    if (!found) {
		ids.add(log.getAccountId());
	    }
	    found = false;
	    for (String id : ids) {
		if (id.equals(log.getFriendId())) {
		    found = true;
		    break;
		}
	    }
	    if (!found) {
		ids.add(log.getFriendId());
	    }
	}
	return ids;
    }

    // private boolean hasFriend(String id, List<Friend> friends) {
    // if (friends == null || friends.size() < 1) {
    // return false;
    // }
    // for (Friend f : friends) {
    // if (f.getFriendId().equals(id)) {
    // return true;
    // }
    // }
    // return false;
    // }

    private List<FriendLog> removeFinashed(List<FriendLog> list,
	    String accountId) {
	if (list == null || list.size() < 1) {
	    return null;
	}

	int count = list.size();
	for (int i = count - 1; i >= 0; i--) {
	    FriendLog log = list.get(i);
	    if (log.getLogType() != FriendLog.LogType.Adding) {
		list.remove(i);
		continue;
	    }
	}
	return list;
    }

    protected void setCounterInfo(AccountInfo info, int countByAccountId,
	    int countSharedByAccountId, int countAcknowledgment) {

	CounterInfo counter = new CounterInfo(countSharedByAccountId,
		countByAccountId, countAcknowledgment);

	info.setCounter(counter);
    }

    /**
     * 同意好友添加
     * 
     * @param accountId
     * @param token
     * @return
     */
    @Path("/friend/{accountId}")
    @PUT
    public Response accept(@PathParam("accountId") String accountId,
	    @HeaderParam("Authorization") String token) {
	AuthToken auth = getAuthToken(token);
	if (auth == null) {
	    return Response.status(403).build();
	}

	FriendLog log = friendService.getLogByAccountIdAndFriendId(accountId,
		auth.getAccountId());
	if (log == null) {
	    return Response.status(404).build();
	}
	friendService.acceptFriendRequest(log.getId(), auth.getAccountId());

	return Response.status(Status.OK).build();
    }

    /**
     * 添加好友申请
     * 
     * @param friendAddObject
     * @param token
     * @return
     */
    @Path("/friend-request")
    @POST
    @Consumes({ "application/json" })
    public Response add(String friendAddObject,
	    @HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);

	JSONObject ja = JSONObject.fromObject(friendAddObject);
	String remark = "";
	if (ja.get("alias") != null) {
	    remark = ja.get("alias").toString();
	}
	String postscript = "";
	if (ja.get("message") != null) {
	    postscript = ja.get("message").toString();
	}
	String thirdpartyType = "";
	if (ja.get("thirdpartyType") != null) {
	    thirdpartyType = ja.getString("thirdpartyType");
	}

	friendService.addFriend(accountId, ja.get("accountId")
		.toString(), remark, postscript, thirdpartyType);

	return Response.status(201).build();
    }

    /**
     * 更新好友关系
     * 
     * @param accountId
     * @param token
     * @return
     */
    @Path("/friend/{accountId}/friend")
    @PUT
    public Response FriendRelation(@PathParam("accountId") String accountId,
	    @HeaderParam("Authorization") String token,
	    FriendRelation friendRelation) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}
	String myAccountId = this.getAccountIdOrGuestId(token);

	Friend f = friendService.getByAccountIdAndFriendId(myAccountId,
		accountId);
	if (f == null) {
	    return Response.status(404).build();
	}

	if (friendRelation.getAlias() != null) {
	    f.setRemarkName(friendRelation.getAlias());
	    f.setRemarkNameInPinyin(Pinyin4jUtils.getPinYin(friendRelation
		    .getAlias()));
	}
	if (friendRelation.getFrequently() != null) {
	    f.setFriendType("true".equals(friendRelation.getFrequently()) ? FriendType.Favorite
		    : FriendType.Normal);
	}

	friendService.update(f);
	return Response.status(Status.OK).build();
    }

    /**
     * 查询好友列表
     * 
     * @param token
     * @return
     */
    @Path("/friend")
    @GET
    @Produces({ "application/json" })
    public Response list(@HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}
	String visitAccountId = this.getAccountIdOrGuestId(token);
	List<Account> friends = null;
	friends = friendService.findFriends(visitAccountId);
	List<AccountInfo> fs = InfoConverter.convertToAccounts(friends);

	if (fs != null) {

	    for (AccountInfo accountInfo : fs) {
		setCounterInfo(accountInfo,
			friendService.countByAccountId(accountInfo.getId()),
			friendService.countSharedByAccountId(accountInfo
				.getId()),
			acknowledgmentService.totalByAccountIdAndContent(
				accountInfo.getId(), null));
	    }
	}
	ListResult r = new ListResult();
	AccountInfo[] ais = null;

	if (fs != null) {
	    fs = getFriendInfos(fs, visitAccountId);
	    ais = new AccountInfo[fs.size()];
	    r.setResult(fs.toArray(ais));
	}
	return Response.ok(r).build();
    }

    /**
     * 查询常用联系人列表
     * 
     * @param token
     * @return
     */
    @Path("/friend/frequently")
    @GET
    @Produces({ "application/json" })
    public Response listFriendFrequently(
	    @HeaderParam("Authorization") String token,
	    @QueryParam("limit") int limit,
	    @QueryParam("offset") int offset) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}

	String visitAccountId = this.getAccountIdOrGuestId(token);
	List<Account> friends = null;
	List<AccountInfo> fis = null;
	friends = friendService.findFavoriteFriends(visitAccountId);
	int total = friendService.countByAccountId(visitAccountId);

	fis = InfoConverter.convertToAccounts(friends);

	if (fis != null) {

	    for (AccountInfo accountInfo : fis) {
		setCounterInfo(accountInfo,
			friendService.countByAccountId(accountInfo.getId()),
			friendService.countSharedByAccountId(accountInfo
				.getId()),
			acknowledgmentService.totalByAccountIdAndContent(
				accountInfo.getId(), null));

	    }
	}

	if (limit == 0) {
	    limit = total;
	}
	PagedResult<AccountInfo> sr = new PagedResult<AccountInfo>();
	sr.setResult(getFriendInfos(fis, visitAccountId));
	sr.setLastPage(limit, offset, total);
	sr.setTotal(total);
	return Response.ok(sr).build();
    }

    /**
     * 查询上次联系人列表
     * 
     * @param token
     * @return
     */
    @Path("/friend/last-time-used")
    @GET
    @Produces({ "application/json" })
    public Response listFriendLasttimeUsed(
	    @HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}

	String visitAccountId = this.getAccountIdOrGuestId(token);
	List<Account> friends = null;
	FriendSharedLog friendSharedLog = friendSharedLogService
		.getFriendSharedLog(visitAccountId);

	List<String> friendIds = null;
	if (friendSharedLog != null) {
	    friendIds = friendSharedLog.getFriendId();
	}
	if (friendIds != null) {
	    friends = accountService.findByIdList(friendIds);
	}
	List<AccountInfo> fs = InfoConverter.convertToAccounts(friends);

	if (fs != null) {

	    for (AccountInfo accountInfo : fs) {
		setCounterInfo(accountInfo,
			friendService.countByAccountId(accountInfo.getId()),
			friendService.countSharedByAccountId(accountInfo
				.getId()),
			acknowledgmentService.totalByAccountIdAndContent(
				accountInfo.getId(), null));

	    }
	}
	ListResult r = new ListResult();
	AccountInfo[] ais = null;
	if (fs != null) {
	    fs = getFriendInfos(fs, visitAccountId);
	    ais = new AccountInfo[fs.size()];
	    r.setResult(fs.toArray(ais));
	}
	return Response.ok(r).build();
    }

    /**
     * 查询好友日志
     * 
     * @param token
     * @return
     */
    @Path("/friend-request")
    @GET
    @Produces({ "application/json" })
    public Response listLogs(@HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);

	List<FriendLog> list = friendService.findFriendLog(accountId);
	list = removeFinashed(list, accountId);
	List<FriendLogInfo> fls = null;
	if (list != null && list.size() > 0) {
	    List<Account> accounts = accountService
		    .findByIdList(getFriendList(list));
	    fls = InfoConverter.convertToFriendLogs(list, accounts);
	}
	FriendLogResult r = new FriendLogResult();
	FriendLogInfo[] fis = new FriendLogInfo[fls.size()];
	if (fls != null) {
	    r.setResult(fls.toArray(fis));
	}

	return Response.ok(r).build();
    }

    /**
     * 拒绝好友添加
     * 
     * @param accountId
     * @param token
     * @return
     */
    @Path("/friend-request/{accountId}")
    @DELETE
    public Response reject(@PathParam("accountId") String accountId,
	    @HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}

	String myAccountId = this.getAccountIdOrGuestId(token);

	FriendLog log = friendService.getLogByAccountIdAndFriendId(accountId,
		myAccountId);
	if (log == null) {
	    return Response.status(404).build();
	}
	friendService.rejectFriendRequest(log.getId(), myAccountId);

	return Response.status(Status.OK).build();
    }

    /**
     * 删除好友
     * 
     * @param accountId
     * @param token
     * @return
     */
    @Path("/friend/{accountId}")
    @DELETE
    public Response remove(@PathParam("accountId") String accountId,
	    @HeaderParam("Authorization") String token) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}

	String myAccountId = this.getAccountIdOrGuestId(token);

	Friend f = friendService.getByAccountIdAndFriendId(myAccountId,
		accountId);
	if (f == null) {
	    return Response.status(404).build();
	}

	if (!friendService.removeFriend(myAccountId, accountId, "")) {
	    return Response.status(500).build();
	}

	// 删除好友邮箱白名单
	friendMailConfigService.removeFriendMailConfig(myAccountId, accountId);
	friendMailConfigService.removeFriendMailConfig(accountId, myAccountId);

	return Response.status(Status.OK).build();
    }

    /**
     * 批量更新好友关系
     * 
     * @param token
     * @return
     */
    @Path("/friend")
    @Consumes({ "application/json" })
    @PUT
    public Response updateFriendRelations(
	    @HeaderParam("Authorization") String token,
	    List<FriendRelation> friendRelations) {
	Response valid = this.validAuthorizationAndGuestUser(token);
	if (valid != null) {
	    return valid;
	}
	String accountId = this.getAccountIdOrGuestId(token);

	if (friendRelations == null) {
	    return Response.status(Status.NO_CONTENT).build();
	}

	for (FriendRelation friendRelation : friendRelations) {
	    Friend f = friendService.getByAccountIdAndFriendId(
		    accountId, friendRelation.getAccount());

	    if (f == null) {
		continue;
	    }

	    if (friendRelation.getAlias() != null) {
		f.setRemarkName(friendRelation.getAlias());
		f.setRemarkNameInPinyin(Pinyin4jUtils.getPinYin(friendRelation
			.getAlias()));

	    }
	    if (friendRelation.getFrequently() != null) {
		f.setFriendType("true".equals(friendRelation.getFrequently()) ? FriendType.Favorite
			: FriendType.Normal);

	    }
	    friendService.update(f);
	}
	return Response.status(Status.NO_CONTENT).build();
    }
}
