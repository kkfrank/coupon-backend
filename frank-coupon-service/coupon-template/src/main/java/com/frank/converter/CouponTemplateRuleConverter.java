package com.frank.converter;

import com.alibaba.fastjson.JSON;
import com.frank.coupon.vo.CouponTemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class CouponTemplateRuleConverter implements AttributeConverter<CouponTemplateRule, String> {

    @Override
    public String convertToDatabaseColumn(CouponTemplateRule couponTemplateRule) {
        return JSON.toJSONString(couponTemplateRule);
    }

    @Override
    public CouponTemplateRule convertToEntityAttribute(String dbData) {
        return JSON.parseObject(dbData, CouponTemplateRule.class);
    }
}
