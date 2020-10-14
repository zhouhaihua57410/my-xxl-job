package com.netease.vcloud.meeting.server.cronjob.splitter;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/14 11:16
 * @Description:
 */
@Component("meetingInfoNotifySplitter")
public class MeetingInfoNotifySplitter extends Splitter {
    @Override
    public List query(long timestamp) {
        return null;
    }
}
