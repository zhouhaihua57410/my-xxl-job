package com.netease.vcloud.meeting.server.cronjob.handler;

import com.google.gson.Gson;
import com.netease.vcloud.meeting.server.cronjob.mapper.AppMapper;
import com.netease.vcloud.meeting.server.cronjob.mapper.MapperInfoNotifyMapper;
import com.netease.vcloud.meeting.server.cronjob.model.App;
import com.netease.vcloud.meeting.server.cronjob.model.MapperInfoNotify;
import com.netease.vcloud.meeting.server.cronjob.model.Msg;
import com.netease.vcloud.meeting.server.cronjob.sender.SendStorage;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 11:24
 * @Description:
 */
@Component
public class HttpJobHandler extends IJobHandler {


    private static Logger logger = LoggerFactory.getLogger("xxl-job logger");

    @Autowired
    private MapperInfoNotifyMapper mapperInfoNotifyMapper;

    @Autowired
    private SendStorage SendStorage;

    @Autowired
    private AppMapper appMapper;


    @XxlJob(value = "httpJobHandler")
    @Override
    public ReturnT<String> execute(String param) throws Exception {

        List<MapperInfoNotify> meetingList = mapperInfoNotifyMapper.listByStatus();
        for (MapperInfoNotify meeting : meetingList) {
            App app = appMapper.queryByAppId(meeting.getAppId());
            Msg msg =  new Gson().fromJson(meeting.getBody(), Msg.class);
            logger.info("HttpJobHandler receive msg:{}", msg);
            SendStorage.sendToApp(app.getCallbackUrl(), app.getAppKey(), app.getAppSecret(), meeting.getBody());
        }

        return SUCCESS;
    }
}
