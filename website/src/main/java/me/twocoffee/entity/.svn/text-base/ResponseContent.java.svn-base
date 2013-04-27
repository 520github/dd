package me.twocoffee.entity;

import java.util.ArrayList;
import java.util.List;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Content.Counter;
import me.twocoffee.entity.Content.FilePayload;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.VideoPayload.VideoType;
import me.twocoffee.service.entity.ContentDetail;
import me.twocoffee.service.entity.ContentDetail.From;
import me.twocoffee.service.entity.ContentDetail.ResponseComment;

import org.apache.commons.lang.StringUtils;

public class ResponseContent {
    public static class File {
	private String id;
	private String name;
	private String contentType;
	private Long length;
	private String url;
	private String extension;

	public String getContentType() {
	    return contentType;
	}

	public String getExtension() {
	    return extension;
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

	public String getUrl() {
	    return url;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public void setExtension(String extension) {
	    this.extension = extension;
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

	public void setUrl(String url) {
	    this.url = url;
	}
    }

    public static class Image {
	private String id;
	/** 图片的网址 */
	private String url;
	/** 图片文件的大小。单位是byte */
	private int length;
	/** 图片的宽度， 单位是像素 */
	private int width;
	/** 图片的高度， 单位是像素 */
	private int height;
	private String name;
	private String mimeType;

	public int getHeight() {
	    return height;
	}

	public String getId() {
	    return id;
	}

	public int getLength() {
	    return length;
	}

	public String getMimeType() {
	    return mimeType;
	}

	public String getName() {
	    return name;
	}

	public String getUrl() {
	    return url;
	}

	public int getWidth() {
	    return width;
	}

	public void setHeight(int height) {
	    this.height = height;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public void setLength(int length) {
	    this.length = length;
	}

	public void setMimeType(String mimeType) {
	    this.mimeType = mimeType;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	public void setWidth(int width) {
	    this.width = width;
	}
    }

    public static class Product {
	/** 数量 */
	private int amount;
	/** 商品的图片网址 */
	private String picture;
	/** 商品图片文件的尺寸。单位是byte */
	private String name;
	/** 价格， 主要精度 */
	private String price;

	public int getAmount() {
	    return amount;
	}

	public String getName() {
	    return name;
	}

	public String getPicture() {
	    return picture;
	}

	public String getPrice() {
	    return price;
	}

	public void setAmount(int amount) {
	    this.amount = amount;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public void setPicture(String picture) {
	    this.picture = picture;
	}

	public void setPrice(String price) {
	    this.price = price;
	}

    }

    public static class ResponseAttachment {
	private String url;
	private String fileName;
	private long size;
	private String contentType;

	public String getContentType() {
	    return contentType;
	}

	public String getFileName() {
	    return fileName;
	}

	public long getSize() {
	    return size;
	}

	public String getUrl() {
	    return url;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public void setSize(long size) {
	    this.size = size;
	}

	public void setUrl(String url) {
	    this.url = url;
	}
    }

    public static class ResponseScore {
	private float value;
	private int vote;

	public float getValue() {
	    return value;
	}

	public int getVote() {
	    return vote;
	}

	public void setValue(float value) {
	    this.value = value;
	}

	public void setVote(int vote) {
	    this.vote = vote;
	}
    }

    public static class Video {

	/**
	 * web端播放器详细信息
	 */
	private String embed;

	private List<VideoContent> stream;

	private String thumbnail;

	public String getEmbed() {
	    return embed;
	}

	public List<VideoContent> getStream() {
	    return stream;
	}

	public String getThumbnail() {
	    return thumbnail;
	}

	public void setEmbed(String embed) {
	    this.embed = embed;
	}

	public void setStream(List<VideoContent> stream) {
	    this.stream = stream;
	}

	public void setThumbnail(String thumbnail) {
	    this.thumbnail = thumbnail;
	}
    }

    public static class VideoContent {
	private String contentType;
	private String url;

	public String getContentType() {
	    return contentType;
	}

	public String getUrl() {
	    return url;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

    }

    public static class Web {
	private String content;
	private List<ResponseAttachment> attachment;

	public List<ResponseAttachment> getAttachment() {
	    return attachment;
	}

	public String getContent() {
	    return content;
	}

	public void setAttachment(List<ResponseAttachment> attachment) {
	    this.attachment = attachment;
	}

	public void setAttachment_trans(List<Attachment> attachmentList) {
	    List<ResponseAttachment> responseAttachmentList = new ArrayList<ResponseAttachment>();

	    if (attachmentList != null) {
		int index = 0;
		for (Attachment attachment : attachmentList) {
		    ResponseAttachment responseAttachment = new ResponseAttachment();
		    responseAttachment.setContentType(attachment
			    .getContentType());
		    responseAttachment.setSize(attachment.getSize());
		    responseAttachment.setFileName(String.valueOf(index));
		    responseAttachmentList.add(responseAttachment);
		    responseAttachment.setUrl(StringUtils.isBlank(attachment
			    .getArchiveUrl()) ? attachment.getOrgUrl()
			    : attachment.getArchiveUrl());

		    index++;
		}
	    }
	    this.attachment = responseAttachmentList;
	}

	public void setContent(String content) {
	    this.content = content;
	}
    }

    // contentId
    private String id;
    private String responseId;
    private boolean delete = false;
    private String url;
    private String title;
    private List<String> tag;
    private List<String> userTag;
    private String date;
    private String lastModified;
    private String summary;
    private ResponseComment comment;
    private ResponseScore score;
    private List<From> from = new ArrayList<From>();
    private Counter counter;
    private String shareLink;
    private Product product;

    private Web web;

    private List<PrivateMessageSession> chatSession;

    /**
     * 类型
     */
    private SystemTagEnum type;

    /**
     * 收藏来源
     */
    private SystemTagEnum source;

    /**
     * folder列表
     */
    private List<String> folders;

    /** 收藏日期国际化 */
    private String date_i18n;

    private Image image;

    private Video video;

    private File file;

    /** 列表要显示的图片url(可能是：商品图片、就一张图片、文章中的第一张图片) */
    private String imageUrl;

    public String getShareLink() {
	return shareLink;
    }

    public void setShareLink(String shareLink) {
	this.shareLink = shareLink;
    }

    private ResponseComment getComment(Comment comment) {
	ResponseComment responseComment = new ResponseComment();
	if (comment.getLastComment() != null) {
	    responseComment
		    .setDate(DateUtil.FormatDateUTC(comment.getLastComment()));
	}
	responseComment.setScore(comment.getScore());
	responseComment.setText(comment.getText());
	return responseComment;
    }

    public List<PrivateMessageSession> getChatSession() {
	return chatSession;
    }

    public ResponseComment getComment() {
	return comment;
    }

    public Counter getCounter() {
	return counter;
    }

    public String getDate() {
	return date;
    }

    public String getDate_i18n() {
	return date_i18n;
    }

    public File getFile() {
	return file;
    }

    public List<String> getFolders() {
	return folders;
    }

    public List<From> getFrom() {
	return from;
    }

    public String getId() {
	return id;
    }

    public Image getImage() {
	return image;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public String getLastModified() {
	return lastModified;
    }

    public Product getProduct() {
	return product;
    }

    public String getResponseId() {
	return responseId;
    }

    public ResponseScore getScore() {
	return score;
    }

    public SystemTagEnum getSource() {
	return source;
    }

    public String getSummary() {
	return summary;
    }

    public List<String> getTag() {
	return tag;
    }

    public String getTitle() {
	return title;
    }

    public SystemTagEnum getType() {
	return type;
    }

    public String getUrl() {
	return url;
    }

    public List<String> getUserTag() {
	return userTag;
    }

    public Video getVideo() {
	return video;
    }

    public Web getWeb() {
	return web;
    }

    public boolean isDelete() {
	return delete;
    }

    public void setChatSession(List<PrivateMessageSession> chatSession) {
	this.chatSession = chatSession;
    }

    public void setComment(ResponseComment comment) {
	this.comment = comment;
    }

    public void setComment_trans(Comment comment) {
	this.comment = getComment(comment);
    }

    public void setCounter(Counter counter) {
	this.counter = counter;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public void setDate_i18n(String dateI18n) {
	date_i18n = dateI18n;
    }

    public void setDelete(boolean delete) {
	this.delete = delete;
    }

    public void setFile(File file) {
	this.file = file;
    }

    public void setFile_trans(FilePayload filePayload) {
	if (filePayload == null)
	    return;
	File file = new File();
	file.setId(filePayload.getId());
	file.setName(filePayload.getName());
	file.setContentType(filePayload.getContentType());
	file.setLength(filePayload.getLength());
	file.setUrl(filePayload.getUrl());
	file.setExtension(filePayload.getPostfix());
	this.file = file;
    }

    public void setFolders(List<String> folders) {
	this.folders = folders;
    }

    public void setFrom(List<From> from) {
	this.from = from;
    }

    public void setFrom_trans(From from) {
	this.from.add(from);
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setImage(Image image) {
	this.image = image;
    }

    public void setImage_trans(ImagePayload imagePayload) {
	if (imagePayload != null) {
	    Image image = new Image();
	    image.setHeight(imagePayload.getHeight());
	    image.setLength(imagePayload.getLength());
	    image.setUrl(imagePayload.getUrl());
	    image.setWidth(imagePayload.getWidth());
	    image.setMimeType(imagePayload.getMimeType());
	    image.setName(imagePayload.getName());
	    image.setId(imagePayload.getId());
	    this.image = image;
	}
    }

    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    public void setLastModified(String lastModified) {
	this.lastModified = lastModified;
    }

    public void setProduct(Product product) {
	this.product = product;
    }

    public void setProduct_trans(ProductPayload productPayload) {
	if (productPayload != null) {
	    Product product = new Product();
	    product.setAmount(productPayload.getAmount());
	    product.setName(productPayload.getName());
	    product.setPicture(productPayload.getArchivePicture());
	    if (product.getPicture() == null
		    || product.getPicture().trim().length() < 1) {
		product.setPicture(productPayload.getPicture());
	    }
	    product.setPrice(productPayload.getPrice());
	    this.product = product;
	}
    }

    public void setResponseId(String responseId) {
	this.responseId = responseId;
    }

    public void setScore(ResponseScore score) {
	this.score = score;
    }

    public void setScore_trans(ContentDetail contentDetail) {
	if (contentDetail != null) {
	    ResponseScore responseScore = new ResponseScore();
	    if (null != contentDetail.getContent().getCounter()
		    && contentDetail.getContent().getCounter().getVote() > 0) {
		responseScore.setValue(contentDetail.getContent().getCounter()
			.getTotalScore()
			/ contentDetail.getContent().getCounter().getVote());
		responseScore
			.setVote(contentDetail.getContent().getCounter()
				.getVote());
	    } else {
		responseScore.setValue(0);
		responseScore.setVote(0);
	    }

	    this.score = responseScore;
	}
    }

    public void setSource(SystemTagEnum source) {
	this.source = source;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public void setTag(List<String> tag) {
	this.tag = tag;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setType(SystemTagEnum type) {
	this.type = type;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public void setUserTag(List<String> userTag) {
	this.userTag = userTag;
    }

    public void setVideo(Video video) {
	this.video = video;
    }

    public void setVideo_trans(VideoPayload videoPayload) {
	if (videoPayload == null)
	    return;
	Video video = new Video();
	video.setEmbed(videoPayload.getWebPlayerContent());
	List<VideoContent> videoContent = new ArrayList<VideoContent>();
	VideoContent vc = new VideoContent();
	if (videoPayload.getVideoType().equals(VideoType.M3u8)) {
	    vc.setContentType("application/vnd.apple.mpegurl");
	}
	vc.setUrl(videoPayload.getM3u8Content().getUrl());
	videoContent.add(vc);
	video.setStream(videoContent);
	video.setThumbnail(videoPayload.getImageUrl());
	this.video = video;
    }

    public void setWeb(Web web) {
	this.web = web;
    }

}
