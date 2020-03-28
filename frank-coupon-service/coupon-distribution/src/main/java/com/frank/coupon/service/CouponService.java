package com.frank.coupon.service;

import com.frank.coupon.entity.Coupon;
import com.frank.coupon.exception.CouponException;
import com.frank.coupon.vo.AcquireTemplateRequest;
import com.frank.coupon.vo.SettlementInfo;

import java.util.List;

public interface CouponService {
    List<Coupon> findByUserIdStatus(Long userId, Integer status) throws CouponException;

    Coupon acquireTemplate(AcquireTemplateRequest request);

    //结算（核销）优惠券
    SettlementInfo settlement(SettlementInfo info);
}
