package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PayementDao {
    int create(Payment payment);
    Payment getPaymentById(@Param("id") Long id);


}
