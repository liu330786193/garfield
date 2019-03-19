package com.lyl.garfield.api.jvm;

import com.lyl.garfield.api.trace.DefaultTraceSegment;

import java.util.EnumSet;

public enum TopicConfig {

    /**
     * TRACE的监控
     */
    TRACE("cat-agent-trace", DefaultTraceSegment.class),

    /**
     * JVM的系统监控
     */
    THREAD("cat-agent-thread", JvmDTO.class),
    MEMORY("cat-agent-memory", JvmDTO.class),
    GC("cat-agent-gc", JvmDTO.class);

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