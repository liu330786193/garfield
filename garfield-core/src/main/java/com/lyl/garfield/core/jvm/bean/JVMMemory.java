package com.lyl.garfield.core.jvm.bean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * https://docs.oracle.com/javase/7/docs/api/java/lang/management/MemoryUsage.html
 *
 * @author mozilla
 */
public class JVMMemory implements JVMMemoryMBean {

    private static JVMMemory instance = new JVMMemory();

    public static JVMMemory getInstance() {
        return instance;
    }

    private MemoryMXBean     memoryMXBean;

    private MemoryPoolMXBean permGenMxBean;
    private MemoryPoolMXBean oldGenMxBean;
    private MemoryPoolMXBean edenSpaceMxBean;
    private MemoryPoolMXBean pSSurvivorSpaceMxBean;

    private MemoryPoolMXBean codeCacheMxBean;
    private MemoryPoolMXBean metaSpaceMxBean;
    private MemoryPoolMXBean compressedClassSpace;

    private JVMMemory(){
        memoryMXBean = ManagementFactory.getMemoryMXBean();

        List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean item : list) {
            if ("CMS Perm Gen".equals(item.getName()) //
                || "Perm Gen".equals(item.getName()) //
                || "PS Perm Gen".equals(item.getName()) //
                || "G1 Perm Gen".equals(item.getName()) //
            ) {
                permGenMxBean = item;
            } else if ("CMS Old Gen".equals(item.getName()) //
                       || "Tenured Gen".equals(item.getName()) //
                       || "PS Old Gen".equals(item.getName()) //
                       || "G1 Old Gen".equals(item.getName()) //
            ) {
                oldGenMxBean = item;
            } else if ("Par Eden Space".equals(item.getName()) //
                       || "Eden Space".equals(item.getName()) //
                       || "PS Eden Space".equals(item.getName()) //
                       || "G1 Eden".equals(item.getName()) //
            ) {
                edenSpaceMxBean = item;
            } else if ("Par Survivor Space".equals(item.getName()) //
                       || "Survivor Space".equals(item.getName()) //
                       || "PS Survivor Space".equals(item.getName()) //
                       || "G1 Survivor".equals(item.getName()) //
            ) {
                pSSurvivorSpaceMxBean = item;
            }else if("Code Cache".equals(item.getName())){
                codeCacheMxBean = item;
            }else if("Metaspace".equals(item.getName())){
                metaSpaceMxBean = item;
            }else if("Compressed Class Space".equals(item.getName())){
                compressedClassSpace = item;
            }
        }
    }

    // Memory Heap
    @Override
    public long getHeapMemoryCommitted() {
        return memoryMXBean.getHeapMemoryUsage().getCommitted();
    }

    @Override
    public long getHeapMemoryInit() {
        return memoryMXBean.getHeapMemoryUsage().getInit();
    }

    @Override
    public long getHeapMemoryMax() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

    @Override
    public long getHeapMemoryUsed() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    // Memory NonHeap
    @Override
    public long getNonHeapMemoryCommitted() {
        return memoryMXBean.getNonHeapMemoryUsage().getCommitted();
    }

    @Override
    public long getNonHeapMemoryInit() {
        return memoryMXBean.getNonHeapMemoryUsage().getInit();
    }

    @Override
    public long getNonHeapMemoryMax() {
        return memoryMXBean.getNonHeapMemoryUsage().getMax();
    }

    @Override
    public long getNonHeapMemoryUsed() {
        return memoryMXBean.getNonHeapMemoryUsage().getUsed();
    }

    // memory permGen

    @Override
    public long getPermGenCommitted() {
        if (null == permGenMxBean) {
            return 0;
        }
        return permGenMxBean.getUsage().getCommitted();
    }

    @Override
    public long getPermGenInit() {
        if (null == permGenMxBean) {
            return 0;
        }
        return permGenMxBean.getUsage().getInit();
    }

    @Override
    public long getPermGenMax() {
        if (null == permGenMxBean) {
            return 0;
        }
        return permGenMxBean.getUsage().getMax();
    }

    @Override
    public long getPermGenUsed() {
        if (null == permGenMxBean) {
            return 0;
        }
        return permGenMxBean.getUsage().getUsed();
    }

    // memory oldGen

    @Override
    public long getOldGenCommitted() {
        if (null == oldGenMxBean) {
            return 0;
        }
        return oldGenMxBean.getUsage().getCommitted();
    }

    @Override
    public long getOldGenInit() {
        if (null == oldGenMxBean) {
            return 0;
        }
        return oldGenMxBean.getUsage().getInit();
    }

    @Override
    public long getOldGenMax() {
        if (null == oldGenMxBean) {
            return 0;
        }
        return oldGenMxBean.getUsage().getMax();
    }

    @Override
    public long getOldGenUsed() {
        if (null == oldGenMxBean) {
            return 0;
        }
        return oldGenMxBean.getUsage().getUsed();
    }

    // memory edenSpace
    @Override
    public long getEdenSpaceCommitted() {
        if (null == edenSpaceMxBean) {
            return 0;
        }
        return edenSpaceMxBean.getUsage().getCommitted();
    }

    @Override
    public long getEdenSpaceInit() {
        if (null == edenSpaceMxBean) {
            return 0;
        }
        return edenSpaceMxBean.getUsage().getInit();
    }

    @Override
    public long getEdenSpaceMax() {
        if (null == edenSpaceMxBean) {
            return 0;
        }
        return edenSpaceMxBean.getUsage().getMax();
    }

    @Override
    public long getEdenSpaceUsed() {
        if (null == edenSpaceMxBean) {
            return 0;
        }
        return edenSpaceMxBean.getUsage().getUsed();
    }

    // memory survivor
    @Override
    public long getSurvivorCommitted() {
        if (null == pSSurvivorSpaceMxBean) {
            return 0;
        }
        return pSSurvivorSpaceMxBean.getUsage().getCommitted();
    }

    @Override
    public long getSurvivorInit() {
        if (null == pSSurvivorSpaceMxBean) {
            return 0;
        }
        return pSSurvivorSpaceMxBean.getUsage().getInit();
    }

    @Override
    public long getSurvivorMax() {
        if (null == pSSurvivorSpaceMxBean) {
            return 0;
        }
        return pSSurvivorSpaceMxBean.getUsage().getMax();
    }

    @Override
    public long getSurvivorUsed() {
        if (null == pSSurvivorSpaceMxBean) {
            return 0;
        }
        return pSSurvivorSpaceMxBean.getUsage().getUsed();
    }

    // Metaspace
    @Override
    public long getMetaspaceCommitted() {
        if (null == metaSpaceMxBean) {
            return 0;
        }
        return metaSpaceMxBean.getUsage().getCommitted();
    }

    @Override
    public long getMetaspaceInit() {
        if (null == metaSpaceMxBean) {
            return 0;
        }
        return metaSpaceMxBean.getUsage().getInit();
    }

    @Override
    public long getMetaspaceMax() {
        if (null == metaSpaceMxBean) {
            return 0;
        }
        return metaSpaceMxBean.getUsage().getMax();
    }

    @Override
    public long getMetaspaceUsed() {
        if (null == metaSpaceMxBean) {
            return 0;
        }
        return metaSpaceMxBean.getUsage().getUsed();
    }


    // ==================== extra info ========================

    // code cache
    @Override
    public long getCodeCacheCommitted() {
        if (null == codeCacheMxBean) {
            return 0;
        }
        return codeCacheMxBean.getUsage().getCommitted();
    }

    @Override
    public long getCodeCacheInit() {
        if (null == codeCacheMxBean) {
            return 0;
        }
        return codeCacheMxBean.getUsage().getInit();
    }

    @Override
    public long getCodeCacheMax() {
        if (null == codeCacheMxBean) {
            return 0;
        }
        return codeCacheMxBean.getUsage().getMax();
    }

    @Override
    public long getCodeCacheUsed() {
        if (null == codeCacheMxBean) {
            return 0;
        }
        return codeCacheMxBean.getUsage().getUsed();
    }

    // compressed class space
    @Override
    public long getCompressedClassSpaceCommitted() {
        if (null == compressedClassSpace) {
            return 0;
        }
        return compressedClassSpace.getUsage().getCommitted();
    }

    @Override
    public long getCompressedClassSpaceInit() {
        if (null == compressedClassSpace) {
            return 0;
        }
        return compressedClassSpace.getUsage().getInit();
    }

    @Override
    public long getCompressedClassSpaceMax() {
        if (null == compressedClassSpace) {
            return 0;
        }
        return compressedClassSpace.getUsage().getMax();
    }

    @Override
    public long getCompressedClassSpaceUsed() {
        if (null == compressedClassSpace) {
            return 0;
        }
        return compressedClassSpace.getUsage().getUsed();
    }


}
