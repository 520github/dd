package me.twocoffee.common.webbase;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class BaseExceptionHandler implements HandlerExceptionResolver {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		log.warn("Handle exception:: " + ex.getClass().getName());

		//output exception to string buffer
		StringWriter sbr = new StringWriter();
		ex.printStackTrace(new PrintWriter(sbr));
		
		// only show debug info in development env.
		request.setAttribute("detailErrorMessage", sbr.toString());
		log.error(sbr.toString());
		return new ModelAndView("common/error");
	}
}
