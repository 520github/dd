/**
 * 
 */
package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.twocoffee.common.BaseController;
import me.twocoffee.common.mail.Mail;
import me.twocoffee.common.mail.SendMail;

/**
 * @author xuehui.miao
 *
 */
@Controller
@RequestMapping("/mail")
public class MailController extends BaseController {
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String sendMail(HttpServletRequest request, Model model) {
		
		return "/mail/send";
	}
	
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String sendMailDo(Mail mail,HttpServletRequest request, Model model) {
		SendMail send = new SendMail(mail);
		String mes = send.sendMail();
		model.addAttribute("mail", mail);
		model.addAttribute("errorMes", mes);
		return "/mail/send";
	}
}
