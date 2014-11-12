package com.easy.web.init;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.easy.holder.BeanHolder;
import com.easy.init.BaseComponentScanBean;
import com.easy.init.IComponentScanBean;
import com.easy.util.Assert;
import com.easy.util.EasyResource;
import com.easy.util.EasyUtils;
import com.easy.util.StringUtils;
import com.easy.web.annotation.EasyAction;

public class MvcComponentScanBean extends BaseComponentScanBean implements IComponentScanBean {

    public static Map<String, BeanHolder> holder = new HashMap<String, BeanHolder>();

    public void handle(String... basePackages) throws Exception {
        synchronized (this) {

            Assert.notNull(basePackages, "basePackages is null");
            for (String basePackage : basePackages) {
                String packageSearchPath = EasyResource.CLASSPATH_ALL_URL_PREFIX
                        + EasyUtils.convertClassNameToResourcePath(basePackage);
                BeanHolder[] resBeanHolder = getResources(packageSearchPath);
                for (BeanHolder bean : resBeanHolder) {
                    URL uri = bean.getUri();
                    // 得到协议的名称
                    String protocol = bean.getUri().getProtocol();
                    // 如果是以文件的形式保存在服务器上
                    if ("file".equals(protocol)) {
                        // ("file类型的扫描");
                        // 获取包的物理路径
                        String filePath = URLDecoder.decode(uri.getFile(), "UTF-8");
                        findAndAddClassesInPackage(basePackage, filePath);
                    }
                }
            }
        }
    }

    public static void findAndAddClassesInPackage(String basePackage, String filePath)
            throws ClassNotFoundException {

        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }

        File[] files = dir.listFiles();

        // 循环所有文件
        for (File file : files) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                StringBuilder packeageNameTemp = new StringBuilder();
                if (StringUtils.isEmpty(basePackage)) {
                    packeageNameTemp.append(file.getName());
                } else {
                    packeageNameTemp.append(basePackage).append(".").append(file.getName());
                }
                findAndAddClassesInPackage(packeageNameTemp.toString(), file.getAbsolutePath());
            } else {
                String fileName = file.getName();
                if (fileName.endsWith(".class")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    Class<?> classz = Class.forName(basePackage + "." + fileName);
                    Method[] methods = classz.getDeclaredMethods();
                    for (Method method : methods) {
                        BeanHolder beanholder = null;
                        // ③获取方法上所标注的注解对象
                        EasyAction ea = method.getAnnotation(EasyAction.class);
                        if (ea != null) {
                            if (!StringUtils.isEmpty(ea.path())) {
                                beanholder = new BeanHolder();
                                beanholder.setClassz(classz);
                                beanholder.setBeanName(basePackage + "." + fileName);
                                beanholder.setMethodName(method.getName());
                                holder.put(ea.path(), beanholder);
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public BeanHolder[] getResources(String locationPattern) throws IOException {
        Assert.notNull(locationPattern, "Location pattern must not be null");
        Set<BeanHolder> beanHolder = new LinkedHashSet<BeanHolder>(16);
        if (locationPattern.startsWith(EasyResource.CLASSPATH_ALL_URL_PREFIX)) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> dirs = classLoader.getResources(locationPattern.replace(
                    EasyResource.CLASSPATH_ALL_URL_PREFIX, ""));

            while (dirs.hasMoreElements()) {
                URL originalUrl = dirs.nextElement();
                beanHolder.add(new BeanHolder(getCleanedUrl(originalUrl, originalUrl.toString())));
            }
        }
        return beanHolder.toArray(new BeanHolder[beanHolder.size()]);
    }

    private URL getCleanedUrl(URL originalUrl, String originalPath) {
        try {
            return new URL(StringUtils.cleanPath(originalPath));
        } catch(MalformedURLException ex) {
            return originalUrl;
        }
    }

}
