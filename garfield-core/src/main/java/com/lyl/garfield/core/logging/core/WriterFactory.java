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

package com.lyl.garfield.core.logging.core;


import com.timevale.cat.core.boot.AgentPackageNotFoundException;
import com.timevale.cat.core.boot.AgentPackagePath;
import com.timevale.cat.core.conf.Config;
import com.timevale.cat.core.utils.StringUtil;

public class WriterFactory {
    public static IWriter getLogWriter() {
        if (AgentPackagePath.isPathFound()) {
            if (StringUtil.isEmpty(Config.Logging.DIR)) {
                try {
                    Config.Logging.DIR = AgentPackagePath.getPath() + "/logs";
                } catch (AgentPackageNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return FileWriter.get();
        } else {
            return SystemOutWriter.INSTANCE;
        }
    }
}
