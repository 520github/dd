package me.twocoffee.service.weixin.handle;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import me.twocoffee.entity.ImagePayload;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.service.weixin.WeiXinPublicRequestEntity;

@Service("saveImageContentWeixinHandle")
public class SaveImageContentWeixinHandle extends
		AbstractSaveContentWeixinHandle {

	@Override
	protected void setPostContent(WeiXinPublicRequestEntity request) {
		String imageUrl = StringUtils.trimToEmpty(request.getImageUrl());
		String referer = imageUrl.indexOf("/")==-1?imageUrl:imageUrl.substring(0, imageUrl.indexOf("/"));
        Map contentMap = contentService.getContentByUrl(ContentType.Image.toString(), imageUrl, referer);
        if(contentMap != null && contentMap.get("id") != null) {
        	postContent.setId((String)contentMap.get("id"));
        }
		ImagePayload imagePayload = new ImagePayload();
		
		String mimeType = imageUrl.lastIndexOf(".")==-1 ? "" : imageUrl.substring(imageUrl.lastIndexOf(".")+1);
		String name = imageUrl.lastIndexOf("/")==-1 ? "" : imageUrl.substring(imageUrl.lastIndexOf("/")+1);;
		imagePayload.setUrl(imageUrl);
		imagePayload.setMimeType(mimeType);
		imagePayload.setName(name);
		postContent.setImagePayload(imagePayload);
	}

}
