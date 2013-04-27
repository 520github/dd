package me.twocoffee.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.service.ContentSearcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("file")
public class FileController {
	@Autowired
	private FileOperator fileOperator = null;
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
		String id = null;
		try {
			id = fileOperator.putFile(new File("D:\\tmp\\1.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (id == null)
			return "put error.";
					
		String url = fileOperator.getFileUrl(id);
		return url;
	}
}
