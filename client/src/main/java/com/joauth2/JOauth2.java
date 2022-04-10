package com.joauth2;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author wujiawei
 * @see
 * @since 2022/4/8 15:05
 */
public class JOauth2 {
    private final Log log = LogFactory.get();
    
    public JOauth2() {
        log.info("JOauth2 init");
        new Constructor().execute();
    }
    
}
