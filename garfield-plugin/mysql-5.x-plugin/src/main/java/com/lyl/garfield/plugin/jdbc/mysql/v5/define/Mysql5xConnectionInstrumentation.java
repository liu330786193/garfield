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

import com.lyl.garfield.core.plugin.match.ClassMatch;


public class Mysql5xConnectionInstrumentation extends ConnectionInstrumentation {
    public static final String ENHANCE_CLASS = "com.mysql.jdbc.ConnectionImpl";

    public static final String CJ_JDBC_ENHANCE_CLASS = "com.mysql.cj.jdbc.ConnectionImpl";

    @Override protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(ENHANCE_CLASS, CJ_JDBC_ENHANCE_CLASS);
    }
}