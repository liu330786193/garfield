package com.lyl.garfield.core.jvm.collector.system;

import com.timevale.cat.core.utils.MixAll;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.timevale.cat.core.constants.CatAgentItemConstants.*;
import static com.timevale.cat.core.utils.MixAll.formatDouble;

/**
 * http://www.linuxhowtos.org/System/procstat.htm
 * http://www.cnblogs.com/no7dw/archive/2011/07/04/2097300.html
 * http://blog.csdn.net/jk110333/article/details/8683478
 * http://www.linuxhowtos.org/manpages/5/proc.htm
 *
 * 对于Cpu的各列说明:
 *
 * cpu 10132153 290696 3084719 46828483 16683 0 25195 0 175628 0
     cpu0 1393280 32966 572056 13343292 6130 0 17875 0 23933 0 The amount of time, measured in units of USER_HZ (1/100ths of a second on most architectures, use sysconf(_SC_CLK_TCK) to obtain the right value), that the system ("cpu" line) or the specific CPU ("cpuN" line) spent in various states:
     user
     (1) Time spent in user mode.
     nice
     (2) Time spent in user mode with low priority (nice).
     system
     (3) Time spent in system mode.
     idle
     (4) Time spent in the idle task. This value should be USER_HZ times the second entry in the /proc/uptime pseudo-file.
     iowait (since Linux 2.5.41)
     (5) Time waiting for I/O to complete. This value is not reliable, for the following reasons:
     1.
     The CPU will not wait for I/O to complete; iowait is the time that a task is waiting for I/O to complete. When a CPU goes into idle state for outstanding task I/O, another task will be scheduled on this CPU.
     2.
     On a multi-core CPU, the task waiting for I/O to complete is not running on any CPU, so the iowait of each CPU is difficult to calculate.
     3.
     The value in this field may decrease in certain conditions.
     irq (since Linux 2.6.0-test4)
     (6) Time servicing interrupts.
     softirq (since Linux 2.6.0-test4)
     (7) Time servicing softirqs.
     steal (since Linux 2.6.11)
     (8) Stolen time, which is the time spent in other operating systems when running in a virtualized environment
     guest (since Linux 2.6.24)
     (9) Time spent running a virtual CPU for guest operating systems under the control of the Linux kernel.
     guest_nice (since Linux 2.6.33)
     (10) Time spent running a niced guest (virtual CPU for guest operating systems under the control of the Linux kernel).
 *
 * @Author mozilla
 */
public class CpuCollector {

    private static final String CPU_PATH = "/proc/stat";

    private static boolean span = false;
    private static long last_ctxt = 0;
    private static long last_us = 0;
    private static long last_ni = 0;
    private static long last_sy = 0;
    private static long last_id = 0;
    private static long last_wa = 0;
    private static long last_hi = 0;
    private static long last_si = 0;
    private static long last_st = 0;

    public static Map<Object, Object> collect(){
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        try {
            List<String[]> cpuList = MixAll.file2String(new File(CPU_PATH));
            String[] cpu = null;
            String[] ctxt = null;
            if(cpuList != null && cpuList.size() > 0){
                for(String[] line : cpuList){
                    if(line[0].equals("cpu")){
                        cpu = line;
                    }
                    if(line[0].equals("ctxt")){
                        ctxt = line;
                    }
                }
            }

            if(cpu == null || ctxt == null){
                return result;
            }

            // 对于较早发行版的linux暂不输出
            if(cpu.length < 9 || ctxt.length != 2){
                return result;
            }

            // 首次进入先设置span
            if(!span){
                last_ctxt = Long.parseLong(ctxt[1]);
                last_us = Long.parseLong(cpu[1]);
                last_ni = Long.parseLong(cpu[2]);
                last_sy = Long.parseLong(cpu[3]);
                last_id = Long.parseLong(cpu[4]);
                last_wa = Long.parseLong(cpu[5]);
                last_hi = Long.parseLong(cpu[6]);
                last_si = Long.parseLong(cpu[7]);
                last_st = Long.parseLong(cpu[8]);
                span = true;
                return result;
            }

            long now_ctxt = Long.parseLong(ctxt[1]);
            long now_us = Long.parseLong(cpu[1]);
            long now_ni = Long.parseLong(cpu[2]);
            long now_sy = Long.parseLong(cpu[3]);
            long now_id = Long.parseLong(cpu[4]);
            long now_wa = Long.parseLong(cpu[5]);
            long now_hi = Long.parseLong(cpu[6]);
            long now_si = Long.parseLong(cpu[7]);
            long now_st = Long.parseLong(cpu[8]);
            long lastTotal = last_us + last_ni + last_sy + last_id + last_wa + last_hi + last_si + last_st;
            long nowTotal = now_us + now_ni + now_sy + now_id + now_wa + now_hi + now_si + now_st;

            result.put(STAT_CPU_CTXT.getCode(), now_ctxt - last_ctxt);
            result.put(STAT_CPU_US.getCode(), formatDouble(now_us - last_us, nowTotal - lastTotal));
            result.put(STAT_CPU_NI.getCode(), formatDouble(now_ni - last_ni, nowTotal - lastTotal));
            result.put(STAT_CPU_SY.getCode(), formatDouble(now_sy - last_sy, nowTotal - lastTotal));
            result.put(STAT_CPU_ID.getCode(), formatDouble(now_id - last_id, nowTotal - lastTotal));
            result.put(STAT_CPU_WA.getCode(), formatDouble(now_wa - last_wa, nowTotal - lastTotal));
            result.put(STAT_CPU_HI.getCode(), formatDouble(now_hi - last_hi, nowTotal - lastTotal));
            result.put(STAT_CPU_SI.getCode(), formatDouble(now_si - last_si, nowTotal - lastTotal));
            result.put(STAT_CPU_ST.getCode(), formatDouble(now_st - last_st, nowTotal - lastTotal));

            last_ctxt = now_ctxt;
            last_us = now_us;
            last_ni = now_ni;
            last_sy = now_sy;
            last_id = now_id;
            last_wa = now_wa;
            last_hi = now_hi;
            last_si = now_si;
            last_st = now_st;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}