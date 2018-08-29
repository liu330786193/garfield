package com.lyl.garfield.core.plugin.aliyuncs.v4;/*
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

import com.aliyuncs.http.HttpRequest;
import com.aliyuncs.http.HttpResponse;
import com.timevale.cat.api.component.ComponentsDefine;
import com.timevale.cat.api.trace.AbstractSpan;
import com.timevale.cat.api.trace.SpanLayer;
import com.timevale.cat.api.trace.tag.Tags;
import com.timevale.cat.core.context.ContextManager;
import com.timevale.cat.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.timevale.cat.core.plugin.interceptor.enhance.StaticMethodsAroundInterceptor;

import java.lang.reflect.Method;

public class AliyuncsResponseInterceptor implements StaticMethodsAroundInterceptor {

    public static final String ALIYUN_OPERATION_NAME_PREFIX = "Aliyun";

    @Override
    public void beforeMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, MethodInterceptResult result) {
        HttpRequest request = (HttpRequest) allArguments[0];
        String url = request.getUrl();
        String domain = url.substring(0, url.indexOf("?") - 1);
        AbstractSpan span = ContextManager.createExitSpan(ALIYUN_OPERATION_NAME_PREFIX, domain);
        span.setComponent(ComponentsDefine.ALIYUN_OSS);
        Tags.URL.set(span, domain);
        Tags.HTTP.METHOD.set(span, request.getMethod().name());
        SpanLayer.asHttp(span);
    }

    @Override
    public Object afterMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        AbstractSpan span = ContextManager.activeSpan();
        if (ret == null){
            return ret;
        }
        HttpResponse httpResponse = (HttpResponse) ret;
        int statusCode = httpResponse.getStatus();
        if (statusCode >= 400){
            span.errorOccurred();
        }
        Tags.STATUS_CODE.set(span, Integer.toString(statusCode));
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        AbstractSpan activeSpan = ContextManager.activeSpan();
        activeSpan.errorOccurred();
        activeSpan.log(t);
    }
}
