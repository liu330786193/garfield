package com.lyl.garfield.core.context.trace;

import com.timevale.cat.api.trace.DefaultTraceSegment;
import com.timevale.cat.api.trace.DefaultTraceSegmentRef;
import com.timevale.cat.api.trace.DefaultTracingSpan;
import com.timevale.cat.core.conf.Config;
import com.timevale.cat.core.context.ids.GlobalIdGenerator;
import com.timevale.cat.core.context.ids.NewDistributedTraceId;
import com.timevale.cat.core.utils.NetworkSingleton;

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
