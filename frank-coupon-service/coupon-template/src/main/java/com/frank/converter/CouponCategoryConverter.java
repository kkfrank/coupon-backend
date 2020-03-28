package com.frank.converter;

import com.frank.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * AttributeConverter<X,Y>
 * X: 实体属性类型 Y:数据库字段类型
 */
@Convert
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {

    //插入和更新时使用
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }


    //查询时使用
    @Override
    public CouponCategory convertToEntityAttribute(String dbData) {
        return CouponCategory.of(dbData);
    }
}
