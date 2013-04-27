/**
 * 
 */
package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuehui.miao
 *
 */
@Controller
@RequestMapping("/square")
public class SquareController {
	@RequestMapping("/home")
	public String goSquareHome(HttpServletRequest request, Model model) {
		return "/content/square/home";
	}
}
