package com.frank.coupon.converter;

import com.frank.coupon.constant.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;


@Convert
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer dbData) {
        return CouponStatus.of(dbData);
    }
}
