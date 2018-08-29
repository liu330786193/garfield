package com.lyl.garfield.core.kafka;

import com.timevale.cat.api.conf.TopicConfig;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ProtobufDeserializer<T> implements Deserializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null){
            return null;
        }
        TopicConfig topicConfig = TopicConfig.matchFor(topic);
        Class<T> clazz = topicConfig.topicClass;
        Schema schema = RuntimeSchema.getSchema(clazz);
        T t = null;
        try {
            t = (T) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProtobufIOUtil.mergeFrom(data, t, schema);
        return t;
    }

    @Override
    public void close() {

    }
}
