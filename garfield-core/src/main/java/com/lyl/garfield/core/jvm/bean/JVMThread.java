package com.lyl.garfield.core.jvm.bean;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author mozilla 2017年12月25日 下午2:25:16
 */
public class JVMThread implements JVMThreadMBean {

    private volatile long         lastCPUTime;
    private volatile long         lastCPUUpTime;
    private OperatingSystemMXBean OperatingSystem;
    private RuntimeMXBean         Runtime;

    private static JVMThread      instance = new JVMThread();

    public static JVMThread getInstance() {
        return instance;
    }

    private ThreadMXBean threadMXBean;

    private JVMThread(){
        threadMXBean = ManagementFactory.getThreadMXBean();
        OperatingSystem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Runtime = ManagementFactory.getRuntimeMXBean();

        try {
            lastCPUTime = OperatingSystem.getProcessCpuTime();
        } catch (Exception e) {
          //  LOG.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        JVMThread.getInstance().getProcessCpuTimeRate();
    }

    @Override
    public BigDecimal getProcessCpuTimeRate() {
        long cpuTime = OperatingSystem.getProcessCpuTime();
        long upTime = Runtime.getUptime();

        long elapsedCpu = cpuTime - lastCPUTime;
        long elapsedTime = upTime - lastCPUUpTime;

        lastCPUTime = cpuTime;
        lastCPUUpTime = upTime;

        BigDecimal cpuRate;
        if (elapsedTime <= 0) {
            return new BigDecimal(0);
        }

        float cpuUsage = elapsedCpu / (elapsedTime * 10000F);
        cpuRate = new BigDecimal(cpuUsage, new MathContext(4));

        return cpuRate.divide(new BigDecimal(java.lang.Runtime.getRuntime().availableProcessors()));
    }

    @Override
    public int getDaemonThreadCount() {
        return threadMXBean.getDaemonThreadCount();
    }

    @Override
    public int getThreadCount() {
        return threadMXBean.getThreadCount();
    }

    @Override
    public long getTotalStartedThreadCount() {
        return threadMXBean.getTotalStartedThreadCount();
    }

    @Override
    public int getDeadLockedThreadCount() {
        try {
            long[] deadLockedThreadIds = threadMXBean.findDeadlockedThreads();
            if (deadLockedThreadIds == null) {
                return 0;
            }
            return deadLockedThreadIds.length;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    // TODO 可以增加dump 线程的功能
}
