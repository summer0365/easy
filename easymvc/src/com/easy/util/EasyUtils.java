package com.easy.util;


public class EasyUtils {

	public static String convertClassNameToResourcePath(String className) {
		if (StringUtils.isEmpty(className)) {
			return null;
		}
		return className.replace('.', '/');
	}

}
