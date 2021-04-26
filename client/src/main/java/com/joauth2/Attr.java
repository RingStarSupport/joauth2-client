package com.joauth2;

import cn.hutool.setting.dialect.Props;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态变量统一管理
 *
 * @author wujiawei0926@yeah.net
 * @see
 * @since 2019/5/6
 */
public class Attr {
    
    private static final ConcurrentHashMap<String, Object> ctx = new ConcurrentHashMap<>();

    // 令牌
    public static String TOKEN = "";
    // 间隔结束时间
    public static Date END_TIME = null;
    
    // 请求间隔
    public static void setIntervals(int value) {
        synchronized (Attr.class) {
            ctx.put(Constant.INTERVALS, value);
        }
    }
    
    public static int getIntervals() {
        Object num = ctx.get(Constant.INTERVALS);
        return num == null ? 0 : (Integer) num;
    }
    
    
    // 当前用户数量
    public static void setTotalUser(int value) {
        synchronized (Attr.class) {
            ctx.put(Constant.TOTAL_USER, value);
        }
    }
    
    public static int getTotalUser() {
        Object num = ctx.get(Constant.TOTAL_USER);
        return num == null ? 0 : (Integer) num;
    }
    
    
    // 最大用户数量
    public static void setMaxUser(int value) {
        synchronized (Attr.class) {
            ctx.put(Constant.MAX_USER, value);
        }
    }
    
    public static int getMaxUser() {
        Object maxUser = ctx.get(Constant.MAX_USER);
        return maxUser == null ? 0 : (Integer) maxUser;
    }
    
    
    
    // 离线模式
    public static boolean OFFLINE = false;
    // 开发（演示）模式
    public static boolean DEBUG_MODE = false;
    // 用于输出的信息
    public static String MESSAGE_TMP = "";
    // 配置文件
    public static Props props;
    // Cron的ID
    public static String CRON_UPGRADE_ID = null;
    public static String CRON_APPDATA_ID = null;

    // 支持加密
    public static boolean canEncrypt = true;

    // 统一提示信息
    public static synchronized String getMessage() {
        Object obj = ctx.get(Constant.MESSAGE);
        return obj == null ? "" : (String) obj;
    }
    public static void setMessage(String message, boolean canEncrypt) {
        synchronized (Attr.class) {
            ctx.put(Constant.MESSAGE, message);
            Attr.canEncrypt = canEncrypt;
        }
    }
    public static void setMessage(String message) {
        synchronized (Attr.class) {
            ctx.put(Constant.MESSAGE, message);
        }
    }

    // 重启记录ID
    public static int RESTART_RECORD_ID = -1;

    // app名称（用于邮件提醒）
    public static String APP_NAME = "";

    // 发送邮件相关数据
    public static String MAIL_HOST = "smtp.163.com";
    public static int MAIL_PORT = 25;
    public static String MAIL_FROM = "joauth2@163.com";
    public static String MAIL_PASS = "1q2w3e";
    public static String MAIL_USER = "joauth2";
    public static String MAIL_TO = "36677336@qq.com";

    // 需要重启的文件类型
    public static String[] FILE_RESTART_TYPE = {"class", "java", "xml", "properties", "jar"};
    
    

}
