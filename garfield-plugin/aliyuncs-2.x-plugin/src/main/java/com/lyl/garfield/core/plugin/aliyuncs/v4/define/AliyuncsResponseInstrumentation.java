package com.lyl.garfield.core.plugin.aliyuncs.v4.define;/*
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

import com.timevale.cat.core.plugin.interceptor.ConstructorInterceptPoint;
import com.timevale.cat.core.plugin.interceptor.StaticMethodsInterceptPoint;
import com.timevale.cat.core.plugin.interceptor.enhance.ClassStaticMethodsEnhancePluginDefine;
import com.timevale.cat.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.timevale.cat.core.plugin.match.NameMatch.byName;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

/**
 *
 * @author zhangxin
 */
public class AliyuncsResponseInstrumentation extends ClassStaticMethodsEnhancePluginDefine {

    private static final String RESPONSE_ENHANCE_CLASS = "com.aliyuncs.http.HttpResponse";

    private static final String RESPONSE_METHOD = "getResponse";

    private static final String RESPONSE_INTERCEPT_CLASS = "com.timevale.cat.plugin.aliyuncs.v4.AliyuncsResponseInterceptor";


    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(RESPONSE_ENHANCE_CLASS);
    }

    @Override
    protected StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return new StaticMethodsInterceptPoint[]{
                new StaticMethodsInterceptPoint(){
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(RESPONSE_METHOD).and(takesArguments(1));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return RESPONSE_INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

}
