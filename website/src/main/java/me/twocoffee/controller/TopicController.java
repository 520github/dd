package me.twocoffee.controller;

import javax.ws.rs.core.Response;

import me.twocoffee.common.BaseController;
import me.twocoffee.service.rpc.TopicRpcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TopicController extends BaseController {
	@Autowired
	private TopicRpcService topicService;
	
	@RequestMapping(value= "/topic/isList",method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> isList(){
		int status = topicService.isList();
		return buildJSONResult("code", status+"");
		
	}
	
}
