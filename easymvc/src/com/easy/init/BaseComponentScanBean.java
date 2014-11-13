package com.easy.init;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import com.easy.holder.BeanHolder;
import com.easy.util.Assert;
import com.easy.util.EasyResource;
import com.easy.util.StringUtils;

public class BaseComponentScanBean {

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
