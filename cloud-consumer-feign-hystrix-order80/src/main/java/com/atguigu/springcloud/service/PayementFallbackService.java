package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PayementFallbackService implements PayementHystrixService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return "PayementFallbackService.paymentInfo_OK,熔断处理!";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "PayementFallbackService.paymentInfo_TimeOut()熔断处理";
    }
}
