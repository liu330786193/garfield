package com.lyl.garfield.core.utils;

import com.lyl.garfield.api.trace.DefaultTraceSegment;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtoBufUtils {

    public static  byte[] serializer(Object data){
        if (data == null){
            return null;
        }
        Schema schema = RuntimeSchema.getSchema(data.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff = null;
        try {
            protostuff = ProtobufIOUtil.toByteArray(data, schema, buffer);
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage(), e);
        }finally {
            buffer.clear();
        }
        return protostuff;
    }

    public static <T> T deserializer(byte[] data, Class<T> clazz){
        if (data == null){
            return null;
        }
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

    public static DefaultTraceSegment deserializerTest(byte[] data){
        if (data == null){
            return null;
        }
        DefaultTraceSegment traceSegment = new DefaultTraceSegment();
        Schema schema = RuntimeSchema.getSchema(DefaultTraceSegment.class);
        ProtobufIOUtil.mergeFrom(data, traceSegment, schema);
        return traceSegment;
    }
}
