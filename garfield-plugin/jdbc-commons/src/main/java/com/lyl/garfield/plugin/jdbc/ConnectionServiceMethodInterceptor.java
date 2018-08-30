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

package com.lyl.garfield.plugin.jdbc;

import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.SpanLayer;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.lyl.garfield.plugin.jdbc.trace.ConnectionInfo;

import java.lang.reflect.Method;

/**
 * {@link ConnectionServiceMethodInterceptor} create an exit span when the following methods execute:
 * 1. close
 * 2. rollback
 * 3. releaseSavepoint
 * 4. commit
 *
 * @author zhangxin
 */
public class ConnectionServiceMethodInterceptor implements InstanceMethodsAroundInterceptor {

    @Override
    public final void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                   Class<?>[] argumentsTypes,
                                   MethodInterceptResult result) throws Throwable {
        ConnectionInfo connectInfo = (ConnectionInfo)objInst.getSkyWalkingDynamicField();
        AbstractSpan span = ContextManager.createExitSpan(connectInfo.getDBType() + "/JDBI/Connection/" + method.getName(), connectInfo.getDatabasePeer());
        Tags.DB_TYPE.set(span, "sql");
        Tags.DB_INSTANCE.set(span, connectInfo.getDatabaseName());
        Tags.DB_STATEMENT.set(span, "");
        span.setComponent(connectInfo.getComponent());
        SpanLayer.asDB(span);
    }

    @Override
    public final Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override public final void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }

}
