package me.twocoffee.service.weixin.handle;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;

@Service("saveLinkWeixinHandle")
public class SaveLinkWeixinHandle extends AbstractSaveContentWeixinHandle {
	@Override
	protected void setPostContent(WeiXinPublicRequestEntity request) {
		String url = StringUtils.trimToEmpty(request.getUrl());
		if(StringUtils.isBlank(url)) {
			url = StringUtils.trimToEmpty(request.getContent());
		}
		String referer = url.indexOf("/")==-1?url:url.substring(0, url.indexOf("/"));
		postContent.setTitle(request.getTitle());
        Map contentMap = contentService.getContentByUrl(ContentType.Web.toString(), url, referer);
        if(contentMap != null && contentMap.get("id") != null) {
        	postContent.setId((String)contentMap.get("id"));
        }
	}

}
