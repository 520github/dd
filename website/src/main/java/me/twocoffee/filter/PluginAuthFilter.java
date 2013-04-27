package me.twocoffee.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class PluginAuthFilter extends AuthFilter {

	private VelocityEngine velocityEngine;
	private Random random = new Random();

	@Override
	protected void showLoginView(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		Map model = new HashMap();
		int seed = random.nextInt();
		model.put("seed", seed);

		String view = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,
				LocaleContextHolder.getLocale()
						+ loginPage,
				"utf-8", model);
		view = view.replaceAll("\r\n", "");
		view = view.replaceAll("\n", "");

		Map responseEntity = new HashMap();
		responseEntity.put("seed", seed);
		responseEntity.put("showLogin", true);
		responseEntity.put("view",view);

		response.setContentType("application/x-javascript;charset=utf-8");

		response.getWriter().write(
				request.getParameter("callback") + "("
						+ JSONObject.fromObject(responseEntity).toString()
						+ ")");
	}
	@Override
	protected void showDomainView(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String setDomainPage="/views/clipper/bookmark/set_domain.html";
		String view = VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine,
				LocaleContextHolder.getLocale()
						+ setDomainPage,
				"utf-8", null);
		
		view = view.replaceAll("\r\n", "");
		view = view.replaceAll("\n", "");
		
		response.setContentType("application/x-javascript;charset=utf-8");
		
		Map responseEntity = new HashMap();
		responseEntity.put("view",view);


		response.getWriter().write(
				request.getParameter("callback") + "("
						+ JSONObject.fromObject(responseEntity).toString()
						+ ")");
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		velocityEngine = (VelocityEngine) applicationContext
				.getBean("velocityEngine");
	}
}
