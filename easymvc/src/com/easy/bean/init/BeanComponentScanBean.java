package com.easy.bean.init;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.easy.bean.annotation.EasyService;
import com.easy.bean.annotation.Inject;
import com.easy.holder.BeanHolder;
import com.easy.init.BaseComponentScanBean;
import com.easy.init.IComponentScanBean;
import com.easy.util.Assert;
import com.easy.util.EasyResource;
import com.easy.util.EasyUtils;
import com.easy.util.StringUtils;

public class BeanComponentScanBean extends BaseComponentScanBean implements IComponentScanBean {

    private static List<BeanHolder> beanList = new ArrayList<BeanHolder>();

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

            setFiledValue(beanList);

        }
    }

    public static void findAndAddClassesInPackage(String basePackage, String filePath)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {

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
                    boolean flag = classz.isAnnotationPresent(EasyService.class);
                    if (flag) {
                        EasyService easyService = classz.getAnnotation(EasyService.class);
                        BeanHolder beanholder = new BeanHolder();
                        beanholder.setBeanName(basePackage + "." + fileName);
                        beanholder.setMethodName(easyService.name());
                        beanholder.setClassz(classz.newInstance());
                        EasyBeanFactory.beanHolder.put(easyService.name(), beanholder);
                        beanList.add(beanholder);
                    }

                } else {
                    continue;
                }
            }
        }
    }

    private void setFiledValue(List<BeanHolder> beanList) {
        if (beanList != null && beanList.size() > 0) {
            for (BeanHolder bean : beanList) {
                Object classzInstance = bean.getClassz();
                Field[] fields = classzInstance.getClass().getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        boolean fieldFlag = field.isAnnotationPresent(Inject.class);
                        if (fieldFlag) {
                            try {
                                Method method = classzInstance.getClass().getMethod(
                                        "set" + StringUtils.firstCodeUpper(field.getName()),
                                        field.getType());
                                method.invoke(
                                        classzInstance,
                                        Class.forName(EasyBeanFactory.beanHolder.get(field.getName()).getBeanName())
                                                .newInstance());
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            bean.setClassz(classzInstance);
                            EasyBeanFactory.beanHolder.put(bean.getMethodName(), bean);
                        }
                    }
                }
            }
        }
    }
}
