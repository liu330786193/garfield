package com.lyl.garfield.core.constants;

/**
 * 监控项整理.
 *
 * @author mozilla
 */
public enum CatAgentItemConstants {

    // 0-99 公共场景公用
    COMMON_RT_MAX(1, "响应最长时间", "ms"),
    COMMON_RT_MIN(2, "响应最短时间", "ms"),
    COMMON_RT_AVG(3, "响应平均时间", "ms"),
    COMMON_EXEC_COUNT(4, "执行次数"),
    COMMON_RT_REQUEST(5, "单次执行RT", "ms"),

    // 100-199 JVM预留
    JVM_HOSTNAME(101, "主机名称"),
    JVM_LOCAL_IP(102, "机器IP"),
    JVM_PID(103, "进程ID"),
    JVM_START_TIME(104, "应用启动时间"),
    JVM_INPUT_ARGUMENT(105, "JVM启动参数"),
    JVM_ARCH(106, "硬件平台"),
    JVM_AVAILABLE_PROCESSORS(107, "可用处理器个数"),
    JVM_OS(108, "操作系统"),
    JVM_FILE_ENCODE(109, "文件编码"),
    JVM_NAME(110, "JVM名称"),
    JVM_JAVA_VERSION(111, "JAVA_VERSION"),
    JVM_JAVA_SPEC_VERSION(112, "JAVA_SPEC_VERSION"),
    JVM_JAVA_HOME(113, "JAVA_HOME"),
    JVM_JAVA_LIBRARY_PATH(114, "JAVA_LIBRARY_PATH"),
    JVM_LOADED_CLASS_COUNT(115, "当前已加载类个数"),
    JVM_TOTAL_LOADED_CLASS_COUNT(116, "历史总加载类个数"),
    JVM_UNLOADED_CLASS_COUNT(117, "卸载类总个数"),
    JVM_TOTAL_COMPILATION_TIME(118, "应用编译时间"),

    JVM_MEMORY_EDEN_COMMITTED(130, "eden区系统保留空间大小", "byte"),
    JVM_MEMORY_EDEN_INIT(131, "eden区初始空间大小", "byte"),
    JVM_MEMORY_EDEN_MAX(132, "eden区最大空间大小", "byte"),
    JVM_MEMORY_EDEN_USED(133, "eden区已用空间大小", "byte"),

    JVM_MEMORY_HEAP_COMMITTED(134, "heap区系统保留空间大小", "byte"),
    JVM_MEMORY_HEAP_INIT(135, "heap区初始空间大小", "byte"),
    JVM_MEMORY_HEAP_MAX(136, "heap区最大空间大小", "byte"),
    JVM_MEMORY_HEAP_USED(137, "heap区已用空间大小", "byte"),

    JVM_MEMORY_NON_HEAP_COMMITTED(138, "non-heap区系统保留空间大小", "byte"),
    JVM_MEMORY_NON_HEAP_INIT(139, "non-heap区初始空间大小", "byte"),
    JVM_MEMORY_NON_HEAP_MAX(140, "non-heap区最大空间大小", "byte"),
    JVM_MEMORY_NON_HEAP_USED(141, "non-heap区已用空间大小", "byte"),

    JVM_MEMORY_OLD_COMMITTED(142, "old区系统保留空间大小", "byte"),
    JVM_MEMORY_OLD_INIT(143, "old区初始空间大小", "byte"),
    JVM_MEMORY_OLD_MAX(144, "old区最大空间大小", "byte"),
    JVM_MEMORY_OLD_USED(145, "old区已用空间大小", "byte"),

    JVM_MEMORY_PERM_COMMITTED(146, "perm区系统保留空间大小", "byte"),
    JVM_MEMORY_PERM_INIT(147, "perm区初始空间大小", "byte"),
    JVM_MEMORY_PERM_MAX(148, "perm区最大空间大小", "byte"),
    JVM_MEMORY_PERM_USED(149, "perm区已用空间大小", "byte"),

