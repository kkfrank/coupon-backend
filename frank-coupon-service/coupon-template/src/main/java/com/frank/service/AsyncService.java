package com.frank.service;

import com.frank.entity.CouponTemplate;


public interface AsyncService {
    void asyncCreateCouponByTemplate(CouponTemplate template);
}
