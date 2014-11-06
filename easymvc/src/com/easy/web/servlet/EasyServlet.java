package com.easy.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easy.bean.init.BeanComponentScanBean;
import com.easy.holder.BeanHolder;
import com.easy.init.IComponentScanBean;
import com.easy.web.annotation.MakeAction;
import com.easy.web.init.MvcComponentScanBean;

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
        String actionPackages = (String) getServletContext().getAttribute("easyaction");
        String beanPackages = (String) getServletContext().getAttribute("easybean");

        IComponentScanBean mvcsc = new MvcComponentScanBean();
        IComponentScanBean beansc = new BeanComponentScanBean();
        try {
            mvcsc.handle(actionPackages);
            beansc.handle(beanPackages);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
