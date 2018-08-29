package com.lyl.garfield.core.jvm.collector;


import com.timevale.cat.api.jvm.JvmDTO;
import com.timevale.cat.core.conf.Config;
import com.timevale.cat.core.jvm.bean.JVMGC;
import com.timevale.cat.core.jvm.bean.JVMInfo;
import com.timevale.cat.core.jvm.bean.JVMMemory;
import com.timevale.cat.core.jvm.bean.JVMThread;
import com.timevale.cat.core.utils.IPUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.timevale.cat.core.constants.CatAgentConstants.*;
import static com.timevale.cat.core.constants.CatAgentItemConstants.*;

/**
 * jvm信息采集(内存,GC,线程)
 * 
 * @author mozilla 2017-12-25 上午12:53:53
 */
public class JvmCollector extends BaseCollector {

    private static JVMInfo jvmInfo = JVMInfo.getInstance();

    public static JvmDTO getCommonPacket(String cat){
        JvmDTO commonPacket = new JvmDTO();
        commonPacket.setApp(Config.Agent.APPLICATION_ID);
        commonPacket.setCat(cat);
        commonPacket.setTimestamp(System.currentTimeMillis());
        commonPacket.setIp(IPUtils.getAllIpv4NoLoopbackAddresses().iterator().next());
        commonPacket.setInstanceId(IPUtils.getMACAddress() + ":" + commonPacket.getApp());
        return commonPacket;
    }

    public static JvmDTO getJvmBase(){
        JvmDTO basePacket = getCommonPacket(CATEGORY_BASE);
        Map<Object, Object> itemMap = new LinkedHashMap<Object, Object>();
        itemMap.put(JVM_HOSTNAME.getCode(), jvmInfo.getHostname());
        itemMap.put(JVM_PID.getCode(), jvmInfo.getPID());
        itemMap.put(JVM_START_TIME.getCode(), DateFormat.format(jvmInfo.getStartTime()));
        itemMap.put(JVM_INPUT_ARGUMENT.getCode(), jvmInfo.getInputArguments());
        itemMap.put(JVM_ARCH.getCode(), jvmInfo.getArch());
        itemMap.put(JVM_AVAILABLE_PROCESSORS.getCode(), jvmInfo.getAvailableProcessors());
        itemMap.put(JVM_OS.getCode(), jvmInfo.getOSName() + "[" + jvmInfo.getOSVersion() + "]");
        itemMap.put(JVM_FILE_ENCODE.getCode(), jvmInfo.getFileEncode());
        itemMap.put(JVM_NAME.getCode(), jvmInfo.getJVM());
        itemMap.put(JVM_JAVA_VERSION.getCode(), jvmInfo.getJavaVersion());
        itemMap.put(JVM_JAVA_SPEC_VERSION.getCode(), jvmInfo.getJavaSpecificationVersion());
        itemMap.put(JVM_JAVA_HOME.getCode(), jvmInfo.getJavaHome());
        itemMap.put(JVM_JAVA_LIBRARY_PATH.getCode(), jvmInfo.getJavaLibraryPath());
        itemMap.put(JVM_LOADED_CLASS_COUNT.getCode(), jvmInfo.getLoadedClassCount());
        itemMap.put(JVM_TOTAL_LOADED_CLASS_COUNT.getCode(), jvmInfo.getTotalLoadedClassCount());
        itemMap.put(JVM_UNLOADED_CLASS_COUNT.getCode(), jvmInfo.getUnloadedClassCount());
        itemMap.put(JVM_TOTAL_COMPILATION_TIME.getCode(), jvmInfo.getTotalCompilationTime());

        basePacket.setData(itemMap);
        return basePacket;
    }

