package com.frank.coupon.service;

import com.frank.coupon.entity.Coupon;
import com.frank.coupon.exception.CouponException;

import java.util.List;

public interface RedisService {

    List<Coupon> getCachedCoupons(Long userId, Integer status);

    //避免缓存穿透
    void saveEmptyCouponToCache(Long userId, List<Integer> status);

    String tryAcquireCouponFromCache(Integer templateId);

    Integer addCouponsToCache(Long userId, List<Coupon> couponList, Integer status) throws CouponException;
}
