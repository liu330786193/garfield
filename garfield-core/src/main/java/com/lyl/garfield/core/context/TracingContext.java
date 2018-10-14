/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.lyl.garfield.core.context;

import com.lyl.garfield.api.trace.*;
import com.lyl.garfield.core.boot.ServiceManager;
import com.lyl.garfield.core.conf.Config;
import com.lyl.garfield.core.context.ids.ID;
import com.lyl.garfield.core.context.trace.TraceSegment;
import com.lyl.garfield.core.context.trace.TraceSegmentRef;
import com.lyl.garfield.core.sampling.SamplingService;

import java.util.LinkedList;
import java.util.List;

public class TracingContext implements AbstractTracerContext {
    /**
     * @see {@link SamplingService}
     */
    private SamplingService samplingService;

    /**
     * The final {@link TraceSegment}, which includes all finished spans.
     */
    private TraceSegment segment;

    /**
     * Active spans stored in a Stack, usually called 'ActiveSpanStack'. This {@link LinkedList} is the in-memory
     * storage-structure. <p> I use {@link LinkedList#removeLast()}, {@link LinkedList#addLast(Object)} and {@link
     * LinkedList#last} instead of {@link #pop()}, {@link #push(AbstractSpan)}, {@link #peek()}
     */
    private LinkedList<AbstractSpan> activeSpanStack = new LinkedList<AbstractSpan>();

    /**
     * A counter for the next span.
     */
    private int spanIdGenerator;

    /**
     * Initialize all fields with default value.
     */
    TracingContext() {
        this.segment = new TraceSegment();
        this.spanIdGenerator = 0;
        if (samplingService == null) {
            samplingService = ServiceManager.INSTANCE.findService(SamplingService.class);
        }
    }

    /**
     * Inject the context into the given carrier, only when the active span is an exit one.
     *
     * @param carrier to carry the context for crossing process.
     * @throws IllegalStateException, if the active span isn't an exit one.
     * @see {@link AbstractTracerContext#inject(ContextCarrier)}
     */
    @Override
    public void inject(ContextCarrier carrier) {
        AbstractSpan span = this.activeSpan();
        if (!span.isExit()) {
            throw new IllegalStateException("Inject can be done only in Exit Span");
        }

        WithPeerInfo spanWithPeer = (WithPeerInfo)span;
        String peer = spanWithPeer.getPeer();

        carrier.setTraceSegmentId(new ID(this.segment.getTraceSegmentId()));
        carrier.setSpanId(span.getSpanId());
        carrier.setParentApplicationInstanceId(segment.getApplicationInstanceId());
        carrier.setPeerHost(peer);
        List<DefaultTraceSegmentRef> refs = this.segment.getRefs();
        String operationName;
        String entryApplicationInstanceId;
        if (refs != null && refs.size() > 0) {
            DefaultTraceSegmentRef ref = refs.get(0);
            operationName = ref.getEntryOperationName();
            entryApplicationInstanceId = ref.getEntryApplicationInstanceId();
        } else {
            AbstractSpan firstSpan = first();
            operationName = firstSpan.getOperationName();
            entryApplicationInstanceId = this.segment.getApplicationInstanceId();
        }
        carrier.setEntryApplicationInstanceId(entryApplicationInstanceId);
        carrier.setEntryOperationName(operationName);
        carrier.setParentOperationName(first().getOperationName());
        carrier.setDistributedTraceIds(this.segment.getRgts());
    }

    /**
     * Extract the carrier to build the reference for the pre segment.
     *
     * @param carrier carried the context from a cross-process segment.
     * @see {@link AbstractTracerContext#extract(ContextCarrier)}
     */
    @Override
    public void extract(ContextCarrier carrier) {
        this.segment.ref(new TraceSegmentRef(carrier));
        this.segment.relatedGlobalTraces(carrier.getDistributedTraceId());
    }

