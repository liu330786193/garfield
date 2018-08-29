package com.lyl.garfield.core.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 一些杂七杂八的工具合集.
 *
 * @Author mozilla
 */
public class MixAll {

    private static NumberFormat nf = NumberFormat.getInstance();

    private static NumberFormat pf = NumberFormat.getPercentInstance();

    static{
        nf.setMinimumFractionDigits(4);
        pf.setMinimumFractionDigits(2);
    }

    /**
     * 将文件内容转换为字符列表返回.适用于获取/proc目录下的文件的场景
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String[]> file2String(final File file) throws IOException {
        List<String[]> result = new ArrayList<String[]>();
        FileReader fileReader = null;
        BufferedReader fr = null;
        try {
            fileReader = new FileReader(file);
            fr = new BufferedReader(fileReader);
            String line = null;
            while ((line = fr.readLine()) != null) {
                result.add(line.trim().split("\\s+"));
            }
        }finally {
            if(fr != null){
                fr.close();
            }

            if(fileReader != null){
                fileReader.close();
            }
        }
        return result;
    }

    /**
     * SQL格式的标准统一输出.
     * 例:
     *    select *
     *    from
     *    A
     *    WHERE NAME = ?
     *
     * 输出
     *    select * from a where name = ?
     *
     * @param sql
     * @return
     */
    public static String formatSQL(String sql){
        if(StringUtil.isBlank(sql)){
            return sql;
        }

        // 删除换行符
        sql = sql.replace("\r\n", "");

        // 考虑多个空格情况下的打散
        String[] array = sql.trim().split("\\s+");
        StringBuffer sb = new StringBuffer();
        for(String word : array){
            sb.append(word).append(" ");
        }

        // 删除末尾空格并统一切换为小写
        return sb.toString().trim().toLowerCase();
    }

    /**
     * 小数格式输出
     *
     * @param left
     * @param right
     * @return
     */
    public synchronized static String formatDouble(long left, long right){
        if(right <= 0){
            return "0.0000";
        }

        return nf.format(left*1.0 / right);
    }

    /**
     * 转换为百分数
     *
     * @param value
     * @return
     */
    public synchronized static String formatPercent(double value){
        return pf.format(value);
    }

    /**
     * 对于简单文本做MD5签名
     *
     * @param source
     * @return
     */
    public static String md5(String source) {
        StringBuffer sb = new StringBuffer(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));
            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            // IGNORE
            return null;
        }

        return sb.toString();
    }
}
