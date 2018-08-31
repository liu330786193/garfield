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

package com.lyl.garfield.plugin.toolkit.trace.activation;

import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.lyl.garfield.core.plugin.match.MethodAnnotationMatch.byMethodAnnotationMatch;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * {@link TraceAnnotationActivation} enhance all method that annotated with <code>org.skywalking.apm.toolkit.datacarrier.annotation.Trace</code>
 * by <code>org.skywalking.apm.toolkit.activation.datacarrier.TraceAnnotationMethodInterceptor</code>.
 *
 * @author zhangxin
 */
public class TraceAnnotationActivation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String TRACE_ANNOTATION_METHOD_INTERCEPTOR = "com.lyl.garfield.plugin.toolkit.trace.activation.TraceAnnotationMethodInterceptor";
    public static final String TRACE_ANNOTATION = "com.lyl.garfield.toolkit.trace.Trace";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return isAnnotatedWith(named(TRACE_ANNOTATION));
                }

                @Override public String getMethodsInterceptor() {
                    return TRACE_ANNOTATION_METHOD_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return byMethodAnnotationMatch(new String[] {TRACE_ANNOTATION});
    }
}
