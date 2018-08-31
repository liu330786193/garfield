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

package com.lyl.garfield.api.component;

/**
 * The supported list of skywalking java sniffer.
 *
 * @author wusheng
 */
public class ComponentsDefine {

    public static final OfficialComponent TOMCAT = new OfficialComponent(1, "tomcat");

    public static final OfficialComponent HTTPCLIENT = new OfficialComponent(2, "HttpClient");

    public static final OfficialComponent MYSQL = new OfficialComponent(3, "Mysql");

    public static final OfficialComponent ORACLE = new OfficialComponent(4, "ORACLE");

    public static final OfficialComponent FEIGN = new OfficialComponent(5, "Feign");

    public static final OfficialComponent SPRING_MVC_ANNOTATION = new OfficialComponent(6, "SpringMVC");
  
    public static final OfficialComponent ACTIVE_MQ = new OfficialComponent(7, "ActiveMQ");

    public static final OfficialComponent HTTP_ASYNC_CLIENT = new OfficialComponent(8, "http_async_client");

    public static final OfficialComponent ALIYUN_OSS = new OfficialComponent(9, "Aliyun_oss");

    public static final OfficialComponent MONGODB = new OfficialComponent(10, "MongoDB");

    private static ComponentsDefine instance = new ComponentsDefine();

    private String[] components;

    public static ComponentsDefine getInstance() {
        return instance;
    }

    public ComponentsDefine() {
        components = new String[11];
        addComponent(TOMCAT);
        addComponent(HTTPCLIENT);
        addComponent(MYSQL);
        addComponent(ORACLE);
        addComponent(FEIGN);
        addComponent(SPRING_MVC_ANNOTATION);
        addComponent(ACTIVE_MQ);
        addComponent(HTTP_ASYNC_CLIENT);
        addComponent(ALIYUN_OSS);
        addComponent(MONGODB);
    }

    private void addComponent(OfficialComponent component) {
        components[component.getId()] = component.getName();
    }

    public String getComponentName(int componentId) {
        if (componentId > components.length - 1 || componentId == 0) {
            return null;
        } else {
            return components[componentId];
        }
    }
}
