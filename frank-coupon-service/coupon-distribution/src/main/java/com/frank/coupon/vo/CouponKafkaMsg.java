package com.frank.coupon.vo;

import java.util.List;

/**
 * 优惠券kafka消息对象
 */
public class CouponKafkaMsg {
    private Integer status;

    // Coupon id list
    private List<Integer> couponIdList;

    public CouponKafkaMsg(Integer status, List<Integer> couponIdList) {
        this.status = status;
        this.couponIdList = couponIdList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getCouponIdList() {
        return couponIdList;
    }

    public void setCouponIdList(List<Integer> couponIdList) {
        this.couponIdList = couponIdList;
    }
}
