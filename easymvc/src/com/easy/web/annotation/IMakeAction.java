package com.easy.web.annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easy.holder.BeanHolder;

public interface IMakeAction {
    
    public BeanHolder creatAction(HttpServletRequest request);
    
    public void callAction(HttpServletRequest request, HttpServletResponse response,
            BeanHolder holder);

}