    /**
     * Capture the snapshot of current context.
     *
     * @return the snapshot of context for cross-thread propagation
     * @see {@link AbstractTracerContext#capture()}
     */
    @Override
    public ContextSnapshot capture() {
        List<DefaultTraceSegmentRef> refs = this.segment.getRefs();
        ContextSnapshot snapshot = new ContextSnapshot(new ID(segment.getTraceSegmentId()),
            activeSpan().getSpanId(),
            segment.getRgts());
        String entryOperationName;
        String entryApplicationInstanceId;
        AbstractSpan firstSpan = first();
        if (refs != null && refs.size() > 0) {
            DefaultTraceSegmentRef ref = refs.get(0);
            entryOperationName = ref.getEntryOperationName();
            entryApplicationInstanceId = ref.getEntryApplicationInstanceId();
        } else {
            entryOperationName = firstSpan.getOperationName();
            entryApplicationInstanceId = this.segment.getApplicationInstanceId();
        }
        snapshot.setEntryApplicationInstanceId(entryApplicationInstanceId);
        snapshot.setEntryOperationName(entryOperationName);
        snapshot.setParentOperationName(firstSpan.getOperationName());
        return snapshot;
    }

    /**
     * Continue the context from the given snapshot of parent thread.
     *
     * @param snapshot from {@link #capture()} in the parent thread.
     * @see {@link AbstractTracerContext#continued(ContextSnapshot)}
     */
    @Override
    public void continued(ContextSnapshot snapshot) {
        this.segment.ref(new TraceSegmentRef(snapshot));
        this.segment.relatedGlobalTraces(snapshot.getDistributedTraceId());
    }

    /**
     * @return the first global datacarrier id.
     */
    @Override
    public String getReadableGlobalTraceId() {
        return segment.getRgts().get(0);
    }

    /**
     * Create an entry span
     *
     * @param operationName most likely a jvm name
     * @return span instance.
     * @see {@link EntrySpan}
     */
    @Override
    public AbstractSpan createEntrySpan(final String operationName) {
        if (isLimitMechanismWorking()) {
            NoopSpan span = new NoopSpan();
            return push(span);
        }
        AbstractSpan entrySpan;
        final AbstractSpan parentSpan = peek();
        final int parentSpanId = parentSpan == null ? -1 : parentSpan.getSpanId();
        if (parentSpan == null) {
            return push(new EntrySpan(spanIdGenerator++, parentSpanId, operationName).start());
        } else if (parentSpan.isEntry()) {
            return parentSpan.setOperationName(operationName).start();
        } else {
            throw new IllegalStateException("The Entry Span can't be the child of Non-Entry Span");
        }
    }

    /**
     * Create a local span
     *
     * @param operationName most likely a local method signature, or business name.
     * @return the span represents a local logic block.
     * @see {@link LocalSpan}
     */
    @Override
    public AbstractSpan createLocalSpan(final String operationName) {
        if (isLimitMechanismWorking()) {
            NoopSpan span = new NoopSpan();
            return push(span);
        }
        AbstractSpan parentSpan = peek();
        final int parentSpanId = parentSpan == null ? -1 : parentSpan.getSpanId();
        DefaultTracingSpan span = new LocalSpan(spanIdGenerator++, parentSpanId, operationName);
        span.start();
        return push(span);
    }

    /**
     * Create an exit span
     *
     * @param operationName most likely a jvm name of remote
     * @param remotePeer the network id(ip:port, hostname:port or ip1:port1,ip2,port, etc.)
     * @return the span represent an exit point of this segment.
     * @see {@link ExitSpan}
     */
    @Override
    public AbstractSpan createExitSpan(final String operationName, final String remotePeer) {
        AbstractSpan exitSpan;
        AbstractSpan parentSpan = peek();
        if (parentSpan != null && parentSpan.isExit()) {
            exitSpan = parentSpan;
        } else {
            final int parentSpanId = parentSpan == null ? -1 : parentSpan.getSpanId();
            if (isLimitMechanismWorking()) {
                exitSpan = new NoopExitSpan(remotePeer);
            }else {
                exitSpan = new ExitSpan(spanIdGenerator++, parentSpanId, operationName, remotePeer);
            }
            push(exitSpan);
        }
        exitSpan.start();
        return exitSpan;
    }

