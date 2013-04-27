package me.twocoffee.service.fetch.video;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import me.twocoffee.common.SpringContext;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.M3u8;
import me.twocoffee.entity.VideoPayload;
import me.twocoffee.entity.VideoPayload.VideoType;
import me.twocoffee.service.fetch.FetchService;

public class YoukuCrawler implements Crawler {
	private static Pattern youkuPattern = Pattern.compile("^http://v.youku.com/v_show/id_([^\\s]+).html", Pattern.CASE_INSENSITIVE);
	
	private M3u8Parser parser = new M3u8Parser();
	
	private FetchService fetchService = null;
	
	@Override
	public boolean isMine(String url) {
		Matcher matcher = youkuPattern.matcher(url);
		return matcher.find();
	}

	public Content getContent(String url) {
		if (fetchService == null) {
			fetchService = (FetchService)SpringContext.getBean("fetchServiceImpl");
		}
		
		String html = fetchService.getStringResource(url);
		String json = fetchService.getStringResource(getInfoUrl(url));
		
		Content content = new Content();
		content.setContentType(Content.ContentType.Video);
		content.setUrl(url);
		content.setTitle(getTitle(url, html));
		VideoPayload videoPayload = new VideoPayload();
		videoPayload.setVideoType(getVideoType());
		
		if (videoPayload.getVideoType() == VideoPayload.VideoType.M3u8) {
			videoPayload.setM3u8Content(getM3u8ByUrl(url));
			if (videoPayload.getM3u8Content() == null)
				return null;
		}
		else {
			videoPayload.setPhonePlayerUrl(getPhonePlayer(url));
			if (videoPayload.getPhonePlayerUrl() == null)
				return null;
		}
		videoPayload.setWebPlayerUrl(getWebPlayer(url));
		videoPayload.setWebPlayerContent(getWebPlayerContent(url, html));
		videoPayload.setImageUrl(getImageUrl(url, json));
		
		content.setVideoPayload(videoPayload);
		if (videoPayload.getImageUrl() != null) {
			content.setSummary("<img src=\"" + videoPayload.getImageUrl() + "\" />");
		}
		return content;
	}

	private String getInfoUrl(String url) {
		String infoFormat = "http://v.youku.com/player/getPlayList/VideoIDS/%s";
		return String.format(infoFormat, getIdByUrl(url));
	}

	private static Pattern titlePattern = Pattern.compile("<meta[^>]*name=\"title\"[^>]*content=[\"']?([^>'\"]*)", Pattern.CASE_INSENSITIVE);
	private static Pattern embedPattern = Pattern.compile("(<embed.*</embed>)", Pattern.CASE_INSENSITIVE);
	
	private String getTitle(String url, String html) {
		Matcher matcher = titlePattern.matcher(html);
		if (matcher.find())
			return matcher.group(1);
		
		throw new IllegalArgumentException(url + " not found title.");
	}
	
	private VideoType getVideoType() {
		return VideoType.M3u8;
	}

	private String getWebPlayer(String url) {
		String webUrlFormat = "http://player.youku.com/player.php/sid/%s/v.swf";
		return String.format(webUrlFormat, getIdByUrl(url));
	}
	
	private String getWebPlayerContent(String url, String html) {
		Matcher matcher = embedPattern.matcher(html);
		if (matcher.find()) {
			String embed = matcher.group(1);
			String key = "<embed ";
			embed = embed.substring(0,embed.indexOf(key)+key.length()) + " wmode=\"transparent\" " + embed.substring(embed.indexOf(key)+key.length());
			return embed;
		}
			
		
		String webUrlFormat = "http://player.youku.com/player.php/sid/%s/v.swf";
		String webUrl = String.format(webUrlFormat, getIdByUrl(url));
		
		StringBuffer sb = new StringBuffer();
		sb.append("<embed src=\"");
		sb.append(webUrl);
		sb.append("\" quality=\"high\" width=\"480\" height=\"400\" align=\"middle\" allowScriptAccess=\"always\" allowFullScreen=\"true\" wmode=\"transparent\" type=\"application/x-shockwave-flash\"></embed>");
		System.out.println("" +sb.toString());
		return sb.toString();
	}

	private String getIdByUrl(String url) {
		Matcher matcher = youkuPattern.matcher(url);
		if (!matcher.find())
			return null;
		return matcher.group(1);
	}

	private M3u8 getM3u8ByUrl(String url) {
		String m3u8Format = "http://v.youku.com/player/getRealM3U8/vid/%s//video.m3u8";
		String m3u8Url = String.format(m3u8Format, getIdByUrl(url));
		//return parser.parse(m3u8Url);
		M3u8 m = new M3u8();
		m.setUrl(m3u8Url);
		return m;
	}

	private String getPhonePlayer(String url) {
		return null;
	}

	private static Pattern logoPattern = Pattern.compile("logo[^:]*:\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
	
	private String getImageUrl(String url, String json) {
		Matcher matcher = logoPattern.matcher(json);
		if (!matcher.find())
			return null;
		return matcher.group(1).replace("\\", "");
	}
	
	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	private String revert(String str){
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i < str.length() - 6;)
		{
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++)
			{
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar)
				{
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}
}
