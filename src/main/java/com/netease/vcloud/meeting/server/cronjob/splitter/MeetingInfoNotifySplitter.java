package com.netease.vcloud.meeting.server.cronjob.splitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.vcloud.meeting.server.cronjob.cache.MeetingInfoNotifyRedis;
import com.netease.vcloud.meeting.server.cronjob.mapper.MapperInfoNotifyMapper;
import com.netease.vcloud.meeting.server.cronjob.model.MeetingInfoNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhouhaihua
 * @date: 2020/10/14 11:16
 * @Description:
 */
@Component("meetingInfoNotifySplitter")
public class MeetingInfoNotifySplitter extends Splitter {

    private static Logger logger = LoggerFactory.getLogger(MeetingInfoNotifySplitter.class);

    @Autowired
    private MapperInfoNotifyMapper mapperInfoNotifyMapper;

    @Resource
    private MeetingInfoNotifyRedis meetingInfoNotifyRedis;


    @Override
    public List query(long timestamp) {
        String result;
        if (meetingInfoNotifyRedis.exist(MEETING_INFO_REDIS_KEY)){
            result = meetingInfoNotifyRedis.getMeetingInfoList(MEETING_INFO_REDIS_KEY);
            return new Gson().fromJson(result, new TypeToken<List<Long>>() {}.getType());
        }else {
            List list = getList();
            meetingInfoNotifyRedis.set(MEETING_INFO_REDIS_KEY, new Gson().toJson(list));
            return list;
        }
    }

    private List getList() {
        List<MeetingInfoNotify> meetingInfoNotifies = mapperInfoNotifyMapper.listByStatus();
        if (CollectionUtils.isEmpty(meetingInfoNotifies)){
            return null;
        }
        return meetingInfoNotifies.stream().map(MeetingInfoNotify::getId).collect(Collectors.toList());
    }
}
