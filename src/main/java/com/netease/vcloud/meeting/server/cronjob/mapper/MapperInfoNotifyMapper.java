package com.netease.vcloud.meeting.server.cronjob.mapper;

import com.netease.vcloud.meeting.server.cronjob.model.MeetingInfoNotify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 17:08
 * @Description:
 */

@Mapper
@Component
public interface MapperInfoNotifyMapper {

    List<MeetingInfoNotify> listByStatus();
}
