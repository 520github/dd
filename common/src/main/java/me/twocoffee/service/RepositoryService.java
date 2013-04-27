package me.twocoffee.service;

import java.util.List;
import java.util.Map;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.entity.PublicRepository;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.StatisticTag;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.entity.ContentAndAccount;
import me.twocoffee.service.entity.ContentDetail;

public interface RepositoryService {

    /**
     * 清除所有朋友会话内的消息数
     * 
     * @param accountId
     *            会话拥有者id
     */
    void clearAllFriendSessionBadge(String accountId);

    /**
     * 清除朋友会话内的消息数
     * 
     * @param targets
     *            会话拥有者id,会话的内容和会话目标
     */
    void clearFriendSessionBadge(List<ContentAndAccount> targets);

    /**
     * 得到分享数
     * 
     * @param id
     * @return
     */
    int countSharedByAccountId(String id);

    /**
     * 删除某个收藏关系
     * 
     * @param id
     */
    void delete(String id);

    /**
     * 根据contentId和accountId获取内容详情信息
     * 
     * @param contentId
     * @param accountId
     * @return
     */
    ContentDetail findContentDetailByContentIdAndAccountId(String contentId,
	    String accountId);

    /**
     * 获取一个repositoryId的详情信息
     * 
     * @param id
     * @return
     */
    ContentDetail findContentDetailById(String id);

    /**
     * 从repositoryId列表得到内容的详细信息
     * 
     * @param repositoryIdList
     * @return
     */
    List<ContentDetail> findContentDetailsById(List<String> repositoryIdList);

    /**
     * 获取某个用户的收藏列表
     * 
     * @param accountId
     * @return
     */
    List<Repository> findRepositoryByAccountId(String accountId);

    /**
     * 从repositoryId列表得到内容的详细信息
     * 
     * @param repositoryIdList
     * @return
     */
    List<ContentDetail> findSharedContentDetailsById(
	    List<String> repositoryIdList);

    /**
     * 根据id获取收藏关系
     * 
     * @param id
     * @return
     */
    Repository getById(String id);

    /**
     * 得到某个账户的最近分享的内容
     * 
     * @param accountId
     * @param orderby
     * @param PageSize
     * @param PageNum
     * @return
     */
    List<ContentDetail> getContentDetailList(String accountId, String orderby,
	    int PageSize,
	    int PageNum);

    /**
     * 得到最旧的数据
     * 
     * @param id
     * @return
     */
    Repository getLatestByAccount(String id);

    /**
     * 根据内容ID及AccoutId获取PublicRepository对象
     * 
     * @param contentId
     * @param accountId
     * @return
     */
    PublicRepository getPublicRepositoryByContentIdAndAccountId(
	    String contentId, String accountId);

    /**
     * 根据内容ID及AccoutId获取Repository对象
     * 
     * @param contentId
     * @param accountId
     * @return
     */
    Repository getRepositoryByContentIdAndAccountId(String contentId,
	    String accountId);

    /**
     * 得到folder系统标签
     * 
     * @param r
     * @return
     */
    List<String> getRepositoryFolders(Repository r);

    /**
     * 得到来源系统标签
     * 
     * @param r
     * @return
     */
    SystemTagEnum getRepositorySource(Repository r);

    /**
     * 得到类型标签
     * 
     * @param r
     * @return
     */
    SystemTagEnum getRepositoryType(Repository r);

    List<ContentDetail> getSharedByAccountId(String id, String orderby,
	    int PageSize, int PageNum);

    /**
     * 重新获取系统标签
     * 
     * @param newTagList
     * @param oldTagList
     * @param menuType
     * @return
     */
    List<String> getTag(List<String> newTagList, List<String> oldTagList,
	    String menuType);

    /**
     * 重新获取系统标签
     * 
     * @param newTagList
     * @param oldTagList
     * @param menuType
     * @return
     */
    List<String> getTag(String newTag, List<String> oldTagList, String menuType);

    /**
     * 获取谋篇文章前N个最热的标签列表
     * 
     * @param contentId
     * @param limit
     * @return
     */
    List<String> getTopTagListByContentId(String contentId, int limit);

    /**
     * 获取谋篇文章前N个最热的标签Map
     * 
     * @param contentId
     * @param limit
     * @return
     */
    Map<String, String> getTopTagMapByContentId(String contentId, int limit);

    /**
     * 获取谋篇文章前N个最热的标签字符串
     * 
     * @param contentId
     * @param joinStr
     * @param limit
     * @return
     */
    String getTopTagStrByContentId(String contentId, String joinStr, int limit);

    /**
     * 判断当前是否存在此系统标签
     * 
     * @param r
     * @param tag
     * @return
     */
    boolean hasSystemTag(Repository r, SystemTagEnum tag);

    /**
     * 根据contentId和accoutnId判断是否已存在对应的PublicRepository
     * 
     * @param contentId
     * @param accountId
     * @return
     */
    boolean isContainPublicRepository(String contentId, String accountId);

    /**
     * 根据contentId和accoutnId判断是否已存在对应的Repository
     * 
     * @param contentId
     * @param accoutnId
     * @return
     */
    boolean isContainRepository(String contentId, String accoutnId);

    /**
     * 判断待查找的tag是否在系统tag中存在
     * 
     * @param tagList
     * @param tag
     * @return
     */
    boolean isContainTag(List<String> tagList, String tag);

    /**
     * 判断新的tag是否已经存在于原有的tag中
     * 
     * @param newTagList
     * @param oldTagList
     * @return
     */
    boolean isExistTag(List<String> newTagList, List<String> oldTagList);

    /**
     * 删除某个用户的某个标签
     * 
     * @param accountId
     * @param tag
     */
    void removeTagByAccountIdAndTag(String accountId, String tag);

    /**
     * 保存
     * 
     * @param repository
     */
    void save(Repository repository);

    /**
     * 保存至公共关系
     * 
     * @param repository
     */
    void savePublicRepository(PublicRepository repository);

    /**
     * 根据某个用户统计文章相关tag的数量
     * 
     * @param accountId
     * @return
     */
    List<StatisticTag> statisticTagList(String accountId);

    /**
     * 根据某个用户统计文章相关tag的数量
     * 
     * @param accountId
     * @return
     */
    Map<String, StatisticTag> statisticTagMap(String accountId);

    /**
     * 更新朋友间关于内容的评论数
     * 
     */
    void updateFriendSessionBadge(PrivateMessageSession source,
	    PrivateMessageSession target);

    /**
     * 更新某个用户的某个标签
     * 
     * @param accountId
     * @param oldTag
     * @param newTag
     */
    void updateTagByAccountIdAndTag(String accountId, String oldTag,
	    String newTag);

    public List<Repository> getAllRepository();

    /**
     * 根据多个系统标签获取Repository列表
     * 
     * @param systemTagList
     * @param offset
     * @param limit
     * @return
     */
    List<Repository> getRepositoryBySystemTags(List<String> systemTagList,
	    int offset, int limit);

    /**
     * 根据多个系统标签和多个不存在的系统标签获取Repository列表
     * 
     * @param systemTagList
     * @param notSystemTagList
     * @param offset
     * @param limit
     * @return
     */
    List<Repository> getRepositoryBySystemTags(List<String> systemTagList,
	    List<String> notSystemTagList, int offset, int limit);

    /**
     * 条件搜索
     * 
     * @param accountId
     * @param tag
     * @param userTag
     * @param offset
     * @param limit
     * @return
     */
    public PagedResult search(String accountId, String tag,
	    String userTag, String friendId, int offset, int limit);

    void updateShareLink(Repository dr);
}
