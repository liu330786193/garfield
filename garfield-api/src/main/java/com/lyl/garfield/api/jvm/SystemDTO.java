package com.lyl.garfield.api.jvm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author mozilla
 */
public class SystemDTO extends BaseDTO {

    private Map<String, Map<Object, Object>> io = new LinkedHashMap<String, Map<Object, Object>>();
    private Map<Object, Object> load = new LinkedHashMap<Object, Object>();
    private Map<Object, Object> memory = new LinkedHashMap<Object, Object>();
    private Map<String, Map<Object, Object>> tcp = new LinkedHashMap<String, Map<Object, Object>>();
    private Map<String, Map<Object, Object>> traffic = new LinkedHashMap<String, Map<Object, Object>>();
    private Map<Object, Object> cpu = new LinkedHashMap<Object, Object>();

    public Map<String, Map<Object, Object>> getIo() {
        return io;
    }

    public void setIo(Map<String, Map<Object, Object>> io) {
        this.io = io;
    }

    public Map<Object, Object> getLoad() {
        return load;
    }

    public void setLoad(Map<Object, Object> load) {
        this.load = load;
    }

    public Map<Object, Object> getMemory() {
        return memory;
    }

    public void setMemory(Map<Object, Object> memory) {
        this.memory = memory;
    }

    public Map<String, Map<Object, Object>> getTcp() {
        return tcp;
    }

    public void setTcp(Map<String, Map<Object, Object>> tcp) {
        this.tcp = tcp;
    }

    public Map<String, Map<Object, Object>> getTraffic() {
        return traffic;
    }

    public void setTraffic(Map<String, Map<Object, Object>> traffic) {
        this.traffic = traffic;
    }

    public Map<Object, Object> getCpu() {
        return cpu;
    }

    public void setCpu(Map<Object, Object> cpu) {
        this.cpu = cpu;
    }
}
