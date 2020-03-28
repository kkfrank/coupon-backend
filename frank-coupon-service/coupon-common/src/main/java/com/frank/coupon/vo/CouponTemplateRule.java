package com.frank.coupon.vo;

import com.frank.coupon.constant.PeriodType;
import org.apache.commons.lang3.StringUtils;

/**
 *  优惠券规则定义
 */
public class CouponTemplateRule {
    private Expiration expiration;
    private Discount discount;

    //每个人最多领几张
    private Integer limit;

    private Usage usage;

    //可以和哪些优惠券叠加使用，同一类的优惠券在这不允许叠加  List[]
    private String weight;

    public boolean validate(){
        return expiration.validate() && discount.validate() && usage.validate() && limit >0 && StringUtils.isNotBlank(weight);
    }
    public static class Expiration{
        // PeriodType中的code
        private Integer period;

        // 有效期间隔 只对SHIFT类型有效
        private Integer gap;

        //优惠券模板失效日期，两类规则都起作用
        private Long deadline;

        public Integer getPeriod() {
            return period;
        }

        public void setPeriod(Integer period) {
            this.period = period;
        }

        public Integer getGap() {
            return gap;
        }

        public void setGap(Integer gap) {
            this.gap = gap;
        }

        public Long getDeadline() {
            return deadline;
        }

        public void setDeadline(Long deadline) {
            this.deadline = deadline;
        }

        boolean validate(){
            //最简单校验
            if(period == null || gap == null || deadline == null){
                return false;
            }
            return PeriodType.of(period) != null && gap >0 && deadline >0;
        }
    }

    /**
     * 由优惠券的类型决定
     */
    public static class Discount{
        // 额度 满减(20) 折扣(85) 立减(10)
        private Integer quota;

        // 基准 需要满多少才可用
        private Integer base;

        public Integer getQuota() {
            return quota;
        }

        public void setQuota(Integer quota) {
            this.quota = quota;
        }

        public Integer getBase() {
            return base;
        }

        public void setBase(Integer base) {
            this.base = base;
        }

        boolean validate(){
            return quota > 0 && base > 0;
        }
    }


    public static class Usage{
        private String province;
        private String city;

        /*商品类型  文娱 生鲜 家居 全品类*/
        private String goodsType;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        boolean validate(){
            return StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city) && StringUtils.isNotBlank(goodsType);
        }
    }


    public Expiration getExpiration() {
        return expiration;
    }

    public void setExpiration(Expiration expiration) {
        this.expiration = expiration;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
