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

package com.lyl.garfield.api.trace.tag;


import java.io.Serializable;

public final class Tags implements Serializable {
    private Tags() {
    }

    public static final StringTag URL = new StringTag("url");


    public static final StringTag STATUS_CODE = new StringTag("status_code");


    public static final StringTag DB_TYPE = new StringTag("db.type");


    public static final StringTag DB_INSTANCE = new StringTag("db.instance");


    public static final StringTag DB_STATEMENT = new StringTag("db.statement");


    public static final StringTag DB_BIND_VARIABLES = new StringTag("db.bind_vars");

    public static final StringTag SIP = new StringTag("sip");

    public static final class HTTP {
        public static final StringTag METHOD = new StringTag("http.method");
    }
}
