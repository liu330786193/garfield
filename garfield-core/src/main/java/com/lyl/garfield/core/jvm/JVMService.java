package com.lyl.garfield.core.jvm;/*
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

import com.timevale.cat.api.jvm.SystemDTO;
import com.timevale.cat.core.boot.BootService;
import com.timevale.cat.core.boot.DefaultNamedThreadFactory;
import com.timevale.cat.core.conf.Config;
import com.timevale.cat.core.constants.CatAgentConstants;
import com.timevale.cat.core.jvm.collector.JvmCollector;
import com.timevale.cat.core.jvm.collector.system.*;
import com.timevale.cat.core.kafka.KafkaSender;
import com.timevale.cat.core.utils.NetworkSingleton;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wusheng
 */

public class JVMService implements BootService {

    private volatile ScheduledExecutorService collectScheduledExecutor;
    private KafkaSender producer;

    private volatile ScheduledFuture<?> threadScheduledFuture;
    private volatile ScheduledFuture<?> gcScheduledFuture;
    private volatile ScheduledFuture<?> memoryScheduledFuture;


    private AtomicInteger count = new AtomicInteger();

    @Override
    public void beforeBoot() throws Throwable {
        collectScheduledExecutor = new ScheduledThreadPoolExecutor(4, new DefaultNamedThreadFactory("JVMService-produce"));
    }


    @Override
    public void boot() throws Throwable {
        //发送基础数据
//        sendBaseJvmInfo();
        //发送定时数据
        sendFixedRateJvmInf();
    }

    //这个暂时不收集不处理
    /*private void sendBaseJvmInfo(){

        new Thread(() -> {
            producer.send(JvmCollector.getJvmBase());
        }).start();
    }*/

    private void sendFixedRateJvmInf(){
        memoryScheduledFuture = collectScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                KafkaSender.getInstance().sendMemeory(JvmCollector.getJvmMemory());
            }
        }, Config.Jvm.INITIAL_DELAY, Config.Jvm.PERIOD, TimeUnit.SECONDS);
        gcScheduledFuture = collectScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                KafkaSender.getInstance().sendGc(JvmCollector.getJvmGC());
            }
        }, Config.Jvm.INITIAL_DELAY, Config.Jvm.PERIOD, TimeUnit.SECONDS);
        threadScheduledFuture = collectScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                KafkaSender.getInstance().sendThread(JvmCollector.getJvmThread());
            }
        }, Config.Jvm.INITIAL_DELAY, Config.Jvm.PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void afterBoot() throws Throwable {
    }

    @Override
    public void shutdown() throws Throwable {
        memoryScheduledFuture.cancel(true);
        gcScheduledFuture.cancel(true);
        threadScheduledFuture.cancel(true);
        collectScheduledExecutor.shutdown();
    }

    //判断是否是静态方法
    public static boolean isLinuxSystem(){
        String os = System.getProperty("os.name");
        return os != null && !os.contains("Mac") && !os.contains("Windows");
    }

    //构造系统级别数据 磁盘 IO 负载 内存 传输
    private static SystemDTO buildSystemDTO(){
        SystemDTO systemDTO = new SystemDTO();
        systemDTO.setApp(Config.Agent.APPLICATION_ID);
        systemDTO.setCat(CatAgentConstants.CATEGORY_SYSTEM);
        systemDTO.setTimestamp(System.currentTimeMillis());
        systemDTO.setIp(NetworkSingleton.getInstance().getIp());
        systemDTO.setInstanceId(NetworkSingleton.getInstance().getInstanceId());
        systemDTO.setCpu(CpuCollector.collect());
        systemDTO.setIo(IOCollector.collect());
        systemDTO.setLoad(LoadCollector.collect());
        systemDTO.setMemory(MemoryCollector.collect());
        systemDTO.setTcp(TCPCollector.collect());
        systemDTO.setTraffic(TrafficCollector.collect());
        return systemDTO;
    }

}
