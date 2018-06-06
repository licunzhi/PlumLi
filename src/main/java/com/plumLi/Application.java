package com.plumLi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot 启动类
 *
 * @author licunzhi
 */
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.plumLi"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}