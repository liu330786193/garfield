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


import com.timevale.cat.api.trace.DictionaryUtil;
import com.timevale.cat.core.context.ids.ID;
import com.timevale.cat.core.utils.StringUtil;

import java.util.List;

/**
 * The <code>ContextSnapshot</code> is a snapshot for current context. The snapshot carries the info for building
 * reference between two segments in two thread, but have a causal relationship.
 *
 * @author wusheng
 */
public class ContextSnapshot {
    /**
     * datacarrier segment id of the parent datacarrier segment.
     */
    private ID traceSegmentId;

    /**
     * span id of the parent span, in parent datacarrier segment.
     */
    private int spanId = -1;

    private String entryOperationName;

    private String parentOperationName;

    private String primaryDistributedTraceId;

    private String entryApplicationInstanceId = DictionaryUtil.emptyValue();

    ContextSnapshot(ID traceSegmentId, int spanId,
                    List<String> distributedTraceIds) {
        this.traceSegmentId = traceSegmentId;
        this.spanId = spanId;
        if (distributedTraceIds != null) {
            this.primaryDistributedTraceId = distributedTraceIds.get(0);
        }
    }

    public void setEntryOperationName(String entryOperationName) {
        this.entryOperationName = "#" + entryOperationName;
    }

    public void setParentOperationName(String parentOperationName) {
        this.parentOperationName = "#".concat(parentOperationName);
    }

    public String getDistributedTraceId() {
        return primaryDistributedTraceId;
    }

    public ID getTraceSegmentId() {
        return traceSegmentId;
    }

    public int getSpanId() {
        return spanId;
    }

    public String getParentOperationName() {
        return parentOperationName;
    }

    public boolean isValid() {
        return traceSegmentId != null
            && spanId > -1
            && entryApplicationInstanceId != DictionaryUtil.emptyValue()
            && primaryDistributedTraceId != null
            && !StringUtil.isEmpty(entryOperationName)
            && !StringUtil.isEmpty(parentOperationName);
    }

    public String getEntryOperationName() {
        return entryOperationName;
    }

    public void setEntryApplicationInstanceId(String entryApplicationInstanceId) {
        this.entryApplicationInstanceId = entryApplicationInstanceId;
    }

    public String getEntryApplicationInstanceId() {
        return entryApplicationInstanceId;
    }
    
    public boolean isFromCurrent() {
        return traceSegmentId.equals(ContextManager.capture().getTraceSegmentId());
    }
}
