package com.netease.vcloud.meeting.server.cronjob.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 11:24
 * @Description:
 */
@Component
public class HttpJobHandler extends IJobHandler {


    private static Logger logger = LoggerFactory.getLogger("xxl-job logger");


    @XxlJob(value = "httpJobHandler")
    @Override
    public ReturnT<String> execute(String param) throws Exception {




        List<String> list = new ArrayList<>();



        for (String s : list) {

        }

        return SUCCESS;
    }
}
