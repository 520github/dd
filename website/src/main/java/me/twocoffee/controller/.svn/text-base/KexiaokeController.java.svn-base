package me.twocoffee.controller;

import javax.servlet.http.HttpServletRequest;

import me.twocoffee.dao.ContentDao;
import me.twocoffee.entity.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ke")
public class KexiaokeController {

	@Autowired
	private ContentDao contentDao;

	@RequestMapping("/xiaoke")
	public ResponseEntity<String> out(HttpServletRequest request, Model model,
			String title,
			String summary,
			String content, String code, String id) {
		if (code != null && code.equals("babeeta2012514") && id != null) {
			Content ct = contentDao.getById(id);
			if (ct != null) {
				if (title != null && !title.equals("")) {
					ct.setTitle(title);
				}
				if (summary != null && !summary.equals("")) {
					ct.setSummary(summary);
				}
				ct.getHtmlPayload().setContent(content);
				contentDao.updateContent(ct);
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
