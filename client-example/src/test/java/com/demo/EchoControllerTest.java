package com.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;

/**
 * @author wujiawei
 * @see
 * @since 2021/4/26 上午8:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EchoControllerTest {
    
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EchoController()).build();
    }
    
    @Test
    public void testEcho() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/echo")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("name", "hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("echo success"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void testLogin() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/login")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        System.out.println("=========\n" + result.getResponse().getContentAsString());
    }
    

}
