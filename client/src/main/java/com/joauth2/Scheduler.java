package com.joauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务类
 *
 * @author wujiawei0926@yeah.net
 * @see
 * @since 2019/5/6
 */
public class Scheduler {
    
    private final Log log = LogFactory.get();
    private final ScheduledExecutorService executorService;
    private static final long periodTime = 2312L;
    
    public Scheduler() {
        this.executorService = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("com.joauth2.scheduler");
                return thread;
            }
        });
    }

    /**
     * 定时获取最新的应用数据
     */
    public void refreshClient() {
        Attr.CRON_APPDATA_ID = CronUtil.schedule("*/1 * * * *", (Task) () -> {
            synchronized (Scheduler.class) {
                Client.updateAppData();
                // 在授权的有效时间内持续获取更新数据
                Date endTime = Attr.END_TIME;
                // 判断是否是间隔的结束时间
                Date now = new Date();
                if (endTime == null || endTime.getTime() < now.getTime()) {
//                    Client.updateAppData();
                }
            }
        });
    }
    
    /**
     * 告诉服务端我还活着
     * 当服务端超过N分钟未接收到「我还活着」的消息时，强制杀死该客户端
     */
    public void keepAlive() {
        executorService.schedule(new AliveTask(), periodTime, TimeUnit.MILLISECONDS);
        CronUtil.schedule("*/1 * * * *", (Task) Client::keepAlive);
    }
    
    class AliveTask implements Runnable {
        @Override
        public void run() {
            if (Attr.OFFLINE || StrUtil.isEmpty(Attr.TOKEN)) {
                return;
            }
            try {
                Client.keepAlive();
            } catch (Exception e) {
                log.error("KeepAliveTask error", e);
            } finally {
                executorService.schedule(new AliveTask(), periodTime, TimeUnit.MILLISECONDS);
            }
        }
    }

}
