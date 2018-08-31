package com.lyl.garfield.api.trace;


import com.lyl.garfield.api.component.Component;
import com.lyl.garfield.api.util.KeyValuePair;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultTracingSpan implements AbstractSpan, Serializable {
    protected int spanId;
    protected int parentSpanId;
    protected List<KeyValuePair> tags;
    protected String operationName;
    protected SpanLayer layer;
    protected long startTime;
    protected long endTime;
    protected boolean errorOccurred = false;
    protected int componentId = 0;
    protected boolean entry = false;
    protected boolean exit = false;
    protected List<LogDataEntity> logs;
    protected long time;

    public DefaultTracingSpan(){}

    protected DefaultTracingSpan(int spanId, int parentSpanId, String operationName) {
        this.operationName = operationName;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
    }

    /**
     * Set a key:value tag on the Span.
     *
     * @return this Span instance, for chaining
     */
    @Override
    public DefaultTracingSpan tag(String key, String value) {
        if (tags == null) {
            tags = new LinkedList<KeyValuePair>();
        }
        tags.add(new KeyValuePair(key, value));
        return this;
    }

    public boolean finish(DefaultTraceSegment owner) {
        this.endTime = System.currentTimeMillis();
        this.time = this.endTime - this.startTime;
        owner.archive(this);
        return true;
    }

    @Override
    public DefaultTracingSpan start() {
        this.startTime = System.currentTimeMillis();
        return this;
    }

    /**
     * Record an exception event of the current walltime timestamp.
     *
     * @param t any subclass of {@link Throwable}, which occurs in this span.
     * @return the Span, for chaining
     */
    @Override
    public DefaultTracingSpan log(Throwable t) {
        if (logs == null) {
            logs = new LinkedList<LogDataEntity>();
        }
        logs.add(new LogDataEntity.Builder()
            .add(new KeyValuePair("event", "error"))
            .add(new KeyValuePair("error.kind", t.getClass().getName()))
            .add(new KeyValuePair("message", t.getMessage()))
//            .add(new KeyValuePair("stack", ThrowableTransformer.INSTANCE.convert2String(t, 4000)))
            .build(System.currentTimeMillis()));
        return this;
    }

    /**
     * Record a common log with multi fields, for supporting opentracing-java
     *
     * @param fields
     * @return the Span, for chaining
     */
    @Override
    public DefaultTracingSpan log(long timestampMicroseconds, Map<String, ?> fields) {
        if (logs == null) {
            logs = new LinkedList<LogDataEntity>();
        }
        LogDataEntity.Builder builder = new LogDataEntity.Builder();
        for (Map.Entry<String, ?> entry : fields.entrySet()) {
            builder.add(new KeyValuePair(entry.getKey(), entry.getValue().toString()));
        }
        logs.add(builder.build(timestampMicroseconds));
        return this;
    }

    /**
     * In the scope of this span tracing context, error occurred, in auto-instrumentation mechanism, almost means throw
     * an exception.
     *
     * @return span instance, for chaining.
     */
    @Override
    public DefaultTracingSpan errorOccurred() {
        this.errorOccurred = true;
        return this;
    }

    @Override
    public boolean isEntry() {
        return false;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * Set the operation name, just because these is not compress dictionary value for this name. Use the entire string
     * temporarily, the agent will compress this name in async mode.
     *
     * @param operationName
     * @return span instance, for chaining.
     */
    @Override
    public DefaultTracingSpan setOperationName(String operationName) {
        this.operationName = operationName;
        return this;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    @Override
    public int getSpanId() {
        return spanId;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public DefaultTracingSpan setLayer(SpanLayer layer) {
        this.layer = layer;
        return this;
    }

    /**
     * Set the component of this span, with internal supported. Highly recommend to use this way.
     *
     * @param component
     * @return span instance, for chaining.
     */
    @Override
    public DefaultTracingSpan setComponent(Component component) {
        this.componentId = component.getId();
        return this;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getParentSpanId() {
        return parentSpanId;
    }

    public List<KeyValuePair> getTags() {
        return tags;
    }

    public List<LogDataEntity> getLogs() {
        return logs;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public SpanLayer getLayer() {
        return layer;
    }

    public void setSpanId(int spanId) {
        this.spanId = spanId;
    }

    public void setParentSpanId(int parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public void setTags(List<KeyValuePair> tags) {
        this.tags = tags;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setLogs(List<LogDataEntity> logs) {
        this.logs = logs;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
