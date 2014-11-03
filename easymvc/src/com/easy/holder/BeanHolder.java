package com.easy.holder;

import java.net.URL;

import com.easy.web.service.BeanElement;

public class BeanHolder implements BeanElement {

	public String beanName;
	
	public URL uri;
	
	public Class<?> classz;
	
	public String methodName;
	
	public BeanHolder(){
	}

	public BeanHolder(URL uri){
		this.uri = uri;
	}
	
	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanNames) {
		this.beanName = beanNames;
	}

	public URL getUri() {
		return uri;
	}

	public void setUri(URL uri) {
		this.uri = uri;
	}

	public Class<?> getClassz() {
		return classz;
	}

	public void setClassz(Class<?> classz) {
		this.classz = classz;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
