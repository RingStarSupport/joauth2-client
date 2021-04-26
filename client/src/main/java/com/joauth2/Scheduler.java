package com.joauth2;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;

import java.util.Date;

/**
 * 定时任务类
 *
 * @author wujiawei0926@yeah.net
 * @see
 * @since 2019/5/6
 */
public class Scheduler {

    /**
     * 定时获取最新的应用数据
     */
    public static void refreshClient() {
        Attr.CRON_APPDATA_ID = CronUtil.schedule("*/1 * * * *", (Task) () -> {
            synchronized (Scheduler.class) {
                // 在授权的有效时间内持续获取更新数据
                Date endTime = Attr.END_TIME;
                // 判断是否是间隔的结束时间
                Date now = new Date();
                if (endTime == null || endTime.getTime() < now.getTime()) {
                    Client.updateAppData();
                }
            }
        });
    }
    
    /**
     * 告诉服务端我还活着
     * 当服务端超过N分钟未接收到「我还活着」的消息时，强制杀死该客户端
     */
    public static void keepAlive() {
        CronUtil.schedule("*/1 * * * *", (Task) Client::keepAlive);
    }

}
