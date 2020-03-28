package com.frank.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.frank.converter.CouponCategoryConverter;
import com.frank.converter.CouponTemplateRuleConverter;
import com.frank.converter.DistributeTargetConverter;
import com.frank.converter.ProductLineConverter;
import com.frank.coupon.constant.CouponCategory;
import com.frank.coupon.constant.DistributeTarget;
import com.frank.coupon.constant.ProductLine;
import com.frank.coupon.vo.CouponTemplateRule;
import com.frank.serializer.CouponTemplateSerializer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基础属性 + 规则属性
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon_template")
@JsonSerialize(using = CouponTemplateSerializer.class)
public class CouponTemplate implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    //@Transient不映射到表中    @Basic映射到表中(默认)
    private Integer id;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo", nullable = false)
    private String logo;

    @Column(name = "description", nullable = false)
    private String desc;

    @Column(name = "category", nullable = false)
    @Convert(converter = CouponCategoryConverter.class)
    private CouponCategory category;

    @Column(name = "product_line", nullable = false)
    @Convert(converter = ProductLineConverter.class)
    private ProductLine productLine;

    //count也是数据表中的关键字
    @Column(name = "count", nullable = false)
    private Integer count;

    @CreatedDate // @UpdateTimestamp  @CreatedBy
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "create_by", nullable = false)
    private Long createBy;

    //优惠券模板的编码
    @Column(name = "template_key", nullable = false)
    private String key;

    @Column(name = "target", nullable = false)
    @Convert(converter = DistributeTargetConverter.class)
    private DistributeTarget target;

    //存储json字符串
    @Column(name="rule", nullable = false)
    @Convert(converter = CouponTemplateRuleConverter.class)
    private CouponTemplateRule rule;

    public CouponTemplate(String name, String logo, String desc, String category,
                          Integer productLine, Integer count, Long createBy, Integer target,
                          CouponTemplateRule rule
                          ){
        this.available = false;
        this.expired = false;
        //优惠券唯一编码 4位(产品线和类型) 8位（日期：20200222）+id(扩充4位随机)
        this.key = productLine.toString() + category + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        this.name = name;
        this.logo=logo;
        this.desc = desc;
        this.category = CouponCategory.of(category);
        this.productLine = ProductLine.of(productLine);
        this.count = count;
        this.createBy = createBy;
        this.target = DistributeTarget.of(target);
        this.rule = rule;
    }

    public CouponTemplate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
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

    public CouponCategory getCategory() {
        return category;
    }

    public void setCategory(CouponCategory category) {
        this.category = category;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DistributeTarget getTarget() {
        return target;
    }

    public void setTarget(DistributeTarget target) {
        this.target = target;
    }

    public CouponTemplateRule getRule() {
        return rule;
    }

    public void setRule(CouponTemplateRule rule) {
        this.rule = rule;
    }
}
