package com.netease.vcloud.meeting.server.cronjob.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhouhaihua
 * @date: 2020/10/12 11:38
 * @Description:
 */


@Component
public class DemoJobHandler extends IJobHandler {

    private static Logger logger = LoggerFactory.getLogger("xxl-job logger");

    @XxlJob(value = "demoJobHandler")
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("***** demo job start *****");
        System.out.println("***** demo job start *****");

//        for (int i = 0; i < 5; i++) {
//            logger.info("beat at:" + i);
//            TimeUnit.SECONDS.sleep(2);
//        }
        return SUCCESS;
    }
}
