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

package com.lyl.garfield.plugin.mysql.v5.define;

import com.lyl.garfield.core.plugin.match.ClassMatch;

import static com.lyl.garfield.core.plugin.match.NameMatch.byName;

/**
 * of mysql driver jar.
 *
 * @author zhangxin
 */
public class Mysql50ConnectionInstrumentation extends ConnectionInstrumentation {
    @Override
    protected ClassMatch enhanceClass() {
        return byName("com.mysql.jdbc.Connection");
    }

    @Override protected String[] witnessClasses() {
        return new String[] {"com.mysql.jdbc.CursorRowProvider"};
    }
}
