package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController {

	@RequestMapping("/us")
	public String about(HttpServletRequest request, Model model) {
		return "about/about";
	}

	// 商务合作
	@RequestMapping("/business")
	public String business(HttpServletRequest request, Model model) {
		return "about/business";
	}

	@RequestMapping("/plugtool")
	public String plugtool(String toolType, HttpServletRequest request,
			Model model) {
		// if (toolType == null || toolType.trim().length() < 1) {
		// toolType = "plug";
		// }
		model.addAttribute("toolType", toolType);
		return "about/plugtool";
	}

	// 招聘
	@RequestMapping("/recruitment")
	public String recruitment(HttpServletRequest request, Model model) {
		return "about/recruitment";
	}

	@RequestMapping("/tools")
	public String tools(HttpServletRequest request, Model model) {
		return "about/tools";
	}
}
