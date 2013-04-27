package me.twocoffee.service.fetch.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Content;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.fetch.HtmlAttachmentService;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HtmlAttachmentServiceImpl implements
	HtmlAttachmentService {

    @Autowired
    private ContentService contentService;
    protected static final Logger LOGGER = LoggerFactory
	    .getLogger(HtmlAttachmentServiceImpl.class);

    /**
     * 解析字符串
     * 
     * @param inputHTML
     *            String
     * @return Parser
     */
    public Parser createParser(String inputHTML) {
	Lexer mLexer = new Lexer(new Page(inputHTML));
	return new Parser(mLexer, new DefaultParserFeedback(
		DefaultParserFeedback.QUIET));

    }

    // 相对路径转绝对路径
    private static String makeAbsoluteURL(String strWeb, String innerURL) {
	// TODO Auto-generated method stub
	if (innerURL != null
		&& (innerURL.toLowerCase().startsWith("http://") || (innerURL
			.toLowerCase().startsWith("https://")))) {
	    return innerURL;
	}
	if (StringUtils.isBlank(strWeb)) {
	    return null;
	}
	URL linkUri = null;
	try {
	    linkUri = new URL(new URL(strWeb), innerURL);
	} catch (MalformedURLException e) {
	    LOGGER.error(e.getMessage(), e);
	    return null;
	}
	return linkUri.toString();
    }

    @Override
    public List<String> getImages(String url, String html) {
	if (html == null || html.trim().length() < 1) {
	    return null;
	}
	Map<String, Integer> imgMap = new HashMap<String, Integer>();
	List<String> imgList = new ArrayList<String>();
	try {
	    Parser parser = createParser(html);
	    parser.reset();
	    NodeFilter img = new NodeClassFilter(ImageTag.class);
	    NodeList list = parser.extractAllNodesThatMatch(img);
	    for (int i = 0; i < list.size(); i++) {
		ImageTag image = (ImageTag) list.elementAt(i);
		if (((image.getAttribute("real_src") != null) && (image
			.getAttribute("src") != null))) {

		    imgMap.put(image
			    .getAttribute("real_src"), 1);

		} else {
		    imgMap.put(
			    makeAbsoluteURL(url,
				    image.getImageURL()),
			    1);

		}
	    }

	    Iterator<String> it = imgMap.keySet().iterator();
	    while (it.hasNext()) {
		String key = it.next().toString();
		imgList.add(key);
	    }
	} catch (ParserException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return imgList;
    }

    @Override
    public void updateAttachments(Content content) {
	// TODO replaceAll attachment in htmlPayload content
	// TODO save content
	List<Attachment> attachmentList = content.getHtmlPayload()
		.getAttachment();

	Map<String, String> srcMap = new HashMap<String, String>();
	for (Attachment attachment : attachmentList) {
	    if (attachment == null) {
		continue;
	    }
	    srcMap.put(attachment.getOrgUrl(), attachment.getArchiveUrl());
	}
	try {
	    String html = modifyTagUrl(content.getHtmlPayload().getContent(),
		    srcMap, content.getUrl());
	    content.setHtmlPayload(content.getHtmlPayload().setContent(html));
	} catch (ParserException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	contentService.save(content);
    }

    public String modifyTagUrl(String document, Map<String, String> srcMap,
	    String webUrl)
	    throws ParserException {
	CssSelectorNodeFilter nodeFilter = new CssSelectorNodeFilter("img");
	Parser parser = Parser.createParser(document, "utf-8");
	NodeList list = parser.parse(null);
	String html;
	try {
	    html = modifyUrl(list, nodeFilter, srcMap, webUrl).toHtml();
	} catch (MalformedURLException e) {
	    LOGGER.error(e.getMessage(), e);
	    return document;
	}
	return html;

    }

    private NodeList modifyUrl(NodeList list, CssSelectorNodeFilter nodeFilter,
	    Map<String, String> srcMap, String webUrl)
	    throws MalformedURLException {
	if (list == null) {
	    return null;
	}

	Node node = null;
	SimpleNodeIterator iterator = list.elements();
	while (iterator.hasMoreNodes()) {
	    node = iterator.nextNode();
	    if (node == null) {
		break;
	    }
	    if (node instanceof Tag) {
		Tag tag = (Tag) node;
		if (nodeFilter.accept(tag)) {
		    if (((tag.getAttribute("real_src") != null) && (tag
			    .getAttribute("src") != null))) {
			String realSrc = tag.getAttribute("real_src");
			tag.setAttribute("src", srcMap
				.get(makeAbsoluteURL(webUrl, realSrc)));

		    } else {
			String link = tag.getAttribute("src");
			if (link != null) {
			    tag.setAttribute("src",
				    srcMap.get(makeAbsoluteURL(
					    webUrl, link)));
			}
		    }
		}
		modifyUrl(node.getChildren(), nodeFilter, srcMap, webUrl);
	    }
	}

	return list;
    }

    @Override
    public String getAbsoluteURL(String webUrl, String innerURL) {
	String absoluteURL = innerURL;
	absoluteURL = makeAbsoluteURL(webUrl, innerURL);
	return absoluteURL;
    }

    @Override
    public List<String> clearAndRecieveImageUrl(String url, Content content) {
	String html = content.getHtmlPayload().getContent();

	if (html == null || html.trim().length() < 1) {
	    return null;
	}
	Map<String, Integer> imgMap = new HashMap<String, Integer>();
	List<String> imgList = new ArrayList<String>();

	try {
	    Parser parser = createParser(html);
	    parser.reset();
	    NodeFilter img = new NodeClassFilter(ImageTag.class);
	    NodeList list = parser.parse(null);
	    getImageUrlAndModifyNode(url, imgMap, img, list);
	    content.getHtmlPayload().setContent(list.toHtml());
	    Iterator<String> it = imgMap.keySet().iterator();

	    while (it.hasNext()) {
		String key = it.next().toString();
		imgList.add(key);
	    }

	} catch (ParserException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return imgList;
    }

    private void getImageUrlAndModifyNode(String url,
	    Map<String, Integer> imgMap, NodeFilter img, NodeList list) {

	if (list == null) {
	    return;
	}

	for (int i = 0; i < list.size(); i++) {
	    Node imgNode = list.elementAt(i);

	    if (imgNode == null) {
		return;
	    }

	    if (img.accept(imgNode)) {
		ImageTag image = (ImageTag) imgNode;

		if (((image.getAttribute("real_src") != null) && (image
			.getAttribute("src") != null))) {

		    String unescapedHtml = StringEscapeUtils
			    .unescapeHtml(image.getAttribute("real_src"));

		    imgMap.put(unescapedHtml, 1);
		    image.setAttribute("real_src", unescapedHtml);

		} else {
		    String unescapedHtml = StringEscapeUtils
			    .unescapeHtml(image.getImageURL());

		    String absoluteUrl = makeAbsoluteURL(url,
			    unescapedHtml);

		    if (absoluteUrl != null) {
			imgMap.put(absoluteUrl
				,
				1);

			image.setImageURL(absoluteUrl);
		    }
		}

	    } else {
		getImageUrlAndModifyNode(url, imgMap, img,
			imgNode.getChildren());

	    }
	}
    }
}
