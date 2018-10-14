package com.lyl.garfield.core.jvm.collector;

import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class BaseCollector {

    // 1440 = 60*24 one day(每分钟采集一次)
    public static final int queueSize  = 60 * 24;

    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 如果满了,丢弃数据
    public static void checkQueueSize(ArrayBlockingQueue<?> queue) {
        if (queue.size() >= queueSize) {
            queue.poll();
        }
    }

}