    public static JvmDTO getJvmMemory(){
        JVMMemory memory = JVMMemory.getInstance();
        JvmDTO memoryPacket = getCommonPacket(CATEGORY_JVM_MEMORY);
        Map<Object, Object> itemMap = new LinkedHashMap<Object, Object>();
        itemMap.put(JVM_MEMORY_EDEN_COMMITTED.getCode(), memory.getEdenSpaceCommitted());
        itemMap.put(JVM_MEMORY_EDEN_INIT.getCode(), memory.getEdenSpaceInit());
        itemMap.put(JVM_MEMORY_EDEN_MAX.getCode(), memory.getEdenSpaceMax());
        itemMap.put(JVM_MEMORY_EDEN_USED.getCode(), memory.getEdenSpaceUsed());

        itemMap.put(JVM_MEMORY_OLD_COMMITTED.getCode(), memory.getOldGenCommitted());
        itemMap.put(JVM_MEMORY_OLD_INIT.getCode(), memory.getOldGenInit());
        itemMap.put(JVM_MEMORY_OLD_MAX.getCode(), memory.getOldGenMax());
        itemMap.put(JVM_MEMORY_OLD_USED.getCode(), memory.getOldGenUsed());

        itemMap.put(JVM_MEMORY_HEAP_COMMITTED.getCode(), memory.getHeapMemoryCommitted());
        itemMap.put(JVM_MEMORY_HEAP_INIT.getCode(), memory.getHeapMemoryInit());
        itemMap.put(JVM_MEMORY_HEAP_MAX.getCode(), memory.getHeapMemoryMax());
        itemMap.put(JVM_MEMORY_HEAP_USED.getCode(), memory.getHeapMemoryUsed());

        itemMap.put(JVM_MEMORY_NON_HEAP_COMMITTED.getCode(), memory.getNonHeapMemoryCommitted());
        itemMap.put(JVM_MEMORY_NON_HEAP_INIT.getCode(), memory.getNonHeapMemoryInit());
        itemMap.put(JVM_MEMORY_NON_HEAP_MAX.getCode(), memory.getNonHeapMemoryMax());
        itemMap.put(JVM_MEMORY_NON_HEAP_USED.getCode(), memory.getNonHeapMemoryUsed());

        String javaSpecVersion = jvmInfo.getJavaSpecificationVersion();
        double version = 0;
        try{
            version = Double.parseDouble(javaSpecVersion);
        }catch(Exception e){
            // ignore
        }

        // 1.8+已经不再有perm区
        if(version < 1.8) {
            itemMap.put(JVM_MEMORY_PERM_COMMITTED.getCode(), memory.getPermGenCommitted());
            itemMap.put(JVM_MEMORY_PERM_INIT.getCode(), memory.getPermGenInit());
            itemMap.put(JVM_MEMORY_PERM_MAX.getCode(), memory.getPermGenMax());
            itemMap.put(JVM_MEMORY_PERM_USED.getCode(), memory.getPermGenUsed());
        }

        itemMap.put(JVM_MEMORY_SURVIVOR_COMMITTED.getCode(), memory.getSurvivorCommitted());
        itemMap.put(JVM_MEMORY_SURVIVOR_INIT.getCode(), memory.getSurvivorInit());
        itemMap.put(JVM_MEMORY_SURVIVOR_MAX.getCode(), memory.getSurvivorMax());
        itemMap.put(JVM_MEMORY_SURVIVOR_USED.getCode(), memory.getSurvivorUsed());

        // 1.8新增的metaspace
        if(version >= 1.8) {
            itemMap.put(JVM_MEMORY_METASPACE_COMMITTED.getCode(), memory.getMetaspaceCommitted());
            itemMap.put(JVM_MEMORY_METASPACE_INIT.getCode(), memory.getMetaspaceInit());
            itemMap.put(JVM_MEMORY_METASPACE_MAX.getCode(), memory.getMetaspaceMax());
            itemMap.put(JVM_MEMORY_METASPACE_USED.getCode(), memory.getMetaspaceUsed());
        }

        itemMap.put(JVM_MEMORY_CODE_CACHE_COMMITTED.getCode(), memory.getCodeCacheCommitted());
        itemMap.put(JVM_MEMORY_CODE_CACHE_INIT.getCode(), memory.getCodeCacheInit());
        itemMap.put(JVM_MEMORY_CODE_CACHE_MAX.getCode(), memory.getCodeCacheMax());
        itemMap.put(JVM_MEMORY_CODE_CACHE_USED.getCode(), memory.getCodeCacheUsed());

        itemMap.put(JVM_MEMORY_COMPRESSED_CLASS_SPACE_COMMITTED.getCode(), memory.getCompressedClassSpaceCommitted());
        itemMap.put(JVM_MEMORY_COMPRESSED_CLASS_SPACE_INIT.getCode(), memory.getCompressedClassSpaceInit());
        itemMap.put(JVM_MEMORY_COMPRESSED_CLASS_SPACE_MAX.getCode(), memory.getCompressedClassSpaceMax());
        itemMap.put(JVM_MEMORY_COMPRESSED_CLASS_SPACE_USED.getCode(), memory.getCompressedClassSpaceUsed());

        memoryPacket.setData(itemMap);
        return memoryPacket;
    }

    public static JvmDTO getJvmGC(){
        JVMGC gc = JVMGC.getInstance();

        JvmDTO gcPacket = getCommonPacket(CATEGORY_JVM_GC);

        Map<Object, Object> itemMap = new LinkedHashMap<Object, Object>();
        itemMap.put(JVM_GC_YOUNG_COLLECTION_COUNT.getCode(), gc.getYoungGCCollectionCount());
        itemMap.put(JVM_GC_YOUNG_COLLECTION_TIME.getCode(), gc.getYoungGCCollectionTime());
        itemMap.put(JVM_GC_FULL_COLLECTION_COUNT.getCode(), gc.getFullGCCollectionCount());
        itemMap.put(JVM_GC_FULL_COLLECTION_TIME.getCode(), gc.getFullGCCollectionTime());
        itemMap.put(JVM_GC_SPAN_FULL_COLLECTION_COUNT.getCode(), gc.getSpanFullGCCollectionCount());
        itemMap.put(JVM_GC_SPAN_FULL_COLLECTION_TIME.getCode(), gc.getSpanFullGCCollectionTime());
        itemMap.put(JVM_GC_SPAN_YOUNG_COLLECTION_COUNT.getCode(), gc.getSpanYoungGCCollectionCount());
        itemMap.put(JVM_GC_SPAN_YOUNG_COLLECTION_TIME.getCode(), gc.getSpanYoungGCCollectionTime());
        gcPacket.setData(itemMap);
        return gcPacket;
    }

    public static JvmDTO getJvmThread(){
        JVMThread thread = JVMThread.getInstance();
        JvmDTO threadPacket = getCommonPacket(CATEGORY_JVM_THREAD);
        Map<Object, Object> itemMap = new LinkedHashMap<Object, Object>();
        itemMap.put(JVM_THREAD_COUNT.getCode(), thread.getThreadCount());
        itemMap.put(JVM_THREAD_DAMEON_COUNT.getCode(), thread.getDaemonThreadCount());
        itemMap.put(JVM_THREAD_DEADLOCK_COUNT.getCode(), thread.getDeadLockedThreadCount());
        itemMap.put(JVM_THREAD_PROCESS_CPU_TIME_RATE.getCode(), thread.getProcessCpuTimeRate());
        itemMap.put(JVM_THREAD_TOTAL_STARTED_COUNT.getCode(), thread.getTotalStartedThreadCount());
        threadPacket.setData(itemMap);
        return threadPacket;
    }
}











