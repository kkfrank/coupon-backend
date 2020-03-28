package com.frank.coupon.vo;

import java.util.List;

/**
 * 结算信息
 * userId
 * list<goods>
*  list<coupon>
 *  final    price
 */
public class SettlementInfo {
    private Long userId;

    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    private List<GoodsInfo> goodsInfos;

    //结算， 不是核销
    private Boolean computed;

    private Double price;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getComputed() {
        return computed;
    }

    public void setComputed(Boolean computed) {
        this.computed = computed;
    }

    public List<CouponAndTemplateInfo> getCouponAndTemplateInfos() {
        return couponAndTemplateInfos;
    }

    public void setCouponAndTemplateInfos(List<CouponAndTemplateInfo> couponAndTemplateInfos) {
        this.couponAndTemplateInfos = couponAndTemplateInfos;
    }

    public List<GoodsInfo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodsInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static class CouponAndTemplateInfo{
        private Integer couponId;
        private Integer templateId;

        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
        }

        public Integer getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Integer templateId) {
            this.templateId = templateId;
        }
    }
}
