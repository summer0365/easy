package com.easy.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EasyAction {

	/**
	 * 配置和action方法关联的URL
	 */
	public String path();

	/**
	 * ，Action接收是Post请求还是Get请求。默认是Post和Get请求
	 * @return
	 */
	public String method() default "";

}
