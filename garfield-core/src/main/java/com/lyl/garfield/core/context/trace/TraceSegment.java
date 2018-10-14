package com.lyl.garfield.core.context.trace;

import com.lyl.garfield.api.trace.DefaultTraceSegment;
import com.lyl.garfield.api.trace.DefaultTraceSegmentRef;
import com.lyl.garfield.api.trace.DefaultTracingSpan;
import com.lyl.garfield.core.conf.Config;
import com.lyl.garfield.core.context.ids.GlobalIdGenerator;
import com.lyl.garfield.core.context.ids.NewDistributedTraceId;
import com.lyl.garfield.core.utils.NetworkSingleton;

import java.util.ArrayList;
import java.util.LinkedList;

public class TraceSegment extends DefaultTraceSegment {

    public TraceSegment() {
        this.traceSegmentId = GlobalIdGenerator.generate().toString();
        this.spans = new LinkedList<DefaultTracingSpan>();
        NewDistributedTraceId traceId = new NewDistributedTraceId();
        this.rgts = new ArrayList<String>();
        this.rgts.add(traceId.toString());
        this.applicationId = Config.Agent.APPLICATION_ID;
        this.applicationInstanceId = NetworkSingleton.getInstance().getInstanceId();
        this.ip = NetworkSingleton.getInstance().getIp();
    }

    public void ref(TraceSegmentRef refSegment) {
        if (refs == null) {
            refs = new LinkedList<DefaultTraceSegmentRef>();
        }
        if (!refs.contains(refSegment)) {
            refs.add(refSegment);
        }
    }

    public void relatedGlobalTraces(String distributedTraceId) {
        this.rgts.add(distributedTraceId);
    }

    public TraceSegment finish(boolean isSizeLimited) {
        this.isSizeLimited = isSizeLimited;
        return this;
    }

    public boolean hasRef() {
        return !(refs == null || refs.size() == 0);
    }

}
