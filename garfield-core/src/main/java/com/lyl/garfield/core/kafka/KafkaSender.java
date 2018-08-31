//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lyl.garfield.core.kafka;

import com.alibaba.fastjson.JSON;
import com.lyl.garfield.api.jvm.JvmDTO;
import com.lyl.garfield.api.trace.DefaultTraceSegment;
import com.lyl.garfield.core.boot.AgentPackageNotFoundException;
import com.lyl.garfield.core.boot.AgentPackagePath;
import com.lyl.garfield.core.conf.Config;
import com.lyl.garfield.core.logging.api.ILog;
import com.lyl.garfield.core.logging.api.LogManager;
import com.lyl.garfield.core.utils.StringUtil;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

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

    public static void initializeLog4j() {

        if (StringUtil.isEmpty(Config.Logging.DIR)) {
            try {
                Config.Logging.DIR = AgentPackagePath.getPath() + "/logs";
            } catch (AgentPackageNotFoundException e) {
                e.printStackTrace();
            }
        }

        Properties pro = new Properties();
        pro.put("log4j.rootLogger", "debug,D,E");

        pro.put("log4j.appender.D", "org.apache.log4j.DailyRollingFileAppender");
        pro.put("log4j.appender.D.File", Config.Logging.DIR + "/trace.log");
        pro.put("log4j.appender.D.Append", "true");
        pro.put("log4j.appender.D.Threshold", "INFO");
        pro.put("log4j.appender.D.DatePattern", "'.'yyyy-MM-dd");
        pro.put("log4j.appender.D.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.D.layout.ConversionPattern", "%d %-5p %c{2} - %m %X{TimeKey}%n");

        pro.put("log4j.appender.E", "org.apache.log4j.DailyRollingFileAppender");
        pro.put("log4j.appender.E.File", Config.Logging.DIR + "/trace-error.log");
        pro.put("log4j.appender.E.Append", "true");
        pro.put("log4j.appender.E.Threshold", "ERROR");
        pro.put("log4j.appender.E.DatePattern", "'.'yyyy-MM-dd");
        pro.put("log4j.appender.E.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.E.layout.ConversionPattern", "%d %-5p %c{2} - %m %X{TimeKey}%n");

        PropertyConfigurator.configure(pro);
    }
}
