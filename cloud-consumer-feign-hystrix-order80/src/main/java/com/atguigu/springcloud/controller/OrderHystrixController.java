package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PayementHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.util.resources.cldr.ss.CalendarData_ss_ZA;

@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
@RestController
@Slf4j
public class OrderHystrixController {
    @Autowired
    private PayementHystrixService payementHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = payementHystrixService.paymentInfo_OK(id);
        return result;
    }

    /**
     * 配置针对性的熔断方法,  >>>  造成代码膨胀
     * @param id
     * @return
     */
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutFallbackMethod",commandProperties = {
//            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "1500")
//    })
    @HystrixCommand  //和上边的会冲突
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        int x = 10/0;
        String result = payementHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    public String paymentInfo_TimeOutFallbackMethod(@PathVariable("id") Integer id){
        return "我是消费者80,对方支付系统繁忙或者自己代码运行出错!请检查重试!";
    }


    /**
     * 配置全局的降级方法
     * 在类上配置
     *  @DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
     * 之后那个方法需要降级,就直接加@HystrixCommand 注解就可以了
     * @return
     */
    public String payment_Global_FallbackMethod(){
        return  "全局默认配置Global异常处理信息, 请稍后再试!";
    }




}
