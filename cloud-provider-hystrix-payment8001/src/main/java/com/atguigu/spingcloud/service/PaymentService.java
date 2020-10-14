package com.atguigu.spingcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /**
     * 正常访问的方法
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id){
        return "线程池:   "+Thread.currentThread().getName()+"  paymentInfo_OK ,id:"+id+",OKOKOKOKO!";
    }

//    HystrixProperty设置三秒以内执行完属于正常, 超时就是异常的
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id){
        int timeNumber = 5;
        int age = 10/0;   //故意出错, 只要当前方法不可以用,就会进入降级方法paymentInfo_TimeOutHandler()
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:   "+Thread.currentThread().getName()+"  paymentInfo_TimeOut ,id:"+id+",OKOKOKOKO!"+", 正常程序,需要耗时"+timeNumber+"秒钟";
    }

    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池:   "+Thread.currentThread().getName()+"  paymentInfo_TimeOutHandler ,系统繁忙,请稍后再试!id:"+id+",OKOKOKOKO!"+", 熔断方法执行!";
    }


//------------------上边服务降级,下边服务熔断----------------------------------


    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
//        属性值都是从这个类获取的   HystrixCommandProperties
        @HystrixProperty(name = "circuitBreaker.enabled",value="true"),  //是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value="10"), //请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value="10000"), //时间窗口期
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value="60") //失败率达到多少后跳闸
//        总结来说就是:在(时间窗口期) 10秒之间(10000毫秒),有10次请求之间, 有百分之60是失败的, 就跳闸
    })
    public String paymentCircuitBreaker(Integer id){
        if(id < 0)
        {
            throw new RuntimeException("***********id不能为负数!");
        }
        String SerialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+"调用成功,流水号:"+SerialNumber;
    }

    public String paymentCircuitBreaker_fallback(Integer id){
        return "id 不能为负数,请重试!";
    }






}
