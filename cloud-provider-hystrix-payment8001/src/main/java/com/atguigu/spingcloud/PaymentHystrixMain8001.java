package com.atguigu.spingcloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

@EnableCircuitBreaker
@EnableHystrix
@SpringBootApplication
public class PaymentHystrixMain8001 {

    /**
     * 此配置是为了服务监控而配置,与服务容错本身无关, springcloud升级后的坑
     * ServletRegistrationBean因为springboot的默认路径不是"/hystrix.strem"
     * 只要在自己项目里配置上下文的servelt就可以了
     * 如果不配置就会报错,提示:
     *  Unable to connect to Command Metric Stream.
     */
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class,args);
    }

}
