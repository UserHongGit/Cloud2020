package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBanlancer {
    ServiceInstance instances(List<ServiceInstance> serviceInstances);



}
