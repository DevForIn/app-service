package com.mooo.devforin.appservice;

import com.mooo.devforin.appservice.config.security.JasyptConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppServiceApplicationTests {

    @Autowired
    JasyptConfig jasyptConfig;

    @Test
    void contextLoads() {

        System.out.println("password:" + jasyptConfig.stringEncryptor().encrypt("kblife1234"));

    }

}