    JVM_MEMORY_SURVIVOR_COMMITTED(150, "survivor区系统保留空间大小", "byte"),
    JVM_MEMORY_SURVIVOR_INIT(151, "survivor区初始空间大小", "byte"),
    JVM_MEMORY_SURVIVOR_MAX(152, "survivor区最大空间大小", "byte"),
    JVM_MEMORY_SURVIVOR_USED(153, "survivor区已用空间大小", "byte"),

    JVM_MEMORY_METASPACE_COMMITTED(154, "Metaspace区系统保留空间大小", "byte"),
    JVM_MEMORY_METASPACE_INIT(155, "Metaspace区初始空间大小", "byte"),
    JVM_MEMORY_METASPACE_MAX(156, "Metaspace区最大空间大小", "byte"),
    JVM_MEMORY_METASPACE_USED(157, "Metaspace区已用空间大小", "byte"),

    JVM_MEMORY_CODE_CACHE_COMMITTED(158, "code cache区系统保留空间大小", "byte"),
    JVM_MEMORY_CODE_CACHE_INIT(159, "code cache区初始空间大小", "byte"),
    JVM_MEMORY_CODE_CACHE_MAX(160, "code cache区最大空间大小", "byte"),
    JVM_MEMORY_CODE_CACHE_USED(161, "code cache区已用空间大小", "byte"),

    JVM_MEMORY_COMPRESSED_CLASS_SPACE_COMMITTED(162, "compressed class space区系统保留空间大小", "byte"),
    JVM_MEMORY_COMPRESSED_CLASS_SPACE_INIT(163, "compressed class space区初始空间大小", "byte"),
    JVM_MEMORY_COMPRESSED_CLASS_SPACE_MAX(164, "compressed class space区最大空间大小", "byte"),
    JVM_MEMORY_COMPRESSED_CLASS_SPACE_USED(165, "compressed class space区已用空间大小", "byte"),

    JVM_THREAD_DAMEON_COUNT(170, "dameon线程运行个数"),
    JVM_THREAD_DEADLOCK_COUNT(171, "死锁线程运行个数"),
    JVM_THREAD_PROCESS_CPU_TIME_RATE(172, "CPU处理比率"),
    JVM_THREAD_COUNT(173, "线程运行个数"),
    JVM_THREAD_TOTAL_STARTED_COUNT(174, "已启动线程总个数(包含死亡)"),

    JVM_GC_YOUNG_COLLECTION_COUNT(180, "YGC总次数"),
    JVM_GC_YOUNG_COLLECTION_TIME(181, "YGC总消耗时间", "ms"),
    JVM_GC_FULL_COLLECTION_COUNT(182, "FGC总次数"),
    JVM_GC_FULL_COLLECTION_TIME(183, "FGC总消耗时间", "ms"),
    JVM_GC_SPAN_YOUNG_COLLECTION_COUNT(184, "YGC最近一次区间总执行次数"),
    JVM_GC_SPAN_YOUNG_COLLECTION_TIME(185, "YGC最近一次区间总执行耗时", "ms"),
    JVM_GC_SPAN_FULL_COLLECTION_COUNT(186, "FGC最近一次区间总执行次数"),
    JVM_GC_SPAN_FULL_COLLECTION_TIME(187, "FGC最近一次区间总执行耗时", "ms"),

    // 补偿新增:JVM堆区百分比
    JVM_MEMORY_HEAP_USED_PERCENT(188, "堆空间使用占比"),

