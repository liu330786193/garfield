package com.lyl.garfield.core.jvm.collector.system;

import com.lyl.garfield.core.utils.MixAll;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lyl.garfield.core.constants.CatAgentItemConstants.*;

/**
 * https://access.redhat.com/documentation/zh-cn/red_hat_enterprise_linux/7/html/performance_tuning_guide/sect-red_hat_enterprise_linux-performance_tuning_guide-networking-monitoring_and_diagnosing_performance_problems#sect-Red_Hat_Enterprise_Linux-Performance_Tuning_Guide-Monitoring_and_diagnosing_performance_problems-procnetsnmp
 * http://blog.csdn.net/b2222505/article/details/54233524
 * http://www.cnblogs.com/super-king/p/3296333.html
 * https://gist.github.com/MarloweW/5146bb0acd0aa05adf076a449d795890
 *
 * SNMP文件包括了IP,Icmp,IcmpMsg,TCP,UDP.在本期监控里,我们只关注TCP和UDP数据.
 *
 * @Author mozilla
 */
public class TCPCollector {

    private static final String TCP_PATH = "/proc/net/snmp";

    private static final String CAT_TCP_TOTAL = "tcpTotal";
    private static final String CAT_TCP_SPAN = "tcpSpan";
    private static final String CAT_UDP_TOTAL = "udpTotal";
    private static final String CAT_UDP_SPAN = "udpSpan";

    private static boolean tcp_span = false;
    private static long last_active_opens = 0;
    private static long last_passive_opens = 0;
    private static long last_attempt_fails = 0;
    private static long last_estab_resets = 0;
    private static long last_curr_estab = 0;
    private static long last_in_segs = 0;
    private static long last_out_segs = 0;
    private static long last_retrans_segs = 0;
    private static long last_in_errs = 0;

    private static boolean udp_span = false;
    private static long last_in_datagrams = 0;
    private static long last_out_datagrams = 0;
    private static long last_no_ports = 0;
    private static long last_in_errors = 0;

    /**
     * 处理UDP
     *
     * @param udpColumn
     * @param udpValue
     * @return
     */
    private static Map<String, Map<Object, Object>> handleUdp(String[] udpColumn, String[] udpValue) {
        Map<String, Map<Object, Object>> result = new LinkedHashMap<String, Map<Object, Object>>();
        // 首次进入先设置span
        if (!udp_span) {
            last_in_datagrams = Long.parseLong(udpValue[1]);
            last_no_ports = Long.parseLong(udpValue[2]);
            last_in_errors = Long.parseLong(udpValue[3]);
            last_out_datagrams = Long.parseLong(udpValue[4]);
            udp_span = true;
            return result;
        }

        long now_in_datagrams = Long.parseLong(udpValue[1]);
        long now_no_ports = Long.parseLong(udpValue[2]);
        long now_in_errors = Long.parseLong(udpValue[3]);
        long now_out_datagrams = Long.parseLong(udpValue[4]);
        Map<Object, Object> totalMap = new LinkedHashMap<Object, Object>();
        totalMap.put(SNMP_UDP_IN_DATAGRAMS.getCode(), now_in_datagrams);
        totalMap.put(SNMP_UDP_NO_PORTS.getCode(), now_no_ports);
        totalMap.put(SNMP_UDP_IN_ERRORS.getCode(), now_in_errors);
        totalMap.put(SNMP_UDP_OUT_DATAGRAMS.getCode(), now_out_datagrams);
        result.put(CAT_UDP_TOTAL, totalMap);

        Map<Object, Object> spanMap = new LinkedHashMap<Object, Object>();
        spanMap.put(SNMP_UDP_IN_DATAGRAMS_SPAN.getCode(), now_in_datagrams - last_in_datagrams);
        spanMap.put(SNMP_UDP_NO_PORTS_SPAN.getCode(), now_no_ports - last_no_ports);
        spanMap.put(SNMP_UDP_IN_ERRORS_SPAN.getCode(), now_in_errors - last_in_errors);
        spanMap.put(SNMP_UDP_OUT_DATAGRAMS_SPAN.getCode(), now_out_datagrams - last_out_datagrams);
        result.put(CAT_UDP_SPAN, spanMap);

        last_in_datagrams = now_in_datagrams;
        last_no_ports = now_no_ports;
        last_in_errors = now_in_errors;
        last_out_datagrams = now_out_datagrams;
        return result;
    }

