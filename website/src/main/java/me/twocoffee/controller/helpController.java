package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class helpController {

	@RequestMapping("/us")
	public String out(HttpServletRequest request, Model model, String out) {
		return "help/help";
	}
}
