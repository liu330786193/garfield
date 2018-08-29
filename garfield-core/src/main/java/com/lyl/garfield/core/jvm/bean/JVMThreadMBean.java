package com.lyl.garfield.core.jvm.bean;

import java.math.BigDecimal;

public interface JVMThreadMBean {

    int getDaemonThreadCount();

    int getThreadCount();

    long getTotalStartedThreadCount();

    int getDeadLockedThreadCount();

    BigDecimal getProcessCpuTimeRate();
}
