package me.twocoffee.service.fetch.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.twocoffee.common.BasicHttpClient;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.dao.ContentDao;
import me.twocoffee.dao.DocQueueDao;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.DocQueue;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.HtmlPayload.Attachment;
import me.twocoffee.entity.ProductPayload;
import me.twocoffee.service.ConfigurationService;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.fetch.HtmlAttachmentService;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchServiceImpl implements FetchService {

    private static final Logger logger = LoggerFactory
	    .getLogger(FetchServiceImpl.class);

    public final static String SERVICE_ID;

    static {
	String hostName = "default";
	String servicePath = "";
	try {
	    java.net.InetAddress localMachine = java.net.InetAddress
		    .getLocalHost();
	    servicePath = new File(".").getCanonicalPath();
	    hostName = localMachine.getHostName();
	} catch (IOException e) {
	    logger.error(e.getMessage());
	} finally {
	    SERVICE_ID = hostName + ":" + servicePath;
	}
    }

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private DocQueueDao docQueueDao;

    // 线程池的大小过大 本以为mongodb的连接数等于线程数*主机连接数，但现在看来并非如此，经测试50左右现在不会报连接异常
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 200,
	    10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()) {
    };

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private HtmlAttachmentService htmlAttachmentService;

    @Autowired
    private ConfigurationService configurationService;

    private void fetchResources(List<HtmlPayload.Attachment> attachments) {
	HttpClient httpclient = BasicHttpClient.getHttpClientThreadLocal();
	for (HtmlPayload.Attachment a : attachments) {
	    String url = requestAndStoreResource(httpclient, a.getOrgUrl());
	    a.setArchiveUrl(url);
	}
    }

    private String getContent(HttpEntity httpEntity) throws ParseException,
	    IOException {
	String encoding = getHttpEncoding(httpEntity);
	if (encoding != null) {
	    return EntityUtils.toString(httpEntity, encoding);
	}

	String header = getHeaderString(httpEntity, "ISO-8859-1");
	encoding = getCharset(header);
	if (encoding == null || encoding.equals("")) {
	    encoding = "utf-8";
	}

	String r = new String(header.getBytes("ISO-8859-1"), encoding);
	r += EntityUtils.toString(httpEntity, encoding);
	return r;
    }

    /**
     * 得到html头部分
     * 
     * @param httpEntity
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    private String getHeaderString(HttpEntity httpEntity, String defaultEncoding)
	    throws IllegalStateException, IOException {
	int len = 1500;
	byte[] buff = new byte[len];
	httpEntity.getContent().read(buff);
	return new String(buff, defaultEncoding);
    }

    private String getHttpEncoding(HttpEntity httpEntity) {
	String encoding = null;
	if (httpEntity.getContentEncoding() != null
		&& httpEntity.getContentEncoding().getValue() != null) {
	    encoding = httpEntity.getContentEncoding().getValue();
	} else if (httpEntity.getContentType() != null) {
	    HeaderElement values[] = httpEntity.getContentType().getElements();
	    if (values.length > executor.getCorePoolSize()) {
		NameValuePair param = values[0].getParameterByName("charset");
		if (param != null) {
		    encoding = param.getValue();
		}
	    }
	}

	if (encoding != null && encoding.toLowerCase().equals("gb2312")) {
	    encoding = "gbk";
	}
	return encoding;
    }
    
    private String requestAndStoreResource(HttpClient httpclient, String target) {
    	return this.requestAndStoreResource(httpclient, target, null);
    }
    private String requestAndStoreResource(HttpClient httpclient, String target,String source) {
	String url = null;

	URI uri = null;
	try {
	    uri = new URI(target, true);
	} catch (URIException e1) {
	    logger.error(e1.getMessage());
	} catch (NullPointerException e1) {
	    logger.error(e1.getMessage());
	}
	if (uri == null) {
	    return null;
	}
	HttpGet httpGet = new HttpGet(uri.toString());
	if(StringUtils.isNotBlank(source)) {
		httpGet.setHeader("referer", source);
	}
	InputStream instream = null;
	try {
	    logger.info("Fetching.{}", uri.toString());
	    HttpResponse response = httpclient.execute(httpGet);
	    HttpEntity entity = response.getEntity();
	    int statusCode = response.getStatusLine().getStatusCode();
	    if (statusCode == 200 && entity != null) {
		logger.info("Fetching succeed.{}", uri.toString());
		if (entity.getContentLength() < 1) {
		    logger.error("Fetched error:content length < 1.",
			    uri.toString());
		    return null;
		}
		instream = entity.getContent();
		url = fileOperator.putFile(instream, entity.getContentLength(),
			uri.getName());
		url = fileOperator.getFileUrl(url);
	    } else {
		httpGet.abort();
		logger.info("Fetching staus code:{}.{}", statusCode,
			uri.toString());
	    }

	} catch (IOException e) {
	    httpGet.abort();
	    logger.error(e.getMessage());
	} finally {
	    if (instream != null) {
		try {
		    instream.close();
		} catch (IOException e) {
		    logger.error(e.getMessage());
		}
	    }
	}
	return url;
    }

    /**
     * 得到html编码
     * 
     * @param html
     * @return
     */
    protected String getCharset(String html) {
	Pattern pattern = Pattern.compile(
		"<meta.*?charset=[\"']*([^\";]*)[\"']*?.*?>",
		Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(html);
	String encoding = "utf-8";
	if (matcher.find()) {
	    encoding = matcher.group(1);
	}

	if (encoding != null && encoding.toLowerCase().equals("gb2312")) {
	    encoding = "gbk";
	}
	return encoding;
    }

    @Override
    public void addToFetch(Content content) {
	DocQueue doc = new DocQueue();
	doc.setContentId(content.getId());
	doc.setProcessing(false);
	docQueueDao.save(doc);
    }

    @Override
    public String getResource(String url) {
	return getResource(url,"");
    }
    
    @Override
    public String getResource(String url, String sourceUrl) {
    	HttpClient httpClient = BasicHttpClient.getHttpClient();
    	return requestAndStoreResource(httpClient, url,sourceUrl);
    }

    @Override
    public String getStringResource(String url) {
	String resource = null;
	int index = url.lastIndexOf("#");
	if (index >= 0) {
	    url = url.substring(0, index);
	}
	HttpGet httpGet = new HttpGet(url);
	httpGet.setHeader("User-Agent",
		"Mozilla/5.0 (Windows NT 6.1; rv:10.0.1) Gecko/20100101 Firefox/10.0.1");
	try {
	    logger.info("Fetching {}.", url);
	    HttpResponse httpReponse = BasicHttpClient.getHttpClient().execute(
		    httpGet);
	    int statusCode = httpReponse.getStatusLine().getStatusCode();
	    if (statusCode == 200) {
		HttpEntity httpEntity = httpReponse.getEntity();
		resource = getContent(httpEntity);
		logger.info("Fetch succeed,{}.", url);
	    } else {
		httpGet.abort();
		logger.info("Fetch status code:{},{}.", statusCode, url);
	    }

	} catch (Exception e1) {
	    httpGet.abort();
	    logger.error(e1.getMessage());
	}
	return resource;
    }

    public void init() throws Exception {
	final Date latestFetchDate;

	if (StringUtils.isBlank(configurationService.getLatestFetchDate())) {
	    latestFetchDate = new Date();
	} else {
	    latestFetchDate = DateUtil.parse("yyyy-MM-dd HH:mm:ss",
		    configurationService.getLatestFetchDate());
	}
	// 启动时重置所有队列
	docQueueDao.resetQueueByServiceId(SERVICE_ID);
	// 启动线程池
	logger.info("Fetcher executor thread starting...",
		executor.getCorePoolSize());
	for (int i = 0; i < executor.getCorePoolSize(); i++) {
	    executor.execute(new Runnable() {
		@Override
		public void run() {
		    while (true) {
			try {
			    DocQueue doc = null;
			    synchronized (this) {
				doc = docQueueDao.peek(SERVICE_ID);
				if (doc == null) {
				    wait(5000);// 如果队列为空，wait
				    continue;
				}
			    }
			    logger.info("Peek doc contentId:{} to fetch.",
				    doc.getContentId());
			    Content content = contentDao.getById(doc
				    .getContentId());
			    if (content == null) {
				logger.info(
					"Doc contentId:{} disappeared,What happened?",
					doc.getContentId());
				continue;
			    }
			    if (content.getContentType().compareTo(
				    ContentType.Product) == 0) {
				// TODO fetch ProductPayload.picture 抓取产品的图片
				ProductPayload productPayload = content
					.getProductPayload();
				if (productPayload != null) {
				    String url = getResource(productPayload
					    .getPicture());
				    if (url != null) {
					productPayload.setPicture(url);
				    }
				    contentDao.save(content);
				} else {
				    logger.info("Here happened a strange thing!Content.productPayload is null!");
				}
			    } else {
				// 抓取网页正文或者文摘中的附件(更新内容中的url链接)
				if (content.getHtmlPayload() != null) {
				    List<HtmlPayload.Attachment> attachments = content
					    .getHtmlPayload().getAttachment();

				    if (attachments != null
					    && attachments.size() > 0) {

					if (doc.getDate().before(
						latestFetchDate)) {
					    resetContent(content);

					} else {
					    fetchResources(attachments);
					    content.getHtmlPayload()
						    .setAttachment(
							    attachments);

					    htmlAttachmentService
						    .updateAttachments(content);
					}
				    }

				} else {
				    logger.info("Here happened a strange thing!Content.htmlPayload is null!");
				}
			    }
			    docQueueDao.delete(doc);
			    logger.info(
				    "Doc content Id:{} has fetched,now dequeue.",
				    doc.getContentId());

			} catch (Exception e) {
			    logger.error(e.getMessage());
			}
		    }
		}
	    });
	}
	logger.info("Fetcher executor thread pool starting completed,size:{}",
		executor.getCorePoolSize());
    }

    @Override
    public void shutdown() {
	logger.info("Shutting down Fetcher ThreadPoolExecutor Pool");
	executor.shutdown();
    }

    public void resetContent(Content content) {
	logger.info("reset content {}", content.getId());

	try {

	    if (content.getHtmlPayload() != null
		    && content.getHtmlPayload().getAttachment() != null
		    && content.getHtmlPayload().getAttachment().size() > 0) {

		String html = content.getHtmlPayload().getContent();
		List<String> imagesFromContent = fetchImages(html);
		List<Attachment> needToAdds = new ArrayList<Attachment>(
			2);

		for (String image : imagesFromContent) {
		    boolean hasImageInAttachment = false;

		    for (Attachment attachment : content
			    .getHtmlPayload().getAttachment()) {

			if (attachment.getArchiveUrl().equals(image)) {
			    hasImageInAttachment = true;
			    break;
			}
		    }

		    if (!hasImageInAttachment) {
			Attachment atta = new Attachment();
			atta.setArchiveUrl(image);
			needToAdds.add(atta);
		    }
		}

		if (needToAdds.size() > 0) {
		    content.getHtmlPayload().getAttachment()
			    .addAll(needToAdds);

		    logger.info("need reset!");

		} else {
		    logger.info("need not reset!");
		}
	    }

	} catch (Exception e) {
	    logger.error("reset error!");
	}
    }

    private List<String> fetchImages(String html) throws Exception {
	List<String> images = new ArrayList<String>();
	CssSelectorNodeFilter nodeFilter = new CssSelectorNodeFilter("img");
	Parser parser = Parser.createParser(html, "utf-8");
	NodeList list = parser.parse(null);
	fetchNode(images, nodeFilter, list);
	return images;
    }

    private void fetchNode(List<String> images,
	    CssSelectorNodeFilter nodeFilter, NodeList list) {

	Node node = null;

	if (list == null) {
	    return;
	}
	SimpleNodeIterator iterator = list.elements();

	while (iterator.hasMoreNodes()) {
	    node = iterator.nextNode();

	    if (node == null) {
		break;
	    }

	    if (node instanceof Tag) {
		Tag tag = (Tag) node;

		if (nodeFilter.accept(tag)) {
		    String link = tag.getAttribute("src");

		    if (link != null && !"".equals(link)
			    && link.startsWith("http")) {
			images.add(link);
		    }
		}
		fetchNode(images, nodeFilter, node.getChildren());
	    }
	}
    }
}
