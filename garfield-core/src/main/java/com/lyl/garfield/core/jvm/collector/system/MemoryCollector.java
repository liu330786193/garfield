package com.lyl.garfield.core.jvm.collector.system;

import com.lyl.garfield.core.utils.MixAll;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lyl.garfield.core.constants.CatAgentItemConstants.*;

/**
 * eg:
 *
 * uname -a
 *   Linux iZ23iceqiuzZ 2.6.18-274.el5 #1 SMP Fri Jul 8 17:36:59 EDT 2011 x86_64 x86_64 x86_64 GNU/Linux
 *
 * cat /proc/meminfo
 *   MemTotal:      4054120 kB
     MemFree:        748160 kB
     Buffers:        176120 kB
     Cached:        1169292 kB
     SwapCached:          0 kB
     Active:        2674264 kB
     Inactive:       503816 kB
     HighTotal:           0 kB
     HighFree:            0 kB
     LowTotal:      4054120 kB
     LowFree:        748160 kB
     SwapTotal:           0 kB
     SwapFree:            0 kB
     Dirty:              92 kB
     Writeback:           0 kB
     AnonPages:     1832672 kB
     Mapped:          59148 kB
     Slab:           104492 kB
     PageTables:       7260 kB
     NFS_Unstable:        0 kB
     Bounce:              0 kB
     CommitLimit:   2027060 kB
     Committed_AS:  4156024 kB
     VmallocTotal: 34359738367 kB
     VmallocUsed:       592 kB
     VmallocChunk: 34359737723 kB
     HugePages_Total:     0
     HugePages_Free:      0
     HugePages_Rsvd:      0
     Hugepagesize:     2048 kB

 *
 * @Author mozilla
 */
public class MemoryCollector {

    private static final String MEMORY_PATH = "/proc/meminfo";

    private static final Map<String, Integer> codeMap = new LinkedHashMap<String, Integer>();

    static{
        codeMap.put("MemTotal:", MEMORY_MEM_TOTAL.getCode());
        codeMap.put("MemFree:", MEMORY_MEM_FREE.getCode());
    }

    public static Map<Object, Object> collect(){
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        try {
            List<String[]> memoryList = MixAll.file2String(new File(MEMORY_PATH));
            long memoryTotal = 0L;
            long memoryFree = 0L;
            if(memoryList != null && memoryList.size() > 0){
                for(String[] line : memoryList){
                    Integer code = codeMap.get(line[0]);
                    if(code != null){
                        result.put(code, line[1]);
                        if(code == MEMORY_MEM_TOTAL.getCode()){
                            memoryTotal = Long.parseLong(line[1]);
                        }
                        if(code == MEMORY_MEM_FREE.getCode()){
                            memoryFree = Long.parseLong(line[1]);
                        }
                    }
                }
            }

            long used = memoryTotal - memoryFree;
            result.put(MEMORY_MEM_USED.getCode(), used);
        }catch(Exception e){
            // ignore
        }
        return result;
    }
}
