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

package com.lyl.garfield.core.plugin.toolkit.trace.activation;

import com.timevale.cat.api.trace.AbstractSpan;
import com.timevale.cat.api.trace.tag.Tags;
import com.timevale.cat.core.context.ContextManager;
import com.timevale.cat.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.timevale.cat.core.plugin.interceptor.enhance.StaticMethodsAroundInterceptor;

import java.lang.reflect.Method;

public class ActiveSpanLogInterceptor implements StaticMethodsAroundInterceptor {
    @Override
    public void beforeMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes,
                                       MethodInterceptResult result) {
        AbstractSpan activeSpan = null;
        try {
            activeSpan = ContextManager.activeSpan();
            activeSpan.log((Throwable) allArguments[0]);
            activeSpan.errorOccurred();
            Tags.STATUS_CODE.set(activeSpan, "500");
            Tags.URL.set(activeSpan, activeSpan.getOperationName());
        } catch (NullPointerException e) {
        }
    }

    @Override
    public Object afterMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes,
        Object ret) {
        return ret;
    }

    @Override
    public void handleMethodException(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes,
        Throwable t) {

    }
}
