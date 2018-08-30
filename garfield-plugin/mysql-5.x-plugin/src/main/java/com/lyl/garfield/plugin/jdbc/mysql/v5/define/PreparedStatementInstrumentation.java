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

package com.lyl.garfield.plugin.jdbc.mysql.v5.define;


import com.lyl.garfield.core.plugin.interceptor.ConstructorInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.lyl.garfield.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.lyl.garfield.core.plugin.match.ClassMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * {@link PreparedStatementInstrumentation} define that the mysql-2.x plugin intercepts the following methods in the
 * com.mysql.cj.jdbc.PreparedStatement} class:
 * 1. execute <br/>
 * 2. executeQuery <br/>
 * 3. executeUpdate <br/>
 * 4. executeLargeUpdate <br/>
 * 5. addBatch <br/>
 *
 * @author zhangxin
 */
public class PreparedStatementInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String PREPARED_STATEMENT_CLASS_NAME = "com.mysql.jdbc.PreparedStatement";
    private static final String SERVICE_METHOD_INTERCEPTOR = "com.lyl.garfield.plugin.jdbc.mysql.StatementExecuteMethodsInterceptor";
    public static final String MYSQL6_PREPARED_STATEMENT_CLASS_NAME = "com.mysql.cj.jdbc.PreparedStatement";
    public static final String JDBC42_PREPARED_STATEMENT_CLASS_NAME = "com.mysql.jdbc.JDBC42PreparedStatement";

    @Override protected final ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected final InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("execute")
                        .or(named("executeQuery"))
                        .or(named("executeUpdate"))
                        .or(named("executeLargeUpdate"))
                        .or(named("addBatch"));
                }

                @Override public String getMethodsInterceptor() {
                    return SERVICE_METHOD_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(PREPARED_STATEMENT_CLASS_NAME, MYSQL6_PREPARED_STATEMENT_CLASS_NAME, JDBC42_PREPARED_STATEMENT_CLASS_NAME);
    }
}