    // 200-299 URL监控预留
    @Deprecated     // 受限制于http1.1的Transfer-Encoding:chunked的原因,暂时无法获取到BYTE数据包大小
    URL_BYTE_IN(201, "数据包流入字节大小", "byte"),
    @Deprecated
    URL_BYTE_OUT(202, "数据包流出字节大小", "byte"),
    URL_2XX_COUNT(203, "HTTP响应码200-299总个数"),
    URL_3XX_COUNT(204, "HTTP响应码300-399总个数"),
    URL_4XX_COUNT(205, "HTTP响应码400-499总个数"),
    URL_5XX_COUNT(206, "HTTP响应码500-599总个数"),
    URL_STATUS_CODE(207, "HTTP单次请求响应码"),
    URL_TAG(208, "业务标签"),

    // 300-399 SQL层面预留, 可重用公共部分

    // 400-499 系统预留
    DISK_READ_COMPLETED(401, "成功完成的读请求次数"),
    DISK_READ_COMPLETED_SPAN(402, "区间内成功完成的读请求次数"),
    DISK_READ_MERGED(403, "读请求的合并(Merge)次数"),
    DISK_READ_MERGED_SPAN(404, "区间内读请求的合并(Merge)次数"),
    DISK_READ_SECTORS(405, "读请求的扇区数总和"),
    DISK_READ_SECTORS_SPAN(406, "区间内读请求的扇区数总和"),
    DISK_READ_TIME_SPENT(407, "读请求花费的时间总和", "ms"),
    DISK_READ_TIME_SPENT_SPAN(408, "区间内读请求花费的时间总和", "ms"),
    DISK_WRITE_COMPLTED(409, "成功完成的写请求次数"),
    DISK_WRITE_COMPLTED_SPAN(410, "区间内成功完成的写请求次数"),
    DISK_WRITE_MERGED(411, "写请求合并(Merge)次数"),
    DISK_WRITE_MERGED_SPAN(412, "区间内写请求合并(Merge)次数"),
    DISK_WRITE_SECTORS(413, "写请求的扇区数总和"),
    DISK_WRITE_SECTORS_SPAN(414, "区间内写请求的扇区数总和"),
    DISK_WRITE_TIME_SPENT(415, "写请求花费的时间总和", "ms"),
    DISK_WRITE_TIME_SPENT_SPAN(416, "区间内写请求花费的时间总和", "ms"),
    DISK_IO_CURRENT_IN_PROGESS(417, "块设备队列中的IO请求数"),
    DISK_TIME_SPENT_DOING_IO(418, "块设备队列非空时间总和"),
    DISK_TIME_SPENT_DOING_IO_SPAN(419, "区间内块设备队列非空时间总和"),
    DISK_WEIGHTED_TIME_SPENT_DOING_IO(420, "块设备队列非空时间加权总和", "ms"),
    DISK_WEIGHTED_TIME_SPENT_DOING_IO_SPAN(421, "区间内块设备队列非空时间加权总和", "ms"),

    LOAD_1(451, "过去1分钟的平均load"),
    LOAD_5(452, "过去5分钟的平均load"),
    LOAD_15(453, "过去15分钟的平均load"),

    MEMORY_MEM_TOTAL(501, "系统内存总和", "kb"),
    MEMORY_MEM_FREE(502, "系统内存剩余", "kb"),
    MEMORY_MEM_USED(503, "系统内存已使用(包含buffer/cache)", "kb"),

    NET_RECEIVE_BYTES(551, "接收总字节数"),
    NET_RECEIVE_BYTES_SPAN(552, "区间接收总字节数"),
    NET_RECEIVE_PACKETS(553, "接收的总数据包个数"),
    NET_RECEIVE_PACKETS_SPAN(554, "区间接收的总数据包个数"),
    NET_TRANSMIT_BYTES(555, "发送总字节数"),
    NET_TRANSMIT_BYTES_SPAN(556, "区间发送总字节数"),
    NET_TRANSMIT_PACKETS(557, "发送的总数据包个数"),
    NET_TRANSMIT_PACKETS_SPAN(558, "区间发送的总数据包个数"),

