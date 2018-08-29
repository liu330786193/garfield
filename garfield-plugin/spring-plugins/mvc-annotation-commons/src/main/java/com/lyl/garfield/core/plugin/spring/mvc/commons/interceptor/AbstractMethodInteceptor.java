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

package com.lyl.garfield.core.plugin.spring.mvc.commons.interceptor;

import com.timevale.cat.api.component.ComponentsDefine;
import com.timevale.cat.api.trace.AbstractSpan;
import com.timevale.cat.api.trace.SpanLayer;
import com.timevale.cat.api.trace.tag.Tags;
import com.timevale.cat.core.context.CarrierItem;
import com.timevale.cat.core.context.ContextCarrier;
import com.timevale.cat.core.context.ContextManager;
import com.timevale.cat.core.plugin.interceptor.enhance.EnhancedInstance;
import com.timevale.cat.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.timevale.cat.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.timevale.cat.plugin.spring.mvc.commons.EnhanceRequireObjectCache;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * the abstract method inteceptor
 */
public abstract class AbstractMethodInteceptor implements InstanceMethodsAroundInterceptor {
    public abstract String getRequestURL(Method method);

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {
        EnhanceRequireObjectCache pathMappingCache = (EnhanceRequireObjectCache)objInst.getSkyWalkingDynamicField();
        String requestURL = pathMappingCache.findPathMapping(method);
        if (requestURL == null) {
            requestURL = getRequestURL(method);
            pathMappingCache.addPathMapping(method, requestURL);
            requestURL = pathMappingCache.findPathMapping(method);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ContextCarrier contextCarrier = new ContextCarrier();
        CarrierItem next = contextCarrier.items();
        while (next.hasNext()) {
            next = next.next();
            next.setHeadValue(request.getHeader(next.getHeadKey()));
        }

        AbstractSpan span = ContextManager.createEntrySpan(requestURL, contextCarrier);
        Tags.URL.set(span, request.getRequestURL().toString());
        Tags.HTTP.METHOD.set(span, request.getMethod());
        span.setComponent(ComponentsDefine.SPRING_MVC_ANNOTATION);
        SpanLayer.asHttp(span);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        HttpServletResponse response = ((EnhanceRequireObjectCache)objInst.getSkyWalkingDynamicField()).getHttpServletResponse();

        AbstractSpan span = ContextManager.activeSpan();
        if (response.getStatus() >= 400) {
            span.errorOccurred();
        }
        Tags.STATUS_CODE.set(span, Integer.toString(response.getStatus()));
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }
}
