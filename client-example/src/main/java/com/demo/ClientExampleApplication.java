package com.demo;

import com.joauth2.JOAuthContextListener;
import com.joauth2.JOAuthListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author wujiawei
 * @see
 * @since 2021/4/26 上午8:44
 */
@ServletComponentScan(basePackageClasses = {JOAuthListener.class, JOAuthContextListener.class})
@SpringBootApplication
public class ClientExampleApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ClientExampleApplication.class, args);
    }
    
}
