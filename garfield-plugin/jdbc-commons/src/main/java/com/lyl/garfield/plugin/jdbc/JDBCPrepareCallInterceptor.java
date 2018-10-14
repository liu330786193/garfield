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

import com.lyl.garfield.core.plugin.interceptor.enhance.EnhancedInstance;
import com.lyl.garfield.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import com.lyl.garfield.core.plugin.interceptor.enhance.MethodInterceptResult;
import com.lyl.garfield.plugin.jdbc.trace.ConnectionInfo;
import com.lyl.garfield.plugin.jdbc.trace.SWCallableStatement;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;

/**
 * {@link JDBCPrepareCallInterceptor} return {@link SWCallableStatement} instance that wrapper the real CallStatement
 * instance when the client call <code>prepareCall</code> method.
 *
 * @author zhangxin
 */
public class JDBCPrepareCallInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {

    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        if (objInst.getSkyWalkingDynamicField() == null) {
            return ret;
        }
        return new SWCallableStatement((Connection)objInst, (CallableStatement)ret, (ConnectionInfo)objInst.getSkyWalkingDynamicField(), (String)allArguments[0]);
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {

    }
}
