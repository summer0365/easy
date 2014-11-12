package com.easy.util;

import org.apache.log4j.Logger;

public class LogUtil {
    private static Logger logger = Logger.getLogger(LogUtil.class);

    public static void error(String msg) {
        logger.error(msg);
    }

}
