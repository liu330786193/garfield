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
package com.lyl.garfield.core.boot;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangkewei
 */
public class DefaultNamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger BOOT_SERVICE_SEQ = new AtomicInteger(0);
    private final AtomicInteger threadSeq = new AtomicInteger(0);
    private final String namePrefix;
    public DefaultNamedThreadFactory(String name) {
        namePrefix = "SkywalkingAgent-" + BOOT_SERVICE_SEQ.incrementAndGet() + "-" + name + "-";
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r,namePrefix + threadSeq.getAndIncrement());
        t.setDaemon(true);
        return t;
    }
}
