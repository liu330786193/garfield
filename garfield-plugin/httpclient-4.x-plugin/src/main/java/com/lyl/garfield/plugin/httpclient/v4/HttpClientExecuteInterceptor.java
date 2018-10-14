package com.lyl.garfield.plugin.httpclient.v4;/*
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
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClientExecuteInterceptor implements InstanceMethodsAroundInterceptor {

    public static final int IGNORE_PORT = 7001;

    @Override public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                       Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        if (allArguments[0] == null || allArguments[1] == null) {
            // illegal args, can't trace. ignore.
            return;
        }
        final HttpHost httpHost = (HttpHost)allArguments[0];
        HttpRequest httpRequest = (HttpRequest)allArguments[1];
        final ContextCarrier contextCarrier = new ContextCarrier();
        AbstractSpan span = null;

        String remotePeer = httpHost.getHostName() + ":" + (httpHost.getPort() > 0 ? httpHost.getPort() :
                "https".equals(httpHost.getSchemeName().toLowerCase()) ? 443 : 80);

        try {
            URL url = new URL(httpRequest.getRequestLine().getUri());
            span = ContextManager.createExitSpan(url.getPath(), contextCarrier, remotePeer);
        } catch (MalformedURLException e) {
            throw e;
        }

        span.setComponent(ComponentsDefine.HTTPCLIENT);
        Tags.URL.set(span, httpRequest.getRequestLine().getUri());
        Tags.HTTP.METHOD.set(span, httpRequest.getRequestLine().getMethod());
        SpanLayer.asHttp(span);

        CarrierItem next = contextCarrier.items();
        while (next.hasNext()) {
            next = next.next();
            httpRequest.setHeader(next.getHeadKey(), next.getHeadValue());
        }
    }

    @Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                                        Class<?>[] argumentsTypes, Object ret) throws Throwable {
        if (allArguments[0] == null || allArguments[1] == null || ret == null) {
            return ret;
        }

        HttpResponse response = (HttpResponse)ret;
        int statusCode = response.getStatusLine().getStatusCode();
        AbstractSpan span = ContextManager.activeSpan();

        //去除心跳信息
        //如果不是ip的话是域名的话 host.getPort()的值为-1
        HttpHost host = (HttpHost) allArguments[0];
        if (host.getPort() == IGNORE_PORT){
            if (statusCode >= 400){
                span.errorOccurred();
                Tags.STATUS_CODE.set(span, Integer.toString(statusCode));
                ContextManager.stopSpan();
            }
            ContextManager.ignoreTrace();
            return ret;
        }
        //正常的http请求信息
        if (statusCode >= 400) {
            span.errorOccurred();
        }
        Tags.STATUS_CODE.set(span, Integer.toString(statusCode));
        ContextManager.stopSpan();
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                                Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan activeSpan = ContextManager.activeSpan();
        activeSpan.errorOccurred();
        activeSpan.log(t);
    }
}
