package com.demo;

import com.joauth2.Attr;
import com.joauth2.ClientLogin;
import com.joauth2.ClientUser;
import com.joauth2.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wujiawei
 * @see
 * @since 2021/4/26 上午8:46
 */
@Slf4j
@RestController
public class EchoController {
    
    @GetMapping("echo")
    public String echo() {
        return "echo success";
    }
    
    @GetMapping("attr")
    public String getStaticAttr() {
        int maxUser = Attr.getMaxUser();
        return "max user :" + maxUser;
    }
    
    @GetMapping("login")
    public String login(HttpServletRequest request) {
        String username = "username";
        int id = 1;
        
        ClientUser<Object> user = new ClientUser<Object>();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(username);
        R result = ClientLogin.login(user, request);
        if (result.getCode() == 200) {
            return "login success";
        }
        
        return "login fail: " + result.getMsg();
    }
    
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        ClientLogin.logout(1, request.getSession());
        return "total user: " + Attr.getTotalUser();
    }
    
}
