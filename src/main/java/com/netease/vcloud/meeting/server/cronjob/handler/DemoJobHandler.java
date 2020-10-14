package com.netease.vcloud.meeting.server.cronjob.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 11:38
 * @Description:
 */


@Component
public class DemoJobHandler extends MeetingJobHandler {

    private static Logger logger = LoggerFactory.getLogger(DemoJobHandler.class);

    @Override
    public void doJob(Object split, Long timestamp) throws DataAccessException {
        logger.info("demo job start, execute unit split :" + split.toString());
    }

    @Override
    protected String getSplitter() {
        return "defaultSplitter";
    }

    @XxlJob(value = "demoJobHandler")
    @Override
    public ReturnT<String> execute(String param) {

        return super.execute(param);
    }
}
