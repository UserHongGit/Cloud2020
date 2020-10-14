package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PayementController {
    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
        System.out.println("热部署!!");
        if (i > 0) {
            return new CommonResult(200, "插入成功!serverPort: "+serverPort, i);
        }else {
            return new CommonResult<>(444,"插入失败!serverPort: "+serverPort);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment paymentById = paymentService.getPaymentById(id);
        log.info("**********插入结果: "+ paymentById);
        if (paymentById != null) {
            return new CommonResult(200, "查询成功!serverPort:"+serverPort, paymentById);
        }else {
            return new CommonResult<>(444,id+"查询失败!ID:serverPort: "+serverPort);
        }
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

}
