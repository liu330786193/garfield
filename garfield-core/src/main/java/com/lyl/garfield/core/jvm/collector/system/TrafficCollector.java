package com.lyl.garfield.core.jvm.collector.system;

import com.lyl.garfield.core.utils.MixAll;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lyl.garfield.core.constants.CatAgentItemConstants.*;

/**
 * http://www.onlamp.com/pub/a/linux/2000/11/16/LinuxAdmin.html
 * http://blog.csdn.net/yzy1103203312/article/details/77848192
 * http://www.361way.com/proc-awk-network/4971.html
 *
 * attention:
 *
 * redhat:
 *   Inter-|   Receive                                                |  Transmit
     face |bytes    packets errs drop fifo frame compressed multicast|bytes    packets errs drop fifo colls carrier compressed
     lo:3645014508 10637877    0    0    0     0          0         0 3645014508 10637877    0    0    0     0       0          0
     eth0:26092004378 61025436    0    0    0     0          0         0 23697620592 49665409    0    0    0     0       0          0
     eth1:62034834182 188520247    0    0    0     0          0         0 34076374617 186784589    0    0    0     0       0          0
 *
 * fedora:
     Inter-|      Receive   |        Transmit
     face         |bytes    packets  errs      drop  fifo  frame  compressed  multicast|bytes  packets   errs   drop  fifo  colls  carrier  compressed
     lo:          11531785  99165    0         0     0     0      0           0                11531785  99165  0     0     0      0        0           0
     enp0s3:      37703347  27836    0         0     0     0      0           0                1014008   12784  0     0     0      0        0           0
 *
 *  compressed: The number of compressed packets transmitted or received by the device driver.
 *  (This appears to be unused in the 2.2.15 kernel)
 *  过滤了该选项.
 *
 *  相对来说最关心的是pktin,pktout,pbtin,pbtout.
 *
 * @Author mozilla
 */
public class TrafficCollector {

    private static final String TRAFFIC_PATH = "/proc/net/dev";

    private static Map<String, Map<Integer, String>> lastMap = new HashMap<String, Map<Integer, String>>();

    public static Map<String, Map<Object, Object>> collect(){
        Map<String, Map<Object, Object>> result = new LinkedHashMap<String, Map<Object, Object>>();
        try {
            List<String[]> netList = MixAll.file2String(new File(TRAFFIC_PATH));

            // 前两行为title.
            if(netList == null || netList.size() <= 2){
                return result;
            }

            // 每一行对应一个网卡.
            int index = 0;
            for(String[] line : netList){
                index++;
                if(index <= 2){
                    // ignore title
                    continue;
                }

                String[] firstkv = line[0].split(":");
                String netCard = firstkv[0];
                if(netCard.startsWith("virbr") || netCard.equals("lo")){
                    // ignore virtual bridge net and localhost
                    continue;
                }

                if(firstkv.length == 1){
                    handleFedora(result, line, netCard);
                }else{
                    handleNormal(result, line, netCard, firstkv[1]);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 针对Fedora系统的特殊处理
     *
     * @param result
     * @param line
     * @param netCard
     */
    private static void handleFedora(Map<String, Map<Object, Object>> result,
        String[] line, String netCard){

        calculate(result, netCard, line[1], line[2], line[9], line[10]);
    }

    /**
     * 针对其余的linux系统的标准处理
     *
     * @param result
     * @param line
     * @param netCard
     * @param first
     */
    private static void handleNormal(Map<String, Map<Object, Object>> result,
        String[] line, String netCard, String first){

        calculate(result, netCard, first, line[1], line[8], line[9]);
    }

    private static void calculate(Map<String, Map<Object, Object>> result,
            String netCard, String pktin, String pktout, String pkbin, String pkbout){
        // 针对某个网卡的上一次数据
        Map<Integer, String> singleLastMap = lastMap.get(netCard);
        if(singleLastMap == null){
            singleLastMap = new HashMap<Integer, String>();
            singleLastMap.put(NET_RECEIVE_BYTES.getCode(), pkbin);
            singleLastMap.put(NET_RECEIVE_PACKETS.getCode(), pktin);
            singleLastMap.put(NET_TRANSMIT_BYTES.getCode(), pkbout);
            singleLastMap.put(NET_TRANSMIT_PACKETS.getCode(), pktout);
            lastMap.put(netCard, singleLastMap);
            return;
        }

        long last_receive_bytes = Long.parseLong(singleLastMap.get(NET_RECEIVE_BYTES.getCode()));
        long last_receive_packets = Long.parseLong(singleLastMap.get(NET_RECEIVE_PACKETS.getCode()));
        long last_transmit_bytes = Long.parseLong(singleLastMap.get(NET_TRANSMIT_BYTES.getCode()));
        long last_transmit_packets = Long.parseLong(singleLastMap.get(NET_TRANSMIT_PACKETS.getCode()));

        Map<Object, Object> entityMap = new LinkedHashMap<Object, Object>();
        entityMap.put(NET_RECEIVE_BYTES.getCode(), pkbin);
        entityMap.put(NET_RECEIVE_BYTES_SPAN.getCode(), Long.parseLong(pkbin) - last_receive_bytes);
        entityMap.put(NET_RECEIVE_PACKETS.getCode(), pktin);
        entityMap.put(NET_RECEIVE_PACKETS_SPAN.getCode(), Long.parseLong(pktin) - last_receive_packets);
        entityMap.put(NET_TRANSMIT_BYTES.getCode(), pkbout);
        entityMap.put(NET_TRANSMIT_BYTES_SPAN.getCode(), Long.parseLong(pkbout) - last_transmit_bytes);
        entityMap.put(NET_TRANSMIT_PACKETS.getCode(), pktout);
        entityMap.put(NET_TRANSMIT_PACKETS_SPAN.getCode(), Long.parseLong(pktout) - last_transmit_packets);
        result.put(netCard, entityMap);
    }
}