    /**
     * 处理TCP
     *
     * @param tcpColumn
     * @param tcpValue
     * @return
     */
    private static Map<String, Map<Object, Object>> handleTcp(String[] tcpColumn, String[] tcpValue){
        Map<String, Map<Object, Object>> result = new LinkedHashMap<String, Map<Object, Object>>();
        // 首次进入先设置span
        if(!tcp_span){
            last_active_opens = Long.parseLong(tcpValue[5]);
            last_passive_opens = Long.parseLong(tcpValue[6]);
            last_attempt_fails = Long.parseLong(tcpValue[7]);
            last_estab_resets = Long.parseLong(tcpValue[8]);
            last_curr_estab = Long.parseLong(tcpValue[9]);
            last_in_segs = Long.parseLong(tcpValue[10]);
            last_out_segs = Long.parseLong(tcpValue[11]);
            last_retrans_segs = Long.parseLong(tcpValue[12]);
            last_in_errs = Long.parseLong(tcpValue[13]);
            tcp_span = true;
            return result;
        }

        long now_active_opens = Long.parseLong(tcpValue[5]);
        long now_passive_opens = Long.parseLong(tcpValue[6]);
        long now_attempt_fails = Long.parseLong(tcpValue[7]);
        long now_estab_resets = Long.parseLong(tcpValue[8]);
        long now_curr_estab = Long.parseLong(tcpValue[9]);
        long now_in_segs = Long.parseLong(tcpValue[10]);
        long now_out_segs = Long.parseLong(tcpValue[11]);
        long now_retrans_segs = Long.parseLong(tcpValue[12]);
        long now_in_errs = Long.parseLong(tcpValue[13]);
        Map<Object, Object> totalMap = new LinkedHashMap<Object, Object>();
        totalMap.put(SNMP_TCP_ACTIVE_OPENS.getCode(), now_active_opens);
        totalMap.put(SNMP_TCP_PASSIVE_OPENS.getCode(), now_passive_opens);
        totalMap.put(SNMP_TCP_ATTEMPT_FAILS.getCode(), now_attempt_fails);
        totalMap.put(SNMP_TCP_ESTAB_RESETS.getCode(), now_estab_resets);
        totalMap.put(SNMP_TCP_CURR_ESTAB.getCode(), now_curr_estab);
        totalMap.put(SNMP_TCP_IN_SEGS.getCode(), now_in_segs);
        totalMap.put(SNMP_TCP_OUT_SEGS.getCode(), now_out_segs);
        totalMap.put(SNMP_TCP_RETRANS_SEGS.getCode(), now_retrans_segs);
        totalMap.put(SNMP_TCP_IN_ERRS.getCode(), now_in_errs);
        result.put(CAT_TCP_TOTAL, totalMap);

        Map<Object, Object> spanMap = new LinkedHashMap<Object, Object>();
        spanMap.put(SNMP_TCP_ACTIVE_OPENS_SPAN.getCode(), now_active_opens - last_active_opens);
        spanMap.put(SNMP_TCP_PASSIVE_OPENS_SPAN.getCode(), now_passive_opens - last_passive_opens);
        spanMap.put(SNMP_TCP_ATTEMPT_FAILS_SPAN.getCode(), now_attempt_fails - last_attempt_fails);
        spanMap.put(SNMP_TCP_ESTAB_RESETS_SPAN.getCode(), now_estab_resets - last_estab_resets);
        spanMap.put(SNMP_TCP_CURR_ESTAB_SPAN.getCode(), now_curr_estab - last_curr_estab);
        spanMap.put(SNMP_TCP_IN_SEGS_SPAN.getCode(), now_in_segs - last_in_segs);
        spanMap.put(SNMP_TCP_OUT_SEGS_SPAN.getCode(), now_out_segs - last_out_segs);
        spanMap.put(SNMP_TCP_RETRANS_SEGS_SPAN.getCode(), now_retrans_segs - last_retrans_segs);
        spanMap.put(SNMP_TCP_RETRANS_SEGS_PERCENT_SPAN.getCode(),
                MixAll.formatDouble(now_retrans_segs - last_retrans_segs, now_out_segs - last_out_segs));
        spanMap.put(SNMP_TCP_IN_ERRS_SPAN.getCode(), now_in_errs - last_in_errs);
        result.put(CAT_TCP_SPAN, spanMap);

        last_active_opens = now_active_opens;
        last_passive_opens = now_passive_opens;
        last_attempt_fails = now_attempt_fails;
        last_estab_resets = now_estab_resets;
        last_curr_estab = now_curr_estab;
        last_in_segs = now_in_segs;
        last_out_segs = now_out_segs;
        last_retrans_segs = now_retrans_segs;
        last_in_errs = now_in_errs;

        return result;
    }

