package com.lyl.garfield.core.jvm.collector.system;

import com.timevale.cat.core.utils.MixAll;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.timevale.cat.core.constants.CatAgentItemConstants.*;

/**
 * ramdisk:http://blog.csdn.net/hshl1214/article/details/8513972
 * device:https://www.debian.org/releases/jessie/i386/apcs04.html.zh-cn
 *
 * https://www.kernel.org/doc/Documentation/ABI/testing/procfs-diskstats
 * https://www.cnblogs.com/titer1/archive/2012/04/04/2431593.html
 * http://blog.csdn.net/aaronychen/article/details/2270048
 * https://www.kernel.org/doc/Documentation/iostats.txt
 * http://linuxperf.com/?p=156
 *
 *       域	 	Quoted	                            解释
 *       F1		major number	                    此块设备的主设备号
 *       F2		minor mumber	                    此块设备的次设备号
 *       F3		device name	                        此块设备名字
 *       F4		reads completed successfully	    成功完成的读请求次数
 *       F5		reads merged	                    读请求的次数
 *       F6		sectors read	                    读请求的扇区数总和
 *       F7		time spent reading (ms)	            读请求花费的时间总和
 *       F8		writes completed	                成功完成的写请求次数
 *       F9		writes merged	                    写请求合并的次数
 *       F10	sectors written	                    写请求的扇区数总和
 *       F11	time spent writing (ms)	            写请求花费的时间总和
 *       F12	I/Os currently in progress	        块设备队列中的IO请求数
 *       F13	time spent doing I/Os (ms)	        块设备队列非空时间总和
 *       F14	weighted time spent doing I/Os (ms)	块设备队列非空时间加权总和
 *
 * @Author mozilla
 */
public class IOCollector {

    private static final String IO_PATH = "/proc/diskstats";
    private static final String CAT_TOTAL = "total";
    private static final String CAT_SPAN = "span";

    private static final int STATS_PROP_SIZE = 14;
    private static boolean span = false;
    private static long last_read_completed = 0;
    private static long last_read_merged = 0;
    private static long last_read_sectors = 0;
    private static long last_read_time_spent = 0;
    private static long last_write_completed = 0;
    private static long last_write_merged = 0;
    private static long last_write_sectors = 0;
    private static long last_write_time_spent = 0;
    private static long last_time_spent_io = 0;
    private static long last_time_spent_io_weighted = 0;

    public static Map<String, Map<Object, Object>> collect(){
        Map<String, Map<Object, Object>> result = new LinkedHashMap<String, Map<Object, Object>>();

        try {
            List<String[]> diskList = MixAll.file2String(new File(IO_PATH));
            String[] deviceLine = null;
            if(diskList != null){
                for(String[] line : diskList){
                    String device = line[2];
                    if(("sda".equals(device) || "hda".equals(device)) && line.length == STATS_PROP_SIZE){
                        deviceLine = line;
                    }
                }
            }

            if(deviceLine != null){

                // 首次进入先设置span
                if(!span){
                    last_read_completed = Long.parseLong(deviceLine[3]);
                    last_read_merged = Long.parseLong(deviceLine[4]);
                    last_read_sectors = Long.parseLong(deviceLine[5]);
                    last_read_time_spent = Long.parseLong(deviceLine[6]);
                    last_write_completed = Long.parseLong(deviceLine[7]);
                    last_write_merged = Long.parseLong(deviceLine[8]);
                    last_write_sectors = Long.parseLong(deviceLine[9]);
                    last_write_time_spent = Long.parseLong(deviceLine[10]);
                    last_time_spent_io = Long.parseLong(deviceLine[12]);
                    last_time_spent_io_weighted = Long.parseLong(deviceLine[13]);
                    span = true;
                    return result;
                }

                long now_read_completed = Long.parseLong(deviceLine[3]);
                long now_read_merged = Long.parseLong(deviceLine[4]);
                long now_read_sectors = Long.parseLong(deviceLine[5]);
                long now_read_time_spent = Long.parseLong(deviceLine[6]);
                long now_write_completed = Long.parseLong(deviceLine[7]);
                long now_write_merged = Long.parseLong(deviceLine[8]);
                long now_write_sectors = Long.parseLong(deviceLine[9]);
                long now_write_time_spent = Long.parseLong(deviceLine[10]);
                long now_io_current_in_progress = Long.parseLong(deviceLine[11]);
                long now_time_spent_io = Long.parseLong(deviceLine[12]);
                long now_time_spent_io_weighted = Long.parseLong(deviceLine[13]);

                Map<Object, Object> totalMap = new LinkedHashMap<Object, Object>();
                totalMap.put(DISK_READ_COMPLETED.getCode(), now_read_completed);
                totalMap.put(DISK_READ_MERGED.getCode(), now_read_merged);
                totalMap.put(DISK_READ_SECTORS.getCode(), now_read_sectors);
                totalMap.put(DISK_READ_TIME_SPENT.getCode(), now_read_time_spent);
                totalMap.put(DISK_WRITE_COMPLTED.getCode(), now_write_completed);
                totalMap.put(DISK_WRITE_MERGED.getCode(), now_write_merged);
                totalMap.put(DISK_WRITE_SECTORS.getCode(), now_write_sectors);
                totalMap.put(DISK_WRITE_TIME_SPENT.getCode(), now_write_time_spent);
                totalMap.put(DISK_IO_CURRENT_IN_PROGESS.getCode(), now_io_current_in_progress);
                totalMap.put(DISK_TIME_SPENT_DOING_IO.getCode(), now_time_spent_io);
                totalMap.put(DISK_WEIGHTED_TIME_SPENT_DOING_IO.getCode(), now_time_spent_io_weighted);
                result.put(CAT_TOTAL, totalMap);

                Map<Object, Object> spanMap = new LinkedHashMap<Object, Object>();
                spanMap.put(DISK_READ_COMPLETED_SPAN.getCode(), now_read_completed - last_read_completed);
                spanMap.put(DISK_READ_MERGED_SPAN.getCode(), now_read_merged - last_read_merged);
                spanMap.put(DISK_READ_SECTORS_SPAN.getCode(), now_read_sectors - last_read_sectors);
                spanMap.put(DISK_READ_TIME_SPENT_SPAN.getCode(), now_read_time_spent - last_read_time_spent);
                spanMap.put(DISK_WRITE_COMPLTED_SPAN.getCode(), now_write_completed - last_write_completed);
                spanMap.put(DISK_WRITE_MERGED_SPAN.getCode(), now_write_merged - last_write_merged);
                spanMap.put(DISK_WRITE_SECTORS_SPAN.getCode(), now_write_sectors - last_write_sectors);
                spanMap.put(DISK_WRITE_TIME_SPENT_SPAN.getCode(), now_write_time_spent - last_write_time_spent);
                spanMap.put(DISK_TIME_SPENT_DOING_IO_SPAN.getCode(), now_time_spent_io - last_time_spent_io);
                spanMap.put(DISK_WEIGHTED_TIME_SPENT_DOING_IO_SPAN.getCode(), now_time_spent_io_weighted - last_time_spent_io_weighted);
                result.put(CAT_SPAN, spanMap);

                last_read_completed = now_read_completed;
                last_read_merged = now_read_merged;
                last_read_sectors = now_read_sectors;
                last_read_time_spent = now_read_time_spent;
                last_write_completed = now_write_completed;
                last_write_merged = now_write_merged;
                last_write_sectors = now_write_sectors;
                last_write_time_spent = now_write_time_spent;
                last_time_spent_io = now_time_spent_io;
                last_time_spent_io_weighted = now_time_spent_io_weighted;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}





