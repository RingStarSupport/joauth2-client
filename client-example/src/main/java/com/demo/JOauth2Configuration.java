package com.demo;

import com.joauth2.JOauth2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujiawei
 * @see
 * @since 2022/4/8 15:06
 */
@Configuration
public class JOauth2Configuration {
    
    @Bean
    public JOauth2 joauth2() {
        return new JOauth2();
    }
    
}
