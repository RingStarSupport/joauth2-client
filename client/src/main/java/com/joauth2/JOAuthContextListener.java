package com.joauth2;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Servlet监听
 *
 * @author wujiawei0926@yeah.net
 * @see
 * @since 2019/5/7
 */
@WebListener
public class JOAuthContextListener implements ServletContextListener {

    private final Log log = LogFactory.get();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Constructor().execute();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.debug("------------contextDestroyed------------");
        ClientLogin.initApp();
        Client.offline();
    }

    
}
