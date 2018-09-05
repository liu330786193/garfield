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

package com.lyl.garfield.core.conf;

import com.lyl.garfield.core.logging.core.LogLevel;

/**
 * This is the core config in sniffer agent.
 *
 * @author wusheng
 */
public class Config {


    public static class Agent {

        public static String APPLICATION_ID = "lyl";

        public static int SAMPLE_N_PER_3_SECS = -1;

        public static String IGNORE_SUFFIX = ".jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg";

        public static int SPAN_LIMIT_PER_SEGMENT = 300;

        public static boolean IS_OPEN_DEBUGGING_CLASS = false;
    }

    public static class Logging{

        public static String FILE_NAME = "garfield-agent.log";

        public static String DIR = "";

        public static int MAX_FILE_SIZE = 300 * 1024 * 1024;

        public static LogLevel LEVEL = LogLevel.DEBUG;
    }

    public static class Buffer {

        public static int CHANNEL_SIZE = 5;

        public static int BUFFER_SIZE = 300;
    }

    public static class Jvm {

        public static int INITIAL_DELAY = 10;

        public static int PERIOD = 5;
    }

    public static class Trace {

        public static int WAIT_TIME = 20;
    }

    public static class Plugin {
        public static class MongoDB {
            /**
             * If true, trace all the parameters, default is false. Only trace the operation, not include parameters.
             */
            public static boolean TRACE_PARAM = false;
        }
    }
}
