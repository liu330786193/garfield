package com.lyl.garfield.plugin.druid.v1;/*
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

import com.aliyun.oss.internal.OSSObjectOperation;
import com.aliyuncs.http.HttpRequest;
import com.aliyuncs.http.HttpResponse;
import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.LocalSpan;
import com.lyl.garfield.api.trace.SpanLayer;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;

public class AliyuncsMetadataInterceptor implements InstanceMethodsAroundInterceptor {

    public static final String ALIYUN_METADATA_OPERATION_NAME_PREFIX = "Aliyun/metadata";

    //如果错误码等于NoSuchKey 则忽略这个异常
    public static final String ALIYUN_METADATA_ERROR_CODE = "NoSuchKey";


    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        HttpRequest request = (HttpRequest) allArguments[0];
        OSSObjectOperation operation = (OSSObjectOperation) objInst;
        String domain = operation.getEndpoint().toString();
        AbstractSpan span = ContextManager.createExitSpan(ALIYUN_METADATA_OPERATION_NAME_PREFIX, domain);
        span.setComponent(ComponentsDefine.ALIYUN_OSS);
        Tags.URL.set(span, domain);
        Tags.HTTP.METHOD.set(span, request.getMethod().name());
        SpanLayer.asHttp(span);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        int statusCode = 500;
        if (ret != null){
            HttpResponse httpResponse = (HttpResponse) ret;
            statusCode = httpResponse.getStatus();
        }
        LocalSpan span = (LocalSpan) ContextManager.activeSpan();
        Tags.STATUS_CODE.set(span, Integer.toString(statusCode));
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan activeSpan = ContextManager.activeSpan();
        activeSpan.errorOccurred();
        activeSpan.log(t);
    }
}
