package com.lyl.garfield.core.utils;

import com.timevale.cat.core.conf.Config;

import java.util.Collection;

public class NetworkSingleton {

    private static final NetworkSingleton INSTANCE = new NetworkSingleton();

    private Collection<String> allIpv4NoLoopbackAddresses;

    public String localHostName;

    private String ip;

    private String instanceId;

    private long tracePartOne;

    private NetworkSingleton(){
        this.allIpv4NoLoopbackAddresses = IPUtils.getAllIpv4NoLoopbackAddresses();
        this.localHostName = IPUtils.getLocalHostName();
        this.ip = this.allIpv4NoLoopbackAddresses.iterator().next();
        this.instanceId = this.ip.concat(":").concat(Config.Agent.APPLICATION_ID);
        this.tracePartOne = Long.parseLong(this.ip.replaceAll("\\.", ""));
    }


    public static NetworkSingleton getInstance(){
        return NetworkSingleton.INSTANCE;
    }

    public String getIp() {
        return ip;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public long getTracePartOne() {
        return tracePartOne;
    }

}
