package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBanlancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OderController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBanlancer loadBanlancer;
    @Autowired
    private DiscoveryClient discoveryClient;

//    public static final String PAYMENT_URL = "http://localhost:8001";   //本地ip访问,走本地
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";   //走Eureka
   /*
   使用服务名去请求服务, 因为服务是集群,两个provider,这时候restTemplate就找不到要请求访问哪一个了
    需要在配置restTemplate Bean对象的时候,添加@LoadBalanced 添加默认的负载均衡规则
    */

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return  restTemplate.postForObject(PAYMENT_URL+"/payment/create"
                , payment,CommonResult.class);
    }

    @GetMapping("/consumer/payment/{id}")
    public CommonResult<Payment> getPayement(@PathVariable("id") Long id){
        return  restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,
                CommonResult.class);
    }

    @GetMapping("/consumer/payment2/{id}")
    public CommonResult<Payment> getPayement2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        if(entity.getStatusCode().is2xxSuccessful()) {
            return  entity.getBody();
        }else {
            return  new CommonResult<>(444,"操作失败!");
        }
    }

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE"); //获得该微服务下的所有实例,即所有的跑的程序
        instances.forEach(item -> {
            log.info(item.getServiceId()+"****"+item.getHost()+"***"+item.getPort()+"*****"+item.getUri());
        });
        if(instances == null || instances.size() <= 0) {
            return null;
        }

        ServiceInstance serviceInstance = loadBanlancer.instances(instances);
        URI uri = serviceInstance.getUri();

        return restTemplate.getForObject(uri+"/payment/lb",String.class);
    }

    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin(){
        return restTemplate.getForObject("http://localhost:8001"+"/payment/zipkin",String.class);
    }



}
