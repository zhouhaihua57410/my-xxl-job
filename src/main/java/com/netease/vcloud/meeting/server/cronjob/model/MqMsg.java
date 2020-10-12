package com.netease.vcloud.meeting.server.cronjob.model;

import java.util.HashMap;
import java.util.Map;


/**
 * @author: zhouhaihua
 * @date: 2020/9/7 17:54
 * @Description:
 */

public class MqMsg {
    private Map<String, Object> headers;
    private String msg;

    public MqMsg(String msg) {
        this.msg = msg;
    }

    public MqMsg(Map<String, Object> headers, String msg) {
        this.headers = headers;
        this.msg = msg;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MqMsg header(String key, Object value){
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public Object header(String key){
        return headers == null ? null : headers.get(key);
    }
}
