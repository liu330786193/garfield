package com.lyl.garfield.core.kafka;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.alibaba.fastjson.JSON;
import com.lyl.garfield.api.conf.TopicConfig;
import com.lyl.garfield.api.jvm.JvmDTO;
import com.lyl.garfield.api.trace.DefaultTraceSegment;
import com.lyl.garfield.core.boot.AgentPackageNotFoundException;
import com.lyl.garfield.core.boot.AgentPackagePath;
import com.lyl.garfield.core.conf.Config;
import com.lyl.garfield.core.logging.api.ILog;
import com.lyl.garfield.core.logging.api.LogManager;
import com.lyl.garfield.core.utils.StringUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.PropertyConfigurator;

import java.util.*;

public class KafkaSender {

    private static final ILog logger = LogManager.getLogger(KafkaSender.class);

    private static final KafkaSender INSTANCE = new KafkaSender();

    private Producer producer;

    private KafkaSender() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", Config.Kafka.BOOTSTRAP_SERVERS);
        properties.put("acks", Config.Kafka.ACKS);
        properties.put("retries", Config.Kafka.RETRIES);
        properties.put("batch.size", Config.Kafka.BATCH_SIZE);
        properties.put("linger.ms", Config.Kafka.LINGER_MS);
        properties.put("buffer.memory", Config.Kafka.BUFFER_MEMORY);
        properties.put("key.serializer", Config.Kafka.KEY_SERIALIZER);
        properties.put("value.serializer", Config.Kafka.VALUE_SERIALIZER);
        //添加生产者Client_id
//        properties.put("client.id", Config.Kafka.CLIENT_ID);
        //初始化log4j日志属性
        initializeLog4j();
        this.producer = new KafkaProducer(properties);
    }


    public void sendThread(JvmDTO jvmDTO) {
        this.producer.send(new ProducerRecord<String, JvmDTO>(TopicConfig.THREAD.topicName, jvmDTO));
    }

    public void sendGc(JvmDTO jvmDTO) {
        this.producer.send(new ProducerRecord<String, JvmDTO>(TopicConfig.GC.topicName, jvmDTO));
    }

    public void sendMemeory(JvmDTO jvmDTO) {
        this.producer.send(new ProducerRecord<String, JvmDTO>(TopicConfig.MEMORY.topicName, jvmDTO));
    }

    public void sendTrace(DefaultTraceSegment traceSegment){

        if (logger.isDebugEnable()){
            System.out.println(JSON.toJSONString(traceSegment));
            logger.debug(("Trace:" + JSON.toJSONString(traceSegment)));
        }
        this.producer.send(new ProducerRecord<String, DefaultTraceSegment>(TopicConfig.TRACE.topicName, traceSegment));
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
        pro.put("log4j.appender.D.File", Config.Logging.DIR + "/kafka.log");
        pro.put("log4j.appender.D.Append", "true");
        pro.put("log4j.appender.D.Threshold", "INFO");
        pro.put("log4j.appender.D.DatePattern", "'.'yyyy-MM-dd");
        pro.put("log4j.appender.D.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.D.layout.ConversionPattern", "%d %-5p %c{2} - %m %X{TimeKey}%n");

        pro.put("log4j.appender.E", "org.apache.log4j.DailyRollingFileAppender");
        pro.put("log4j.appender.E.File", Config.Logging.DIR + "/kafka-error.log");
        pro.put("log4j.appender.E.Append", "true");
        pro.put("log4j.appender.E.Threshold", "ERROR");
        pro.put("log4j.appender.E.DatePattern", "'.'yyyy-MM-dd");
        pro.put("log4j.appender.E.layout", "org.apache.log4j.PatternLayout");
        pro.put("log4j.appender.E.layout.ConversionPattern", "%d %-5p %c{2} - %m %X{TimeKey}%n");

        PropertyConfigurator.configure(pro);
    }
}
