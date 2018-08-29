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

package com.lyl.garfield.api.trace;


/**
 * The <code>ExitSpan</code> represents a jvm consumer point, such as Feign, Okhttp client for a Http jvm.
 *
 * It is an exit point or a leaf span(our old name) of datacarrier tree.
 * In a single rpc call, because of a combination of discovery libs, there maybe contain multi-layer exit point:
 *
 * The <code>ExitSpan</code> only presents the first one.
 *
 * Such as: Dubbox -> Apache Httpcomponent -> ....(Remote)
 * The <code>ExitSpan</code> represents the Dubbox span, and ignore the httpcomponent span's info.
 *
 * @author wusheng
 */
public class ExitSpan extends StackBasedTracingSpan implements WithPeerInfo {

    private String peer;

    public ExitSpan(int spanId, int parentSpanId, String operationName, String peer) {
        super(spanId, parentSpanId, operationName);
        this.peer = peer;
    }

    /**
     * Set the {@link #startTime}, when the first start, which means the first jvm provided.
     */
    @Override
    public ExitSpan start() {
        if (++stackDepth == 1) {
            super.start();
        }
        return this;
    }

    @Override
    public ExitSpan tag(String key, String value) {
        if (stackDepth == 1) {
            super.tag(key, value);
        }
        return this;
    }

    @Override
    public DefaultTracingSpan setLayer(SpanLayer layer) {
        if (stackDepth == 1) {
            return super.setLayer(layer);
        } else {
            return this;
        }
    }

    @Override
    public DefaultTracingSpan setComponent(Component component) {
        if (stackDepth == 1) {
            return super.setComponent(component);
        } else {
            return this;
        }
    }

    @Override
    public ExitSpan log(Throwable t) {
        if (stackDepth == 1) {
            super.log(t);
        }
        return this;
    }

    @Override
    public DefaultTracingSpan setOperationName(String operationName) {
        if (stackDepth == 1) {
            return super.setOperationName(operationName);
        } else {
            return this;
        }
    }

    @Override public boolean isEntry() {
        return false;
    }

    @Override public boolean isExit() {
        return true;
    }

    @Override
    public String getPeer() {
        return peer;
    }
}
