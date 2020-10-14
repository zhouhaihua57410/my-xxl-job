package com.netease.vcloud.meeting.server.cronjob.splitter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.vcloud.meeting.server.cronjob.cache.MeetingInfoNotifyRedis;
import com.netease.vcloud.meeting.server.cronjob.handler.DemoJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/14 09:56
 * @Description:
 */
@Component("defaultSplitter")
public class DefaultSplitter extends Splitter {

    private static Logger logger = LoggerFactory.getLogger(DefaultSplitter.class);

    @Resource
    private MeetingInfoNotifyRedis meetingInfoNotifyRedis;

    @Override
    public List query(long timestamp) {
        String result;
        if (meetingInfoNotifyRedis.exist(DEMO_REDIS_KEY)){
            result = meetingInfoNotifyRedis.getMeetingInfoList(DEMO_REDIS_KEY);
            logger.info("get list by redis");
            return new Gson().fromJson(result, new TypeToken<List<Long>>() {}.getType());
        }else {
            List list = getList();
            meetingInfoNotifyRedis.set(DEMO_REDIS_KEY, new Gson().toJson(list));
            logger.info("get list by db");
            return list;
        }
    }

    private static List getList(){
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(4L);
        list.add(5L);
        list.add(6L);
        list.add(7L);
        list.add(8L);
        list.add(9L);
        list.add(10L);
        list.add(11L);
        list.add(12L);
        list.add(13L);
        list.add(14L);
        list.add(15L);
        list.add(16L);
        list.add(17L);
        list.add(18L);
        list.add(19L);
        list.add(20L);
        return list;
    }
}
