package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrderFeignMain8888 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain8888.class,args);
    }
}
