package com.lyl.garfield.core.constants;

/**
 * @author mozilla
 */
public class CatAgentConstants {

    public static final String CATEGORY_BASE = "base";

    public static final String CATEGORY_JVM_GC = "jvm-gc";

    public static final String CATEGORY_JVM_THREAD = "jvm-thread";

    public static final String CATEGORY_JVM_MEMORY = "jvm-memory";

    public static final String CATEGORY_SQL = "sql";

    public static final String CATEGORY_URL = "url";

    public static final String CATEGORY_SYSTEM = "system";


    public static final int MESSAGE_MAIL = 1;

    public static final int MESSAGE_SMS = 2;

    public static final int MESSAGE_DING = 3;


    public static final String MESSAGE_TEXT_MAIL_TEXT = "系统告警,请注意";

    // 邮件通知文案
    public static final String MESSAGE_TEXT_MAIL_CONTENT =
            "您负责的系统%app%于%time%发生告警,详情为%item%超过监控阈值%valve%,达到%value%,如下机器发生告警[%cluster%],请速排查原因!";

    // 短信通知文案
    public static final String MESSAGE_TEXT_SMS_CONTENT =
            "您负责的系统%app%于%time%发生告警,详情为%item%超过监控阈值%valve%,达到%value%,如下机器发生告警[%cluster%],请速排查原因!";



    // 通用状态: 成功,正确,有效,合法
    public static final int STATUS_YES = 1;
    // 通用状态: 失败,错误,无效,非法
    public static final int STATUS_NO = 0;



    // 未知错误
    public static final String ERROR_SYSTEM = "ERROR_SYSTEM";
    // 非法参数
    public static final String ERROR_PARAMETER = "ERROR_PARAMETER";
    // 应用不存在
    public static final String ERROR_APP_NOT_EXIST = "ERROR_APP_NOT_EXIST";
    // 指定监控项不存在
    public static final String ERROR_ITEM_NOT_EXIST = "ERROR_ITEM_NOT_EXIST";
    // 告警规则不存在
    public static final String ERROR_ALARM_RULE_NOT_EXIST = "ERROR_ALARM_RULE_NOT_EXIST";



    public static final String PATH_BASE = "/tmp/monitor/base.log";

    public static final String PATH_GC = "/tmp/monitor/gc.log";

    public static final String PATH_MEMORY = "/tmp/monitor/memroy.log";

    public static final String PATH_THREAD = "/tmp/monitor/thread.log";

    public static final String PATH_SQL = "/tmp/monitor/sql.log";

    public static final String PATH_URL = "/tmp/monitor/url.log";

    public static final String PATH_SYSTEM = "/tmp/monitor/system.log";
}














