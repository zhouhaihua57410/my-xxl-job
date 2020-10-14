package com.netease.vcloud.meeting.server.cronjob.sender;

import com.netease.vcloud.meeting.server.cronjob.model.HeaderKeys;
import com.netease.vcloud.meeting.server.cronjob.util.AppHttpClient;
import com.netease.vcloud.meeting.server.cronjob.util.CheckSumBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.UUID;

/**
 * @author: zhouhaihua
 * @date: 2020/9/8 11:37
 * @Description:
 */
@Component
public class SendStorage {

    private static final Logger logger = LoggerFactory.getLogger(SendStorage.class);

    private static final Long HOUR_MILLISECONDS = 3600 * 1000L;

    @Autowired
    private AppHttpClient appHttpClient;


    public void sendToApp(String url, String appKey, String secret, String msgStr) {
        String curTime = String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        String checkSum = CheckSumBuilder.getCheckSum(nonce, curTime, secret, CheckSumBuilder.getMD5(msgStr));
        try {
            StringEntity stringEntity = new StringEntity(msgStr, ContentType.APPLICATION_JSON);
            HttpUriRequest request = RequestBuilder.post().setUri(url)
                    .addHeader(HeaderKeys.KEY_APP_KEY, appKey)
                    .addHeader(HeaderKeys.KEY_CHECKSUM, checkSum)
                    .addHeader(HeaderKeys.KEY_NONCE, nonce)
                    .addHeader(HeaderKeys.KEY_CUR_TIME, curTime)
                    .setEntity(stringEntity)
                    .build();

            String result = appHttpClient.execute(request, 2000);
            logger.debug("SendToApp: callbackUrl: {}, msg:{}, response: {}", url, msgStr, result);
            if (result != null){
                //TODO 修改数据库状态
            }

            logger.debug("SendToApp: callbackUrl: {}, msg:{}, response: {}", url, msgStr);

        } catch (Throwable e) {
            logger.debug("SendToApp: callbackUrl: {}, msg:{}, exception: {}", url, msgStr, e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }


}
