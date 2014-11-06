package com.easy.web.annotation;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easy.holder.BeanHolder;
import com.easy.web.init.MvcComponentScanBean;

public class MakeAction {

    public BeanHolder creatAction(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        Map<String, BeanHolder> holderMap = MvcComponentScanBean.holder;
        BeanHolder actionHandleObj = holderMap.get(servletPath);
        return actionHandleObj;
    }

    public void callAction(HttpServletRequest request, HttpServletResponse response,
            BeanHolder holder) {

        try {
            String actionClassName = holder.getBeanName();
            String methodName = holder.getMethodName();
            Class<?> actionClass = Class.forName(actionClassName);
            Method method = actionClass.getClass().getMethod(methodName, HttpServletRequest.class,
                    HttpServletResponse.class);
            method.invoke(actionClass, request, response);
        } catch(Exception e) {
            e.printStackTrace();

        }
    }

}
