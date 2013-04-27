package me.twocoffee.service.fetch;

import java.util.List;

import me.twocoffee.entity.Content;

/**
 * 附件提取
 * 
 * @author chongf
 * 
 */
public interface HtmlAttachmentService {
    /**
     * 从html中提取img标签的src属性
     * 
     * @param html
     * @return
     */
    public List<String> getImages(String url, String html);

    public List<String> clearAndRecieveImageUrl(String url, Content content);

    /**
     * 根据content.htmlPayload.attachment 更新正文中的attachment
     * 
     * @param content
     */
    public void updateAttachments(Content content);

    /**
     * 
     * @param webUrl
     * @param innerURL
     * @return
     */
    public String getAbsoluteURL(String webUrl, String innerURL);
}
