package com.netease.vcloud.meeting.server.cronjob.cache;

import com.netease.vcloud.meeting.server.cronjob.util.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/14 11:35
 * @Description:
 */
@Configuration
@Component
public class MeetingInfoNotifyRedis {

    private static final Logger logger = LoggerFactory.getLogger(MeetingInfoNotifyRedis.class);


    @Autowired
    private JedisUtil redisUtil;

    @Value("${redis.maxKeyExpireSecond:10}")
    private int maxKeyExpireSecond;

    public String getMeetingInfoList(String key) {
        String result = null;
        try {
            result = redisUtil.get(key);
            if (StringUtils.isBlank(result)) {
                return null;
            } else {
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        finally {
            logger.info("get meeting members. key: {}, result: {}", key, result);
        }
    }

    public void set(String key, String value){
        boolean ret = false;
        try {
            ret = redisUtil.setEx(key , value, maxKeyExpireSecond) > 0;
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            logger.info("set meeting members. key: {}, value: {}, ret: {}", key, value, ret);
        }
    }

    public boolean exist(String key){
        return  redisUtil.exists(key);
    }

}
