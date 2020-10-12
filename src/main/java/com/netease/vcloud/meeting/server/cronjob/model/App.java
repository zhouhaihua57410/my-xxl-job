package com.netease.vcloud.meeting.server.cronjob.model;

import java.util.Date;

/**
 * @Author: luoweiheng
 * @Date: 2020/4/29 15:27
 * @Description:
 */
public class App {
    private Long appId;

    private Long appGroupId;

    private Long tenantId;

    private String appName;

    private String appKey;

    private String appSecret;

    private String imAppKey;

    private String imAppSecret;

    private String nrtcAppKey;

    private String nrtcAppSecret;

    private Date expireTime;

    private Date dbCreateTime;

    private String callbackUrl;

    public App() {}

    public App(Long appGroupId, Long tenantId, String appName, String appKey,
               String appSecret, String imAppKey, String imAppSecret,
               String nrtcAppKey, String nrtcAppSecret, Date expireTime, Date dbCreateTime) {
        this.appGroupId = appGroupId;
        this.tenantId = tenantId;
        this.appName = appName;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.imAppKey = imAppKey;
        this.imAppSecret = imAppSecret;
        this.nrtcAppKey = nrtcAppKey;
        this.nrtcAppSecret = nrtcAppSecret;
        this.expireTime = expireTime;
        this.dbCreateTime = dbCreateTime;
    }

    public Long getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(Long appGroupId) {
        this.appGroupId = appGroupId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getImAppKey() {
        return imAppKey;
    }

    public void setImAppKey(String imAppKey) {
        this.imAppKey = imAppKey;
    }

    public String getImAppSecret() {
        return imAppSecret;
    }

    public void setImAppSecret(String imAppSecret) {
        this.imAppSecret = imAppSecret;
    }

    public String getNrtcAppKey() {
        return nrtcAppKey;
    }

    public void setNrtcAppKey(String nrtcAppKey) {
        this.nrtcAppKey = nrtcAppKey;
    }

    public String getNrtcAppSecret() {
        return nrtcAppSecret;
    }

    public void setNrtcAppSecret(String nrtcAppSecret) {
        this.nrtcAppSecret = nrtcAppSecret;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getDbCreateTime() {
        return dbCreateTime;
    }

    public void setDbCreateTime(Date dbCreateTime) {
        this.dbCreateTime = dbCreateTime;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
