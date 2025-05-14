package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SkyApplication.class, args);
        String property = run.getEnvironment().getProperty("server.port");
        log.info("启动成功，http://localhost:{},",property);
    }
}
