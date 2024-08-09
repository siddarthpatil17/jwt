package com.random.siddhu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.random.siddhu")
@EnableScheduling
@EnableTransactionManagement
public class SiddhuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiddhuApplication.class, args);
    }
}
