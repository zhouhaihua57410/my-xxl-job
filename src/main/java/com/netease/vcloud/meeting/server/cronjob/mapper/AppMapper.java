package com.netease.vcloud.meeting.server.cronjob.mapper;

import com.netease.vcloud.meeting.server.cronjob.model.App;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 17:26
 * @Description:
 */

@Mapper
@Component
public interface AppMapper {
    App queryByAppId(Long appId);

}