    /**
     * @return the active span of current context, the top element of {@link #activeSpanStack}
     */
    @Override
    public AbstractSpan activeSpan() {
        AbstractSpan span = peek();
        if (span == null) {
            throw new IllegalStateException("No active span.");
        }
        return span;
    }

    /**
     * Stop the given span, if and only if this one is the top element of {@link #activeSpanStack}. Because the tracing
     * core must make sure the span must match in a stack module, like any program did.
     *
     * @param span to finish
     */
    @Override
    public void stopSpan(AbstractSpan span) {
        AbstractSpan lastSpan = peek();
        if (lastSpan == span) {
            if (lastSpan instanceof DefaultTracingSpan) {
                DefaultTracingSpan toFinishSpan = (DefaultTracingSpan)lastSpan;
                if (toFinishSpan.finish(segment)) {
                    pop();
                }
            } else {
                pop();
            }
        } else {
            throw new IllegalStateException("Stopping the unexpected span = " + span);
        }

        if (activeSpanStack.isEmpty()) {
            this.finish();
        }
    }

    @Override
    public void ignoreTrace() {
        stopSpan(activeSpan());
        segment.setIgnore(true);
    }

    /**
     * Finish this context, and notify all {@link TracingContextListener}s, managed by {@link
     * TracingContext.ListenerManager}
     */
    private void finish() {
        TraceSegment finishedSegment = segment.finish(isLimitMechanismWorking());
        /**
         * Recheck the segment if the segment contains only one span.
         * Because in the runtime, can't sure this segment is part of distributed datacarrier.
         *
         * @see {@link #createSpan(String, long, boolean)}
         */
        if (!segment.hasRef() && segment.isSingleSpanSegment()) {
            if (!samplingService.trySampling()) {
                finishedSegment.setIgnore(true);
            }
        }
        TracingContext.ListenerManager.notifyFinish(finishedSegment);
    }

    /**
     * The <code>ListenerManager</code> represents an event notify for every registered listener, which are notified
     * when the <cdoe>TracingContext</cdoe> finished, and {@link #segment} is ready for further process.
     */
    public static class ListenerManager {
        private static List<TracingContextListener> LISTENERS = new LinkedList<TracingContextListener>();

        /**
         * Add the given {@link TracingContextListener} to {@link #LISTENERS} list.
         *
         * @param listener the new listener.
         */
        public static synchronized void add(TracingContextListener listener) {
            LISTENERS.add(listener);
        }

        /**
         * Notify the {@link TracingContext.ListenerManager} about the given {@link TraceSegment} have finished. And
         * trigger {@link TracingContext.ListenerManager} to notify all {@link #LISTENERS} 's {@link
         * TracingContextListener#afterFinished(TraceSegment)}
         *
         * @param finishedSegment
         */
        static void notifyFinish(TraceSegment finishedSegment) {
            for (TracingContextListener listener : LISTENERS) {
                listener.afterFinished(finishedSegment);
            }
        }

        /**
         * Clear the given {@link TracingContextListener}
         */
        public static synchronized void remove(TracingContextListener listener) {
            LISTENERS.remove(listener);
        }

    }

    /**
     * @return the top element of 'ActiveSpanStack', and remove it.
     */
    private AbstractSpan pop() {
        return activeSpanStack.removeLast();
    }

    /**
     * Add a new Span at the top of 'ActiveSpanStack'
     *
     * @param span
     */
    private AbstractSpan push(AbstractSpan span) {
        activeSpanStack.addLast(span);
        return span;
    }

    /**
     * @return the top element of 'ActiveSpanStack' only.
     */
    private AbstractSpan peek() {
        if (activeSpanStack.isEmpty()) {
            return null;
        }
        return activeSpanStack.getLast();
    }

    private AbstractSpan first() {
        return activeSpanStack.getFirst();
    }

    private boolean isLimitMechanismWorking() {
        return spanIdGenerator >= Config.Agent.SPAN_LIMIT_PER_SEGMENT;
    }

}
