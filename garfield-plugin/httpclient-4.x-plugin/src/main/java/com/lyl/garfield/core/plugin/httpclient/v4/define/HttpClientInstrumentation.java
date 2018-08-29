package com.lyl.garfield.core.plugin.httpclient.v4.define;/*
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
import com.timevale.cat.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;

/**
 * {@link HttpClientInstrumentation} present that skywalking intercepts {@link HttpClientInstrumentation#enhanceClass()}
 *
 * @author zhangxin
 */
public abstract class HttpClientInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String INTERCEPT_CLASS = "com.timevale.cat.plugin.httpclient.v4.HttpClientExecuteInterceptor";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
    }

    protected String getInstanceMethodsInterceptor() {
        return INTERCEPT_CLASS;
    }
}
