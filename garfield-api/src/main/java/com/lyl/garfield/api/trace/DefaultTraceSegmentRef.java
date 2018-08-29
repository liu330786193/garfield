package com.lyl.garfield.api.trace;

import java.io.Serializable;

public class DefaultTraceSegmentRef implements Serializable {

    protected SegmentRefType type;
    protected String traceSegmentId;
    protected int spanId = -1;
    protected String peerHost;
    protected String entryAppligarfieldionInstanceId = DictionaryUtil.emptyValue();
    protected String parentAppligarfieldionInstanceId = DictionaryUtil.emptyValue();
    protected String entryOperationName;
    protected String parentOperationName;

    public SegmentRefType getType() {
        return type;
    }

    public void setType(SegmentRefType type) {
        this.type = type;
    }

    public String getTraceSegmentId() {
        return traceSegmentId;
    }

    public void setTraceSegmentId(String traceSegmentId) {
        this.traceSegmentId = traceSegmentId;
    }

    public int getSpanId() {
        return spanId;
    }

    public void setSpanId(int spanId) {
        this.spanId = spanId;
    }

    public String getPeerHost() {
        return peerHost;
    }

    public void setPeerHost(String peerHost) {
        this.peerHost = peerHost;
    }

    public String getEntryAppligarfieldionInstanceId() {
        return entryAppligarfieldionInstanceId;
    }

    public void setEntryAppligarfieldionInstanceId(String entryAppligarfieldionInstanceId) {
        this.entryAppligarfieldionInstanceId = entryAppligarfieldionInstanceId;
    }

    public String getParentAppligarfieldionInstanceId() {
        return parentAppligarfieldionInstanceId;
    }

    public void setParentAppligarfieldionInstanceId(String parentAppligarfieldionInstanceId) {
        this.parentAppligarfieldionInstanceId = parentAppligarfieldionInstanceId;
    }

    public String getEntryOperationName() {
        return entryOperationName;
    }

    public void setEntryOperationName(String entryOperationName) {
        this.entryOperationName = entryOperationName;
    }

    public String getParentOperationName() {
        return parentOperationName;
    }

    public void setParentOperationName(String parentOperationName) {
        this.parentOperationName = parentOperationName;
    }

    public enum SegmentRefType implements Serializable {
        CROSS_PROCESS,
        CROSS_THREAD
    }
}
