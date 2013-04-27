package me.twocoffee.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "content", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Content {

    public static enum ContentType {
	/** 内容剪辑， 片段 */
	HtmlClip,
	/** 图片 */
	Image,
	/** 商品 */
	Product,
	/** 网址， 带正文 */
	Web,
	/** 网址，没有正文 */
	Url,
	/** 视频 */
	Video,
	/** 文件 */
	File
    }

    /**
     * 计数器
     * 
     * @author leon
     */
    @Embedded
    public static class Counter {
	/** 被收集的数量 */
	private int collect;
	/** 被评分的数量 */
	private int vote;
	/** 被评论的数量 */
	private int comment;
	/** 被评论的总分 */
	private int totalScore;
	/** 被分享的数量 */
	private int share;
	/** 被访问的数量 */
	private int visit;
	/** 好友答谢数 */
	private int acknowledgment;

	public int getAcknowledgment() {
	    return acknowledgment;
	}

	public int getCollect() {
	    return collect;
	}

	public int getComment() {
	    return comment;
	}

	public int getShare() {
	    return share;
	}

	public int getTotalScore() {
	    return totalScore;
	}

	public int getVisit() {
	    return visit;
	}

	public int getVote() {
	    return vote;
	}

	public void setAcknowledgment(int acknowledgment) {
	    this.acknowledgment = acknowledgment;
	}

	public Counter setCollect(int collect) {
	    this.collect = collect;
	    return this;
	}

	public void setComment(int comment) {
	    this.comment = comment;
	}

	public Counter setShare(int share) {
	    this.share = share;
	    return this;
	}

	public void setTotalScore(int totalScore) {
	    this.totalScore = totalScore;
	}

	public Counter setVisit(int visit) {
	    this.visit = visit;
	    return this;
	}

	public void setVote(int vote) {
	    this.vote = vote;
	}

    }

    @Embedded
    public static class FilePayload {

	private String id;

	private String name;

	private String contentType;

	private Long length;

	private String url;

	private String postfix;

	public String getContentType() {
	    return contentType;
	}

	public String getId() {
	    return id;
	}

	public Long getLength() {
	    return length;
	}

	public String getName() {
	    return name;
	}

	public String getPostfix() {
	    return postfix;
	}

	public String getUrl() {
	    return url;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public void setLength(Long length) {
	    this.length = length;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public void setPostfix(String postfix) {
	    this.postfix = postfix;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

    }

    private FilePayload filePayload;

    /**
     * 如果是Image，会有这个字段
     */
    private ImagePayload imagePayload;

    /** 內容的類型 */
    private ContentType contentType;

    /** 计数器 */
    private Counter counter;

    /** html 内容 */
    private HtmlPayload htmlPayload;

    @Id
    private String id;
    /**
     * 语言，双字母，cn or en
     */
    private String language;
    /** 商品内容, 由于考虑到mongodb中的mapping问题，没有做成继承层次 */
    private ProductPayload productPayload;

    /**
     * 视频内容
     */
    private VideoPayload videoPayload;

    /** 具体的内容 */
    private String summary;

    /**
     * 标题，对应网页、产品的标题
     */
    private String title;

    /**
     * 数据的来源
     */
    @Indexed
    private String url;

    public ContentType getContentType() {
	return contentType;
    }

    public Counter getCounter() {
	return counter;
    }

    public FilePayload getFilePayload() {
	return filePayload;
    }

    public HtmlPayload getHtmlPayload() {
	if (htmlPayload == null)
	    htmlPayload = new HtmlPayload();
	return htmlPayload;
    }

    public String getId() {
	return id;
    }

    public ImagePayload getImagePayload() {
	return imagePayload;
    }

    public String getLanguage() {
	return language;
    }

    public ProductPayload getProductPayload() {
	return productPayload;
    }

    public String getSummary() {
	return summary;
    }

    public String getTitle() {
	return title;
    }

    public String getUrl() {
	return url;
    }

    public VideoPayload getVideoPayload() {
	return videoPayload;
    }

    public Content setCollect(int collect) {
	if (counter == null) {
	    counter = new Counter();
	}
	counter.setCollect(counter.getCollect() + collect);
	if (counter.getCollect() < 0)
	    counter.setCollect(0);
	return this;
    }

    public Content setContentType(ContentType contentType) {
	this.contentType = contentType;
	return this;
    }

    public Content setCounter(Counter counter) {
	this.counter = counter;
	return this;
    }

    public void setFilePayload(FilePayload filePayload) {
	this.filePayload = filePayload;
    }

    public Content setHtmlPayload(HtmlPayload htmlPayload) {
	this.htmlPayload = htmlPayload;
	return this;
    }

    public Content setId(String id) {
	this.id = id;
	return this;
    }

    public Content setImagePayload(ImagePayload imagePayload) {
	this.imagePayload = imagePayload;
	return this;
    }

    public Content setLanguage(String language) {
	this.language = language;
	return this;
    }

    public Content setProductPayload(ProductPayload productPayload) {
	this.productPayload = productPayload;
	return this;
    }

    public Content setSummary(String summary) {
	this.summary = summary;
	return this;
    }

    public Content setTitle(String title) {
	this.title = title;
	return this;
    }

    public Content setUrl(String url) {
	this.url = url;
	return this;
    }

    public void setVideoPayload(VideoPayload videoPayload) {
	this.videoPayload = videoPayload;
    }

    public Content setVisit(int visit) {
	if (counter == null) {
	    counter = new Counter();
	}
	counter.setVisit(counter.getVisit() + visit);
	return this;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	if (null != productPayload) {
	    builder.append("contenttype [").append(contentType)
		    .append("] language [").append(language)
		    .append("] title [")
		    .append(title).append("] url [").append(url)
		    .append("] product [").append(productPayload.toString())
		    .append("]");
	} else {
	    builder.append("contenttype [").append(contentType)
		    .append("] language [").append(language)
		    .append("] title [")
		    .append(title).append("] url [").append(url)
		    .append("]");
	}
	return builder.toString();
    }

}
