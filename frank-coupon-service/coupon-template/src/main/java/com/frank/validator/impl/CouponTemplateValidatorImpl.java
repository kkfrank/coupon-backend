package com.frank.validator.impl;

import com.frank.entity.CouponTemplate;
import com.frank.validator.CouponTemplateValidator;
import org.springframework.stereotype.Component;


@Component
public class CouponTemplateValidatorImpl implements CouponTemplateValidator {

    @Override
    public CouponTemplate validateCreate(CouponTemplate template) {
        return null;
    }
}
