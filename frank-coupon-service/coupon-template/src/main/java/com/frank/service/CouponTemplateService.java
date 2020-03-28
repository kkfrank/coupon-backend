package com.frank.service;

import com.frank.coupon.exception.CouponException;
import com.frank.entity.CouponTemplate;
import com.frank.vo.CouponTemplateRequest;
import com.frank.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;

public interface CouponTemplateService {
    CouponTemplate create(CouponTemplateRequest request) throws CouponException;

    CouponTemplate findById(Integer id) throws CouponException;

    List<CouponTemplateSDK> findAllUsable();

    List<CouponTemplateSDK> findByIds(Collection<Integer> ids);


    //根据用户Id查找当前可以领取的优惠券模板
    List<CouponTemplateSDK> findAvailableByUserId(Long userId);
}
