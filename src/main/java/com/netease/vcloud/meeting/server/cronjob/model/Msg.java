package com.netease.vcloud.meeting.server.cronjob.model;

import java.io.Serializable;

public class Msg implements Serializable {
    private Integer msgType;
    private Object msgBody;

    public Msg() {
    }

    public Msg(Integer msgType, Object msgBody) {
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Object getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(Object msgBody) {
        this.msgBody = msgBody;
    }
}
