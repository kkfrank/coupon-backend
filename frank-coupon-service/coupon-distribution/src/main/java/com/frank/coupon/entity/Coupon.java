package com.frank.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.frank.coupon.constant.CouponStatus;
import com.frank.coupon.converter.CouponStatusConverter;
import com.frank.coupon.serializer.CouponSerializer;
import com.frank.vo.CouponTemplateSDK;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonSerialize(using= CouponSerializer.class)
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    //逻辑外键 不是实体外键
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @CreatedDate//jpa审计功能 EntityListeners
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    @Transient//标志此列不在表中  @Basic是默认，说明在表中
    private CouponTemplateSDK couponTemplateSDK;

    public Coupon() {
    }


    public Coupon(Integer templateId, Long userId, String couponCode, CouponStatus status) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }

    public static Coupon invalidCoupon(){
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Date getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public CouponTemplateSDK getCouponTemplateSDK() {
        return couponTemplateSDK;
    }

    public void setCouponTemplateSDK(CouponTemplateSDK couponTemplateSDK) {
        this.couponTemplateSDK = couponTemplateSDK;
    }
}
