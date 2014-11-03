package com.easy.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easy.holder.BeanHolder;
import com.easy.web.annotation.MakeAction;
import com.easy.web.init.ComponentScanBean;

public class EasyServlet extends HttpServlet {

    private MakeAction makeAction = new MakeAction();

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BeanHolder holder = makeAction.creatAction(request);    
        makeAction.callAction(request, response, holder);
        
    }

    public final void init() {

        String basePackages = (String) getServletContext().getAttribute("easyaction");
        ComponentScanBean sc = new ComponentScanBean();
        try {
            sc.handle(basePackages);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
