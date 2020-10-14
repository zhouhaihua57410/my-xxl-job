package com.netease.vcloud.meeting.server.cronjob.model;

import static java.lang.System.getProperties;

/**
 * @author: zhouhaihua
 * @date: 2020/9/8 11:41
 * @Description:
 */

public class Conf {

    public static Long getLong(String key, Long defaultValue) {
        try {
            return Long.valueOf(getProperties().getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        try {
            return Integer.valueOf(getProperties().getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
