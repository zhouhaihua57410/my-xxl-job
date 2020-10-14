package com.netease.vcloud.meeting.server.cronjob.handler;

import com.netease.vcloud.meeting.server.cronjob.common.TaskManager;
import com.netease.vcloud.meeting.server.cronjob.splitter.Splitter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.ShardingUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: zhouhaihua
 * @date: 2020/10/13 17:08
 * @Description:
 */

public abstract class MeetingJobHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(MeetingJobHandler.class);


    @Autowired
    private ApplicationContext applicationContext;

    @Resource(name = "schedule")
    private TaskManager schedule;

    public abstract void doJob(String split, Long timestamp) throws DataAccessException;

    protected abstract String getSplitter();


    @Override
    public ReturnT<String> execute(String params) {
        logger.info("execute job, tick param:" + params);
        String splitterName = getSplitter();
        final Long timestamp = System.currentTimeMillis();
        if (StringUtils.isBlank(splitterName)) {
            Splitter splitter = (Splitter) applicationContext.getBean("defaultSplitter");
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            List list = splitter.split(shardingVO.getIndex(), shardingVO.getTotal(), timestamp);
            if (list != null) {
                doJob(list.get(0).toString(), timestamp);
                logger.info(String.format("finished, timestamp=%d, split=%s", timestamp, "default"));
            }
        } else {
            Splitter splitter = (Splitter) applicationContext.getBean(getSplitter()); // 根据上下文自己去获取
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            Integer shardingIndex = shardingVO.getIndex();
            List list = splitter.split(shardingIndex, shardingVO.getTotal(), timestamp);
            if (list != null && !list.isEmpty()) {
                for (final Object splitId : list) {
                    schedule.getExecutor().execute(() -> {
                        try {
                            doJob(splitId.toString(), timestamp);
                            logger.info(String.format("finished, timestamp=%d, split=%s", timestamp, splitId));
                        }catch (Exception e){
                            logger.error("#doJob() occur an error", e);
                        }
                    });
                }
            }
        }
        return SUCCESS;
    }
}
