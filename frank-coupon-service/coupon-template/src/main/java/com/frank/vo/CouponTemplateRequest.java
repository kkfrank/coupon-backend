package com.frank.vo;

import com.frank.coupon.constant.CouponCategory;
import com.frank.coupon.constant.DistributeTarget;
import com.frank.coupon.constant.ProductLine;
import com.frank.coupon.vo.CouponTemplateRule;
import org.apache.commons.lang3.StringUtils;

public class CouponTemplateRequest {
    private String name;
    private String logo;
    private String desc;
    private String category;
    private Integer productLine;
    private Integer count;
    private Long createBy;
    private Integer target;
    private CouponTemplateRule rule;


    public boolean validate(){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(logo) || StringUtils.isEmpty(desc)){
            return false;
        }
        if(CouponCategory.of(category) == null || ProductLine.of(productLine) == null || DistributeTarget.of(target) == null){
            return false;
        }
        if(count <=0 || createBy <=0 ){
            return false;
        }
        if(!rule.validate()){
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getProductLine() {
        return productLine;
    }

    public void setProductLine(Integer productLine) {
        this.productLine = productLine;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public CouponTemplateRule getRule() {
        return rule;
    }

    public void setRule(CouponTemplateRule rule) {
        this.rule = rule;
    }
}
