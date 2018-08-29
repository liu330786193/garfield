package com.lyl.garfield.api.jvm;

import java.io.Serializable;

/**
 * 报文
 *
 * @author mozilla
 */
public class BaseDTO implements Serializable {

    // 应用名
    protected String app;
    // 应用IP
    protected String ip;
    // 实例ID(在应用IP基础上唯一确定一个采集目标)
    protected String instanceId;
    // 时间戳
    protected Object timestamp;
    // 类目
    protected String garfield;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getgarfield() {
        return garfield;
    }

    public void setgarfield(String garfield) {
        this.garfield = garfield;
    }
}
