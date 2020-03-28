package com.frank.coupon.vo;

import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.constant.PeriodType;
import com.frank.coupon.entity.Coupon;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券分类
 */
public class CouponClassify {
    private List<Coupon> usableList;
    private List<Coupon> usedList;
    private List<Coupon> expiredList;

    public List<Coupon> getUsableList() {
        return usableList;
    }

    public void setUsableList(List<Coupon> usableList) {
        this.usableList = usableList;
    }

    public List<Coupon> getUsedList() {
        return usedList;
    }

    public void setUsedList(List<Coupon> usedList) {
        this.usedList = usedList;
    }

    public List<Coupon> getExpiredList() {
        return expiredList;
    }

    public void setExpiredList(List<Coupon> expiredList) {
        this.expiredList = expiredList;
    }

    public static CouponClassify classify(List<Coupon> couponList){
        CouponClassify couponClassify = new CouponClassify();

        List<Coupon> usableList = new ArrayList<>();
        List<Coupon> usedList = new ArrayList<>();
        List<Coupon> expiredList = new ArrayList<>();
        couponList.forEach(item->{
            //只有查询coupon的时候才会判断是否过期，存在延迟
            //判断是否过期
            boolean isExpired;
            long curTime = new Date().getTime();

            Integer periodType = item.getCouponTemplateSDK().getRule().getExpiration().getPeriod();
            if(PeriodType.REGULAR.getCode().equals(periodType)){
                isExpired = item.getCouponTemplateSDK().getRule().getExpiration().getDeadline() <= curTime;
            }else {
                isExpired = DateUtils.addDays(item.getAssignTime(),
                            item.getCouponTemplateSDK().getRule().getExpiration().getGap()).getTime() <= curTime;
            }
            if(item.getStatus() == CouponStatus.USABLE){
                usableList.add(item);
            }else if(item.getStatus() == CouponStatus.EXPIRED || isExpired){
                expiredList.add(item);
            }else{
                usedList.add(item);
            }
        });
        couponClassify.setUsableList(usableList);
        couponClassify.setUsedList(usedList);
        couponClassify.setExpiredList(expiredList);
        return couponClassify;
    }
}
