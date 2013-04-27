package me.twocoffee.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppController {
	
	@RequestMapping("firefox")
	public String onekeyFirefox(HttpServletRequest request, Model model) {
		model.addAttribute("title", request.getParameter("title"));
		String url = request.getParameter("href");
		try {
			if(url != null)url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		model.addAttribute("url", url);
		String content = request.getParameter("text");
		if(content == null) {
			content = request.getParameter("imgsrc");
		}
		if(content == null)content = "";
		content = content.replaceAll("\r\n", "<br>");
		model.addAttribute("content", content);
		return "app/onekeyfirefox/onekeyfirefoxhome";
	}
	
	@RequestMapping("firefox/add")
	public String onekeyFirefoxAdd(HttpServletRequest request, Model model) {
		return "app/onekeyfirefox/onekeyfirefoxindex";
	}
	
	@RequestMapping("firefox/list")
	public String onekeyFirefoxList(HttpServletRequest request, Model model) {
		return "app/onekeyfirefox/onekeyfirefoxlist";
	}
	
	@RequestMapping("firefox/detail")
	public String onekeyFirefoxDetail(HttpServletRequest request, Model model) {
		model.addAttribute("id", request.getParameter("id"));
		return "app/onekeyfirefox/onekeyfirefoxdetail";
	}
}
