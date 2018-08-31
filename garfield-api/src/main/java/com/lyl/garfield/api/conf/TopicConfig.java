package com.lyl.garfield.api.conf;

import com.lyl.garfield.api.jvm.JvmDTO;
import com.lyl.garfield.api.trace.DefaultTraceSegment;

import java.util.EnumSet;

public enum TopicConfig {

    /**
     * TRACE的监控
     */
    TRACE("garfield-agent-trace",DefaultTraceSegment.class),

    /**
     * JVM的系统监控
     */
    THREAD("garfield-agent-thread", JvmDTO.class),
    MEMORY("garfield-agent-memory", JvmDTO.class),
    GC("garfield-agent-gc", JvmDTO.class);

    public final String topicName;
    public final Class topicClass;

    TopicConfig(String topicName, Class topicClass){
        this.topicClass = topicClass;
        this.topicName = topicName;
    }

    public static TopicConfig matchFor(String topicName){
        return EnumSet.allOf(TopicConfig.class).stream()
                .filter(topic -> topic.topicName.equals(topicName))
                .findFirst()
                .orElse(null);
    }

}