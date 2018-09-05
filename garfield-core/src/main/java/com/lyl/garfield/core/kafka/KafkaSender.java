//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lyl.garfield.core.kafka;

import com.alibaba.fastjson.JSON;
import com.lyl.garfield.api.jvm.JvmDTO;
import com.lyl.garfield.api.trace.DefaultTraceSegment;
import com.lyl.garfield.core.logging.api.ILog;
import com.lyl.garfield.core.logging.api.LogManager;

public class KafkaSender {

    private static final ILog logger = LogManager.getLogger(KafkaSender.class);

    private static final KafkaSender INSTANCE = new KafkaSender();


    public void sendThread(JvmDTO jvmDTO) {
    }

    public void sendGc(JvmDTO jvmDTO) {
    }

    public void sendMemeory(JvmDTO jvmDTO) {
    }

    public void sendTrace(DefaultTraceSegment traceSegment){

        if (logger.isDebugEnable()){
            System.out.println(JSON.toJSONString(traceSegment));
            logger.debug(("Trace:" + JSON.toJSONString(traceSegment)));
        }
    }

    public static KafkaSender getInstance() {
        return INSTANCE;
    }


}
