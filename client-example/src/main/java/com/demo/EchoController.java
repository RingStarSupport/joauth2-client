package com.demo;

import cn.hutool.core.util.RandomUtil;
import com.joauth2.Attr;
import com.joauth2.ClientLogin;
import com.joauth2.ClientUser;
import com.joauth2.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wujiawei
 * @see
 * @since 2021/4/26 上午8:46
 */
@Slf4j
@RestController
public class EchoController {
    
    private static final List<Integer> ids = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @GetMapping("echo")
    public String echo() {
        return "echo success";
    }
    
    @GetMapping("max-user")
    public String getStaticAttr() {
        int maxUser = Attr.getMaxUser();
        return "max user :" + maxUser;
    }
    
    @GetMapping("login")
    public String login(HttpServletRequest request) {
        String username = RandomUtil.randomString(RandomUtil.randomInt(1, 9));
        int id = RandomUtil.randomInt();
        ids.add(id);
        
        ClientUser<Object> user = new ClientUser<Object>();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(username);
        R result = ClientLogin.login(user, request);
        if (result.getCode() == 200) {
            return "login success: total user["+ Attr.getTotalUser() +"], max user["+ Attr.getMaxUser() +"]";
        }
        
        return "login fail: " + result.getMsg();
    }
    
    @GetMapping("login/batch")
    public String loginBatch(Integer max, HttpServletRequest request) {
        max = max == null ? 66 : max;
        CountDownLatch latch = new CountDownLatch(max);
        for (int i = 0; i < max; i++) {
            final int finalI = i;
            final HttpServletRequest finalReq = request;
            executorService.submit(()->{
                try {
                    String username = RandomUtil.randomString(RandomUtil.randomInt(1, 9));
                    int id = RandomUtil.randomInt(10, 999999);
                    ids.add(id);
    
                    ClientUser<Object> user = new ClientUser<Object>();
                    user.setId(id);
                    user.setUsername(username);
                    user.setNickname(username);
                    R r = ClientLogin.login(user, finalReq);
                    log.info("["+ finalI +"] " + r.getMsg());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
    
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "all user login complete, total user["+ Attr.getTotalUser() +"]";
    }
    
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        int index = RandomUtil.randomInt(0, ids.size());
        int id = ids.get(index);
        ClientLogin.logout(id, request.getSession());
        ids.remove(id);
        return "total user: " + Attr.getTotalUser();
    }
    
    @GetMapping("logout/all")
    public String logoutAll(HttpServletRequest request) {
        for (Integer id : ids) {
            ClientLogin.logout(id, request.getSession());
        }
        ids.clear();
        return "complete";
    }
    
}
