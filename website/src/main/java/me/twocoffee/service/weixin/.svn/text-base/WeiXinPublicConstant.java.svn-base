package me.twocoffee.service.weixin;

import me.twocoffee.common.constant.SystemConstant;

public class WeiXinPublicConstant {
	public  static String domainName = "http://"+SystemConstant.domainName;
	public  static String Hello2BizUser = "Hello2BizUser";
	public  static String HELP = "h";
	public  static String DOWNLOAD = "d";
	public  static String MORE_CONTENT_TIP = "更多内容";
	public  static String GetLastestContent = "m";//
	public  static String WEIXIN_UNKNOWN_CONTENT = "对不起，多多收藏暂时不支持当前类型内容的收藏！";
	public  static String EMPTY_CONTENT_TIP = "你的多多帐号还没有收藏内容哦！";
	public  static String WEIXIN_CONTENT_SAVE_SUCCESS = "恭喜，内容收藏成功！";
	public  static String WEIXIN_CONTENT_SAVE_FAILURE = "亲，内容收藏失败了，试试重新再来一次！";
	public  static int WEIXIN_TITLE_MAX_LENGTH = 20;
	public  static int WEIXIN_CONTENT_MAX_LENGTH = 64;
	public  static int REPEAT_COMMAND_NUM = 10;
	public  static String WEIXIN_TEMPLATE_ID = "#id#";
	public  static String WEIXIN_TEMPLATE_REPOSITORY_ID = "#repositoryId#";
	public  static String WEIXIN_THIRDPARTY_CALLBACK_URL_PRE = "/weixin/thirdparty/callback/";
	public  static String HTML_LEFT = "<";//&lt;
	public  static String HTML_RIGHT = ">";//&gt;
	public  static String HTML_A_LEFT = HTML_LEFT +"a";
	public  static String HTML_A_RIGHT = HTML_LEFT +"/a"+HTML_RIGHT;
	public  static String LIEN = "\n\n";
	public  static String LIEN_ONE = "\n";
	public  static String WEIXIN_BIND_TEXT = "绑定帐号";
	public  static String WEIXIN_REGISTER_TEXT = "注册帐号";
	
	public  static String WEIXIN_LOGIN_BIND_URL = domainName+"/weixin/"+WEIXIN_TEMPLATE_ID;
	public  static String WEIXIN_LOGIN_BIND_URL_A = HTML_A_LEFT+" href=\""+WEIXIN_LOGIN_BIND_URL+"\""+HTML_RIGHT+""+WEIXIN_BIND_TEXT+""+HTML_A_RIGHT;
	public  static String WEIXIN_REGISTER_BIND_URL = domainName+"/weixin/register/"+WEIXIN_TEMPLATE_ID;
	public  static String WEIXIN_REGISTER_BIND_URL_A = HTML_A_LEFT+" href=\""+WEIXIN_REGISTER_BIND_URL+"\""+HTML_RIGHT+""+WEIXIN_REGISTER_TEXT+""+HTML_A_RIGHT;
	public  static String WEIXIN_CONTENT_ITEM_URL = domainName+"/weixin/content/item/"+WEIXIN_TEMPLATE_ID+"/"+WEIXIN_TEMPLATE_REPOSITORY_ID;
	public  static String WEIXIN_CONTENT_LIST_URL = domainName+"/weixin/content/list/"+WEIXIN_TEMPLATE_ID;
	public  static String WEIXIN_CONTENT_LIST_URL_A = HTML_A_LEFT+" href=\""+WEIXIN_CONTENT_LIST_URL+"\" target=\"_blank\""+HTML_RIGHT+"点击我"+HTML_A_RIGHT;;
	public  static String WEIXIN_DOWNLOAD_URL = domainName + "/minisite/mobile/clientIndex.html";
	public  static String WEIXIN_DOWNLOAD_URL_A = HTML_A_LEFT+" href=\""+WEIXIN_DOWNLOAD_URL+"\""+HTML_RIGHT+"下载多多客户端"+HTML_A_RIGHT;;
	
	public  static String WEIXIN_TEMPLATE_MESSAGE = "多多可以帮您收藏微信或手机里的有用信息。"+LIEN
											        +"方法：绑定“多多收藏”帐号，发送想要收藏的信息给“多多收藏”公众帐号，收藏成功"+LIEN_ONE
											        +"(将复制的链接发给我,我会直接抽取正文帮您保存,方便您查看^_^)"+LIEN
											        +"绑定多多收藏帐号\n请点击:"+WEIXIN_LOGIN_BIND_URL_A+LIEN
											        +"注册多多收藏帐号\n请点击："+WEIXIN_REGISTER_BIND_URL_A+LIEN_ONE
											        +"回复“m”查看已收藏信息"+LIEN_ONE
											        +"回复“d”下载多多客户端"+LIEN_ONE
											        +"回复“h”获取帮助菜单。"+LIEN_ONE;
	
	public  static String WEIXIN_TEMPLATE_BIND_TIP = "欢迎关注多多收藏！"+LIEN
		                                             + WEIXIN_TEMPLATE_MESSAGE;		                                             
	
	public  static String WEIXIN_TEMPLATE_HELP_TIP = WEIXIN_TEMPLATE_MESSAGE;
	
	public  static String WEIXIN_UN_SUBSCRIBE_TIP = "亲,您已经取消关系我的多多了,我们欢迎您常回来看看!";
	
	public static enum ServiceType {
		Hello,
		SaveText,
		SaveImage,
		SaveLink,
		GetLastestContent,
		Unsubscribe,
		Help,
		Download,
		Unknown,
	}
	public static enum RequestType {
		defaultWeixinRequest,
	}
	
	public static enum HandleType {
		generateBindWeixinHandle,
		saveTextContentWeixinHandle,
		saveImageContentWeixinHandle,
		saveLinkWeixinHandle,
		getLastestContentWeixinHandle,
		unsubscribeWeixinHandle,
		helpWeixinHandle,
		downloadWeixinHandle,
		unknownWeixinHandle
	}
	
	public static enum ResponseType {
		defaultWeixinResponse
	}
	
	public static enum BindMessage {
		success,
		failure
	}
}
