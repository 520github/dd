package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * 文档队列
 * 
 * @author chongf
 * 
 */
@Entity(value = "docQueue", noClassnameStored = true)
@Indexes({
	// 用于一般的队列查询
	@Index("processing, -date"),
	// 用于重置队列
	@Index("fetcher, processing")
})
public class DocQueue {
    private String contentId;// 存放contentId
    private Date date;// 创建时间

    private String fetcher;// 保存服务的ID，serviceId，标记是哪个服务抓取的

    @Id
    private String id;// 唯一性标记

    private boolean processing;// 正在处理

    public String getContentId() {
	return contentId;
    }

    public Date getDate() {
	return date;
    }

    public String getFetcher() {
	return fetcher;
    }

    public String getId() {
	return id;
    }

    public boolean isProcessing() {
	return processing;
    }

    public void setContentId(String contentId) {
	this.contentId = contentId;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public void setFetcher(String fetcher) {
	this.fetcher = fetcher;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setProcessing(boolean processing) {
	this.processing = processing;
    }

}
