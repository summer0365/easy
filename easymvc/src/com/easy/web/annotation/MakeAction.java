package com.easy.web.annotation;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easy.holder.BeanHolder;
import com.easy.util.LogUtil;
import com.easy.util.StringUtils;
import com.easy.web.init.MvcComponentScanBean;

public class MakeAction {

    private static String projectName;

    private static boolean isSet = false;

    public BeanHolder creatAction(HttpServletRequest request) {
        String servletPath = request.getRequestURI();

        if (!StringUtils.isEmpty(projectName) && isSet) {
            servletPath.replace(projectName, "");
        } else {
            setProjectName(request.getContextPath());
            if (!StringUtils.isEmpty(projectName)) {
                servletPath.replace(projectName, "");
            }
        }
        Map<String, BeanHolder> holderMap = MvcComponentScanBean.holder;
        BeanHolder actionHandleObj = holderMap.get(servletPath);
        return actionHandleObj;
    }

    public void callAction(HttpServletRequest request, HttpServletResponse response,
            BeanHolder holder) {
        if (holder == null) {
            LogUtil.error("holder is null!");
            return;
        }
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

    public static String getProjectName() {
        return projectName;
    }

    public static synchronized void setProjectName(String projectName) {
        if (isSet) {
            return;
        }
        isSet = true;
        if (StringUtils.isEmpty(MakeAction.projectName) && !StringUtils.isEmpty(projectName)) {
            MakeAction.projectName = projectName;
        }
    }

}
