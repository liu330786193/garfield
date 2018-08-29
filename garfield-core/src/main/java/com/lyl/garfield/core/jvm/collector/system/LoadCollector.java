package com.lyl.garfield.core.jvm.collector.system;

import com.timevale.cat.core.utils.MixAll;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.timevale.cat.core.constants.CatAgentItemConstants.*;

/**
 * @Author mozilla
 */
public class LoadCollector {

    private static final String LOAD_PATH = "/proc/loadavg";

    public static Map<Object, Object> collect(){
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        try {
            List<String[]> loadList = MixAll.file2String(new File(LOAD_PATH));
            if(loadList != null && loadList.size() > 0){
                String[] line = loadList.get(0);
                result.put(LOAD_1.getCode(), line[0]);
                result.put(LOAD_5.getCode(), line[1]);
                result.put(LOAD_15.getCode(), line[2]);
            }
        }catch(Exception e){
            // ignore
        }
        return result;
    }
}
