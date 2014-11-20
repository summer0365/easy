package com.easy.web.init;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.easy.bean.annotation.EasyService;
import com.easy.bean.init.EasyBeanFactory;
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
                    // �õ�Э�������
                    String protocol = bean.getUri().getProtocol();
                    // ��������ļ�����ʽ�����ڷ�������
                    if ("file".equals(protocol)) {
                        // ("file���͵�ɨ��");
                        // ��ȡ��������·��
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
            // log.warn("�û�������� " + packageName + " ��û���κ��ļ�");
            return;
        }

        File[] files = dir.listFiles();

        // ѭ�������ļ�
        for (File file : files) {
            // �����Ŀ¼ �����ɨ��
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
                    Object newclassz = null;
                    if(classz.isAnnotationPresent(EasyService.class)){
                        EasyService es = classz.getAnnotation(EasyService.class);
                        newclassz = EasyBeanFactory.beanHolder.get(es.name()).getClassz();
                    }else{
                        try {
                            newclassz = classz.newInstance();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Method[] methods = classz.getDeclaredMethods();
                    for (Method method : methods) {
                        BeanHolder beanholder = null;
                        // �ۻ�ȡ����������ע��ע�����
                        EasyAction ea = method.getAnnotation(EasyAction.class);
                        if (ea != null) {
                            if (!StringUtils.isEmpty(ea.path())) {
                                beanholder = new BeanHolder();
                                beanholder.setClassz(newclassz);
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

}
