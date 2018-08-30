package com.lyl.garfield.core.context.trace;

import com.lyl.garfield.api.trace.DefaultTraceSegmentRef;
import com.lyl.garfield.core.context.ContextCarrier;
import com.lyl.garfield.core.context.ContextSnapshot;
import com.lyl.garfield.core.utils.NetworkSingleton;

public class TraceSegmentRef extends DefaultTraceSegmentRef {

    public TraceSegmentRef(ContextCarrier carrier) {
        this.type = SegmentRefType.CROSS_PROCESS;
        this.traceSegmentId = carrier.getTraceSegmentId().toString();
        this.spanId = carrier.getSpanId();
        this.parentApplicationInstanceId = carrier.getParentApplicationInstanceId();
        this.entryApplicationInstanceId = carrier.getEntryApplicationInstanceId();

        String host = carrier.getPeerHost();
        if (host.charAt(0) == '#') {
            this.peerHost = host.substring(1);
        }
        String entryOperationName = carrier.getEntryOperationName();
        if (entryOperationName.charAt(0) == '#') {
            this.entryOperationName = entryOperationName.substring(1);
        }
        String parentOperationName = carrier.getParentOperationName();
        if (parentOperationName.charAt(0) == '#') {
            this.parentOperationName = parentOperationName.substring(1);
        }
    }

    public TraceSegmentRef(ContextSnapshot snapshot) {
        this.type = SegmentRefType.CROSS_THREAD;
        this.traceSegmentId = snapshot.getTraceSegmentId().toString();
        this.spanId = snapshot.getSpanId();
        this.parentApplicationInstanceId = NetworkSingleton.getInstance().getInstanceId();
        this.entryApplicationInstanceId = snapshot.getEntryApplicationInstanceId();
        String entryOperationName = snapshot.getEntryOperationName();
        if (entryOperationName.charAt(0) == '#') {
            this.entryOperationName = entryOperationName.substring(1);
        }
        String parentOperationName = snapshot.getParentOperationName();
        if (parentOperationName.charAt(0) == '#') {
            this.parentOperationName = parentOperationName.substring(1);
        }
    }

}
