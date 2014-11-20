package com.easy.bean.init;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.easy.holder.BeanHolder;

public class EasyBeanFactory {

    public static Map<String, BeanHolder> beanHolder = new ConcurrentHashMap<String, BeanHolder>();
    
}
