package com.frank.vo;

import com.frank.coupon.vo.CouponTemplateRule;

/**
 * 微服务之间用的模块信息定义
 */
public class CouponTemplateSDK {
    private Integer id;
    private String name;
    private String logo;
    private String desc;
    private String category;
    private Integer productLine;
    private String key;
    private Integer target;
    private CouponTemplateRule rule;

//    private Integer count;
//    private Long createBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
