/**
 * 
 */
package me.twocoffee.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.twocoffee.common.BasicHttpClient;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.service.fetch.impl.FetchServiceImpl;

import org.apache.commons.httpclient.URI;
import org.apache.commons.io.IOUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author momo
 * 
 */
public class HttpClientUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(FetchServiceImpl.class);

	public static String getStringResource(String url) {
		String resource = null;

		try {
			HttpGet httpGet = new HttpGet(url);
			resource = getStringResource(url, httpGet);

		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		return resource;
	}

	public static String getStringResource(String url,
			HttpRequest req) {

		String resource = "";
		logger.info("Fetching {}.", url);

		try {
			HttpResponse httpReponse = BasicHttpClient.getHttpClient().execute(
					(HttpUriRequest) req);

			int statusCode = httpReponse.getStatusLine().getStatusCode();

			if (statusCode == 200) {
				HttpEntity httpEntity = httpReponse.getEntity();
				resource = getContent(httpEntity);
				logger.info("Fetch succeed,{}.", url);

			} else {
				logger.info("Fetch status code:{},{}.", statusCode, url);
			}

		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		return resource;
	}

	private static String getContent(HttpEntity httpEntity)
			throws ParseException,
			IOException {

		String encoding = getHttpEncoding(httpEntity);

		if (encoding != null)
			return EntityUtils.toString(httpEntity, encoding);

		String header = getHeaderString(httpEntity, "ISO-8859-1");
		encoding = getCharset(header);
		String r = new String(header.getBytes("ISO-8859-1"), encoding);
		r += EntityUtils.toString(httpEntity, encoding);
		return r;
	}

	private static String getHttpEncoding(HttpEntity httpEntity) {
		String encoding = null;

		if (httpEntity.getContentEncoding() != null
				&& httpEntity.getContentEncoding().getValue() != null) {

			encoding = httpEntity.getContentEncoding().getValue();

		} else if (httpEntity.getContentType() != null) {
			HeaderElement values[] = httpEntity.getContentType().getElements();

			if (values.length > 0) {
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

	/**
	 * 得到html头部分
	 * 
	 * @param httpEntity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static String getHeaderString(HttpEntity httpEntity,
			String defaultEncoding)
			throws IllegalStateException, IOException {

		int len = 1500;
		byte[] buff = new byte[len];
		httpEntity.getContent().read(buff);
		return new String(buff, defaultEncoding);
	}

	/**
	 * 得到html编码
	 * 
	 * @param html
	 * @return
	 */
	private static String getCharset(String html) {
		Pattern pattern = Pattern.compile(
				"<meta.*?charset=[\"']*?([^\";]*)[\"']*?.*?>",
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

	public static String requestAndStoreResource(HttpClient httpclient,
			String target, FileOperator fileOperator) {

		String url = null;
		InputStream instream = null;

		try {
			URI uri = new URI(target, false);
			HttpGet httpGet = new HttpGet(uri.toString());
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
				logger.info("Fetching staus code:{}.{}", statusCode,
						uri.toString());

			}

		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());

		} catch (IOException e) {
			logger.error(e.getMessage());

		} finally {
			IOUtils.closeQuietly(instream);
		}
		return url;
	}
}
