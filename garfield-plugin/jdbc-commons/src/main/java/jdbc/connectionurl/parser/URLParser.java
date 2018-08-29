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

package jdbc.connectionurl.parser;

import com.timevale.cat.plugin.jdbc.trace.ConnectionInfo;

/**
 * {@link URLParser#parser(String)} support parse the connection url, such as Mysql, Oracle, H2 Database. But there are
 * some url cannot be parsed, such as Oracle connection url with multiple host.
 *
 * @author zhangxin
 */
public class URLParser {

    private static final String MYSQL_JDBC_URL_PREFIX = "jdbc:mysql";

    public static ConnectionInfo parser(String url) {
        ConnectionURLParser parser = null;
        if (url.startsWith(MYSQL_JDBC_URL_PREFIX)) {
            parser = new MysqlURLParser(url);
        }
        return parser.parse();
    }
}