    SNMP_TCP_ACTIVE_OPENS(601, "主动打开的tcp连接数量"),          // 向远程服务器发起连接
    SNMP_TCP_ACTIVE_OPENS_SPAN(602, "区间主动打开的tcp连接数量"),
    SNMP_TCP_PASSIVE_OPENS(603, "被动打开的tcp连接数量"),         // 客户端连接本机
    SNMP_TCP_PASSIVE_OPENS_SPAN(604, "区间被动打开的tcp连接数量"),
    SNMP_TCP_ATTEMPT_FAILS(605, "连接失败的数量"),
    SNMP_TCP_ATTEMPT_FAILS_SPAN(606, "区间连接失败的数量"),
    SNMP_TCP_ESTAB_RESETS(607, "连接关闭数量"),
    SNMP_TCP_ESTAB_RESETS_SPAN(608, "区间连接关闭数量"),
    SNMP_TCP_CURR_ESTAB(609, "当前连接数"),
    SNMP_TCP_CURR_ESTAB_SPAN(610, "区间当前连接数"),
    SNMP_TCP_IN_SEGS(611, "收到的tcp报文数量"),
    SNMP_TCP_IN_SEGS_SPAN(612, "区间收到的tcp报文数量"),
    SNMP_TCP_OUT_SEGS(613, "发出的tcp报文数量"),
    SNMP_TCP_OUT_SEGS_SPAN(614, "区间发出的tcp报文数量"),
    SNMP_TCP_RETRANS_SEGS(615, "重传的报文数量"),
    SNMP_TCP_RETRANS_SEGS_SPAN(616, "区间重传的报文数量"),
    SNMP_TCP_RETRANS_SEGS_PERCENT_SPAN(617, "区间重传率", "%"),
    SNMP_TCP_IN_ERRS(618, "报文错误"),
    SNMP_TCP_IN_ERRS_SPAN(619, "区间报文错误"),

    SNMP_UDP_IN_DATAGRAMS(630, "流入数据包数量"),
    SNMP_UDP_IN_DATAGRAMS_SPAN(631, "区间流入数据包数量"),
    SNMP_UDP_OUT_DATAGRAMS(632, "流出数据包数量"),
    SNMP_UDP_OUT_DATAGRAMS_SPAN(633, "区间流出数据包数量"),
    SNMP_UDP_NO_PORTS(634, "目的地址或者端口不存在数量"),
    SNMP_UDP_NO_PORTS_SPAN(635, "区间目的地址或者端口不存在数量"),
    SNMP_UDP_IN_ERRORS(636, "错误包数量"),
    SNMP_UDP_IN_ERRORS_SPAN(637, "区间错误包数量"),

    STAT_CPU_CTXT(651, "CPU发生的上下文切换次数"),
    STAT_CPU_US(652, "用户态使用占比", "%"),
    STAT_CPU_NI(653, "用户进程空间内改变过优先级的进程占比", "%"),
    STAT_CPU_SY(654, "系统态使用占比", "%"),
    STAT_CPU_ID(655, "空闲占比", "%"),
    STAT_CPU_WA(656, "等待输入输出占比", "%"),
    STAT_CPU_HI(657, "硬中断占比", "%"),
    STAT_CPU_SI(658, "软中断占比", "%"),
    STAT_CPU_ST(659, "StealTime占比", "%");


    private int code;
    private String desc;
    private String unit;

    CatAgentItemConstants(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    CatAgentItemConstants(int code, String desc, String unit) {
        this.code = code;
        this.desc = desc;
        this.unit = unit;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getUnit() {
        return unit;
    }

    public static String getDesc(int code){
        for (CatAgentItemConstants item : CatAgentItemConstants.values()){
            if(item.getCode() == code){
                return item.getDesc();
            }
        }

        return null;
    }

    public static String getUnit(int code){
        for (CatAgentItemConstants item : CatAgentItemConstants.values()){
            if(item.getCode() == code){
                return item.getUnit();
            }
        }

        return "";
    }
}
