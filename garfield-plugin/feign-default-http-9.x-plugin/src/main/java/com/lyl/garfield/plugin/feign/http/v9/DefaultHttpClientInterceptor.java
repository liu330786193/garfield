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

package com.lyl.garfield.plugin.feign.http.v9;


import com.lyl.garfield.api.component.ComponentsDefine;
import com.lyl.garfield.api.trace.AbstractSpan;
import com.lyl.garfield.api.trace.SpanLayer;
import com.lyl.garfield.api.trace.tag.Tags;
import com.lyl.garfield.core.context.CarrierItem;
import com.lyl.garfield.core.context.ContextCarrier;
import com.lyl.garfield.core.context.ContextManager;
import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;
import feign.Request;
import feign.Response;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * {@link DefaultHttpClientInterceptor} intercept the default implementation of http calls by the Feign.
 *
 * @author peng-yongsheng
 */
public class DefaultHttpClientInterceptor implements InstanceMethodsAroundInterceptor {

    private static final String COMPONENT_NAME = "FeignDefaultHttp";

    /**
     * Get the {@link Request} from {@link EnhancedInstance}, then create {@link AbstractSpan} and set host,
     * port, kind, component, url from {@link Request}.
     * Through the reflection of the way, set the http header of context data into {@link Request#headers}.
     *
     * @param method
     * @param result change this result, if you want to truncate the method.
     * @throws Throwable
     */
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                             Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        Request request = (Request)allArguments[0];

        URL url = new URL(request.url());
        ContextCarrier contextCarrier = new ContextCarrier();
        int port = url.getPort() == -1 ? 80 : url.getPort();
        String remotePeer = url.getHost() + ":" + port;
        String operationName = url.getPath();
        if (operationName == null || operationName.length() == 0) {
            operationName = "/";
        }
        AbstractSpan span = ContextManager.createExitSpan(operationName, contextCarrier, remotePeer);
        span.setComponent(ComponentsDefine.FEIGN);
        Tags.HTTP.METHOD.set(span, request.method());
        Tags.URL.set(span, request.url());
        SpanLayer.asHttp(span);

        Field headersField = Request.class.getDeclaredField("headers");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headersField, headersField.getModifiers() & ~Modifier.FINAL);

        headersField.setAccessible(true);
        Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
        CarrierItem next = contextCarrier.items();
        while (next.hasNext()) {
            next = next.next();
            List<String> contextCollection = new LinkedList<String>();
            contextCollection.add(next.getHeadValue());
            headers.put(next.getHeadKey(), contextCollection);
        }
        headers.putAll(request.headers());

        headersField.set(request, Collections.unmodifiableMap(headers));
    }

    /**
     * Get the status code from {@link Response}, when status code greater than 400, it means there was some errors in
     * the server.
     * Finish the {@link AbstractSpan}.
     *
     * @param method
     * @param ret the method's original return value.
     * @return
     * @throws Throwable
     */
    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        AbstractSpan span = ContextManager.activeSpan();
        //返回结果有可能为null
        if (ret != null) {
            Response response = (Response)ret;
            int statusCode = response.status();
            if (statusCode >= 400) {
                span.errorOccurred();
            }
            Tags.STATUS_CODE.set(span, Integer.toString(statusCode));
        }else {
            span.errorOccurred();
            Tags.STATUS_CODE.set(span, "500");
        }

        ContextManager.stopSpan();
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                                Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }
}
