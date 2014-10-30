package com.easy.web.servlet;

import javax.servlet.http.HttpServlet;

import com.easy.web.init.ComponentScanBean;

public class EasyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final void init() {

		String basePackages = (String) getServletContext().getAttribute("easyaction");
		ComponentScanBean sc = new ComponentScanBean();
		try {
			sc.handle(basePackages);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
