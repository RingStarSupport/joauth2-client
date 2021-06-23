package com.joauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.dialect.Props;
import com.joauth2.upgrade.FileManager;

/**
 * 初始化构造器
 * @author wujiawei
 * @see
 * @since 2021/6/22 10:26 上午
 */
public class Constructor {
    
    private final Log log = LogFactory.get();
    
    public void execute() {
        String localIp = AuthSecureUtils.getInnetIp();
        log.info("[constructor] 当前IP: {}", localIp);
    
        // 检测配置文件
        String checkPropsStr = checkProps();
        if (StrUtil.isNotEmpty(checkPropsStr)) {
            Attr.setMessage(OAuth2Constants.INVALID_PROPERTIES + checkPropsStr);
            Attr.canEncrypt = false;
        }
    
        // 检测token和expire_in
//        if (StrUtil.isNotEmpty(Attr.TOKEN) && Attr.getIntervals() != 0) {
//            Attr.setMessage("无效的TOKEN，请检查Client文件是否损坏");
//            Attr.canEncrypt = false;
//        }
    
        // 获取token
        if (Attr.canEncrypt) {
            Attr.setMessage(Client.getCode());
        }
        Attr.MESSAGE_TMP = Attr.getMessage();
        log.info("[constructor] {}",Attr.getMessage());
    
        // 初始化应用
        ClientLogin.initApp();
    
        if (!Attr.OFFLINE && StrUtil.isNotEmpty(Attr.TOKEN)) {
            // 延迟刷新Client数据
            Scheduler.refreshClient();
            // keepalive
            Scheduler.keepAlive();
            // 开启自动更新
            FileManager.autoUpgrade();
            // 开启定时任务
            CronUtil.start();
        }
    }
    
    /**
     * 检查配置文件
     * @return
     */
    private String checkProps() {
        StringBuilder sb = new StringBuilder("");
        Props props = null;
        
        // 读取配置文件
        try {
            Attr.props = new Props("application.properties");
            props = Attr.props;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (props == null) {
            log.error("缺少配置项，请检查配置文件application.properties和授权相关配置");
        }
        
        if (!props.containsKey("auth.app_key")) {
            sb.append("[auth.app_key] ");
        }
        
        if (!props.containsKey("auth.app_secret")) {
            sb.append("[auth.app_secret] ");
        }
        
        if (!props.containsKey("auth.url")) {
            sb.append("[auth.url] ");
        }
        
        if (!props.containsKey("auth.app_encrypt")) {
            sb.append( "[auth.app_encrypt] " );
        }
        
        return sb.toString();
    }
}
