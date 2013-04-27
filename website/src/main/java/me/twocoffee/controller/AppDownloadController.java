package me.twocoffee.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppDownloadController {

	@RequestMapping("/download")
	public void dev(HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null || userAgent.equals("")) {
			response.sendRedirect("/download/duoduowp.xat");
		} else if (userAgent.contains("iphone") || userAgent.contains("iPhone")) {
			//response.sendRedirect("/download/duoduoIphone.ipa");
			//response.sendRedirect("itms-services://?action=download-manifest&url=http://www.mduoduo.com/download/app_1.1.0.plist");
			response.sendRedirect("https://itunes.apple.com/cn/app/duo-duo/id567772073");
		} else if (userAgent.contains("android")
				|| userAgent.contains("Android")
				|| userAgent.contains("linux")
				|| userAgent.contains("Linux")) {
			response.sendRedirect("/download/duoduoAndroid.apk");
		} else if (userAgent.contains("ipad") || userAgent.contains("iPad")) {
			// response.sendRedirect("/download/duoduoIpad.ipa");
			// 临时使用
			response.sendRedirect("itms-services://?action=download-manifest&url=http://www.mduoduo.com/download/app_1.1.0.plist");
		} else {
			response.sendRedirect("/download/duoduowp.xat");
		}
		return;
	}

}
