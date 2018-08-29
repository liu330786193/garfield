package com.lyl.garfield.api.trace;

import java.io.Serializable;
import java.util.List;

public class DefaultTraceSegment implements Serializable {

    protected String traceSegmentId;
    protected List<DefaultTraceSegmentRef> refs;
    protected List<DefaultTracingSpan> spans;
    protected String appligarfieldionId;
    protected String appligarfieldionInstanceId;
    protected String ip;
    protected List<String> rgts;
    protected boolean ignore = false;
    protected boolean isSizeLimited = false;
    protected boolean singleSpanSegment;

    public void archive(DefaultTracingSpan finishedSpan) {
        spans.add(finishedSpan);
    }

    public String getTraceSegmentId() {
        return traceSegmentId;
    }

    public void setTraceSegmentId(String traceSegmentId) {
        this.traceSegmentId = traceSegmentId;
    }

    public List<DefaultTraceSegmentRef> getRefs() {
        return refs;
    }

    public void setRefs(List<DefaultTraceSegmentRef> refs) {
        this.refs = refs;
    }

    public List<DefaultTracingSpan> getSpans() {
        return spans;
    }

    public void setSpans(List<DefaultTracingSpan> spans) {
        this.spans = spans;
    }

    public String getAppligarfieldionId() {
        return appligarfieldionId;
    }

    public void setAppligarfieldionId(String appligarfieldionId) {
        this.appligarfieldionId = appligarfieldionId;
    }

    public String getAppligarfieldionInstanceId() {
        return appligarfieldionInstanceId;
    }

    public void setAppligarfieldionInstanceId(String appligarfieldionInstanceId) {
        this.appligarfieldionInstanceId = appligarfieldionInstanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getRgts() {
        return rgts;
    }

    public void setRgts(List<String> rgts) {
        this.rgts = rgts;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isSizeLimited() {
        return isSizeLimited;
    }

    public void setSizeLimited(boolean sizeLimited) {
        isSizeLimited = sizeLimited;
    }

    public boolean isSingleSpanSegment() {
        return this.spans != null && this.spans.size() == 1;
    }

    public void setSingleSpanSegment(boolean singleSpanSegment) {
        this.singleSpanSegment = singleSpanSegment;
    }

}
