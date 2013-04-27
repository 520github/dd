package me.twocoffee.service.entity;

import java.util.ArrayList;
import java.util.List;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.common.util.FileImageUtil;
import me.twocoffee.entity.Comment;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.Repository.FriendShare;
import me.twocoffee.entity.SystemTagEnum;

public class ContentDetail implements Comparable<ContentDetail> {
    public static class Avatar {
	private String large;
	private String medium;
	private String small;

	public String getLarge() {
	    return large;
	}

	public String getMedium() {
	    return medium;
	}

	public String getSmall() {
	    return small;
	}

	public void setLarge(String large) {
	    this.large = large;
	}

	public void setMedium(String medium) {
	    this.medium = medium;
	}

	public void setSmall(String small) {
	    this.small = small;
	}
    }

    public static class From {

	private String accountId;
	private String date;
	private String name;
	private ResponseComment comment;
	private Avatar avatar;
	private boolean acknowledgment;

	public String getAccountId() {
	    return accountId;
	}

	public Avatar getAvatar() {
	    return avatar;
	}

	public ResponseComment getComment() {
	    return comment;
	}

	public String getDate() {
	    return date;
	}

	public String getName() {
	    return name;
	}

	public boolean isAcknowledgment() {
	    return acknowledgment;
	}

	public void setAccountId(String accountId) {
	    this.accountId = accountId;
	}

	public void setAcknowledgment(boolean acknowledgment) {
	    this.acknowledgment = acknowledgment;
	}

	public void setAvatar(Avatar avatar) {
	    this.avatar = avatar;
	}

	public void setAvatar_trans(Avatar avatar) {
	    this.avatar = avatar;
	}

	public void setComment(ResponseComment comment) {
	    this.comment = comment;
	}

	public void setComment_trans(Comment comment) {
	    if (comment != null) {
		ResponseComment responseComment = new ResponseComment();
		this.setDate(DateUtil.FormatDateUTC(comment
			.getLastComment()));
		responseComment.setScore(comment.getScore());
		responseComment.setText(comment.getText());
		this.comment = responseComment;
	    }
	}

	public void setComment_trans2(FriendShare friend) {
	    if (friend == null) {
		return;
	    }
	    ResponseComment responseComment = new ResponseComment();
	    responseComment.setScore(friend.getScore());
	    responseComment.setText(friend.getComment());
	    this.comment = responseComment;
	}

	public void setDate(String date) {
	    this.date = date;
	}

	public void setName(String name) {
	    this.name = name;
	}

    }

    public static class ResponseComment {
	private int score;
	private String text;
	private String date;

	public String getDate() {
	    return date;
	}

	public int getScore() {
	    return score;
	}

	public String getText() {
	    return text;
	}

	public void setDate(String date) {
	    this.date = date;
	}

	public void setScore(int score) {
	    this.score = score;
	}

	public void setText(String text) {
	    this.text = text;
	}
    }

    private Repository repository;

    private Content content;

    /** 收藏日期国际化 */
    private String date_i18n;
    /** 列表要显示的图片url(可能是：商品图片、就一张图片、文章中的第一张图片) */
    private String imageUrl;
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

    /**
     * comment列表
     */
    private List<Comment> comments;

    /** 来自好友分享列表 */
    private List<From> from = new ArrayList<From>();

    public ContentDetail() {

    }

    public ContentDetail(Repository r, Content c) {
	repository = r;
	content = c;
    }

    @Override
    public int compareTo(ContentDetail o) {

	if (this.getRepository().getLastModified().getTime() < (o
		.getRepository().getLastModified().getTime() / 1000) * 1000) {
	    return 1;
	} else if (this.getRepository().getLastModified().getTime() > (o
		.getRepository().getLastModified().getTime() / 1000) * 1000) {
	    return -1;
	}
	return 0;
    }

    public List<Comment> getComments() {
	return comments;
    }

    public Content getContent() {
	return content;
    }

    public String getDate_i18n() {
	return date_i18n;
    }

    public List<String> getFolders() {
	return folders;
    }

    public List<From> getFrom() {
	return from;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public Repository getRepository() {
	return repository;
    }

    public SystemTagEnum getSource() {
	return source;
    }

    public SystemTagEnum getType() {
	return type;
    }

    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    public void setContent(Content content) {
	this.content = content;
	this.setImageUrl(null);
    }

    public void setDate_i18n(String dateI18n) {
	date_i18n = dateI18n;
    }

    public void setFolders(List<String> folders) {
	this.folders = folders;
    }

    public void setFrom(List<From> from) {
	this.from = from;
    }

    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
	if (imageUrl != null && imageUrl.trim().length() > 0) {
	    return;
	}
	if (content == null)
	    return;
	if (content.getContentType() == null)
	    return;
	if (ContentType.Image.equals(content.getContentType())) {// 图片
	    if (content.getImagePayload() != null) {
		this.imageUrl = content.getImagePayload().getUrl();
	    }
	} 
	else if(ContentType.File.equals(content.getContentType())) {
		String fileType = content.getFilePayload() == null?"":content.getFilePayload().getPostfix();
		this.imageUrl = FileImageUtil.getFileTypePath(fileType);
	}
	else if (ContentType.Product.equals(content.getContentType())) {// 商品
	    if (content.getProductPayload() != null) {
		this.imageUrl = content.getProductPayload().getArchivePicture();
		if (this.imageUrl == null || this.imageUrl.trim().length() < 1) {
		    this.imageUrl = content.getProductPayload().getPicture();
		}
	    }
	} 
	else if(ContentType.Video.equals(content.getContentType())
			&& content.getVideoPayload()!= null) {
		this.imageUrl = content.getVideoPayload().getImageUrl();
	}
	else if (ContentType.HtmlClip.equals(content.getContentType())
		|| ContentType.Web.equals(content.getContentType())
		|| ContentType.Url.equals(content.getContentType())) {// 正文、片段、url情况
	    if (content.getHtmlPayload() != null) {
		List<Attachment> list = content.getHtmlPayload()
			.getAttachment();
		if (list != null && list.size() > 0) {
		    Attachment atta = list.get(0);
		    if (atta != null)
			this.imageUrl = atta.getArchiveUrl();
		}
	    }
	}
	if (this.imageUrl == null
		|| this.imageUrl.trim().length() == 0)
	    this.imageUrl = "";
    }

    public void setRepository(Repository repository) {
	this.repository = repository;
    }

    public void setSource(SystemTagEnum source) {
	this.source = source;
    }

    public void setType(SystemTagEnum type) {
	this.type = type;
    }
}