    /**
     *   Tcp: RtoAlgorithm RtoMin RtoMax MaxConn
     *         4
     *        ActiveOpens PassiveOpens AttemptFails EstabResets CurrEstab InSegs OutSegs RetransSegs InErrs OutRsts
         Tcp: 1 200 120000 -1 3876564 6563968 486636 273225 61 258219972 243898501 593483 6701 1446536
         Udp: InDatagrams NoPorts InErrors OutDatagrams
         Udp: 1507552 157351 464 2179888

         tcp
         ActiveOpens:主动打开的tcp连接数量。
         PassiveOpens:被动打开的tcp连接数量。
         AttemptFails: 连接失败的数量。
         EstabResets: established 中发生的 reset。
         CurrEstab:当前状态为ESTABLISHED的tcp连接数。
         InSegs: 收到的tcp报文数量。
         OutSegs:发出的tcp报文数量。
         RetransSegs: 重传的报文数量。
         InErrs: 错误包
         OutRsts: 本期暂时不做采集

         retran:系统的重传率 (RetransSegs－last RetransSegs) ／ (OutSegs－last OutSegs) * 100%
         udp

         InDatagrams
         OutDatagrams
         NoPorts: 目的地址或者端口不存在。
         InErrors： 无效数据包。
         RcvbufErrors：内核的 buffer 满了导致的接收失败。
         SndbufErrors：同上。
         InCsumErrors：checksum 错误的 udp 包数量。
     */
    public static Map<String, Map<Object, Object>> collect(){
        Map<String, Map<Object, Object>> result = new LinkedHashMap<String, Map<Object, Object>>();
        try {
            List<String[]> netList = MixAll.file2String(new File(TCP_PATH));

            String[] tcpColumn = null;
            String[] tcpValue = null;
            String[] udpColumn = null;
            String[] udpValue = null;
            for(String[] line : netList){
                String type = line[0];
                if(type.equals("Tcp:")){
                    if(tcpColumn == null){
                        tcpColumn = line;
                        continue;
                    }else{
                        tcpValue = line;
                    }
                }
                if(type.equals("Udp:")){
                    if(udpColumn == null){
                        udpColumn = line;
                        continue;
                    }else{
                        udpValue = line;
                    }
                }
            }

            // 异常数据
            if(tcpColumn == null || tcpValue == null || udpColumn == null || udpValue == null){
                return result;
            }
            if(tcpColumn.length != tcpValue.length || udpColumn.length != udpValue.length){
                return result;
            }

            result.putAll(handleTcp(tcpColumn, tcpValue));
            result.putAll(handleUdp(udpColumn, udpValue));
            return result;
        }catch(Exception e){
            // ignore
        }
        return result;
    }
}
