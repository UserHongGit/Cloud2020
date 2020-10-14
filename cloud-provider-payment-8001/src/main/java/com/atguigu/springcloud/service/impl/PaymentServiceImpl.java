package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.PayementDao;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PayementDao payementDao;

    @Override
    public int create(Payment payment) {
        return payementDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return payementDao.getPaymentById(id);
    }
}
