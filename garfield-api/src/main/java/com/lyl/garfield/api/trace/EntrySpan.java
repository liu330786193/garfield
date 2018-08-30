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

import com.lyl.garfield.api.component.Component;

/**
 * The <code>EntrySpan</code> represents a jvm provider point, such as tomcat server entrance.
 *
 * It is a start point of {@link TraceSegment}, even in a complex appligarfieldion, there maybe have multi-layer entry point,
 * the <code>EntrySpan</code> only represents the first one.
 *
 * But with the last <code>EntrySpan</code>'s tag and logs, which have more details about a jvm provider.
 *
 * Such as: tomcat Embed -> Dubbox
 * The <code>EntrySpan</code> represents the Dubbox span.
 *
 * @author wusheng
 */
public class EntrySpan extends StackBasedTracingSpan {
    private int currentMaxDepth;

    public EntrySpan(int spanId, int parentSpanId, String operationName) {
        super(spanId, parentSpanId, operationName);
        this.currentMaxDepth = 0;
    }

    @Override
    public EntrySpan start() {
        if ((currentMaxDepth = ++stackDepth) == 1) {
            super.start();
        }
        clearWhenRestart();
        return this;
    }

    @Override
    public EntrySpan tag(String key, String value) {
        if (stackDepth == currentMaxDepth) {
            super.tag(key, value);
        }
        return this;
    }

    @Override
    public DefaultTracingSpan setLayer(SpanLayer layer) {
        if (stackDepth == currentMaxDepth) {
            return super.setLayer(layer);
        } else {
            return this;
        }
    }

    @Override
    public DefaultTracingSpan setComponent(Component component) {
        if (stackDepth == currentMaxDepth) {
            return super.setComponent(component);
        } else {
            return this;
        }
    }

    @Override
    public DefaultTracingSpan setOperationName(String operationName) {
        if (stackDepth == currentMaxDepth) {
            return super.setOperationName(operationName);
        } else {
            return this;
        }
    }

    @Override
    public EntrySpan log(Throwable t) {
        super.log(t);
        return this;
    }


    @Override public boolean isEntry() {
        return true;
    }

    @Override public boolean isExit() {
        return false;
    }

    private void clearWhenRestart() {
        this.componentId = DictionaryUtil.nullValue();
        this.layer = null;
        this.logs = null;
        this.tags = null;
    }
}
