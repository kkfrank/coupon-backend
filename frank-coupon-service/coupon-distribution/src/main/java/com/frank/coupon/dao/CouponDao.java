package com.frank.coupon.dao;

import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * just like where userId = xxx and status = xxx
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
