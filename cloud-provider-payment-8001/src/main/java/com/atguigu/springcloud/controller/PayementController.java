package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class PayementController {
    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/payment/discovery")
    public DiscoveryClient discoveryById(){
        List<String> services = discoveryClient.getServices();  //获取所有微服务
        services.forEach(item -> {
            log.info("所有的服务名:****"+item);
        });
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service"); //获得该微服务下的所有实例,即所有的跑的程序
        instances.forEach(item -> {
            log.info(item.getServiceId()+"****"+item.getHost()+"***"+item.getPort()+"*****"+item.getUri());
        });
        return this.discoveryClient;
    }

    @PostMapping(value = "/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
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

    /**
     * 故意超时
     * @return
     */
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping(value = "/payment/zipkin")
    public String paymentZipkin(){
        return  "hi, I'am paymentZipkin server fall back, >>>>>>>>>>>>>>";
    }

}
